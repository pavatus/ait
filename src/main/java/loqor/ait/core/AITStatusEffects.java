package loqor.ait.core;

import dev.pavatus.planet.core.effect.LunarRegolithEffect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.effects.ZeitonHighEffect;

public class AITStatusEffects {
    public static final RegistryEntry<StatusEffect> ZEITON_HIGH = registerStatusEffect("zeiton_high",
            new ZeitonHighEffect());
    public static final RegistryEntry<StatusEffect> LUNAR_REGOLITH = registerStatusEffect("lunar_regolith",
            new LunarRegolithEffect());


    private static RegistryEntry<StatusEffect> registerStatusEffect(String name, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(AITMod.MOD_ID, name), effect);
    }

    public static void init() {
        AITMod.LOGGER.info("Registering AIT's Effects");
    }
}
