package mdteam.ait.tardis.exterior.variant;

import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.variant.door.DoorSchema;
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
        /*return switch(direction) {
            case DOWN, UP -> pos;
            case NORTH -> pos.add(0,0.207,-0.63f);
            case SOUTH -> pos.add(0,0.207,0.63f);
            case WEST -> pos.add(-0.63f,0.207,0);
            case EAST -> pos.add(0.63f,0.207,0);
        };*/
        return super.adjustPortalPos(pos, direction);
    }
}
