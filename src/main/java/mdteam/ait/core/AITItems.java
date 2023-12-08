package mdteam.ait.core;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.annotations.AssignedName;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.item.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import mdteam.ait.tardis.TardisDesktopSchema;
import net.minecraft.util.Identifier;

public class AITItems implements ItemRegistryContainer {

    // TARDIS related
    public static final Item TARDIS_ITEM = new TardisItemBuilder(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));

    // Tools
    public static final Item REMOTE_ITEM = new RemoteItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));

    // Keys/Key Templates
    public static final Item IRON_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item GOLD_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item NETHERITE_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item CLASSIC_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item GOLD_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

    // Sonic Screwdrivers
    @AssignedName("mechanical")
    public static final Item MECHANICAL_SONIC_SCREWDRIVER = new ExteriorSelectItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    @AssignedName("coral")
    public static final Item CORAL_SONIC_SCREWDRIVER = new InteriorSelectItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), new Identifier(AITMod.MOD_ID, "default_cave"));


    // THE STUPID SHIT CATEGORY :)
    // This is only temporary for testing until proper interior/exterior changing is done fixme

    //public static void createExteriorItems() {
    //    for (ExteriorEnum exterior : ExteriorEnum.values()) {
    //        Registry.register(Registries.ITEM, exterior.name().toLowerCase() + "_item", new ExteriorSelectItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP),exterior));
    //    }
    //}
    //public static void createInteriorItems() {
    //    for (TardisDesktopSchema interior : AITDesktops.iterator()) {
    //        Registry.register(Registries.ITEM, interior.id().toString().toLowerCase() + "_item", new InteriorSelectItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP),interior.id()));
    //    }
    //}
}
