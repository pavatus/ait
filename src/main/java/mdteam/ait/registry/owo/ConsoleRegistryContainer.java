package mdteam.ait.registry.owo;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import mdteam.ait.registry.ConsoleRegistry;
import mdteam.ait.tardis.console.ConsoleSchema;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public interface ConsoleRegistryContainer extends AutoRegistryContainer<ConsoleSchema> {
    @Override
    default Registry<ConsoleSchema> getRegistry() {
        return ConsoleRegistry.REGISTRY;
    }

    @Override
    default Class<ConsoleSchema> getTargetFieldType() {
        return ConsoleSchema.class;
    }
}
