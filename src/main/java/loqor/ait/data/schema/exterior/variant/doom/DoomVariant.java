package loqor.ait.data.schema.exterior.variant.doom;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.tardis.animation.ExteriorAnimation;
import loqor.ait.core.tardis.animation.PulsatingAnimation;
import loqor.ait.data.schema.door.DoorSchema;
import loqor.ait.data.schema.door.impl.DoomDoorVariant;
import loqor.ait.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.data.schema.exterior.category.DoomCategory;
import loqor.ait.registry.impl.door.DoorRegistry;

public class DoomVariant extends ExteriorVariantSchema {
    public static final Identifier REFERENCE = AITMod.id("exterior/doom");

    public DoomVariant() {
        super(DoomCategory.REFERENCE, REFERENCE);
    }

    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(DoomDoorVariant.REFERENCE);
    }

    @Override
    public boolean hasPortals() {
        return false;
    }
}
