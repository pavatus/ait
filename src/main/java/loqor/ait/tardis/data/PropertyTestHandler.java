package loqor.ait.tardis.data;

import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.Value;

public class PropertyTestHandler extends KeyedTardisComponent {

    private static final Property<Boolean> BOOL = new Property<>(Property.Type.BOOL, "bool", true);

    private final Value<Boolean> bool = BOOL.create(this);

    public PropertyTestHandler() {
        super(Id.TESTING);
    }

    @Override
    protected void onInit(InitContext ctx) {
        bool.of(this, BOOL);
    }

    public Value<Boolean> getBool() {
        return bool;
    }
}
