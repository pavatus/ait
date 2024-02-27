package mdteam.ait.client.registry.exterior.impl.booth;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.BoothExteriorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientBoothVariant extends ClientExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/booth/booth_";

    protected ClientBoothVariant(String name) {
        super(new Identifier(AITMod.MOD_ID, "exterior/booth/" + name));

        this.name = name;
    }


    @Override
    public ExteriorModel model() {
        return new BoothExteriorModel(BoothExteriorModel.getTexturedModelData().createModel());
    }
    @Override
    public Identifier texture() {
        return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + ".png");
    }

    @Override
    public Identifier emission() {
        return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + "_emission" + ".png");
    }

    @Override
    public Vector3f sonicItemTranslations() {
        return new Vector3f(0.845f, 1.125f, 1.05f);
    }
}