package loqor.ait.tardis.data.properties.v2.bool;

import loqor.ait.tardis.data.properties.v2.Value;

public class BoolValue extends Value<Boolean> {

    protected BoolValue(boolean value) {
        super(value);
    }

    private BoolValue(Boolean value) {
        super(value);
    }

    @Override
    public void set(Boolean value, boolean sync) {
        super.set(BoolProperty.normalize(value), sync);
    }

    public static Object serializer() {
        return new Serializer<Boolean, BoolValue>(Boolean.class, BoolValue::new);
    }
}