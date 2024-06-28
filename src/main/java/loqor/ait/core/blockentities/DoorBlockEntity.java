package loqor.ait.core.blockentities;

import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.blocks.types.HorizontalDirectionalBlock;
import loqor.ait.core.data.DirectedBlockPos;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.item.KeyItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.link.v2.interior.InteriorLinkableBlockEntity;
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

public class DoorBlockEntity extends InteriorLinkableBlockEntity {

	public DoorBlockEntity(BlockPos pos, BlockState state) {
		super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);
	}

	public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState blockState, T tDoor) {
		DoorBlockEntity door = (DoorBlockEntity) tDoor;

		if (door.tardis().isEmpty())
			return;

		Tardis tardis = door.tardis().get();
		DirectedGlobalPos.Cached globalExteriorPos = tardis.travel2().position();

		if (world.isClient())
			return;

		BlockPos exteriorPos = globalExteriorPos.getPos();
		World exteriorWorld = globalExteriorPos.getWorld();

		if (exteriorWorld == null || exteriorPos == null)
			return;

		BlockState exteriorBlockState = exteriorWorld.getBlockState(exteriorPos);

		if (exteriorBlockState.getBlock() instanceof ExteriorBlock && !tardis.areShieldsActive()) {
			world.setBlockState(pos, blockState.with(Properties.WATERLOGGED,
					exteriorBlockState.get(Properties.WATERLOGGED) && tardis.door().isOpen()
			), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);

			world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
			world.scheduleFluidTick(pos, blockState.getFluidState().getFluid(), blockState.getFluidState().getFluid().getTickRate(world));
		}
	}

	public void useOn(World world, boolean sneaking, PlayerEntity player) {
		if (player == null || this.tardis().isEmpty())
			return;

		Tardis tardis = this.tardis().get();

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

		if (tardis.travel2().getState() == TravelHandlerBase.State.LANDED)
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

		if (this.tardis().isEmpty())
			return;

		Tardis tardis = this.tardis().get();

		if (tardis.door().isClosed())
			return;

		if (tardis.getLockedTardis())
			return;

		if (PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.IS_FALLING))
			return;

		if (DependencyChecker.hasPortals() && tardis.getExterior().getVariant().hasPortals())
			return;

		TravelHandler travel = tardis.travel2();

		if (travel.getState() == TravelHandlerBase.State.FLIGHT) {
			TardisUtil.dropOutside(tardis, entity); // SHOULD properly drop someone out at the correct position instead of the not correct position :)
			return;
		}

		if (travel.getState() != TravelHandlerBase.State.LANDED)
			return;

		TardisUtil.teleportOutside(tardis, entity);
	}

	@Override
	public void onLinked() {
		if (this.tardis().isEmpty())
			return;

		this.tardis().get().getDesktop().setInteriorDoorPos(DirectedBlockPos.create(
				this.pos, (byte) RotationPropertyHelper.fromDirection(this.getFacing()))
		);
	}
}