package loqor.ait.tardis.data.properties.v2.integer;

import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.v2.Value;

public class IntValue extends Value<Integer> {

    protected IntValue(TardisComponent holder, IntProperty property, int value) {
        super(holder, property, value);
    }

    @Override
    public void set(Integer value, boolean sync) {
        super.set(IntProperty.normalize(value), sync);
    }
}