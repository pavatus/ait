package mdteam.ait.tardis.handler;

public class PropertiesHolder { // todo move more things over to properties
    public static final String AUTO_LAND = "auto_land";

    public static void setAutoLand(PropertiesHandler handler, boolean val) {
        if (handler.getData().contains(AUTO_LAND)) handler.getData().remove(AUTO_LAND); // fixme dont know if this is necessary

        handler.getData().putBoolean(AUTO_LAND, val);
    }
    public static boolean willAutoLand(PropertiesHandler handler) {
        if (handler.getData().contains(AUTO_LAND)) return false; // this shouldnt happen due to default properties, but if necessary could set the value here

        return handler.getData().getBoolean(AUTO_LAND);
    }
}
