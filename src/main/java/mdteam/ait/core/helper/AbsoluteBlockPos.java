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

import java.io.Serializable;
import java.util.Iterator;

public class AbsoluteBlockPos implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient World dimension;
    public String dimensionValue;
    public String dimensionRegistry;
    int x;
    int y;
    int z;

    public AbsoluteBlockPos(int x, int y, int z, World dimension) {
        this.x = x;
        this.y = y;
        this.z = z;
        setDimension(dimension);
    }
    public AbsoluteBlockPos(BlockPos pos, World dimension) {
        this(pos.getX(), pos.getY(), pos.getZ(), dimension);
    }

    public void setDimension(World dimension) {
        this.dimension = dimension;
        this.dimensionValue = this.dimension.getRegistryKey().getValue().toString();
        this.dimensionRegistry = this.dimension.getRegistryKey().getRegistry().toString();
    }

    public BlockPos toBlockPos() {
        return new BlockPos(x, y, z);
    }

    public World getDimension() {
        if(dimension == null) {
            dimension = AITMod.mcServer.getWorld(RegistryKey.of(RegistryKey.ofRegistry(new Identifier(dimensionRegistry)),new Identifier(dimensionValue)));
        }
        return dimension;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public AbsoluteBlockPos above() {
        return new AbsoluteBlockPos(getX(), getY() + 1, getZ());
    }

    @Override
    public String toString() {
        return "AbsoluteBlockPos["+ getX() + "_" + getY() + "_" + getZ() + "]";
    }

}

