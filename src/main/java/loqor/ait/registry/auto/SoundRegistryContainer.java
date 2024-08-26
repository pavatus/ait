package loqor.ait.registry.auto;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public interface SoundRegistryContainer extends AutoRegistryContainer<SoundEvent> {

    @Override
    default Registry<SoundEvent> getRegistry() {
        return Registries.SOUND_EVENT;
    }

    @Override
    default Class<SoundEvent> getTargetFieldType() {
        return SoundEvent.class;
    }
}
