package mdteam.ait.client.registry.exterior.impl.doom;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.coral.CoralGrowthExteriorModel;
import mdteam.ait.client.models.exteriors.DoomExteriorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.client.renderers.coral.CoralRenderer;
import mdteam.ait.client.renderers.exteriors.ExteriorRenderer;
import net.minecraft.util.Identifier;

public class ClientDoomVariant extends ClientExteriorVariantSchema {
    public ClientDoomVariant() {
        super(new Identifier(AITMod.MOD_ID, "exterior/doom"));
    }


    @Override
    public ExteriorModel model() {
        return new DoomExteriorModel(DoomExteriorModel.getTexturedModelData().createModel());
    }
    @Override
    public Identifier texture() {
        return ExteriorRenderer.DOOM_TEXTURE;
    }

    @Override
    public Identifier emission() {
        return ExteriorRenderer.DOOM_TEXTURE_EMISSION;
    }
}