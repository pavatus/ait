package loqor.ait.core.blockentities;

import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.blocks.types.HorizontalDirectionalBlock;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.item.KeyItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.link.LinkableBlockEntity;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static loqor.ait.tardis.util.TardisUtil.findTardisByInterior;

public class DoorBlockEntity extends LinkableBlockEntity {

	public DoorBlockEntity(BlockPos pos, BlockState state) {
		super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);

		if (!this.hasWorld())
			return;

		if (this.findTardis().isEmpty())
			return;

		this.linkDesktop();
	}

	public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState blockState, T tDoor) {
		DoorBlockEntity door = (DoorBlockEntity) tDoor;

		if (door.tardisId == null)
			door.findTardis();

		if (door.findTardis().isEmpty())
			return;

		if (!world.isClient() && door.findTardis().get().getExteriorPos() == null)
			return;

		World exteriorWorld = door.findTardis().get().getExteriorPos().getWorld();

		if (exteriorWorld == null)
			return;

		BlockState exteriorBlockState = exteriorWorld.getBlockState(door.findTardis().get().getExteriorPos());

		if (exteriorBlockState.getBlock() instanceof ExteriorBlock && !door.findTardis().get().areShieldsActive()) {
			world.setBlockState(pos, blockState.with(Properties.WATERLOGGED, exteriorBlockState.get(Properties.WATERLOGGED) && door.findTardis().get().getDoor().isOpen()), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
			world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
			world.scheduleFluidTick(pos, blockState.getFluidState().getFluid(), blockState.getFluidState().getFluid().getTickRate(world));
		}
	}

	public void useOn(World world, boolean sneaking, PlayerEntity player) {
		if (player == null || this.findTardis().isEmpty())
			return;

		Tardis tardis = this.findTardis().get();

		if (tardis.isGrowth() && tardis.hasGrowthExterior())
			return;

		if (player.getMainHandStack().getItem() instanceof KeyItem && !tardis.siege().isActive()) {
			ItemStack key = player.getMainHandStack();
			NbtCompound tag = key.getOrCreateNbt();

			if (!tag.contains("tardis"))
				return;

			if (Objects.equals(tardis.getUuid().toString(), tag.getString("tardis"))) {
				DoorData.toggleLock(tardis, (ServerPlayerEntity) player);
			} else {
				world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
				player.sendMessage(Text.translatable("tardis.key.identity_error"), true); //TARDIS does not identify with key
			}

			return;
		}

		DoorData.useDoor(tardis, (ServerWorld) world, this.getPos(), (ServerPlayerEntity) player);
	}

	public Direction getFacing() {
		return this.getCachedState().get(HorizontalDirectionalBlock.FACING);
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	public void onEntityCollision(Entity entity) {
		if (this.getWorld() != TardisUtil.getTardisDimension())
			return;

		if (this.findTardis().isEmpty())
			return;

		Tardis tardis = this.findTardis().get();

		if (tardis.getDoor().isClosed())
			return;

		if (tardis.getLockedTardis())
			return;

		if (PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.IS_FALLING)) {
			return;
		}

		if (DependencyChecker.hasPortals() && tardis.getExterior().getVariant().hasPortals()) {
			return;
		}

		TardisTravel travel = tardis.travel();

		if (travel.getState() == TardisTravel.State.FLIGHT) {
			TardisUtil.dropOutside(tardis, entity); // SHOULD properly drop someone out at the correct position instead of the not correct position :)
			return;
		}

		if (travel.getState() != TardisTravel.State.LANDED)
			return;

		TardisUtil.teleportOutside(tardis, entity);
	}

	@Override
	public Optional<Tardis> findTardis() {
		if (this.tardisId == null) {
			Tardis found = findTardisByInterior(pos, !this.getWorld().isClient());
			if (found != null)
				this.setTardis(found);
		}

		return super.findTardis();
	}

	public void setTardis(UUID uuid) {
		super.setTardis(uuid);

		this.linkDesktop();
	}

	public void linkDesktop() {
		if (this.findTardis().isEmpty())
			return;

		this.setDesktop(this.getDesktop());
	}

	public TardisDesktop getDesktop() {
		if (this.findTardis().isEmpty())
			return null;

		return this.findTardis().get().getDesktop();
	}

	public void setDesktop(TardisDesktop desktop) {
		if (!this.hasWorld() || this.getWorld().isClient())
			return;

		desktop.setInteriorDoorPos(new AbsoluteBlockPos.Directed(
				this.pos, TardisUtil.getTardisDimension(), RotationPropertyHelper.fromDirection(this.getFacing()))
		);
	}
}