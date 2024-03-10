package mdteam.ait.client.registry.console.impl;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.CoralConsoleModel;
import mdteam.ait.client.registry.console.ClientConsoleVariantSchema;
import mdteam.ait.tardis.console.variant.coral.CoralVariant;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class ClientGreenCoralVariant extends ClientConsoleVariantSchema {
	public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/coral.png"));
	public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/coral_emission.png"));

	public ClientGreenCoralVariant() {
		super(CoralVariant.REFERENCE, CoralVariant.REFERENCE);
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
	public ConsoleModel model() {
		return new CoralConsoleModel(CoralConsoleModel.getTexturedModelData().createModel());
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(1.15f, 1.2f, 0.5f);
	}

	@Override
	public float[] sonicItemRotations() {
		return new float[]{90f, 135f};
	}
}