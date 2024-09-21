package loqor.ait.core.tardis;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.gson.InstanceCreator;

import net.minecraft.server.MinecraftServer;

import loqor.ait.api.TardisComponent;
import loqor.ait.data.Exclude;
import loqor.ait.data.enummap.EnumMap;
import loqor.ait.data.schema.desktop.TardisDesktopSchema;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.TardisComponentRegistry;

public class ServerTardis extends Tardis {

    @Exclude(strategy = Exclude.Strategy.NETWORK)
    protected int version = 2;

    @Exclude
    private boolean removed;

    @Exclude
    private final EnumMap<TardisComponent.IdLike, TardisComponent> delta = new EnumMap<>(TardisComponentRegistry::values,
            TardisComponent[]::new);

    // since enummap doesn't track this sort of info
    @Exclude
    private short deltaSize = 0;

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
        if (component == null)
            return;

        if (component.tardis() != this)
            return;

        if (this.delta.put(component.getId(), component) == null)
            this.deltaSize++;
    }

    public void consumeDelta(Consumer<TardisComponent> consumer) {
        if (!this.hasDelta())
            return;

        for (TardisComponent component : this.delta.getValues()) {
            if (component == null)
                continue;

            consumer.accept(component);
        }

        this.delta.clear();
        this.deltaSize = 0;
    }

    public short getDeltaSize() {
        return deltaSize;
    }

    public boolean hasDelta() {
        return this.deltaSize != 0;
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
