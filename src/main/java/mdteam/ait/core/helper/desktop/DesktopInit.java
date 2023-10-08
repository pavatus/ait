package mdteam.ait.core.helper.desktop;

import mdteam.ait.core.helper.desktop.impl.WarDesktop;

import java.util.HashMap;
import java.util.Map;

public class DesktopInit {
    private static final Map<String, DesktopSchema> interiors = new HashMap<>();

    /**
     * Desktops are registered here:
     */
    public static void init() {
        DesktopInit.register(new WarDesktop());
    }
    public static void register(DesktopSchema interior) {interiors.put(interior.getID(),interior);}
    public static DesktopSchema get(String id) {
        return interiors.get(id);}
}
