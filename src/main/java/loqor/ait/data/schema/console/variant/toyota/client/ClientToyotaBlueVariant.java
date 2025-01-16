package loqor.ait.data.schema.console.variant.toyota.client;

import org.joml.Vector3f;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.client.models.consoles.ToyotaConsoleModel;
import loqor.ait.data.schema.console.ClientConsoleVariantSchema;
import loqor.ait.data.schema.console.variant.toyota.ToyotaBlueVariant;

public class ClientToyotaBlueVariant extends ClientConsoleVariantSchema {
    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/consoles/toyota_default.png"));
    public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/consoles/toyota_blue_emission.png"));

    public ClientToyotaBlueVariant() {
        super(ToyotaBlueVariant.REFERENCE, ToyotaBlueVariant.REFERENCE);
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
        return new ToyotaConsoleModel(ToyotaConsoleModel.getTexturedModelData().createModel());
    }

    @Override
    public Vector3f sonicItemTranslations() {
        return new Vector3f(-0.5275f, 1.35f, 0.7f);
    }

    @Override
    public float[] sonicItemRotations() {
        return new float[]{-120f, -45f};
    }
    @Override
    public Vector3f handlesTranslations() {
        return new Vector3f(0.05f, 1.75f, 0.36f);
    }

    @Override
    public float[] handlesRotations() {
        return new float[]{60f, 135f};
    }
}
