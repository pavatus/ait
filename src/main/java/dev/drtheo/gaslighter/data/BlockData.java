package dev.drtheo.gaslighter.data;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public record BlockData(BlockPos pos, BlockState state) { }
