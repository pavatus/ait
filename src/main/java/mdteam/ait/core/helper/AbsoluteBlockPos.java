package mdteam.ait.core.helper;

import mdteam.ait.AITMod;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
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

    public NbtCompound writeToNbt() {
        NbtCompound nbt = new NbtCompound();

        nbt.putString("worldRegistry",this.dimension.getRegistryKey().getRegistry().toString());
        nbt.putString("worldValue",this.dimension.getRegistryKey().getValue().toString());
        nbt.put("pos",NbtHelper.fromBlockPos(this));
        nbt.putInt("direction",this.direction.getId());

        return nbt;
    }
    public static AbsoluteBlockPos readFromNbt(NbtCompound nbt) {
        World dim = AITMod.mcServer.getWorld(World.OVERWORLD);
        BlockPos pos = new BlockPos(0,0,0);
        Direction dir = Direction.NORTH;

        if (nbt.contains("worldRegistry") && nbt.contains("worldValue")) {
            dim = AITMod.mcServer.getWorld(RegistryKey.of(RegistryKey.ofRegistry(new Identifier(nbt.getString("worldRegistry"))),new Identifier(nbt.getString("worldValue"))));
        }
        if (nbt.contains("pos")) pos = NbtHelper.toBlockPos(nbt.getCompound("pos"));
        if (nbt.contains("direction")) dir = Direction.byId(nbt.getInt("direction"));

        return new AbsoluteBlockPos(dim,dir,pos);
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

