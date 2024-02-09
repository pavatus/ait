package mdteam.ait.client.registry.console.impl;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.CoralConsoleModel;
import mdteam.ait.client.registry.console.ClientConsoleVariantSchema;
import mdteam.ait.tardis.console.variant.coral.WhiteCoralVariant;
import net.minecraft.util.Identifier;

public class ClientWhiteCoralVariant extends ClientConsoleVariantSchema {
    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/coral_white.png"));
    public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/coral_white_emission.png"));

    public ClientWhiteCoralVariant() {
        super(WhiteCoralVariant.REFERENCE);
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
}
