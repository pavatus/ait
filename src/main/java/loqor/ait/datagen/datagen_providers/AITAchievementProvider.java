package loqor.ait.datagen.datagen_providers;

import java.util.function.Consumer;

import dev.pavatus.module.ModuleRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Items;
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

        ModuleRegistry.instance().iterator().forEachRemaining(module -> module.getDataGenerator().ifPresent(dataGenerator -> {
            dataGenerator.advancements(consumer);
        }));

        Advancement placeCoral = Advancement.Builder.create()
                .display(AITBlocks.CORAL_PLANT, Text.translatable("achievement.ait.title.place_coral"),
                        Text.translatable("achievement.ait.description.place_coral"),
                        new Identifier("textures/entity/end_portal.png"), // the background for the
                        // advancement screen
                        AdvancementFrame.TASK, true, true, true)
                .criterion("place_coral", new PlaceCoralCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/root");

        Advancement firstEnter = Advancement.Builder.create().parent(placeCoral)
                .display(AITItems.TARDIS_ITEM, Text.translatable("achievement.ait.title.enter_tardis"),
                        Text.translatable("achievement.ait.description.enter_tardis"), null, AdvancementFrame.CHALLENGE, true,
                        true, false)
                .criterion("enter_tardis", new EnterTardisCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/enter_tardis"); // for now this is the root advancement, meaning
        // its the first
        // one
        // that shows

        Advancement ironKey = Advancement.Builder.create().parent(firstEnter)
                .display(AITItems.IRON_KEY, Text.translatable("achievement.ait.title.iron_key"),
                        Text.translatable("achievement.ait.description.iron_key"), null, AdvancementFrame.TASK, true, false, true)
                .criterion("iron_key", InventoryChangedCriterion.Conditions.items(AITItems.IRON_KEY))
                .build(consumer, AITMod.MOD_ID + "/iron_key");

        Advancement goldKey = Advancement.Builder.create().parent(ironKey)
                .display(AITItems.GOLD_KEY, Text.translatable("achievement.ait.title.gold_key"), Text.translatable("achievement.ait.description.gold_key"), null,
                        AdvancementFrame.TASK, true, false, true)
                .criterion("gold_key", InventoryChangedCriterion.Conditions.items(AITItems.GOLD_KEY))
                .build(consumer, AITMod.MOD_ID + "/gold_key");

        Advancement netheriteKey = Advancement.Builder.create().parent(goldKey)
                .display(AITItems.NETHERITE_KEY, Text.translatable("achievement.ait.title.netherite_key"), Text.translatable("achievement.ait.description.netherite_key"),
                        null, AdvancementFrame.TASK, true, true, true)
                .criterion("netherite_key", InventoryChangedCriterion.Conditions.items(AITItems.NETHERITE_KEY))
                .build(consumer, AITMod.MOD_ID + "/netherite_key");

        Advancement classicKey = Advancement.Builder.create().parent(netheriteKey)
                .display(AITItems.CLASSIC_KEY, Text.translatable("achievement.ait.title.classic_key"),
                        Text.translatable("achievement.ait.description.classic_key"), null, AdvancementFrame.TASK, true, true, true)
                .criterion("classic_key", InventoryChangedCriterion.Conditions.items(AITItems.CLASSIC_KEY))
                .build(consumer, AITMod.MOD_ID + "/classic_key");

        Advancement firstDemat = Advancement.Builder.create().parent(firstEnter)
                .display(Items.ENDER_EYE, Text.translatable("achievement.ait.title.first_demat"), Text.translatable(
                        "achievement.ait.description.first_demat"),
                        null, AdvancementFrame.CHALLENGE, true, true, true)
                .criterion("first_demat", new TakeOffCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/first_demat");

        Advancement firstCrash = Advancement.Builder.create().parent(firstDemat)
                .display(Items.TNT, Text.translatable("achievement.ait.title.first_crash"), Text.translatable(
                        "achievement.ait.description.first_crash"),
                        null, AdvancementFrame.CHALLENGE, true, true, true)
                .criterion("first_crash", new CrashCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/first_crash");

        Advancement breakGrowth = Advancement.Builder.create().parent(firstEnter)
                .display(Items.OAK_LEAVES, Text.translatable("achievement.ait.title.break_growth"), Text.translatable(
                        "achievement.ait.description.break_growth"),
                        null, AdvancementFrame.TASK, true, false, true)
                .criterion("break_growth", new BreakVegetationCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/break_growth");

        Advancement redecoration = Advancement.Builder.create().parent(firstEnter)
                .display(AITItems.TARDIS_ITEM, Text.translatable("achievement.ait.title.redecorate"),
                        Text.translatable("achievement.ait.description.redecorate"), null, AdvancementFrame.TASK, true, false, true)
                .criterion("redecorate", new RedecorateCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/redecorate");

        // @TODO Duzo please do this i don't remember how
        Advancement ultimateCounter = Advancement.Builder.create()
                .display(AITItems.SONIC_SCREWDRIVER, Text.translatable("achievement.ait.title.ultimate_counter"),
                        Text.translatable("achievement.ait.description.ultimate_counter"), null, AdvancementFrame.TASK, true, false, true)
                .criterion("ultimate_counter", new UltimateCounterCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/ultimate_counter");

        Advancement forcedEntry = Advancement.Builder.create()
                .display(Items.NETHERITE_AXE, Text.translatable("achievement.ait.title.forced_entry"),
                        Text.translatable("achievement.ait.description.forced_entry"), null, AdvancementFrame.TASK, true, false, true)
                .criterion("forced_entry", new ForcedEntryCriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/forced_entry");

        // @TODO Duzo please do this i don't remember how
        Advancement pui = Advancement.Builder.create()
                .display(AITItems.ZEITON_DUST, Text.translatable("achievement.ait.title.pui"),
                        Text.translatable("achievement.ait.description.pui"), null, AdvancementFrame.TASK, true, false, true)
                .criterion("pui", new PUICriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/pui");

        // @TODO Duzo please do this i don't remember how
        Advancement bonding = Advancement.Builder.create()
                .display(AITBlocks.CORAL_PLANT, Text.translatable("achievement.ait.title.bonding"),
                        Text.translatable("achievement.ait.description.bonding"), null, AdvancementFrame.TASK, true, false, true)
                .criterion("bonding", new PUICriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/bonding");

        // @TODO Duzo please do this i don't remember how
        Advancement ownerShip = Advancement.Builder.create()
                .display(AITItems.TARDIS_ITEM, Text.translatable("achievement.ait.title.owner_ship"),
                        Text.translatable("achievement.ait.description.owner_ship"), null, AdvancementFrame.TASK, true, false, true)
                .criterion("owner_ship", new PUICriterion.Conditions())
                .build(consumer, AITMod.MOD_ID + "/owner_ship");

    }
}
