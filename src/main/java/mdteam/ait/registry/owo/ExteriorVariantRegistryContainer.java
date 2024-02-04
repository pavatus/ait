package mdteam.ait.registry.owo;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import net.minecraft.registry.Registry;

public interface ExteriorVariantRegistryContainer extends AutoRegistryContainer<ExteriorVariantSchema> {
    @Override
    default Registry<ExteriorVariantSchema> getRegistry() {
        return ExteriorVariantRegistry.REGISTRY;
    }

    @Override
    default Class<ExteriorVariantSchema> getTargetFieldType() {
        return ExteriorVariantSchema.class;
    }
}
