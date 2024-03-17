package loqor.ait.core.util;

import loqor.ait.AITMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class AITModTags {
	public static class Blocks {
		public static final TagKey<Block> SONIC_INTERACTABLE = createTag("sonic_interactable");

		private static TagKey<Block> createTag(String name) {
			return TagKey.of(RegistryKeys.BLOCK, new Identifier(AITMod.MOD_ID, name));
		}
	}

	public static class Items {
		public static final TagKey<Item> SONIC_ITEM = createTag("sonic_item");
		public static final TagKey<Item> CLUSTER_MAX_HARVESTABLES = createTag("cluster_max_harvestables");

		private static TagKey<Item> createTag(String name) {
			return TagKey.of(RegistryKeys.ITEM, new Identifier(AITMod.MOD_ID, name));
		}
	}
}
