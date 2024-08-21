package loqor.ait.core.blocks.control;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.AITItems;
import loqor.ait.core.blockentities.control.RedstoneControlBlockEntity;
import loqor.ait.tardis.util.TardisUtil;

public class RedstoneControlBlock extends ControlBlock {
    private static final BooleanProperty POWERED = Properties.POWERED;

    public RedstoneControlBlock(Settings settings) {
        super(settings);

        this.setDefaultState(
                this.getStateManager().getDefaultState().with(POWERED, false)
        );
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

        boolean wasPowered = state.get(POWERED);
        boolean powered = world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(pos.up());

        if (wasPowered == powered) {
            return;
        }

        state = state.with(POWERED, powered);
        world.setBlockState(pos, state, Block.NO_REDRAW);

        entity.run((ServerPlayerEntity) user, !powered);
    }

    @Override
    public Item asItem() {
        return AITItems.REDSTONE_CONTROL;
    }


    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);

        builder.add(POWERED);
    }
}
