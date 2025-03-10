package dev.amble.ait.core;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

import dev.amble.ait.AITMod;

public class AITDamageTypes {
    public static final RegistryKey<DamageType> TARDIS_SQUASH_DAMAGE_TYPE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE,
            AITMod.id("tardis_squash_damage_type"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }
}
