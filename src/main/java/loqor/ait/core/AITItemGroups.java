package loqor.ait.core;

import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;

public class AITItemGroups {
    //Creative Inventory Tabs
    public static final OwoItemGroup MAIN = OwoItemGroup
            .builder(new Identifier(AITMod.MOD_ID, "item_group"), () -> Icon.of(AITItems.TARDIS_ITEM))
            .disableDynamicTitle().build();

    public static final OwoItemGroup FABRICATOR = OwoItemGroup
                        .builder(new Identifier(AITMod.MOD_ID, "fabricator"), () -> Icon.of(AITItems.BLUEPRINT))
            .disableDynamicTitle().build();
}
