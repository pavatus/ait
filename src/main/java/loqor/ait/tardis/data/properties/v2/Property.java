package loqor.ait.tardis.data.properties.v2;

import loqor.ait.AITMod;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class Property<T> {

    /**
     * Due to a circular-dependency between a component and a property, it should be excluded.
     */
    @Exclude private TardisComponent holder;
    private final Identifier id;
    private final Type type;
    private T value;

    public Property(KeyedTardisComponent holder, Type type, String name) {
        this.id = new Identifier(holder.getId().toString().toLowerCase(), name);
        this.type = type;
    }

    public Property(KeyedTardisComponent component, Type type, String name, T def) {
        this(component, type, name);
        this.value = def;
    }

    public void init(KeyedTardisComponent holder) {
        this.holder = holder;
        holder.register(this);
    }

    public Type getType() {
        return type;
    }

    public Identifier getId() {
        return id;
    }

    public TardisComponent getHolder() {
        return holder;
    }

    public T get() {
        return value;
    }

    public void set(Property<T> value) {
        this.set(value.get(), true);
    }

    public void set(T value) {
        this.set(value, true);
    }

    public void set(T value, boolean sync) {
        this.value = value;

        if (sync) {
            if (!(this.holder.tardis() instanceof ServerTardis tardis)) {
                AITMod.LOGGER.warn("Can't sync on a client world!", new IllegalAccessException());
                return;
            }

            ServerTardisManager.getInstance().sendPropertyV2ToSubscribers(tardis, this);
        }
    }

    public void flatMap(Function<T, T> func) {
        this.set(func.apply(this.value), true);
    }

    public void flatMap(Function<T, T> func, boolean sync) {
        this.set(func.apply(this.value), sync);
    }

    @SuppressWarnings("unchecked")
    public void read(PacketByteBuf buf) {
        this.set((T) this.type.decoder.apply(buf), false);
    }

    public void write(PacketByteBuf buf) {
        this.type.encoder.accept(buf, this.value);
    }

    public static Property<Integer> forInt(KeyedTardisComponent component, String name) {
        return forInt(component, name, 0);
    }

    public static Property<Integer> forInt(KeyedTardisComponent component, String name, int def) {
        return new Property<>(component, Type.INT, name, def);
    }

    public static Property<Boolean> forBool(KeyedTardisComponent component, String name) {
        return forBool(component, name, false);
    }

    public static Property<Boolean> forBool(KeyedTardisComponent component, String name, boolean def) {
        return new Property<>(component, Type.BOOL, name, def);
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
    }
}
