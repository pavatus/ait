package mdteam.ait.client.registry.console.impl;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.HartnellConsoleModel;
import mdteam.ait.client.registry.console.ClientConsoleVariantSchema;
import mdteam.ait.tardis.console.variant.hartnell.MintHartnellVariant;
import net.minecraft.util.Identifier;

public class ClientMintHartnellVariant extends ClientConsoleVariantSchema {
	public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/hartnell_mint_console.png"));
	public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/hartnell_console_emission.png"));

	public ClientMintHartnellVariant() {
		super(MintHartnellVariant.REFERENCE, MintHartnellVariant.REFERENCE);
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
