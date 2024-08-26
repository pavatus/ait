package loqor.ait.core.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.blockentities.ConsoleGeneratorBlockEntity;
import loqor.ait.core.blocks.types.HorizontalDirectionalBlock;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.util.TardisUtil;

public class ConsoleGeneratorBlock extends HorizontalDirectionalBlock implements BlockEntityProvider {

    public ConsoleGeneratorBlock(Settings settings) {
        super(settings);
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ConsoleGeneratorBlockEntity(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
            ItemStack itemStack) {
        if (world.isClient())
            return;
        Tardis tardis = TardisUtil.findTardisByInterior(pos, true);
        if (tardis == null)
            return;
        if (world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD && tardis.isGrowth()) {
            // dont place yo
            world.breakBlock(pos, true);
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f,
                    new ItemStack(AITBlocks.CONSOLE_GENERATOR)));
            return;
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ConsoleGeneratorBlockEntity consoleGeneratorBlockEntity)
            consoleGeneratorBlockEntity.useOn(world, player.isSneaking(), player);
        return ActionResult.SUCCESS;
    }
}
