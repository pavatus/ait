package loqor.ait.tardis.console.variant.alnico.client;

import org.joml.Vector3f;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.AlnicoConsoleModel;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.core.data.schema.console.ClientConsoleVariantSchema;
import loqor.ait.tardis.console.variant.alnico.BlueAlnicoVariant;

public class ClientBlueAlnicoVariant extends ClientConsoleVariantSchema {
    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/consoles/alnico_blue.png"));
    public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/consoles/alnico_blue_emission.png"));

    public ClientBlueAlnicoVariant() {
        super(BlueAlnicoVariant.REFERENCE, BlueAlnicoVariant.REFERENCE);
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
