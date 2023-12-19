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

    public ServerTardisConsole(Tardis tardis, ConsoleEnum console, ControlTypes[] controlTypes) {
        super(tardis, console, controlTypes);
    }

    @Override
    public void setControlTypes(ControlTypes[] controlTypes) {
        super.setControlTypes(controlTypes);
        this.tardis.markDirty();
    }

    @Override
    public void setType(ConsoleEnum console) {
        super.setType(console);
        this.tardis.markDirty();
    }

    @Override
    public void startTick(MinecraftServer server) {
    }

    @Override
    public void tick(MinecraftServer server) {}

    @Override
    public void tick(ServerWorld world) {}

    @Override
    public void tick(MinecraftClient client) {}
}
