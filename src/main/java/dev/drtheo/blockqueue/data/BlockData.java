package dev.drtheo.blockqueue.data;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public record BlockData(BlockState state, BlockPos pos, int flags) { }