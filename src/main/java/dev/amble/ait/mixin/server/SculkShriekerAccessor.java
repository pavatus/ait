package dev.amble.ait.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.block.entity.SculkShriekerBlockEntity;

@Mixin(SculkShriekerBlockEntity.class)
public interface SculkShriekerAccessor {

    @Accessor
    int getWarningLevel();
}
