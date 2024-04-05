package loqor.ait.registry.owo;

import io.wispforest.owo.registration.reflect.AutoRegistryContainer;
import loqor.ait.registry.ConsoleRegistry;
import loqor.ait.tardis.console.type.ConsoleTypeSchema;
import net.minecraft.registry.Registry;

public interface ConsoleRegistryContainer extends AutoRegistryContainer<ConsoleTypeSchema> {
	@Override
	default Registry<ConsoleTypeSchema> getRegistry() {
		return ConsoleRegistry.REGISTRY;
	}

	@Override
	default Class<ConsoleTypeSchema> getTargetFieldType() {
		return ConsoleTypeSchema.class;
	}
}
