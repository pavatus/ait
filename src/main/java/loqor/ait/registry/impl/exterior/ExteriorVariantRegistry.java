package loqor.ait.registry.impl.exterior;

import loqor.ait.AITMod;
import loqor.ait.registry.datapack.DatapackRegistry;
import loqor.ait.registry.unlockable.UnlockableRegistry;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.datapack.DatapackExterior;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.tardis.exterior.variant.bookshelf.BookshelfDefaultVariant;
import loqor.ait.tardis.exterior.variant.booth.*;
import loqor.ait.tardis.exterior.variant.box.*;
import loqor.ait.tardis.exterior.variant.capsule.CapsuleDefaultVariant;
import loqor.ait.tardis.exterior.variant.capsule.CapsuleFireVariant;
import loqor.ait.tardis.exterior.variant.capsule.CapsuleSoulVariant;
import loqor.ait.tardis.exterior.variant.classic.*;
import loqor.ait.tardis.exterior.variant.doom.DoomVariant;
import loqor.ait.tardis.exterior.variant.easter_head.EasterHeadDefaultVariant;
import loqor.ait.tardis.exterior.variant.easter_head.EasterHeadFireVariant;
import loqor.ait.tardis.exterior.variant.easter_head.EasterHeadSoulVariant;
import loqor.ait.tardis.exterior.variant.geometric.GeometricDefaultVariant;
import loqor.ait.tardis.exterior.variant.geometric.GeometricFireVariant;
import loqor.ait.tardis.exterior.variant.geometric.GeometricSoulVariant;
import loqor.ait.tardis.exterior.variant.growth.CoralGrowthVariant;
import loqor.ait.tardis.exterior.variant.plinth.PlinthDefaultVariant;
import loqor.ait.tardis.exterior.variant.plinth.PlinthFireVariant;
import loqor.ait.tardis.exterior.variant.plinth.PlinthSoulVariant;
import loqor.ait.tardis.exterior.variant.renegade.RenegadeCabinetVariant;
import loqor.ait.tardis.exterior.variant.renegade.RenegadeDefaultVariant;
import loqor.ait.tardis.exterior.variant.renegade.RenegadeTronVariant;
import loqor.ait.tardis.exterior.variant.tardim.TardimDefaultVariant;
import loqor.ait.tardis.exterior.variant.tardim.TardimFireVariant;
import loqor.ait.tardis.exterior.variant.tardim.TardimSoulVariant;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExteriorVariantRegistry extends UnlockableRegistry<ExteriorVariantSchema> {
	private static ExteriorVariantRegistry INSTANCE;

	protected ExteriorVariantRegistry() {
		super(DatapackExterior::fromInputStream, null, "exterior", true);
	}

	@Override
	public ExteriorVariantSchema fallback() {
		return ExteriorVariantRegistry.BOX_DEFAULT;
	}

	@Override
	public void syncToClient(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(REGISTRY.size());

		for (ExteriorVariantSchema schema : REGISTRY.values()) {
			if (schema instanceof DatapackExterior variant) {
				buf.encodeAsJson(DatapackExterior.CODEC, variant);
				continue;
			}

			buf.encodeAsJson(DatapackExterior.CODEC, new DatapackExterior(
					schema.name(), schema.id(), schema.categoryId(), schema.id(), DatapackExterior.DEFAULT_TEXTURE,
					DatapackExterior.DEFAULT_TEXTURE, DatapackExterior.DEFAULT_TEXTURE, false, schema.getRequirement())
			);
		}

		ServerPlayNetworking.send(player, this.packet, buf);
	}

	@Override
	public void readFromServer(PacketByteBuf buf) {
		PacketByteBuf copy = PacketByteBufs.copy(buf);
		ClientExteriorVariantRegistry.getInstance().readFromServer(copy);

		REGISTRY.clear();
		this.defaults();

		int size = buf.readInt();

		for (int i = 0; i < size; i++) {
			DatapackExterior variant = buf.decodeAsJson(DatapackExterior.CODEC);

			if (!variant.wasDatapack())
				continue;

			register(variant);
		}

		AITMod.LOGGER.info("Read {} exterior variants from server", size);
	}

	public static ExteriorVariantRegistry getInstance() {
		if (INSTANCE == null) {
			AITMod.LOGGER.debug("ExteriorVariantRegistry was not initialized, Creating a new instance");
			INSTANCE = new ExteriorVariantRegistry();
		}

		return INSTANCE;
	}

	public static List<ExteriorVariantSchema> withParent(ExteriorCategorySchema parent) {
		List<ExteriorVariantSchema> list = new ArrayList<>();

		for (ExteriorVariantSchema schema : ExteriorVariantRegistry.getInstance().REGISTRY.values()) {
			if (schema.category().equals(parent))
				list.add(schema);
		}

		return list;
	}

	public ExteriorVariantSchema pickRandomWithParent(ExteriorCategorySchema parent, Random random) {
		return DatapackRegistry.getRandom(ExteriorVariantRegistry.withParent(parent), random, this.fallback());
	}

	public ExteriorVariantSchema pickRandomWithParent(ExteriorCategorySchema parent) {
		return this.pickRandomWithParent(parent, RANDOM);
	}

	public static ExteriorVariantSchema TARDIM_DEFAULT;
	public static ExteriorVariantSchema TARDIM_FIRE;
	public static ExteriorVariantSchema TARDIM_SOUL;
	public static ExteriorVariantSchema BOX_DEFAULT;
	public static ExteriorVariantSchema BOX_FIRE;
	public static ExteriorVariantSchema BOX_SOUL;
	public static ExteriorVariantSchema BOX_FUTURE;
	public static ExteriorVariantSchema BOX_CORAL;
	public static ExteriorVariantSchema BOX_TOKAMAK;
	public static ExteriorVariantSchema BOX_CHERRY;
	public static ExteriorVariantSchema PRIME;
	public static ExteriorVariantSchema YETI;
	public static ExteriorVariantSchema DEFINITIVE;
	public static ExteriorVariantSchema PTORED;
	public static ExteriorVariantSchema MINT;
	public static ExteriorVariantSchema CAPSULE_DEFAULT;
	public static ExteriorVariantSchema CAPSULE_SOUL;
	public static ExteriorVariantSchema CAPSULE_FIRE;
	public static ExteriorVariantSchema BOOTH_DEFAULT;
	public static ExteriorVariantSchema BOOTH_FIRE;
	public static ExteriorVariantSchema BOOTH_SOUL;
	public static ExteriorVariantSchema BOOTH_VINTAGE;
	public static ExteriorVariantSchema BOOTH_BLUE;
	public static ExteriorVariantSchema HEAD_DEFAULT;
	public static ExteriorVariantSchema HEAD_SOUL;
	public static ExteriorVariantSchema HEAD_FIRE;
	public static ExteriorVariantSchema CORAL_GROWTH;
	public static ExteriorVariantSchema DOOM;
	public static ExteriorVariantSchema PLINTH_DEFAULT;
	public static ExteriorVariantSchema PLINTH_SOUL;
	public static ExteriorVariantSchema PLINTH_FIRE;
	public static ExteriorVariantSchema RENEGADE_DEFAULT;
	public static ExteriorVariantSchema RENEGADE_TRON;
	public static ExteriorVariantSchema RENEGADE_CABINET;
	public static ExteriorVariantSchema BOOKSHELF_DEFAULT;
	public static ExteriorVariantSchema GEOMETRIC_DEFAULT;
	public static ExteriorVariantSchema GEOMETRIC_FIRE;
	public static ExteriorVariantSchema GEOMETRIC_SOUL;

	@Override
	protected void defaults() {
		// TARDIM
		TARDIM_DEFAULT = register(new TardimDefaultVariant());
		TARDIM_FIRE = register(new TardimFireVariant());
		TARDIM_SOUL = register(new TardimSoulVariant());

		// Police Box
		BOX_DEFAULT = register(new PoliceBoxDefaultVariant());
		BOX_SOUL = register(new PoliceBoxSoulVariant());
		BOX_FIRE = register(new PoliceBoxFireVariant());
		BOX_FUTURE = register(new PoliceBoxFuturisticVariant());
		BOX_CORAL = register(new PoliceBoxCoralVariant());
		BOX_TOKAMAK = register(new PoliceBoxTokamakVariant());
		BOX_CHERRY = register(new PoliceBoxCherryVariant());

		// Classic Box
		PRIME = register(new ClassicBoxPrimeVariant());
		YETI = register(new ClassicBoxYetiVariant());
		DEFINITIVE = register(new ClassicBoxDefinitiveVariant());
		PTORED = register(new ClassicBoxPtoredVariant());
		MINT = register(new ClassicBoxMintVariant());

		// Capsule
		CAPSULE_DEFAULT = register(new CapsuleDefaultVariant());
		CAPSULE_SOUL = register(new CapsuleSoulVariant());
		CAPSULE_FIRE = register(new CapsuleFireVariant());

		// Booth
		BOOTH_DEFAULT = register(new BoothDefaultVariant());
		BOOTH_FIRE = register(new BoothFireVariant());
		BOOTH_SOUL = register(new BoothSoulVariant());
		BOOTH_VINTAGE = register(new BoothVintageVariant());
		BOOTH_BLUE = register(new BoothBlueVariant());

		// Easter Head
		HEAD_DEFAULT = register(new EasterHeadDefaultVariant());
		HEAD_SOUL = register(new EasterHeadSoulVariant());
		HEAD_FIRE = register(new EasterHeadFireVariant());

		// Coral Growth
		CORAL_GROWTH = register(new CoralGrowthVariant());

		// Doom
		DOOM = register(new DoomVariant());

		// Plinth
		PLINTH_DEFAULT = register(new PlinthDefaultVariant());
		PLINTH_SOUL = register(new PlinthSoulVariant());
		PLINTH_FIRE = register(new PlinthFireVariant());

		// Renegade
		RENEGADE_DEFAULT = register(new RenegadeDefaultVariant());
		RENEGADE_TRON = register(new RenegadeTronVariant());
		RENEGADE_CABINET = register(new RenegadeCabinetVariant());

		// Bookshelf
		BOOKSHELF_DEFAULT = register(new BookshelfDefaultVariant());

		// Geometric
		GEOMETRIC_DEFAULT = register(new GeometricDefaultVariant());
		GEOMETRIC_FIRE = register(new GeometricFireVariant());
		GEOMETRIC_SOUL = register(new GeometricSoulVariant());
	}
}
