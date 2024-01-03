package mdteam.ait.tardis.util;

import mdteam.ait.tardis.handler.WaypointHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// todo for now this is identical to abpd but will eventually hold more
public class Waypoint extends AbsoluteBlockPos.Directed {
    public Waypoint(int x, int y, int z, SerialDimension dimension, Direction direction) {
        super(x, y, z, dimension, direction);
    }

    public Waypoint(BlockPos pos, SerialDimension dimension, Direction direction) {
        super(pos, dimension, direction);
    }

    public Waypoint(AbsoluteBlockPos pos, Direction direction) {
        super(pos, direction);
    }

    public Waypoint(int x, int y, int z, World world, Direction direction) {
        super(x, y, z, world, direction);
    }

    public Waypoint(BlockPos pos, World world, Direction direction) {
        super(pos, world, direction);
    }

    public static Waypoint fromDirected(AbsoluteBlockPos.Directed pos) {
        return new Waypoint(pos, pos.getDirection());
    }
}
