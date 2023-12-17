package mdteam.ait.core.blocks;

import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.VariantEnum;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITDesktops;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.server.ServerTardis;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.item.Item;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoralPlantBlock extends Block {

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
            ServerTardisManager.getInstance().create(new AbsoluteBlockPos.Directed(pos, world, Direction.NORTH), ExteriorEnum.CAPSULE, VariantEnum.DEFAULT, ConsoleEnum.BOREALIS, AITDesktops.get(new Identifier(AITMod.MOD_ID, "default_cave")), false);

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
        builder.add(new Property[]{AGE});
    }

    static {
        AGE = Properties.AGE_7;
    }
}
