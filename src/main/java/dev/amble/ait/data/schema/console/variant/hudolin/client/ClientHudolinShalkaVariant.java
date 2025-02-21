package dev.amble.ait.data.schema.console.variant.hudolin.client;

import net.minecraft.util.Identifier;

import dev.amble.ait.AITMod;
import dev.amble.ait.client.models.consoles.ConsoleModel;
import dev.amble.ait.client.models.consoles.HudolinConsoleModel;
import dev.amble.ait.data.schema.console.ClientConsoleVariantSchema;
import dev.amble.ait.data.schema.console.variant.hudolin.HudolinShalkaVariant;

public class ClientHudolinShalkaVariant extends ClientConsoleVariantSchema {
    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/consoles/hudolin_console_shalka.png"));
    public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/consoles/hudolin_console_shalka_emission.png"));

    public ClientHudolinShalkaVariant() {
        super(HudolinShalkaVariant.REFERENCE, HudolinShalkaVariant.REFERENCE);
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
}
