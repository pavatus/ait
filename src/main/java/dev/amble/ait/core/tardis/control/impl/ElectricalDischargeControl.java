package dev.amble.ait.core.tardis.control.impl;

import dev.drtheo.scheduler.api.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import dev.amble.ait.core.AITSounds;
import dev.amble.ait.core.engine.SubSystem;
import dev.amble.ait.core.tardis.Tardis;
import dev.amble.ait.core.tardis.control.Control;
import dev.amble.ait.core.tardis.control.impl.pos.IncrementManager;

public class ElectricalDischargeControl extends Control {

    private static final int BASE_ARTRON_COST = 2750;
    private static final int BASE_EFFECT_RADIUS = 2;
    private static final int BASE_DURATION = 80;
    private static final int STATUS_EFFECT_DURATION = 400;
    private static final int ARTRON_COST_PER_INCREMENT = 50;

    public ElectricalDischargeControl() {
        super("electrical_discharge");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console, boolean leftClick) {
        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        int incrementLevel = IncrementManager.increment(tardis);
        int effectRadius = BASE_EFFECT_RADIUS + (incrementLevel + 5);
        int artronCost = BASE_ARTRON_COST + (incrementLevel + ARTRON_COST_PER_INCREMENT);

        if (tardis.fuel().getCurrentFuel() < artronCost) {
            player.sendMessage(Text.literal("§cERROR: Insufficient Artron Energy! Required: " + artronCost + " AU"), true);
            return false;
        }

        tardis.fuel().removeFuel(artronCost);

        BlockPos exteriorPos = tardis.travel().position().getPos();
        ServerWorld exteriorWorld = tardis.travel().position().getWorld();

        world.playSound(null, console, AITSounds.DING, SoundCategory.BLOCKS, 1.0F, 1.0F);

        Scheduler.get().runTaskLater(() -> {
            applyElectricalEffects(exteriorWorld, exteriorPos, effectRadius);
            exteriorWorld.playSound(null, exteriorPos, AITSounds.CLOISTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
            spawnElectricParticles(exteriorWorld, exteriorPos, effectRadius);
        }, TimeUnit.TICKS, 40);

        Scheduler.get().runTaskLater(() -> world.playSound(null, console, AITSounds.DING, SoundCategory.BLOCKS, 1.0F, 1.0F), TimeUnit.TICKS, BASE_DURATION);

        return true;
    }

    private void applyElectricalEffects(ServerWorld world, BlockPos pos, int radius) {
        Box effectBox = new Box(pos).expand(radius);

        world.getEntitiesByClass(LivingEntity.class, effectBox, entity -> true).forEach(entity -> {
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, STATUS_EFFECT_DURATION, 1));
            entity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, STATUS_EFFECT_DURATION / 4, 1));
        });
    }

    private void spawnElectricParticles(ServerWorld world, BlockPos pos, int radius) {
        for (BlockPos targetPos : BlockPos.iterate(pos.add(-radius, -1, -radius), pos.add(radius, 2, radius))) {
            world.spawnParticles(ParticleTypes.ELECTRIC_SPARK, targetPos.getX() + 0.5, targetPos.getY() + 1.0, targetPos.getZ() + 0.5, 5, 0, 0.05, 0, 0.1);
        }
    }

    @Override
    protected SubSystem.IdLike requiredSubSystem() {
        return SubSystem.Id.DESPERATION;
    }

    @Override
    public long getDelayLength() {
        return 5000;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.DING;
    }
}
