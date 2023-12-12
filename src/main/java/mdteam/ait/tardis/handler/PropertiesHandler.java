package mdteam.ait.tardis.handler;

import mdteam.ait.AITMod;

import java.util.HashMap;

public class PropertiesHandler { // todo move more things over to properties
    public static final String AUTO_LAND = "auto_land";
    public static final String SEARCH_DOWN = "search_down";

    // Should these methods be in the holder instead?

    /**
     * used for setting things to a boolean
     */
    public static void set(PropertiesHolder holder, String key, boolean val) {
        if (holder.getData().containsKey(key)) {
            holder.getData().replace(key, val);
            return;
        }

        holder.getData().put(key, val);
    }

    public static boolean get(PropertiesHolder holder, String key) {
        if (!holder.getData().containsKey(key)) return false;

        if (!(holder.getData().get(key) instanceof Boolean)) {
            AITMod.LOGGER.warn("Tried to grab key " + key + " which was not a boolean!");
            return false;
        }

        return (boolean) holder.getData().get(key);
    }

    public static void setAutoLand(PropertiesHolder handler, boolean val) {
        set(handler, AUTO_LAND, val);
    }
    public static boolean willAutoLand(PropertiesHolder holder) {
        return get(holder, AUTO_LAND);
    }

    public static HashMap<String, Object> createDefaultProperties() {
        HashMap<String, Object> map = new HashMap<>();

        map.put(AUTO_LAND, false);
        map.put(SEARCH_DOWN, false);

        return map;
    }
}
