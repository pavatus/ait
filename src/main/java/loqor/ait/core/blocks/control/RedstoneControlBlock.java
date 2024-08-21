package loqor.ait.core.blocks.control;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.blockentities.control.RedstoneControlBlockEntity;
import loqor.ait.tardis.util.TardisUtil;

public class RedstoneControlBlock extends ControlBlock {
    public RedstoneControlBlock(Settings settings) {
        super(settings);
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RedstoneControlBlockEntity(pos, state);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!(world.getBlockEntity(pos) instanceof RedstoneControlBlockEntity entity)) return;
        if (world.isClient()) return;
        if (entity.tardis().isEmpty()) return;

        PlayerEntity user = TardisUtil.getPlayerInsideInterior(entity.tardis().get());
        if (user == null) return;

        boolean powered = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());

        entity.run((ServerPlayerEntity) user, !powered);
    }
}
