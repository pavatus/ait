package dev.codiak.blocks;

import dev.codiak.tiles.BOTIBlockEntity;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class BOTIBlock extends Block implements BlockEntityProvider {
    public BOTIBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BOTIBlockEntity(pos, state);
    }
}
