package mdteam.ait.tardis.variant.door;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.doors.ClassicDoorModel;
import mdteam.ait.client.models.doors.DoorModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ClassicDoorVariant extends DoorSchema {
    public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "door/classic");

    public ClassicDoorVariant() {
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
            case NORTH -> pos.add(0,0.29,-0.641);
            case SOUTH -> pos.add(0,0.29,0.641);
            case WEST -> pos.add(-0.641,0.29,0);
            case EAST -> pos.add(0.641,0.29,0);
        };
    }
}
