package loqor.ait.tardis.base;

import loqor.ait.AITMod;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.PropertyMap;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class KeyedTardisComponent extends TardisComponent {

    @Exclude private PropertyMap data;

    /**
     * Do NOT under any circumstances run logic in this constructor.
     * Default field values should be inlined. All logic should be done in an appropriate init method.
     *
     * @implNote The {@link TardisComponent#tardis()} will always be null at the time this constructor gets called.
     */
    public KeyedTardisComponent(Id id) {
        super(id);
    }

    @Override
    protected void onEarlyInit(InitContext ctx) {
        this.data = new PropertyMap();
    }

    public void register(Property<?> property) {
        this.data.put(property.getId(), property);
    }

    public void update(Identifier id, PacketByteBuf buf) {
        Property<?> property = this.data.get(id);

        if (property == null) {
            AITMod.LOGGER.error("No property with id '{}' on {}", id, this.getId());
            return;
        }

        property.read(buf);
    }
}
