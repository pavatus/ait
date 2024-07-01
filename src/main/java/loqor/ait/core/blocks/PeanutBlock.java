package loqor.ait.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.Optional;

public class PeanutBlock extends Block {

    public PeanutBlock(Settings settings) {
        super(settings);
    }

    public void explode(World world, BlockPos pos) {
        ExplosionBehavior explosionBehavior = new ExplosionBehavior(){

            @Override
            public Optional<Float> getBlastResistance(Explosion explosion, BlockView world, BlockPos pos, BlockState blockState, FluidState fluidState) {
                return Optional.of(Blocks.WATER.getBlastResistance());
            }
        };

        world.createExplosion(null, world.getDamageSources().outOfWorld(), explosionBehavior, pos.toCenterPos(), 100, true, World.ExplosionSourceType.MOB);
    }
}
