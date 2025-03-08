package dev.amble.ait.core.tardis.control.impl;

import dev.drtheo.scheduler.api.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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

    //I wanted to make it so the higher the increment the higher the AU cost but it dident like that so
    //Also i wanted to make it spawn particles but that stopped working for some reason and caused massive lag :Clueless:
    private static final int BASE_ARTRON_COST = 2750;
    private static final int BASE_EFFECT_RADIUS = 2;
    private static final int BASE_DURATION = 80;
    private static final int STATUS_EFFECT_DURATION = 400;

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
        int effectRadius = BASE_EFFECT_RADIUS + (incrementLevel * 5);

        if (tardis.fuel().getCurrentFuel() < BASE_ARTRON_COST) {
            player.sendMessage(Text.literal("§cERROR: Insufficient Artron Energy!"), true);
            return false;
        }

        tardis.fuel().removeFuel(BASE_ARTRON_COST);

        BlockPos exteriorPos = tardis.travel().position().getPos();
        ServerWorld exteriorWorld = tardis.travel().position().getWorld();

        world.playSound(null, console, AITSounds.DING, SoundCategory.BLOCKS, 1.0F, 1.0F);

        Scheduler.get().runTaskLater(() -> {
            applyElectricalEffects(exteriorWorld, exteriorPos, effectRadius);
            exteriorWorld.playSound(null, exteriorPos, AITSounds.CLOISTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
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
