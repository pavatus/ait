package loqor.ait.tardis.data.properties.v2.bool;

import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.v2.Property;
import net.minecraft.network.PacketByteBuf;

public class BoolProperty extends Property<Boolean> {

    public static final Type<Boolean> TYPE = new Type<>(PacketByteBuf::writeBoolean, PacketByteBuf::readBoolean);

    public BoolProperty(String name) {
        this(name, false);
    }

    public BoolProperty(String name, Boolean def) {
        this(name, normalize(def));
    }

    public BoolProperty(String name, boolean def) {
        super(TYPE, name, def);
    }

    @Override
    public BoolValue create(KeyedTardisComponent holder) {
        BoolValue result = new BoolValue(holder, this, this.def);
        holder.register(result);

        return result;
    }

    public static boolean normalize(Boolean value) {
        return value != null && value;
    }
}
