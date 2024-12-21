package loqor.ait.core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;

import loqor.ait.AITMod;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.item.SubSystemItem;
import loqor.ait.core.item.*;
import loqor.ait.core.item.blueprint.BlueprintItem;
import loqor.ait.core.item.blueprint.BlueprintRegistry;
import loqor.ait.core.item.blueprint.BlueprintSchema;
import loqor.ait.core.item.control.GenericControlBlockItem;
import loqor.ait.core.item.link.AbstractLinkItem;
import loqor.ait.core.item.link.FluidLinkItem;
import loqor.ait.core.item.link.MercurialLinkItem;
import loqor.ait.core.item.part.MachinePartItem;
import loqor.ait.datagen.datagen_providers.util.NoEnglish;


public class AITItems implements ItemRegistryContainer {

    // TODO ADVENT ITEMS GO UP HERE AND DECLARED IN THE STATIC METHOD AT THE BOTTOM
    public static Item COBBLED_SNOWBALL;
    public static Item HOT_CHOCOLATE_POWDER;
    public static Item HOT_CHOCOLATE;
    public static Item MUG;
    public static final FoodComponent ZEITON_DUST_FOOD = new FoodComponent.Builder().hunger(4).saturationModifier(0.3f)
            .statusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1000, 3), 1.0F)
            .statusEffect(new StatusEffectInstance(AITStatusEffects.ZEITON_HIGH, 500, 1), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.WITHER, 500, 1), 0.5F)
            .build();

    // TARDIS
    @NoEnglish
    public static final Item TARDIS_ITEM = new TardisItemBuilder(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).fireproof().maxCount(1));
    @NoEnglish
    public static final Item SIEGE_ITEM = new SiegeTardisItem(new OwoItemSettings().fireproof());

    // Functional Items
    @NoEnglish
    public static final Item REMOTE_ITEM = new RemoteItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    @NoEnglish
    public static final Item ARTRON_COLLECTOR = new ArtronCollectorItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    public static final Item RIFT_SCANNER = new RiftScannerItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    public static final Item HAMMER = new HammerItem(3, -2.4F,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).maxDamage(600));
    public static final Item RESPIRATOR = new RenderableArmorItem(ArmorMaterials.IRON, ArmorItem.Type.HELMET,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).maxDamage(80), true);;
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
    @NoEnglish
    public static final Item GOLD_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), "Gold Key", "Gold Nugget");
    @NoEnglish
    public static final Item NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), "Netherite Key", "Netherite Scrap");
    @NoEnglish
    public static final Item CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE = new KeySmithingTemplateItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), "Classic Key", "Amethyst Shard");
    public static final Item SONIC_SCREWDRIVER = new SonicItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

    // Crafting items
    public static final Item ZEITON_SHARD = new ZeitonShardItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item CHARGED_ZEITON_CRYSTAL = new ChargedZeitonCrystalItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));
    public static final Item ZEITON_DUST = new Item(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).food(ZEITON_DUST_FOOD));

    // Machine parts
    public static final Item ARTRON_FLUID_LINK = new FluidLinkItem(AbstractLinkItem.Type.ARTRON,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item DATA_FLUID_LINK = new FluidLinkItem(AbstractLinkItem.Type.DATA,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item VORTEX_FLUID_LINK = new FluidLinkItem(AbstractLinkItem.Type.VORTEX,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

    public static final Item ARTRON_MERCURIAL_LINK = new MercurialLinkItem(AbstractLinkItem.Type.ARTRON,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item DATA_MERCURIAL_LINK = new MercurialLinkItem(AbstractLinkItem.Type.DATA,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item VORTEX_MERCURIAL_LINK = new MercurialLinkItem(AbstractLinkItem.Type.VORTEX,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

    public static final Item CONDENSER = new MachinePartItem(MachinePartItem.Type.CONDENSER,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item MANIPULATOR = new MachinePartItem(MachinePartItem.Type.MANIPULATOR,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
    public static final Item BULB = new MachinePartItem(MachinePartItem.Type.BULB,
            new OwoItemSettings() .group(AITMod.AIT_ITEM_GROUP));
    public static final Item INDUCTOR = new MachinePartItem(MachinePartItem.Type.INDUCTOR,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

    // Components
    public static final Item DEMATERIALIZATION_CIRCUIT = new SubSystemItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), SubSystem.Id.DEMAT);

    public static final Item SHIELDS_CIRCUIT = new SubSystemItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), SubSystem.Id.SHIELDS);
    public static final Item BACKUP_CIRCUIT = new SubSystemItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), SubSystem.Id.EMERGENCY_POWER);
    public static final Item GRAVITATIONAL_CIRCUIT = new SubSystemItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), SubSystem.Id.GRAVITATIONAL);
    public static final Item CHAMELEON_CIRCUIT = new SubSystemItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), SubSystem.Id.CHAMELEON);
    public static final Item DESPERATION_CIRCUIT = new SubSystemItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), SubSystem.Id.DESPERATION);
    public static final Item STABILISERS = new SubSystemItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), SubSystem.Id.STABILISERS);
    public static final Item LIFE_SUPPORT = new SubSystemItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP), SubSystem.Id.LIFE_SUPPORT);

    @NoEnglish
    public static final Item GALLIFREY_FALLS_PAINTING = new AITDecorationItem(AITEntityTypes.GALLIFREY_FALLS_PAINTING_TYPE, new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

    // Blueprint
    public static final Item BLUEPRINT = new BlueprintItem(
            new OwoItemSettings().rarity(Rarity.EPIC));

    // Waypoint-related
    public static final Item WAYPOINT_CARTRIDGE = new WaypointItem(
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1));

    // Music discs
    @NoEnglish
    public static final Item DRIFTING_MUSIC_DISC = new MusicDiscItem(1, AITSounds.DRIFTING_MUSIC,
            new OwoItemSettings().maxCount(1).rarity(Rarity.RARE), 169);

    @NoEnglish
    public static final Item WONDERFUL_TIME_IN_SPACE = new MusicDiscItem(1, AITSounds.WONDERFUL_TIME_IN_SPACE,
            new OwoItemSettings().maxCount(1).rarity(Rarity.RARE), 73);

    @NoEnglish
    public static final Item MERCURY_MUSIC_DISC = new MusicDiscItem(11, AITSounds.ERROR,
            new OwoItemSettings().maxCount(1).rarity(Rarity.RARE), 216);


    // Block controls

     public static final Item REDSTONE_CONTROL = new
             GenericControlBlockItem(AITBlocks.REDSTONE_CONTROL_BLOCK, new
     OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

     // TODO ADVENT STUFF

    static {
        /*if (isUnlockedOnThisDay(Calendar.DECEMBER, 27)) {
            // TODO FESTIVE SANTA RESPIRATOR
        }*/
        if (isUnlockedOnThisDay(Calendar.DECEMBER, 29)) {
            COBBLED_SNOWBALL = new CobbledSnowballItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(16));
        }
        if (isUnlockedOnThisDay(Calendar.JANUARY, 2)) {
            HOT_CHOCOLATE_POWDER = new Item(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).food(ZEITON_DUST_FOOD));
            HOT_CHOCOLATE = new HotChocolateItem(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
            MUG = new Item(new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));
        }
        /*if (isUnlockedOnThisDay(Calendar.JANUARY,4)) {
            // TODO SONIC CANDY CANE
        }
        if (isUnlockedOnThisDay(Calendar.JANUARY,5)) {
           // TODO FESTIVE ICY EXTERIOR, SOMETHING EXTRA?
        }
        if (isUnlockedAfterAdvent2024()) {
            // TODO DALEK MOD EXTERIOR CROSSOVER
        }*/
    }

    public static boolean isUnlockedOnThisDay(int month, int day) {
        return getAdventDates(month, Calendar.JANUARY, day, 6);
    }

    public static boolean isUnlockedAfterAdvent2024() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DATE);

        return year >= 2025 && day >= 6;
    }

    public static boolean getAdventDates(int monthBegin, int monthEnd, int dayBegin, int dayEnd) {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Handle the case where the date range spans across two years
        if (monthBegin == Calendar.DECEMBER && monthEnd == Calendar.JANUARY) {
            return (currentMonth == Calendar.DECEMBER && currentDay >= dayBegin) || (currentMonth == Calendar.JANUARY && currentDay <= dayEnd);
        } else {
            return currentMonth >= monthBegin && currentMonth <= monthEnd && currentDay >= dayBegin && currentDay <= dayEnd;
        }
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

    static {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.addAfter(Items.MUSIC_DISC_RELIC, DRIFTING_MUSIC_DISC);
            entries.addAfter(DRIFTING_MUSIC_DISC, MERCURY_MUSIC_DISC);
        });

        ItemGroupEvents.modifyEntriesEvent(RegistryKey.of(RegistryKeys.ITEM_GROUP, AITMod.AIT_ITEM_GROUP.id())).register(entries -> {
            for (BlueprintSchema schema : BlueprintRegistry.getInstance().toList()) {
                entries.add(BlueprintItem.createStack(schema));
            }
        });
    }
}
