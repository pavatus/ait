package loqor.ait.tardis.data.properties.v2.bool;

import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.v2.Value;

public class BoolValue extends Value<Boolean> {

    protected BoolValue(TardisComponent holder, BoolProperty property, boolean value) {
        super(holder, property, value);
    }

    @Override
    public void set(Boolean value, boolean sync) {
        super.set(BoolProperty.normalize(value), sync);
    }
}