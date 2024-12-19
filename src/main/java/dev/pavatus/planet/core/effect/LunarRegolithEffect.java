package dev.pavatus.planet.core.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class LunarRegolithEffect extends StatusEffect {

    public LunarRegolithEffect() {
        super(StatusEffectCategory.HARMFUL, 0xF5F5F5); // Customize color
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, amplifier, false, false));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 100, amplifier, false, false));

            int lunarDuration = entity.getStatusEffect(this).getDuration();

            int delayBeforeEffect = lunarDuration - 1000;

            if (delayBeforeEffect <= 0) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 6, false, false));

            }
        }
    }
}
