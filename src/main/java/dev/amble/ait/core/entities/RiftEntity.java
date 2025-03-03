package dev.amble.ait.core.entities;

import java.util.List;
import java.util.Random;

import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import dev.amble.ait.AITMod;
import dev.amble.ait.core.AITBlocks;
import dev.amble.ait.core.AITEntityTypes;
import dev.amble.ait.core.AITItems;
import dev.amble.ait.core.AITSounds;
import dev.amble.ait.core.item.SonicItem;

public class RiftEntity extends LivingEntity {
    private int interactAmount = 0;
    private int ambientSoundCooldown = 0;
    private int currentSoundIndex = 0;
    private static final Random RANDOM = new Random();

    private static final SoundEvent[] RIFT_SOUNDS = {
            AITSounds.RIFT1_AMBIENT,
            AITSounds.RIFT2_AMBIENT,
            AITSounds.RIFT3_AMBIENT
    };

    private static final int[] RIFT_DURATIONS = {
            15 * 20,
            13 * 20,
            14 * 20
    };

    public RiftEntity(EntityType<?> type, World world) {
        super(AITEntityTypes.RIFT_ENTITY, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return List.of(new ItemStack[0]);
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public DataTracker getDataTracker() {
        return super.getDataTracker();
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (this.getWorld().isClient()) return ActionResult.SUCCESS;

        ItemStack stack = player.getStackInHand(hand);

        if (stack.getItem() instanceof SonicItem sonic) {
            if (!this.getWorld().isClient()) {
                sonic.addFuel(1000, stack);
                this.getWorld().playSound(null, this.getBlockPos(), AITSounds.RIFT_SONIC, SoundCategory.AMBIENT, 1f, 1f);
                this.discard();
            }
            return ActionResult.SUCCESS;

        }
        interactAmount += 1;

        if (interactAmount >= 3) {
            boolean gotFragment = this.getWorld().getRandom().nextBoolean();

            player.damage(this.getWorld().getDamageSources().hotFloor(), 7);
            if (gotFragment) {
                spawnItem(this.getWorld(), this.getBlockPos(), new ItemStack(AITItems.CORAL_FRAGMENT));
                this.getWorld().playSound(null, player.getBlockPos(), AITSounds.RIFT_SUCCESS, SoundCategory.AMBIENT, 1f, 1f);
            } else {
                spawnItem(this.getWorld(), this.getBlockPos(), new ItemStack(Items.PAPER));
                this.getWorld().playSound(null, this.getBlockPos(), AITSounds.RIFT_FAIL, SoundCategory.AMBIENT, 1f, 1f);
                spreadTardisCoral(this.getWorld(), this.getBlockPos());
            }

            this.discard();

            return gotFragment ? ActionResult.SUCCESS : ActionResult.FAIL;
        }

        return ActionResult.CONSUME;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    public static void spawnItem(World world, BlockPos pos, ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        world.spawnEntity(entity);
    }

    //TODO: Optimize this stuff and make it use "world height map" or whatever
    //I wasent able to because rifts brain is null which makes world height thingy hate it
    //Duzo if your seeing this pls fix rifts and their dammed null brain :pray:
    private void spreadTardisCoral(World world, BlockPos pos) {
        int radius = 4;
        for (BlockPos targetPos : BlockPos.iterate(pos.add(-radius, -radius, -radius), pos.add(radius, radius, radius))) {
            if (RANDOM.nextFloat() < 0.3f) { // 30% chance per block
                BlockState currentState = world.getBlockState(targetPos);

                BlockState newState = getReplacementBlock(currentState);
                if (newState != null) {
                    world.setBlockState(targetPos, newState, Block.NOTIFY_ALL);

                    world.addParticle((ParticleEffect) AITMod.CORAL_PARTICLE,
                            targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5,
                            0, 0, 0);

                    if (newState.isOf(AITBlocks.TARDIS_CORAL_BLOCK)) {
                        placeCoralFans(world, targetPos);
                    }
                }
            }
        }
    }

    private BlockState getReplacementBlock(BlockState currentState) {
        Block block = currentState.getBlock();

        if (block instanceof SlabBlock) return AITBlocks.TARDIS_CORAL_SLAB.getDefaultState()
                .with(Properties.SLAB_TYPE, currentState.get(Properties.SLAB_TYPE));

        if (block instanceof StairsBlock) return AITBlocks.TARDIS_CORAL_STAIRS.getDefaultState()
                .with(Properties.HORIZONTAL_FACING, currentState.get(Properties.HORIZONTAL_FACING))
                .with(Properties.SLAB_TYPE, currentState.get(Properties.SLAB_TYPE))
                .with(Properties.STAIR_SHAPE, currentState.get(Properties.STAIR_SHAPE));


        if (canTransform(block)) return AITBlocks.TARDIS_CORAL_BLOCK.getDefaultState();

        return null;
    }

    private boolean canTransform(Block block) {
        return block == Blocks.STONE || block == Blocks.DIRT || block == Blocks.GRASS_BLOCK ||
                block == Blocks.SAND || block == Blocks.DEEPSLATE;
    }

    private void placeCoralFans(World world, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockPos adjacent = pos.offset(dir);
            if (world.getBlockState(adjacent).isAir() && isCoralBlock(world.getBlockState(pos))) {
                world.setBlockState(adjacent, AITBlocks.TARDIS_CORAL_FAN.getDefaultState()
                        .with(Properties.WATERLOGGED,false)
                        .with(Properties.FACING, dir), Block.NOTIFY_ALL);
            }
        }
    }

    private boolean isCoralBlock(BlockState state) {
        return state.isOf(AITBlocks.TARDIS_CORAL_BLOCK);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient()) {
            if (ambientSoundCooldown > 0) {
                ambientSoundCooldown--;
            } else {
                this.getWorld().playSound(null, this.getBlockPos(), RIFT_SOUNDS[currentSoundIndex], SoundCategory.AMBIENT, 1.0f, 1.0f);
                ambientSoundCooldown = RIFT_DURATIONS[currentSoundIndex];
                currentSoundIndex = (currentSoundIndex + 1) % RIFT_SOUNDS.length;
            }
        }
    }

    @Override
    public Arm getMainArm() {
        return Arm.LEFT;
    }
}
