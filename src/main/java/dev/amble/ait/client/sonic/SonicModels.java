package dev.amble.ait.client.sonic;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;


// TODO: when finished, move to SonicRendering
public class SonicModels {

    public static void init() {
        ModelLoadingPlugin.register(context -> {
            System.out.println("Tada");
            context.modifyModelOnLoad().register((model, context1) -> {
                if (context1.id().getNamespace().equals("ait") && context1.id().getPath().startsWith("models/item/sonic"))
                    System.out.println(context1.id());

                return model;
            });
        });
    }
}
