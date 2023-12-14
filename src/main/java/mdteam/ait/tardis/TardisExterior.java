package mdteam.ait.tardis;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.VariantEnum;

public class TardisExterior {

    @Exclude
    protected final Tardis tardis;
    private ExteriorEnum exterior;
    private VariantEnum variant;

    public TardisExterior(Tardis tardis, ExteriorEnum exterior, VariantEnum variant) {
        this.tardis = tardis;
        this.exterior = exterior;
        this.variant = variant;
    }

    public ExteriorEnum getType() {
        return exterior;
    }

    public VariantEnum getVariant() {
        return variant;
    }

    public void setType(ExteriorEnum exterior) {
        this.exterior = exterior;
    }

    public void setVariant(VariantEnum variant) {
        this.variant = variant;
    }
}
