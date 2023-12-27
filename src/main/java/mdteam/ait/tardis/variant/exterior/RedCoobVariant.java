package mdteam.ait.tardis.variant.exterior;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.animation.PulsatingAnimation;
import mdteam.ait.client.models.exteriors.CoobExteriorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.core.AITDoors;
import mdteam.ait.core.AITExteriors;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.exterior.CubeExterior;
import mdteam.ait.tardis.variant.door.CapsuleDoorVariant;
import mdteam.ait.tardis.variant.door.DoorSchema;
import net.minecraft.util.Identifier;

public class RedCoobVariant extends ExteriorVariantSchema {
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/cube";

    public RedCoobVariant() {
        super(CubeExterior.REFERENCE, new Identifier(AITMod.MOD_ID, "cube_red"));
    }


    @Override
    public Identifier texture() {
        return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + ".png");
    }

    @Override
    public Identifier emission() {
        return null;
    }

    @Override
    public ExteriorModel model() {
        return new CoobExteriorModel(CoobExteriorModel.getTexturedModelData().createModel());
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return AITDoors.get(CapsuleDoorVariant.REFERENCE);
    }
}
