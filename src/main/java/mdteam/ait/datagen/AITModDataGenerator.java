package mdteam.ait.datagen;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.AITSounds;
import mdteam.ait.datagen.datagen_providers.AITLanguageProvider;
import mdteam.ait.datagen.datagen_providers.AITModelProvider;
import mdteam.ait.datagen.datagen_providers.AITSoundProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class AITModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		generateLanguages(pack);
		generateBlockModels(pack);
		generateSoundData(pack);
	}

	public void generateSoundData(FabricDataGenerator.Pack pack) {
		pack.addProvider((((output, registriesFuture) ->  {
			AITSoundProvider provider = new AITSoundProvider(output);

			provider.addSound("secret_music", AITSounds.SECRET_MUSIC);

			// TARDIS
			provider.addSound("tardis/demat", AITSounds.DEMAT);
			provider.addSound("tardis/mat", AITSounds.MAT);
			provider.addSound("hop_demat", AITSounds.HOP_DEMAT);
			provider.addSound("hop_mat", AITSounds.HOP_MAT);
			provider.addSound("fail_demat", AITSounds.FAIL_DEMAT);
			provider.addSound("fail_mat", AITSounds.FAIL_MAT);
			provider.addSound("emergency_mat", AITSounds.EMERG_MAT);
			provider.addSound("tardis/eighth_demat", AITSounds.EIGHT_DEMAT);
			provider.addSound("tardis/eighth_mat", AITSounds.EIGHT_MAT);

			// Controls
			provider.addSound("demat_lever_pull", AITSounds.DEMAT_LEVER_PULL);
			provider.addSound("handbrake_lever_pull", AITSounds.HANDBRAKE_LEVER_PULL);

			return provider;
		})));
	}

	public void generateBlockModels(FabricDataGenerator.Pack pack) {
		pack.addProvider(((output, registriesFuture) -> {
			AITModelProvider aitModelProvider = new AITModelProvider(output);
			aitModelProvider.registerDirectionalBlock(AITBlocks.RADIO);
			aitModelProvider.registerDirectionalBlock(AITBlocks.DISPLAY_CONSOLE);
			aitModelProvider.registerDirectionalBlock(AITBlocks.EXTERIOR_BLOCK);
			aitModelProvider.registerDirectionalBlock(AITBlocks.DOOR_BLOCK);

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
	 * @param output The data generator output.
	 * @param registriesFuture The registries future.
	 * @param languageType The language type.
	 * @return The AITLanguageProvider.
	 */
	public AITLanguageProvider addEnglishTranslations(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LanguageType languageType) {
		AITLanguageProvider aitLanguageProvider = new AITLanguageProvider(output, languageType);

		aitLanguageProvider.addTranslation(AITMod.AIT_ITEM_GROUP, "Adventures In Time");
		aitLanguageProvider.addTranslation(AITItems.TARDIS_ITEM, "TARDIS");
		// aitLanguageProvider.addTranslation(AITItems.TOYOTA_ITEM, "Toyota Item | TEMP |");
		aitLanguageProvider.addTranslation(AITItems.REMOTE_ITEM, "Stattenheim Remote");
		aitLanguageProvider.addTranslation(AITItems.IRON_KEY, "Iron Key");
		aitLanguageProvider.addTranslation(AITItems.GOLD_KEY, "Gold Key");
		aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY, "Netherite Key");
		aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY, "Classic Key");
		aitLanguageProvider.addTranslation(AITBlocks.DISPLAY_CONSOLE, "Console");
		aitLanguageProvider.addTranslation(AITBlocks.RADIO, "Radio");
		aitLanguageProvider.addTranslation(AITBlocks.EXTERIOR_BLOCK, "Exterior");
		aitLanguageProvider.addTranslation(AITBlocks.DOOR_BLOCK, "Door");

		return aitLanguageProvider;
	}

	/**
	 * Adds French translations to the language file.
	 * @param output The data generator output.
	 * @param registriesFuture The registries future.
	 * @param languageType The language type.
	 * @return The AITLanguageProvider.
	 */
	public AITLanguageProvider addFrenchTranslations(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LanguageType languageType) {
		AITLanguageProvider aitLanguageProvider = new AITLanguageProvider(output, languageType);

		aitLanguageProvider.addTranslation(AITMod.AIT_ITEM_GROUP, "Le Adventures In Time");
		aitLanguageProvider.addTranslation(AITItems.TARDIS_ITEM, "Le TARDIS");
		// aitLanguageProvider.addTranslation(AITItems.TOYOTA_ITEM, "Le Toyota Item | TEMP |");
		aitLanguageProvider.addTranslation(AITItems.REMOTE_ITEM, "Le Stattenheim Remote");
		aitLanguageProvider.addTranslation(AITItems.IRON_KEY, "Le Iron Key");
		aitLanguageProvider.addTranslation(AITItems.GOLD_KEY, "Le Gold Key");
		aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY, "Le Netherite Key");
		aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY, "Le Classic Key");
		aitLanguageProvider.addTranslation(AITBlocks.DISPLAY_CONSOLE, "Le Console");
		aitLanguageProvider.addTranslation(AITBlocks.RADIO, "Le Radio");
		aitLanguageProvider.addTranslation(AITBlocks.EXTERIOR_BLOCK, "Le Exterior");
		aitLanguageProvider.addTranslation(AITBlocks.DOOR_BLOCK, "Le Door");

		return aitLanguageProvider;
	}

	/**
	 * Adds Spanish translations to the language file.
	 * @param output The data generator output.
	 * @param registriesFuture The registries future.
	 * @param languageType The language type.
	 * @return The AITLanguageProvider.
	 */
	public AITLanguageProvider addSpanishTranslations(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, LanguageType languageType) {
		AITLanguageProvider aitLanguageProvider = new AITLanguageProvider(output, languageType);

		aitLanguageProvider.addTranslation(AITMod.AIT_ITEM_GROUP, "Adventuro en timo");
		aitLanguageProvider.addTranslation(AITItems.TARDIS_ITEM, "TARDIS");
		// aitLanguageProvider.addTranslation(AITItems.TOYOTA_ITEM, "Toyotota | TEMP |");
		aitLanguageProvider.addTranslation(AITItems.REMOTE_ITEM, "Stato Remoto");
		aitLanguageProvider.addTranslation(AITItems.IRON_KEY, "Irono Keyo");
		aitLanguageProvider.addTranslation(AITItems.GOLD_KEY, "Goldo Keyo");
		aitLanguageProvider.addTranslation(AITItems.NETHERITE_KEY, "Netherito Keyo");
		aitLanguageProvider.addTranslation(AITItems.CLASSIC_KEY, "Classic Keyo");
		aitLanguageProvider.addTranslation(AITBlocks.DISPLAY_CONSOLE, "Consolo");
		aitLanguageProvider.addTranslation(AITBlocks.RADIO, "Radoio");
		aitLanguageProvider.addTranslation(AITBlocks.EXTERIOR_BLOCK, "Exterioro");
		aitLanguageProvider.addTranslation(AITBlocks.DOOR_BLOCK, "Dooro");

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
