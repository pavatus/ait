package loqor.ait.tardis.wrapper.server;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.google.gson.InstanceCreator;

import net.minecraft.server.MinecraftServer;

import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.base.TardisComponent;

public class ServerTardis extends Tardis {

    @Exclude(strategy = Exclude.Strategy.NETWORK)
    protected int version = 2;

    @Exclude
    private boolean removed;

    @Exclude
    private final Set<TardisComponent> delta = new HashSet<>();

    public ServerTardis(UUID uuid, TardisDesktopSchema schema, ExteriorVariantSchema variantType) {
        super(uuid, new ServerTardisDesktop(schema), new TardisExterior(variantType));
    }

    private ServerTardis() {
        super();
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void tick(MinecraftServer server) {
        this.getHandlers().tick(server);
    }

    public void markDirty(TardisComponent component) {
        if (component.tardis() != this)
            return;

        this.delta.add(component);
    }

    public Set<TardisComponent> getDelta() {
        return delta;
    }

    public void clearDelta() {
        this.delta.clear();
    }

    public static Object creator() {
        return new ServerTardisCreator();
    }

    static class ServerTardisCreator implements InstanceCreator<ServerTardis> {

        @Override
        public ServerTardis createInstance(Type type) {
            return new ServerTardis();
        }
    }
}
