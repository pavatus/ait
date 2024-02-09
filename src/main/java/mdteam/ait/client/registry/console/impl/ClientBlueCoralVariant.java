package mdteam.ait.client.registry.console.impl;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.client.models.consoles.CoralConsoleModel;
import mdteam.ait.client.registry.console.ClientConsoleVariantSchema;
import mdteam.ait.tardis.console.variant.coral.BlueCoralVariant;
import net.minecraft.util.Identifier;

public class ClientBlueCoralVariant extends ClientConsoleVariantSchema {
    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/coral_blue.png"));
    public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/coral_blue_emission.png"));

    public ClientBlueCoralVariant() {
        super(BlueCoralVariant.REFERENCE);
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
