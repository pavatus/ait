package mdteam.ait.mixin.server;

import mdteam.ait.AITMod;
import mdteam.ait.compat.immersive.ITardisPortal;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.portal.Portal;

import java.util.UUID;

@Mixin(Portal.class)
public abstract class PortalMixin implements ITardisPortal {

    @Unique
    private UUID tardisId;

    @Override
    public void setTardis(Tardis tardis) {
        this.tardisId = tardis.getUuid();
    }

    @Override
    public void setTardis(UUID id) {
        this.tardisId = id;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void ait_tick(CallbackInfo ci) {
        Portal portal = (Portal) (Object) this;

        if (portal.getWorld().isClient()) return;
        if (tardisId == null) return;

        Tardis found = ServerTardisManager.getInstance().getTardis(tardisId);
        if (found == null) return;

        if (!found.getDoor().isClosed()) return;
        // we know we are closed and have a tardis so we shouldnt be existing AHHH
        AITMod.LOGGER.info("Killing portal (" + portal.getId() + ") with tardis (" + tardisId + ")");
        portal.discard();
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void ait_writeNbt(NbtCompound nbt, CallbackInfo ci) {
        if (tardisId != null) {
            nbt.putUuid("tardis_uuid", tardisId);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void ait_readNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("tardis_uuid")) {
            tardisId = nbt.getUuid("tardis_uuid");
        }
    }
}
