package mdteam.ait.registry.owo;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import mdteam.ait.registry.ConsoleRegistry;
import mdteam.ait.registry.ConsoleVariantRegistry;
import mdteam.ait.tardis.console.ConsoleSchema;
import mdteam.ait.tardis.variant.console.ConsoleVariantSchema;
import net.minecraft.registry.Registry;

public interface ConsoleVariantRegistryContainer extends AutoRegistryContainer<ConsoleVariantSchema> {
    @Override
    default Registry<ConsoleVariantSchema> getRegistry() {
        return ConsoleVariantRegistry.REGISTRY;
    }

    @Override
    default Class<ConsoleVariantSchema> getTargetFieldType() {
        return ConsoleVariantSchema.class;
    }
}
