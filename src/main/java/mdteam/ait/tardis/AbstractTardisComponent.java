package mdteam.ait.tardis;

import mdteam.ait.core.util.data.Exclude;

/**
 * Base class for all tardis components.
 * @implNote There should be NO logic run in the constructor. If you need to have such logic, implement it in {@link AbstractTardisComponent#init()} method!
 */
public abstract class AbstractTardisComponent {

    @Exclude
    protected final ITardis tardis;
    private final boolean shouldInit;
    private final String id;

    public AbstractTardisComponent(ITardis tardis, String id) {
        this(tardis, id, true);
    }

    public AbstractTardisComponent(ITardis tardis, String id, boolean shouldInit) {
        this.tardis = tardis;
        this.shouldInit = shouldInit;

        this.id = id;
    }

    public void init() { }

    public boolean shouldInit() {
        return shouldInit;
    }

    public ITardis getTardis() {
        return tardis;
    }

    public String getId() {
        return id;
    }
}
