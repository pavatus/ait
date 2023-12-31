package mdteam.ait.client.registry.exterior.impl.box;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.BoothExteriorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.models.exteriors.PoliceBoxModel;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientPoliceBoxVariant extends ClientExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/police_box/police_box_";

    protected ClientPoliceBoxVariant(String name) {
        super(new Identifier(AITMod.MOD_ID, "exterior/police_box/" + name));

        this.name = name;
    }


    @Override
    public ExteriorModel model() {
        return new PoliceBoxModel(PoliceBoxModel.getTexturedModelData().createModel());
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