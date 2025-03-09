package dev.amble.ait.core.entities;

import java.util.Random;

import dev.amble.lib.util.TeleportUtil;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.world.*;

import dev.amble.ait.AITMod;
import dev.amble.ait.core.*;
import dev.amble.ait.core.entities.base.DummyAmbientEntity;
import dev.amble.ait.core.item.SonicItem;
import dev.amble.ait.core.util.StackUtil;

public class RiftEntity extends DummyAmbientEntity {

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
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        if (player.getServer() == null) {
            super.onPlayerCollision(player);
            return;
        }
        ServerWorld world = player.getServer().getWorld(AITDimensions.TIME_VORTEX_WORLD);
        if (world == null) {
            super.onPlayerCollision(player);
            return;
        }
        TeleportUtil.teleport(player, world, new Vec3d(0, 0, 0), player.bodyYaw);
    }

    @Override
    public final ActionResult interactMob(PlayerEntity player, Hand hand) {
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
                StackUtil.spawn(this.getWorld(), this.getBlockPos(), new ItemStack(AITItems.CORAL_FRAGMENT));
                this.getWorld().playSound(null, player.getBlockPos(), AITSounds.RIFT_SUCCESS, SoundCategory.AMBIENT, 1f, 1f);
            } else {
                StackUtil.spawn(this.getWorld(), this.getBlockPos(), new ItemStack(Items.PAPER));
                this.getWorld().playSound(null, this.getBlockPos(), AITSounds.RIFT_FAIL, SoundCategory.AMBIENT, 1f, 1f);
                spreadTardisCoral(this.getWorld(), this.getBlockPos());
            }

            this.discard();

            return gotFragment ? ActionResult.SUCCESS : ActionResult.FAIL;
        }

        return ActionResult.CONSUME;
    }

    private void spreadTardisCoral(World world, BlockPos pos) {
        int radius = 4;
        for (BlockPos targetPos : BlockPos.iterate(pos.add(-radius, -radius, -radius), pos.add(radius, radius, radius))) {
            if (RANDOM.nextFloat() < 0.3f) { // 30% chance per block
                BlockState currentState = world.getBlockState(targetPos);

                BlockState newState = getReplacementBlock(currentState);
                if (newState != null) {
                    world.setBlockState(targetPos, newState, Block.NOTIFY_ALL);

                    world.addParticle(AITMod.CORAL_PARTICLE,
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

    public static boolean canSpawn(EntityType<RiftEntity> rift,
                                   ServerWorldAccess serverWorldAccess, SpawnReason spawnReason,
                                   BlockPos pos, net.minecraft.util.math.random.Random random) {
        if (!(serverWorldAccess instanceof StructureWorldAccess worldAccess)) return false;

        if (spawnReason == SpawnReason.STRUCTURE) {
            AITMod.LOGGER.info("Spawning rift at {}, reason: structure", pos);
            return worldAccess.getBlockState(pos).isAir();
        }

        ChunkPos chunkPos = new ChunkPos(pos);
        boolean bl = ChunkRandom.getSlimeRandom(chunkPos.x, chunkPos.z,
                worldAccess.getSeed(), 987234910L).nextInt(8) == 0;
        if (random.nextInt(55) == 0 && bl) {
            AITMod.LOGGER.info("Spawning rift at {}, reaason: random", pos);
            return worldAccess.getBlockState(pos).isAir();
        }

        AITMod.LOGGER.info("Failed to spawn rift at {}", pos);
        return false;
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        double d = packet.getX();
        double e = packet.getY() + 0;
        double f = packet.getZ();
        float g = packet.getYaw();
        float h = packet.getPitch();
        this.updateTrackedPosition(d, e, f);
        this.bodyYaw = packet.getHeadYaw();
        this.headYaw = packet.getHeadYaw();
        this.prevBodyYaw = this.bodyYaw;
        this.prevHeadYaw = this.headYaw;
        this.setId(packet.getId());
        this.setUuid(packet.getUuid());
        this.updatePositionAndAngles(d, e, f, g, h);
        this.setVelocity(packet.getVelocityX(), packet.getVelocityY(), packet.getVelocityZ());
        this.updatePosition(d, e, f);
    }
}
