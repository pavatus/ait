package mdteam.ait.tardis.variant.exterior.booth;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.animation.PulsatingAnimation;
import mdteam.ait.client.models.exteriors.BoothExteriorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.tardis.ExteriorEnum;
import mdteam.ait.core.AITDoors;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.variant.door.BoothDoorVariant;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class BoothVariant extends ExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/booth/booth_";

    protected BoothVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
        super(ExteriorEnum.BOOTH, new Identifier(modId, "booth_" + name));

        this.name = name;
    }
    protected BoothVariant(String name) {
        this(name, AITMod.MOD_ID);
    }


    @Override
    public ExteriorModel model() {
        return new BoothExteriorModel(BoothExteriorModel.getTexturedModelData().createModel());
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return AITDoors.get(BoothDoorVariant.REFERENCE);
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
