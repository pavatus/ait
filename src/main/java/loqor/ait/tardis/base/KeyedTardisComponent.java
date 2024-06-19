package loqor.ait.tardis.base;

import loqor.ait.AITMod;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.data.properties.v2.PropertyMap;
import loqor.ait.tardis.data.properties.v2.Value;
import net.minecraft.network.PacketByteBuf;

public abstract class KeyedTardisComponent extends TardisComponent {

    @Exclude(strategy = Exclude.Strategy.FILE) private PropertyMap data = new PropertyMap();

    /**
     * Do NOT under any circumstances run logic in this constructor.
     * Default field values should be inlined. All logic should be done in an appropriate init method.
     *
     * @implNote The {@link TardisComponent#tardis()} will always be null at the time this constructor gets called.
     */
    public KeyedTardisComponent(IdLike id) {
        super(id);
    }

    @Override
    protected void init(InitContext context) {
        if (this.data == null)
            this.data = new PropertyMap();

        super.init(context);
    }

    public void register(Value<?> property) {
        this.data.put(property.getProperty().getName(), property);
    }

    public void update(String name, PacketByteBuf buf, byte mode) {
        Value<?> property = this.data.get(name);

        if (property == null) {
            AITMod.LOGGER.error("No property with id '{}' on {}", name, this.getId());
            return;
        }

        property.read(buf, mode);
    }

    @Override
    public void dispose() {
        super.dispose();

        this.data.dispose();
    }

    public PropertyMap getPropertyData() {
        return data;
    }
}
