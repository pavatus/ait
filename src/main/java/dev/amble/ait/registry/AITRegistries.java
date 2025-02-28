package dev.amble.ait.registry;

import dev.amble.ait.AITMod;
import dev.amble.ait.core.tardis.control.Control;
import dev.amble.ait.data.datapack.DatapackSonic;
import dev.amble.ait.data.schema.sonic.SonicSchema;
import dev.amble.ait.registry.impl.ControlRegistry;
import dev.amble.lib.registry.AmbleRegistry;
import dev.amble.lib.registry.DynamicAmbleRegistry;
import dev.amble.lib.registry.SimpleAmbleRegistry;
import dev.amble.lib.registry.builder.AmbleRegistryBuilder;
import dev.amble.lib.registry.entrypoint.BootstrapEntrypoint;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class AITRegistries implements BootstrapEntrypoint {

    public static Registry<Control> CONTROL;

    public static Registry<SonicSchema> SONIC;
    public static SonicSchema SONIC_DEFAULT;

    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(AITMod.id(id));
    }

    @Override
    public void onBootstrap() {
        CONTROL = AmbleRegistryBuilder.simple(
                AITRegistries.<Control>of("control")
        ).intrusive().defaults(Control::registerAndGetDefault).build().get();

        DynamicAmbleRegistry<SonicSchema> sonic = AmbleRegistryBuilder.dynamic(
                AITRegistries.of("sonic"),
                DatapackSonic.CODEC
        ).build();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            SONIC = sonic.get(server.getOverworld());
            SONIC_DEFAULT = SONIC.get(AITMod.id("prime"));
        });
    }
}
