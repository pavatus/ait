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

public class TardisTravel2 extends KeyedTardisComponent implements TardisTickable {

    private static final RangedIntProperty SPEED = new RangedIntProperty("speed", 7, 0);

    private static final BoolProperty AUTO_LAND = new BoolProperty("auto_land");
    private static final BoolProperty HANDBRAKE = new BoolProperty("handbrake", true);

    private static final Property<DirectedGlobalPos> TARGET_POS = new Property<>(Property.Type.DIRECTED_GLOBAL_POS, null, null);

    private static final Property<DirectedGlobalPos> DESTINATION = TARGET_POS.copy("destination");
    private static final Property<DirectedGlobalPos> LAST_POS = TARGET_POS.copy("last_position");
    private static final Property<DirectedGlobalPos> POSITION = TARGET_POS.copy("position");


    private final RangedIntValue speed = SPEED.create(this);

    private final BoolValue autoLand = AUTO_LAND.create(this);
    private final BoolValue handbrake = HANDBRAKE.create(this);

    private final Value<DirectedGlobalPos> destination = DESTINATION.create(this);
    private final Value<DirectedGlobalPos> lastPosition = LAST_POS.create(this);
    private final Value<DirectedGlobalPos> position = POSITION.create(this);

    public TardisTravel2() {
        super(Id.TRAVEL);
    }

    @Override
    public void onLoaded() {
        speed.of(this, SPEED);

        autoLand.of(this, AUTO_LAND);
        handbrake.of(this, HANDBRAKE);

        destination.of(this, DESTINATION);
        lastPosition.of(this, LAST_POS);
        position.of(this, POSITION);
    }

    public void setHandbrake(boolean value) {
        handbrake.set(value);
    }

    public boolean getHandbrake() {
        return handbrake.get();
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
}
