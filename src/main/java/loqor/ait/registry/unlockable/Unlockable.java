package loqor.ait.registry.unlockable;

import loqor.ait.core.data.base.Identifiable;
import loqor.ait.tardis.data.loyalty.Loyalty;

import java.util.Optional;

public interface Unlockable extends Identifiable {
    UnlockType unlockType();
    Optional<Loyalty> requirement();

    /**
     * Decides whether this desktop should be auto-unlocked on creation.
     * aka - freebee, freeby
     */
    default boolean freebie() {
        return this.requirement().isEmpty();
    }

    enum UnlockType {
        EXTERIOR,
        CONSOLE,
        SONIC,
        DESKTOP
    }
}
