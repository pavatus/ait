package loqor.ait.core.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

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

        if (isClaimed(world, pos)) {
            // dont place yo
            world.breakBlock(pos, true);
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(AITBlocks.LANDING_PAD)));
            return;
        }

        claimChunk(world, pos);
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);

        if (world.isClient()) return;

        releaseChunk(world.getServer(), pos);
    }

    private static boolean isClaimed(World world, BlockPos pos) {
        LandingPadManager manager = LandingPadManager.getInstance(world);

        return manager.getRegion(pos).isPresent();
    }
    private static void claimChunk(World world, BlockPos pos) {
        LandingPadManager manager = LandingPadManager.getInstance(world);

        manager.claim(pos);
    }
    private static void releaseChunk(MinecraftServer server, BlockPos pos) {
        LandingPadManager manager = LandingPadManager.getInstance(server);

        manager.release(pos);
    }
}
