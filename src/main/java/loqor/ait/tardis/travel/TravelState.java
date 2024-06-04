package loqor.ait.tardis.travel;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel2;

public interface TravelState<T extends TravelState.AbstractContext> {

    default void onEnable(T context) { }

    default void onDisable(T context) { }

    default void onHandbrake(T context, boolean handbrake) { }

    default void onSpeedChange(T context, int speed) { }

    TardisTravel2.State getNext();

    abstract class AbstractContext {

        private final TardisTravel2 travel;

        protected AbstractContext(TardisTravel2 travel) {
            this.travel = travel;
        }

        public TardisTravel2 travel() {
            return this.travel;
        }

        public Tardis tardis() {
            return this.travel.tardis();
        }
    }
}