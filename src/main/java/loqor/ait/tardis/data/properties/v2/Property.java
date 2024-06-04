package loqor.ait.tardis.data.properties.v2;

import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.base.KeyedTardisComponent;
import net.minecraft.network.PacketByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Property<T> {

    private final Type<T> type;
    private final String name;
    protected final T def;

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

    public Property<T> copy(String name) {
        return new Property<>(this.type, name, this.def);
    }

    public Property<T> copy(String name, T def) {
        return new Property<>(this.type, name, def);
    }

    public static <T extends Enum<T>> Property<T> forEnum(String name, Class<T> clazz, T def) {
        return new Property<>(Type.forEnum(clazz), name, def);
    }

    public static class Type<T> {

        public static final Type<DirectedGlobalPos> DIRECTED_GLOBAL_POS = new Type<>((buf, pos) -> pos.write(buf), DirectedGlobalPos::read);

        private final BiConsumer<PacketByteBuf, T> encoder;
        private final Function<PacketByteBuf, T> decoder;

        public Type(BiConsumer<PacketByteBuf, T> encoder, Function<PacketByteBuf, T> decoder) {
            this.encoder = encoder;
            this.decoder = decoder;
        }

        public void encode(PacketByteBuf buf, T value) {
            this.encoder.accept(buf, value);
        }

        public T decode(PacketByteBuf buf) {
            return this.decoder.apply(buf);
        }

        public static <T extends Enum<T>> Type<T> forEnum(Class<T> clazz) {
            return new Type<>(PacketByteBuf::writeEnumConstant, buf -> buf.readEnumConstant(clazz));
        }
    }

    public interface Mode {
        byte UPDATE = 0;
        byte NULL = 1;

        static byte forValue(Value<?> o) {
            return o.get() == null ? NULL : UPDATE;
        }
    }
}
