package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.minecraft.util.Identifier;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class DoorHandler {
    public static final Identifier SEND = new Identifier(AITMod.MOD_ID, "send_door_handler");
    private final UUID tardisId;
    private float rightRot, leftRot;
    private boolean locked;

    public DoorHandler(UUID tardis) {
        this.tardisId = tardis;
    }

    public static boolean isClient() {
        return FabricLauncherBase.getLauncher().getEnvironmentType() == EnvType.CLIENT;
    }
    public static boolean isServer() {
        return !isClient();
    }

    public Tardis tardis() {
        if (isClient()) {
            AtomicReference<Tardis> tardis = null;
            ClientTardisManager.getInstance().getTardis(this.tardisId, (tardis::set)); // this consumer thing is annoying??
            return tardis.get();
        }

        return ServerTardisManager.getInstance().getTardis(this.tardisId);
    }

    public void sync() {
        if (isClient()) return;

        if (this.tardis() == null) return;

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis());
    }

    public void setRightRot(float var) {
//        if (isClient()) {
//            AITMod.LOGGER.warn("Client tried to set right rot to " + var);
//            return;
//        }

        this.rightRot = var;

        if (isServer())
            this.sync();
    }
    public void setLeftRot(float var) {
//        if (isClient()) {
//            AITMod.LOGGER.warn("Client tried to set left rot to " + var);
//            return;
//        }

        this.leftRot = var;
        if (isServer())
            this.sync();
    }

    public void setLocked(boolean var) {
//        if (isClient()) {
//            AITMod.LOGGER.warn("Client tried to set locked to " + var);
//            return;
//        }

        this.locked = var;
        if (isServer())
            this.sync();
    }

    public float right() {
        return this.rightRot;
    }
    public float left() {
        return this.leftRot;
    }
    public boolean locked() {
        return this.locked;
    }
}
