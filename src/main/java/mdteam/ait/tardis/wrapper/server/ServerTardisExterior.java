package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.tardis.TardisTickable;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisExterior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class ServerTardisExterior extends TardisExterior implements TardisTickable {

    public ServerTardisExterior(Tardis tardis, ExteriorSchema exterior, ExteriorVariantSchema variant) {
        super(tardis, exterior, variant);
    }

    @Override
    public void setType(ExteriorSchema exterior) {
        super.setType(exterior);
        this.getLinkedTardis().markDirty();
    }

    @Override
    public void setVariant(ExteriorVariantSchema variant) {
        super.setVariant(variant);

        this.getLinkedTardis().markDirty();
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
