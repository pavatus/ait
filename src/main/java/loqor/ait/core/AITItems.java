package loqor.ait.core;

import java.util.ArrayList;
import java.util.List;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
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
import loqor.ait.datagen.datagen_providers.util.NoEnglish;

public class AITItems implements ItemRegistryContainer {
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
    @NoEnglish
    public static final Item DRIFTING_MUSIC_DISC = new MusicDiscItem(1, AITSounds.DRIFTING_MUSIC,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), 169);
    @NoEnglish
    public static final Item MERCURY_MUSIC_DISC = new MusicDiscItem(11, AITSounds.MERCURY_MUSIC,
            new OwoItemSettings().group(AITMod.AIT_ITEM_GROUP).maxCount(1).rarity(Rarity.RARE), 216);

    // Block controls

     public static final Item REDSTONE_CONTROL = new
             GenericControlBlockItem(AITBlocks.REDSTONE_CONTROL_BLOCK, new
     OwoItemSettings().group(AITMod.AIT_ITEM_GROUP));

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
