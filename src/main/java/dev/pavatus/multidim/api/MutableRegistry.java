package dev.pavatus.multidim.api;

import com.mojang.serialization.Lifecycle;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public interface MutableRegistry<T> {
    boolean multidim$remove(T entry);
    boolean multidim$remove(Identifier key);

    void multidim$freeze();
    void multidim$unfreeze();
    boolean multidim$isFrozen();

    boolean contains(RegistryKey<T> key);
    RegistryEntry.Reference<T> add(RegistryKey<T> key, T entry, Lifecycle lifecycle);
}
