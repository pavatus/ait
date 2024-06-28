package loqor.ait.tardis.manager;

import loqor.ait.AITMod;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.StatsData;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.data.loyalty.LoyaltyHandler;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class TardisBuilder {

    private final UUID uuid;
    private DirectedGlobalPos.Cached pos;
    private TardisDesktopSchema desktop;
    private ExteriorVariantSchema exterior;

    private final List<Consumer<ServerTardis>> postInit = new ArrayList<>();

    public TardisBuilder(UUID uuid) {
        this.uuid = uuid;
    }

    public TardisBuilder() {
        this(UUID.randomUUID());
    }

    public TardisBuilder at(DirectedGlobalPos.Cached pos) {
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

    public TardisBuilder owner(PlayerEntity player) {
        return this.<StatsData>with(TardisComponent.Id.STATS, stats -> {
            stats.setPlayerCreatorName(player.getName().getString());
            stats.markPlayerCreatorName();
        }).<LoyaltyHandler>with(TardisComponent.Id.LOYALTY, loyalty ->
                loyalty.set(player, new Loyalty(Loyalty.Type.COMPANION))
        );
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
        Tardis.init(tardis, TardisComponent.InitContext.createdAt(this.pos));

        tardis.travel2().initPos(pos);
        tardis.travel2().placeAndAnimate();

        for (Consumer<ServerTardis> consumer : this.postInit) {
            consumer.accept(tardis);
        }

        AITMod.LOGGER.info("Built {} in {}ms", tardis, System.currentTimeMillis() - start);
        return tardis;
    }
}
