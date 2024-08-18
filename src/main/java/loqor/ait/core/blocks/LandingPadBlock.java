package loqor.ait.core.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.AITBlocks;
import loqor.ait.tardis.data.landing.LandingPadManager;

public class LandingPadBlock extends Block {

    public LandingPadBlock(FabricBlockSettings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (world.isClient()) return;
        ServerWorld serverWorld = (ServerWorld) world;

        if (isClaimed(serverWorld, pos)) {
            // dont place yo
            world.breakBlock(pos, true);
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(AITBlocks.LANDING_PAD)));
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
        LandingPadManager manager = LandingPadManager.getInstance(world);

        return manager.getRegion(pos).isPresent();
    }
    private static void claimChunk(ServerWorld world, BlockPos pos) {
        LandingPadManager manager = LandingPadManager.getInstance(world);

        manager.claim(pos);
    }
    private static void releaseChunk(ServerWorld world, BlockPos pos) {
        LandingPadManager manager = LandingPadManager.getInstance(world);

        manager.release(pos);
    }
}
