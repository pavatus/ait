package mdteam.ait.datagen;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITDamageTypes;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.AITSounds;
import mdteam.ait.datagen.datagen_providers.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class AITModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        generateLanguages(pack);
        generateItemTags(pack); // fixme im not sure why this is being silly goofy
        generateRecipes(pack);
        generateBlockModels(pack);
        generateSoundData(pack);
        generateAdvancements(pack);
    }

    private void generateAdvancements(FabricDataGenerator.Pack pack) {
        pack.addProvider(AITAchievementProvider::new);
    }

    public void generateRecipes(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            AITRecipeProvider provider = new AITRecipeProvider(output);
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.IRON_KEY, 1)
                    .pattern(" N ")
                    .pattern("IEI")
                    .pattern("IRI")
                    .input('N', Items.IRON_NUGGET)
                    .input('I', Items.IRON_INGOT)
                    .input('E', Items.ENDER_PEARL)
                    .input('R', Items.REDSTONE)
                    .criterion(FabricRecipeProvider.hasItem(Items.IRON_NUGGET),
                            FabricRecipeProvider.conditionsFromItem(Items.IRON_NUGGET))
                    .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT),
                            FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                    .criterion(FabricRecipeProvider.hasItem(Items.ENDER_PEARL),
                            FabricRecipeProvider.conditionsFromItem(Items.ENDER_PEARL))
                    .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE),
                            FabricRecipeProvider.conditionsFromItem(Items.REDSTONE))
            );
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE, 1)
                    .pattern("GGG")
                    .pattern("GNG")
                    .pattern("GGG")
                    .input('N', Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .input('G', Items.GOLD_NUGGET)
                    .criterion(FabricRecipeProvider.hasItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                            FabricRecipeProvider.conditionsFromItem(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE))
                    .criterion(FabricRecipeProvider.hasItem(Items.GOLD_NUGGET),
                            FabricRecipeProvider.conditionsFromItem(Items.GOLD_NUGGET))
            );
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE, 1)
                    .pattern("SSS")
                    .pattern("SGS")
                    .pattern("SSS")
                    .input('G', AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE)
                    .input('S', Items.NETHERITE_SCRAP)
                    .criterion(FabricRecipeProvider.hasItem(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE),
                            FabricRecipeProvider.conditionsFromItem(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE))
                    .criterion(FabricRecipeProvider.hasItem(Items.NETHERITE_SCRAP),
                            FabricRecipeProvider.conditionsFromItem(Items.NETHERITE_SCRAP))
            );
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE, 1)
                    .pattern("SAS")
                    .pattern("INI")
                    .pattern("SAS")
                    .input('I', Items.NETHERITE_INGOT)
                    .input('N', AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE)
                    .input('S', Items.NETHERITE_SCRAP)
                    .input('A', Items.AMETHYST_SHARD)
                    .criterion(FabricRecipeProvider.hasItem(Items.NETHERITE_INGOT),
                            FabricRecipeProvider.conditionsFromItem(Items.NETHERITE_INGOT))
                    .criterion(FabricRecipeProvider.hasItem(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE),
                            FabricRecipeProvider.conditionsFromItem(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE))
                    .criterion(FabricRecipeProvider.hasItem(Items.NETHERITE_SCRAP),
                            FabricRecipeProvider.conditionsFromItem(Items.NETHERITE_SCRAP))
                    .criterion(FabricRecipeProvider.hasItem(Items.AMETHYST_SHARD),
                            FabricRecipeProvider.conditionsFromItem(Items.AMETHYST_SHARD))
            );
            /*provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.REMOTE_ITEM, 1)
                    .pattern(" R ")
                    .pattern("ICI")
                    .pattern("IPI")
                    .input('R', Items.REDSTONE_BLOCK)
                    .input('I', Items.NETHERITE_INGOT)
                    .input('C', Items.COPPER_INGOT)
                    .input('P', Items.REPEATER)
                    .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE_BLOCK),
                            FabricRecipeProvider.conditionsFromItem(Items.REDSTONE_BLOCK))
                    .criterion(FabricRecipeProvider.hasItem(Items.NETHERITE_INGOT),
                            FabricRecipeProvider.conditionsFromItem(Items.NETHERITE_INGOT))
                    .criterion(FabricRecipeProvider.hasItem(Items.COPPER_INGOT),
                            FabricRecipeProvider.conditionsFromItem(Items.COPPER_INGOT))
                    .criterion(FabricRecipeProvider.hasItem(Items.REPEATER),
                            FabricRecipeProvider.conditionsFromItem(Items.REPEATER))
            );*/
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.ARTRON_COLLECTOR, 1)
                    .pattern("CCC")
                    .pattern("IRI")
                    .pattern("CCC")
                    .input('C', Items.COPPER_INGOT)
                    .input('I', Items.IRON_INGOT)
                    .input('R', Items.REDSTONE_BLOCK)
                    .criterion(FabricRecipeProvider.hasItem(Items.COPPER_INGOT),
                            FabricRecipeProvider.conditionsFromItem(Items.COPPER_INGOT))
                    .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT),
                            FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                    .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE_BLOCK),
                            FabricRecipeProvider.conditionsFromItem(Items.REDSTONE_BLOCK))
            );
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.RIFT_SCANNER, 1)
                    .pattern(" A ")
                    .pattern("IDI")
                    .pattern("QRQ")
                    .input('A', Items.AMETHYST_SHARD)
                    .input('I', Items.IRON_INGOT)
                    .input('D', Items.DIAMOND)
                    .input('R', Items.REDSTONE_BLOCK)
                    .input('Q', Items.QUARTZ)
                    .criterion(FabricRecipeProvider.hasItem(Items.AMETHYST_SHARD), FabricRecipeProvider.conditionsFromItem(Items.AMETHYST_SHARD))
                    .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                    .criterion(FabricRecipeProvider.hasItem(Items.DIAMOND), FabricRecipeProvider.conditionsFromItem(Items.DIAMOND))
                    .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE_BLOCK), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE_BLOCK))
                    .criterion(FabricRecipeProvider.hasItem(Items.QUARTZ), FabricRecipeProvider.conditionsFromItem(Items.QUARTZ))
            );
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, AITBlocks.CORAL_PLANT, 1)
                    .pattern("SRS")
                    .pattern("BCB")
                    .pattern("ERE")
                    .input('C', Items.DEAD_BRAIN_CORAL)
                    .input('R', Items.END_CRYSTAL)
                    .input('E', Items.ENDER_EYE)
                    .input('S', Items.BLAZE_ROD)
                    .input('B', Items.REDSTONE_BLOCK)
                    .criterion(FabricRecipeProvider.hasItem(Items.DEAD_BRAIN_CORAL), FabricRecipeProvider.conditionsFromItem(Items.DEAD_BRAIN_CORAL))
                    .criterion(FabricRecipeProvider.hasItem(Items.END_CRYSTAL), FabricRecipeProvider.conditionsFromItem(Items.END_CRYSTAL))
                    .criterion(FabricRecipeProvider.hasItem(Items.ENDER_EYE), FabricRecipeProvider.conditionsFromItem(Items.ENDER_EYE))
                    .criterion(FabricRecipeProvider.hasItem(Items.BLAZE_ROD), FabricRecipeProvider.conditionsFromItem(Items.BLAZE_ROD))
                    .criterion(FabricRecipeProvider.hasItem(Items.REDSTONE_BLOCK), FabricRecipeProvider.conditionsFromItem(Items.REDSTONE_BLOCK)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.MECHANICAL_SONIC_SCREWDRIVER, 1)
                    .pattern(" IE")
                    .pattern("ICI")
                    .pattern("BI ")
                    .input('I', Items.IRON_INGOT)
                    .input('E', Items.ENDER_EYE)
                    .input('C', Items.COMPARATOR)
                    .input('B', Items.BLAZE_ROD)
                    .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT))
                    .criterion(FabricRecipeProvider.hasItem(Items.ENDER_EYE), FabricRecipeProvider.conditionsFromItem(Items.ENDER_EYE))
                    .criterion(FabricRecipeProvider.hasItem(Items.COMPARATOR), FabricRecipeProvider.conditionsFromItem(Items.COMPARATOR))
                    .criterion(FabricRecipeProvider.hasItem(Items.BLAZE_ROD), FabricRecipeProvider.conditionsFromItem(Items.BLAZE_ROD)));
            provider.addShapelessRecipe(ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.RENAISSANCE_SONIC_SCREWDRIVER, 1)
                    .input(AITItems.CORAL_SONIC_SCREWDRIVER)
                    .criterion(FabricRecipeProvider.hasItem(AITItems.CORAL_SONIC_SCREWDRIVER), FabricRecipeProvider.conditionsFromItem(AITItems.CORAL_SONIC_SCREWDRIVER)));
            provider.addShapelessRecipe(ShapelessRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.CORAL_SONIC_SCREWDRIVER, 1)
                    .input(AITItems.MECHANICAL_SONIC_SCREWDRIVER)
                    .criterion(FabricRecipeProvider.hasItem(AITItems.MECHANICAL_SONIC_SCREWDRIVER), FabricRecipeProvider.conditionsFromItem(AITItems.MECHANICAL_SONIC_SCREWDRIVER)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, AITBlocks.CONSOLE, 1)
                    .pattern(" G ")
                    .pattern("CEC")
                    .pattern(" I ")
                    .input('G', Items.GLASS)
                    .input('C', Items.COMPARATOR)
                    .input('E', Items.END_CRYSTAL)
                    .input('I', Items.IRON_INGOT)
                    .criterion(FabricRecipeProvider.hasItem(Items.GLASS), FabricRecipeProvider.conditionsFromItem(Items.GLASS))
                    .criterion(FabricRecipeProvider.hasItem(Items.COMPARATOR), FabricRecipeProvider.conditionsFromItem(Items.COMPARATOR))
                    .criterion(FabricRecipeProvider.hasItem(Items.END_CRYSTAL), FabricRecipeProvider.conditionsFromItem(Items.END_CRYSTAL))
                    .criterion(FabricRecipeProvider.hasItem(Items.IRON_INGOT), FabricRecipeProvider.conditionsFromItem(Items.IRON_INGOT)));
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, AITBlocks.DOOR_BLOCK, 1)
                    .pattern("GCG")
                    .pattern("CDC")
                    .pattern("CCC")
                    .input('D', Items.IRON_DOOR)
                    .input('G', Items.GLASS_PANE)
                    .input('C', Items.LIGHT_GRAY_CONCRETE)
                    .criterion(FabricRecipeProvider.hasItem(Items.IRON_DOOR), FabricRecipeProvider.conditionsFromItem(Items.IRON_DOOR))
                    .criterion(FabricRecipeProvider.hasItem(Items.GLASS_PANE), FabricRecipeProvider.conditionsFromItem(Items.GLASS_PANE))
                    .criterion(FabricRecipeProvider.hasItem(Items.LIGHT_GRAY_CONCRETE), FabricRecipeProvider.conditionsFromItem(Items.LIGHT_GRAY_CONCRETE)));
            generateSmithingRecipes(provider);
            return provider;
        })));
    }

    public void generateSmithingRecipes(AITRecipeProvider provider) {
        provider.addSmithingTransformRecipe(SmithingTransformRecipeJsonBuilder.create(
                                Ingredient.ofItems(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.ofItems(AITItems.IRON_KEY),
                                Ingredient.ofItems(Items.GOLD_NUGGET),
                                RecipeCategory.TOOLS, AITItems.GOLD_KEY)
                        .criterion(FabricRecipeProvider.hasItem(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE),
                                FabricRecipeProvider.conditionsFromItem(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE))
                        .criterion(FabricRecipeProvider.hasItem(AITItems.IRON_KEY),
                                FabricRecipeProvider.conditionsFromItem(AITItems.IRON_KEY))
                        .criterion(FabricRecipeProvider.hasItem(Items.GOLD_NUGGET),
                                FabricRecipeProvider.conditionsFromItem(Items.GOLD_NUGGET))
                        .criterion(FabricRecipeProvider.hasItem(AITItems.GOLD_KEY),
                                FabricRecipeProvider.conditionsFromItem(AITItems.GOLD_KEY)),
                new Identifier(AITMod.MOD_ID, "gold_key_smithing"));
        provider.addSmithingTransformRecipe(SmithingTransformRecipeJsonBuilder.create(
                                Ingredient.ofItems(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.ofItems(AITItems.GOLD_KEY),
                                Ingredient.ofItems(Items.NETHERITE_SCRAP),
                                RecipeCategory.TOOLS, AITItems.NETHERITE_KEY)
                        .criterion(FabricRecipeProvider.hasItem(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE),
                                FabricRecipeProvider.conditionsFromItem(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE))
                        .criterion(FabricRecipeProvider.hasItem(AITItems.GOLD_KEY),
                                FabricRecipeProvider.conditionsFromItem(AITItems.GOLD_KEY))
                        .criterion(FabricRecipeProvider.hasItem(Items.NETHERITE_SCRAP),
                                FabricRecipeProvider.conditionsFromItem(Items.NETHERITE_SCRAP))
                        .criterion(FabricRecipeProvider.hasItem(AITItems.NETHERITE_KEY),
                                FabricRecipeProvider.conditionsFromItem(AITItems.NETHERITE_KEY)),
                new Identifier(AITMod.MOD_ID, "netherite_key_smithing"));
        provider.addSmithingTransformRecipe(SmithingTransformRecipeJsonBuilder.create(
                                Ingredient.ofItems(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.ofItems(AITItems.NETHERITE_KEY),
                                Ingredient.ofItems(Items.AMETHYST_SHARD),
                                RecipeCategory.TOOLS, AITItems.CLASSIC_KEY)
                        .criterion(FabricRecipeProvider.hasItem(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE),
                                FabricRecipeProvider.conditionsFromItem(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE))
                        .criterion(FabricRecipeProvider.hasItem(AITItems.NETHERITE_KEY),
                                FabricRecipeProvider.conditionsFromItem(AITItems.NETHERITE_KEY))
                        .criterion(FabricRecipeProvider.hasItem(Items.AMETHYST_SHARD),
                                FabricRecipeProvider.conditionsFromItem(Items.AMETHYST_SHARD))
                        .criterion(FabricRecipeProvider.hasItem(AITItems.CLASSIC_KEY),
                                FabricRecipeProvider.conditionsFromItem(AITItems.CLASSIC_KEY)),
                new Identifier(AITMod.MOD_ID, "classic_key_smithing"));
    }

    public void generateSoundData(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> {
            AITSoundProvider provider = new AITSoundProvider(output);

            provider.addSound("secret_music", AITSounds.SECRET_MUSIC);
            provider.addSound("even_more_secret_music", AITSounds.EVEN_MORE_SECRET_MUSIC);

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
            provider.addSound("tardis/console_shutdown", AITSounds.SHUTDOWN);
            provider.addSound("tardis/console_powerup", AITSounds.POWERUP);
            provider.addSound("tardis/eighth_demat", AITSounds.EIGHT_DEMAT);
            provider.addSound("tardis/eighth_mat", AITSounds.EIGHT_MAT);

            // Controls
            provider.addSound("controls/demat_lever_pull", AITSounds.DEMAT_LEVER_PULL);
            provider.addSound("controls/handbrake_lever_pull", AITSounds.HANDBRAKE_LEVER_PULL);
            provider.addSound("controls/knock", AITSounds.KNOCK);
            provider.addSound("controls/snap", AITSounds.SNAP);

            // Tools
            provider.addSound("tools/goes_ding", AITSounds.DING);

            // Hums
            provider.addSound("tardis/hums/toyota_hum", AITSounds.TOYOTA_HUM);
            provider.addSound("tardis/hums/coral_hum", AITSounds.CORAL_HUM);
            // Creaks
            provider.addSound("tardis/creaks/creak_one", AITSounds.CREAK_ONE);
            provider.addSound("tardis/creaks/creak_two", AITSounds.CREAK_TWO);
            provider.addSound("tardis/creaks/creak_three", AITSounds.CREAK_THREE);
            // Other
            provider.addSound("tardis/cloister", AITSounds.CLOISTER);

            return provider;
        })));
    }

    public void generateItemTags(FabricDataGenerator.Pack pack) {
        pack.addProvider(AITItemTagProvider::new);
    }

    public void generateBlockModels(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> {
            AITModelProvider aitModelProvider = new AITModelProvider(output);
            aitModelProvider.registerDirectionalBlock(AITBlocks.RADIO);
            aitModelProvider.registerDirectionalBlock(AITBlocks.CONSOLE);
            aitModelProvider.registerDirectionalBlock(AITBlocks.EXTERIOR_BLOCK);
            aitModelProvider.registerDirectionalBlock(AITBlocks.DOOR_BLOCK);
            aitModelProvider.registerDirectionalBlock(AITBlocks.CORAL_PLANT);

            //falloutModelProvider.registerSimpleBlock(AITBlocks.DEEPSLATE_URANIUM_ORE);
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
    }

    /**
     * Adds English translations to the language file.
     *
     * @param output           The data generator output.
     * @param registriesFuture The registries future.
     * @param languageType     The language type.
     * @return The AITLanguageProvider.
     */
    public AITLanguageProvider addEnglishTranslations(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LanguageType languageType) {
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
        aitLanguageProvider.addTranslation(AITItems.RIFT_SCANNER, "Rift Scanner");
        aitLanguageProvider.addTranslation(AITItems.MECHANICAL_SONIC_SCREWDRIVER, "Mechanical Sonic Screwdriver");
        aitLanguageProvider.addTranslation(AITItems.RENAISSANCE_SONIC_SCREWDRIVER, "Renaissance Sonic Screwdriver");
        aitLanguageProvider.addTranslation(AITItems.CORAL_SONIC_SCREWDRIVER, "Coral Sonic Screwdriver");
        aitLanguageProvider.addTranslation(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
        aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
        aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
        aitLanguageProvider.addTranslation(AITBlocks.RADIO, "Radio");
        aitLanguageProvider.addTranslation(AITBlocks.EXTERIOR_BLOCK, "Exterior");
        aitLanguageProvider.addTranslation(AITBlocks.CORAL_PLANT, "TARDIS Coral");
        aitLanguageProvider.addTranslation("death.attack.tardis_squash", "%1$s got squashed by a TARDIS!");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info1", "Artron Chunk Info: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info2", "Artron left in chunk: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info3", "This is not a rift chunk");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.holdformoreinfo", "Hold shift for more info");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.active", "Protocol 116: ACTIVE");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.inactive", "Protocol 116: INACTIVE");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning1", "The TARDIS is out of fuel and cannot dematerialize");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning2", "The TARDIS is refueling and is unable to dematerialize");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning3", "Cannot translocate exterior to interior dimension");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.notardis", "Remote does not identify with any TARDIS");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.active", "Antigravs: ACTIVE");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.inactive", "Antigravs: INACTIVE");
        aitLanguageProvider.addTranslation("message.ait.tardis.control.dimension.info", "Dimension: ");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.destination_nonexistent", "Fast Return: Last Position Nonexistent!");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.last_position", "Fast Return: LAST POSITION SET");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.current_position", "Fast Return: CURRENT POSITION SET");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_813.active", "Protocol 813: ACTIVE");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_813.inactive", "Protocol 813: INACTIVE");
        aitLanguageProvider.addTranslation("tardis.message.control.handbrake.on", "Handbrake: ON");
        aitLanguageProvider.addTranslation("tardis.message.control.handbrake.off", "Handbrake: OFF");
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
        aitLanguageProvider.addTranslation("message.ait.sonic.handbrakedisengaged", "Handbrake disengaged, destination set to current position");
        aitLanguageProvider.addTranslation("message.ait.sonic.mode","Mode: ");
        aitLanguageProvider.addTranslation("message.ait.sonic.none", "None");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning4", "Target has been reset and updated, the device is now pointing towards your new target");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.upgrade","Upgrade");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.key", "Key Type: ");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.ingredient", "Material: ");
        aitLanguageProvider.addTranslation("tooltip.ait.key.notardis", "Key does not identify with any TARDIS");
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
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.not_enough_fuel", "The TARDIS does not have enough fuel to change it's interior");
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.warning", "Interior reconfiguration started! Please leave the interior.");
        aitLanguageProvider.addTranslation("command.ait.realworld.response", "Spawned a real world TARDIS at: ");

        return aitLanguageProvider;
    }

    /**
     * Adds French translations to the language file.
     *
     * @param output           The data generator output.
     * @param registriesFuture The registries future.
     * @param languageType     The language type.
     * @return The AITLanguageProvider.
     */
    public AITLanguageProvider addFrenchTranslations(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LanguageType languageType) {
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
        aitLanguageProvider.addTranslation(AITItems.MECHANICAL_SONIC_SCREWDRIVER, "Tournevis Sonique Mécanique");
        aitLanguageProvider.addTranslation(AITItems.RENAISSANCE_SONIC_SCREWDRIVER, "Tournevis Sonique Renaissance");
        aitLanguageProvider.addTranslation(AITItems.CORAL_SONIC_SCREWDRIVER, "Tournevis Sonique Coral");
        aitLanguageProvider.addTranslation(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE, "Modèle de forge");
        aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE, "Modèle de forge");
        aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE, "Modèle de forge");
        aitLanguageProvider.addTranslation(AITBlocks.RADIO, "Radio");
        aitLanguageProvider.addTranslation(AITBlocks.EXTERIOR_BLOCK, "Exterieur");
        aitLanguageProvider.addTranslation(AITBlocks.CORAL_PLANT, "Corail TARDIS");
        aitLanguageProvider.addTranslation("death.attack.tardis_squash", "%1$s a été écrasé(e) par un TARDIS!");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info1", "Information sur le Chunk à Artron: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info2", "Artron laissé dans le chunk: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info3", "Ceci n'est pas un chunk à faille");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.holdformoreinfo", "Maintenez la touche shift pour plus d'informations");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.active", "Protocole 116: ACTIF");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.inactive", "Protocole 116: INACTIF");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning1", "Le TARDIS n’a plus de carburant et ne peux plus se dématérialiser");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning2", "Le TARDIS est en train de se recharger et est incapable de se dématérialiser");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning3", "Impossible de passer de la dimension extérieure à la dimension intérieure");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.notardis", "La télécommande n’est pas connecté avec le TARDIS");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.active", "Antigravs: ACTIF");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.inactive", "Antigravs: INACTIF");
        aitLanguageProvider.addTranslation("message.ait.tardis.control.dimension.info", "Dimension: ");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.destination_nonexistent", "Retour Rapide: Dernière position inexistante!");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.last_position", " Retour Rapide: DERNIÈRE POSITION DÉFINIE");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.current_position", "Fast Return: POSITION ACTUELLE DÉFINIE");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_813.active", "Protocole 813: ACTIF");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_813.inactive", "Protocole 813: INACTF");
        aitLanguageProvider.addTranslation("tardis.message.control.handbrake.on", "Frein à main: ON");
        aitLanguageProvider.addTranslation("tardis.message.control.handbrake.off", "Frein à main: OFF");
        aitLanguageProvider.addTranslation("tardis.message.control.landtype.on", "Recherche Surface: ON");
        aitLanguageProvider.addTranslation("tardis.message.control.landtype.off", "Recherche Surface: OFF");
        aitLanguageProvider.addTranslation("tardis.message.control.randomiser.destination", "Destination: ");
        aitLanguageProvider.addTranslation("tardis.message.control.refueler.enabled", "Rechargement: Activé");
        aitLanguageProvider.addTranslation("tardis.message.control.refueler.disabled", "Rechargement: Désactivé");
        aitLanguageProvider.addTranslation("tardis.message.destination_biome", "Biome de Destination: ");
        aitLanguageProvider.addTranslation("tardis.message.control.increment.info", "Incrément: ");
        aitLanguageProvider.addTranslation("tardis.message.control.randomiser.poscontrol", "Destination: ");
        aitLanguageProvider.addTranslation("command.ait.riftchunk.isariftchunk", "Ceci est un chunk à faille");
        aitLanguageProvider.addTranslation("command.ait.riftchunk.isnotariftchunk", "Ceci n’est pas un chunk à faille");
        aitLanguageProvider.addTranslation("message.ait.sonic.riftfound", "CHUNK À FAILLE TROUVÉ");
        aitLanguageProvider.addTranslation("message.ait.sonic.riftnotfound", "CHUNK À FAILLE NON TROUVÉ");
        aitLanguageProvider.addTranslation("message.ait.sonic.handbrakedisengaged", "Frein à main desserré, destination définie à la position actuelle");
        aitLanguageProvider.addTranslation("message.ait.sonic.mode","Mode: ");
        aitLanguageProvider.addTranslation("message.ait.sonic.none", "Aucun");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning4", "La cible a été réinitialisée et mise à jour, l'appareil est maintenant orienté vers votre nouvelle cible");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.upgrade","Amélioration");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.key", "Type de Clé: ");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.ingredient", "Matériau: ");
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
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.not_enough_fuel", "The TARDIS does not have enough fuel to change it's interior");
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.warning", "Interior reconfiguration started! Please leave the interior.");
        aitLanguageProvider.addTranslation("command.ait.realworld.response", "Spawned a real world TARDIS at: ");


        return aitLanguageProvider;
    }

    /**
     * Adds Spanish translations to the language file.
     *
     * @param output           The data generator output.
     * @param registriesFuture The registries future.
     * @param languageType     The language type.
     * @return The AITLanguageProvider.
     */
    public AITLanguageProvider addSpanishTranslations(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LanguageType languageType) {
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
        aitLanguageProvider.addTranslation(AITItems.MECHANICAL_SONIC_SCREWDRIVER, "Mechanical Sonic Screwdriver");
        aitLanguageProvider.addTranslation(AITItems.RENAISSANCE_SONIC_SCREWDRIVER, "Renaissance Sonic Screwdriver");
        aitLanguageProvider.addTranslation(AITItems.CORAL_SONIC_SCREWDRIVER, "Coral Sonic Screwdriver");
        aitLanguageProvider.addTranslation(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
        aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
        aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
        aitLanguageProvider.addTranslation(AITBlocks.RADIO, "Radio");
        aitLanguageProvider.addTranslation(AITBlocks.EXTERIOR_BLOCK, "Exterior");
        aitLanguageProvider.addTranslation(AITBlocks.CORAL_PLANT, "TARDIS Coral");
        aitLanguageProvider.addTranslation("death.attack.tardis_squash", "%1$s got squashed by a TARDIS!");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info1", "Artron Chunk Info: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info2", "Artron left in chunk: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info3", "This is not a rift chunk");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.holdformoreinfo", "Hold shift for more info");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.active", "Protocol 116: ACTIVE");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.inactive", "Protocol 116: INACTIVE");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning1", "The TARDIS is out of fuel and cannot dematerialize");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning2", "The TARDIS is refueling and is unable to dematerialize");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning3", "Cannot translocate exterior to interior dimension");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.notardis", "Remote does not identify with any TARDIS");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.active", "Antigravs: ACTIVE");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.inactive", "Antigravs: INACTIVE");
        aitLanguageProvider.addTranslation("message.ait.tardis.control.dimension.info", "Dimension: ");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.destination_nonexistent", "Fast Return: Last Position Nonexistent!");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.last_position", "Fast Return: LAST POSITION SET");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.current_position", "Fast Return: CURRENT POSITION SET");
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
        aitLanguageProvider.addTranslation("message.ait.sonic.handbrakedisengaged", "Handbrake disengaged, destination set to current position");
        aitLanguageProvider.addTranslation("message.ait.sonic.mode","Mode: ");
        aitLanguageProvider.addTranslation("message.ait.sonic.none", "None");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning4", "Target has been reset and updated, the device is now pointing towards your new target");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.upgrade","Upgrade");
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
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.not_enough_fuel", "The TARDIS does not have enough fuel to change it's interior");
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.warning", "Interior reconfiguration started! Please leave the interior.");
        aitLanguageProvider.addTranslation("command.ait.realworld.response", "Spawned a real world TARDIS at: ");

        return aitLanguageProvider;
    }

    public AITLanguageProvider addGermanTranslations(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LanguageType languageType) {
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
        aitLanguageProvider.addTranslation(AITItems.MECHANICAL_SONIC_SCREWDRIVER, "Mechanischer Schallschraubenzieher");
        aitLanguageProvider.addTranslation(AITItems.RENAISSANCE_SONIC_SCREWDRIVER, "Renaissance Schallschraubenzieher");
        aitLanguageProvider.addTranslation(AITItems.CORAL_SONIC_SCREWDRIVER, "Korallen Schallschraubenzieher");
        aitLanguageProvider.addTranslation(AITItems.GOLD_KEY_UPGRADE_SMITHING_TEMPLATE, "Schmiedevorlage");
        aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY_UPGRADE_SMITHING_TEMPLATE, "Schmiedevorlage");
        aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY_UPGRADE_SMITHING_TEMPLATE, "Schmiedevorlage");
        aitLanguageProvider.addTranslation(AITBlocks.RADIO, "Radio");
        aitLanguageProvider.addTranslation(AITBlocks.EXTERIOR_BLOCK, "Äußere Hülle");
        aitLanguageProvider.addTranslation(AITBlocks.CORAL_PLANT, "TARDIS Koralle");
        aitLanguageProvider.addTranslation("death.attack.tardis_squash", "%1$s wurde von einer TARDIS zerquetscht!");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info1", "Artron Chunk Info: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info2", "Artron noch im Chunk: ");
        aitLanguageProvider.addTranslation("message.ait.riftscanner.info3", "Dies ist kein Riss-Chunk");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.holdformoreinfo", "Shift halten für weitere Informationen");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.active", "Protokoll 116: AKTIV");
        aitLanguageProvider.addTranslation("tardis.message.control.protocol_116.inactive", "Protocol 116: INACTIVE");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning1", "Die TARDIS benötigt Treibstoff und kann sich nicht dematerialisieren");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning2", "Die TARDIS tankt und kann sich nicht dematerialisieren");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning3", "Äußere Hülle kann nicht in die innere Dimension verschoben werden");
        aitLanguageProvider.addTranslation("tooltip.ait.remoteitem.notardis", "Fernbedienung identifiziert sich mit keiner TARDIS");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.active", "Antigravitation: AKTIVIERT");
        aitLanguageProvider.addTranslation("tardis.message.control.antigravs.inactive", "Antigravitation: DEAKTIVIERT");
        aitLanguageProvider.addTranslation("message.ait.tardis.control.dimension.info", "Dimension: ");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.destination_nonexistent", "Rückreise: Letzte Position existiert nicht!");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.last_position", "Rückreise: LETZTE POSITION GESETZT");
        aitLanguageProvider.addTranslation("tardis.message.control.fast_return.current_position", "Rückreise: JETZIGE POSITION GESETZT");
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
        aitLanguageProvider.addTranslation("message.ait.sonic.handbrakedisengaged", "Handbremse deaktiviert, Koordinaten auf jetzige Position gesetzt");
        aitLanguageProvider.addTranslation("message.ait.sonic.mode","Modus: ");
        aitLanguageProvider.addTranslation("message.ait.sonic.none", "Keiner");
        aitLanguageProvider.addTranslation("message.ait.remoteitem.warning4", "Ziel wurde zurückgesetzt und aktualisiert, das Gerät zeigt nun in Richtung des neuen Ziels");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.upgrade","Upgrade");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.key", "Schlüsseltyp: ");
        aitLanguageProvider.addTranslation("message.ait.keysmithing.ingredient", "Material: ");
        aitLanguageProvider.addTranslation("tooltip.ait.key.notardis", "Schlüssel identifiziert sich mit keiner TARDIS");
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
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.not_enough_fuel", "The TARDIS does not have enough fuel to change it's interior");
        aitLanguageProvider.addTranslation("tardis.message.interiorchange.warning", "Interior reconfiguration started! Please leave the interior.");
        aitLanguageProvider.addTranslation("command.ait.realworld.response", "Spawned a real world TARDIS at:");

        return aitLanguageProvider;
    }

    public void generate_DE_AT_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> addGermanTranslations(output, registriesFuture, LanguageType.DE_AT))); // de_at (German Austria)
    }

    public void generate_DE_CH_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> addGermanTranslations(output, registriesFuture, LanguageType.DE_CH))); // de_ch (German Switzerland)
    }

    public void generate_DE_DE_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> addGermanTranslations(output, registriesFuture, LanguageType.DE_DE))); // de_de (German Germany)
    }

    public void generate_NDS_DE_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> addGermanTranslations(output, registriesFuture, LanguageType.NDS_DE))); // nds_de (Nordic German)
    }

    public void generate_EN_US_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> addEnglishTranslations(output, registriesFuture, LanguageType.EN_US))); // en_us (English US)
    }

    public void generate_EN_UK_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> addEnglishTranslations(output, registriesFuture, LanguageType.EN_UK))); // en_uk (English UK)
    }

    public void generate_FR_CA_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> addFrenchTranslations(output, registriesFuture, LanguageType.FR_CA)))); // fr_ca (French Canadian)
    }

    public void generate_FR_FR_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> addFrenchTranslations(output, registriesFuture, LanguageType.FR_FR)))); // fr_fr (French France)
    }

    public void generate_ES_AR_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_AR)))); // es_ar (Spanish Argentina)
    }

    public void generate_ES_CL_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_CL)))); // es_cl (Spanish Chile)
    }

    public void generate_ES_EC_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_EC)))); // es_ec (Spanish Ecuador)
    }

    public void generate_ES_ES_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_ES)))); // es_es (Spanish Spain)
    }

    public void generate_ES_MX_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_MX)))); // es_mx (Spanish Mexico)
    }

    public void generate_ES_UY_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_UY)))); // es_uy (Spanish Uruguay)
    }

    public void generate_ES_VE_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider((((output, registriesFuture) -> addSpanishTranslations(output, registriesFuture, LanguageType.ES_VE)))); // es_ve (Spanish Venezuela)
    }

    public void generate_EN_AU_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> addEnglishTranslations(output, registriesFuture, LanguageType.EN_AU))); // en_au (English Australia)
    }

    public void generate_EN_CA_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> addEnglishTranslations(output, registriesFuture, LanguageType.EN_CA))); // en_ca (English Canada)
    }

    public void generate_EN_GB_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> addEnglishTranslations(output, registriesFuture, LanguageType.EN_GB))); // en_gb (English Great Britain)
    }

    public void generate_EN_NZ_Language(FabricDataGenerator.Pack pack) {
        pack.addProvider(((output, registriesFuture) -> addEnglishTranslations(output, registriesFuture, LanguageType.EN_NZ))); // en_nz (English New Zealand)
    }
}
