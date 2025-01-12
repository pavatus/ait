package loqor.ait.mixin.server;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;

import loqor.ait.api.Clearable;

@Mixin(ChunkSection.class)
public abstract class ChunkSectionMixin implements Clearable {

    @Mutable
    @Shadow @Final private PalettedContainer<BlockState> blockStateContainer;

    @Shadow private short nonEmptyBlockCount;

    @Shadow private short randomTickableBlockCount;

    @Shadow private short nonEmptyFluidCount;

    @Shadow public abstract boolean isEmpty();

    @Override
    public void ait$clear() {
        if (this.isEmpty())
            return;

        this.blockStateContainer = new PalettedContainer<>(Block.STATE_IDS, Blocks.AIR.getDefaultState(), PalettedContainer.PaletteProvider.BLOCK_STATE);
        this.nonEmptyBlockCount = 0;
        this.randomTickableBlockCount = 0;
        this.nonEmptyFluidCount = 0;
    }
}
