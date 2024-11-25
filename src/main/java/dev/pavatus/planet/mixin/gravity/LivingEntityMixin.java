package dev.pavatus.planet.mixin.gravity;

import dev.pavatus.planet.PlanetModule;
import dev.pavatus.planet.core.planet.Planet;
import dev.pavatus.planet.core.planet.PlanetRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import loqor.ait.core.AITTags;
import loqor.ait.core.tardis.dim.TardisDimension;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void ait$tickMovement(CallbackInfo ci) {
        if (!(PlanetModule.isLoaded())) return;

        Planet planet = PlanetRegistry.getInstance().get(this.getWorld());
        if (planet == null) return;
        if (!planet.hasGravityModifier()) return;

        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.isSwimming() || entity.hasNoGravity() || entity.isFallFlying() || entity.isSpectator()) return;
        if (entity instanceof PlayerEntity player) {
            if (player.getAbilities().flying) return;
        }


        Vec3d movement = this.getVelocity();
        this.setVelocity(movement.x, movement.y + planet.gravity(), movement.z);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void ait$tick(CallbackInfo ci) {

        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player) {
            if (player.isCreative()) return;
        }

        Planet planet = PlanetRegistry.getInstance().get(this.getWorld());
        if (planet == null) return;

        if (planet.isFreezing() &&  !Planet.hasFullSuit(entity)) {
            // freeze effect on cold planets
            if (entity.getFrozenTicks() < entity.getMinFreezeDamageTicks()) {
                entity.setFrozenTicks(entity.getMinFreezeDamageTicks() + 20);
            }
            return;
        }

        if (TardisDimension.isTardisDimension(entity.getWorld())) {
            ItemStack stack = entity.getEquippedStack(EquipmentSlot.HEAD);
            if (!TardisDimension.get(entity.getWorld()).get().subsystems().lifeSupport().isEnabled() &&
                    (!stack.isIn(AITTags.Items.FULL_RESPIRATORS) || !stack.isIn(AITTags.Items.HALF_RESPIRATORS))) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 1,
                        200, false, false));
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                        200, 1, false, false));
            }
        }

        if ((!planet.hasOxygen() && !Planet.hasOxygenInTank(entity))) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,
                    200, 1, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 1,
                    200, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                    200, 1, false, false));
        }
    }

}
