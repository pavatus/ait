package loqor.ait.datagen.datagen_providers;

import java.util.function.Consumer;

import dev.pavatus.planet.core.PlanetBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ChangedDimensionCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITItems;
import loqor.ait.core.advancement.*;

public class AITAchievementProvider extends FabricAdvancementProvider {
    public AITAchievementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        // todo replace all literals with translatables
        // todo planet advancements need to be moved to the planet module butttt i cannot be bothered *shrug*

        Advancement Mars = Advancement.Builder.create()
                .display(
                        PlanetBlocks.MARTIAN_SAND,
                        Text.literal("Mars"),
                        Text.literal("Landed on Mars for the first time"),
                        new Identifier(AITMod.MOD_ID, "textures/block/martian_stone.png"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        true
                )
                .criterion(
                        "mars",
                        ChangedDimensionCriterion.Conditions.to(
                                RegistryKey.of(
                                        RegistryKeys.WORLD,
                                        new Identifier(AITMod.MOD_ID, "mars")
                                )
                        )
                )
                .build(consumer, AITMod.MOD_ID + "/mars_root");

        Advancement Moon = Advancement.Builder.create()
                .display(
                        PlanetBlocks.REGOLITH,
                        Text.literal("Moon"),
                        Text.literal("Landed on the Moon for the first time"),
                        new Identifier("textures/block/regolith.png"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        true
                )
                .criterion(
                        "moon",
                        ChangedDimensionCriterion.Conditions.to(
                                RegistryKey.of(
                                        RegistryKeys.WORLD,
                                        new Identifier(AITMod.MOD_ID, "moon")
                                )
                        )
                )
                .build(consumer, AITMod.MOD_ID + "/moon_root");

        Advancement landOnMars = Advancement.Builder.create()
                .display(
                    PlanetBlocks.MARTIAN_STONE,
                        Text.literal("You were not the first."),
                        Text.literal("Landed on Mars for the first time"),
                        new Identifier(AITMod.MOD_ID, "textures/block/martian_stone.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        true
                )
                .criterion(
                        "enter_mars",
                        ChangedDimensionCriterion.Conditions.to(
                                RegistryKey.of(
                                        RegistryKeys.WORLD,
                                        new Identifier(AITMod.MOD_ID, "mars")
                                )
                        )
                )
                .build(consumer, AITMod.MOD_ID + "/enter_mars");

        Advancement landOnMoon = Advancement.Builder.create()
                .display(
                    PlanetBlocks.ANORTHOSITE,
                        Text.literal("One small step for Time Lords"),
                        Text.literal("Landed on the Moon for the first time"),
                        new Identifier("textures/block/regolith.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        true
                )
                .criterion(
                        "enter_moon",
                        ChangedDimensionCriterion.Conditions.to(
                                RegistryKey.of(
                                        RegistryKeys.WORLD,
                                        new Identifier(AITMod.MOD_ID, "moon")
                                )
                        )
                )
                .build(consumer, AITMod.MOD_ID + "/enter_moon");

        Advancement placeCoral = Advancement.Builder.create()
                .display(AITBlocks.CORAL_PLANT, Text.literal("Gardening Guru"),
                        Text.literal("Plant the TARDIS Coral, the seed of time itself."),
                        new Identifier("textures/entity/end_portal.png"), // the background for the
                        // advancement screen
                        AdvancementFrame.TASK, true, true, true)
                .criterion("place_coral", new PlaceCoralCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/root");
        Advancement firstEnter = Advancement.Builder.create().parent(placeCoral)
                .display(AITItems.TARDIS_ITEM, Text.literal("How Does It Fit?"),
                        Text.literal("Enter the TARDIS for the first time"), null, AdvancementFrame.CHALLENGE, true,
                        true, false)
                .criterion("enter_tardis", new EnterTardisCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/enter_tardis"); // for now this is the root advancement, meaning
        // its the first
        // one
        // that shows

        Advancement ironKey = Advancement.Builder.create().parent(firstEnter)
                .display(AITItems.IRON_KEY, Text.literal("More than Just a Piece of Metal"),
                        Text.literal("Gain an Iron Key"), null, AdvancementFrame.TASK, true, false, true)
                .criterion("iron_key", InventoryChangedCriterion.Conditions.items(AITItems.IRON_KEY))
                .build(consumer, AITMod.MOD_ID + "/iron_key");
        Advancement goldKey = Advancement.Builder.create().parent(ironKey)
                .display(AITItems.GOLD_KEY, Text.literal("Golden Gatekeeper"), Text.literal("Gain a Golden Key"), null,
                        AdvancementFrame.TASK, true, false, true)
                .criterion("gold_key", InventoryChangedCriterion.Conditions.items(AITItems.GOLD_KEY))
                .build(consumer, AITMod.MOD_ID + "/gold_key");
        Advancement netheriteKey = Advancement.Builder.create().parent(goldKey)
                .display(AITItems.NETHERITE_KEY, Text.literal("Forged in Fire"), Text.literal("Gain a Netherite Key"),
                        null, AdvancementFrame.TASK, true, true, true)
                .criterion("netherite_key", InventoryChangedCriterion.Conditions.items(AITItems.NETHERITE_KEY))
                .build(consumer, AITMod.MOD_ID + "/netherite_key");
        Advancement classicKey = Advancement.Builder.create().parent(netheriteKey)
                .display(AITItems.CLASSIC_KEY, Text.literal("Time Traveler's Apprentice"),
                        Text.literal("Gain a Classic Key"), null, AdvancementFrame.TASK, true, true, true)
                .criterion("classic_key", InventoryChangedCriterion.Conditions.items(AITItems.CLASSIC_KEY))
                .build(consumer, AITMod.MOD_ID + "/classic_key");

        Advancement firstDemat = Advancement.Builder.create().parent(firstEnter)
                .display(Items.ENDER_EYE, Text.literal("Maiden Voyage"), Text.literal(
                        "Successfully initiate the takeoff sequence and experience your first journey through time and space with your TARDIS."),
                        null, AdvancementFrame.CHALLENGE, true, true, true)
                .criterion("first_demat", new TakeOffCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/first_demat");
        Advancement firstCrash = Advancement.Builder.create().parent(firstDemat)
                .display(Items.TNT, Text.literal("Temporal Turbulence"), Text.literal(
                        "Embrace the chaos of time and space by unintentionally crashing your TARDIS for the first time."),
                        null, AdvancementFrame.CHALLENGE, true, true, true)
                .criterion("first_crash", new CrashCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/first_crash");
        Advancement breakGrowth = Advancement.Builder.create().parent(firstEnter)
                .display(Items.OAK_LEAVES, Text.literal("Temporal Gardener"), Text.literal(
                        "Tend to the temporal vines and foliage clinging to your TARDIS by breaking off vegetation."),
                        null, AdvancementFrame.TASK, true, false, true)
                .criterion("break_growth", new BreakVegetationCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/break_growth");
    }


}
