package mdteam.ait.tardis.exterior.variant;

import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.door.PoliceBoxCoralDoorVariant;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class PoliceBoxCoralVariant extends PoliceBoxVariant {
    public PoliceBoxCoralVariant() {
        super("coral");
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(PoliceBoxCoralDoorVariant.REFERENCE);
    }

    @Override
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        pos = super.adjustPortalPos(pos, direction);
        return pos.add(0,-0.05,0);
    }

    @Override
    public double portalHeight() {
        return 2.2;
    }
}
