package loqor.ait.tardis;

import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.travel.DematTravelState;
import loqor.ait.tardis.travel.FlightTravelState;
import loqor.ait.tardis.travel.LandedTravelState;
import loqor.ait.tardis.travel.RematTravelState;

import java.util.function.Supplier;

public class TardisTravel2 extends KeyedTardisComponent implements TardisTickable {

    private StateHolder holder;

    public TardisTravel2() {
        super(Id.TRAVEL);
    }

    @Override
    protected void onInit(InitContext ctx) {

    }

    public State getState() {
        return holder.state();
    }

    public StateHolder getStateHolder() {
        return holder;
    }

    record StateHolder(State state, TravelState impl) {

        StateHolder(State state) {
            this(state, state.create());
        }
    }

    public enum State {
        LANDED(LandedTravelState::new),
        DEMAT(DematTravelState::new),
        FLIGHT(FlightTravelState::new),
        REMAT(RematTravelState::new);

        private final Supplier<TravelState> supplier;

        State(Supplier<TravelState> supplier) {
            this.supplier = supplier;
        }

        public TravelState create() {
            return supplier.get();
        }
    }

    public static abstract class TravelState {

        public void onHandbrake(TardisTravel2 travel, boolean handbrake) { }

        public abstract State getNext();

        public void cycle() {

        }
    }
}
