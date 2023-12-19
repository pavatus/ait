package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.tardis.TardisTickable;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisDesktopSchema;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class ServerTardisDesktop extends TardisDesktop implements TardisTickable {
    private boolean dirty = false;

    public ServerTardisDesktop(Tardis tardis, TardisDesktopSchema schema) {
        super(tardis, schema);
    }

    @Override
    public void setInteriorDoorPos(AbsoluteBlockPos.Directed pos) {
        super.setInteriorDoorPos(pos);

        markDirty();
    }

    @Override
    public void setConsolePos(AbsoluteBlockPos.Directed pos) {
        super.setConsolePos(pos);

        markDirty();
    }

    private void sync() {
        ServerTardisManager.getInstance().sendToSubscribers(this.tardis);
    }

    public boolean isDirty() {
        return dirty;
    }
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void startTick(MinecraftServer server) {
        if (isDirty()) this.sync();
    }

    @Override
    public void tick(MinecraftServer server) {}

    @Override
    public void tick(ServerWorld world) {}

    @Override
    public void tick(MinecraftClient client) {}
}
