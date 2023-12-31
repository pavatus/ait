package mdteam.ait.client.registry.exterior.impl.tardim;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.exteriors.EasterHeadModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.models.exteriors.TardimExteriorModel;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import net.minecraft.util.Identifier;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class ClientTardimVariant extends ClientExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/tardim/tardim_";

    protected ClientTardimVariant(String name) {
        super(new Identifier(AITMod.MOD_ID, "exterior/tardim/" + name));

        this.name = name;
    }


    @Override
    public ExteriorModel model() {
        return new TardimExteriorModel(TardimExteriorModel.getTexturedModelData().createModel());
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