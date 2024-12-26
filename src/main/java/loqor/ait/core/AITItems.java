package loqor.ait.core;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.item.Item;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import loqor.ait.AITMod;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.item.SubSystemItem;
import loqor.ait.core.item.*;
import loqor.ait.core.item.blueprint.BlueprintItem;
import loqor.ait.core.item.link.AbstractLinkItem;
import loqor.ait.core.item.link.FluidLinkItem;
import loqor.ait.core.item.link.MercurialLinkItem;
import loqor.ait.core.item.part.MachinePartItem;
import loqor.ait.datagen.datagen_providers.util.NoEnglish;



public class AITItems {
    public static final FoodComponent ZEITON_DUST_FOOD = new FoodComponent.Builder()
            .nutrition(4)
            .saturationModifier(0.3f)
            .alwaysEdible().build();

    public static final ConsumableComponent ZEITON_DUST_FOOD_EFFECT = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.SPEED, 1000, 3), 1.0F))
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(AITStatusEffects.ZEITON_HIGH, 500, 1), 1.0F))
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.WITHER, 500, 1), 0.5F))
            .build();

    // TARDIS
    @NoEnglish
    public static final Item TARDIS_ITEM = registerItem("tardis_item", new TardisItemBuilder(
            new Item.Settings().fireproof().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "tardis_item")))));


    @NoEnglish
    public static final Item SIEGE_ITEM = registerItem("siege_item", new SiegeTardisItem(new Item.Settings().fireproof().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "siege_item"))))
    );

    /*
    // Functional Items
    @NoEnglish
    public static final Item REMOTE_ITEM = new RemoteItem(
            new Item.Settings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    @NoEnglish
    public static final Item ARTRON_COLLECTOR = new ArtronCollectorItem(
            new Item.Settings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    public static final Item RIFT_SCANNER = new RiftScannerItem(
            new Item.Settings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    public static final Item HAMMER = new HammerItem(3, -2.4F,
            new Item.Settings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).maxDamage(600));
    public static final Item RESPIRATOR = new RenderableArmorItem(ArmorMaterials.IRON, ArmorItem.Type.HELMET,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).maxDamage(80), true);;
    public static final Item FACELESS_RESPIRATOR = new RenderableArmorItem(ArmorMaterials.IRON,
            ArmorItem.Type.HELMET, new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).maxDamage(80),
            true);
    public static final Item HYPERCUBE = new HypercubeItem(new OwoItemSettings().maxCount(1).group(AITMod.AIT_ITEM_GROUP));
    public static final Item HAZANDRA = new InteriorTeleporterItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
*/
    // Keys/Key Templates
    public static final Item IRON_KEY = registerItem("iron_key", new KeyItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "iron_key")))));
    public static final Item GOLD_KEY = registerItem("gold_key", new KeyItem(
            new Item.Settings().rarity(Rarity.UNCOMMON).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "gold_key"))), KeyItem.Protocols.SNAP));
    public static final Item NETHERITE_KEY = registerItem("netherite_key", new KeyItem(
            new Item.Settings().rarity(Rarity.RARE).fireproof().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "netherite_key"))), KeyItem.Protocols.SNAP,
            KeyItem.Protocols.HAIL));
    public static final Item CLASSIC_KEY = registerItem("classic_key", new KeyItem(
            new Item.Settings().rarity(Rarity.EPIC).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "classic_key"))), KeyItem.Protocols.SNAP,
            KeyItem.Protocols.HAIL));

    // Creative only skeleton key
    public static final Item SKELETON_KEY = registerItem("skeleton_key_upgrade_smithing_template", new KeyItem(
            new Item.Settings().rarity(Rarity.EPIC).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "skeleton_key_upgrade_smithing_template"))), KeyItem.Protocols.SKELETON,
            KeyItem.Protocols.HAIL, KeyItem.Protocols.SNAP));
    @NoEnglish
    public static final Item GOLD_KEY_UPGRADE_SMITHING_TEMPLATE = registerItem("gold_key_upgrade_smithing_template", new KeySmithingTemplateItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "gold_key_upgrade_smithing_template"))), "Gold Key", "Gold Nugget"));
    @NoEnglish
    public static final Item NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE = registerItem("netherite_key_upgrade_smithing_template", new KeySmithingTemplateItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "netherite_key_upgrade_smithing_template"))), "Netherite Key", "Netherite Scrap"));
    @NoEnglish
    public static final Item CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE = registerItem("classic_key_upgrade_smithing_template", new KeySmithingTemplateItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "classic_key_upgrade_smithing_template"))), "Classic Key", "Amethyst Shard"));
    public static final Item SONIC_SCREWDRIVER = registerItem("sonic_screwdriver", new SonicItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "sonic_screwdriver")))));

    // Crafting items
    public static final Item ZEITON_SHARD = registerItem("zeiton_shard", new ZeitonShardItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "zeiton_shard")))));
    public static final Item CHARGED_ZEITON_CRYSTAL = registerItem("charged_zeiton_crystal", new ChargedZeitonCrystalItem(
            new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "charged_zeiton_crystal")))));
    public static final Item ZEITON_DUST = registerItem("zeiton_dust", new Item(
            new Item.Settings().food(ZEITON_DUST_FOOD, ZEITON_DUST_FOOD_EFFECT).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "zeiton_dust")))));

    // Machine parts
    public static final Item ARTRON_FLUID_LINK = registerItem("artron_fluid_link", new FluidLinkItem(AbstractLinkItem.Type.ARTRON,
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "artron_fluid_link"))) /* .group(AITMod.AIT_ITEM_GROUP) */));
    public static final Item DATA_FLUID_LINK = registerItem("data_fluid_link", new FluidLinkItem(AbstractLinkItem.Type.DATA,
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "data_fluid_link"))) /* .group(AITMod.AIT_ITEM_GROUP) */));
    public static final Item VORTEX_FLUID_LINK = registerItem("vortex_fluid_link", new FluidLinkItem(AbstractLinkItem.Type.VORTEX,
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "vortex_fluid_link"))) /* .group(AITMod.AIT_ITEM_GROUP) */));

    public static final Item ARTRON_MERCURIAL_LINK = registerItem("artron_mercurial_link", new MercurialLinkItem(AbstractLinkItem.Type.ARTRON,
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "artron_mercurial_link"))) /* .group(AITMod.AIT_ITEM_GROUP) */));
    public static final Item DATA_MERCURIAL_LINK = registerItem("data_mercurial_link", new MercurialLinkItem(AbstractLinkItem.Type.DATA,
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "data_mercurial_link"))) /* .group(AITMod.AIT_ITEM_GROUP) */));
    public static final Item VORTEX_MERCURIAL_LINK = registerItem("vortex_mercurial_link", new MercurialLinkItem(AbstractLinkItem.Type.VORTEX,
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "vortex_mercurial_link"))) /* .group(AITMod.AIT_ITEM_GROUP) */));

    public static final Item CONDENSER = registerItem("condenser", new MachinePartItem(MachinePartItem.Type.CONDENSER,
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "condenser"))) /* .group(AITMod.AIT_ITEM_GROUP) */));
    public static final Item MANIPULATOR = registerItem("manipulator", new MachinePartItem(MachinePartItem.Type.MANIPULATOR,
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "manipulator"))) /* .group(AITMod.AIT_ITEM_GROUP) */));
    public static final Item BULB = registerItem("bulb", new MachinePartItem(MachinePartItem.Type.BULB,
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "bulb"))) /* .group(AITMod.AIT_ITEM_GROUP) */));
    public static final Item INDUCTOR = registerItem("inductor", new MachinePartItem(MachinePartItem.Type.INDUCTOR,
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "inductor"))) /* .group(AITMod.AIT_ITEM_GROUP) */));

    // Components
    public static final Item DEMATERIALIZATION_CIRCUIT = new SubSystemItem(
            new Item.Settings(), SubSystem.Id.DEMAT);

    public static final Item SHIELDS_CIRCUIT = registerItem("shields", new SubSystemItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "shields"))), SubSystem.Id.SHIELDS));
    public static final Item BACKUP_CIRCUIT = registerItem("backup_circuit", new SubSystemItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "backup_circuit"))), SubSystem.Id.EMERGENCY_POWER));
    public static final Item GRAVITATIONAL_CIRCUIT = registerItem("gravitational_circuit", new SubSystemItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "gravitational_circuit"))), SubSystem.Id.GRAVITATIONAL));
    public static final Item CHAMELEON_CIRCUIT = registerItem("chameleon_circuit", new SubSystemItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "chameleon_circuit"))), SubSystem.Id.CHAMELEON));
    public static final Item DESPERATION_CIRCUIT = registerItem("desperation_circuit", new SubSystemItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "desperation_circuit"))), SubSystem.Id.DESPERATION));
    public static final Item STABILISERS = registerItem("stabilisers", new SubSystemItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "stabilisers"))), SubSystem.Id.STABILISERS));
    public static final Item LIFE_SUPPORT = registerItem("life_support", new SubSystemItem(
            new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "life_support"))), SubSystem.Id.LIFE_SUPPORT));



    // Blueprint
    public static final Item BLUEPRINT = registerItem("blueprint", new BlueprintItem(
            new Item.Settings().rarity(Rarity.EPIC).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "blueprint")))));

    // Waypoint-related
    public static final Item WAYPOINT_CARTRIDGE = registerItem("waypoint_cartridge", new WaypointItem(
            new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "waypoint_cartridge")))));
