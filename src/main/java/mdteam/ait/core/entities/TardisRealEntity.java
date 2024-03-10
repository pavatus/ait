package mdteam.ait.core.entities;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.LinkableEntity;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.core.item.SiegeTardisItem;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.DoorData;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class TardisRealEntity extends LinkableEntity {

	public static final TrackedData<Optional<UUID>> PLAYER_UUID;

	public TardisRealEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	static {
		PLAYER_UUID = DataTracker.registerData(TardisRealEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
	}

	private TardisRealEntity(World world, UUID tardisID, double x, double y, double z, UUID playerUuid) {
		this(AITEntityTypes.TARDIS_REAL_ENTITY_TYPE, world);
		this.dataTracker.set(TARDIS_ID, Optional.of(tardisID));
		this.dataTracker.set(PLAYER_UUID, Optional.of(playerUuid));
		this.setPosition(x, y, z);
		this.setVelocity(Vec3d.ZERO);
	}

	/*public static TardisRealEntity spawnFromExteriorBlockEntity(World world, BlockPos pos) {
		BlockEntity block_entity = world.getBlockEntity(pos);
		if (!(block_entity instanceof ExteriorBlockEntity exterior_block_entity))
			throw new IllegalStateException("Failed to find the exterior block entity!");
		if (exterior_block_entity.findTardis().isEmpty()) return null;
		BlockState block_state = world.getBlockState(pos);
		if (!(block_state.getBlock() instanceof ExteriorBlock))
			throw new IllegalStateException("Failed to find the exterior block!");
		TardisRealEntity tardis_real_entity = new TardisRealEntity(world, exterior_block_entity.findTardis().get().getUuid(), (double) pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, block_state);
		PropertiesHandler.set(exterior_block_entity.findTardis().get(), PropertiesHandler.IS_IN_REAL_FLIGHT, true);
		world.spawnEntity(tardis_real_entity);
		tardis_real_entity.setRotation(block_state.get(ExteriorBlock.FACING).asRotation(), 0);
		return tardis_real_entity;
	}

	public static void testSpawnFromExteriorBlockEntity(World world, BlockPos pos, BlockPos spawnPos) {
		BlockEntity block_entity = world.getBlockEntity(pos);
		if (!(block_entity instanceof ExteriorBlockEntity exterior_block_entity))
			throw new IllegalStateException("Failed to find the exterior block entity!");
		if (exterior_block_entity.findTardis().isEmpty()) return;
		BlockState block_state = world.getBlockState(pos);
		if (!(block_state.getBlock() instanceof ExteriorBlock))
			throw new IllegalStateException("Failed to find the exterior block!");
		TardisRealEntity tardis_real_entity = new TardisRealEntity(world, exterior_block_entity.findTardis().get().getUuid(), (double) spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, block_state);
		PropertiesHandler.set(exterior_block_entity.findTardis().get(), PropertiesHandler.IS_IN_REAL_FLIGHT, true);
		world.spawnEntity(tardis_real_entity);
		tardis_real_entity.setRotation(block_state.get(ExteriorBlock.FACING).asRotation(), 0f);
	}*/

	public static void spawnFromTardisId(World world, UUID tardisId, BlockPos spawnPos, PlayerEntity player) {
		if(world.isClient()) return;
		Tardis tardis = ServerTardisManager.getInstance().getTardis(tardisId);
		TardisRealEntity tardisRealEntity = new TardisRealEntity(world, tardis.getUuid(), (double) spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, player.getUuid());
		PropertiesHandler.set(tardis, PropertiesHandler.IS_IN_REAL_FLIGHT, true, true);
		world.spawnEntity(tardisRealEntity);
		tardis.getTravel().toFlight();
		tardisRealEntity.setRotation(tardis.getExterior().getExteriorPos().getDirection().asRotation(), 0);
	}

	@Override
	public void tick() {
		super.tick();
		if(this.getPlayer().isEmpty()) return;
		PlayerEntity user = this.getPlayer().get();
		boolean bl =  PropertiesHandler.getBool(this.getTardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT);
		MinecraftClient client = MinecraftClient.getInstance();
		if (bl) {
			if (user.getWorld().isClient()) {
				client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
				client.options.hudHidden = true;
			} else {
				this.refreshPositionAndAngles(user.getX(),
						user.getY(),
						user.getZ(),
						user.getYaw(), 0);
				if (user.isSneaking() && user.isOnGround()) {
					getTardis().getTravel().setStateAndLand(new AbsoluteBlockPos.Directed(user.getBlockPos(), user.getWorld(), user.getHorizontalFacing()));
					if(getTardis().getTravel().getState() == TardisTravel.State.LANDED) PropertiesHandler.set(getTardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT, false);
				}
			}
		} else if (!getTardis().getTravel().inFlight()) {
			if(user.getWorld().isClient()) {
				client.options.setPerspective(Perspective.FIRST_PERSON);
				client.options.hudHidden = false;
			} else {
				user.clearStatusEffects();
				TardisUtil.teleportInside(getTardis(), user);
				this.dataTracker.set(PLAYER_UUID, Optional.empty());
				this.discard();
			}
		}
	}

	public float getRotation(float tickDelta) {
		return ((float) this.age + tickDelta) / 10.0f;
	}

	@Override
	public float getBodyYaw() {
		return 180.0f - this.getRotation(0.5f) / ((float) Math.PI * 2) * 360.0f;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if(this.getPlayer().isEmpty()) return nbt;
		nbt.putString("PlayerUuid", this.getPlayer().get().getUuid().toString());
		return nbt;
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(PLAYER_UUID, Optional.empty());
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if(!nbt.contains("PlayerUuid")) return;
		this.dataTracker.set(PLAYER_UUID, Optional.of(UUID.fromString(nbt.getString("PlayerUuid"))));
	}

	public Optional<PlayerEntity> getPlayer() {
		if(this.getWorld() == null || this.dataTracker.get(PLAYER_UUID).isEmpty()) return Optional.empty();
		return Optional.ofNullable(this.getWorld().getPlayerByUuid(this.dataTracker.get(PLAYER_UUID).get()));
	}
}
