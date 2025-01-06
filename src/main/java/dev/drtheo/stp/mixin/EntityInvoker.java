package dev.drtheo.stp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public interface EntityInvoker {

    @Invoker("unsetRemoved")
    void stp$unsetRemoved();
}
