package loqor.ait.tardis.manager;

import loqor.ait.AITMod;
import loqor.ait.tardis.util.TardisMap;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisManager;

/**
 * A tardis manager that holds an additional buffer for old TARDISes.
 * This ensures that the TARDISes get disposed of after use.
 */
public abstract class AgingTardisManager<T extends Tardis, C> extends TardisManager<T, C> {

    protected final TardisMap<T> ageBuffer = new TardisMap<>();

    /**
     * Replaces the old tardis with a new one, provided in the arguments.
     */
    protected void updateAge(T newTardis) {
        T old = this.lookup.put(newTardis);

        if (old == null)
            return;

        old.age();
        AITMod.LOGGER.info("Marking {} as aged", old);
        T realOld = this.ageBuffer.put(old);

        if (realOld == null)
            return;

        AITMod.LOGGER.info("Disposing {}", realOld);
        realOld.dispose();
    }

    @Override
    public void reset() {
        this.ageBuffer.clear();
        super.reset();
    }
}
