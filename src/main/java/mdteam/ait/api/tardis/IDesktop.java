package mdteam.ait.api.tardis;

import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.Corners;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.io.Serializable;

/**
 * This class should be immutable.
 */
public interface IDesktop extends Serializable {

    BlockPos getInteriorDoorPos();
    IDesktopSchema getSchema();
    Corners getCorners();

    default BlockPos offsetDoorPosition() {
        World world = TardisUtil.getTardisDimension();
        BlockPos pos = this.getInteriorDoorPos();

        Direction doorDirection = world.getBlockState(pos).get(Properties.HORIZONTAL_FACING);

        return switch (doorDirection) {
            case DOWN, UP -> throw new IllegalArgumentException("Cannot adjust door position with direction: " + doorDirection);
            case NORTH -> new BlockPos.Mutable(pos.getX() + 0.5, pos.getY(), pos.getZ() - 1);
            case SOUTH -> new BlockPos.Mutable(pos.getX() + 0.5, pos.getY(), pos.getZ() + 1);
            case EAST -> new BlockPos.Mutable(pos.getX() + 1, pos.getY(), pos.getZ() + 0.5);
            case WEST -> new BlockPos.Mutable(pos.getX() - 1, pos.getY(), pos.getZ() + 0.5);
        };
    }
}
