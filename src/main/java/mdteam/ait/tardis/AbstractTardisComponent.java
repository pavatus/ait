package mdteam.ait.tardis;

import mdteam.ait.core.util.data.Exclude;

/**
 * Base class for all tardis components.
 * @implNote There should be NO logic run in the constructor. If you need to have such logic, implement it in {@link AbstractTardisComponent#init()} method!
 */
public abstract class AbstractTardisComponent {

    @Exclude
    protected final Tardis tardis;
    private final boolean shouldInit;

    public AbstractTardisComponent(Tardis tardis) {
        this(tardis, true);
    }

    public AbstractTardisComponent(Tardis tardis, boolean shouldInit) {
        this.tardis = tardis;
        this.shouldInit = shouldInit;
    }

    public void init() { }

    public boolean shouldInit() {
        return shouldInit;
    }

    public Tardis getTardis() {
        return tardis;
    }
}
