package mdteam.ait.datagen.datagen_providers;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.AITItems;
import mdteam.ait.tardis.advancement.*;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.*;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class AITAchievementProvider extends FabricAdvancementProvider  {
    public AITAchievementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        // todo replace all literals with translatables

        Advancement placeCoral = Advancement.Builder.create()
                .display(
                        AITBlocks.CORAL_PLANT,
                        Text.literal("Gardening Guru"),
                        Text.literal("Plant the TARDIS Coral, the seed of time itself."),
                        new Identifier("textures/entity/end_portal.png"), // the background for the advancement screen
                        AdvancementFrame.TASK,
                        true,
                        true,
                        true
                )
                .criterion("place_coral", new PlaceCoralCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/root");
        Advancement firstEnter = Advancement.Builder.create()
                .parent(placeCoral)
                .display(
                        AITItems.TARDIS_ITEM,
                        Text.literal("How Does It Fit?"),
                        Text.literal("Enter the TARDIS for the first time"),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("enter_tardis", ChangedDimensionCriterion.Conditions.to(AITDimensions.TARDIS_DIM_WORLD))
                .build(consumer, AITMod.MOD_ID + "/enter_tardis"); // for now this is the root advancement, meaning its the first one that shows

        Advancement ironKey = Advancement.Builder.create()
                .parent(firstEnter)
                .display(
                        AITItems.IRON_KEY,
                        Text.literal("More than Just a Piece of Metal"),
                        Text.literal("Gain an Iron Key"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        false,
                        true
                )
                .criterion("iron_key", InventoryChangedCriterion.Conditions.items(AITItems.IRON_KEY))
                .build(consumer, AITMod.MOD_ID + "/iron_key");
        Advancement goldKey = Advancement.Builder.create()
                .parent(ironKey)
                .display(
                        AITItems.GOLD_KEY,
                        Text.literal("Golden Gatekeeper"),
                        Text.literal("Gain a Golden Key"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        false,
                        true
                )
                .criterion("gold_key", InventoryChangedCriterion.Conditions.items(AITItems.GOLD_KEY))
                .build(consumer, AITMod.MOD_ID + "/gold_key");
        Advancement netheriteKey = Advancement.Builder.create()
                .parent(goldKey)
                .display(
                        AITItems.NETHERITE_KEY,
                        Text.literal("Forged in Fire"),
                        Text.literal("Gain a Netherite Key"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        true
                )
                .criterion("netherite_key", InventoryChangedCriterion.Conditions.items(AITItems.NETHERITE_KEY))
                .build(consumer, AITMod.MOD_ID + "/netherite_key");
        Advancement classicKey = Advancement.Builder.create()
                .parent(netheriteKey)
                .display(
                        AITItems.CLASSIC_KEY,
                        Text.literal("Time Traveler's Apprentice"),
                        Text.literal("Gain a Classic Key"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        true
                )
                .criterion("classic_key", InventoryChangedCriterion.Conditions.items(AITItems.CLASSIC_KEY))
                .build(consumer, AITMod.MOD_ID + "/classic_key");

        Advancement firstDemat = Advancement.Builder.create()
                .parent(firstEnter)
                .display(
                        Items.ENDER_EYE,
                        Text.literal("Maiden Voyage"),
                        Text.literal("Successfully initiate the takeoff sequence and experience your first journey through time and space with your TARDIS."),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        true
                )
                .criterion("first_demat", new TakeOffCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/first_demat");
        Advancement firstCrash = Advancement.Builder.create()
                .parent(firstDemat)
                .display(
                        Items.TNT,
                        Text.literal("Temporal Turbulence"),
                        Text.literal("Embrace the chaos of time and space by unintentionally crashing your TARDIS for the first time."),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        true
                )
                .criterion("first_crash", new CrashCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/first_crash");
        Advancement breakGrowth = Advancement.Builder.create()
                .parent(firstEnter)
                .display(
                        Items.OAK_LEAVES,
                        Text.literal("Temporal Gardener"),
                        Text.literal("Tend to the temporal vines and foliage clinging to your TARDIS by breaking off vegetation."),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        false,
                        true
                )
                .criterion("break_growth", new BreakVegetationCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/break_growth");
    }
}
