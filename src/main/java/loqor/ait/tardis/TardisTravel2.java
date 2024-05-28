package loqor.ait.tardis;

import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.properties.v2.Property;

public class TardisTravel2 extends KeyedTardisComponent {

    private final Property<Integer> SPEED = Property.forInt(this, "speed");
    private final Property<Integer> MAX_SPEED = Property.forInt(this, "max_speed", 7);

    public TardisTravel2() {
        super(Id.TRAVEL);
    }

    public int getSpeed() {
        return PropertiesHandler.getInt(this.tardis.properties(), PropertiesHandler.SPEED);
    }

    public void setSpeed(int speed) {
        PropertiesHandler.set(this.tardis, PropertiesHandler.SPEED, speed);
    }

    public int getMaxSpeed() {
        return PropertiesHandler.getInt(this.tardis.properties(), PropertiesHandler.MAX_SPEED);
    }

    public void setMaxSpeed(int speed) {
        PropertiesHandler.set(this.tardis, PropertiesHandler.MAX_SPEED, speed);
    }
}
