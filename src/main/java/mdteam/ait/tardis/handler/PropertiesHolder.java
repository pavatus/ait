package mdteam.ait.tardis.handler;

import java.util.HashMap;
import java.util.UUID;

import static mdteam.ait.tardis.handler.PropertiesHandler.AUTO_LAND;

public class PropertiesHolder extends TardisHandler {
    private final HashMap<String, Object> data; // might replace the generic object with a property class that has impls eg Property.Boolean, etc
    public PropertiesHolder(UUID tardisId, HashMap<String, Object> data) {
        super(tardisId);
        this.data = data;
    }
    public PropertiesHolder(UUID tardis) {
        this(tardis, createDefaultProperties());
    }

    public HashMap<String, Object> getData() {
        return this.data;
    }

    public static HashMap<String, Object> createDefaultProperties() {
        HashMap<String, Object> map = new HashMap<>();

        map.put(AUTO_LAND, false);
        map.put(PropertiesHandler.SEARCH_DOWN, true);

        return map;
    }
}
