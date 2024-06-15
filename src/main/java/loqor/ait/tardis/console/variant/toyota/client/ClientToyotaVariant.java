package loqor.ait.tardis.console.variant.toyota.client;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.client.models.consoles.ToyotaConsoleModel;
import loqor.ait.core.data.schema.console.ClientConsoleVariantSchema;
import loqor.ait.tardis.console.variant.toyota.ToyotaVariant;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class ClientToyotaVariant extends ClientConsoleVariantSchema {
	public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/toyota_default.png"));
	public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/toyota_orange_emission.png"));
	public static final Identifier NO_EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/toyota_orange_disabled.png"));

	public ClientToyotaVariant() {
		super(ToyotaVariant.REFERENCE, ToyotaVariant.REFERENCE);
	}

	@Override
	public Identifier texture() {
		return TEXTURE;
	}

	@Override
	public Identifier emission() {
		return EMISSION;
	}

	@Override
	public Identifier noEmission() {
		return NO_EMISSION;
	}

	@Override
	public ConsoleModel model() {
		return new ToyotaConsoleModel(ToyotaConsoleModel.getTexturedModelData().createModel());
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(-0.5275f, 1.35f, 0.7f);
	}

	@Override
	public float[] sonicItemRotations() {
		return new float[]{-120f, -45f};
	}
}
