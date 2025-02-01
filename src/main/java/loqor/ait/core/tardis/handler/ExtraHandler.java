package loqor.ait.core.tardis.handler;

import net.minecraft.item.ItemStack;

import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.core.AITItems;
import loqor.ait.data.properties.Property;
import loqor.ait.data.properties.Value;

public class ExtraHandler extends KeyedTardisComponent {
    private static final Property<ItemStack> SET_REFRESHMENT_ITEM = new Property<>(Property.Type.ITEM_STACK, "set_refreshment_item", (ItemStack) null);

    private final Value<ItemStack> setRefreshmentItemValue = SET_REFRESHMENT_ITEM.create(this);

    public ExtraHandler() {
        super(Id.EXTRAS);
    }

    @Override
    public void onCreate() {
        this.setRefreshmentItem(AITItems.COFFEE.getDefaultStack());
    }

    @Override
    public void onLoaded() {
        setRefreshmentItemValue.of(this, SET_REFRESHMENT_ITEM);
    }

    public ItemStack getRefreshmentItem() {
        ItemStack itemStack = setRefreshmentItemValue.get();
        return itemStack != null ? itemStack : ItemStack.EMPTY;
    }

    public void setRefreshmentItem(ItemStack item) {
        setRefreshmentItemValue.set(item);
    }
}
