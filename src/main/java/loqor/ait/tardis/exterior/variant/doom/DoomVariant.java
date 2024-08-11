package loqor.ait.tardis.exterior.variant.doom;

import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.registry.impl.door.DoorRegistry;
import loqor.ait.tardis.animation.ExteriorAnimation;
import loqor.ait.tardis.animation.PulsatingAnimation;
import loqor.ait.tardis.door.DoomDoorVariant;
import loqor.ait.tardis.exterior.category.DoomCategory;

public class DoomVariant extends ExteriorVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/doom");

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
