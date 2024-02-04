package mdteam.ait.registry.owo;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import mdteam.ait.registry.CategoryRegistry;
import mdteam.ait.tardis.exterior.ExteriorCategory;
import net.minecraft.registry.Registry;

public interface ExteriorRegistryContainer extends AutoRegistryContainer<ExteriorCategory> {
    @Override
    default Registry<ExteriorCategory> getRegistry() {
        return CategoryRegistry.REGISTRY;
    }

    @Override
    default Class<ExteriorCategory> getTargetFieldType() {
        return ExteriorCategory.class;
    }
}
