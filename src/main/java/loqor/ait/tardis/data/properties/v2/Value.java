package loqor.ait.tardis.data.properties.v2;

import com.google.gson.*;
import loqor.ait.AITMod;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.network.PacketByteBuf;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

public class Value<T> {

    /**
     * Due to a circular-dependency between a component and a property, it should be excluded.
     */
    @Exclude private TardisComponent holder;
    @Exclude protected Property<T> property;

    private T value;

    protected Value(TardisComponent holder, Property<T> property, T value) {
        this.holder = holder;
        this.property = property;
        this.value = value;
    }

    protected Value(T value) {
        this.value = value;
    }

    public void of(KeyedTardisComponent holder, Property<T> property) {
        this.holder = holder;
        this.property = property;

        holder.register(this);
    }

    public Property<T> getProperty() {
        return property;
    }

    public TardisComponent getHolder() {
        return holder;
    }

    public T get() {
        return value;
    }

    public void set(Value<T> value) {
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

    public void read(PacketByteBuf buf, byte mode) {
        if (this.property == null)
            throw new IllegalStateException("Couldn't get the parent property value! Maybe you forgot to initialize the value field on load?");

        T value = mode == Property.Mode.UPDATE
                ? this.property.getType().decode(buf) : null;

        this.set(value, false);
    }

    public void write(PacketByteBuf buf) {
        this.property.getType().encode(buf, this.value);
    }

    public static Object serializer() {
        return new Serializer<>(Value::new);
    }

    protected static class Serializer<V, T extends Value<V>> implements JsonSerializer<T>, JsonDeserializer<T> {

        private final Class<?> clazz;
        private final Function<V, T> creator;

        public Serializer(Function<V, T> creator) {
            this(null, creator);
        }

        public Serializer(Class<?> clazz, Function<V, T> creator) {
            this.clazz = clazz;
            this.creator = creator;
        }

        @Override
        public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Type type = clazz;

            if (typeOfT instanceof ParameterizedType parameter)
                type = parameter.getActualTypeArguments()[0];

            return this.creator.apply(context.deserialize(json, type));
        }

        @Override
        public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.get());
        }
    }
}
