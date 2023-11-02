package mdteam.ait.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import the.mdteam.ait.Exclude;

public class Corners {
    private final Box box;
    private final BlockPos first;
    private final BlockPos second;

    public Corners(BlockPos first, BlockPos second) {
        this.box = new Box(first, second);

        this.first = first;
        this.second = second;
    }

    public Box getBox() {
        return box;
    }

    public BlockPos getFirst() {
        return first;
    }

    public BlockPos getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Corners{" +
                "box=" + box +
                ", first=" + first +
                ", second=" + second +
                '}';
    }
}
