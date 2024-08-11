package loqor.ait.core.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.blockentities.LandingPadBlockEntity;

public class LandingPadBlock extends Block implements BlockEntityProvider {

    public LandingPadBlock(FabricBlockSettings settings) {
        super(settings);
    }

    @Nullable @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new LandingPadBlockEntity(pos, state);
    }
}
