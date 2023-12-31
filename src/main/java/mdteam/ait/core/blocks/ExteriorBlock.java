package mdteam.ait.core.blocks;

import mdteam.ait.api.ICantBreak;
import mdteam.ait.compat.DependencyChecker;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.core.entities.FallingTardisEntity;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.util.ParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExteriorBlock extends FallingBlock implements BlockEntityProvider, ICantBreak {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    public static final VoxelShape LEDGE_NORTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 2.0, 16.0, 32.0, 16.0),
            Block.createCuboidShape(0, 0, -3.5, 16,1, 16));
    public static final VoxelShape LEDGE_EAST_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 0.0, 14.0, 32.0, 16.0),
            Block.createCuboidShape(0, 0, 0, 19.5,1, 16));
    public static final VoxelShape LEDGE_SOUTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 32.0, 14.0),
            Block.createCuboidShape(0, 0, 0, 16,1, 19.5));
    public static final VoxelShape LEDGE_WEST_SHAPE = VoxelShapes.union(Block.createCuboidShape(2.0, 0.0, 0.0, 16.0, 32.0, 16.0),
            Block.createCuboidShape(-3.5, 0, 0, 16,1, 16));

    public static final VoxelShape CUBE_NORTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 2.0, 16.0, 32.0, 16.0),
            Block.createCuboidShape(0, 0, -3.5, 16,1, 16));
    public static final VoxelShape CUBE_EAST_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 0.0, 14.0, 32.0, 16.0),
            Block.createCuboidShape(0, 0, 0, 19.5,1, 16));
    public static final VoxelShape CUBE_SOUTH_SHAPE = VoxelShapes.union(Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 32.0, 14.0),
            Block.createCuboidShape(0, 0, 0, 16,1, 19.5));
    public static final VoxelShape CUBE_WEST_SHAPE = VoxelShapes.union(Block.createCuboidShape(2.0, 0.0, 0.0, 16.0, 32.0, 16.0),
            Block.createCuboidShape(-3.5, 0, 0, 16,1, 16));

    public ExteriorBlock(Settings settings) {
        super(settings.nonOpaque());

        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return AITItems.TARDIS_ITEM.getDefaultStack();
    }


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ExteriorBlockEntity) || ((ExteriorBlockEntity) blockEntity).tardis() == null)
            return getNormalShape(state, world, pos);

        TardisTravel.State travelState = ((ExteriorBlockEntity) blockEntity).tardis().getTravel().getState();
        if (travelState == TardisTravel.State.LANDED || ((ExteriorBlockEntity) blockEntity).getAlpha() > 0.75)
            return getNormalShape(state, world, pos);

        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        /*BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ExteriorBlockEntity) || ((ExteriorBlockEntity) blockEntity).tardis() == null)
            return getNormalShape(state, world, pos);

        TardisTravel.State travelState = ((ExteriorBlockEntity) blockEntity).tardis().getTravel().getState();
        if (travelState == TardisTravel.State.LANDED || ((ExteriorBlockEntity) blockEntity).getAlpha() > 0.75)
            return getNormalShape(state, world, pos);*/

        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        // todo move these to a reusable method

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ExteriorBlockEntity) || ((ExteriorBlockEntity) blockEntity).tardis() == null)
            return getLedgeShape(state, world, pos);

        // todo this better because disabling collisions looks bad, should instead only disable if near to the portal or if walking into the block from the door direction
        if (DependencyChecker.hasPortals())
            if (((ExteriorBlockEntity) blockEntity).tardis().getDoor().isOpen() && ((ExteriorBlockEntity) blockEntity).tardis().getExterior().getType().hasPortals()) // for some reason this check totally murders fps ??
                return VoxelShapes.empty();

        TardisTravel.State travelState = ((ExteriorBlockEntity) blockEntity).tardis().getTravel().getState();
        if (travelState == TardisTravel.State.LANDED || ((ExteriorBlockEntity) blockEntity).getAlpha() > 0.75)
            return getLedgeShape(state, world, pos);

        return VoxelShapes.empty();
    }

    public VoxelShape getNormalShape(BlockState state, BlockView world, BlockPos pos) {
        if ( world.getBlockEntity(pos) instanceof ExteriorBlockEntity exterior && exterior.tardis() != null && exterior.tardis().getExterior().getVariant().bounding(state.get(FACING)) != null)
            return exterior.tardis().getExterior().getVariant().bounding(state.get(FACING));

        return switch (state.get(FACING)) {
            case NORTH -> CUBE_NORTH_SHAPE;
            case EAST -> CUBE_EAST_SHAPE;
            case SOUTH -> CUBE_SOUTH_SHAPE;
            case WEST -> CUBE_WEST_SHAPE;
            default ->
                    throw new RuntimeException("Invalid facing direction in " + this + ", //How did this happen? I messed up Plan A.");
        };
    }

    public VoxelShape getLedgeShape(BlockState state, BlockView world, BlockPos pos) {
        // fixme these wont have ledges probably
        if (world.getBlockEntity(pos) instanceof ExteriorBlockEntity exterior && exterior.tardis() != null && exterior.tardis().getExterior().getVariant().bounding(state.get(FACING)) != null)
            return exterior.tardis().getExterior().getVariant().bounding(state.get(FACING));

        return switch (state.get(FACING)) {
            case NORTH -> LEDGE_NORTH_SHAPE;
            case EAST -> LEDGE_EAST_SHAPE;
            case SOUTH -> LEDGE_SOUTH_SHAPE;
            case WEST -> LEDGE_WEST_SHAPE;
            default ->
                    throw new RuntimeException("Invalid facing direction in " + this + ", //How did this happen? I messed up Plan A.");
        };
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof ExteriorBlockEntity) || ((ExteriorBlockEntity) blockEntity).tardis() == null)
            return getNormalShape(state, world, pos);

        TardisTravel.State travelState = ((ExteriorBlockEntity) blockEntity).tardis().getTravel().getState();
        if (travelState == TardisTravel.State.LANDED || ((ExteriorBlockEntity) blockEntity).getAlpha() > 0.75)
            return getNormalShape(state, world, pos);

        return VoxelShapes.empty();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient())
            return ActionResult.SUCCESS;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ExteriorBlockEntity exteriorBlockEntity) {
            if (world.isClient()) {
                if (ClientTardisManager.getInstance().loadedTardises.contains(exteriorBlockEntity.tardis().getUuid())) {
                    ClientTardisManager.getInstance().loadedTardises.add(exteriorBlockEntity.tardis().getUuid());
                }
                ClientTardisManager.getInstance().ask(pos);
            }
            exteriorBlockEntity.useOn((ServerWorld) world, player.isSneaking(), player);
        }

        return ActionResult.CONSUME;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient())
            return;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ExteriorBlockEntity exterior) {
            exterior.onEntityCollision(entity);
        }
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

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return (world1, blockPos, blockState, ticker) -> {
            if (ticker instanceof ExteriorBlockEntity exterior) {
                exterior.tick(world, blockPos, blockState, exterior);
            }
        };
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);

        if (!world.isClient()) {
            BlockEntity entity = world.getBlockEntity(pos);

            if (!(entity instanceof ExteriorBlockEntity))
                return;

            ((ExteriorBlockEntity) entity).onBroken();
        }
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // this is called when the block is first placed, but we have a demat anim..
        tryFall(state, world, pos);
    }

    public void tryFall(BlockState state, ServerWorld world, BlockPos pos) {
        if (canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= world.getBottomY()
                && findTardis(world, pos) != null
                && !PropertiesHandler.getBool(findTardis(world, pos).getHandlers().getProperties(), PropertiesHandler.ANTIGRAVS_ENABLED)
                && findTardis(world, pos).getTravel().getState() == TardisTravel.State.LANDED) {
            FallingTardisEntity falling = FallingTardisEntity.spawnFromBlock(world, pos, state);
            // OH SHIT WE FALLING
            this.configureFallingTardis(falling, world, pos);
        }
    }

    private Tardis findTardis(ServerWorld world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof ExteriorBlockEntity exterior) {
            return exterior.tardis();
        }
        return null;
    }

    public void onLanding(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos, FallingTardisEntity fallingTardisEntity) {
        if(fallingTardisEntity.tardis() == null) return;
        fallingTardisEntity.tardis().getTravel().setPosition(new AbsoluteBlockPos.Directed(pos, world, fallingTardisEntity.tardis().getTravel().getPosition().getDirection()));

        world.playSound(null, pos, AITSounds.LAND_THUD, SoundCategory.BLOCKS);
        if(fallingTardisEntity.tardis().getDesktop().getConsolePos() != null)
            TardisUtil.getTardisDimension().playSound(null, fallingTardisEntity.tardis().getDesktop().getConsolePos(), AITSounds.LAND_THUD, SoundCategory.BLOCKS);

        PropertiesHandler.set(fallingTardisEntity.tardis().getHandlers().getProperties(), PropertiesHandler.IS_FALLING, false);
        PropertiesHandler.set(fallingTardisEntity.tardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED, false);
        fallingTardisEntity.tardis().markDirty();
    }

    protected void configureFallingTardis(FallingTardisEntity entity, ServerWorld world, BlockPos pos) {
    }
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(16) == 0) {
            BlockPos blockPos = pos.down();
            if (canFallThrough(world.getBlockState(blockPos))) {
                ParticleUtil.spawnParticle(world, pos, random, ParticleTypes.TOTEM_OF_UNDYING);
            }
        }
    }

    @Override
    public void onTryBreak(World world, BlockPos pos, BlockState state) {

    }
}
