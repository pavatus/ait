package mdteam.ait.core.util;

import mdteam.ait.AITMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class AITModTags {
    public static class Blocks {
        //public static final TagKey<Block> test = TagKey.of(RegistryKeys.BLOCK, new Identifier(AITMod.MOD_ID, "test"));

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(AITMod.MOD_ID, name));
        }
    }
    public static class Items {
        //public static final TagKey<Item> test = TagKey.of(RegistryKeys.ITEM, new Identifier(AITMod.MOD_ID, "test"));

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(AITMod.MOD_ID, name));
        }
    }
}
