package dev.pavatus.planet.mixin.gravity;


import dev.pavatus.planet.core.planet.Planet;
import dev.pavatus.planet.core.planet.PlanetRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import loqor.ait.core.AITDimensions;
import loqor.ait.core.AITStatusEffects;
import loqor.ait.core.AITTags;
import loqor.ait.core.world.TardisServerWorld;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void ait$tickMovement(CallbackInfo ci) {
        Planet planet = PlanetRegistry.getInstance().get(this.getWorld());

        if (planet == null)
            return;

        if (!planet.hasGravityModifier())
            return;

        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.isSwimming() || entity.hasNoGravity() || entity.isFallFlying() || entity.isSpectator()) return;
        if (entity instanceof PlayerEntity player) {
            if (player.getAbilities().flying) return;
        }
        if (entity.getType() == EntityType.BOAT || entity.getType() == EntityType.CHEST_BOAT) { return;}

        Vec3d movement = this.getVelocity();
        this.setVelocity(movement.x, movement.y + planet.gravity(), movement.z);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void ait$tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof PlayerEntity player
                && (player.isCreative() || player.isSpectator()))
             return;

        Planet planet = PlanetRegistry.getInstance().get(this.getWorld());

        if (planet == null)
            return;

        if (planet.isFreezing() &&  !Planet.hasFullSuit(entity) && !entity.hasStatusEffect(AITStatusEffects.OXYGENATED)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity.getFrozenTicks() < entity.getMinFreezeDamageTicks()) {
                entity.setFrozenTicks(entity.getMinFreezeDamageTicks() + 20);
            }
            return;
        }

        if (entity.getWorld() instanceof TardisServerWorld tardisWorld) {
            ItemStack stack = entity.getEquippedStack(EquipmentSlot.HEAD);

            if (!tardisWorld.getTardis().subsystems().lifeSupport().isEnabled() &&
                    (!stack.isIn(AITTags.Items.FULL_RESPIRATORS) || !stack.isIn(AITTags.Items.HALF_RESPIRATORS))) {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 1,
                        200, false, false));
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                        200, 1, false, false));
            }
        }

        if (!planet.hasOxygen() && !Planet.hasOxygenInTank(entity) && !entity.hasStatusEffect(AITStatusEffects.OXYGENATED)) {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA,
                    200, 1, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 1,
                    200, false, false));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,
                    200, 1, false, false));
        }

        if (entity.getWorld().isClient()) return;

        // TODO this might be like, crazy laggy but oh well
        hitDimensionThreshold(entity, 600, AITDimensions.MOON, AITDimensions.SPACE);
        hitDimensionThreshold(entity, 600, 256, World.OVERWORLD, AITDimensions.SPACE);
        hitDimensionThreshold(entity, 500, AITDimensions.MARS, AITDimensions.SPACE);
    }

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void ait$handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        Planet planet = PlanetRegistry.getInstance().get(this.getWorld());
        if (planet == null) return;
        if (planet.hasNoFallDamage()) {
            cir.setReturnValue(false);
        }
    }

    @Unique private static void hitDimensionThreshold(Entity entity, int heightForTeleport, RegistryKey<World> dimFrom, RegistryKey<World> dimTo) {
        hitDimensionThreshold(entity, heightForTeleport, heightForTeleport, dimFrom, dimTo);
    }

    @Unique private static void hitDimensionThreshold(Entity entity, int heightForTeleportFrom, int heightForTeleportTo, RegistryKey<World> dimFrom, RegistryKey<World> dimTo) {
        ServerWorld entityWorld = (ServerWorld) entity.getWorld();
        MinecraftServer server = entityWorld.getServer();
        ServerWorld destinationWorld = server.getWorld(dimTo);
        /*if (entity.getWorld().getRegistryKey() == dimFrom) {
            if (entity.getY() >= heightForTeleportFrom) {
                if (entity instanceof ServerPlayerEntity player) {
                    Vec3d vec = new Vec3d(entity.getX(), heightForTeleportTo, entity.getZ());
                    WorldUtil.teleportToWorld(player, destinationWorld, vec,
                            player.getYaw(),
                            player.getPitch());
                    player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
                } else {
                    entity.teleport(destinationWorld, entity.getX(), heightForTeleportTo, entity.getZ(),
                            Set.of(),
                            entity.getYaw(),
                            entity.getPitch());
                }
            }
        }*/
        if (entity.getBlockY() >= heightForTeleportFrom && entityWorld.getRegistryKey() == dimFrom) {
            System.out.println(entity.getBlockPos() + " | teleporting to | " + destinationWorld);
            entity.moveToWorld(destinationWorld);
        }
    }
}
