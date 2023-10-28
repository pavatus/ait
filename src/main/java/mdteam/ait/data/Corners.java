package mdteam.ait.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

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
}
