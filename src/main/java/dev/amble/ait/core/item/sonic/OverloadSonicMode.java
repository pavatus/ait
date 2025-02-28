package dev.amble.ait.core.item.sonic;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import dev.amble.ait.core.AITSounds;
import dev.amble.ait.core.AITTags;
import dev.amble.ait.data.schema.sonic.SonicSchema;

public class OverloadSonicMode extends SonicMode {

    protected OverloadSonicMode(int index) {
        super(index);
    }

    @Override
    public Text text() {
        return Text.translatable("sonic.ait.mode.overload").formatted(Formatting.RED, Formatting.BOLD);
    }

    @Override
    public void stopUsing(ItemStack stack, World world, LivingEntity user, int ticks, int ticksLeft) {
        if (!(world instanceof ServerWorld serverWorld))
            return;

        this.process(serverWorld, user, ticks);
    }

    @Override
    public void tick(ItemStack stack, World world, LivingEntity user, int ticks, int ticksLeft) {
        if (!(world instanceof ServerWorld serverWorld) || ticks % 10 != 0)
            return;

        this.process(serverWorld, user, ticks);
    }

    private void process(ServerWorld world, LivingEntity user, int ticks) {
        HitResult hitResult = SonicMode.getHitResult(user);

        if (hitResult instanceof BlockHitResult blockHit)
            this.overloadBlock(blockHit.getBlockPos(), world, user, ticks);
    }

    private void overloadBlock(BlockPos pos, ServerWorld world, LivingEntity user, int ticks) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (!state.isIn(AITTags.Blocks.SONIC_INTERACTABLE))
            return;

        if (canMakeRedstoneTweak(ticks)) {
            if (block instanceof DaylightDetectorBlock) {
                activateBlock(world, pos, user, state.with(Properties.POWER, 15));
            } else if (block instanceof RedstoneLampBlock) {
                activateBlock(world, pos, user, state.cycle(Properties.LIT));
            } else if (block instanceof ComparatorBlock) {
                activateBlock(world, pos, user, state.with(Properties.POWERED, true));
                forceRedstonePower(world, pos, 5 * 20);
            } else if (block instanceof RepeaterBlock) {
                activateBlock(world, pos, user, state.with(Properties.POWERED, true));
                forceRedstonePower(world, pos, 5 * 20);
            } else if (block == Blocks.REDSTONE_WIRE) {
                int newPower = (state.get(Properties.POWER) == 0) ? 15 : 0;
                activateBlock(world, pos, user, state.with(Properties.POWER, newPower));
                world.updateNeighbors(pos, block);
            }
        }

        if (canLit(ticks) && block instanceof TntBlock) {
            TntBlock.primeTnt(world, pos);
            world.removeBlock(pos, false);
            world.emitGameEvent(user, GameEvent.BLOCK_DESTROY, pos);
        }
    }

    private void activateBlock(ServerWorld world, BlockPos pos, LivingEntity user, BlockState newState) {
        world.setBlockState(pos, newState, Block.NOTIFY_ALL);
        world.emitGameEvent(user, GameEvent.BLOCK_CHANGE, pos);
        world.playSound(null, pos, AITSounds.SONIC_TWEAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.updateNeighbors(pos, newState.getBlock());
        spawnParticles(world, pos);
    }

    private void forceRedstonePower(ServerWorld world, BlockPos pos, int durationTicks) {
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        world.setBlockState(pos, state.with(Properties.POWERED, true), Block.NOTIFY_ALL);
        world.updateNeighbors(pos, block);
        world.scheduleBlockTick(pos, block, durationTicks);
    }

    private void spawnParticles(ServerWorld world, BlockPos pos) {
        world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 5, 0.2, 0.2, 0.2, 0.01);
        world.spawnParticles(ParticleTypes.LAVA, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 3, 0.1, 0.1, 0.1, 0.01);
    }

    private static boolean canLit(int ticks) {
        return ticks >= 10;
    }

    private static boolean canMakeRedstoneTweak(int ticks) {
        return ticks >= 10;
    }

    @Override
    public int maxTime() {
        return 5 * 60 * 20; // 5 minutes
    }

    @Override
    public Identifier model(SonicSchema.Models models) {
        return models.overload();
    }
}
