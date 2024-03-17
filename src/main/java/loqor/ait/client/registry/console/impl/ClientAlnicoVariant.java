package loqor.ait.client.registry.console.impl;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.AlnicoConsoleModel;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.client.registry.console.ClientConsoleVariantSchema;
import loqor.ait.tardis.console.variant.alnico.AlnicoVariant;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

public class ClientAlnicoVariant extends ClientConsoleVariantSchema {
	public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/alnico.png"));
	public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/alnico_emission.png"));

	public ClientAlnicoVariant() {
		super(AlnicoVariant.REFERENCE, AlnicoVariant.REFERENCE);
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
		return new AlnicoConsoleModel(AlnicoConsoleModel.getTexturedModelData().createModel());
	}

	@Override
	public Vector3f sonicItemTranslations() {
		return new Vector3f(-0.55f, 1.1f, -0.1f);
	}
}
