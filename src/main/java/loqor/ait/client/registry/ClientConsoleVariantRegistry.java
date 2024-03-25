package loqor.ait.client.registry;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.client.registry.console.ClientConsoleVariantSchema;
import loqor.ait.client.registry.console.impl.*;
import loqor.ait.registry.DatapackRegistry;
import loqor.ait.tardis.console.variant.ConsoleVariantSchema;
import loqor.ait.tardis.console.variant.DatapackConsole;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ClientConsoleVariantRegistry extends DatapackRegistry<ClientConsoleVariantSchema> {
	private static ClientConsoleVariantRegistry INSTANCE;

	public static ClientConsoleVariantSchema registerStatic(ClientConsoleVariantSchema schema) {
		return getInstance().register(schema);
	}

	public static DatapackRegistry<ClientConsoleVariantSchema> getInstance() {
		if (INSTANCE == null) {
			AITMod.LOGGER.info("ClientConsoleVariantRegistry was not initialized, Creating a new instance");
			INSTANCE = new ClientConsoleVariantRegistry();
			INSTANCE.init();
		}

		return INSTANCE;
	}

	/**
	 * Will return the clients version of a servers door variant
	 *
	 * @param parent
	 * @return the first variant found as there should only be one client version
	 */
	public static ClientConsoleVariantSchema withParent(ConsoleVariantSchema parent) {
		for (ClientConsoleVariantSchema schema : getInstance().toArrayList()) {
			if (schema.parent() == null) continue;
			if (schema.parent().id().equals(parent.id())) return schema;
		}

		return null;
	}

	public static ClientConsoleVariantSchema withSameParent(ClientConsoleVariantSchema schema) {
		for (ClientConsoleVariantSchema s : getInstance().toArrayList()) {
			if (s.parent() == null) continue;
			if (schema.equals(s)) continue;
			if (s.parent().parent().id().equals(schema.parent().parent().id())) return s;
		}

		return null;
	}

	/**
	 * Do not call
	 */
	@Override
	public void syncToClient(ServerPlayerEntity player) {

	}

	@Override
	public void readFromServer(PacketByteBuf buf) {
		int size = buf.readInt();

		if (size == 0) return;

		for (int i = 0; i < size; i++) {
			ClientConsoleVariantSchema variantSchema = convertDatapack(buf.decodeAsJson(DatapackConsole.CODEC));
			if (variantSchema == null) return;
			this.register(variantSchema);
		}

		AITMod.LOGGER.info("Read {} console variants from server", size);
	}

	public static ClientConsoleVariantSchema convertDatapack(DatapackConsole variant) {
		if (!variant.wasDatapack()) return convertNonDatapack(variant);
		return new ClientConsoleVariantSchema(variant.id()) {
			@Override
			public Identifier texture() {
				return variant.texture();
			}

			@Override
			public Identifier emission() {
				return variant.emission();
			}

			@Override
			public ConsoleModel model() {
				return withSameParent(this).model();
			}
		};
	}

	private static ClientConsoleVariantSchema convertNonDatapack(DatapackConsole variant) {
		if (variant.wasDatapack()) return convertDatapack(variant);

		return getInstance().get(variant.id());
	}

	//public static ClientConsoleVariantSchema BOREALIS;
	//public static ClientConsoleVariantSchema AUTUMN;
	public static ClientConsoleVariantSchema HARTNELL;
	public static ClientConsoleVariantSchema HARTNELL_WOOD;
	public static ClientConsoleVariantSchema HARTNELL_KELT;
	public static ClientConsoleVariantSchema HARTNELL_MINT;
	public static ClientConsoleVariantSchema CORAL_GREEN;
	public static ClientConsoleVariantSchema CORAL_BLUE;
	public static ClientConsoleVariantSchema CORAL_WHITE;
	public static ClientConsoleVariantSchema COPPER;
	public static ClientConsoleVariantSchema TOYOTA;
	public static ClientConsoleVariantSchema TOYOTA_BLUE;
	public static ClientConsoleVariantSchema TOYOTA_LEGACY;
	public static ClientConsoleVariantSchema ALNICO;
	public static ClientConsoleVariantSchema STEAM;
	public static ClientConsoleVariantSchema STEAM_CHERRY;

	public void init() {
		// Borealis variants
		//BOREALIS = register(new ClientBorealisVariant());
		//AUTUMN = register(new ClientAutumnVariant());

		// Hartnell variants
		HARTNELL = register(new ClientHartnellVariant());
		HARTNELL_KELT = register(new ClientKeltHartnellVariant());
		HARTNELL_MINT = register(new ClientMintHartnellVariant());
		HARTNELL_WOOD = register(new ClientWoodenHartnellVariant()); // fixme this texture is awful - make tright remake it

		// Coral variants
		CORAL_GREEN = register(new ClientGreenCoralVariant());
		CORAL_BLUE = register(new ClientBlueCoralVariant());
		CORAL_WHITE = register(new ClientWhiteCoralVariant());

		// Copper variants
		//COPPER = register(new ClientCopperVariant());

		// Toyota variants
		TOYOTA = register(new ClientToyotaVariant());
		TOYOTA_BLUE = register(new ClientToyotaBlueVariant());
		TOYOTA_LEGACY = register(new ClientToyotaLegacyVariant());

		// Alnico variants
		ALNICO = register(new ClientAlnicoVariant());

		// Steam variants
		STEAM = register(new ClientSteamVariant());
		STEAM_CHERRY = register(new ClientSteamCherryVariant());
	}
}
