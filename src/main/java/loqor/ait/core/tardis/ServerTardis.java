package loqor.ait.core.tardis;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import com.google.gson.InstanceCreator;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import loqor.ait.api.TardisComponent;
import loqor.ait.core.tardis.dim.TardisDimension;
import loqor.ait.data.Exclude;
import loqor.ait.data.schema.desktop.TardisDesktopSchema;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;

public class ServerTardis extends Tardis {

    @Exclude(strategy = Exclude.Strategy.NETWORK)
    protected int version = 2;

    @Exclude
    private boolean removed;

    @Exclude
    private final Set<TardisComponent> delta = new HashSet<>(32);

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

        this.delta.add(component);
    }

    public void consumeDelta(Consumer<TardisComponent> consumer) {
        if (this.delta.isEmpty())
            return;

        for (TardisComponent component : this.delta) {
            consumer.accept(component);
        }

        this.delta.clear();
    }

    public boolean hasDelta() {
        return !this.delta.isEmpty();
    }

    public int getDeltaSize() {
        return this.delta.size();
    }

    @Override
    public ServerWorld getInteriorWorld() {
        return TardisDimension.getOrCreate(this);
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
