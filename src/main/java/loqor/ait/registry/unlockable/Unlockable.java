package loqor.ait.registry.unlockable;

import java.util.Optional;

import loqor.ait.api.Identifiable;
import loqor.ait.tardis.handler.loyalty.Loyalty;

public interface Unlockable extends Identifiable {
    UnlockType unlockType();

    Optional<Loyalty> requirement();

    /**
     * Decides whether this desktop should be auto-unlocked on creation. aka -
     * freebee, freeby
     */
    default boolean freebie() {
        return this.requirement().isEmpty();
    }

    enum UnlockType {
        EXTERIOR, CONSOLE, SONIC, DESKTOP
    }
}
