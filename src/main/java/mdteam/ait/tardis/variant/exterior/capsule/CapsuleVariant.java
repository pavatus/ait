package mdteam.ait.tardis.variant.exterior.capsule;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ClassicAnimation;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.models.exteriors.CapsuleExteriorModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.core.AITExteriors;
import mdteam.ait.tardis.ExteriorEnum;
import mdteam.ait.core.AITDoors;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.exterior.CapsuleExterior;
import mdteam.ait.tardis.exterior.PoliceBoxExterior;
import mdteam.ait.tardis.variant.door.CapsuleDoorVariant;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

// a useful class for creating tardim variants as they all have the same filepath you know
public abstract class CapsuleVariant extends ExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/capsule/capsule_";

    protected CapsuleVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
        super(AITExteriors.get(CapsuleExterior.REFERENCE), new Identifier(modId, "exterior/capsule/" + name));

        this.name = name;
    }
    protected CapsuleVariant(String name) {
        this(name, AITMod.MOD_ID);
    }

    @Override
    public ExteriorModel model() {
        return new CapsuleExteriorModel(CapsuleExteriorModel.getTexturedModelData().createModel());
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new ClassicAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return AITDoors.get(CapsuleDoorVariant.REFERENCE);
    }


    @Override
    public Identifier texture() {
        return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + ".png");
    }

    @Override
    public Identifier emission() {
        return null; // as of rn none of these have emission
    }
}
