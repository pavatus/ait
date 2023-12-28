package mdteam.ait.tardis.variant.exterior.easter_head;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.animation.PulsatingAnimation;
import mdteam.ait.client.models.exteriors.EasterHeadModel;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.exterior.EasterHeadExterior;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.door.EasterHeadDoorVariant;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

// a useful class for creating easter_head variants as they all have the same filepath you know
public abstract class EasterHeadVariant extends ExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/easter_head/easter_head_";

    protected EasterHeadVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
        super(EasterHeadExterior.REFERENCE, new Identifier(modId, "exterior/easter_head/" + name));

        this.name = name;
    }
    protected EasterHeadVariant(String name) {
        this(name, AITMod.MOD_ID);
    }

    @Override
    public ExteriorModel model() {
        return new EasterHeadModel(EasterHeadModel.getTexturedModelData().createModel());
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(EasterHeadDoorVariant.REFERENCE);
    }

    @Override
    public Identifier texture() {
        return new Identifier(AITMod.MOD_ID, TEXTURE_PATH + name + ".png");
    }

    @Override
    public Identifier emission() {
        return null;
    }
}
