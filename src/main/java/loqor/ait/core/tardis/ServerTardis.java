package loqor.ait.core.tardis;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.google.gson.InstanceCreator;

import net.minecraft.server.MinecraftServer;

import loqor.ait.api.TardisComponent;
import loqor.ait.data.bsp.Exclude;
import loqor.ait.data.schema.desktop.TardisDesktopSchema;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;

public class ServerTardis extends Tardis {

    @Exclude(strategy = Exclude.Strategy.NETWORK)
    protected int version = 2;

    @Exclude
    private boolean removed;

    @Exclude
    private final Set<TardisComponent> delta = new HashSet<>();

    public ServerTardis(UUID uuid, TardisDesktopSchema schema, ExteriorVariantSchema variantType) {
        super(uuid, new TardisDesktop(schema), new TardisExterior(variantType));
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
