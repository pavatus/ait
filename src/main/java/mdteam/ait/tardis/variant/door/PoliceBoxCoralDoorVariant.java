package mdteam.ait.tardis.variant.door;

import mdteam.ait.AITMod;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class PoliceBoxCoralDoorVariant extends DoorSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "door/police_box/coral");

    public PoliceBoxCoralDoorVariant() {
        super(REFERENCE);
    }

    @Override
    public boolean isDouble() {
        return true;
    }
    @Override
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        return switch (direction) {
            case DOWN, UP -> pos;
            case NORTH -> pos.add(0,0.05,0.45);
            case SOUTH -> pos.add(0,0.05,-0.45);
            case WEST -> pos.add(0.45,0.05,0);
            case EAST -> pos.add(-0.45,0.05,0);
        };
    }
}
