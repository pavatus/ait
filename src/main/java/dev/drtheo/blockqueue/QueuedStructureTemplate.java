package dev.drtheo.blockqueue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.mojang.datafixers.util.Pair;
import dev.drtheo.blockqueue.data.TimeUnit;
import dev.drtheo.blockqueue.util.StepUtil;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.BitSetVoxelSet;
import net.minecraft.world.ServerWorldAccess;

import loqor.ait.mixin.server.structure.StructureTemplateAccessor;

public class QueuedStructureTemplate {

    private static final Direction[] directions = new Direction[]{ Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    private final List<StructureTemplate.PalettedBlockInfoList> blockInfoLists;
    private final List<StructureTemplate.StructureEntityInfo> entities;
    private final Vec3i size;

    public QueuedStructureTemplate(StructureTemplate template) {
        this((StructureTemplateAccessor) template);
    }

    private QueuedStructureTemplate(StructureTemplateAccessor accessor) {
        this.blockInfoLists = accessor.getBlockInfo();
        this.entities = accessor.getEntities();
        this.size = accessor.getSize();
    }

    public Optional<ActionQueue> place(ServerWorldAccess world, BlockPos pos, BlockPos pivot, StructurePlacementData placementData, Random random, int flags) {
        if (this.blockInfoLists.isEmpty())
            return Optional.empty();

        List<StructureTemplate.StructureBlockInfo> randomBlocks = placementData.getRandomBlockInfos(this.blockInfoLists, pos).getAll();

        List<BlockPos> flowingFluid = new ArrayList<>(placementData.shouldPlaceFluids() ? randomBlocks.size() : 0);
        List<BlockPos> stillFluid = new ArrayList<>(placementData.shouldPlaceFluids() ? randomBlocks.size() : 0);

        if (randomBlocks.isEmpty() && (placementData.shouldIgnoreEntities() || this.entities.isEmpty()) || this.size.getX() < 1 || this.size.getY() < 1 || this.size.getZ() < 1)
            return Optional.empty();

        BlockBox blockBox = placementData.getBoundingBox();
        List<Pair<BlockPos, NbtCompound>> nbtList = new ArrayList<>(randomBlocks.size());

        var ctx = new Object() {
            int x1 = Integer.MAX_VALUE;
            int y1 = Integer.MAX_VALUE;
            int z1 = Integer.MAX_VALUE;

            int x2 = Integer.MIN_VALUE;
            int y2 = Integer.MIN_VALUE;
            int z2 = Integer.MIN_VALUE;
        };

        List<StructureTemplate.StructureBlockInfo> processedBlocks = StructureTemplate.process(world, pos, pivot, placementData, randomBlocks);
        Iterator<StructureTemplate.StructureBlockInfo> blockInfoIter = processedBlocks.iterator();

        return Optional.of(new ActionQueue().thenRun(f -> StepUtil.scheduleSteps(f, () -> {
            if (!blockInfoIter.hasNext())
                return true;

            StructureTemplate.StructureBlockInfo blockInfo = blockInfoIter.next();

            BlockPos blockPos = blockInfo.pos();

            if (blockBox != null && !blockBox.contains(blockPos))
                return false;

            FluidState fluidState = placementData.shouldPlaceFluids() ? world.getFluidState(blockPos) : null;
            BlockState blockState = blockInfo.state().mirror(placementData.getMirror()).rotate(placementData.getRotation());

            if (blockInfo.nbt() != null)
                Clearable.clear(world.getBlockEntity(blockPos));

            // !
            world.setBlockState(blockPos, blockState, flags);

            ctx.x1 = Math.min(ctx.x1, blockPos.getX());
            ctx.y1 = Math.min(ctx.y1, blockPos.getY());
            ctx.z1 = Math.min(ctx.z1, blockPos.getZ());

            ctx.x2 = Math.max(ctx.x2, blockPos.getX());
            ctx.y2 = Math.max(ctx.y2, blockPos.getY());
            ctx.z2 = Math.max(ctx.z2, blockPos.getZ());

            nbtList.add(Pair.of(blockPos, blockInfo.nbt()));
            BlockEntity blockEntity = world.getBlockEntity(blockPos);

            // !
            if (blockInfo.nbt() != null && blockEntity != null)
                this.readNbt(blockEntity, blockInfo.nbt(), random);

            if (fluidState == null)
                return false;

            if (blockState.getFluidState().isStill()) {
                stillFluid.add(blockPos);
                return false;
            }

            if (!(blockState.getBlock() instanceof FluidFillable fillable))
                return false;

            // !
            fillable.tryFillWithFluid(world, blockPos, blockState, fluidState);

            if (fluidState.isStill())
                return false;

            flowingFluid.add(blockPos);
            return false;
        }, TimeUnit.TICKS, 1, 20))
                .thenRun(() -> {
                    // !
                    this.fillWithFluid(world, flowingFluid, stillFluid);

                    if (ctx.x1 <= ctx.x2)
                        this.update(world, placementData, nbtList, ctx.x1, ctx.y1, ctx.z1,
                                ctx.x2, ctx.y2, ctx.z2, flags);

                    if (!placementData.shouldIgnoreEntities())
                        this.spawnEntities(world, pos, placementData.getMirror(), placementData.getRotation(), placementData.getPosition(), blockBox, placementData.shouldInitializeMobs());
                }));
    }

    protected void readNbt(BlockEntity blockEntity, NbtCompound nbt, Random random) {
        if (blockEntity instanceof LootableContainerBlockEntity)
            nbt.putLong("LootTableSeed", random.nextLong());

        blockEntity.readNbt(nbt);
    }

    private void update(ServerWorldAccess world, StructurePlacementData placementData, List<Pair<BlockPos, NbtCompound>> nbtList, int x1, int y1, int z1, int x2, int y2, int z2, int flags) {
        if (!placementData.shouldUpdateNeighbors()) {
            BitSetVoxelSet voxelSet = new BitSetVoxelSet(x2 - x1 + 1, y2 - y1 + 1, z2 - z1 + 1);

            for (Pair<BlockPos, NbtCompound> pair : nbtList) {
                BlockPos pos = pair.getFirst();
                voxelSet.set(pos.getX() - x1, pos.getY() - y1, pos.getZ() - z1);
            }

            StructureTemplate.updateCorner(world, flags, voxelSet, x1, y1, z1);
        }

        for (Pair<BlockPos, NbtCompound> pair : nbtList) {
            BlockPos pos = pair.getFirst();
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (!placementData.shouldUpdateNeighbors()) {
                BlockState originalState = world.getBlockState(pos);
                BlockState processedState = Block.postProcessState(originalState, world, pos);

                // !
                if (originalState != processedState)
                    world.setBlockState(pos, processedState, flags & ~Block.NOTIFY_NEIGHBORS | Block.FORCE_STATE);

                world.updateNeighbors(pos, processedState.getBlock());
            }

            if (pair.getSecond() == null || blockEntity == null)
                continue;

            blockEntity.markDirty();
        }
    }

    private void fillWithFluid(ServerWorldAccess world, List<BlockPos> flowing, List<BlockPos> still) {
        boolean shouldContinue = true;

        while (shouldContinue && !flowing.isEmpty()) {
            shouldContinue = false;

            for (BlockPos blockPos : flowing) {
                BlockState blockState = world.getBlockState(blockPos);
                FluidState fluidState = world.getFluidState(blockPos);
                Block block = blockState.getBlock();

                if (!(block instanceof FluidFillable fillable))
                    continue;

                for (int o = 0; o < directions.length && !fluidState.isStill(); ++o) {
                    BlockPos offsetPos = blockPos.offset(directions[o]);
                    FluidState offsetFluidState = world.getFluidState(offsetPos);
                    if (!offsetFluidState.isStill() || still.contains(offsetPos))
                        continue;

                    fluidState = offsetFluidState;
                }

                if (!fluidState.isStill())
                    continue;

                // !
                fillable.tryFillWithFluid(world, blockPos, blockState, fluidState);
                shouldContinue = true;
            }
        }
    }

    private void spawnEntities(ServerWorldAccess world, BlockPos pos, BlockMirror mirror, BlockRotation rotation, BlockPos pivot, @Nullable BlockBox area, boolean initializeMobs) {
        for (StructureTemplate.StructureEntityInfo structureEntityInfo : this.entities) {
            BlockPos blockPos = StructureTemplate.transformAround(structureEntityInfo.blockPos, mirror, rotation, pivot).add(pos);

            if (area != null && !area.contains(blockPos))
                continue;

            NbtCompound nbtCompound = structureEntityInfo.nbt.copy();

            Vec3d vec3d = StructureTemplate.transformAround(structureEntityInfo.pos, mirror, rotation, pivot);
            Vec3d vec3d2 = vec3d.add(pos.getX(), pos.getY(), pos.getZ());

            NbtList nbtList = new NbtList();

            nbtList.add(NbtDouble.of(vec3d2.x));
            nbtList.add(NbtDouble.of(vec3d2.y));
            nbtList.add(NbtDouble.of(vec3d2.z));

            nbtCompound.put("Pos", nbtList);
            nbtCompound.remove("UUID");

            getEntity(world, nbtCompound).ifPresent(entity -> {
                float yaw = entity.applyRotation(rotation) + entity.applyMirror(mirror) - entity.getYaw();
                entity.refreshPositionAndAngles(vec3d.x, vec3d.y, vec3d.z, yaw, entity.getPitch());

                if (initializeMobs && entity instanceof MobEntity mob)
                    mob.initialize(world, world.getLocalDifficulty(BlockPos.ofFloored(vec3d2)), SpawnReason.STRUCTURE, null, nbtCompound);

                world.spawnEntityAndPassengers(entity);
            });
        }
    }

    private static Optional<Entity> getEntity(ServerWorldAccess world, NbtCompound nbt) {
        try {
            return EntityType.getEntityFromNbt(nbt, world.toServerWorld());
        } catch (Exception exception) {
            return Optional.empty();
        }
    }
}
