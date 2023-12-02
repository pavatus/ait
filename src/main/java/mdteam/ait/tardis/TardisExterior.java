package mdteam.ait.tardis;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.util.data.Exclude;

public class TardisExterior {

    @Exclude
    protected final Tardis tardis;
    private ExteriorEnum exterior;
    private boolean locked;

    public TardisExterior(Tardis tardis, ExteriorEnum exterior) {
        this(tardis, exterior, false);
    }

    public TardisExterior(Tardis tardis, ExteriorEnum exterior, boolean locked) {
        this.tardis = tardis;
        this.exterior = exterior;
        this.locked = locked;
    }

    public ExteriorEnum getType() {
        return exterior;
    }

    public void setType(ExteriorEnum exterior) {
        this.exterior = exterior;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
