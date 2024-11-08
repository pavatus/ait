package loqor.ait.core;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Rarity;

import loqor.ait.AITMod;
import loqor.ait.core.item.*;
import loqor.ait.core.item.blueprint.BlueprintItem;
import loqor.ait.core.item.control.GenericControlBlockItem;
import loqor.ait.core.item.link.AbstractLinkItem;
import loqor.ait.core.item.link.FluidLinkItem;
import loqor.ait.core.item.link.MercurialLinkItem;
import loqor.ait.core.item.part.MachineItem;
import loqor.ait.core.item.part.MachinePartItem;

public class AITItems implements ItemRegistryContainer {
    public static final FoodComponent ZEITON_DUST_FOOD = new FoodComponent.Builder().hunger(4).saturationModifier(0.3f)
            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1000, 3), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 500, 1), 0.5F).build();

    // TARDIS
    public static final Item TARDIS_ITEM = new TardisItemBuilder(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).fireproof().maxCount(1));
    public static final Item SIEGE_ITEM = new SiegeTardisItem(new OwoItemSettings().fireproof());

    // Functional Items
    public static final Item REMOTE_ITEM = new RemoteItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    public static final Item ARTRON_COLLECTOR = new ArtronCollectorItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    public static final Item RIFT_SCANNER = new RiftScannerItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    public static final Item HAMMER = new HammerItem(3, -2.4F,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).maxDamage(600));
    public static final Item RESPIRATOR = new RenderableArmorItem(ArmorMaterials.IRON, ArmorItem.Type.HELMET,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).maxDamage(80), true);
    public static final Item SPACESUIT_HELMET = new SpacesuitItem(ArmorMaterials.IRON, ArmorItem.Type.HELMET,
            new OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP).maxDamage(240), false);
    public static final Item SPACESUIT_CHESTPLATE = new SpacesuitItem(ArmorMaterials.IRON, ArmorItem.Type.CHESTPLATE,
            new OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP).maxDamage(240), false);
    public static final Item SPACESUIT_LEGGINGS = new SpacesuitItem(ArmorMaterials.IRON, ArmorItem.Type.LEGGINGS,
            new OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP).maxDamage(240), false);
    public static final Item SPACESUIT_BOOTS = new SpacesuitItem(ArmorMaterials.IRON, ArmorItem.Type.BOOTS,
            new OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP).maxDamage(240), false);
    public static final Item FACELESS_RESPIRATOR = new RenderableArmorItem(ArmorMaterials.IRON,
            ArmorItem.Type.HELMET, new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).maxDamage(80),
            true);
    public static final Item HYPERCUBE = new HypercubeItem(new OwoItemSettings().maxCount(1).group(AITMod.AIT_ITEM_GROUP));
    public static final Item HAZANDRA = new InteriorTeleporterItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

    // Keys/Key Templates
    public static final Item IRON_KEY = new KeyItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item GOLD_KEY = new KeyItem(
            new OwoItemSettings().rarity(Rarity.UNCOMMON).group(AITMod.AIT_ITEM_GROUP), KeyItem.Protocols.SNAP);
    public static final Item NETHERITE_KEY = new KeyItem(
            new OwoItemSettings().rarity(Rarity.RARE).group(AITMod.AIT_ITEM_GROUP).fireproof(), KeyItem.Protocols.SNAP,
            KeyItem.Protocols.HAIL);
    public static final Item CLASSIC_KEY = new KeyItem(
            new OwoItemSettings().rarity(Rarity.EPIC).group(AITMod.AIT_ITEM_GROUP), KeyItem.Protocols.SNAP,
            KeyItem.Protocols.HAIL);

    // Creative only skeleton key
    public static final Item SKELETON_KEY = new KeyItem(
            new OwoItemSettings().rarity(Rarity.EPIC).group(AITMod.AIT_ITEM_GROUP), KeyItem.Protocols.SKELETON,
            KeyItem.Protocols.HAIL, KeyItem.Protocols.SNAP);

    public static final Item GOLD_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), "Gold Key", "Gold Nugget");
    public static final Item NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), "Netherite Key", "Netherite Scrap");
    public static final Item CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), "Classic Key", "Amethyst Shard");
    public static final Item SONIC_SCREWDRIVER = new SonicItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

    // Crafting items
    public static final Item ZEITON_SHARD = new Item(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item CHARGED_ZEITON_CRYSTAL = new ChargedZeitonCrystalItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    public static final Item ZEITON_DUST = new Item(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).food(ZEITON_DUST_FOOD));

    // Machine parts
    public static final Item ARTRON_FLUID_LINK = new FluidLinkItem(AbstractLinkItem.Type.ARTRON,
            new OwoItemSettings() /* .group(AITMod.AIT_ITEM_GROUP) */);
    public static final Item DATA_FLUID_LINK = new FluidLinkItem(AbstractLinkItem.Type.DATA,
            new OwoItemSettings() /* .group(AITMod.AIT_ITEM_GROUP) */);
    public static final Item VORTEX_FLUID_LINK = new FluidLinkItem(AbstractLinkItem.Type.VORTEX,
            new OwoItemSettings() /* .group(AITMod.AIT_ITEM_GROUP) */);

    public static final Item ARTRON_MERCURIAL_LINK = new MercurialLinkItem(AbstractLinkItem.Type.ARTRON,
            new OwoItemSettings() /* .group(AITMod.AIT_ITEM_GROUP) */);
    public static final Item DATA_MERCURIAL_LINK = new MercurialLinkItem(AbstractLinkItem.Type.DATA,
            new OwoItemSettings() /* .group(AITMod.AIT_ITEM_GROUP) */);
    public static final Item VORTEX_MERCURIAL_LINK = new MercurialLinkItem(AbstractLinkItem.Type.VORTEX,
            new OwoItemSettings() /* .group(AITMod.AIT_ITEM_GROUP) */);

    public static final Item CONDENSER = new MachinePartItem(MachinePartItem.Type.CONDENSER,
            new OwoItemSettings() /* .group(AITMod.AIT_ITEM_GROUP) */);
    public static final Item MANIPULATOR = new MachinePartItem(MachinePartItem.Type.MANIPULATOR,
            new OwoItemSettings() /* .group(AITMod.AIT_ITEM_GROUP) */);
    public static final Item BULB = new MachinePartItem(MachinePartItem.Type.BULB,
            new OwoItemSettings() /* .group(AITMod.AIT_ITEM_GROUP) */);
    public static final Item INDUCTOR = new MachinePartItem(MachinePartItem.Type.INDUCTOR,
            new OwoItemSettings() /* .group(AITMod.AIT_ITEM_GROUP) */);

    // Components
    public static final Item DEMATERIALIZATION_CIRCUIT = new MachineItem(
            new OwoItemSettings() /* .group(AITMod.AIT_ITEM_GROUP) */);

    // Blueprint
    public static final Item BLUEPRINT = new BlueprintItem(
            new OwoItemSettings() /* .group(AITMod.AIT_ITEM_GROUP) */.rarity(Rarity.EPIC));

    // Waypoint-related
    public static final Item WAYPOINT_CARTRIDGE = new WaypointItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));

    // Music discs
    public static final Item DRIFTING_MUSIC_DISC = new MusicDiscItem(1, AITSounds.DRIFTING_MUSIC,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), 169);
    public static final Item MERCURY_MUSIC_DISC = new MusicDiscItem(11, AITSounds.MERCURY_MUSIC,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), 216);

    // Block controls

     public static final Item REDSTONE_CONTROL = new
             GenericControlBlockItem(AITBlocks.REDSTONE_CONTROL_BLOCK, new
     OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

     // Mars

    // Martian Stone

    public static final Item MARTIAN_SAND = new
            BlockItem(AITBlocks.MARTIAN_SAND, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_STONE = new
            BlockItem(AITBlocks.MARTIAN_STONE, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_STONE_SLAB = new
            BlockItem(AITBlocks.MARTIAN_STONE_SLAB, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_STONE_WALL = new
            BlockItem(AITBlocks.MARTIAN_STONE_WALL, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_STONE_STAIRS = new
            BlockItem(AITBlocks.MARTIAN_STONE_STAIRS, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_STONE_BUTTON = new
            BlockItem(AITBlocks.MARTIAN_STONE_BUTTON, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_STONE_PRESSURE_PLATE = new
            BlockItem(AITBlocks.MARTIAN_STONE_PRESSURE_PLATE, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    // Martian Bricks
    public static final Item MARTIAN_BRICKS = new
            BlockItem(AITBlocks.MARTIAN_BRICKS, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_BRICK_SLAB = new
            BlockItem(AITBlocks.MARTIAN_BRICK_SLAB, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_BRICK_WALL = new
            BlockItem(AITBlocks.MARTIAN_BRICK_WALL, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_BRICK_STAIRS = new
            BlockItem(AITBlocks.MARTIAN_BRICK_STAIRS, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    // Martian Cobblestone
    public static final Item MARTIAN_COBBLESTONE = new
            BlockItem(AITBlocks.MARTIAN_COBBLESTONE, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_COBBLESTONE_WALL = new
            BlockItem(AITBlocks.MARTIAN_COBBLESTONE_WALL, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_COBBLESTONE_STAIRS = new
            BlockItem(AITBlocks.MARTIAN_COBBLESTONE_STAIRS, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_COBBLESTONE_SLAB = new
            BlockItem(AITBlocks.MARTIAN_COBBLESTONE_SLAB, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item MARTIAN_PILLAR = new
            BlockItem(AITBlocks.MARTIAN_PILLAR, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item CHISELED_MARTIAN_STONE = new
            BlockItem(AITBlocks.CHISELED_MARTIAN_STONE, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item CRACKED_MARTIAN_BRICKS = new
            BlockItem(AITBlocks.CRACKED_MARTIAN_BRICKS, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    // Polished Martian Stone

    public static final Item POLISHED_MARTIAN_STONE = new
            BlockItem(AITBlocks.POLISHED_MARTIAN_STONE, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item POLISHED_MARTIAN_STONE_STAIRS = new
            BlockItem(AITBlocks.POLISHED_MARTIAN_STONE_STAIRS, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item POLISHED_MARTIAN_STONE_SLAB = new
            BlockItem(AITBlocks.POLISHED_MARTIAN_STONE_SLAB, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    // Smooth Martian Stone

    public static final Item SMOOTH_MARTIAN_STONE = new
            BlockItem(AITBlocks.SMOOTH_MARTIAN_STONE, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item SMOOTH_MARTIAN_STONE_SLAB = new
            BlockItem(AITBlocks.SMOOTH_MARTIAN_STONE_SLAB, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    // Mars

    // Anorthosite Stone

    public static final Item REGOLITH = new
            BlockItem(AITBlocks.REGOLITH, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item ANORTHOSITE = new
            BlockItem(AITBlocks.ANORTHOSITE, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item ANORTHOSITE_SLAB = new
            BlockItem(AITBlocks.ANORTHOSITE_SLAB, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item ANORTHOSITE_WALL = new
            BlockItem(AITBlocks.ANORTHOSITE_WALL, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item ANORTHOSITE_STAIRS = new
            BlockItem(AITBlocks.ANORTHOSITE_STAIRS, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));


    // Anorthosite Bricks
    public static final Item ANORTHOSITE_BRICKS = new
            BlockItem(AITBlocks.ANORTHOSITE_BRICKS, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item ANORTHOSITE_BRICK_SLAB = new
            BlockItem(AITBlocks.ANORTHOSITE_BRICK_SLAB, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item ANORTHOSITE_BRICK_WALL = new
            BlockItem(AITBlocks.ANORTHOSITE_BRICK_WALL, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item ANORTHOSITE_BRICK_STAIRS = new
            BlockItem(AITBlocks.ANORTHOSITE_BRICK_STAIRS, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item ANORTHOSITE_PILLAR = new
            BlockItem(AITBlocks.ANORTHOSITE_PILLAR, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item CHISELED_ANORTHOSITE_STONE = new
            BlockItem(AITBlocks.CHISELED_ANORTHOSITE, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item CRACKED_ANORTHOSITE_BRICKS = new
            BlockItem(AITBlocks.CRACKED_ANORTHOSITE_BRICKS, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    // Polished Anorthosite Stone

    public static final Item POLISHED_ANORTHOSITE_STONE = new
            BlockItem(AITBlocks.POLISHED_ANORTHOSITE, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item POLISHED_ANORTHOSITE_STONE_STAIRS = new
            BlockItem(AITBlocks.POLISHED_ANORTHOSITE_STAIRS, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item POLISHED_ANORTHOSITE_STONE_SLAB = new
            BlockItem(AITBlocks.POLISHED_ANORTHOSITE_SLAB, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    // Smooth Anorthosite Stone

    public static final Item SMOOTH_ANORTHOSITE_STONE = new
            BlockItem(AITBlocks.SMOOTH_ANORTHOSITE, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

    public static final Item SMOOTH_ANORTHOSITE_STONE_SLAB = new
            BlockItem(AITBlocks.SMOOTH_ANORTHOSITE_SLAB, new
            OwoItemSettings().group(AITMod.AIT_PLANETS_ITEM_GROUP));

}
