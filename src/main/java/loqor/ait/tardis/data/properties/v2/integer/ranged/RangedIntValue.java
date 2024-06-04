package loqor.ait.tardis.data.properties.v2.integer.ranged;

import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.v2.Value;

public class RangedIntValue extends Value<Integer> {

    protected RangedIntValue(TardisComponent holder, RangedIntProperty property, int value) {
        super(holder, property, value);
    }

    @Override
    public void set(Integer value, boolean sync) {
        super.set(RangedIntProperty.normalize(this.asRanged(), value), sync);
    }

    private RangedIntProperty asRanged() {
        return (RangedIntProperty) this.property;
    }
}