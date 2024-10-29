package loqor.ait.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An interface which stops a block from being broken
 */
public interface ICantBreak {
    /**
     * Called when the block was attempted to be broken
     */
    default void onTryBreak(World world, BlockPos pos, BlockState state) {
    }
}
