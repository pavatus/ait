package mdteam.ait.core;

import mdteam.ait.api.tardis.IDesktopSchema;
import mdteam.ait.core.desktops.WarDesktop;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class AITDesktops {

    private static final Map<Identifier, IDesktopSchema> interiors = new HashMap<>();

    /**
     * Desktops are registered here:
     */
    public static void init() {
        AITDesktops.register(new WarDesktop());
    }

    public static void register(IDesktopSchema interior) {
        interiors.put(interior.id(), interior);
    }
    public static IDesktopSchema get(Identifier id) {
        return interiors.get(id);
    }
}
