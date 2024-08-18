package loqor.ait.core.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import loqor.ait.tardis.data.landing.LandingPad;

public class LandingPadBlock extends Block {

    public LandingPadBlock(FabricBlockSettings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);

        Vec3d centre = pos.up().toCenterPos();
        world.addParticle(ParticleTypes.GLOW, centre.getX(), centre.getY() - 0.5, centre.getZ(), 0.0, 0.0, 0.0);

        if (random.nextDouble() < 0.2f) {
            world.playSound(centre.getX(), centre.getY(), centre.getZ(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.BLOCKS, 0.1f, 1f, true);
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (world.isClient()) return;
        ServerWorld serverWorld = (ServerWorld) world;

        if (isClaimed(serverWorld, pos)) {
            // dont place yo
            world.breakBlock(pos, true);
            return;
        }

        claimChunk(serverWorld, pos);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);

        if (world.isClient()) return;

        releaseChunk((ServerWorld) world, pos);
    }

    private static boolean isClaimed(ServerWorld world, BlockPos pos) {
        LandingPad.Manager manager = LandingPad.Manager.getInstance(world);

        return manager.getRegion(pos).isPresent();
    }
    private static void claimChunk(ServerWorld world, BlockPos pos) {
        LandingPad.Manager manager = LandingPad.Manager.getInstance(world);

        manager.claim(pos);
    }
    private static void releaseChunk(ServerWorld world, BlockPos pos) {
        LandingPad.Manager manager = LandingPad.Manager.getInstance(world);

        manager.release(pos);
    }
}
