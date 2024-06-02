package loqor.ait.tardis.data.properties.v2;

import loqor.ait.tardis.base.KeyedTardisComponent;
import net.minecraft.network.PacketByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Property<T> {

    private final Type<T> type;
    private final String name;
    private final T def;

    public Property(Type<T> type, String name, T def) {
        this.type = type;
        this.name = name;
        this.def = def;
    }

    public Value<T> create(KeyedTardisComponent holder) {
        Value<T> result = new Value<>(holder, this, this.def);
        holder.register(result);

        return result;
    }

    public String getName() {
        return name;
    }

    public Type<T> getType() {
        return type;
    }

    public static Property<Integer> forInt(String name) {
        return forInt(name, 0);
    }

    public static Property<Integer> forInt(String name, int def) {
        return new Property<>(Type.INT, name, def);
    }

    public static Property<Boolean> forBool(String name) {
        return forBool(name, false);
    }

    public static Property<Boolean> forBool(String name, boolean def) {
        return new Property<>(Type.BOOL, name, def);
    }

    public static class Type<T> {

        public static final Type<Integer> INT = new Type<>(PacketByteBuf::writeInt, PacketByteBuf::readInt);
        public static final Type<Boolean> BOOL = new Type<>(PacketByteBuf::writeBoolean, PacketByteBuf::readBoolean);

        private final BiConsumer<PacketByteBuf, T> encoder;
        private final Function<PacketByteBuf, T> decoder;

        protected Type(BiConsumer<PacketByteBuf, T> encoder, Function<PacketByteBuf, T> decoder) {
            this.encoder = encoder;
            this.decoder = decoder;
        }

        public void encode(PacketByteBuf buf, T value) {
            this.encoder.accept(buf, value);
        }

        public T decode(PacketByteBuf buf) {
            return this.decoder.apply(buf);
        }
    }
}
