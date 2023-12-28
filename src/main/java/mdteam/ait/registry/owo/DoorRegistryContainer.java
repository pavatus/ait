package mdteam.ait.registry.owo;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import mdteam.ait.registry.DesktopRegistry;
import mdteam.ait.registry.DoorRegistry;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.variant.door.DoorSchema;
import net.minecraft.registry.Registry;

public interface DoorRegistryContainer extends AutoRegistryContainer<DoorSchema> {
    @Override
    default Registry<DoorSchema> getRegistry() {
        return DoorRegistry.REGISTRY;
    }

    @Override
    default Class<DoorSchema> getTargetFieldType() {
        return DoorSchema.class;
    }
}
