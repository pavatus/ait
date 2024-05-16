package loqor.ait.registry.impl.exterior;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.core.data.datapack.DatapackExterior;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.datapack.DatapackRegistry;
import loqor.ait.tardis.exterior.variant.bookshelf.client.ClientBookshelfDefaultVariant;
import loqor.ait.tardis.exterior.variant.booth.client.*;
import loqor.ait.tardis.exterior.variant.box.client.*;
import loqor.ait.tardis.exterior.variant.capsule.client.ClientCapsuleDefaultVariant;
import loqor.ait.tardis.exterior.variant.capsule.client.ClientCapsuleFireVariant;
import loqor.ait.tardis.exterior.variant.capsule.client.ClientCapsuleSoulVariant;
import loqor.ait.tardis.exterior.variant.classic.client.*;
import loqor.ait.tardis.exterior.variant.doom.client.ClientDoomVariant;
import loqor.ait.tardis.exterior.variant.easter_head.client.ClientEasterHeadDefaultVariant;
import loqor.ait.tardis.exterior.variant.easter_head.client.ClientEasterHeadFireVariant;
import loqor.ait.tardis.exterior.variant.easter_head.client.ClientEasterHeadSoulVariant;
import loqor.ait.tardis.exterior.variant.growth.client.ClientGrowthVariant;
import loqor.ait.tardis.exterior.variant.plinth.client.ClientPlinthDefaultVariant;
import loqor.ait.tardis.exterior.variant.plinth.client.ClientPlinthFireVariant;
import loqor.ait.tardis.exterior.variant.plinth.client.ClientPlinthSoulVariant;
import loqor.ait.tardis.exterior.variant.renegade.client.ClientRenegadeCabinetVariant;
import loqor.ait.tardis.exterior.variant.renegade.client.ClientRenegadeDefaultVariant;
import loqor.ait.tardis.exterior.variant.renegade.client.ClientRenegadeTronVariant;
import loqor.ait.tardis.exterior.variant.tardim.client.ClientTardimDefaultVariant;
import loqor.ait.tardis.exterior.variant.tardim.client.ClientTardimFireVariant;
import loqor.ait.tardis.exterior.variant.tardim.client.ClientTardimSoulVariant;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class ClientExteriorVariantRegistry extends DatapackRegistry<ClientExteriorVariantSchema> {
	private static ClientExteriorVariantRegistry INSTANCE;

	public static DatapackRegistry<ClientExteriorVariantSchema> getInstance() {
		if (INSTANCE == null) {
			AITMod.LOGGER.info("ClientExteriorVariantRegistry was not initialized, Creating a new instance");
			INSTANCE = new ClientExteriorVariantRegistry();
		}

		return INSTANCE;
	}

	/**
	 * Will return the clients version of a servers door variant
	 * @return the first variant found as there should only be one client version
	 */
	public static ClientExteriorVariantSchema withParent(ExteriorVariantSchema parent) {
		for (ClientExteriorVariantSchema schema : ClientExteriorVariantRegistry.getInstance().toList()) {
			if (schema.parent() == null)
				continue;
			
			if (schema.parent().id().equals(parent.id()))
				return schema;
		}

		return null;
	}

	@Override
	public ClientExteriorVariantSchema fallback() {
		return null;
	}

	/**
	 * Do not call
	 */
	@Override
	public void syncToClient(ServerPlayerEntity player) { }

	@Override
	public void readFromServer(PacketByteBuf buf) {
		int size = buf.readInt();

		for (int i = 0; i < size; i++) {
			this.register(convertDatapack(buf.decodeAsJson(DatapackExterior.CODEC)));
		}

		AITMod.LOGGER.info("Read {} exterior variants from server", size);
	}

	public static ClientExteriorVariantSchema convertDatapack(DatapackExterior variant) {
		if (!variant.wasDatapack())
			return convertNonDatapack(variant);
		
		return new ClientExteriorVariantSchema(variant.id()) {
			@Override
			public Identifier texture() {
				return variant.texture();
			}

			@Override
			public Identifier emission() {
				return variant.emission();
			}

			@Override
			public ExteriorModel model() {
				return getInstance().get(variant.getParentId()).model();
			}

			@Override
			public Vector3f sonicItemTranslations() {
				return new Vector3f(0.5f, 1.2f, 1.2f);
			}
		};
	}

	private static ClientExteriorVariantSchema convertNonDatapack(DatapackExterior variant) {
		if (variant.wasDatapack()) 
			return convertDatapack(variant);

		return getInstance().get(variant.id());
	}

	public static ClientExteriorVariantSchema TARDIM_DEFAULT;
	public static ClientExteriorVariantSchema TARDIM_FIRE;
	public static ClientExteriorVariantSchema TARDIM_SOUL;
	public static ClientExteriorVariantSchema BOX_DEFAULT;
	public static ClientExteriorVariantSchema BOX_FIRE;
	public static ClientExteriorVariantSchema BOX_SOUL;
	public static ClientExteriorVariantSchema BOX_FUTURE;
	public static ClientExteriorVariantSchema BOX_CORAL;
	public static ClientExteriorVariantSchema BOX_CHERRY;
	public static ClientExteriorVariantSchema BOX_TOKAMAK;
	public static ClientExteriorVariantSchema PRIME;
	public static ClientExteriorVariantSchema YETI;
	public static ClientExteriorVariantSchema DEFINITIVE;
	public static ClientExteriorVariantSchema PTORED;
	public static ClientExteriorVariantSchema MINT;
	public static ClientExteriorVariantSchema CAPSULE_DEFAULT;
	public static ClientExteriorVariantSchema CAPSULE_SOUL;
	public static ClientExteriorVariantSchema CAPSULE_FIRE;
	public static ClientExteriorVariantSchema BOOTH_DEFAULT;
	public static ClientExteriorVariantSchema BOOTH_FIRE;
	public static ClientExteriorVariantSchema BOOTH_SOUL;
	public static ClientExteriorVariantSchema BOOTH_VINTAGE;
	public static ClientExteriorVariantSchema BOOTH_BLUE;
	public static ClientExteriorVariantSchema HEAD_DEFAULT;
	public static ClientExteriorVariantSchema HEAD_SOUL;
	public static ClientExteriorVariantSchema HEAD_FIRE;
	public static ClientExteriorVariantSchema CORAL_GROWTH;
	public static ClientExteriorVariantSchema DOOM;
	public static ClientExteriorVariantSchema PLINTH_DEFAULT;
	public static ClientExteriorVariantSchema PLINTH_SOUL;
	public static ClientExteriorVariantSchema PLINTH_FIRE;
	public static ClientExteriorVariantSchema RENEGADE_DEFAULT;
	public static ClientExteriorVariantSchema RENEGADE_TRON;
	public static ClientExteriorVariantSchema RENEGADE_CABINET;
	public static ClientExteriorVariantSchema BOOKSHELF_DEFAULT;

	@Override
	public void onClientInit() {
		// TARDIM
		TARDIM_DEFAULT = register(new ClientTardimDefaultVariant());
		TARDIM_FIRE = register(new ClientTardimFireVariant());
		TARDIM_SOUL = register(new ClientTardimSoulVariant());

		// Police Box
		BOX_DEFAULT = register(new ClientPoliceBoxDefaultVariant());
		BOX_SOUL = register(new ClientPoliceBoxSoulVariant());
		BOX_FIRE = register(new ClientPoliceBoxFireVariant());
		BOX_FUTURE = register(new ClientPoliceBoxFuturisticVariant());
		BOX_CORAL = register(new ClientPoliceBoxCoralVariant());
		BOX_TOKAMAK = register(new ClientPoliceBoxTokamakVariant());
		BOX_CHERRY = register(new ClientPoliceBoxCherryVariant());

		// Classic Box
		PRIME = register(new ClientClassicBoxPrimeVariant());
		YETI = register(new ClientClassicBoxYetiVariant());
		DEFINITIVE = register(new ClientClassicBoxDefinitiveVariant());
		PTORED = register(new ClientClassicBoxPtoredVariant());
		MINT = register(new ClientClassicBoxMintVariant());

		// Capsule
		CAPSULE_DEFAULT = register(new ClientCapsuleDefaultVariant());
		CAPSULE_SOUL = register(new ClientCapsuleSoulVariant());
		CAPSULE_FIRE = register(new ClientCapsuleFireVariant());

		// Booth
		BOOTH_DEFAULT = register(new ClientBoothDefaultVariant());
		BOOTH_FIRE = register(new ClientBoothFireVariant());
		BOOTH_SOUL = register(new ClientBoothSoulVariant());
		BOOTH_VINTAGE = register(new ClientBoothVintageVariant());
		BOOTH_BLUE = register(new ClientBoothBlueVariant());

		// Easter Head
		HEAD_DEFAULT = register(new ClientEasterHeadDefaultVariant());
		HEAD_SOUL = register(new ClientEasterHeadSoulVariant());
		HEAD_FIRE = register(new ClientEasterHeadFireVariant());

		// Coral
		CORAL_GROWTH = register(new ClientGrowthVariant());

		// Doom
		DOOM = register(new ClientDoomVariant());

		// Plinth
		PLINTH_DEFAULT = register(new ClientPlinthDefaultVariant());
		PLINTH_SOUL = register(new ClientPlinthSoulVariant());
		PLINTH_FIRE = register(new ClientPlinthFireVariant());

		// Renegade
		RENEGADE_DEFAULT = register(new ClientRenegadeDefaultVariant());
		RENEGADE_TRON = register(new ClientRenegadeTronVariant());
		RENEGADE_CABINET = register(new ClientRenegadeCabinetVariant());

		// Bookshelf
		BOOKSHELF_DEFAULT = register(new ClientBookshelfDefaultVariant());
	}
}
