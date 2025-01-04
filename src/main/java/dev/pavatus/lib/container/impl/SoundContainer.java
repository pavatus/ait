package dev.pavatus.lib.container.impl;

import dev.pavatus.lib.container.RegistryContainer;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public interface SoundContainer extends RegistryContainer<SoundEvent> {

    @Override
    default Registry<SoundEvent> getRegistry() {
        return Registries.SOUND_EVENT;
    }

    @Override
    default Class<SoundEvent> getTargetClass() {
        return SoundEvent.class;
    }
}
