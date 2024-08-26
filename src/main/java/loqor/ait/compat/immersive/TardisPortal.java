package loqor.ait.compat.immersive;

import java.util.UUID;

import qouteall.imm_ptl.core.portal.Portal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.manager.ServerTardisManager;

public class TardisPortal extends Portal {
    private UUID tardisId;

    public TardisPortal(World world, UUID tardis) {
        super(entityType, world);
        this.setTardis(tardis);
    }

    public TardisPortal(World world, Tardis tardis) {
        this(world, tardis.getUuid());
    }

    private void setTardis(UUID id) {
        this.tardisId = id;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient())
            return;

        if (this.tardisId == null) {
            this.discard();
            return;
        }

        ServerTardisManager.getInstance().getTardis(this.getWorld().getServer(), this.tardisId, tardis -> {
            if (tardis == null) {
                AITMod.LOGGER.info(
                        "Killing portal (" + this.getId() + ") with tardis (" + this.tardisId + ") as found was null");
                this.discard();
                return;
            }

            if (!tardis.door().isClosed())
                return;

            // we know we are closed and have a tardis so we shouldn't be existing AHHH
            AITMod.LOGGER.info(
                    "Killing portal (" + this.getId() + ") with tardis (" + this.tardisId + ") as doors are closed");
            this.discard();
        });
    }

    @Override
    public Iterable<Entity> getPassengersDeep() {
        for (Entity entity : super.getPassengersDeep()) {
            entity.setBoundingBox(entity.getBoundingBox().shrink(0, -0.75f, 0));
        }
        return super.getPassengersDeep();
    }

    @Override
    public boolean isInteractableBy(PlayerEntity player) {
        if (player.distanceTo(this) > 2) {
            return false;
        }
        return super.isInteractableBy(player);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (tardisId != null) {
            nbt.putUuid("tardis_uuid", tardisId);
        }
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("tardis_uuid")) {
            tardisId = nbt.getUuid("tardis_uuid");
        }
        super.readCustomDataFromNbt(nbt);
    }
}
