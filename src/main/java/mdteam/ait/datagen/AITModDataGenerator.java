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
            provider.addShapedRecipe(ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, AITItems.REMOTE_ITEM, 1)
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
            );
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
            provider.addSound("tardis/eighth_demat", AITSounds.EIGHT_DEMAT);
            provider.addSound("tardis/eighth_mat", AITSounds.EIGHT_MAT);

            // Controls
            provider.addSound("controls/demat_lever_pull", AITSounds.DEMAT_LEVER_PULL);
            provider.addSound("controls/handbrake_lever_pull", AITSounds.HANDBRAKE_LEVER_PULL);
            provider.addSound("controls/knock", AITSounds.KNOCK);
            provider.addSound("controls/snap", AITSounds.SNAP);

            // Hums
            provider.addSound("tardis/hums/toyota_hum", AITSounds.TOYOTA_HUM);
            provider.addSound("tardis/hums/coral_hum", AITSounds.CORAL_HUM);
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
        aitLanguageProvider.addTranslation(AITBlocks.DOOR_BLOCK, "Door");
        aitLanguageProvider.addTranslation(AITBlocks.CONSOLE, "Console");
        aitLanguageProvider.addTranslation(AITItems.IRON_KEY, "Iron Key");
        aitLanguageProvider.addTranslation(AITItems.GOLD_KEY, "Gold Key");
        aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY, "Netherite Key");
        aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY, "Classic Key");
        aitLanguageProvider.addTranslation(AITItems.REMOTE_ITEM, "Stattenheim Remote");
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

        return aitLanguageProvider;
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
