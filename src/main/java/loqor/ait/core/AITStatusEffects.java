package loqor.ait.core;

import dev.pavatus.planet.core.effect.LunarRegolithEffect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.effects.OxygenatedEffect;
import loqor.ait.core.effects.ZeitonHighEffect;

public class AITStatusEffects {
    public static StatusEffect ZEITON_HIGH = register(new ZeitonHighEffect(), "zeiton_high");

    public static StatusEffect OXYGENATED = register(new OxygenatedEffect(), "oxygenated");
    public static StatusEffect LUNAR_REGOLITH = register(new LunarRegolithEffect(), "lunar_regolith");

    public static void init() {

    }
    private static StatusEffect register(StatusEffect effect, String name) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(AITMod.MOD_ID, name), effect);
    }
}
