package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.item.TardisItemBuilder;
import mdteam.ait.tardis.exterior.ExteriorCategory;
import mdteam.ait.tardis.data.TardisLink;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.block.entity.BlockEntity;

import java.util.Optional;

public class TardisExterior extends TardisLink {
    private ExteriorCategory exterior;
    private ExteriorVariantSchema variant;

    public TardisExterior(Tardis tardis, ExteriorCategory exterior, ExteriorVariantSchema variant) {
        super(tardis, "exterior");
        this.exterior = exterior;
        this.variant = variant;
    }

    public ExteriorCategory getCategory() {
        if (exterior == null) {
            AITMod.LOGGER.error("Exterior Category was null! Changing to a random one.."); // AHH PANIC AGAIN
            setType(TardisItemBuilder.findRandomExterior());
        }

        return exterior;
    }

    public ExteriorVariantSchema getVariant() {
        if (variant == null) {
            AITMod.LOGGER.error("Variant was null! Changing to a random one.."); // AHH PANIC I BROKE VERYTHIGN!??
            setVariant(TardisItemBuilder.findRandomVariant(getCategory()));
        }

        return variant;
    }

    public void setType(ExteriorCategory exterior) {
        this.exterior = exterior;

        if (exterior != getVariant().category()) {
            AITMod.LOGGER.error("Force changing exterior variant to a random one to ensure it matches!");
            setVariant(TardisItemBuilder.findRandomVariant(exterior));
        }
        if (getTardis().isEmpty()) {
            getTardis().get().getDoor().closeDoors();
        }

        this.sync();
    }

    public void setVariant(ExteriorVariantSchema variant) {
        if (getTardis().isEmpty()) {
            getTardis().get().getDoor().closeDoors();
        }

        this.variant = variant;
        this.sync();
    }

    public Optional<ExteriorBlockEntity> findExteriorBlock() {
        if (getTardis().isEmpty()) return Optional.empty();

        BlockEntity found = this.getExteriorPos().getWorld().getBlockEntity(this.getExteriorPos());

        if (!(found instanceof ExteriorBlockEntity)) return Optional.empty();

        return Optional.of((ExteriorBlockEntity) found);
    }
}
