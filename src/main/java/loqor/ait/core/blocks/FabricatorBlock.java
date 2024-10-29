package loqor.ait.core.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import loqor.ait.core.blockentities.FabricatorBlockEntity;
import loqor.ait.core.blocks.types.HorizontalDirectionalBlock;

public class FabricatorBlock extends HorizontalDirectionalBlock implements BlockEntityProvider {

    public static final VoxelShape DEFAULT_SHAPE = VoxelShapes.cuboid(0, 0, 0, 1, (double) 2 / 16, 1);

    // @TODO MAKE THIS GO ON TOP OF A MACHINE CASING WHICH ENCASES A BLOCK LIKE A
    // SMITHING TABLE OR
    // SOME OTHER CRAFTING
    // TABLE THING
    public FabricatorBlock(Settings settings) {
        super(settings);

        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
            ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        this.setDefaultState(state.with(FACING, placer.getHorizontalFacing().getOpposite()));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        if (!world.getBlockState(pos.down()).isOf(Blocks.SMITHING_TABLE))
            return false;
        return super.canPlaceAt(state, world, pos);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return DEFAULT_SHAPE;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        Direction direction = state.get(FACING);
        double d = (double) pos.getX() + 0.55 - (double) (random.nextFloat() * 0.1f);
        double e = (double) pos.getY() + 0.55 - (double) (random.nextFloat() * 0.1f);
        double f = (double) pos.getZ() + 0.55 - (double) (random.nextFloat() * 0.1f);
        double g = 0.4f - (random.nextFloat() + random.nextFloat()) * 0.4f;
        world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d + (double) direction.getOffsetX() * g,
                e + (double) direction.getOffsetY() * g, f + (double) direction.getOffsetZ() * g,
                random.nextGaussian() * 0.005, random.nextGaussian() * 0.005, random.nextGaussian() * 0.005);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return DEFAULT_SHAPE;
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return DEFAULT_SHAPE;
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FabricatorBlockEntity(pos, state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
