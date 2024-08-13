package loqor.ait.client.util;

import java.util.HashMap;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class ResourceChecker {
    public final HashMap<Identifier, Boolean> checked;

    public ResourceChecker() {
        this.checked = new HashMap<>();
    }

    public boolean exists(Identifier id) {
        return checked.computeIfAbsent(
                id,
                id2 -> MinecraftClient.getInstance().getResourceManager().getResource(id).isPresent()
        );
    }

    private static ResourceChecker instance;

    public static ResourceChecker getInstance() {
        if (instance == null) {
            instance = new ResourceChecker();
        }

        return instance;
    }
}
