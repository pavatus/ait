package mdteam.ait.tardis.variant.exterior.box;

import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.variant.door.DoorSchema;
import mdteam.ait.tardis.variant.door.PoliceBoxCoralDoorVariant;
import mdteam.ait.tardis.variant.door.PoliceBoxTokamakDoorVariant;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class PoliceBoxTokamakVariant extends PoliceBoxVariant {
    public PoliceBoxTokamakVariant() {
        super("tokamak");
    }

    @Override
    public DoorSchema door() {
        return DoorRegistry.REGISTRY.get(PoliceBoxTokamakDoorVariant.REFERENCE);
    }

    @Override
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        pos = super.adjustPortalPos(pos, direction);
        return pos.add(0,-0.1,0);
    }
}
