package loqor.ait.tardis;

import java.util.Optional;
import java.util.UUID;

import io.wispforest.owo.ops.WorldOps;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.util.StackUtil;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;

public class TardisExterior extends TardisComponent {

    public static final Identifier CHANGE_EXTERIOR = new Identifier(AITMod.MOD_ID, "change_exterior");

    private static final ExteriorCategorySchema MISSING_CATEGORY = CategoryRegistry.getInstance().fallback();
    private static final ExteriorVariantSchema MISSING_VARIANT = ExteriorVariantRegistry.getInstance().fallback();

    private ExteriorCategorySchema category;
    private ExteriorVariantSchema variant;

    static {
        ServerPlayNetworking.registerGlobalReceiver(CHANGE_EXTERIOR, (server, player, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            Identifier exteriorValue = Identifier.tryParse(buf.readString());
            boolean variantChange = buf.readBoolean();
            String variantValue = buf.readString();

            ServerTardisManager.getInstance().getTardis(server, uuid, tardis -> {
                ExteriorVariantSchema schema = ExteriorVariantRegistry.getInstance()
                        .get(Identifier.tryParse(variantValue));

                // no hax
                if (!tardis.isUnlocked(schema))
                    return;

                server.execute(() -> StackUtil.playBreak(player));

                tardis.getExterior().setType(CategoryRegistry.getInstance().get(exteriorValue));
                WorldOps.updateIfOnServer(server.getWorld(tardis.travel().position().getWorld().getRegistryKey()),
                        tardis.travel().position().getPos());
                if (variantChange) {
                    tardis.getExterior().setVariant(schema);
                    WorldOps.updateIfOnServer(server.getWorld(tardis.travel().position().getWorld().getRegistryKey()),
                            tardis.travel().position().getPos());
                }
                TardisEvents.EXTERIOR_CHANGE.invoker().onChange(tardis);
            });
        });
    }

    public TardisExterior(ExteriorVariantSchema variant) {
        super(Id.EXTERIOR);

        this.category = variant.category();
        this.variant = variant;
    }

    private void setMissing() {
        if (this.tardis instanceof ClientTardis clientTardis)
            ClientTardisUtil.changeExteriorWithScreen(clientTardis, MISSING_CATEGORY.id(), MISSING_VARIANT.id(), true);

        this.category = MISSING_CATEGORY;
        this.variant = MISSING_VARIANT;
    }

    public ExteriorCategorySchema getCategory() {
        if (this.category == null)
            this.setMissing();

        return category;
    }

    public ExteriorVariantSchema getVariant() {
        if (this.variant == null)
            this.setMissing();

        return variant;
    }

    public void setType(ExteriorCategorySchema exterior) {
        this.category = exterior;

        if (exterior != this.getVariant().category()) {
            AITMod.LOGGER.error("Force changing exterior variant to a random one to ensure it matches!");
            this.setVariant(ExteriorVariantRegistry.getInstance().pickRandomWithParent(exterior));
        }

        this.sync();
    }

    public void setVariant(ExteriorVariantSchema variant) {
        this.variant = variant;
        this.sync();
    }

    public Optional<ExteriorBlockEntity> findExteriorBlock() {
        BlockEntity found = tardis.travel().position().getWorld().getBlockEntity(tardis.travel().position().getPos());

        if (!(found instanceof ExteriorBlockEntity))
            return Optional.empty();

        return Optional.of((ExteriorBlockEntity) found);
    }
}
