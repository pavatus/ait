package loqor.ait.tardis.data.properties.v2.integer.ranged;

import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.v2.Property;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;

import java.util.function.Supplier;

public class RangedIntProperty extends Property<Integer> {

    public static final Type<Integer> TYPE = new Type<>(PacketByteBuf::writeInt, PacketByteBuf::readInt);

    private final int min;
    private final int max;

    public RangedIntProperty(String name, int max) {
        this(name, 0, max, 0);
    }

    public RangedIntProperty(String name, int max, Integer def) {
        this(name, 0, max, normalize(0, max, def));
    }

    public RangedIntProperty(String name, int min, int max, Integer def) {
        this(name, min, max, () -> normalize(min, max, def));
    }

    public RangedIntProperty(String name, int max, int def) {
        this(name, 0, max, () -> def);
    }

    private RangedIntProperty(String name, int min, int max, Supplier<Integer> def) {
        super(TYPE, name, def);

        this.min = min;
        this.max = max;
    }

    @Override
    public RangedIntValue create(KeyedTardisComponent holder) {
        return (RangedIntValue) super.create(holder);
    }

    @Override
    protected RangedIntValue create(Integer integer) {
        return new RangedIntValue(integer);
    }

    public static int normalize(int min, int max, Integer value) {
        return MathHelper.clamp(value == null ? 0 : value, min, max);
    }

    public static int normalize(RangedIntProperty property, Integer value) {
        return normalize(property.min, property.max, value);
    }
}
