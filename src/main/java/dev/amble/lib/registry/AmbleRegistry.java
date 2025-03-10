package dev.amble.lib.registry;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

abstract class AmbleRegistry<T> {

    private final Identifier id;
    private final RegistryKey<Registry<T>> key;

    public AmbleRegistry(Identifier id) {
        this.id = id;
        this.key = RegistryKey.ofRegistry(id);
    }

    public Identifier getId() {
        return id;
    }

    public RegistryKey<Registry<T>> getKey() {
        return key;
    }
}
