package mdteam.ait.client.registry.exterior.impl.classic;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.ClassicExteriorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.models.exteriors.PoliceBoxModel;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import net.minecraft.util.Identifier;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientClassicBoxVariant extends ClientExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/classic/classic_";

    protected ClientClassicBoxVariant(String name) {
        super(new Identifier(AITMod.MOD_ID, "exterior/classic/" + name));

        this.name = name;
    }


    @Override
    public ExteriorModel model() {
        return new ClassicExteriorModel(ClassicExteriorModel.getTexturedModelData().createModel());
    }
    @Override
    public Identifier texture() {
        return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + ".png");
    }

    @Override
    public Identifier emission() {
        return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + "_emission" + ".png");
    }
}