/*
    // Music discs
    @NoEnglish
    public static final Item DRIFTING_MUSIC_DISC = new MusicDiscItem(1, AITSounds.DRIFTING_MUSIC,
            new Item.Settings().maxCount(1).rarity(Rarity.RARE), 169);

    @NoEnglish
    public static final Item MERCURY_MUSIC_DISC = new MusicDiscItem(11, AITSounds.ERROR,
            new Item.Settings().maxCount(1).rarity(Rarity.RARE), 216);
*/

    // Block controls
/*
     public static final Item REDSTONE_CONTROL = new
             GenericControlBlockItem(AITBlocks.REDSTONE_CONTROL_BLOCK,
     new Item.Settings().group(AITMod.AIT_ITEM_GROUP));
*/

    //TODO: Find another way to register AIT Items automatically, Minecraft Made it worse so we can't register like we used to since they are deprecated in owolib +
    // .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AITMod.MOD_ID, "<item>"))) has to be added every item (way to do automatically?)

    //TODO: Owo depricated the Owo.ItemSettings but theres a replacement for it so we can do .group() buttttttt i'm not gonna do it rn

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(AITMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        AITMod.LOGGER.info("Registering AIT's Items");
    }

    public static List<Item> get() {
        List<Item> list = new ArrayList<>();

        for (Item item : Registries.ITEM) {
            if (Registries.ITEM.getId(item).getNamespace().equalsIgnoreCase(AITMod.MOD_ID)) {
                list.add(item);
            }
        }

        return list;
    }
}
