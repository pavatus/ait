package mdteam.ait.tardis.variant.exterior.easter_head;

import mdteam.ait.AITMod;
import mdteam.ait.tardis.animation.ExteriorAnimation;
import mdteam.ait.tardis.animation.PulsatingAnimation;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.exterior.EasterHeadCategory;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.door.EasterHeadDoorVariant;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

// a useful class for creating easter_head variants as they all have the same filepath you know
public abstract class EasterHeadVariant extends ExteriorVariantSchema {
    private final String name;
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/easter_head/easter_head_";

    protected EasterHeadVariant(String name, String modId) { // idk why i added the modid bit i dont use it later lol
        super(EasterHeadCategory.REFERENCE, new Identifier(modId, "exterior/easter_head/" + name));

        this.name = name;
    }
    protected EasterHeadVariant(String name) {
        this(name, AITMod.MOD_ID);
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
    public boolean hasPortals() {
        return false;
    }
}
