package mdteam.ait.client.registry.exterior.impl.capsule;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.CapsuleExteriorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import net.minecraft.util.Identifier;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientCapsuleVariant extends ClientExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/capsule/capsule_";

    protected ClientCapsuleVariant(String name) {
        super(new Identifier(AITMod.MOD_ID, "exterior/capsule/" + name));

        this.name = name;
    }


    @Override
    public ExteriorModel model() {
        return new CapsuleExteriorModel(CapsuleExteriorModel.getTexturedModelData().createModel());
    }
    @Override
    public Identifier texture() {
        return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + ".png");
    }

    @Override
    public Identifier emission() {
        return null;
    }
}