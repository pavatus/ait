package loqor.ait.registry.impl.console.variant;

import loqor.ait.AITMod;
import loqor.ait.registry.unlockable.UnlockableRegistry;
import loqor.ait.core.data.schema.console.ConsoleTypeSchema;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import loqor.ait.core.data.datapack.DatapackConsole;
import loqor.ait.tardis.console.variant.alnico.AlnicoVariant;
import loqor.ait.tardis.console.variant.alnico.BlueAlnicoVariant;
import loqor.ait.tardis.console.variant.coral.BlueCoralVariant;
import loqor.ait.tardis.console.variant.coral.CoralVariant;
import loqor.ait.tardis.console.variant.coral.WhiteCoralVariant;
import loqor.ait.tardis.console.variant.hartnell.HartnellVariant;
import loqor.ait.tardis.console.variant.hartnell.KeltHartnellVariant;
import loqor.ait.tardis.console.variant.hartnell.MintHartnellVariant;
import loqor.ait.tardis.console.variant.hartnell.WoodenHartnellVariant;
import loqor.ait.tardis.console.variant.steam.SteamCherryVariant;
import loqor.ait.tardis.console.variant.steam.SteamGildedVariant;
import loqor.ait.tardis.console.variant.steam.SteamSteelVariant;
import loqor.ait.tardis.console.variant.steam.SteamVariant;
import loqor.ait.tardis.console.variant.toyota.ToyotaBlueVariant;
import loqor.ait.tardis.console.variant.toyota.ToyotaLegacyVariant;
import loqor.ait.tardis.console.variant.toyota.ToyotaVariant;
import loqor.ait.core.data.datapack.DatapackExterior;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class ConsoleVariantRegistry extends UnlockableRegistry<ConsoleVariantSchema> {
	private static ConsoleVariantRegistry INSTANCE;

	protected ConsoleVariantRegistry() {
		super(DatapackConsole::fromInputStream, null, "console_variants", "console", true);
	}

	public static ConsoleVariantSchema registerStatic(ConsoleVariantSchema schema) {
		return ConsoleVariantRegistry.getInstance().register(schema);
	}

	@Override
	public ConsoleVariantSchema fallback() {
		return null;
	}

	@Override
	public void syncToClient(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(REGISTRY.size());

		for (ConsoleVariantSchema schema : REGISTRY.values()) {
			if (schema instanceof DatapackConsole variant) {
				buf.encodeAsJson(DatapackConsole.CODEC, variant);
				continue;
			}

			buf.encodeAsJson(DatapackConsole.CODEC, new DatapackConsole(schema.id(), schema.parent().id(), DatapackExterior.DEFAULT_TEXTURE, DatapackExterior.DEFAULT_TEXTURE, false));
		}

		ServerPlayNetworking.send(player, this.packet, buf);
	}

	@Override
	public void readFromServer(PacketByteBuf buf) {
		PacketByteBuf copy = PacketByteBufs.copy(buf);
		ClientConsoleVariantRegistry.getInstance().readFromServer(copy);

		REGISTRY.clear();

		this.defaults();
		int size = buf.readInt();

		for (int i = 0; i < size; i++) {
			DatapackConsole variant = buf.decodeAsJson(DatapackConsole.CODEC);
			if (!variant.wasDatapack())
				continue;

			this.register(variant);
		}

		AITMod.LOGGER.info("Read {} console variants from server", size);
	}

	public static ConsoleVariantRegistry getInstance() {
		if (INSTANCE == null) {
			AITMod.LOGGER.debug("ConsoleVariantRegistry was not initialized, Creating a new instance");
			INSTANCE = new ConsoleVariantRegistry();
		}

		return INSTANCE;
	}

	public static List<ConsoleVariantSchema> withParent(ConsoleTypeSchema parent) {
		List<ConsoleVariantSchema> list = new ArrayList<>();

		for (ConsoleVariantSchema schema : ConsoleVariantRegistry.getInstance().REGISTRY.values()) {
			if (schema.parent().equals(parent)) list.add(schema);
		}

		return list;
	}

	public static ConsoleVariantSchema HARTNELL;
	public static ConsoleVariantSchema HARTNELL_WOOD;
	public static ConsoleVariantSchema HARTNELL_KELT;
	public static ConsoleVariantSchema HARTNELL_MINT;
	public static ConsoleVariantSchema CORAL;
	public static ConsoleVariantSchema CORAL_BLUE;
	public static ConsoleVariantSchema CORAL_WHITE;
	public static ConsoleVariantSchema TOYOTA;
	public static ConsoleVariantSchema TOYOTA_BLUE;
	public static ConsoleVariantSchema TOYOTA_LEGACY;
	public static ConsoleVariantSchema ALNICO;
	public static ConsoleVariantSchema ALNICO_BLUE;
	public static ConsoleVariantSchema STEAM;
	public static ConsoleVariantSchema STEAM_CHERRY;
	public static ConsoleVariantSchema STEAM_STEEL;
	public static ConsoleVariantSchema STEAM_GILDED;

	@Override
	protected void defaults() {
		// Hartnell variants
		HARTNELL = registerStatic(new HartnellVariant());
		HARTNELL_KELT = registerStatic(new KeltHartnellVariant());
		HARTNELL_MINT = registerStatic(new MintHartnellVariant());
		HARTNELL_WOOD = registerStatic(new WoodenHartnellVariant()); // fixme this texture is awful - make tright remake it

		// Coral variants
		CORAL = registerStatic(new CoralVariant());
		CORAL_BLUE = registerStatic(new BlueCoralVariant());
		CORAL_WHITE = registerStatic(new WhiteCoralVariant());

		// Toyota variants
		TOYOTA = registerStatic(new ToyotaVariant());
		TOYOTA_BLUE = registerStatic(new ToyotaBlueVariant());
		TOYOTA_LEGACY = registerStatic(new ToyotaLegacyVariant());

		// Alnico variants
		ALNICO = registerStatic(new AlnicoVariant());
		ALNICO_BLUE = registerStatic(new BlueAlnicoVariant());

		// Steam variants
		STEAM = registerStatic(new SteamVariant());
		STEAM_CHERRY = registerStatic(new SteamCherryVariant());
		STEAM_STEEL = registerStatic(new SteamSteelVariant());
		STEAM_GILDED = registerStatic(new SteamGildedVariant());
	}
}
