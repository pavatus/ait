package mdteam.ait.registry.owo;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import mdteam.ait.registry.ConsoleRegistry;
import mdteam.ait.registry.DesktopRegistry;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.console.ConsoleSchema;
import net.minecraft.registry.Registry;

import java.awt.*;

public interface DesktopRegistryContainer extends AutoRegistryContainer<TardisDesktopSchema> {
    @Override
    default Registry<TardisDesktopSchema> getRegistry() {
        return DesktopRegistry.REGISTRY;
    }

    @Override
    default Class<TardisDesktopSchema> getTargetFieldType() {
        return TardisDesktopSchema.class;
    }
}
