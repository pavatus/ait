package mdteam.ait.core;

import mdteam.ait.core.desktops.WarDesktop;
import net.minecraft.util.Identifier;
import the.mdteam.ait.TardisDesktopSchema;

import java.util.HashMap;
import java.util.Map;

public class AITDesktops {

    private static final Map<Identifier, TardisDesktopSchema> interiors = new HashMap<>();

    /**
     * Desktops are registered here:
     */
    public static void init() {
        AITDesktops.register(new WarDesktop());
    }

    public static void register(TardisDesktopSchema interior) {
        interiors.put(interior.id(), interior);
    }
    public static TardisDesktopSchema get(Identifier id) {
        return interiors.get(id);
    }
}
