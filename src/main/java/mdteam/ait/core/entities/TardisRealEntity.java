package mdteam.ait.core.entities;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
        PropertiesHandler.set(exterior_block_entity.getTardis(), PropertiesHandler.IS_IN_REAL_FLIGHT, true);
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
        PropertiesHandler.set(exterior_block_entity.getTardis(), PropertiesHandler.IS_IN_REAL_FLIGHT, true);
        // set dirty for the tardis after this, but not right now cuz I am testing @TODO
        world.spawnEntity(tardis_real_entity);
        tardis_real_entity.setRotation(45f, block_state.get(ExteriorBlock.FACING).asRotation());
        System.out.println(tardis_real_entity);
        return tardis_real_entity;
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
