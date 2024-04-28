package loqor.ait.client.registry;

import loqor.ait.client.registry.console.ClientConsoleVariantSchema;
import loqor.ait.client.registry.console.impl.*;
import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.registry.DatapackRegistry;
import loqor.ait.tardis.console.variant.ConsoleVariantSchema;
import loqor.ait.tardis.console.variant.DatapackConsole;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ClientConsoleVariantRegistry extends DatapackRegistry<ClientConsoleVariantSchema> {
	private static ClientConsoleVariantRegistry INSTANCE;

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
	 * @return the first variant found as there should only be one client version
	 */
	public static ClientConsoleVariantSchema withParent(ConsoleVariantSchema parent) {
		for (ClientConsoleVariantSchema schema : ClientConsoleVariantRegistry.getInstance().toList()) {
			if (schema.parent() == null)
				continue;

			if (schema.parent().id().equals(parent.id()))
				return schema;
		}

		return null;
	}

	public static ClientConsoleVariantSchema withSameParent(ClientConsoleVariantSchema schema) {
		for (ClientConsoleVariantSchema s : ClientConsoleVariantRegistry.getInstance().toList()) {
			if (s.parent() == null)
				continue;

			if (schema.equals(s))
				continue;

			if (s.parent().parent().id().equals(schema.parent().parent().id()))
				return s;
		}

		return null;
	}

	@Override
	public ClientConsoleVariantSchema fallback() {
		return null;
	}

	@Override
	public void syncToClient(ServerPlayerEntity player) { }

	@Override
	public void readFromServer(PacketByteBuf buf) {
		int size = buf.readInt();

		for (int i = 0; i < size; i++) {
			this.register(convertDatapack(buf.decodeAsJson(DatapackConsole.CODEC)));
		}

		AITMod.LOGGER.info("Read {} console variants from server", size);
	}

	public static ClientConsoleVariantSchema convertDatapack(DatapackConsole variant) {
		if (!variant.wasDatapack())
			return convertNonDatapack(variant);

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
		if (variant.wasDatapack())
			return convertDatapack(variant);

		return getInstance().get(variant.id());
	}

	public static ClientConsoleVariantSchema HARTNELL;
	public static ClientConsoleVariantSchema HARTNELL_WOOD;
	public static ClientConsoleVariantSchema HARTNELL_KELT;
	public static ClientConsoleVariantSchema HARTNELL_MINT;
	public static ClientConsoleVariantSchema CORAL_GREEN;
	public static ClientConsoleVariantSchema CORAL_BLUE;
	public static ClientConsoleVariantSchema CORAL_WHITE;
	public static ClientConsoleVariantSchema TOYOTA;
	public static ClientConsoleVariantSchema TOYOTA_BLUE;
	public static ClientConsoleVariantSchema TOYOTA_LEGACY;
	public static ClientConsoleVariantSchema ALNICO;
	public static ClientConsoleVariantSchema STEAM;
	public static ClientConsoleVariantSchema STEAM_CHERRY;

	public void init() {
		// Hartnell variants
		HARTNELL = register(new ClientHartnellVariant());
		HARTNELL_KELT = register(new ClientKeltHartnellVariant());
		HARTNELL_MINT = register(new ClientMintHartnellVariant());
		HARTNELL_WOOD = register(new ClientWoodenHartnellVariant());

		// Coral variants
		CORAL_GREEN = register(new ClientGreenCoralVariant());
		CORAL_BLUE = register(new ClientBlueCoralVariant());
		CORAL_WHITE = register(new ClientWhiteCoralVariant());

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
