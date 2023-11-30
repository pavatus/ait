package mdteam.ait.core;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.item.RemoteItem;
import mdteam.ait.core.item.TardisItemBuilder;
import net.minecraft.item.Item;

public class AITItems implements ItemRegistryContainer {
    public static final Item AITMODCREATIVETAB = new Item(new OwoItemSettings());

    public static final Item TARDIS_ITEM = new TardisItemBuilder(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item TOYOTA_ITEM = new TardisItemBuilder(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), ExteriorEnum.TOYOTA);
    public static final Item REMOTE_ITEM = new RemoteItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
}
