package mdteam.ait.core.entities;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.core.item.SiegeTardisItem;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class TardisRealEntity extends Entity {

    public static final TrackedData<Optional<UUID>> TARDIS_ID;
    private Supplier<BlockState> blockStateSupplier;
    public TardisRealEntity(EntityType<?> type, World world) {
        super(type, world);
        this.blockStateSupplier = AITBlocks.EXTERIOR_BLOCK::getDefaultState;
    }

    private TardisRealEntity(World world, UUID tardisID, double x, double y, double z, BlockState blockState) {
        this(AITEntityTypes.TARDIS_REAL_ENTITY_TYPE, world);
        this.blockStateSupplier = () -> blockState;
        this.dataTracker.set(TARDIS_ID, Optional.of(tardisID));
        this.setPosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
    }

    public static TardisRealEntity spawnFromExteriorBlockEntity(World world, BlockPos pos) {
        BlockEntity block_entity = world.getBlockEntity(pos);
        if (!(block_entity instanceof ExteriorBlockEntity exterior_block_entity)) throw new IllegalStateException("Failed to find the exterior block entity!");
        BlockState block_state = world.getBlockState(pos);
        if (!(block_state.getBlock() instanceof ExteriorBlock)) throw new IllegalStateException("Failed to find the exterior block!");
        TardisRealEntity tardis_real_entity = new TardisRealEntity(world, exterior_block_entity.getTardis().getUuid(), (double)pos.getX() + 0.5, (double)pos.getY(), pos.getZ() + 0.5, block_state);
        PropertiesHandler.setBool(exterior_block_entity.getTardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT, true);
        // set dirty for the tardis after this, but not right now cuz I am testing @TODO
        world.spawnEntity(tardis_real_entity);
        return tardis_real_entity;
    }

    public static TardisRealEntity testSpawnFromExteriorBlockEntity(World world, BlockPos pos, BlockPos spawnPos) {
        BlockEntity block_entity = world.getBlockEntity(pos);
        if (!(block_entity instanceof ExteriorBlockEntity exterior_block_entity)) throw new IllegalStateException("Failed to find the exterior block entity!");
        BlockState block_state = world.getBlockState(pos);
        if (!(block_state.getBlock() instanceof ExteriorBlock)) throw new IllegalStateException("Failed to find the exterior block!");
        TardisRealEntity tardis_real_entity = new TardisRealEntity(world, exterior_block_entity.getTardis().getUuid(), (double)spawnPos.getX() + 0.5, (double)spawnPos.getY(), spawnPos.getZ() + 0.5, block_state);
        PropertiesHandler.setBool(exterior_block_entity.getTardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT, true);
        // set dirty for the tardis after this, but not right now cuz I am testing @TODO
        world.spawnEntity(tardis_real_entity);
        tardis_real_entity.setRotation(block_state.get(ExteriorBlock.FACING).asRotation(), 0);
        System.out.println(tardis_real_entity);
        return tardis_real_entity;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player == null)
            return ActionResult.FAIL;

        boolean sneaking = player.isSneaking();

        if (this.getTardis().isGrowth())
            return ActionResult.FAIL;

        if (player.getMainHandStack().getItem() instanceof KeyItem && !getTardis().isSiegeMode() && !getTardis().getHandlers().getInteriorChanger().isGenerating()) {
            ItemStack key = player.getMainHandStack();
            NbtCompound tag = key.getOrCreateNbt();
            if (!tag.contains("tardis")) {
                return ActionResult.FAIL;
            }
            if (Objects.equals(this.getTardis().getUuid().toString(), tag.getString("tardis"))) {
                DoorHandler.toggleLock(this.getTardis(), (ServerPlayerEntity) player);
            } else {
                this.getWorld().playSound(null, this.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.literal("TARDIS does not identify with key"), true);
            }
            return ActionResult.SUCCESS;
        }

        if (sneaking && getTardis().isSiegeMode() && !getTardis().isSiegeBeingHeld()) {
            SiegeTardisItem.pickupTardis(getTardis(), (ServerPlayerEntity) player);
            return ActionResult.SUCCESS;
        }

        DoorHandler.useDoor(this.getTardis(), (ServerWorld) this.getWorld(), this.getBlockPos(), (ServerPlayerEntity) player);
        // fixme maybe this is required idk the doorhandler already marks the tardis dirty || tardis().markDirty();
        if (sneaking)
            return ActionResult.FAIL;
        return ActionResult.SUCCESS;
    }

    public static TardisRealEntity spawnFromTardisId(World world, UUID tardisId, BlockPos spawnPos) {
        Tardis tardis = ServerTardisManager.getInstance().getTardis(tardisId);
        if(tardis.getExterior().getExteriorPos() == null) return null;
        BlockState blockState = world.getBlockState(tardis.getDesktop().getExteriorPos());
        TardisRealEntity tardisRealEntity = new TardisRealEntity(world, tardis.getUuid(), (double)spawnPos.getX() + 0.5, (double)spawnPos.getY(), spawnPos.getZ() + 0.5, blockState);
        PropertiesHandler.setBool(tardis.getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT, true);
        world.spawnEntity(tardisRealEntity);
        tardisRealEntity.setRotation(blockState.get(ExteriorBlock.FACING).asRotation(), 0);
        return tardisRealEntity;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getTardis() == null) return;
        if(this.getWorld().isClient()) return;
    }

    public float getRotation(float tickDelta) {
        return ((float)this.age + tickDelta) / 20.0f;
    }

    @Override
    public float getBodyYaw() {
        return 180.0f - this.getRotation(0.5f) / ((float)Math.PI * 2) * 360.0f;
    }

    static {
        TARDIS_ID = DataTracker.registerData(TardisRealEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    }

    public BlockState getBlockState() {
        return this.blockStateSupplier.get();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString("TardisID", this.getTardisID().toString());
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.dataTracker.set(TARDIS_ID, Optional.of(UUID.fromString(nbt.getString("TardisID"))));
    }

    public Tardis getTardis() {
        if (getTardisID() == null) {
            AITMod.LOGGER.warn("Tardis ID is null somehow?");
            return null;
        }

        if (TardisUtil.isClient()) {
            return ClientTardisManager.getInstance().getLookup().get(getTardisID());
        }
        return ServerTardisManager.getInstance().getTardis(getTardisID());
    }

    public UUID getTardisID() {
        return this.dataTracker.get(TARDIS_ID).orElse(null);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(TARDIS_ID, Optional.empty());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
