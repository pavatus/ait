package loqor.ait.tardis.data.properties.v2.bool;

import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.v2.Property;
import net.minecraft.network.PacketByteBuf;

import java.util.function.Supplier;

public class BoolProperty extends Property<Boolean> {

    public static final Type<Boolean> TYPE = new Type<>(PacketByteBuf::writeBoolean, PacketByteBuf::readBoolean);

    public BoolProperty(String name) {
        this(name, false);
    }

    public BoolProperty(String name, Boolean def) {
        this(name, normalize(def));
    }

    public BoolProperty(String name, boolean def) {
        this(name, () -> def);
    }

    private BoolProperty(String name, Supplier<Boolean> def) {
        super(TYPE, name, def);
    }

    @Override
    protected BoolValue create(Boolean bool) {
        return new BoolValue(bool);
    }

    @Override
    public BoolValue create(KeyedTardisComponent holder) {
        return (BoolValue) super.create(holder);
    }

    public static boolean normalize(Boolean value) {
        return value != null && value;
    }
}
