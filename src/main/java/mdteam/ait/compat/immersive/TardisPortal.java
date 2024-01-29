package mdteam.ait.compat.immersive;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.portal.Portal;

import java.util.UUID;

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

        if (this.getWorld().isClient()) return;
        if (this.tardisId == null) return;

        Tardis found = ServerTardisManager.getInstance().getTardis(this.tardisId);
        if (found == null) return;

        if (!found.getDoor().isClosed()) return;
        // we know we are closed and have a tardis so we shouldnt be existing AHHH
        AITMod.LOGGER.info("Killing portal (" + this.getId() + ") with tardis (" + this.tardisId + ")");
        this.discard();
    }

    @Override
    public Iterable<Entity> getPassengersDeep() {
        for(Entity entity : super.getPassengersDeep()) {
            entity.setBoundingBox(entity.getBoundingBox().shrink(0, -0.75f, 0));
        }
        return super.getPassengersDeep();
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
