package loqor.ait.tardis.data.properties.v2;

import loqor.ait.AITMod;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

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

    public void of(TardisComponent holder, Property<T> property) {
        this.holder = holder;
        this.property = property;
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

    @SuppressWarnings("unchecked")
    public void read(PacketByteBuf buf) {
        this.set((T) this.property.getType().getDecoder().apply(buf), false);
    }

    public void write(PacketByteBuf buf) {
        this.property.getType().getEncoder().accept(buf, this.value);
    }
}
