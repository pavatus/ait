package loqor.ait.tardis;

import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.data.properties.v2.bool.BoolProperty;
import loqor.ait.tardis.data.properties.v2.bool.BoolValue;
import loqor.ait.tardis.data.properties.v2.integer.ranged.RangedIntProperty;
import loqor.ait.tardis.data.properties.v2.integer.ranged.RangedIntValue;
import loqor.ait.tardis.travel.*;

public class TardisTravel2 extends KeyedTardisComponent implements TardisTickable {

    private static final RangedIntProperty SPEED = new RangedIntProperty("speed", 7, 0);
    private static final Property<State> STATE = Property.forEnum("state", State.class, State.LANDED);

    private static final BoolProperty AUTO_LAND = new BoolProperty("auto_land");
    private static final BoolProperty HANDBRAKE = new BoolProperty("handbrake", true);

    private static final Property<DirectedGlobalPos> TARGET_POS = new Property<>(Property.Type.DIRECTED_GLOBAL_POS, null, null);

    private static final Property<DirectedGlobalPos> DESTINATION = TARGET_POS.copy("destination");
    private static final Property<DirectedGlobalPos> LAST_POS = TARGET_POS.copy("last_position");
    private static final Property<DirectedGlobalPos> POSITION = TARGET_POS.copy("position");


    private final RangedIntValue speed = SPEED.create(this);
    private final Value<State> state = STATE.create(this);

    private final BoolValue autoLand = AUTO_LAND.create(this);
    private final BoolValue handbrake = HANDBRAKE.create(this);

    private final Value<DirectedGlobalPos> destination = DESTINATION.create(this);
    private final Value<DirectedGlobalPos> lastPosition = LAST_POS.create(this);
    private final Value<DirectedGlobalPos> position = POSITION.create(this);

    public TardisTravel2() {
        super(Id.TRAVEL);
    }

    @Override
    protected void onInit(InitContext ctx) {

    }

    @Override
    public void onLoaded() {
        speed.of(this, SPEED);

        autoLand.of(this, AUTO_LAND);
        handbrake.of(this, HANDBRAKE);

        destination.of(this, DESTINATION);
        lastPosition.of(this, LAST_POS);
        position.of(this, POSITION);

        state.of(this, STATE);
    }

    public RangedIntValue speed() {
        return speed;
    }

    public BoolValue autoLand() {
        return autoLand;
    }

    public Value<DirectedGlobalPos> position() {
        return position;
    }

    public Value<DirectedGlobalPos> destination() {
        return destination;
    }

    public <T extends TravelState.AbstractContext> void setState(TravelState<T> state, T context) {
        this.state.flatMap(original -> {
            TravelState<T> real = (TravelState<T>) original.get();
            original.get().onDisable(context);
            state.onEnable(context);
            return state;
        });
    }

    public enum State {
        LANDED(new LandedTravelState()),
        DEMAT(new DematTravelState()),
        FLIGHT(new FlightTravelState()),
        REMAT(new RematTravelState());

        private final TravelState parent;

        State(TravelState parent) {
            this.parent = parent;
        }

        public TravelState<?> get() {
            return parent;
        }
    }

    public interface StateImpl {
        LandedTravelState LANDED = new LandedTravelState();
        DematTravelState DEMAT = new DematTravelState();
        FlightTravelState FLIGHT = new FlightTravelState();
        RematTravelState REMAT = new RematTravelState();
    }
}
