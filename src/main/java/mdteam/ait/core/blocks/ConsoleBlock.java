package mdteam.ait.core.blocks;

import mdteam.ait.api.ICantBreak;
import mdteam.ait.core.AITBlocks;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.AITSounds;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class ConsoleBlock extends HorizontalDirectionalBlock implements BlockEntityProvider, ICantBreak {

    public ConsoleBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ConsoleBlockEntity(pos, state);
    }

    @Override
    public BlockState getAppearance(BlockState state, BlockRenderView renderView, BlockPos pos, Direction side, @Nullable BlockState sourceState, @Nullable BlockPos sourcePos) {
        return super.getAppearance(state, renderView, pos, side, sourceState, sourcePos);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ConsoleBlockEntity consoleBlockEntity)
            consoleBlockEntity.useOn(world, player.isSneaking(), player);

        return ActionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull World world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return (world1, blockPos, blockState, ticker) -> {
            if (ticker instanceof ConsoleBlockEntity console) {
                console.tick(world, blockPos, blockState, console);
            }
        };
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) {
            // dont place yo
            world.breakBlock(pos, true);
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5f,pos.getY(),pos.getZ() + 0.5f,new ItemStack(AITBlocks.CONSOLE)));
            return;
        }

        if (world.getBlockEntity(pos) instanceof ConsoleBlockEntity consoleBlockEntity) {
            consoleBlockEntity.markNeedsControl();
        }
        super.onPlaced(world, pos, state, placer, itemStack);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof PlayerEntity player) {
//            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 30, 0));
//            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 60, 0));
//            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 60, 0));
//            player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 60, 0));
            Random random = new Random();
            int x_random = random.nextInt(1, 10);
            int y_random = random.nextInt(1, 10);
            int z_random = random.nextInt(1, 10);

            boolean is_x_negative = false;
            boolean is_z_negative = false;
            if (random.nextInt(1,3) == 1) {
                is_x_negative = true;
            }
            if (random.nextInt(1,3) == 1) {
                is_z_negative = true;
            }

            world.playSound(null, pos, AITSounds.CLOISTER, SoundCategory.BLOCKS, 4f, 1f);

            player.addVelocity(0.15f * x_random * (is_x_negative ? -1 : 1), 0.1f * y_random, 0.15f * z_random * (is_z_negative ? -1 : 1));

            for (int i = 0; i < 100; i++) {
                world.addParticle(ParticleTypes.ANGRY_VILLAGER, true, pos.getX() + random.nextFloat(-2, 3), pos.getY() + random.nextFloat(2), pos.getZ() + random.nextFloat(-2, 3), random.nextFloat(-5, 5), random.nextFloat(-5, 5), random.nextFloat(-5, 5));
            }
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    // This will literally never happen
    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);

        if (world.getBlockEntity(pos) instanceof ConsoleBlockEntity console) {
            console.onBroken();
        }
    }

    @Override
    public void onTryBreak(World world, BlockPos pos, BlockState state) {

    }
}
