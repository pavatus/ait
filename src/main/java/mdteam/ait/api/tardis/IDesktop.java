package mdteam.ait.api.tardis;

import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.data.Corners;
import net.minecraft.util.math.BlockPos;

import java.io.Serializable;

/**
 * This class should be immutable.
 */
public interface IDesktop extends Serializable {
    AbsoluteBlockPos.Directed getInteriorDoorPos();
    IDesktopSchema getSchema();
    Corners getCorners();
}
