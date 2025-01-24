package loqor.ait.core.tardis.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import dev.pavatus.lib.data.CachedDirectedGlobalPos;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import loqor.ait.AITMod;
import loqor.ait.api.TardisComponent;
import loqor.ait.core.tardis.ServerTardis;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.handler.LoyaltyHandler;
import loqor.ait.core.tardis.handler.StatsHandler;
import loqor.ait.core.world.TardisServerWorld;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.desktop.TardisDesktopSchema;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;

public class TardisBuilder {

    private final UUID uuid;
    private CachedDirectedGlobalPos pos;
    private TardisDesktopSchema desktop;
    private ExteriorVariantSchema exterior;

    private final List<Consumer<ServerTardis>> postInit = new ArrayList<>();

    public TardisBuilder(UUID uuid) {
        this.uuid = uuid;
    }

    public TardisBuilder() {
        this(UUID.randomUUID());
    }

    public TardisBuilder at(CachedDirectedGlobalPos pos) {
        this.pos = pos;
        return this;
    }

    public TardisBuilder desktop(TardisDesktopSchema desktop) {
        this.desktop = desktop;
        return this;
    }

    public TardisBuilder exterior(ExteriorVariantSchema exterior) {
        this.exterior = exterior;
        return this;
    }

    public <T extends TardisComponent> TardisBuilder with(TardisComponent.Id id, Consumer<T> consumer) {
        this.postInit.add(tardis -> {
            T t = tardis.handler(id);
            consumer.accept(t);
        });

        return this;
    }

    public TardisBuilder owner(ServerPlayerEntity player) {
        return this.<StatsHandler>with(TardisComponent.Id.STATS, stats -> {
            stats.setPlayerCreatorName(player.getName().getString());
            stats.markPlayerCreatorName();
        }).<LoyaltyHandler>with(TardisComponent.Id.LOYALTY,
                loyalty -> loyalty.set(player, new Loyalty(Loyalty.Type.COMPANION)));
    }

    private void validate() {
        if (this.pos == null)
            throw new IllegalStateException("Tried to create a TARDIS at null position!");

        if (this.desktop == null) {
            AITMod.LOGGER.warn("No desktop schema supplied for TardisBuilder, choosing a random one!");
            this.desktop = DesktopRegistry.getInstance().getRandom();
        }

        if (this.exterior == null) {
            AITMod.LOGGER.warn("No exterior variant schema supplied for TardisBuilder, choosing a random one!");
            this.exterior = ExteriorVariantRegistry.getInstance().getRandom();
        }
    }

    public ServerTardis build() {
        long start = System.currentTimeMillis();
        this.validate();

        ServerTardis tardis = new ServerTardis(this.uuid, this.desktop, this.exterior);

        long worldStart = System.currentTimeMillis();
        ServerWorld world = TardisServerWorld.create(tardis);
        AITMod.LOGGER.info("Created world {} in {}ms", world, System.currentTimeMillis() - worldStart);

        Tardis.init(tardis, TardisComponent.InitContext.createdAt(this.pos));

        for (Consumer<ServerTardis> consumer : this.postInit) {
            consumer.accept(tardis);
        }

        AITMod.LOGGER.info("Built {} in {}ms", tardis, System.currentTimeMillis() - start);
        return tardis;
    }
}
