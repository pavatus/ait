package mdteam.ait.registry.owo;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.variant.door.DoorSchema;
import net.minecraft.registry.Registry;

public interface ExteriorRegistryContainer extends AutoRegistryContainer<ExteriorSchema> {
    @Override
    default Registry<ExteriorSchema> getRegistry() {
        return ExteriorRegistry.REGISTRY;
    }

    @Override
    default Class<ExteriorSchema> getTargetFieldType() {
        return ExteriorSchema.class;
    }
}
