package loqor.ait.client.registry;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.registry.exterior.ClientExteriorVariantSchema;
import loqor.ait.client.registry.exterior.impl.booth.*;
import loqor.ait.client.registry.exterior.impl.box.*;
import loqor.ait.client.registry.exterior.impl.capsule.ClientCapsuleDefaultVariant;
import loqor.ait.client.registry.exterior.impl.capsule.ClientCapsuleFireVariant;
import loqor.ait.client.registry.exterior.impl.capsule.ClientCapsuleSoulVariant;
import loqor.ait.client.registry.exterior.impl.classic.*;
import loqor.ait.client.registry.exterior.impl.doom.ClientDoomVariant;
import loqor.ait.client.registry.exterior.impl.easter_head.ClientEasterHeadDefaultVariant;
import loqor.ait.client.registry.exterior.impl.easter_head.ClientEasterHeadFireVariant;
import loqor.ait.client.registry.exterior.impl.easter_head.ClientEasterHeadSoulVariant;
import loqor.ait.client.registry.exterior.impl.growth.ClientGrowthVariant;
import loqor.ait.client.registry.exterior.impl.plinth.ClientPlinthDefaultVariant;
import loqor.ait.client.registry.exterior.impl.plinth.ClientPlinthFireVariant;
import loqor.ait.client.registry.exterior.impl.plinth.ClientPlinthSoulVariant;
import loqor.ait.client.registry.exterior.impl.renegade.ClientRenegadeDefaultVariant;
import loqor.ait.client.registry.exterior.impl.renegade.ClientRenegadeTronVariant;
import loqor.ait.client.registry.exterior.impl.tardim.ClientTardimDefaultVariant;
import loqor.ait.client.registry.exterior.impl.tardim.ClientTardimFireVariant;
import loqor.ait.client.registry.exterior.impl.tardim.ClientTardimSoulVariant;
import loqor.ait.registry.DatapackRegistry;
import loqor.ait.tardis.exterior.variant.DatapackExterior;
import loqor.ait.tardis.exterior.variant.ExteriorVariantSchema;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class ClientExteriorVariantRegistry extends DatapackRegistry<ClientExteriorVariantSchema> {
	private static ClientExteriorVariantRegistry INSTANCE;

	public static ClientExteriorVariantSchema registerStatic(ClientExteriorVariantSchema schema) {
		return getInstance().register(schema);
	}

	public static DatapackRegistry<ClientExteriorVariantSchema> getInstance() {
		if (INSTANCE == null) {
			AITMod.LOGGER.info("ClientExteriorVariantRegistry was not initialized, Creating a new instance");
			INSTANCE = new ClientExteriorVariantRegistry();
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
	public static ClientExteriorVariantSchema withParent(ExteriorVariantSchema parent) {
		for (ClientExteriorVariantSchema schema : getInstance().toArrayList()) {
			if (schema.parent() == null) continue;
			if (schema.parent().id().equals(parent.id())) return schema;
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
			ClientExteriorVariantSchema exterior = convertDatapack(buf.decodeAsJson(DatapackExterior.CODEC));
			if (exterior == null) return;
			this.register(exterior);
		}

		AITMod.LOGGER.info("Read {} exterior variants from server", size);
	}

	public static ClientExteriorVariantSchema convertDatapack(DatapackExterior variant) {
		if (!variant.wasDatapack()) return convertNonDatapack(variant);
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
		if (variant.wasDatapack()) return convertDatapack(variant);

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
	public static ClientExteriorVariantSchema COOB; // dont use : (
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

	// AAAAAAAAAAAAAAAAAAAAAAAAAAA SO MANY VARIABLE
	public void init() {
		// TARDIM
		TARDIM_DEFAULT = registerStatic(new ClientTardimDefaultVariant());
		TARDIM_FIRE = registerStatic(new ClientTardimFireVariant());
		TARDIM_SOUL = registerStatic(new ClientTardimSoulVariant());

		// Police Box
		BOX_DEFAULT = registerStatic(new ClientPoliceBoxDefaultVariant());
		BOX_SOUL = registerStatic(new ClientPoliceBoxSoulVariant());
		BOX_FIRE = registerStatic(new ClientPoliceBoxFireVariant());
		BOX_FUTURE = registerStatic(new ClientPoliceBoxFuturisticVariant());
		BOX_CORAL = registerStatic(new ClientPoliceBoxCoralVariant());
		BOX_TOKAMAK = registerStatic(new ClientPoliceBoxTokamakVariant());

		// Classic Box
		PRIME = registerStatic(new ClientClassicBoxPrimeVariant());
		YETI = registerStatic(new ClientClassicBoxYetiVariant());
		DEFINITIVE = registerStatic(new ClientClassicBoxDefinitiveVariant());
		PTORED = registerStatic(new ClientClassicBoxPtoredVariant());
		MINT = registerStatic(new ClientClassicBoxMintVariant());

		// Capsule
		CAPSULE_DEFAULT = registerStatic(new ClientCapsuleDefaultVariant());
		CAPSULE_SOUL = registerStatic(new ClientCapsuleSoulVariant());
		CAPSULE_FIRE = registerStatic(new ClientCapsuleFireVariant());

		// Booth
		BOOTH_DEFAULT = registerStatic(new ClientBoothDefaultVariant());
		BOOTH_FIRE = registerStatic(new ClientBoothFireVariant());
		BOOTH_SOUL = registerStatic(new ClientBoothSoulVariant());
		BOOTH_VINTAGE = registerStatic(new ClientBoothVintageVariant());
		BOOTH_BLUE = registerStatic(new ClientBoothBlueVariant());

		// funny
		// COOB = register(new RedCoobVariant()); // fixme CUBE HAS BEEN REMOVED, REPEAT, CUBE HAS BEEN REMOVED. DO NOT PANIC!!

		// Easter Head
		HEAD_DEFAULT = registerStatic(new ClientEasterHeadDefaultVariant());
		HEAD_SOUL = registerStatic(new ClientEasterHeadSoulVariant());
		HEAD_FIRE = registerStatic(new ClientEasterHeadFireVariant());

		// Coral
		CORAL_GROWTH = registerStatic(new ClientGrowthVariant());

		// Doom
		DOOM = registerStatic(new ClientDoomVariant());

		// Plinth
		PLINTH_DEFAULT = registerStatic(new ClientPlinthDefaultVariant());
		PLINTH_SOUL = registerStatic(new ClientPlinthSoulVariant());
		PLINTH_FIRE = registerStatic(new ClientPlinthFireVariant());

		// Renegade
		RENEGADE_DEFAULT = registerStatic(new ClientRenegadeDefaultVariant());
		RENEGADE_TRON = registerStatic(new ClientRenegadeTronVariant());
	}
}
