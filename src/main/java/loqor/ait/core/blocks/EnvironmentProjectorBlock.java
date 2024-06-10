package loqor.ait.core.blocks;

import loqor.ait.core.blockentities.EnvironmentProjectorBlockEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class EnvironmentProjectorBlock extends Block implements BlockEntityProvider {

    public static final BooleanProperty ENABLED = Properties.ENABLED;
    public static final BooleanProperty POWERED = Properties.POWERED;

    public EnvironmentProjectorBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();

        if (ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()))
            blockState = blockState.with(ENABLED, true).with(POWERED, true);

        return blockState;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ENABLED, POWERED);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient())
            return;

        boolean powered = world.isReceivingRedstonePower(pos);

        if (powered != state.get(POWERED)) {
            if (state.get(ENABLED) != powered) {
                state = state.with(ENABLED, powered);

                Tardis tardis = TardisUtil.findTardisByInterior(pos, true);

                if (tardis == null)
                    return;

                this.toggle(tardis, null, world, pos, powered);
            }

            world.setBlockState(pos, state.with(POWERED, powered), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient())
            return ActionResult.PASS;

        if (hand != Hand.MAIN_HAND)
            return ActionResult.PASS;

        Tardis tardis = TardisUtil.findTardisByInterior(pos, true);

        if (tardis == null)
            return ActionResult.PASS;

        if (player.isSneaking() && world.getBlockEntity(pos) instanceof EnvironmentProjectorBlockEntity projector) {
            projector.switchSkybox(tardis, player);
            return ActionResult.SUCCESS;
        }

        state = state.cycle(ENABLED);
        world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);

        this.toggle(tardis, player, world, pos, state.get(ENABLED));
        return ActionResult.SUCCESS;
    }

    protected void toggle(Tardis tardis, @Nullable PlayerEntity player, World world, BlockPos pos, boolean active) {
        if (world.getBlockEntity(pos) instanceof EnvironmentProjectorBlockEntity projector)
            projector.toggle(tardis, active);

        world.playSound(player, pos, active ? SoundEvents.BLOCK_BEACON_ACTIVATE : SoundEvents.BLOCK_BEACON_DEACTIVATE,
                SoundCategory.BLOCKS, 1.0f, world.getRandom().nextFloat() * 0.1f + 0.9f
        );

        world.emitGameEvent(player, active ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EnvironmentProjectorBlockEntity(pos, state);
    }
}
