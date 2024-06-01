package loqor.ait.tardis.data.properties.v2;

import loqor.ait.tardis.base.KeyedTardisComponent;
import net.minecraft.network.PacketByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Property<T> {

    private final Type type;
    private final String name;
    private final T def;

    public Property(Type type, String name, T def) {
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

    public Type getType() {
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

    public enum Type {
        INT(PacketByteBuf::writeInt, PacketByteBuf::readInt),
        BOOL(PacketByteBuf::writeBoolean, PacketByteBuf::readBoolean);

        private final BiConsumer<PacketByteBuf, Object> encoder;
        private final Function<PacketByteBuf, Object> decoder;

        @SuppressWarnings("unchecked")
        <T> Type(BiConsumer<PacketByteBuf, T> encoder, Function<PacketByteBuf, T> decoder) {
            this.encoder = (BiConsumer<PacketByteBuf, Object>) encoder;
            this.decoder = (Function<PacketByteBuf, Object>) decoder;
        }

        public Function<PacketByteBuf, Object> getDecoder() {
            return decoder;
        }

        public BiConsumer<PacketByteBuf, Object> getEncoder() {
            return encoder;
        }
    }
}
