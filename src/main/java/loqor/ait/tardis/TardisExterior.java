package loqor.ait.tardis;

import java.util.Optional;

import net.minecraft.block.entity.BlockEntity;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.CategoryRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.tardis.base.TardisComponent;

public class TardisExterior extends TardisComponent {
    private ExteriorCategorySchema exterior;
    private ExteriorVariantSchema variant;

    public TardisExterior(ExteriorVariantSchema variant) {
        super(Id.EXTERIOR);
        this.exterior = variant.category();
        this.variant = variant;
    }

    public ExteriorCategorySchema getCategory() {
        if (exterior == null && this.isServer()) {
            AITMod.LOGGER.error("Exterior Category was null! Changing to a random one...");
            this.setType(CategoryRegistry.getInstance().getRandom());
        }

        return exterior;
    }

    public ExteriorVariantSchema getVariant() {
        if (variant == null && this.isServer()) {
            AITMod.LOGGER.error("Variant was null! Changing to a random one...");
            this.setVariant(ExteriorVariantRegistry.getInstance().pickRandomWithParent(CategoryRegistry.CAPSULE));
        }

        return variant;
    }

    public void setType(ExteriorCategorySchema exterior) {
        this.exterior = exterior;

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
