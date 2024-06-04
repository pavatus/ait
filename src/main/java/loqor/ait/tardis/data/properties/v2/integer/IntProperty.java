package loqor.ait.tardis.data.properties.v2.integer;

import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.v2.Property;
import net.minecraft.network.PacketByteBuf;

public class IntProperty extends Property<Integer> {

    public static final Type<Integer> TYPE = new Type<>(PacketByteBuf::writeInt, PacketByteBuf::readInt);

    public IntProperty(String name) {
        this(name, 0);
    }

    public IntProperty(String name, Integer def) {
        this(name, normalize(def));
    }

    public IntProperty(String name, int def) {
        super(TYPE, name, def);
    }

    @Override
    public IntValue create(KeyedTardisComponent holder) {
        IntValue result = new IntValue(holder, this, this.def);
        holder.register(result);

        return result;
    }

    public static int normalize(Integer value) {
        return value == null ? 0 : value;
    }
}
