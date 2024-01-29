package mdteam.ait.tardis.variant.exterior.doom;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.animation.ExteriorAnimation;
import mdteam.ait.tardis.animation.PulsatingAnimation;
import mdteam.ait.tardis.exterior.DoomExterior;
import mdteam.ait.tardis.exterior.GrowthExterior;
import mdteam.ait.tardis.variant.door.CoralGrowthDoorVariant;
import mdteam.ait.tardis.variant.door.DoomDoorVariant;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class DoomVariant extends ExteriorVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/doom");

    public DoomVariant() {
        super(DoomExterior.REFERENCE, REFERENCE);
    }
    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(DoomDoorVariant.REFERENCE);
    }
}
