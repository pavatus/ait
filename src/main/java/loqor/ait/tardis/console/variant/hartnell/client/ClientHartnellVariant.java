package loqor.ait.tardis.console.variant.hartnell.client;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.client.models.consoles.HartnellConsoleModel;
import loqor.ait.core.data.schema.console.ClientConsoleVariantSchema;
import loqor.ait.tardis.console.variant.hartnell.HartnellVariant;
import net.minecraft.util.Identifier;

public class ClientHartnellVariant extends ClientConsoleVariantSchema {
	public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/hartnell_console.png"));
	public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/hartnell_console_emission.png"));

	public ClientHartnellVariant() {
		super(HartnellVariant.REFERENCE, HartnellVariant.REFERENCE);
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
		return new HartnellConsoleModel(HartnellConsoleModel.getTexturedModelData().createModel());
	}
}
