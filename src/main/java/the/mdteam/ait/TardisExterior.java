package the.mdteam.ait;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;

public class TardisExterior {

    @Exclude
    protected final Tardis tardis;
    private ExteriorEnum exterior;

    public TardisExterior(Tardis tardis, ExteriorEnum exterior) {
        this.tardis = tardis;
        this.exterior = exterior;
    }

    public ExteriorEnum getType() {
        return exterior;
    }

    public void setType(ExteriorEnum exterior) {
        this.exterior = exterior;
    }
}
