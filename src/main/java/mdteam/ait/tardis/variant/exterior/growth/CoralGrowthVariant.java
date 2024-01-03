package mdteam.ait.tardis.variant.exterior.growth;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.ExteriorAnimation;
import mdteam.ait.client.animation.PulsatingAnimation;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.exterior.EasterHeadExterior;
import mdteam.ait.tardis.exterior.GrowthExterior;
import mdteam.ait.tardis.variant.door.CapsuleDoorVariant;
import mdteam.ait.tardis.variant.door.CoralGrowthDoorVariant;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.door.EasterHeadDoorVariant;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class CoralGrowthVariant extends ExteriorVariantSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "exterior/coral_growth");

    public CoralGrowthVariant() {
        super(GrowthExterior.REFERENCE, REFERENCE);
    }
    @Override
    public ExteriorAnimation animation(ExteriorBlockEntity exterior) {
        return new PulsatingAnimation(exterior);
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(CoralGrowthDoorVariant.REFERENCE);
    }

    @Override
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        return switch (direction) {
            case DOWN, UP -> pos;
            case NORTH -> pos.add(0,0.1,-0.38);
            case SOUTH -> pos.add(0,0.1,0.38);
            case WEST -> pos.add(-0.38,0.1,0);
            case EAST -> pos.add(0.38,0.1,0);
        };
    }

    @Override
    public double portalHeight() {
        return 2.1d;
    }

    @Override
    public double portalWidth() {
        return 0.9d;
    }
}
