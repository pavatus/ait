package loqor.ait.core;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import loqor.ait.AITMod;
import loqor.ait.core.item.*;
import loqor.ait.core.item.control.GenericControlBlockItem;
import loqor.ait.core.util.AITArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.Rarity;

public class AITItems implements ItemRegistryContainer {
	// TARDIS
	public static final Item TARDIS_ITEM = new TardisItemBuilder(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).fireproof().maxCount(1));
	public static final Item SIEGE_ITEM = new SiegeTardisItem(new OwoItemSettings().fireproof());
	// Functional Items
	public static final Item REMOTE_ITEM = new RemoteItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
	public static final Item ARTRON_COLLECTOR = new ArtronCollectorItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
	public static final Item RIFT_SCANNER = new RiftScannerItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
	public static final Item HAMMER = new HammerItem(3, -2.4F, new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).maxDamage(600));
	public static final Item RESPIRATOR = new WearableArmorItem(AITArmorMaterials.IRON, WearableArmorItem.Type.HELMET, new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).maxDamage(80), true);
	public static final Item FACELESS_RESPIRATOR = new WearableArmorItem(AITArmorMaterials.IRON, WearableArmorItem.Type.HELMET, new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).maxDamage(80), true);
	// Keys/Key Templates
	public static final Item IRON_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
	public static final Item GOLD_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), KeyItem.Protocols.SNAP);
	public static final Item NETHERITE_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).fireproof(), KeyItem.Protocols.SNAP, KeyItem.Protocols.HAIL);
	public static final Item CLASSIC_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), KeyItem.Protocols.SNAP, KeyItem.Protocols.HAIL);
	public static final Item GOLD_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), "Gold Key", "Gold Nugget");
	public static final Item NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), "Netherite Key", "Netherite Scrap");
	public static final Item CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), "Classic Key", "Amethyst Shard");
	public static final Item SONIC_SCREWDRIVER = new SonicItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

	// Crafting items
	public static final Item ZEITON_SHARD = new Item(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
	public static final Item CHARGED_ZEITON_CRYSTAL = new ChargedZeitonCrystalItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));

	// Components
	// public static final Item FLUID_LINK = new Item(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

	// Waypoint-related
	public static final Item WAYPOINT_CARTRIDGE = new WaypointItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
	public static final Item DRIFTING_MUSIC_DISC = new MusicDiscItem(1, AITSounds.DRIFTING_MUSIC, new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), 169);

	// Block controls
	public static final Item BUTTON_CONTROL = new GenericControlBlockItem(AITBlocks.BUTTON_CONTROL_BLOCK, new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
}
