package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.tardis.TardisTickable;
import mdteam.ait.tardis.control.ControlTypes;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisConsole;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class ServerTardisConsole extends TardisConsole implements TardisTickable {
    private boolean dirty = false;

    public ServerTardisConsole(Tardis tardis, ConsoleEnum console, ControlTypes[] controlTypes) {
        super(tardis, console, controlTypes);
    }

    @Override
    public void setControlTypes(ControlTypes[] controlTypes) {
        super.setControlTypes(controlTypes);

        markDirty();
    }

    @Override
    public void setType(ConsoleEnum console) {
        super.setType(console);

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
