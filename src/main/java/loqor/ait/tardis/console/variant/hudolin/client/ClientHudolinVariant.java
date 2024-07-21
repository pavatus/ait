package loqor.ait.tardis.console.variant.hudolin.client;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.client.models.consoles.HudolinConsoleModel;
import loqor.ait.core.data.schema.console.ClientConsoleVariantSchema;
import loqor.ait.tardis.console.variant.hudolin.HudolinVariant;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class ClientHudolinVariant extends ClientConsoleVariantSchema {
	public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/hudolin_console.png"));
	public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/hudolin_console_emission.png"));

	public ClientHudolinVariant() {
		super(HudolinVariant.REFERENCE, HudolinVariant.REFERENCE);
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
		return new HudolinConsoleModel(HudolinConsoleModel.getTexturedModelData().createModel());
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(0.9f, 1.125f, -0.19f);
	}

	@Override
	public float[] sonicItemRotations() {
		return new float[]{30f, 120f};
	}
}
