package mdteam.ait.tardis.variant.exterior.doom;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.animation.ExteriorAnimation;
import mdteam.ait.tardis.animation.PulsatingAnimation;
import mdteam.ait.tardis.exterior.DoomCategory;
import mdteam.ait.tardis.variant.door.DoomDoorVariant;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;

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
