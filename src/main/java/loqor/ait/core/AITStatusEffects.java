package loqor.ait.core;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.effects.ZeitonHighEffect;

public class AITStatusEffects {
    public static StatusEffect ZEITON_HIGH = register(new ZeitonHighEffect(), "zeiton_high");

    public static void init() {

    }
    private static StatusEffect register(StatusEffect effect, String name) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(AITMod.MOD_ID, name), effect);
    }
}
