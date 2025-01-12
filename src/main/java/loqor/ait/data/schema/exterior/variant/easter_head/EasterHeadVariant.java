package loqor.ait.data.schema.exterior.variant.easter_head;


import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.tardis.animation.ExteriorAnimation;
import loqor.ait.core.tardis.animation.PulsatingAnimation;
import loqor.ait.data.Loyalty;
import loqor.ait.data.schema.door.DoorSchema;
import loqor.ait.data.schema.door.impl.EasterHeadDoorVariant;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.data.schema.exterior.category.EasterHeadCategory;
import loqor.ait.registry.impl.door.DoorRegistry;

// a useful class for creating easter_head variants as they all have the same filepath you know
public abstract class EasterHeadVariant extends ExteriorVariantSchema {
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/easter_head/easter_head_";

    protected EasterHeadVariant(String name) {
        super(EasterHeadCategory.REFERENCE, AITMod.id("exterior/easter_head/" + name),
                new Loyalty(Loyalty.Type.COMPANION));
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
