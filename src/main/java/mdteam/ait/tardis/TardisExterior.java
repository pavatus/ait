package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.item.TardisItemBuilder;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.data.TardisLink;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.block.entity.BlockEntity;

import java.util.Optional;

public class TardisExterior extends TardisLink {
    private ExteriorSchema exterior;
    private ExteriorVariantSchema variant;

    public TardisExterior(Tardis tardis, ExteriorSchema exterior, ExteriorVariantSchema variant) {
        super(tardis, "exterior");
        this.exterior = exterior;
        this.variant = variant;
    }

    public ExteriorSchema getType() {
        if (exterior == null) {
            AITMod.LOGGER.error("Exterior Type was null! Changing to a random one.."); // AHH PANIC AGAIN
            setType(TardisItemBuilder.findRandomExterior());
        }

        return exterior;
    }

    public ExteriorVariantSchema getVariant() {
        if (variant == null) {
            AITMod.LOGGER.error("Variant was null! Changing to a random one.."); // AHH PANIC I BROKE VERYTHIGN!??
            setVariant(TardisItemBuilder.findRandomVariant(getType()));
        }

        return variant;
    }

    public void setType(ExteriorSchema exterior) {
        this.exterior = exterior;

        if (exterior != getVariant().parent()) {
            AITMod.LOGGER.error("Force changing exterior variant to a random one to ensure it matches!");
            setVariant(TardisItemBuilder.findRandomVariant(exterior));
        }
        if (tardis() != null) {
            tardis().getDoor().closeDoors();
        }
    }

    public void setVariant(ExteriorVariantSchema variant) {
        if (tardis() != null) {
            tardis().getDoor().closeDoors();
        }

        this.variant = variant;
    }

    public Optional<ExteriorBlockEntity> findExteriorBlock() {
        if (tardis() == null) return Optional.empty();

        BlockEntity found = this.getExteriorPos().getWorld().getBlockEntity(this.getExteriorPos());

        if (!(found instanceof ExteriorBlockEntity)) return Optional.empty();

        return Optional.of((ExteriorBlockEntity) found);
    }
}
