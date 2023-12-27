package mdteam.ait.tardis.variant.exterior;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.ExteriorEnum;
import net.minecraft.util.Identifier;

public class RedCoobVariant extends ExteriorVariantSchema {
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/cube";

    public RedCoobVariant() {
        super(ExteriorEnum.CUBE, new Identifier(AITMod.MOD_ID, "cube_red"));
    }


    @Override
    public Identifier texture() {
        return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + ".png");
    }

    @Override
    public Identifier emission() {
        return null;
    }
}
