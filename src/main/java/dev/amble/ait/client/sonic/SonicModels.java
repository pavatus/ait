package dev.amble.ait.client.sonic;

import static dev.amble.ait.AITMod.LOGGER;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import dev.amble.ait.registry.impl.SonicRegistry;


// TODO: when finished, move to SonicRendering
public class SonicModels {



    public static void init() {
        ModelLoadingPlugin.register(context -> {
            Collection<Identifier> ids = SonicRegistry.getInstance().models();

            LOGGER.info("Loading {}", ids);
            context.addModels(ids.toArray(new Identifier[0]));
        });

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return ResourceReloadListenerKeys.MODELS;
            }

            @Override
            public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
                /*return CompletableFuture.supplyAsync(() -> manager.findResources("models/sonic",
                        identifier -> identifier.getPath().endsWith(".json")), prepareExecutor).thenCompose(map -> {
                    map.forEach((identifier, resource) -> {
                        LOGGER.info("found potential sonic: {}", identifier);
                    });

                    return null;
                });*/
                return new CompletableFuture<>();
            }
        });
    }
}
