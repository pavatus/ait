package loqor.ait.core;

import dev.pavatus.lib.container.impl.ItemGroupContainer;
import dev.pavatus.lib.itemgroup.AItemGroup;

import net.minecraft.item.ItemStack;

import loqor.ait.AITMod;

public class AITItemGroups implements ItemGroupContainer {

    public static final AItemGroup MAIN = AItemGroup.builder(AITMod.id("item_group"))
            .icon(() -> new ItemStack(AITItems.TARDIS_ITEM)).build();

    public static final AItemGroup FABRICATOR = AItemGroup.builder(AITMod.id("fabricator"))
            .icon(() -> new ItemStack(AITItems.BLUEPRINT)).build();
}
