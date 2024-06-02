package loqor.ait.tardis;

import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.travel.*;

import java.util.function.Supplier;

public class TardisTravel2 extends KeyedTardisComponent implements TardisTickable {

    private State state = State.LANDED;

    public TardisTravel2() {
        super(Id.TRAVEL);
    }

    @Override
    protected void onInit(InitContext ctx) {

    }

    public Value<Integer> speed() {
        return null;
    }

    public void setState(State state) {
        this.state.onDisable(this);

        this.state = state;
        this.state.onEnable(this);
    }

    public enum State implements TravelState {
        LANDED(new LandedTravelState()),
        DEMAT(new DematTravelState()),
        FLIGHT(new FlightTravelState()),
        REMAT(new RematTravelState());

        private final TravelState parent;

        State(TravelState parent) {
            this.parent = parent;
        }

        @Override
        public void onEnable(TardisTravel2 travel) {
            this.parent.onEnable(travel);
        }

        @Override
        public void onDisable(TardisTravel2 travel) {
            this.parent.onDisable(travel);
        }

        @Override
        public void onHandbrake(TardisTravel2 travel, boolean handbrake) {
            this.parent.onHandbrake(travel, handbrake);
        }

        @Override
        public State getNext() {
            return this.parent.getNext();
        }
    }
}
