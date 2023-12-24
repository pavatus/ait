package mdteam.ait.tardis.variant.exterior.tardim;

import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class TardimVariant extends ExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/tardim/tardim_";

    protected TardimVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
        super(ExteriorEnum.TARDIM, new Identifier(modId, "tardim_" + name));

        this.name = name;
    }
    protected TardimVariant(String name) {
        this(name, AITMod.MOD_ID);
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
