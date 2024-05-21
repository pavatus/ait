package loqor.ait.core.blockentities;

import com.google.common.collect.Lists;
import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.AITBlocks;
import loqor.ait.core.AITDimensions;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.link.LinkableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static loqor.ait.tardis.util.TardisUtil.findTardisByInterior;

public class EngineCoreBlockEntity extends LinkableBlockEntity {
    private static final Block[] ACTIVATING_BLOCKS;
    public int ticks;
    private float ticksActive;
    private boolean active;
    private boolean eyeOpen;
    private final List<BlockPos> activatingBlocks = Lists.newArrayList();
    @Nullable
    private LivingEntity targetEntity;
    @Nullable
    private UUID targetUuid;
    private long nextAmbientSoundTime;
    public static final String HAS_ENGINE_CORE = "has_engine_core";

    public EngineCoreBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.ENGINE_CORE_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.containsUuid("Target")) {
            this.targetUuid = nbt.getUuid("Target");
        } else {
            this.targetUuid = null;
        }

    }

    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.targetEntity != null) {
            nbt.putUuid("Target", this.targetEntity.getUuid());
        }

    }

    @Override
    public Optional<Tardis> findTardis() {
        if (this.tardisId == null && this.hasWorld()) {
            assert this.getWorld() != null;
            Tardis found = findTardisByInterior(pos, !this.getWorld().isClient());
            if (found != null)
                this.setTardis(found);
        }
        return super.findTardis();
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public NbtCompound toInitialChunkDataNbt() {
        return this.createNbt();
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, EngineCoreBlockEntity blockEntity) {

        if(blockEntity.findTardis().isEmpty() || world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return;

        ++blockEntity.ticks;
        long l = world.getTime();
        List<BlockPos> list = blockEntity.activatingBlocks;
        if (l % 40L == 0L) {
            blockEntity.active = updateActivatingBlocks(world, pos, list);
            openEye(blockEntity, list);
        }

        updateTargetEntity(world, pos, blockEntity);
        spawnNautilusParticles(world, pos, list, blockEntity.targetEntity, blockEntity.ticks);
        if (blockEntity.isActive()) {
            ++blockEntity.ticksActive;
        }

    }

    public static void serverTick(World world, BlockPos pos, BlockState state, EngineCoreBlockEntity blockEntity) {

        if(blockEntity.findTardis().isEmpty() || world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return;

        ++blockEntity.ticks;
        long l = world.getTime();
        List<BlockPos> list = blockEntity.activatingBlocks;
        if (l % 40L == 0L) {
            boolean bl = updateActivatingBlocks(world, pos, list);
            if (bl != blockEntity.active) {
                SoundEvent soundEvent = bl ? SoundEvents.BLOCK_CONDUIT_ACTIVATE : SoundEvents.BLOCK_CONDUIT_DEACTIVATE;
                world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            blockEntity.active = bl;
            openEye(blockEntity, list);
            if (bl) {
                givePlayersEffects(world, pos, list);
                //attackHostileEntity(world, pos, state, list, blockEntity);
            }
        }

        if (blockEntity.isActive()) {
            if (l % 80L == 0L) {
                world.playSound(null, pos, SoundEvents.BLOCK_CONDUIT_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            if (l > blockEntity.nextAmbientSoundTime) {
                blockEntity.nextAmbientSoundTime = l + 60L + (long)world.getRandom().nextInt(40);
                world.playSound(null, pos, SoundEvents.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }

        // @TODO positively awful idea, IM GONNA DO IT ANYWAYS HAHAHA
        Optional<Tardis> tardis = blockEntity.findTardis();
        if (tardis.isEmpty()) return;
        PropertiesHandler.set(tardis.get(), HAS_ENGINE_CORE, blockEntity.isActive(), true);
    }

    private static void openEye(EngineCoreBlockEntity blockEntity, List<BlockPos> activatingBlocks) {
        blockEntity.setEyeOpen(!activatingBlocks.isEmpty()); // @TODO: do not forget to change this for release
    }

    private static boolean updateActivatingBlocks(World world, BlockPos pos, List<BlockPos> activatingBlocks) {
        activatingBlocks.clear();

        int i;
        int j;
        int k;
        for(i = -1; i <= 1; ++i) {
            for(j = -1; j <= 1; ++j) {
                for(k = -1; k <= 1; ++k) {
                    BlockPos blockPos = pos.add(i, j, k);
                    if (!world.isWater(blockPos)) {
                        return false;
                    }
                }
            }
        }

        /*for(i = -2; i <= 2; ++i) {
            for(j = -2; j <= 2; ++j) {
                for(k = -2; k <= 2; ++k) {
                    int l = Math.abs(i);
                    int m = Math.abs(j);
                    int n = Math.abs(k);
                    if ((i == 0 && (m == 2 || n == 2) || j == 0 && (l == 2 || n == 2) || k == 0 && (l == 2 || m == 2))) {
                        BlockPos blockPos2 = pos.add(i, j, k);
                        BlockState blockState = world.getBlockState(blockPos2);

                        for (Block block : ACTIVATING_BLOCKS) {
                            if (blockState.isOf(block)) {
                                activatingBlocks.add(blockPos2);
                            }
                        }
                    }
                }
            }
        }

        return activatingBlocks.size() >= 16;*/
        return true;
    }

    private static void givePlayersEffects(World world, BlockPos pos, List<BlockPos> activatingBlocks) {
        int i = activatingBlocks.size();
        int j = i / 7 * 8;
        int k = pos.getX();
        int l = pos.getY();
        int m = pos.getZ();
        Box box = (new Box(k, l, m, k + 1, l + 1, m + 1)).expand(j).stretch(0.0, world.getHeight(), 0.0);
        List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);
        if (!list.isEmpty()) {
            for (PlayerEntity playerEntity : list) {
                if (pos.isWithinDistance(playerEntity.getBlockPos(), j) && playerEntity.isTouchingWaterOrRain() && !playerEntity.getInventory().player.hasStackEquipped(EquipmentSlot.CHEST)) {
                    playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 130, 0, true, true));
                }
            }

        }
    }

    /*private static void attackHostileEntity(World world, BlockPos pos, BlockState state, List<BlockPos> activatingBlocks, EngineCoreBlockEntity blockEntity) {
        LivingEntity livingEntity = blockEntity.targetEntity;
        int i = activatingBlocks.size();
        if (i < 42) {
            blockEntity.targetEntity = null;
        } else if (blockEntity.targetEntity == null && blockEntity.targetUuid != null) {
            blockEntity.targetEntity = findTargetEntity(world, pos, blockEntity.targetUuid);
            blockEntity.targetUuid = null;
        } else if (blockEntity.targetEntity == null) {
            List<LivingEntity> list = world.getEntitiesByClass(LivingEntity.class, getAttackZone(pos), (entity) -> entity instanceof Monster && entity.isTouchingWaterOrRain());
            if (!list.isEmpty()) {
                blockEntity.targetEntity = list.get(world.random.nextInt(list.size()));
            }
        } else if (!blockEntity.targetEntity.isAlive() || !pos.isWithinDistance(blockEntity.targetEntity.getBlockPos(), 8.0)) {
            blockEntity.targetEntity = null;
        }

        if (blockEntity.targetEntity != null) {
            world.playSound(null, blockEntity.targetEntity.getX(), blockEntity.targetEntity.getY(), blockEntity.targetEntity.getZ(), SoundEvents.BLOCK_CONDUIT_ATTACK_TARGET, SoundCategory.BLOCKS, 1.0F, 1.0F);
            blockEntity.targetEntity.damage(world.getDamageSources().magic(), 4.0F);
        }

        if (livingEntity != blockEntity.targetEntity) {
            world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
        }

    }*/

    private static void updateTargetEntity(World world, BlockPos pos, EngineCoreBlockEntity blockEntity) {
        if (blockEntity.targetUuid == null) {
            blockEntity.targetEntity = null;
        } else if (blockEntity.targetEntity == null || !blockEntity.targetEntity.getUuid().equals(blockEntity.targetUuid)) {
            blockEntity.targetEntity = findTargetEntity(world, pos, blockEntity.targetUuid);
            if (blockEntity.targetEntity == null) {
                blockEntity.targetUuid = null;
            }
        }

    }

    private static Box getAttackZone(BlockPos pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        return (new Box(i,j,k,(i + 1),(j + 1), k + 1)).expand(8.0);
    }

    @Nullable
    private static LivingEntity findTargetEntity(World world, BlockPos pos, UUID uuid) {
        List<LivingEntity> list = world.getEntitiesByClass(LivingEntity.class, getAttackZone(pos), (entity) -> entity.getUuid().equals(uuid));
        return list.size() == 1 ? list.get(0) : null;
    }

    private static void spawnNautilusParticles(World world, BlockPos pos, List<BlockPos> activatingBlocks, @Nullable Entity entity, int ticks) {
        Random random = world.random;
        double d = MathHelper.sin((float)(ticks + 35) * 0.1F) / 2.0F + 0.5F;
        d = (d * d + d) * 0.30000001192092896;
        Vec3d vec3d = new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 1.5 + d, (double)pos.getZ() + 0.5);
        Iterator<BlockPos> var9 = activatingBlocks.iterator();

        float f;
        while(var9.hasNext()) {
            BlockPos blockPos = var9.next();
            if (random.nextInt(50) == 0) {
                BlockPos blockPos2 = blockPos.subtract(pos);
                f = -0.5F + random.nextFloat() + (float)blockPos2.getX();
                float g = -2.0F + random.nextFloat() + (float)blockPos2.getY();
                float h = -0.5F + random.nextFloat() + (float)blockPos2.getZ();
                world.addParticle(ParticleTypes.NAUTILUS, vec3d.x, vec3d.y, vec3d.z, f, g, h);
            }
        }

        if (entity != null) {
            Vec3d vec3d2 = new Vec3d(entity.getX(), entity.getEyeY(), entity.getZ());
            float i = (-0.5F + random.nextFloat()) * (3.0F + entity.getWidth());
            float j = -1.0F + random.nextFloat() * entity.getHeight();
            f = (-0.5F + random.nextFloat()) * (3.0F + entity.getWidth());
            Vec3d vec3d3 = new Vec3d(i, j, f);
            world.addParticle(ParticleTypes.NAUTILUS, vec3d2.x, vec3d2.y, vec3d2.z, vec3d3.x, vec3d3.y, vec3d3.z);
        }

    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isEyeOpen() {
        return this.eyeOpen;
    }

    private void setEyeOpen(boolean eyeOpen) {
        this.eyeOpen = eyeOpen;
    }

    public float getRotation(float tickDelta) {
        return (this.ticksActive + tickDelta) * -0.0375F;
    }

    static {
        ACTIVATING_BLOCKS = new Block[] {
                AITBlocks.ZEITON_BLOCK,
                Blocks.MAGMA_BLOCK
        };
    }

    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        Optional<Tardis> tardis = this.findTardis();
        if (tardis.isEmpty()) return;
        PropertiesHandler.set(tardis.get(), HAS_ENGINE_CORE, false, true);
        tardis.get().disablePower();
    }
}
