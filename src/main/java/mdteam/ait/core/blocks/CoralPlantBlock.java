package mdteam.ait.core.blocks;

import mdteam.ait.AITMod;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITExteriorVariants;
import mdteam.ait.core.blockentities.CoralBlockEntity;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.registry.DesktopRegistry;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.tardis.exterior.CapsuleExterior;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.server.ServerTardis;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoralPlantBlock extends HorizontalDirectionalBlock implements BlockEntityProvider {

    private final VoxelShape DEFAULT = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 32.0, 16.0);

    public static final int MAX_AGE = 7;
    public static final IntProperty AGE;
    public CoralPlantBlock(Settings settings) {
        super(settings);
    }

    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(Blocks.SOUL_SAND);
    }

    protected IntProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 7;
    }

    public int getAge(BlockState state) {
        return (Integer)state.get(this.getAgeProperty());
    }

    public BlockState withAge(int age) {
        return (BlockState)this.getDefaultState().with(this.getAgeProperty(), age);
    }

    public final boolean isMature(BlockState blockState) {
        return this.getAge(blockState) >= this.getMaxAge();
    }

    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                if (!(world.getBlockState(pos.down()).getBlock() instanceof SoulSandBlock)) {
                    world.breakBlock(pos, true);
                    return;
                }

                world.setBlockState(pos, this.withAge(i + 1), 2);
            }
        }

        if (this.getAge(state) >= this.getMaxAge()) {
            // Create a new tardis
            ServerTardis created = ServerTardisManager.getInstance().create(new AbsoluteBlockPos.Directed(pos, world, Direction.NORTH), ExteriorRegistry.REGISTRY.get(CapsuleExterior.REFERENCE), AITExteriorVariants.get(new Identifier(AITMod.MOD_ID, "capsule_default")), DesktopRegistry.CAVE, false);
            // created.getHandlers().getOvergrownHandler().setOvergrown(true); //fixme created.getEnvironmentHandler().setCoralCovered(true);

            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
            lightning.setPos(pos.getX(),pos.getY(),pos.getZ());
            world.spawnEntity(lightning);
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (!(world instanceof ServerWorld)) return;
        if (!(world.getBlockState(pos.down()).getBlock() instanceof SoulSandBlock) || !TardisUtil.isRiftChunk((ServerWorld) world,pos)) {
            // GET IT OUTTA HERE!!!
            world.breakBlock(pos, true);
            world.removeBlockEntity(pos);
        }
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        List<ItemStack> list = super.getDroppedStacks(state, builder);
        list.add(AITBlocks.CORAL_PLANT.asItem().getDefaultStack());

        return list;
    }

    public void applyGrowth(World world, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + this.getGrowthAmount(world);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }

        world.setBlockState(pos, this.withAge(i), 2);
    }

    protected int getGrowthAmount(World world) {
        return MathHelper.nextInt(world.random, 2, 5);
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return (world.getBaseLightLevel(pos, 0) >= 8 || world.isSkyVisible(pos)) && super.canPlaceAt(state, world, pos);
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof RavagerEntity && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            world.breakBlock(pos, true, entity);
        }

        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return DEFAULT;
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return DEFAULT;
    }

    protected ItemConvertible getSeedsItem() {
        return Items.WHEAT_SEEDS;
    }

    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return AITBlocks.CORAL_PLANT.asItem().getDefaultStack();
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        this.applyGrowth(world, pos, state);
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE}).add(FACING);
    }

    static {
        AGE = Properties.AGE_7;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CoralBlockEntity(pos, state);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockRenderView renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
        return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
    }
}
