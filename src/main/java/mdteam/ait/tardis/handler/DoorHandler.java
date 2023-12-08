package mdteam.ait.tardis.handler;

import mdteam.ait.tardis.ClientTardisManager;
import mdteam.ait.tardis.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.tab.Tab;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.UUID;

public class DoorHandler {
    private final UUID tardisId;
    private float left, right;
    private boolean locked;

    public DoorHandler(UUID tardis) {
        this.tardisId = tardis;
    }

    public void setLeftRot(float var) {
        this.left = var;

        sync();
    }
    public void setRightRot(float var) {
        this.right = var;

        sync();
    }
    public float right() {
        return this.right;
    }
    public float left() {
        return this.left;
    }

    public void setLocked(boolean var) {
        this.locked = var;
    }
    public void setLockedAndDoors(boolean var) {
        this.setLocked(var);

        this.setLeftRot(0);
        this.setRightRot(0);
    }
    public boolean locked() {
        return this.locked;
    }

    public void sync() {
        if (isClient()) return;

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis());
    }

    public Tardis tardis() {
        if (isClient()) {
            return ClientTardisManager.getInstance().getLookup().get(tardisId);
        }

        return ServerTardisManager.getInstance().getTardis(tardisId);
    }

    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
    public static boolean isServer() {
        return !isClient();
    }
}
