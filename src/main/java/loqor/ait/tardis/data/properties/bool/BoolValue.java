package loqor.ait.tardis.data.properties.bool;

import loqor.ait.tardis.data.properties.Value;

public class BoolValue extends Value<Boolean> {

    protected BoolValue(Boolean value) {
        super(value);
    }

    @Override
    public void set(Boolean value, boolean sync) {
        super.set(BoolProperty.normalize(value), sync);
    }

    public static Object serializer() {
        return new Serializer<>(BoolProperty.TYPE, BoolValue::new);
    }
}
