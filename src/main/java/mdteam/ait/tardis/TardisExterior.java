package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.core.item.TardisItemBuilder;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;

public class TardisExterior {

    @Exclude
    protected final Tardis tardis;
    private ExteriorSchema exterior;
    private ExteriorVariantSchema variant;

    public TardisExterior(Tardis tardis, ExteriorSchema exterior, ExteriorVariantSchema variant) {
        this.tardis = tardis;
        this.exterior = exterior;
        this.variant = variant;
    }

    public ExteriorSchema getType() {
        return exterior;
    }

    public ExteriorVariantSchema getVariant() {
        if (variant == null) {
            AITMod.LOGGER.error("Variant was null! Changing to a random one.."); // AHH PANIC I BROKE VERYTHIGN!??
            setVariant(TardisItemBuilder.findRandomVariant(exterior));
        }

        return variant;
    }

    public void setType(ExteriorSchema exterior) {
        this.exterior = exterior;

        if (exterior != getVariant().parent()) {
            AITMod.LOGGER.error("Force changing exterior variant to a random one to ensure it matches!");
            setVariant(TardisItemBuilder.findRandomVariant(exterior));
        }
    }

    public void setVariant(ExteriorVariantSchema variant) {
        this.variant = variant;
    }
}
