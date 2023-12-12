package mdteam.ait.core;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.annotations.AssignedName;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import mdteam.ait.AITMod;
import mdteam.ait.core.item.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class AITItems implements ItemRegistryContainer {
    // TARDIS
    public static final Item TARDIS_ITEM = new TardisItemBuilder(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    // Functional Items
    public static final Item REMOTE_ITEM = new RemoteItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    // Keys/Key Templates
    public static final Item IRON_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item GOLD_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), KeyItem.Protocols.SNAP);
    public static final Item NETHERITE_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), KeyItem.Protocols.SNAP);
    public static final Item CLASSIC_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), KeyItem.Protocols.SNAP);
    public static final Item GOLD_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

    // Sonic Screwdrivers
    @AssignedName("mechanical")
    public static final Item MECHANICAL_SONIC_SCREWDRIVER = new SonicItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    @AssignedName("coral")
    public static final Item CORAL_SONIC_SCREWDRIVER = new InteriorSelectItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), new Identifier(AITMod.MOD_ID, "default_cave"));
}
