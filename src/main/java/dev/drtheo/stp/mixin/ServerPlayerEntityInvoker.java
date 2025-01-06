package dev.drtheo.stp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.TeleportTarget;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityInvoker {

    @Invoker("worldChanged")
    void stp$worldChanged(ServerWorld world);

    @Accessor
    void setSyncedExperience(int xp);

    @Accessor
    void setSyncedHealth(float health);

    @Accessor
    void setSyncedFoodLevel(int foodLevel);

    @Accessor
    void setInTeleportationState(boolean inTeleportationState);

    @Invoker("getTeleportTarget")
    TeleportTarget stp$getTeleportTarget(ServerWorld world);
}
