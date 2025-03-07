package dev.amble.ait.core.blocks;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import dev.amble.ait.AITMod;
import dev.amble.ait.core.AITBlocks;
import dev.amble.ait.core.AITItems;
import dev.amble.ait.core.advancement.TardisCriterions;
import dev.amble.ait.core.blockentities.MatrixEnergizerBlockEntity;
import dev.amble.ait.core.blocks.types.HorizontalDirectionalBlock;
import dev.amble.ait.core.item.PersonalityMatrixItem;

@SuppressWarnings("deprecation")
public class MatrixEnergizerBlock extends HorizontalDirectionalBlock implements BlockEntityProvider {

    private final VoxelShape DEFAULT = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 32.0, 16.0);

    public static final IntProperty AGE = Properties.AGE_4;
    public static final BooleanProperty HAS_POWER = BooleanProperty.of("has_power");

    public MatrixEnergizerBlock(Settings settings) {
        super(settings);

        this.setDefaultState(
                this.getDefaultState().with(AGE, 0).with(HAS_POWER, false)
        );
    }

    protected IntProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 4;
    }

    public int getAge(BlockState state) {
        return state.get(this.getAgeProperty());
    }

    public final boolean isMature(BlockState blockState) {
        return this.getAge(blockState) >= this.getMaxAge();
    }
    public static boolean hasPower(BlockState state) {
        return state.get(HAS_POWER);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(Items.NETHER_STAR) && !hasPower(state)) {
            if (world.isClient()) return ActionResult.SUCCESS;

            world.setBlockState(pos, state.with(HAS_POWER, true));
            stack.decrement(1);

            world.playSound(null, pos, SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, 1.0F, 0.6F);

            tryCreate((ServerWorld) world, pos, state);

            return ActionResult.SUCCESS;
        }

        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);

        Vec3d centre = pos.up().toCenterPos();
        for (int i = 0; i < getAge(state); i++) {
            double offsetX = AITMod.RANDOM.nextGaussian() * getAge(state) * 0.01f;
            double offsetY = AITMod.RANDOM.nextGaussian() * getAge(state) * 0.01f;
            double offsetZ = AITMod.RANDOM.nextGaussian() * getAge(state) * 0.01f;
            world.addParticle(ParticleTypes.SCULK_SOUL, centre.getX(), centre.getY() , centre.getZ(), offsetX, offsetY, offsetZ);
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBaseLightLevel(pos, 0) >= 4) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                if (!(world.getBlockState(pos.down()).getBlock() instanceof SculkShriekerBlock)) {
                    world.breakBlock(pos, true);
                    return;
                }

                world.setBlockState(pos, state.with(AGE, i + 1), 2);
            }
        }

        tryCreate(world, pos, state);
    }

    private boolean tryCreate(ServerWorld world, BlockPos pos, BlockState state) {
        if (this.isMature(state) && hasPower(state)) {
            world.playSound(null, pos, SoundEvents.BLOCK_SCULK_CATALYST_BLOOM, SoundCategory.BLOCKS, 1.0F, 1.0F);
            ItemStack pmStack = AITItems.PERSONALITY_MATRIX.getDefaultStack();
            PersonalityMatrixItem pmItem = (PersonalityMatrixItem) pmStack.getItem();
            pmStack = pmItem.randomize();
            ItemEntity matrix = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), pmStack);
            world.spawnEntity(matrix);
            world.breakBlock(pos, false);

            return true;
        }

        return false;
    }
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
            ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (!(placer instanceof ServerPlayerEntity player))
            return;

        if (!(world.getBlockState(pos.down()).getBlock() instanceof SculkShriekerBlock)) {
            world.breakBlock(pos, !placer.isPlayer() || !player.isCreative());
            return;
        }

        if (world.getBlockEntity(pos) instanceof MatrixEnergizerBlockEntity matrix) {
            if (player.getUuid() != null) {
                matrix.creator = player.getUuid();
                matrix.markDirty();
            }
            TardisCriterions.PLACE_ENERGIZER.trigger(player);
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return (world.getBaseLightLevel(pos, 0) >= 4 || world.isSkyVisible(pos)) && super.canPlaceAt(state, world, pos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return DEFAULT;
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return DEFAULT;
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return AITBlocks.MATRIX_ENERGIZER.asItem().getDefaultStack();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE).add(FACING).add(HAS_POWER);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);

        tooltip.add(Text.translatable("tooltip.ait.matrix_energizer").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MatrixEnergizerBlockEntity(pos, state);
    }
}
