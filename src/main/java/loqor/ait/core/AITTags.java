package loqor.ait.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;

public class AITTags {

    public static class Blocks {
        public static final TagKey<Block> SONIC_INTERACTABLE = createTag("sonic_interactable");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(AITMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> FULL_RESPIRATORS = createTag("full_respirators");
        public static final TagKey<Item> HALF_RESPIRATORS = createTag("half_respirators");
        public static final TagKey<Item> SONIC_ITEM = createTag("sonic_item");
        public static final TagKey<Item> CLUSTER_MAX_HARVESTABLES = createTag("cluster_max_harvestables");

        public static final TagKey<Item> NO_BOP = createTag("no_bop");
        public static final TagKey<Item> KEY = createTag("key");
        public static final TagKey<Item> LINK = createTag("link"); // TODO use the tag instead of the item instanceof
        public static final TagKey<Item> IS_TARDIS_FUEL = createTag("is_tardis_fuel");
        public static final TagKey<Item> REPAIRS_SUBSYSTEM = createTag("repairs_subsystem");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(AITMod.MOD_ID, name));
        }
    }
}
