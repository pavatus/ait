package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.tardis.TardisTickable;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisDesktopSchema;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class ServerTardisDesktop extends TardisDesktop implements TardisTickable {

    public ServerTardisDesktop(Tardis tardis, TardisDesktopSchema schema) {
        super(tardis, schema);
    }

    @Override
    public void setInteriorDoorPos(AbsoluteBlockPos.Directed pos) {
        super.setInteriorDoorPos(pos);
        if (this.getTardis().isEmpty()) return;
        this.sync();
    }

    @Override
    public void setConsolePos(AbsoluteBlockPos.Directed pos) {
        super.setConsolePos(pos);

        this.sync();
    }
}
