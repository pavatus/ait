package loqor.ait.mixin.server.gravity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import loqor.ait.core.AITDimensions;
import loqor.ait.core.item.SpacesuitItem;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void ait$tickMovement(CallbackInfo ci) {
        if (this.getWorld().getRegistryKey().equals(AITDimensions.MARS)) {
            Vec3d movement = this.getVelocity();
            this.setVelocity(movement.x, movement.y + 0.05f, movement.z);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void ait$tick(CallbackInfo ci) {

        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player) {
            if (player.isCreative()) return;
        }

        if (this.getWorld().getRegistryKey().equals(AITDimensions.MARS) && !this.hasAllSpaceSuitBits(entity)) {

            if (entity.getFrozenTicks() < entity.getMinFreezeDamageTicks()) {
                entity.setFrozenTicks(entity.getMinFreezeDamageTicks());
            }

            entity.setFrozenTicks(entity.getFrozenTicks() + 2);

            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,
                    200, 1, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 1,
                    200, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                    200, 1, false, false));
        }
    }

    @Unique private boolean hasAllSpaceSuitBits(LivingEntity entity) {
        return entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof SpacesuitItem
                && entity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof SpacesuitItem
                && entity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof SpacesuitItem
                && entity.getEquippedStack(EquipmentSlot.FEET).getItem() instanceof SpacesuitItem;
    }

}
