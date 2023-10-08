package mdteam.ait.core.helper;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Iterator;

public class AbsoluteBlockPos extends BlockPos {
    private final World dimension;
    private final Direction direction;
    public AbsoluteBlockPos(World dimension, Direction direction, int x, int y, int z) {
        super(x,y,z);

        this.direction = direction;
        this.dimension = dimension;
    }
    public AbsoluteBlockPos(World dimension,Direction direction, BlockPos pos) {
        this(dimension,direction, pos.getX(), pos.getY(), pos.getZ());
    }
    public AbsoluteBlockPos(World dimension, BlockPos pos) {
        this(dimension, Direction.NORTH, pos);
    }

//    public AbsoluteBlockPos(RegistryKey<World> dimension, BlockPos pos) {
//        this(TARDISMod.server.getLevel(dimension),pos);
//    }

    public World getDimension() {
        return this.dimension;
    }
//    public RegistryKey<World> getResourceKeyLevel() {
//        // Find the resource key from the server and grab that if it exists
//        if (!TARDISMod.server.levelKeys().contains(this.dimension)) {return null;}
//
//        for (Iterator<ResourceKey<World>> it = TARDISMod.server.levelKeys().iterator(); it.hasNext(); ) {
//            ResourceKey<World> key = it.next();
//            if (TARDISMod.server.getLevel(key) == this.dimension) {
//                return key;
//            }
//        }
//        return null;
//    }
    public Direction getDirection() {return this.direction;}
}

