package loqor.ait.registry.unlockable;

import loqor.ait.core.data.base.Identifiable;
import loqor.ait.tardis.data.loyalty.Loyalty;

public interface Unlockable extends Identifiable {
    UnlockType unlockType();
    Loyalty getRequirement();

    /**
     * Decides whether this desktop should be auto-unlocked on creation.
     * aka - freebee, freeby
     */
    default boolean freebie() {
        return false;
    }

    enum UnlockType {
        EXTERIOR,
        CONSOLE,
        SONIC,
        DESKTOP
    }
}
