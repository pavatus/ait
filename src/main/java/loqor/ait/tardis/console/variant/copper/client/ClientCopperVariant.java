package loqor.ait.tardis.console.variant.copper.client;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.client.models.consoles.CopperConsoleModel;
import loqor.ait.core.data.schema.console.ClientConsoleVariantSchema;
import loqor.ait.tardis.console.variant.copper.CopperVariant;
import net.minecraft.util.Identifier;

public class ClientCopperVariant extends ClientConsoleVariantSchema {
	public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/copper_console.png"));
	public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/copper_console_emission.png"));

	public ClientCopperVariant() {
		super(CopperVariant.REFERENCE, CopperVariant.REFERENCE);
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
		return new CopperConsoleModel(CopperConsoleModel.getTexturedModelData().createModel());
	}
}
