package mdteam.ait.tardis.handler;

public class PropertiesHolder { // todo move more things over to properties
    public static final String AUTO_LAND = "auto_land";

    public static void setAutoLand(PropertiesHandler handler, boolean val) {
        if (handler.getData().containsKey(AUTO_LAND)) {
            handler.getData().replace(AUTO_LAND, val);
            return;
        }

        handler.getData().put(AUTO_LAND, val);
    }
    public static boolean willAutoLand(PropertiesHandler handler) {
        if (!handler.getData().containsKey(AUTO_LAND)) return false; // this shouldnt happen due to default properties, but if necessary could set the value here

        return (boolean) handler.getData().get(AUTO_LAND); // tons of risky casts for this unless i do it the other way i mentioned
    }
}
