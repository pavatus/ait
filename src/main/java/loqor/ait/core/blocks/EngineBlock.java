package loqor.ait.core.blocks;

import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.SubSystemBlock;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import loqor.ait.core.blockentities.EngineBlockEntity;

public class EngineBlock extends SubSystemBlock implements BlockEntityProvider {
    protected static final VoxelShape Y_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 48.0, 16.0);

    public EngineBlock(Settings settings) {
        super(settings, SubSystem.Id.ENGINE);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        ItemStack stack = player.getStackInHand(hand);
        if (blockEntity instanceof EngineBlockEntity engineBlockEntity)
            engineBlockEntity.useOn(state, world, player.isSneaking(), player, stack);

        return ActionResult.SUCCESS;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (world.getBlockEntity(pos) instanceof EngineBlockEntity engine) {
            engine.onPlaced(world, pos, placer);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Y_SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Y_SHAPE;
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EngineBlockEntity(pos, state);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) { // on break
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof EngineBlockEntity engine) {
                world.updateComparators(pos, this);
                engine.onBroken(world, pos);
            }
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);

        if (!(world.getBlockEntity(pos) instanceof EngineBlockEntity engine))
            return;
        if (!engine.isLinked()) return;

        float durability = engine.tardis().get().subsystems().engine().durability();

        if (durability > 10) return;

        // smoke and spark particles & sfx when below 50%
        world.addParticle(ParticleTypes.LARGE_SMOKE, true, pos.getX() + 0.5f, pos.getY() + 1.25,
                pos.getZ() + 0.5f, 0.15, 0, 0);
        world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.5f, pos.getY() + 1.25, pos.getZ() + 0.5f, 0.1,
                0, 0.05f);

        world.addParticle(ParticleTypes.LARGE_SMOKE, true, pos.getX() + 0.5f, pos.getY() + 1.25,
                pos.getZ() + 0.5f, -0.15, 0, 0);
        world.addParticle(ParticleTypes.CLOUD, pos.getX() + 0.5f, pos.getY() + 1.25, pos.getZ() + 0.5f, -0.1,
                0, -0.05f);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}
