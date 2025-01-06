package loqor.ait.core.item.sonic;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import loqor.ait.core.AITTags;
import loqor.ait.data.schema.sonic.SonicSchema;

public class InteractionSonicMode extends SonicMode {

    protected InteractionSonicMode(int index) {
        super(index);
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

        if (!world.getBlockState(pos).isIn(AITTags.Blocks.SONIC_INTERACTABLE))
            return;

        if (canInteract2(ticks)
                && block instanceof TntBlock) {
            TntBlock.primeTnt(world, pos);

            world.removeBlock(pos, false);
            world.emitGameEvent(user, GameEvent.BLOCK_DESTROY, pos);
            return;
        }

        if (canInteract1(ticks)
                && block.getDefaultState().contains(BarrelBlock.OPEN)) {
            world.playSound(user, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f,
                    world.getRandom().nextFloat() * 0.4f + 0.8f);

            world.setBlockState(pos, state.cycle(BarrelBlock.OPEN));
            world.emitGameEvent(user, GameEvent.BLOCK_CHANGE, pos);
            return;
        }

        if (canInteract1(ticks)
                && block.getDefaultState().contains(DoorBlock.OPEN)) {
            world.playSound(user, pos, SoundEvents.BLOCK_WOODEN_DOOR_OPEN, SoundCategory.BLOCKS, 1.0f,
                    world.getRandom().nextFloat() * 0.4f + 0.8f);

            world.setBlockState(pos, state.cycle(DoorBlock.OPEN));
            world.emitGameEvent(user, GameEvent.BLOCK_ACTIVATE, pos);
            return;
        }
//TODO make this work
        if (canInteract3(ticks)
                && state.contains(RedstoneWireBlock.POWER)) {
            world.playSound(user, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f,
                    world.getRandom().nextFloat() * 0.4f + 0.8f);

            world.setBlockState(pos, state.with(RedstoneWireBlock.POWER, 15));
            world.emitGameEvent(user, GameEvent.BLOCK_CHANGE, pos);
        }

        if (canInteract3(ticks)
                && state.contains(DaylightDetectorBlock.INVERTED)) {
            world.playSound(user, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f,
                    world.getRandom().nextFloat() * 0.4f + 0.8f);

            world.setBlockState(pos, state.with(DaylightDetectorBlock.POWER, 15));
            world.emitGameEvent(user, GameEvent.BLOCK_ACTIVATE, pos);
        }

        if (canInteract2(ticks)
                && block.getDefaultState().contains(RedstoneLampBlock.LIT)) {
            world.playSound(user, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f,
                    world.getRandom().nextFloat() * 0.4f + 0.8f);

            world.setBlockState(pos, state.cycle(RedstoneLampBlock.LIT));
            world.emitGameEvent(user, GameEvent.BLOCK_CHANGE, pos);
            return;
        }

        if (canInteract3(ticks)
                && block.getDefaultState().contains(ComparatorBlock.MODE)) {
            world.playSound(user, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f,
                    world.getRandom().nextFloat() * 0.4f + 0.8f);

            world.setBlockState(pos, state.cycle(ComparatorBlock.POWERED));
            world.emitGameEvent(user, GameEvent.BLOCK_CHANGE, pos);
            return;
        }

        if (canInteract3(ticks)
                && block.getDefaultState().contains(RepeaterBlock.POWERED)) {
            world.playSound(user, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f,
                    world.getRandom().nextFloat() * 0.4f + 0.8f);

            world.setBlockState(pos, state.cycle(RepeaterBlock.POWERED));
            world.emitGameEvent(user, GameEvent.BLOCK_CHANGE, pos);
            return;
        }
    }

    private static boolean canInteract1(int ticks) {
        return ticks >= 10;
    }

    private static boolean canInteract2(int ticks) {
        return ticks >= 20;
    }

    private static boolean canInteract3(int ticks) {
        return ticks >= 30;
    }

    @Override
    public int maxTime() {
        return 5 * 60 * 20; // 5 minutes
    }

    @Override
    public Identifier model(SonicSchema.Models models) {
        return models.interaction();
    }
}
