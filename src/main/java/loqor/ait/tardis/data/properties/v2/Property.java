package loqor.ait.tardis.data.properties.v2;

import loqor.ait.AITMod;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.base.KeyedTardisComponent;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Property<T> {

    private final Type<T> type;
    private final String name;

    protected final Function<KeyedTardisComponent, T> def;

    public Property(Type<T> type, String name, Function<KeyedTardisComponent, T> def) {
        this.type = type;
        this.name = name;
        this.def = def;
    }

    public Property(Type<T> type, String name, T def) {
        this(type, name, o -> def);
    }

    public Value<T> create(KeyedTardisComponent holder) {
        T t = this.def == null ? null : this.def.apply(holder);
        Value<T> result = this.create(t);

        result.of(holder, this);
        return result;
    }

    protected Value<T> create(T t) {
        return new Value<>(t);
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
        public static final Type<Identifier> IDENTIFIER = new Type<>(PacketByteBuf::writeIdentifier, PacketByteBuf::readIdentifier);

        public static final Type<RegistryKey<World>> WORLD_KEY = new Type<>(PacketByteBuf::writeRegistryKey, buf -> buf.readRegistryKey(RegistryKeys.WORLD));
        public static final Type<Direction> DIRECTION = new Type<>(PacketByteBuf::writeEnumConstant, buf -> buf.readEnumConstant(Direction.class));

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

    @Deprecated(forRemoval = true)
    public static <T> T warnCompat(String name, T val) {
        AITMod.LOGGER.warn("Property {} needs to get v1 compatibility!", name);
        return val;
    }
}
