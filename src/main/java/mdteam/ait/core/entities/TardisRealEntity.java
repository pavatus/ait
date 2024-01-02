package mdteam.ait.core.entities;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import mdteam.ait.tardis.wrapper.server.ServerTardis;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class TardisRealEntity extends Entity {

    public static final TrackedData<Optional<UUID>> TARDIS_ID;
    public TardisRealEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    private TardisRealEntity(World world, UUID tardisID, double x, double y, double z) {
        this(AITEntityTypes.TARDIS_REAL_ENTITY_TYPE, world);
        this.dataTracker.set(TARDIS_ID, Optional.of(tardisID));
    }

    public static TardisRealEntity spawnFromExteriorBlockEntity(World world, BlockPos pos) {
        BlockEntity block_entity = world.getBlockEntity(pos);
        if (!(block_entity instanceof ExteriorBlockEntity exterior_block_entity)) throw new IllegalStateException("Failed to find the exterior block entity!");

        TardisRealEntity tardis_real_entity = new TardisRealEntity(world, exterior_block_entity.getTardis().getUuid(), (double)pos.getX() + 0.5, (double)pos.getY(), pos.getZ() + 0.5);
        PropertiesHandler.setBool(exterior_block_entity.getTardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT, true);
        // set dirty for the tardis after this, but not right now cuz I am testing @TODO
        world.spawnEntity(tardis_real_entity);
        return tardis_real_entity;
    }

    public static TardisRealEntity testSpawnFromExteriorBlockEntity(World world, BlockPos pos, BlockPos spawnPos) {
        BlockEntity block_entity = world.getBlockEntity(pos);
        if (!(block_entity instanceof ExteriorBlockEntity exterior_block_entity)) throw new IllegalStateException("Failed to find the exterior block entity!");

        TardisRealEntity tardis_real_entity = new TardisRealEntity(world, exterior_block_entity.getTardis().getUuid(), (double)spawnPos.getX() + 0.5, (double)spawnPos.getY(), spawnPos.getZ() + 0.5);
        PropertiesHandler.setBool(exterior_block_entity.getTardis().getHandlers().getProperties(), PropertiesHandler.IS_IN_REAL_FLIGHT, true);
        // set dirty for the tardis after this, but not right now cuz I am testing @TODO
        world.spawnEntity(tardis_real_entity);
        return tardis_real_entity;
    }

    static {
        TARDIS_ID = DataTracker.registerData(TardisRealEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
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

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
