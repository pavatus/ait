package loqor.ait.tardis.desktops;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import loqor.ait.AITMod;
import loqor.ait.mixin.server.structure.StructureTemplateAccessor;
import loqor.ait.mixin.server.structure.StructureTemplateInvoker;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.link.v2.interior.InteriorLinkableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.Clearable;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.BitSetVoxelSet;
import net.minecraft.world.ServerWorldAccess;

import java.util.Iterator;
import java.util.List;

import static net.minecraft.structure.StructureTemplate.StructureBlockInfo;

public class TardisStructureTemplate {

    private final StructureTemplate template;

    public TardisStructureTemplate(StructureTemplate template) {
        this.template = template;
    }

    public boolean place(Tardis tardis, ServerWorldAccess world, BlockPos pos, BlockPos pivot, StructurePlacementData placement, Random random, int flags) {
        StructureTemplateAccessor accessor = (StructureTemplateAccessor) this.template;

        if (accessor.getBlockInfoLists().isEmpty())
            return false;

        List<StructureBlockInfo> list = placement.getRandomBlockInfos(accessor.getBlockInfoLists(), pos).getAll();
        if (list.isEmpty() && (placement.shouldIgnoreEntities() || accessor.getEntities().isEmpty())
                || accessor.getSize().getX() < 1 || accessor.getSize().getY() < 1 || accessor.getSize().getZ() < 1) {
            return false;
        }

        BlockBox blockBox = placement.getBoundingBox();

        List<BlockPos> list2 = Lists.newArrayListWithCapacity(placement.shouldPlaceFluids() ? list.size() : 0);
        List<BlockPos> list3 = Lists.newArrayListWithCapacity(placement.shouldPlaceFluids() ? list.size() : 0);

        List<Pair<BlockPos, NbtCompound>> nbts = Lists.newArrayListWithCapacity(list.size());

        int i = Integer.MAX_VALUE;
        int j = Integer.MAX_VALUE;
        int k = Integer.MAX_VALUE;
        int l = Integer.MIN_VALUE;
        int m = Integer.MIN_VALUE;
        int n = Integer.MIN_VALUE;

        List<StructureBlockInfo> blockInfos = StructureTemplate.process(world, pos, pivot, placement, list);

        for (StructureBlockInfo structureBlockInfo : blockInfos) {
            BlockPos blockPos = structureBlockInfo.pos();

            if (blockBox != null && !blockBox.contains(blockPos))
                continue;

            FluidState fluidState = placement.shouldPlaceFluids() ? world.getFluidState(blockPos) : null;
            BlockState blockState = structureBlockInfo.state().mirror(placement.getMirror()).rotate(placement.getRotation());

            BlockEntity blockEntity;
            if (structureBlockInfo.nbt() != null) {
                blockEntity = world.getBlockEntity(blockPos);
                Clearable.clear(blockEntity);

                world.setBlockState(blockPos, Blocks.BARRIER.getDefaultState(), Block.NO_REDRAW | Block.FORCE_STATE);
            }

            if (!world.setBlockState(blockPos, blockState, flags))
                continue;

            i = Math.min(i, blockPos.getX());
            j = Math.min(j, blockPos.getY());
            k = Math.min(k, blockPos.getZ());
            l = Math.max(l, blockPos.getX());
            m = Math.max(m, blockPos.getY());
            n = Math.max(n, blockPos.getZ());

            nbts.add(Pair.of(blockPos, structureBlockInfo.nbt()));

            if (structureBlockInfo.nbt() != null && (blockEntity = world.getBlockEntity(blockPos)) != null) {
                NbtCompound toLoad = structureBlockInfo.nbt();

                if (blockEntity instanceof LootableContainerBlockEntity)
                    toLoad.putLong("LootTableSeed", random.nextLong());

                if (blockEntity instanceof InteriorLinkableBlockEntity linkable) {
                    AITMod.LOGGER.warn("Linked {} to {}", linkable, tardis);

                    // It's faster to remove the tardis from the nbt than make it do id -> string -> map -> string -> id
                    toLoad.remove("tardis");
                    linkable.link(tardis);
                }

                blockEntity.readNbt(toLoad);
            }

            if (fluidState == null)
                continue;

            if (blockState.getFluidState().isStill()) {
                list3.add(blockPos);
                continue;
            }

            if (!(blockState.getBlock() instanceof FluidFillable fillable))
                continue;

            fillable.tryFillWithFluid(world, blockPos, blockState, fluidState);

            if (fluidState.isStill())
                continue;

            list2.add(blockPos);
        }

        boolean shouldContinue = true;
        Direction[] directions = new Direction[] { Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

        while (shouldContinue && !list2.isEmpty()) {
            shouldContinue = false;
            Iterator<BlockPos> iterator = list2.iterator();

            while (iterator.hasNext()) {
                BlockPos blockPos = iterator.next();
                FluidState fluidState2 = world.getFluidState(blockPos);

                for (int o = 0; o < directions.length && !fluidState2.isStill(); ++o) {
                    BlockPos blockPos3 = blockPos.offset(directions[o]);
                    FluidState fluidState = world.getFluidState(blockPos3);

                    if (!fluidState.isStill() || list3.contains(blockPos3))
                        continue;

                    fluidState2 = fluidState;
                }

                BlockState blockState2 = world.getBlockState(blockPos);
                Block block = blockState2.getBlock();

                if (!fluidState2.isStill() || !(block instanceof FluidFillable fillable))
                    continue;

                fillable.tryFillWithFluid(world, blockPos, blockState2, fluidState2);
                shouldContinue = true;

                iterator.remove();
            }
        }

        if (i <= l) {
            if (!placement.shouldUpdateNeighbors()) {
                BitSetVoxelSet voxelSet = new BitSetVoxelSet(l - i + 1, m - j + 1, n - k + 1);
                for (Pair<BlockPos, NbtCompound> pair : nbts) {
                    BlockPos blockPos = pair.getFirst();

                    voxelSet.set(blockPos.getX() - i, blockPos.getY() - j, blockPos.getZ() - k);
                }

                StructureTemplate.updateCorner(world, flags, voxelSet, i, j, k);
            }

            for (Pair<BlockPos, NbtCompound> pair : nbts) {
                BlockPos blockPos = pair.getFirst();

                if (!placement.shouldUpdateNeighbors()) {
                    BlockState blockState2 = world.getBlockState(blockPos);
                    BlockState blockState3 = Block.postProcessState(blockState2, world, blockPos);

                    if (blockState2 != blockState3)
                        world.setBlockState(blockPos, blockState3, flags & ~Block.NOTIFY_NEIGHBORS | Block.FORCE_STATE);

                    world.updateNeighbors(blockPos, blockState3.getBlock());
                }

                if (pair.getSecond() == null)
                    continue;

                BlockEntity blockEntity = world.getBlockEntity(blockPos);

                if (blockEntity == null)
                    continue;

                blockEntity.markDirty();
            }
        }

        if (!placement.shouldIgnoreEntities()) {
            ((StructureTemplateInvoker) this.template).doSpawnEntities(world, pos, placement.getMirror(),
                    placement.getRotation(), placement.getPosition(), blockBox, placement.shouldInitializeMobs()
            );
        }

        return true;
    }

    public StructureTemplate parent() {
        return template;
    }
}
