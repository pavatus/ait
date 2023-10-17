package mdteam.ait.core.blocks;

import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;

public class ExteriorBlock extends HorizontalDirectionalBlock implements BlockEntityProvider {

    public static final VoxelShape PZ = Block.createCuboidShape(0.01, 0.0, 0.0, 16.0, 32.0, 15.99);
    public static final VoxelShape NZ = Block.createCuboidShape(0.01, 0.0, 0.0, 16.0, 32.0, 15.99);
    public static final VoxelShape PX = Block.createCuboidShape(0.01, 0.0, 0.0, 16.0, 32.0, 15.99);
    public static final VoxelShape NX = Block.createCuboidShape(0.01, 0.0, 0.0, 16.0, 32.0, 15.99);

    public ExteriorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shapePerDirection(state.get(FACING));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ExteriorBlockEntity exteriorBlockEntity) {
            exteriorBlockEntity.useOn(hit, state, player, world, player.isSneaking());
        }
        return ActionResult.CONSUME;
    }

    //@Override
    //public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
    //    super.onEntityCollision(state, world, pos, entity);
    //    if(world.isClient) return;
    //    Vec3d vec = new Vec3d(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ());
    //    Vec3d vected = vec.subtract(pos.getX(), pos.getY(), pos.getZ());
    //    if(vected.equals(Vec3d.ZERO)) {
    //        BlockEntity blockEntity = world.getBlockEntity(pos);
    //        if(blockEntity instanceof ExteriorBlockEntity exteriorBlockEntity) exteriorBlockEntity.onEntityCollision(state, world, pos, entity);
    //    }
    //}

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ExteriorBlockEntity exteriorBlockEntity) {
            exteriorBlockEntity.onPlace(world,pos,state,placer,itemStack);
        }
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> givenType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == givenType ? (BlockEntityTicker<A>) ticker : null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, (world1, pos, state1, be) -> ExteriorBlockEntity.tick(world1, pos, state1, be));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ExteriorBlockEntity(pos, state);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockRenderView renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
        return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
    }

    public VoxelShape shapePerDirection(Direction direction) {
        return switch (direction) {
            case EAST -> NZ;
            case SOUTH -> NX;
            case WEST -> PZ;
            default -> PX;
        };
    }

}
