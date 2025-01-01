package loqor.ait.core;

import dev.pavatus.lib.container.impl.ItemGroupContainer;
import dev.pavatus.lib.itemgroup.AItemGroup;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;

public class AITItemGroups implements ItemGroupContainer {

    public static final AItemGroup MAIN = AItemGroup.builder(new Identifier(AITMod.MOD_ID, "item_group"))
            .icon(() -> new ItemStack(AITItems.TARDIS_ITEM)).build();

    public static final AItemGroup FABRICATOR = AItemGroup.builder(new Identifier(AITMod.MOD_ID, "fabricator"))
            .icon(() -> new ItemStack(AITItems.BLUEPRINT)).build();
}
