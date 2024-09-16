package loqor.ait.mixin.server.gravity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import loqor.ait.core.AITDimensions;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void ait$tick(CallbackInfo ci) {
        if (this.getWorld().getRegistryKey().equals(AITDimensions.MARS)) {
            Vec3d movement = this.getVelocity();
            this.setVelocity(movement.x, movement.y + 0.05f, movement.z);
        }
    }

}
