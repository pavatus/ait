package loqor.ait.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PeanutBlock extends Block {

    public PeanutBlock(Settings settings) {
        super(settings);
    }

    public void explode(World world, BlockPos pos) {
        world.createExplosion(null, world.getDamageSources().outOfWorld(), null, pos.toCenterPos(), 100, true, World.ExplosionSourceType.MOB);
        world.createExplosion(null, world.getDamageSources().outOfWorld(), null, pos.toCenterPos(), 100, true, World.ExplosionSourceType.BLOCK);
        world.createExplosion(null, world.getDamageSources().outOfWorld(), null, pos.toCenterPos(), 100, true, World.ExplosionSourceType.TNT);
    }
}
