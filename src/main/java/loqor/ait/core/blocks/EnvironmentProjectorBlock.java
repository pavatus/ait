package loqor.ait.core.blocks;

import loqor.ait.core.blockentities.EnvironmentProjectorBlockEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.link.v2.interior.InteriorLinkableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
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
    public static final BooleanProperty SILENT = BooleanProperty.of("silent");

    public EnvironmentProjectorBlock(Settings settings) {
        super(settings.emissiveLighting((state, world, pos) -> state.get(EnvironmentProjectorBlock.ENABLED))
                .nonOpaque().luminance(value -> value.get(EnvironmentProjectorBlock.ENABLED) ? 9 : 3));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        boolean powered = ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos());

        return blockState.with(ENABLED, powered).with(POWERED, powered)
                .with(SILENT, ctx.getWorld().getBlockState(ctx.getBlockPos().down())
                        .isIn(BlockTags.WOOL));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ENABLED, POWERED, SILENT);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient())
            return;

        if (world.getBlockEntity(pos) instanceof EnvironmentProjectorBlockEntity projector)
            projector.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient())
            return ActionResult.PASS;

        if (hand != Hand.MAIN_HAND)
            return ActionResult.PASS;

        if (world.getBlockEntity(pos) instanceof EnvironmentProjectorBlockEntity projector)
            return projector.onUse(state, world, pos, player, hand, hit);

        return ActionResult.PASS;
    }

    public static void toggle(Tardis tardis, @Nullable PlayerEntity player, World world, BlockPos pos, BlockState state, boolean active) {
        if (world.getBlockEntity(pos) instanceof EnvironmentProjectorBlockEntity projector)
            projector.toggle(tardis, active);

        if (state.get(SILENT))
            return;

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
