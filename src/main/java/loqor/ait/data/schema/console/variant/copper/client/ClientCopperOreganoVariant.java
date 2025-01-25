package loqor.ait.data.schema.console.variant.copper.client;

import org.joml.Vector3f;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.client.models.consoles.CopperConsoleModel;
import loqor.ait.data.schema.console.ClientConsoleVariantSchema;
import loqor.ait.data.schema.console.variant.copper.CopperOreganoVariant;

public class ClientCopperOreganoVariant extends ClientConsoleVariantSchema {
    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/consoles/copper_oregano.png"));
    public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/consoles/copper_oregano_emission.png"));

    public ClientCopperOreganoVariant() {
        super(CopperOreganoVariant.REFERENCE, CopperOreganoVariant.REFERENCE);
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

    @Override
    public Vector3f sonicItemTranslations() {
        return new Vector3f(1.55f, 1.05f, 1.105f);
    }

    @Override
    public float[] sonicItemRotations() {
        return new float[]{-60f, -12.5f};
    }
    @Override
    public Vector3f handlesTranslations() {
        return new Vector3f(0.8f, 1.25f, 0.68f);
    }

    @Override
    public float[] handlesRotations() {
        return new float[]{-60f, 120f};
    }
}
