package loqor.ait.datagen;

import static net.minecraft.data.server.recipe.RecipeProvider.conditionsFromItem;
import static net.minecraft.data.server.recipe.RecipeProvider.hasItem;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITItems;
import loqor.ait.core.AITSounds;
import loqor.ait.datagen.datagen_providers.*;
import loqor.ait.datagen.datagen_providers.lang.LanguageType;
import loqor.ait.datagen.datagen_providers.loot.AITBlockLootTables;

public class AITModDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        generateLanguages(pack);
        generateItemTags(pack);
        generateBlockTags(pack);
        generateRecipes(pack);
        generateBlockModels(pack);
        generateSoundData(pack);
        generateAdvancements(pack);
        generateLoot(pack);
    }

    public void generateLoot(FabricDataGenerator.Pack pack) {
        pack.addProvider(AITBlockLootTables::new);
    }

    private void generateAdvancements(FabricDataGenerator.Pack pack) {
        pack.addProvider(AITAchievementProvider::new);
    }

    public void generateRecipes(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            AITRecipeProvider provider = new AITRecipeProvider(output);
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.IRON_KEY, 1)
                    .pattern(" N ").pattern("IEI").pattern("IRI").input('N', Items.IRON_NUGGET)
                    .input('I', Items.IRON_INGOT).input('E', Items.ENDER_PEARL).input('R', Items.REDSTONE)
                    .criterion(hasItem(Items.IRON_NUGGET), conditionsFromItem(Items.IRON_NUGGET))
                    .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                    .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
                    .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE)));
            provider.addShapedRecipe(
                    ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, AITBlocks.ZEITON_BLOCK, 1)
                            .pattern("ZZ ").pattern("ZZ ").pattern("   ").input('Z', AITItems.ZEITON_SHARD)
                            .criterion(hasItem(AITItems.ZEITON_SHARD), conditionsFromItem(AITItems.ZEITON_SHARD)));
            provider.addShapedRecipe(
                    ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE, 1)
                            .pattern("GGG").pattern("GNG").pattern("GGG")
                            .input('N', Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).input('G', Items.GOLD_NUGGET)
                            .criterion(hasItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                                    conditionsFromItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE))
                            .criterion(hasItem(Items.GOLD_NUGGET), conditionsFromItem(Items.GOLD_NUGGET)));
            provider.addShapelessRecipe(ShapelessRecipeJsonBuilder
                    .create(RecipeCategory.BREWING, AITItems.ZEITON_DUST, 4).input(AITItems.ZEITON_SHARD)
                    .criterion(hasItem(AITItems.ZEITON_SHARD), conditionsFromItem(AITItems.ZEITON_SHARD)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder
                    .create(RecipeCategory.MISC, AITItems.CHARGED_ZEITON_CRYSTAL, 1).pattern("ZZZ").pattern("CAC")
                    .pattern("ZZZ").input('Z', AITItems.ZEITON_SHARD).input('C', AITBlocks.ZEITON_CLUSTER)
                    .input('A', AITItems.ARTRON_COLLECTOR)
                    .criterion(hasItem(AITItems.ZEITON_SHARD), conditionsFromItem(AITItems.ZEITON_SHARD))
                    .criterion(hasItem(AITBlocks.ZEITON_CLUSTER), conditionsFromItem(AITBlocks.ZEITON_CLUSTER))
                    .criterion(hasItem(AITItems.ARTRON_COLLECTOR), conditionsFromItem(AITItems.ARTRON_COLLECTOR)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder
                    .create(RecipeCategory.MISC, AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE, 1).pattern("SSS")
                    .pattern("SGS").pattern("SSS").input('G', AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE)
                    .input('S', Items.NETHERITE_SCRAP)
                    .criterion(hasItem(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE),
                            conditionsFromItem(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE))
                    .criterion(hasItem(Items.NETHERITE_SCRAP), conditionsFromItem(Items.NETHERITE_SCRAP)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder
                    .create(RecipeCategory.MISC, AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE, 1).pattern("SAS")
                    .pattern("INI").pattern("SAS").input('I', Items.NETHERITE_INGOT)
                    .input('N', AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE).input('S', Items.NETHERITE_SCRAP)
                    .input('A', Items.AMETHYST_SHARD)
                    .criterion(hasItem(Items.NETHERITE_INGOT), conditionsFromItem(Items.NETHERITE_INGOT))
                    .criterion(hasItem(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE),
                            conditionsFromItem(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE))
                    .criterion(hasItem(Items.NETHERITE_SCRAP), conditionsFromItem(Items.NETHERITE_SCRAP))
                    .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder
                    .create(RecipeCategory.TOOLS, AITBlocks.ARTRON_COLLECTOR_BLOCK, 1).pattern(" L ").pattern(" R ")
                    .pattern("SBS").input('L', Items.LIGHTNING_ROD).input('R', Items.IRON_INGOT)
                    .input('S', Items.SMOOTH_STONE_SLAB).input('B', Items.REDSTONE_BLOCK)
                    .criterion(hasItem(Items.LIGHTNING_ROD), conditionsFromItem(Items.LIGHTNING_ROD))
                    .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                    .criterion(hasItem(Items.SMOOTH_STONE_SLAB), conditionsFromItem(Items.SMOOTH_STONE_SLAB))
                    .criterion(hasItem(Items.REDSTONE_BLOCK), conditionsFromItem(Items.REDSTONE_BLOCK)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.ARTRON_COLLECTOR, 1)
                    .pattern("CCC").pattern("IRI").pattern("CCC").input('C', Items.COPPER_INGOT)
                    .input('I', Items.IRON_INGOT).input('R', Items.REDSTONE_BLOCK)
                    .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                    .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                    .criterion(hasItem(Items.REDSTONE_BLOCK), conditionsFromItem(Items.REDSTONE_BLOCK)));
            provider.addShapedRecipe(
                    ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.RIFT_SCANNER, 1).pattern(" A ")
                            .pattern("IDI").pattern("QRQ").input('A', Items.AMETHYST_SHARD).input('I', Items.IRON_INGOT)
                            .input('D', Items.DIAMOND).input('R', Items.REDSTONE_BLOCK).input('Q', Items.QUARTZ)
                            .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                            .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                            .criterion(hasItem(Items.DIAMOND), conditionsFromItem(Items.DIAMOND))
                            .criterion(hasItem(Items.REDSTONE_BLOCK), conditionsFromItem(Items.REDSTONE_BLOCK))
                            .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITBlocks.WAYPOINT_BANK, 1)
                    .pattern("RTR").pattern("BWB").pattern("IEI").input('R', Items.REDSTONE)
                    .input('T', Blocks.TINTED_GLASS).input('B', Items.IRON_BARS).input('W', AITItems.WAYPOINT_CARTRIDGE)
                    .input('I', Blocks.IRON_BLOCK).input('E', Blocks.ENDER_CHEST)
                    .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                    .criterion(hasItem(Items.TINTED_GLASS), conditionsFromItem(Items.TINTED_GLASS))
                    .criterion(hasItem(Items.IRON_BARS), conditionsFromItem(Items.IRON_BARS))
                    .criterion(hasItem(AITItems.WAYPOINT_CARTRIDGE), conditionsFromItem(AITItems.WAYPOINT_CARTRIDGE))
                    .criterion(hasItem(Items.IRON_BLOCK), conditionsFromItem(Items.IRON_BLOCK))
                    .criterion(hasItem(Blocks.ENDER_CHEST), conditionsFromItem(Blocks.ENDER_CHEST)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder
                    .create(RecipeCategory.BUILDING_BLOCKS, AITBlocks.CORAL_PLANT, 1).pattern("ZHZ").pattern("CDC")
                    .pattern("RGR").input('Z', AITBlocks.ZEITON_BLOCK).input('H', AITItems.CHARGED_ZEITON_CRYSTAL)
                    .input('C', AITBlocks.ZEITON_CLUSTER).input('D', Items.DEAD_BRAIN_CORAL)
                    .input('R', AITItems.ZEITON_SHARD).input('G', AITBlocks.CONSOLE_GENERATOR)
                    .criterion(hasItem(AITBlocks.ZEITON_BLOCK), conditionsFromItem(AITBlocks.ZEITON_BLOCK))
                    .criterion(hasItem(AITItems.CHARGED_ZEITON_CRYSTAL),
                            conditionsFromItem(AITItems.CHARGED_ZEITON_CRYSTAL))
                    .criterion(hasItem(AITBlocks.ZEITON_CLUSTER), conditionsFromItem(AITBlocks.ZEITON_CLUSTER))
                    .criterion(hasItem(Items.DEAD_BRAIN_CORAL), conditionsFromItem(Items.DEAD_BRAIN_CORAL))
                    .criterion(hasItem(AITItems.ZEITON_SHARD), conditionsFromItem(AITItems.ZEITON_SHARD))
                    .criterion(hasItem(AITBlocks.CONSOLE_GENERATOR), conditionsFromItem(AITBlocks.CONSOLE_GENERATOR)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder
                    .create(RecipeCategory.BUILDING_BLOCKS, AITBlocks.ENGINE_CORE_BLOCK, 1).pattern("GHG")
                    .pattern("HCH").pattern("GHG").input('G', Items.GLASS).input('H', AITItems.CHARGED_ZEITON_CRYSTAL)
                    .input('C', Items.CONDUIT).criterion(hasItem(Items.GLASS), conditionsFromItem(Items.GLASS))
                    .criterion(hasItem(AITItems.CHARGED_ZEITON_CRYSTAL),
                            conditionsFromItem(AITItems.CHARGED_ZEITON_CRYSTAL))
                    .criterion(hasItem(Items.CONDUIT), conditionsFromItem(Items.CONDUIT)));
            provider.addShapedRecipe(
                    ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, AITBlocks.PLAQUE_BLOCK, 1).pattern("GSG")
                            .pattern("SBS").pattern("GSG").input('G', Items.GOLD_NUGGET).input('S', Items.SPRUCE_SLAB)
                            .input('B', Items.BLACK_CONCRETE)
                            .criterion(hasItem(Items.GOLD_NUGGET), conditionsFromItem(Items.GOLD_NUGGET))
                            .criterion(hasItem(Items.SPRUCE_SLAB), conditionsFromItem(Items.SPRUCE_SLAB))
                            .criterion(hasItem(Items.BLACK_CONCRETE), conditionsFromItem(Items.BLACK_CONCRETE)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.SONIC_SCREWDRIVER, 1)
                    .pattern(" QZ").pattern("IRQ").pattern("CI ").input('I', Items.IRON_INGOT).input('Q', Items.QUARTZ)
                    .input('C', Items.COMPARATOR).input('Z', AITItems.ZEITON_SHARD).input('R', Items.REDSTONE_BLOCK)
                    .group("sonic_item").criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                    .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                    .criterion(hasItem(Items.COMPARATOR), conditionsFromItem(Items.COMPARATOR))
                    .criterion(hasItem(AITItems.ZEITON_SHARD), conditionsFromItem(AITItems.ZEITON_SHARD))
                    .criterion(hasItem(Items.REDSTONE_BLOCK), conditionsFromItem(Items.REDSTONE_BLOCK)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, AITBlocks.CONSOLE_GENERATOR, 1)
                    .pattern(" G ").pattern("CEC").pattern(" I ").input('G', Items.GLASS).input('C', Items.COMPARATOR)
                    .input('E', Items.END_CRYSTAL).input('I', Items.IRON_INGOT)
                    .criterion(hasItem(Items.GLASS), conditionsFromItem(Items.GLASS))
                    .criterion(hasItem(Items.COMPARATOR), conditionsFromItem(Items.COMPARATOR))
                    .criterion(hasItem(Items.END_CRYSTAL), conditionsFromItem(Items.END_CRYSTAL))
                    .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, AITBlocks.DETECTOR_BLOCK, 4)
                    .pattern(" D ").pattern("ICI").pattern(" R ").input('D', Items.DAYLIGHT_DETECTOR)
                    .input('I', Items.IRON_INGOT).input('C', Items.COMPARATOR).input('R', Items.REDSTONE)
                    .criterion(hasItem(Items.DAYLIGHT_DETECTOR), conditionsFromItem(Items.DAYLIGHT_DETECTOR))
                    .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                    .criterion(hasItem(Items.COMPARATOR), conditionsFromItem(Items.COMPARATOR))
                    .criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, AITBlocks.DOOR_BLOCK, 1)
                    .pattern("GCG").pattern("CDC").pattern("CCC").input('D', Items.IRON_DOOR)
                    .input('G', Items.GLASS_PANE).input('C', Items.LIGHT_GRAY_CONCRETE)
                    .criterion(hasItem(Items.IRON_DOOR), conditionsFromItem(Items.IRON_DOOR))
                    .criterion(hasItem(Items.GLASS_PANE), conditionsFromItem(Items.GLASS_PANE))
                    .criterion(hasItem(Items.LIGHT_GRAY_CONCRETE), conditionsFromItem(Items.LIGHT_GRAY_CONCRETE)));
            provider.addShapedRecipe(
                    ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.WAYPOINT_CARTRIDGE, 1).pattern("III")
                            .pattern("IBI").pattern("CGC").input('I', Items.IRON_INGOT).input('B', Items.REDSTONE_BLOCK)
                            .input('C', Items.GREEN_DYE).input('G', Items.GOLD_NUGGET)
                            .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                            .criterion(hasItem(Items.REDSTONE_BLOCK), conditionsFromItem(Items.REDSTONE_BLOCK))
                            .criterion(hasItem(Items.GREEN_DYE), conditionsFromItem(Items.GREEN_DYE))
                            .criterion(hasItem(Items.GOLD_NUGGET), conditionsFromItem(Items.GOLD_NUGGET)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.HAMMER, 1)
                    .pattern("DSD").pattern(" A ").pattern(" T ").input('D', Items.DRIED_KELP).input('S', Items.STRING)
                    .input('A', Items.IRON_AXE).input('T', Items.STICK)
                    .criterion(hasItem(Items.DRIED_KELP), conditionsFromItem(Items.DRIED_KELP))
                    .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                    .criterion(hasItem(Items.IRON_AXE), conditionsFromItem(Items.IRON_AXE))
                    .criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK)));

            provider.addShapedRecipe(
                    ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, AITBlocks.MONITOR_BLOCK, 1).pattern("III")
                            .pattern("IBI").pattern("III").input('I', Items.IRON_INGOT).input('B', Items.ENDER_EYE)
                            .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                            .criterion(hasItem(Items.ENDER_EYE), conditionsFromItem(Items.ENDER_EYE)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder
                    .create(RecipeCategory.REDSTONE, AITBlocks.WALL_MONITOR_BLOCK, 1).pattern("QIQ").pattern("IBI")
                    .pattern("QIQ").input('Q', Items.QUARTZ).input('I', Items.IRON_INGOT).input('B', Items.ENDER_EYE)
                    .criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
                    .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                    .criterion(hasItem(Items.ENDER_EYE), conditionsFromItem(Items.ENDER_EYE)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.RESPIRATOR, 1)
                    .pattern("NNN").pattern("SPS").pattern("WNW").input('N', Items.IRON_NUGGET).input('S', Items.STRING)
                    .input('P', Items.GLASS_PANE).input('W', Items.PINK_WOOL)
                    .criterion(hasItem(Items.IRON_NUGGET), conditionsFromItem(Items.IRON_NUGGET))
                    .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                    .criterion(hasItem(Items.GLASS_PANE), conditionsFromItem(Items.GLASS_PANE))
                    .criterion(hasItem(Items.PINK_WOOL), conditionsFromItem(Items.PINK_WOOL)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder
                    .create(RecipeCategory.TOOLS, AITItems.FACELESS_RESPIRATOR, 1).pattern("   ").pattern(" R ")
                    .pattern("NWN").input('R', Items.REDSTONE).input('N', Items.IRON_NUGGET)
                    .input('W', Items.BLACK_WOOL).criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
                    .criterion(hasItem(Items.IRON_NUGGET), conditionsFromItem(Items.IRON_NUGGET))
                    .criterion(hasItem(Items.BLACK_WOOL), conditionsFromItem(Items.BLACK_WOOL)));

            provider.addShapedRecipe(
                    ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, AITBlocks.ENVIRONMENT_PROJECTOR, 1)
                            .pattern("IGI").pattern("GPG").pattern("ISI").input('I', Items.IRON_INGOT)
                            .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_NUGGET))
                            .input('G', Blocks.GLASS_PANE)
                            .criterion(hasItem(Blocks.GLASS_PANE), conditionsFromItem(Blocks.GLASS_PANE))
                            .input('P', Items.ENDER_PEARL)
                            .criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
                            .input('S', Blocks.SEA_LANTERN)
                            .criterion(hasItem(Blocks.SEA_LANTERN), conditionsFromItem(Blocks.SEA_LANTERN)));

            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, AITBlocks.PEANUT)
                    .pattern("BBB").pattern("BEB").pattern("BBB").input('B', Blocks.BEACON)
                    .criterion(hasItem(Blocks.BEACON), conditionsFromItem(Blocks.BEACON)).input('E', Blocks.DRAGON_EGG)
                    .criterion(hasItem(Blocks.DRAGON_EGG), conditionsFromItem(Blocks.DRAGON_EGG)));

            generateSmithingRecipes(provider);
            return provider;
        })));
    }

    public void generateSmithingRecipes(AITRecipeProvider provider) {
        provider.addSmithingTransformRecipe(
                SmithingTransformRecipeJsonBuilder
                        .create(Ingredient.ofItems(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.ofItems(AITItems.IRON_KEY), Ingredient.ofItems(Items.GOLD_NUGGET),
                                RecipeCategory.TOOLS, AITItems.GOLD_KEY)
                        .criterion(hasItem(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE),
                                conditionsFromItem(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE))
                        .criterion(hasItem(AITItems.IRON_KEY), conditionsFromItem(AITItems.IRON_KEY))
                        .criterion(hasItem(Items.GOLD_NUGGET), conditionsFromItem(Items.GOLD_NUGGET))
                        .criterion(hasItem(AITItems.GOLD_KEY), conditionsFromItem(AITItems.GOLD_KEY)),
                new Identifier(AITMod.MOD_ID, "gold_key_smithing"));
        provider.addSmithingTransformRecipe(
                SmithingTransformRecipeJsonBuilder
                        .create(Ingredient.ofItems(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.ofItems(AITItems.GOLD_KEY), Ingredient.ofItems(Items.NETHERITE_SCRAP),
                                RecipeCategory.TOOLS, AITItems.NETHERITE_KEY)
                        .criterion(hasItem(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE),
                                conditionsFromItem(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE))
                        .criterion(hasItem(AITItems.GOLD_KEY), conditionsFromItem(AITItems.GOLD_KEY))
                        .criterion(hasItem(Items.NETHERITE_SCRAP), conditionsFromItem(Items.NETHERITE_SCRAP))
                        .criterion(hasItem(AITItems.NETHERITE_KEY), conditionsFromItem(AITItems.NETHERITE_KEY)),
                new Identifier(AITMod.MOD_ID, "netherite_key_smithing"));
        provider.addSmithingTransformRecipe(
                SmithingTransformRecipeJsonBuilder
                        .create(Ingredient.ofItems(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.ofItems(AITItems.NETHERITE_KEY), Ingredient.ofItems(Items.AMETHYST_SHARD),
                                RecipeCategory.TOOLS, AITItems.CLASSIC_KEY)
                        .criterion(hasItem(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE),
                                conditionsFromItem(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE))
                        .criterion(hasItem(AITItems.NETHERITE_KEY), conditionsFromItem(AITItems.NETHERITE_KEY))
                        .criterion(hasItem(Items.AMETHYST_SHARD), conditionsFromItem(Items.AMETHYST_SHARD))
                        .criterion(hasItem(AITItems.CLASSIC_KEY), conditionsFromItem(AITItems.CLASSIC_KEY)),
                new Identifier(AITMod.MOD_ID, "classic_key_smithing"));
    }

    public void generateSoundData(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            AITSoundProvider provider = new AITSoundProvider(output);

            // MUSIC
            provider.addSound("music/secret_music", AITSounds.SECRET_MUSIC);
            provider.addSound("music/even_more_secret_music", AITSounds.EVEN_MORE_SECRET_MUSIC);
            provider.addSound("music/drifting_by_radio", AITSounds.DRIFTING_MUSIC);
            provider.addSound("music/mercury_by_nitrogenesis", AITSounds.MERCURY_MUSIC);

            // TARDIS
            provider.addSound("tardis/demat", AITSounds.DEMAT);
            provider.addSound("tardis/mat", AITSounds.MAT);
            provider.addSound("tardis/hop_demat", AITSounds.HOP_DEMAT);
            provider.addSound("tardis/hop_land", AITSounds.HOP_MAT);
            provider.addSound("tardis/land_thud", AITSounds.LAND_THUD);
            provider.addSound("tardis/fail_takeoff", AITSounds.FAIL_DEMAT);
            provider.addSound("tardis/fail_land", AITSounds.FAIL_MAT);
            provider.addSound("tardis/emergency_mat", AITSounds.EMERG_MAT);
            provider.addSound("tardis/flight_loop", AITSounds.FLIGHT_LOOP);
            provider.addSound("tardis/unstable_flight_loop", AITSounds.UNSTABLE_FLIGHT_LOOP);
            provider.addSound("tardis/console_shutdown", AITSounds.SHUTDOWN);
            provider.addSound("tardis/console_powerup", AITSounds.POWERUP);
            provider.addSound("tardis/siege_enable", AITSounds.SIEGE_ENABLE);
            provider.addSound("tardis/siege_disable", AITSounds.SIEGE_DISABLE);
            provider.addSound("tardis/eighth_demat", AITSounds.EIGHT_DEMAT);
            provider.addSound("tardis/eighth_mat", AITSounds.EIGHT_MAT);
            provider.addSound("tardis/ghost_mat", AITSounds.GHOST_MAT);
            provider.addSound("tardis/waypoint_activate", AITSounds.WAYPOINT_ACTIVATE);

            // Controls
            provider.addSound("controls/demat_lever_pull", AITSounds.DEMAT_LEVER_PULL);
            provider.addSound("controls/handbrake_lever_pull", AITSounds.HANDBRAKE_LEVER_PULL);
            provider.addSound("controls/handbrake_up", AITSounds.HANDBRAKE_UP);
            provider.addSound("controls/handbrake_down", AITSounds.HANDBRAKE_DOWN);
            provider.addSound("controls/crank", AITSounds.CRANK);
            provider.addSound("controls/knock", AITSounds.KNOCK);
            provider.addSound("controls/snap", AITSounds.SNAP);
            provider.addSound("controls/bweep", AITSounds.BWEEP);

            // Tools
            provider.addSound("tools/goes_ding", AITSounds.DING);

            // Hums
            provider.addSound("tardis/hums/toyota_hum", AITSounds.TOYOTA_HUM);
            provider.addSound("tardis/hums/coral_hum", AITSounds.CORAL_HUM);
            provider.addSound("tardis/hums/eight_hum", AITSounds.EIGHT_HUM);
            provider.addSound("tardis/hums/copper_hum", AITSounds.COPPER_HUM);
            provider.addSound("tardis/hums/exile_hum", AITSounds.EXILE_HUM);
            provider.addSound("tardis/hums/prime_hum", AITSounds.PRIME_HUM);
            provider.addSound("tardis/hums/tokamak_hum", AITSounds.TOKAMAK_HUM);

            // Creaks
            provider.addSound("tardis/creaks/creak_one", AITSounds.CREAK_ONE);
            provider.addSound("tardis/creaks/creak_two", AITSounds.CREAK_TWO);
            provider.addSound("tardis/creaks/creak_three", AITSounds.CREAK_THREE);
            provider.addSound("tardis/creaks/creak_four", AITSounds.CREAK_FOUR);
            provider.addSound("tardis/creaks/creak_five", AITSounds.CREAK_FIVE);
            provider.addSound("tardis/creaks/creak_six", AITSounds.CREAK_SIX);
            provider.addSound("tardis/creaks/creak_seven", AITSounds.CREAK_SEVEN);
            provider.addSound("tardis/creaks/whisper", AITSounds.WHISPER);

            // Secret
            provider.addSound("tardis/secret/doom_door_open", AITSounds.DOOM_DOOR_OPEN);
            provider.addSound("tardis/secret/doom_door_close", AITSounds.DOOM_DOOR_CLOSE);

            // Sonic
            provider.addSound("sonic/use", AITSounds.SONIC_USE);
            provider.addSound("sonic/switch", AITSounds.SONIC_SWITCH);

            // Other
            provider.addSound("tardis/vortex_sound", AITSounds.VORTEX_SOUND);
            provider.addSound("tardis/exterior/rain", AITSounds.RAIN);
            provider.addSound("tardis/exterior/thunder", AITSounds.THUNDER);

            provider.addSound("tardis/cloister", AITSounds.CLOISTER);
            provider.addSound("tardis/groan", AITSounds.GROAN);

            return provider;
        })));
    }

    public void generateItemTags(FabricDataGenerator.Pack pack) {
        pack.addProvider(AITItemTagProvider::new);
    }

    public void generateBlockTags(FabricDataGenerator.Pack pack) {
        pack.addProvider(AITBlockTagProvider::new);
    }

    public void generateBlockModels(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> {
            AITModelProvider aitModelProvider = new AITModelProvider(output);
            aitModelProvider.registerDirectionalBlock(AITBlocks.CONSOLE);
            aitModelProvider.registerDirectionalBlock(AITBlocks.CONSOLE_GENERATOR);
            aitModelProvider.registerSimpleBlock(AITBlocks.EXTERIOR_BLOCK);
            aitModelProvider.registerDirectionalBlock(AITBlocks.FABRICATOR);
            aitModelProvider.registerDirectionalBlock(AITBlocks.DOOR_BLOCK);
            aitModelProvider.registerDirectionalBlock(AITBlocks.CORAL_PLANT);
            aitModelProvider.registerDirectionalBlock(AITBlocks.ARTRON_COLLECTOR_BLOCK);
            return aitModelProvider;
        }));
    }

    public void generateLanguages(FabricDataGenerator.Pack pack) {
        generate_EN_US_Language(pack); // en_us (English US)
        generate_EN_UK_Language(pack); // en_uk (English UK)
        generate_FR_CA_Language(pack); // fr_ca (French Canadian)
        generate_FR_FR_Language(pack); // fr_fr (French France)
        generate_ES_AR_Language(pack); // es_ar (Spanish Argentina)
        generate_ES_CL_Language(pack); // es_cl (Spanish Chile)
        generate_ES_EC_Language(pack); // es_ec (Spanish Ecuador)
        generate_ES_ES_Language(pack); // es_es (Spanish Spain)
        generate_ES_MX_Language(pack); // es_mx (Spanish Mexico)
        generate_ES_UY_Language(pack); // es_uy (Spanish Uruguay)
        generate_ES_VE_Language(pack); // es_ve (Spanish Venezuela)
        generate_EN_AU_Language(pack); // en_au (English Australia)
        generate_EN_CA_Language(pack); // en_ca (English Canada)
        generate_EN_GB_Language(pack); // en_gb (English Great Britain)
        generate_EN_NZ_Language(pack); // en_nz (English New Zealand)
        generate_DE_DE_Language(pack); // de_de (German Germany)
        generate_DE_AT_Language(pack); // de_at (German Austria)
        generate_DE_CH_Language(pack); // de_ch (German Switzerland)
        generate_NDS_DE_Language(pack); // nds_de (Nordic German)
        generate_PT_BR_Language(pack); // pt_br (Portuguese Brazil)
        generate_RU_RU_Language(pack); // ru_ru (Russian Russia)
    }

    /**
     * Adds English translations to the language file.
     *
     * @param output
     *            The data generator output.
     * @param registriesFuture
     *            The registries future.
     * @param languageType
     *            The language type.
     * @return The AITLanguageProvider.
     */
    public AITLanguageProvider addEnglishTranslations(FabricDataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LanguageType languageType) {
        AITLanguageProvider provider = new AITLanguageProvider(output, languageType);

        provider.addTranslation("itemGroup.ait.item_group", "Adventures In Time");

        provider.addTranslation(AITItems.TARDIS_ITEM, "TARDIS");
        provider.addTranslation(AITItems.IRON_KEY, "Iron Key");
        provider.addTranslation(AITItems.GOLD_KEY, "Gold Key");
        provider.addTranslation(AITItems.NETHERITE_KEY, "Netherite Key");
        provider.addTranslation(AITItems.CLASSIC_KEY, "Classic Key");
        provider.addTranslation(AITItems.SKELETON_KEY, "Skeleton Key");
        provider.addTranslation(AITItems.REMOTE_ITEM, "Stattenheim Remote");
        provider.addTranslation(AITItems.ARTRON_COLLECTOR, "Artron Collector Unit");
        provider.addTranslation(AITItems.RIFT_SCANNER, "Rift Scanner");
        provider.addTranslation(AITItems.CHARGED_ZEITON_CRYSTAL, "Charged Zeiton Crystal");
        provider.addTranslation(AITItems.SIEGE_ITEM, "TARDIS");
        provider.addTranslation(AITItems.DRIFTING_MUSIC_DISC, "Music Disc");
        provider.addTranslation(AITItems.DRIFTING_MUSIC_DISC.getTranslationKey() + ".desc", "Radio - Drifting");
        provider.addTranslation(AITItems.MERCURY_MUSIC_DISC, "Music Disc");
        provider.addTranslation(AITItems.MERCURY_MUSIC_DISC.getTranslationKey() + ".desc", "Nitrogenesis - Mercury");
        provider.addTranslation(AITItems.SONIC_SCREWDRIVER, "Sonic Screwdriver");
        provider.addTranslation(AITItems.BLUEPRINT, "Blueprint");
        provider.addTranslation(AITItems.WAYPOINT_CARTRIDGE, "Waypoint Cartridge");
        provider.addTranslation(AITItems.HAMMER, "Hammer");
        provider.addTranslation(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
        provider.addTranslation(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
        provider.addTranslation(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
        provider.addTranslation(AITItems.WAYPOINT_CARTRIDGE, "Waypoint Cartridge");
        provider.addTranslation(AITItems.ZEITON_SHARD, "Zeiton Shard");
        provider.addTranslation(AITItems.ZEITON_DUST, "Zeiton Dust");
        provider.addTranslation(AITItems.RESPIRATOR, "Respirator");
        provider.addTranslation(AITItems.FACELESS_RESPIRATOR, "Faceless Respirator");

        // Exteriors
        provider.addTranslation("exterior.ait.capsule", "Capsule");
        provider.addTranslation("exterior.ait.police_box", "Police Box");
        provider.addTranslation("exterior.ait.booth", "Booth");
        provider.addTranslation("exterior.ait.renegade", "Renegade");
        provider.addTranslation("exterior.ait.doom", "DOOM");
        provider.addTranslation("exterior.ait.geometric", "Geometric");
        provider.addTranslation("exterior.ait.tardim", "TARDIM");
        provider.addTranslation("exterior.ait.easter_head", "Moyai");
        provider.addTranslation("exterior.ait.plinth", "Plinth");
        provider.addTranslation("exterior.ait.bookshelf", "Bookshelf");
        provider.addTranslation("exterior.ait.classic", "Classic");
        provider.addTranslation("exterior.ait.stallion", "Stallion");

        // Desktops
        provider.addTranslation("desktop.ait.coral", "Coral");
        provider.addTranslation("desktop.ait.war", "War");
        provider.addTranslation("desktop.ait.office", "Office");
        provider.addTranslation("desktop.ait.meridian", "Meridian");
        provider.addTranslation("desktop.ait.botanist", "Botanist");
        provider.addTranslation("desktop.ait.default_cave", "Default Cave");
        provider.addTranslation("desktop.ait.timeless", "Timeless");
        provider.addTranslation("desktop.ait.cave", "Cave");
        provider.addTranslation("desktop.ait.deco", "Deco");
        provider.addTranslation("desktop.ait.alnico", "Alnico");
        provider.addTranslation("desktop.ait.newbury", "Newbury");
        provider.addTranslation("desktop.ait.type_40", "Type 40");
        provider.addTranslation("desktop.ait.copper", "Copper");
        provider.addTranslation("desktop.ait.pristine", "Pristine");
        provider.addTranslation("desktop.ait.victorian", "Victorian");
        provider.addTranslation("desktop.ait.vintage", "Vintage");
        provider.addTranslation("desktop.ait.dev", "Dev");
        provider.addTranslation("desktop.ait.tokamak", "Tokamak");
        provider.addTranslation("desktop.ait.toyota", "Toyota");
        provider.addTranslation("desktop.ait.crystalline", "Crystalline");
        provider.addTranslation("desktop.ait.hourglass", "Hourglass");
        provider.addTranslation("desktop.ait.regal", "Regal");

        // Sonic Screwdrivers
        provider.addTranslation("sonic.ait.prime", "Prime");
        provider.addTranslation("sonic.ait.crystalline", "Crystalline");
        provider.addTranslation("sonic.ait.renaissance", "Renaissance");
        provider.addTranslation("sonic.ait.coral", "Coral");
        provider.addTranslation("sonic.ait.fob", "Fob");
        provider.addTranslation("sonic.ait.copper", "Copper");
        provider.addTranslation("sonic.ait.mechanical", "Mechanical");
        provider.addTranslation("sonic.ait.song", "Song");

        provider.addTranslation(AITBlocks.WAYPOINT_BANK, "Waypoint Bank");
        provider.addTranslation(AITBlocks.LANDING_PAD, "Landing Pad");
        provider.addTranslation(AITBlocks.DETECTOR_BLOCK, "Interior Detector Block");
        provider.addTranslation(AITBlocks.EXTERIOR_BLOCK, "Exterior");
        provider.addTranslation(AITBlocks.CORAL_PLANT, "TARDIS Coral");
        provider.addTranslation(AITBlocks.MONITOR_BLOCK, "Monitor");
        provider.addTranslation(AITBlocks.ARTRON_COLLECTOR_BLOCK, "Artron Collector");
        provider.addTranslation(AITBlocks.ZEITON_BLOCK, "Block of Zeiton");
        provider.addTranslation(AITBlocks.ZEITON_CLUSTER, "Zeiton Cluster");
        provider.addTranslation(AITBlocks.BUDDING_ZEITON, "Budding Zeiton");
        provider.addTranslation(AITBlocks.LARGE_ZEITON_BUD, "Large Zeiton Bud");
        provider.addTranslation(AITBlocks.MEDIUM_ZEITON_BUD, "Medium Zeiton Bud");
        provider.addTranslation(AITBlocks.SMALL_ZEITON_BUD, "Small Zeiton Bud");
        provider.addTranslation(AITBlocks.PLAQUE_BLOCK, "TARDIS Plaque");
        provider.addTranslation(AITBlocks.WALL_MONITOR_BLOCK, "Wall Monitor");
        provider.addTranslation(AITBlocks.ENVIRONMENT_PROJECTOR, "Environment Projector");
        provider.addTranslation(AITBlocks.DOOR_BLOCK, "Door");
        provider.addTranslation(AITBlocks.CONSOLE, "Console");
        provider.addTranslation(AITBlocks.CONSOLE_GENERATOR, "Console Generator");
        provider.addTranslation(AITBlocks.ENGINE_CORE_BLOCK, "Engine Core");
        provider.addTranslation(AITBlocks.PEANUT, "P E A N U T");

        provider.addTranslation("death.attack.tardis_squash", "%1$s got squashed by a TARDIS!");

        provider.addTranslation("tardis.message.control.protocol_116.active", "Protocol 116: ENGAGED");
        provider.addTranslation("tardis.message.control.hail_mary.engaged", "Protocol 813: ENGAGED");
        provider.addTranslation("tardis.message.control.hail_mary.disengaged", "Protocol 813: DISENGAGED");
        provider.addTranslation("tardis.message.control.protocol_116.inactive", "Protocol 116: DISENGAGED");
        provider.addTranslation("tardis.message.control.antigravs.active", "Antigravs: ENGAGED");
        provider.addTranslation("tardis.message.control.antigravs.inactive", "Antigravs: DISENGAGED");
        provider.addTranslation("tardis.message.control.fast_return.destination_nonexistent",
                "Fast Return: Last Position Nonexistent!");
        provider.addTranslation("tardis.message.control.fast_return.last_position", "Fast Return: LAST POSITION SET");
        provider.addTranslation("tardis.message.control.fast_return.current_position",
                "Fast Return: CURRENT POSITION SET");
        provider.addTranslation("tardis.message.control.protocol_813.active", "Protocol 813: ENGAGED");
        provider.addTranslation("tardis.message.control.protocol_813.inactive", "Protocol 813: DISENGAGED");
        provider.addTranslation("tardis.message.control.handbrake.on", "Handbrake: ON");
        provider.addTranslation("tardis.message.control.handbrake.off", "Handbrake: OFF");
        provider.addTranslation("tardis.message.control.randomiser.destination", "Destination: ");
        provider.addTranslation("tardis.message.control.refueler.enabled", "Refueling: ENGAGED");
        provider.addTranslation("tardis.message.control.refueler.disabled", "Refueling: DISENGAGED");
        provider.addTranslation("tardis.message.destination_biome", "Destination Biome: ");
        provider.addTranslation("tardis.message.control.increment.info", "Increment: ");
        provider.addTranslation("tardis.message.control.randomiser.poscontrol", "Destination: ");
        provider.addTranslation("tardis.exterior.sonic.repairing", "Repairing");
        provider.addTranslation("tardis.tool.cannot_repair", "Unable to repair TARDIS with current tool");
        provider.addTranslation("tardis.key.identity_error", "TARDIS does not identify with key");
        provider.addTranslation("tardis.message.control.hads.alarm_enabled", "Alarms: ENGAGED");
        provider.addTranslation("tardis.message.control.hads.alarms_disabled", "Alarms: DISENGAGED");
        provider.addTranslation("tardis.message.control.siege.enabled", "Siege Mode: ENGAGED");
        provider.addTranslation("tardis.message.control.siege.disabled", "Siege Mode: DISENGAGED");
        provider.addTranslation("tardis.message.control.telepathic.success", "Destination Found");
        provider.addTranslation("tardis.message.control.telepathic.failed", "Destination Not Found");
        provider.addTranslation("tardis.message.control.telepathic.choosing", "The TARDIS is choosing...");
        provider.addTranslation("tardis.message.interiorchange.success", "%s has grown to %d");
        provider.addTranslation("message.ait.control.ylandtype", "Vertical Search Mode: %s");
        provider.addTranslation("message.ait.loyalty_amount", "Loyalty Level: %s");
        provider.addTranslation("message.ait.control.xlandtype.on", "Horizontal Search: ENGAGED");
        provider.addTranslation("message.ait.control.xlandtype.off", "Horizontal Search: DISENGAGED");

        provider.addTranslation("message.ait.fuel.add", "Added %s fuel for %s; total: [%sau]");
        provider.addTranslation("message.ait.fuel.remove", "Removed %s fuel for %s; total: [%sau]");
        provider.addTranslation("message.ait.fuel.set", "Set fuel for %s; total: [%sau]");
        provider.addTranslation("message.ait.fuel.get", "Fuel of %s is: [%sau]");
        provider.addTranslation("message.ait.fuel.max", "TARDIS fuel is at max!");

        provider.addTranslation("message.ait.id", "TARDIS id: ");
        provider.addTranslation("message.ait.click_to_copy", "Click to copy");

        provider.addTranslation("message.ait.sonic.riftfound", "RIFT CHUNK FOUND");
        provider.addTranslation("message.ait.sonic.riftnotfound", "RIFT CHUNK NOT FOUND");
        provider.addTranslation("message.ait.sonic.handbrakedisengaged",
                "Handbrake disengaged, destination set to current position");
        provider.addTranslation("message.sonic.not_damaged", "TARDIS is not damaged");
        provider.addTranslation("message.ait.sonic.mode", "Mode: ");
        provider.addTranslation("message.ait.sonic.none", "None");
        provider.addTranslation("message.ait.sonic.currenttype", "Current Casing: ");
        provider.addTranslation("message.ait.remoteitem.warning4",
                "Target has been reset and updated, the device is now pointing towards your new target");
        provider.addTranslation("message.ait.keysmithing.upgrade", "Upgrade");
        provider.addTranslation("message.ait.keysmithing.key", "Key Type: ");
        provider.addTranslation("message.ait.keysmithing.ingredient", "Material: ");
        provider.addTranslation("message.ait.projector.skybox", "Now projecting: %s");
        provider.addTranslation("message.ait.riftscanner.info1", "Artron Chunk Info: ");
        provider.addTranslation("message.ait.riftscanner.info2", "Artron left in chunk: ");
        provider.addTranslation("message.ait.riftscanner.info3", "This is not a rift chunk");
        provider.addTranslation("message.ait.remoteitem.warning1",
                "The TARDIS is out of fuel and cannot dematerialise");
        provider.addTranslation("message.ait.remoteitem.warning2",
                "The TARDIS is refueling and is unable to dematerialise");
        provider.addTranslation("message.ait.remoteitem.warning3", "Cannot translocate exterior to interior dimension");
        provider.addTranslation("message.ait.remoteitem.success1", "Dematerialized TARDIS");
        provider.addTranslation("message.ait.tardis.control.dimension.info", "Dimension: ");
        provider.addTranslation("message.ait.version", "ᴠᴇʀꜱɪᴏɴ");

        provider.addTranslation("tooltip.ait.key.notardis", "Key does not identify with any TARDIS");
        provider.addTranslation("tooltip.ait.remoteitem.holdformoreinfo", "Hold shift for more info");
        provider.addTranslation("tooltip.ait.remoteitem.notardis", "Remote does not identify with any TARDIS");

        provider.addTranslation("screen.ait.monitor.apply", "Apply");
        provider.addTranslation("screen.ait.monitor.fuel", "Fuel");
        provider.addTranslation("screen.ait.monitor.traveltime", "Travel Time");
        provider.addTranslation("screen.ait.interiorsettings.title", "Interior Settings");
        provider.addTranslation("screen.ait.interiorsettings.back", "> Back");
        provider.addTranslation("screen.ait.security.button", "> Security Options");
        provider.addTranslation("screen.ait.interiorsettings.changeinterior", "> Change Interior");
        provider.addTranslation("screen.ait.interiorsettings.cacheconsole", "> Cache Console");

        provider.addTranslation("screen.ait.interior.settings.hum", "HUMS");
        provider.addTranslation("screen.ait.interior.settings.coral", "Coral");
        provider.addTranslation("screen.ait.interior.settings.toyota", "Toyota");
        provider.addTranslation("screen.ait.interior.settings.eight", "Eighth");
        provider.addTranslation("screen.ait.interior.settings.beacon", "Beacon");
        provider.addTranslation("screen.ait.interior.settings.copper", "Copper");
        provider.addTranslation("screen.ait.interior.settings.prime", "Prime");
        provider.addTranslation("screen.ait.interior.settings.tokamak", "Tokamak");
        provider.addTranslation("screen.ait.interior.settings.exile", "Exile");

        // Exterior translations

        // All
        provider.addTranslation("exterior.ait.default", "Default");
        provider.addTranslation("exterior.ait.fire", "Fire");
        provider.addTranslation("exterior.ait.soul", "Soul");
        provider.addTranslation("exterior.ait.gilded", "Gilded");

        // Police box specific
        provider.addTranslation("exterior.ait.tokamak", "Tokamak");
        provider.addTranslation("exterior.ait.coral", "Coral");
        provider.addTranslation("exterior.ait.futuristic", "Futuristic");

        // Classic specific
        provider.addTranslation("exterior.ait.definitive", "Definitive");
        provider.addTranslation("exterior.ait.hudolin", "Hudolin");
        provider.addTranslation("exterior.ait.exile", "Exile");
        provider.addTranslation("exterior.ait.prime", "Prime");
        provider.addTranslation("exterior.ait.ptored", "PTORed");
        provider.addTranslation("exterior.ait.shalka", "Shalka");
        provider.addTranslation("exterior.ait.mint", "Mint");
        provider.addTranslation("exterior.ait.yeti", "Yeti");

        // Renegade specific
        provider.addTranslation("exterior.ait.cabinet", "Cabinet");
        provider.addTranslation("exterior.ait.tron", "Tron");

        // Booth specific
        provider.addTranslation("exterior.ait.blue", "Blue");
        provider.addTranslation("exterior.ait.vintage", "Vintage");
        provider.addTranslation("exterior.ait.white", "White");

        provider.addTranslation("screen.ait.sonic.button", "> Sonic Settings");
        provider.addTranslation("screen.ait.sonicsettings.back", "Back");
        provider.addTranslation("screen.ait.gravity", "> Gravity: %s");
        provider.addTranslation("screen.ait.interor_select.title", "Interior Select");
        provider.addTranslation("screen.ait.security.leave_behind", "> Leave Behind");
        provider.addTranslation("screen.ait.security.hostile_alarms", "> Hostile Alarms");
        provider.addTranslation("screen.ait.security.minimum_loyalty", "> Isomorphic LVL");
        provider.addTranslation("tardis.message.interiorchange.not_enough_fuel",
                "The TARDIS does not have enough fuel to change it's interior");
        provider.addTranslation("tardis.message.interiorchange.warning",
                "Interior reconfiguration started! Please leave the interior.");

        provider.addTranslation("command.ait.realworld.response", "Spawned a real world TARDIS at: ");
        provider.addTranslation("command.ait.riftchunk.cannotsetlevel",
                "This chunk is not a rift chunk, so you can't set the artron levels of it");
        provider.addTranslation("command.ait.riftchunk.setlevel", "Set artron levels in rift chunk to: %s");
        provider.addTranslation("command.ait.riftchunk.cannotgetlevel",
                "This chunk is not a rift chunk, so you can't get the artron levels of it");
        provider.addTranslation("command.ait.riftchunk.getlevel", "AU in rift chunk: %s");
        provider.addTranslation("command.ait.data.get", "Value %s is set to '%s'");
        provider.addTranslation("command.ait.data.set", "Set value %s to '%s'");
        provider.addTranslation("command.ait.data.fail",
                "Can't get value of a property named %s, because component %s is not keyed!");

        provider.addTranslation("riftchunk.ait.tracking", "Rift Tracking");
        provider.addTranslation("riftchunk.ait.cooldown", "Rift tracking is on cooldown");
        provider.addTranslation("waypoint.position.tooltip", "Position");
        provider.addTranslation("waypoint.dimension.tooltip", "Dimension");
        provider.addTranslation("waypoint.direction.tooltip", "Direction");

        provider.addTranslation("ait.blueprint.tooltip", "Blueprint: ");

        return provider;
    }

    /**
     * Adds French translations to the language file.
     *
     * @param output
     *            The data generator output.
     * @param registriesFuture
     *            The registries future.
     * @param languageType
     *            The language type.
     * @return The AITLanguageProvider.
     */
    public AITLanguageProvider addFrenchTranslations(FabricDataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LanguageType languageType) {
        AITLanguageProvider aitLanguageProvider = new AITLanguageProvider(output, languageType);

        aitLanguageProvider.addTranslation(AITMod.AIT_ITEM_GROUP, "Adventures In Time");
        aitLanguageProvider.addTranslation(AITItems.TARDIS_ITEM, "TARDIS");
        aitLanguageProvider.addTranslation(AITBlocks.DOOR_BLOCK, "Porte");
        aitLanguageProvider.addTranslation(AITBlocks.CONSOLE, "Console");
        aitLanguageProvider.addTranslation(AITItems.IRON_KEY, "Clé en Fer");
        aitLanguageProvider.addTranslation(AITItems.GOLD_KEY, "Clé en Or");
        aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY, "Clé en Netherite");
        aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY, "Clé Classique");
        aitLanguageProvider.addTranslation(AITItems.REMOTE_ITEM, "Télécommande Stattenheim");
        aitLanguageProvider.addTranslation(AITItems.ARTRON_COLLECTOR, "Collecteur d’Artron ");
        aitLanguageProvider.addTranslation(AITItems.RIFT_SCANNER, "Scanneur de Faille");
        aitLanguageProvider.addTranslation(AITItems.SONIC_SCREWDRIVER, "Tournevis Sonique");
        // aitLanguageProvider.addTranslation(AITItems.RENAISSANCE_SONIC_SCREWDRIVER,
        // "Tournevis
        // Sonique
        // Renaissance");
        // aitLanguageProvider.addTranslation(AITItems.CORAL_SONIC_SCREWDRIVER,
        // "Tournevis Sonique
        // Coral");
        aitLanguageProvider.addTranslation(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE, "Modèle de forge");
        aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE, "Modèle de forge");
        aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE, "Modèle de forge");
        aitLanguageProvider.addTranslation(AITBlocks.EXTERIOR_BLOCK, "Exterieur");
        aitLanguageProvider.addTranslation(AITBlocks.CORAL_PLANT, "Corail TARDIS");
        aitLanguageProvider.addTranslation("death.attack.tardis_squash", "%1$s a été écrasé(e) par un TARDIS!");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info1", "Information sur le Chunk à Artron: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info2", "Artron laissé dans le chunk: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info3", "Ceci n'est pas un chunk à faille");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.holdformoreinfo",
                "Maintenez la touche shift pour plus d'informations");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.active", "Protocole 116: ACTIF");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.inactive", "Protocole 116: INACTIF");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning1",
                "Le TARDIS n’a plus de carburant et ne peux plus se dématérialiser");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning2",
                "Le TARDIS est en train de se recharger et est incapable de se dématérialiser");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning3",
                "Impossible de passer de la dimension extérieure à la dimension intérieure");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.notardis",
                "La télécommande n’est pas connecté avec le TARDIS");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.active", "Antigravs: ACTIF");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.inactive", "Antigravs: INACTIF");
        aitLanguageProvider.addTranslation("message.ait.tardis.control.dimension.info", "Dimension: ");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.destination_nonexistent",
                "Retour Rapide: Dernière position inexistante!");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.last_position",
                " Retour Rapide: DERNIÈRE POSITION DÉFINIE");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.current_position",
                "Fast Return: POSITION ACTUELLE DÉFINIE");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_813.active", "Protocole 813: ACTIF");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_813.inactive", "Protocole 813: INACTF");
        aitLanguageProvider.addTranslation("tardis.message.control.handbrake.on", "Frein à main: ON");
        aitLanguageProvider.addTranslation("tardis.message.control.handbrake.off", "Frein à main: OFF");
        aitLanguageProvider.addTranslation("tardis.message.control.randomiser.destination", "Destination: ");
        aitLanguageProvider.addTranslation("tardis.message.control.siege.enabled", "Siége: Activé");
        aitLanguageProvider.addTranslation("tardis.message.control.siege.enabled", "Siége: Désactivé");
        aitLanguageProvider.addTranslation("tardis.message.control.refueler.enabled", "Rechargement: Activé");
        aitLanguageProvider.addTranslation("tardis.message.control.refueler.disabled", "Rechargement: Désactivé");
        aitLanguageProvider.addTranslation("tardis.message.destination_biome", "Biome de Destination: ");
        aitLanguageProvider.addTranslation("tardis.message.control.increment.info", "Incrément: ");
        aitLanguageProvider.addTranslation("tardis.message.control.randomiser.poscontrol", "Destination: ");
        aitLanguageProvider.addTranslation("command.ait.riftchunk.isariftchunk", "Ceci est un chunk à faille");
        aitLanguageProvider.addTranslation("command.ait.riftchunk.isnotariftchunk", "Ceci n’est pas un chunk à faille");
        aitLanguageProvider.addTranslation("message.ait.sonic.riftfound", "CHUNK À FAILLE TROUVÉ");
        aitLanguageProvider.addTranslation("message.ait.sonic.riftnotfound", "CHUNK À FAILLE NON TROUVÉ");
        aitLanguageProvider.addTranslation("message.ait.sonic.handbrakedisengaged",
                "Frein à main desserré, destination définie à la position actuelle");
        aitLanguageProvider.addTranslation("message.ait.sonic.mode", "Mode: ");
        aitLanguageProvider.addTranslation("message.ait.sonic.none", "Aucun");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning4",
                "La cible a été réinitialisée et mise à jour, l'appareil est maintenant orienté vers votre nouvelle cible");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.upgrade", "Amélioration");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.key", "Type de Clé: ");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.ingredient", "Matériau: ");
        aitLanguageProvider.addTranslation("message.ait.control.ylandtype", "Recherche Surface Mode: %s");
        aitLanguageProvider.addTranslation("message.ait.control.xlandtype.on", "Horizontal Surface Search: ON");
        aitLanguageProvider.addTranslation("message.ait.control.xlandtype.off", "Horizontal Surface Search: OFF");

        aitLanguageProvider.addTranslation("tooltip.ait.key.notardis", "La clé ne s’identifie avec aucun TARDIS");
        //
        aitLanguageProvider.addTranslation("tardis.message.control.hads.alarm_enabled", "Alarms: Enabled");
        aitLanguageProvider.addTranslation("tardis.message.control.hads.alarms_disabled", "Alarms: Disabled");
        aitLanguageProvider.addTranslation("screen.ait.monitor.desktop_settings", "Desktop Settings");
        aitLanguageProvider.addTranslation("screen.ait.monitor.apply", "Apply");
        aitLanguageProvider.addTranslation("screen.ait.monitor.fuel", "Fuel: ");
        aitLanguageProvider.addTranslation("screen.ait.interiorsettings.title", "Interior Settings");
        aitLanguageProvider.addTranslation("screen.ait.interiorsettings.back", "> Back");
        aitLanguageProvider.addTranslation("screen.ait.interiorsettings.changeinterior", "> Change Interior");
        aitLanguageProvider.addTranslation("screen.ait.interior.settings.hum", "HUMS");
        aitLanguageProvider.addTranslation("screen.ait.interior.settings.coral", "Coral");
        aitLanguageProvider.addTranslation("screen.ait.interior.settings.toyota", "Toyota");
        aitLanguageProvider.addTranslation("screen.ait.interor_select.title", "Interior Select");
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.not_enough_fuel",
                "The TARDIS does not have enough fuel to change it's interior");
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.warning",
                "Interior reconfiguration started! Please leave the interior.");
        aitLanguageProvider.addTranslation("command.ait.realworld.response", "Spawned a real world TARDIS at: ");

        return aitLanguageProvider;
    }

    /**
     * Adds Spanish translations to the language file.
     *
     * @param output
     *            The data generator output.
     * @param registriesFuture
     *            The registries future.
     * @param languageType
     *            The language type.
     * @return The AITLanguageProvider.
     */
    public AITLanguageProvider addSpanishTranslations(FabricDataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LanguageType languageType) {
        AITLanguageProvider aitLanguageProvider = new AITLanguageProvider(output, languageType);

        aitLanguageProvider.addTranslation(AITMod.AIT_ITEM_GROUP, "Adventures In Time");
        aitLanguageProvider.addTranslation(AITItems.TARDIS_ITEM, "TARDIS");
        aitLanguageProvider.addTranslation(AITBlocks.DOOR_BLOCK, "Door");
        aitLanguageProvider.addTranslation(AITBlocks.CONSOLE, "Console");
        aitLanguageProvider.addTranslation(AITItems.IRON_KEY, "Iron Key");
        aitLanguageProvider.addTranslation(AITItems.GOLD_KEY, "Gold Key");
        aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY, "Netherite Key");
        aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY, "Classic Key");
        aitLanguageProvider.addTranslation(AITItems.REMOTE_ITEM, "Stattenheim Remote");
        aitLanguageProvider.addTranslation(AITItems.ARTRON_COLLECTOR, "Artron Collector");
        aitLanguageProvider.addTranslation(AITItems.RIFT_SCANNER, "escáner de Rift");
        aitLanguageProvider.addTranslation(AITItems.SONIC_SCREWDRIVER, "Sonic Screwdriver");
        aitLanguageProvider.addTranslation(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
        aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
        aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
        aitLanguageProvider.addTranslation(AITBlocks.EXTERIOR_BLOCK, "Exterior");
        aitLanguageProvider.addTranslation(AITBlocks.CORAL_PLANT, "TARDIS Coral");
        aitLanguageProvider.addTranslation("death.attack.tardis_squash", "%1$s got squashed by a TARDIS!");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info1", "Artron Chunk Info: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info2", "Artron left in chunk: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info3", "This is not a rift chunk");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.holdformoreinfo", "Hold shift for more info");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.active", "Protocol 116: ACTIVE");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.inactive", "Protocol 116: INACTIVE");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning1",
                "The TARDIS is out of fuel and cannot dematerialise");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning2",
                "The TARDIS is refueling and is unable to dematerialise");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning3",
                "Cannot translocate exterior to interior dimension");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.notardis",
                "Remote does not identify with any TARDIS");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.active", "Antigravs: ACTIVE");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.inactive", "Antigravs: INACTIVE");
        aitLanguageProvider.addTranslation("message.ait.tardis.control.dimension.info", "Dimension: ");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.destination_nonexistent",
                "Fast Return: Last Position Nonexistent!");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.last_position",
                "Fast Return: LAST POSITION SET");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.current_position",
                "Fast Return: CURRENT POSITION SET");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_813.active", "Protocol 813: ACTIVE");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_813.inactive", "Protocol 813: INACTIVE");
        aitLanguageProvider.addTranslation("tardis.message.control.handbrake.on", "handbrake: ON");
        aitLanguageProvider.addTranslation("tardis.message.control.handbrake.off", "handbrake: OFF");
        aitLanguageProvider.addTranslation("tardis.message.control.landtype.on", "Ground Searching: ON");
        aitLanguageProvider.addTranslation("tardis.message.control.landtype.off", "Ground Searching: OFF");
        aitLanguageProvider.addTranslation("tardis.message.control.randomiser.destination", "Destination: ");
        aitLanguageProvider.addTranslation("tardis.message.control.refueler.enabled", "Refueling: Enabled");
        aitLanguageProvider.addTranslation("tardis.message.control.refueler.disabled", "Refueling: Disabled");
        aitLanguageProvider.addTranslation("tardis.message.destination_biome", "Destination Biome: ");
        aitLanguageProvider.addTranslation("tardis.message.control.increment.info", "Increment: ");
        aitLanguageProvider.addTranslation("tardis.message.control.randomiser.poscontrol", "Destination: ");
        aitLanguageProvider.addTranslation("message.ait.sonic.riftfound", "RIFT CHUNK FOUND");
        aitLanguageProvider.addTranslation("message.ait.sonic.riftnotfound", "RIFT CHUNK NOT FOUND");
        aitLanguageProvider.addTranslation("message.ait.sonic.handbrakedisengaged",
                "Handbrake disengaged, destination set to current position");
        aitLanguageProvider.addTranslation("message.ait.sonic.mode", "Mode: ");
        aitLanguageProvider.addTranslation("message.ait.sonic.none", "None");
        aitLanguageProvider.addTranslation("message.ait.sonic.currenttype", "Current Casing: ");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning4",
                "Target has been reset and updated, the device is now pointing towards your new target");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.upgrade", "Upgrade");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.key", "Key Type: ");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.ingredient", "Material: ");
        aitLanguageProvider.addTranslation("tooltip.ait.key.notardis", "Key does not identify with any TARDIS");
        aitLanguageProvider.addTranslation("tardis.message.control.hads.alarm_enabled", "Alarms: Enabled");
        aitLanguageProvider.addTranslation("tardis.message.control.hads.alarms_disabled", "Alarms: Disabled");
        aitLanguageProvider.addTranslation("screen.ait.monitor.desktop_settings", "Desktop Settings");
        aitLanguageProvider.addTranslation("screen.ait.monitor.apply", "Apply");
        aitLanguageProvider.addTranslation("screen.ait.monitor.fuel", "Fuel: ");
        aitLanguageProvider.addTranslation("screen.ait.interiorsettings.title", "Interior Settings");
        aitLanguageProvider.addTranslation("screen.ait.interiorsettings.back", "> Back");
        aitLanguageProvider.addTranslation("screen.ait.interiorsettings.changeinterior", "> Change Interior");
        aitLanguageProvider.addTranslation("screen.ait.interior.settings.hum", "HUMS");
        aitLanguageProvider.addTranslation("screen.ait.interior.settings.coral", "Coral");
        aitLanguageProvider.addTranslation("screen.ait.interior.settings.toyota", "Toyota");
        aitLanguageProvider.addTranslation("screen.ait.interor_select.title", "Interior Select");
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.not_enough_fuel",
                "The TARDIS does not have enough fuel to change it's interior");
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.warning",
                "Interior reconfiguration started! Please leave the interior.");
        aitLanguageProvider.addTranslation("command.ait.realworld.response", "Spawned a real world TARDIS at: ");

        return aitLanguageProvider;
    }

    public AITLanguageProvider addGermanTranslations(FabricDataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LanguageType languageType) {
        AITLanguageProvider aitLanguageProvider = new AITLanguageProvider(output, languageType);

        aitLanguageProvider.addTranslation(AITMod.AIT_ITEM_GROUP, "Abenteuer in der Zeit");
        aitLanguageProvider.addTranslation(AITItems.TARDIS_ITEM, "TARDIS");
        aitLanguageProvider.addTranslation(AITBlocks.DOOR_BLOCK, "Tür");
        aitLanguageProvider.addTranslation(AITBlocks.CONSOLE, "Konsole");
        aitLanguageProvider.addTranslation(AITItems.IRON_KEY, "Eiserner Schlüssel");
        aitLanguageProvider.addTranslation(AITItems.GOLD_KEY, "Goldener Schlüssel");
        aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY, "Netherite Schlüssel");
        aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY, "Klassischer Schlüssel");
        aitLanguageProvider.addTranslation(AITItems.REMOTE_ITEM, "Stattenheim Fernbedienung");
        aitLanguageProvider.addTranslation(AITItems.ARTRON_COLLECTOR, "Artronsammler");
        aitLanguageProvider.addTranslation(AITItems.RIFT_SCANNER, "Riss-Scanner");
        aitLanguageProvider.addTranslation(AITItems.SONIC_SCREWDRIVER, "Schallschraubenzieher");
        // aitLanguageProvider.addTranslation(AITItems.RENAISSANCE_SONIC_SCREWDRIVER,
        // "Renaissance
        // Schallschraubenzieher");
        // aitLanguageProvider.addTranslation(AITItems.CORAL_SONIC_SCREWDRIVER,
        // "Korallen
        // Schallschraubenzieher");
        aitLanguageProvider.addTranslation(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE, "Schmiedevorlage");
        aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE, "Schmiedevorlage");
        aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE, "Schmiedevorlage");
        aitLanguageProvider.addTranslation(AITBlocks.EXTERIOR_BLOCK, "Äußere Hülle");
        aitLanguageProvider.addTranslation(AITBlocks.CORAL_PLANT, "TARDIS Koralle");
        aitLanguageProvider.addTranslation("death.attack.tardis_squash", "%1$s wurde von einer TARDIS zerquetscht!");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info1", "Artron Chunk Info: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info2", "Artron noch im Chunk: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info3", "Dies ist kein Riss-Chunk");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.holdformoreinfo",
                "Shift halten für weitere Informationen");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.active", "Protokoll 116: AKTIV");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.inactive", "Protocol 116: INACTIVE");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning1",
                "Die TARDIS benötigt Treibstoff und kann sich nicht dematerialisieren");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning2",
                "Die TARDIS tankt und kann sich nicht dematerialisieren");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning3",
                "Äußere Hülle kann nicht in die innere Dimension verschoben werden");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.notardis",
                "Fernbedienung identifiziert sich mit keiner TARDIS");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.active", "Antigravitation: AKTIVIERT");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.inactive", "Antigravitation: DEAKTIVIERT");
        aitLanguageProvider.addTranslation("message.ait.tardis.control.dimension.info", "Dimension: ");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.destination_nonexistent",
                "Rückreise: Letzte Position existiert nicht!");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.last_position",
                "Rückreise: LETZTE POSITION GESETZT");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.current_position",
                "Rückreise: JETZIGE POSITION GESETZT");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_813.active", "Protokoll 813: AKTIV");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_813.inactive", "Protocol 813: INACTIVE");
        aitLanguageProvider.addTranslation("tardis.message.control.handbrake.on", "Handbremse: AN");
        aitLanguageProvider.addTranslation("tardis.message.control.handbrake.off", "Handbremse: AUS");
        aitLanguageProvider.addTranslation("tardis.message.control.landtype.on", "Bodensuche: AN");
        aitLanguageProvider.addTranslation("tardis.message.control.landtype.off", "Bodensuche: AUS");
        aitLanguageProvider.addTranslation("tardis.message.control.randomiser.destination", "Zielort: ");
        aitLanguageProvider.addTranslation("tardis.message.control.refueler.enabled", "Tanken: Aktiviert");
        aitLanguageProvider.addTranslation("tardis.message.control.refueler.disabled", "Tanken: Deaktiviert");
        aitLanguageProvider.addTranslation("tardis.message.destination_biome", "Zielbiom: ");
        aitLanguageProvider.addTranslation("tardis.message.control.increment.info", "Steigerung: ");
        aitLanguageProvider.addTranslation("tardis.message.control.randomiser.poscontrol", "Zielort: ");
        aitLanguageProvider.addTranslation("message.ait.sonic.riftfound", "RIFT-CHUNK GEFUNDEN");
        aitLanguageProvider.addTranslation("message.ait.sonic.riftnotfound", "KEIN RIFT-CHUNK GEFUNDEN");
        aitLanguageProvider.addTranslation("message.ait.sonic.handbrakedisengaged",
                "Handbremse deaktiviert, Koordinaten auf jetzige Position gesetzt");
        aitLanguageProvider.addTranslation("message.ait.sonic.mode", "Modus: ");
        aitLanguageProvider.addTranslation("message.ait.sonic.none", "Keiner");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning4",
                "Ziel wurde zurückgesetzt und aktualisiert, das Gerät zeigt nun in Richtung des neuen Ziels");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.upgrade", "Upgrade");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.key", "Schlüsseltyp: ");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.ingredient", "Material: ");
        aitLanguageProvider.addTranslation("tooltip.ait.key.notardis",
                "Schlüssel identifiziert sich mit keiner TARDIS");
        //
        aitLanguageProvider.addTranslation("tardis.message.control.hads.alarm_enabled", "Alarms: Enabled");
        aitLanguageProvider.addTranslation("tardis.message.control.hads.alarms_disabled", "Alarms: Disabled");
        aitLanguageProvider.addTranslation("screen.ait.monitor.desktop_settings", "Desktop Settings");
        aitLanguageProvider.addTranslation("screen.ait.monitor.apply", "Apply");
        aitLanguageProvider.addTranslation("screen.ait.monitor.fuel", "Fuel: ");
        aitLanguageProvider.addTranslation("screen.ait.interiorsettings.title", "Interior Settings");
        aitLanguageProvider.addTranslation("screen.ait.interiorsettings.back", "> Back");
        aitLanguageProvider.addTranslation("screen.ait.interiorsettings.changeinterior", "> Change Interior");
        aitLanguageProvider.addTranslation("screen.ait.interior.settings.hum", "HUMS");
        aitLanguageProvider.addTranslation("screen.ait.interior.settings.coral", "Coral");
        aitLanguageProvider.addTranslation("screen.ait.interior.settings.toyota", "Toyota");
        aitLanguageProvider.addTranslation("screen.ait.interor_select.title", "Interior Select");
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.not_enough_fuel",
                "The TARDIS does not have enough fuel to change it's interior");
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.warning",
                "Interior reconfiguration started! Please leave the interior.");
        aitLanguageProvider.addTranslation("command.ait.realworld.response", "Spawned a real world TARDIS at:");

        return aitLanguageProvider;
    }

    public AITLanguageProvider addPortugueseTranslations(FabricDataOutput output,
            CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LanguageType languageType) {
        AITLanguageProvider provider = new AITLanguageProvider(output, languageType);
        return provider;
    }

    public void generate_DE_AT_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                ((output, registriesFuture) -> addGermanTranslations(output, registriesFuture, LanguageType.DE_AT))); // de_at
                                                                                                                        // (German
                                                                                                                        // Austria)
    }

    public void generate_DE_CH_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                ((output, registriesFuture) -> addGermanTranslations(output, registriesFuture, LanguageType.DE_CH))); // de_ch
                                                                                                                        // (German
                                                                                                                        // Switzerland)
    }

    public void generate_DE_DE_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                ((output, registriesFuture) -> addGermanTranslations(output, registriesFuture, LanguageType.DE_DE))); // de_de
                                                                                                                        // (German
                                                                                                                        // Germany)
    }

    public void generate_NDS_DE_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                ((output, registriesFuture) -> addGermanTranslations(output, registriesFuture, LanguageType.NDS_DE))); // nds_de
                                                                                                                        // (Nordic
                                                                                                                        // German)
    }

    public void generate_EN_US_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                ((output, registriesFuture) -> addEnglishTranslations(output, registriesFuture, LanguageType.EN_US))); // en_us
                                                                                                                        // (English
                                                                                                                        // US)
    }

    public void generate_EN_UK_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                ((output, registriesFuture) -> addEnglishTranslations(output, registriesFuture, LanguageType.EN_UK))); // en_uk
                                                                                                                        // (English
                                                                                                                        // UK)
    }

    public void generate_FR_CA_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                (((output, registriesFuture) -> addFrenchTranslations(output, registriesFuture, LanguageType.FR_CA)))); // fr_ca
                                                                                                                        // (French
                                                                                                                        // Canadian)
    }

    public void generate_FR_FR_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                (((output, registriesFuture) -> addFrenchTranslations(output, registriesFuture, LanguageType.FR_FR)))); // fr_fr
                                                                                                                        // (French
                                                                                                                        // France)
    }

    public void generate_ES_AR_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                (((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_AR)))); // es_ar
                                                                                                                            // (Spanish
                                                                                                                            // Argentina)
    }

    public void generate_ES_CL_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                (((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_CL)))); // es_cl
                                                                                                                            // (Spanish
                                                                                                                            // Chile)
    }

    public void generate_ES_EC_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                (((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_EC)))); // es_ec
                                                                                                                            // (Spanish
                                                                                                                            // Ecuador)
    }

    public void generate_ES_ES_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                (((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_ES)))); // es_es
                                                                                                                            // (Spanish
                                                                                                                            // Spain)
    }

    public void generate_ES_MX_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                (((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_MX)))); // es_mx
                                                                                                                            // (Spanish
                                                                                                                            // Mexico)
    }

    public void generate_ES_UY_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                (((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_UY)))); // es_uy
                                                                                                                            // (Spanish
                                                                                                                            // Uruguay)
    }

    public void generate_ES_VE_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                (((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_VE)))); // es_ve
                                                                                                                            // (Spanish
                                                                                                                            // Venezuela)
    }

    public void generate_EN_AU_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                ((output, registriesFuture) -> addEnglishTranslations(output, registriesFuture, LanguageType.EN_AU))); // en_au
                                                                                                                        // (English
                                                                                                                        // Australia)
    }

    public void generate_EN_CA_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                ((output, registriesFuture) -> addEnglishTranslations(output, registriesFuture, LanguageType.EN_CA))); // en_ca
                                                                                                                        // (English
                                                                                                                        // Canada)
    }

    public void generate_EN_GB_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                ((output, registriesFuture) -> addEnglishTranslations(output, registriesFuture, LanguageType.EN_GB))); // en_gb
                                                                                                                        // (English
                                                                                                                        // Great
                                                                                                                        // Britain)
    }

    public void generate_EN_NZ_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(
                ((output, registriesFuture) -> addEnglishTranslations(output, registriesFuture, LanguageType.EN_NZ))); // en_nz
                                                                                                                        // (English
                                                                                                                        // New
                                                                                                                        // Zealand)
    }

    public void generate_PT_BR_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> addPortugueseTranslations(output, registriesFuture,
                LanguageType.PT_BR))); // pt_br (Portuguese Brazil)
    }

    public void generate_RU_RU_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> new AITLanguageProvider(output, LanguageType.RU_RU))); // ru_ru
                                                                                                                // (Russian
                                                                                                                // Russia)
    }

    public void generate_UK_UA_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> new AITLanguageProvider(output, LanguageType.UK_UA))); // uk_ua
                                                                                                                // (Ukrainian
                                                                                                                // Ukraine)
    }
}
