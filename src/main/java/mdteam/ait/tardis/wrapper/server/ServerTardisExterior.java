package mdteam.ait.tardis.wrapper.server;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.VariantEnum;
import mdteam.ait.tardis.TardisTickable;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisExterior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

public class ServerTardisExterior extends TardisExterior implements TardisTickable {
    private boolean dirty = false;

    public ServerTardisExterior(Tardis tardis, ExteriorEnum exterior, VariantEnum variant) {
        super(tardis, exterior, variant);
    }

    @Override
    public void setType(ExteriorEnum exterior) {
        super.setType(exterior);

        markDirty();
    }

    @Override
    public void setVariant(VariantEnum variant) {
        super.setVariant(variant);

        markDirty();
    }

    private void sync() {
        dirty = false;
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
