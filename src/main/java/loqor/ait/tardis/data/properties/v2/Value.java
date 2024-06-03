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
    @Exclude private Property<T> property;

    private T value;

    public Value(TardisComponent holder, Property<T> property, T value) {
        this.holder = holder;
        this.property = property;
        this.value = value;
    }

    private Value(T value) {
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

    public void read(PacketByteBuf buf) {
        if (this.property == null) {
            throw new IllegalStateException("Couldn't get the parent property value! Maybe you forgot to initialize the value field on load?");
        }

        this.set(this.property.getType().decode(buf), false);
    }

    public void write(PacketByteBuf buf) {
        this.property.getType().encode(buf, this.value);
    }

    public static Object serializer() {
        return new Serializer();
    }

    static class Serializer implements JsonSerializer<Value<?>>, JsonDeserializer<Value<?>> {

        @Override
        public Value<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            ParameterizedType type = (ParameterizedType) typeOfT;
            return new Value<>(context.deserialize(json, type.getActualTypeArguments()[0]));
        }

        @Override
        public JsonElement serialize(Value<?> src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.value);
        }
    }
}
