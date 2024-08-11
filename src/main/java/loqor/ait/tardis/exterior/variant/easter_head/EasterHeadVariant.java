package loqor.ait.tardis.exterior.variant.easter_head;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.door.DoorRegistry;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.door.EasterHeadDoorVariant;
import loqor.ait.tardis.exterior.category.EasterHeadCategory;

// a useful class for creating easter_head variants as they all have the same filepath you know
public abstract class EasterHeadVariant extends ExteriorVariantSchema {
    protected static final String TEXTURE_PATH = "textures/blockentities/exteriors/easter_head/easter_head_";

    protected EasterHeadVariant(String name) {
        super(EasterHeadCategory.REFERENCE, new Identifier(AITMod.MOD_ID, "exterior/easter_head/" + name),
                new Loyalty(Loyalty.Type.PILOT));
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
