package dev.pavatus.lib.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.AbstractBlock;

@Mixin(AbstractBlock.class)
public interface AbstractBlockAccessor {

    @Accessor
    AbstractBlock.Settings getSettings();
}
