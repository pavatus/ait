package loqor.ait.tardis.data.properties.v2.integer;

import loqor.ait.tardis.data.properties.v2.Value;

public class IntValue extends Value<Integer> {

    protected IntValue(Integer value) {
        super(value);
    }

    @Override
    public void set(Integer value, boolean sync) {
        super.set(IntProperty.normalize(value), sync);
    }

    public static Object serializer() {
        return new Serializer<>(Integer.class, IntValue::new);
    }
}