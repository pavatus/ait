package mdteam.ait.tardis.data.properties;

import com.google.gson.internal.LinkedTreeMap;
import mdteam.ait.AITMod;
import mdteam.ait.registry.DesktopRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.data.FuelData;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class PropertiesHandler { // todo move things out of properties
    // this is getting bloated
    public static final String AUTO_LAND = "auto_land";
    public static final String HUM_ENABLED = "hum_enabled";
    public static final String ALARM_ENABLED = "alarm_enabled";
    public static final String FIND_GROUND = "find_ground"; // whether the destination checks will try to find the ground or not
    public static final String PREVIOUSLY_LOCKED = "last_locked";
    public static final String SIEGE_HELD = "siege_held";
    public static final String SIEGE_TIME = "siege_ticks";
    public static final String HANDBRAKE = "handbrake";
    public static final String HAIL_MARY = "hail_mary";
    public static final String IS_FALLING = "is_falling";
    public static final String ANTIGRAVS_ENABLED = "antigravs_enabled";
    public static final String HADS_ENABLED = "hads_enabled";
    public static final String IS_IN_ACTIVE_DANGER = "is_in_active_danger";
    public static final String HAS_POWER = "power";
    public static final String SIEGE_MODE = "siege_mode";
    public static final String IS_IN_REAL_FLIGHT = "is_in_real_flight";
    public static final String DEMAT_TICKS = "demat_ticks";
    public static final String MAT_TICKS = "mat_ticks";
    public static final String SPEED = "speed";
    public static final String IS_CLOAKED = "cloaked";

    // Should these methods be in the holder instead?
    
    public static void set(Tardis tardis, String key, Object val, boolean performSync) {
        if (!hasChanged(tardis.getHandlers().getProperties(), key, val)) return;

        set(tardis.getHandlers().getProperties(), key, val);
        if (performSync)
            sync(tardis.getHandlers().getProperties(), key, tardis.getUuid());
    }
    public static void set(Tardis tardis, String key, Object val) {
        set(tardis, key, val, true);
    }
    private static boolean hasChanged(PropertiesHolder holder, String key, Object newVal) {
        if (!holder.getData().containsKey(key)) return true;
        return !holder.getData().get(key).equals(newVal);
    }

    public static void set(PropertiesHolder holder, String key, Object val) {
        if (holder.getData().containsKey(key)) {
            holder.getData().replace(key, val);
            return;
        }

        holder.getData().put(key, val);
    }

    public static Object get(PropertiesHolder holder, String key) {
        if (!holder.getData().containsKey(key)) return null;

        return holder.getData().get(key);
    }

    public static TardisDesktopSchema getDesktop(PropertiesHolder holder, String key) {
        if (!holder.getData().containsKey(key)) {
            AITMod.LOGGER.error(key + " did not have a schema! Resetting to default..");
            setDesktop(holder,key, DesktopRegistry.get(new Identifier(AITMod.MOD_ID, "cave")));
        }

        return DesktopRegistry.get(getIdentifier(holder,key));
    }
    public static void setDesktop(PropertiesHolder holder, String key, TardisDesktopSchema val) {
        set(holder, key, val.id());
    }

    public static Identifier getIdentifier(PropertiesHolder holder, String key) {
        if (!holder.getData().containsKey(key)) {
            AITMod.LOGGER.error(key + " did not have an identifier! Have fun w that null lol");
            return null;
        }

        // because gson saves it weird
        if (holder.getData().get(key) instanceof LinkedTreeMap map) {
            if (map.get("namespace") == null || map.get("path") == null) {
                AITMod.LOGGER.error("namespace/path was null! Panic - I'm giving back the default desktop id, lets hope this doesnt cause a crash..");
                return DesktopRegistry.get(0).id();
            }

            return Identifier.of((String) map.get("namespace"), (String) map.get("path"));
        }

        return (Identifier) holder.getData().get(key);
    }
    public static boolean getBool(PropertiesHolder holder, String key) {
        if (!holder.getData().containsKey(key)) return false;

        if (!(holder.getData().get(key) instanceof Boolean)) {
            AITMod.LOGGER.warn("Tried to grab key " + key + " which was not a boolean!");
            return false;
        }

        return (boolean) holder.getData().get(key);
    }

    public static String getString(PropertiesHolder holder, String key) {
        if (!holder.getData().containsKey(key)) return "";

        if (!(holder.getData().get(key) instanceof String)) {
            AITMod.LOGGER.warn("Tried to grab key " + key + " which was not a String!");
            return "";
        }

        return (String) holder.getData().get(key);
    }

    public static int getInt(PropertiesHolder holder, String key) {
        if (!holder.getData().containsKey(key)) return 0;

        if (!(holder.getData().get(key) instanceof Integer) && !(holder.getData().get(key) instanceof Double) && !(holder.getData().get(key) instanceof Float)) {
            AITMod.LOGGER.error("Tried to grab key " + key + " which was not an Integer!");
            AITMod.LOGGER.warn("Value was instead: " + holder.getData().get(key));
            return 0;
        }

        if (holder.getData().get(key) instanceof Double d) {
            return d.intValue();
        }
        if (holder.getData().get(key) instanceof Float d) {
            return d.intValue();
        }

        return (int) holder.getData().get(key);
    }

    public static void setSchemaUnlocked(PropertiesHolder holder, TardisDesktopSchema schema, boolean val) {
        set(holder, schema.id().getPath() + "_unlocked", val);
    }
    public static boolean isSchemaUnlocked(PropertiesHolder holder, TardisDesktopSchema schema) {
        return getBool(holder, schema.id().getPath() + "_unlocked");
    }

    @Deprecated
    public static void setAutoPilot(PropertiesHolder handler, boolean val) {
        set(handler, AUTO_LAND, val);
    }

    @Deprecated
    public static boolean willAutoPilot(PropertiesHolder holder) {
        return getBool(holder, AUTO_LAND);
    }
    private static void unlockAllFreebies(HashMap<String, Object> map) {
        for (Iterator<TardisDesktopSchema> it = DesktopRegistry.iterator(); it.hasNext(); ) {
            TardisDesktopSchema schema = it.next();

            map.put(schema.id().getPath() + "_unlocked", schema.freebie());
        }
    }

    // todo kill me bad bad bad i never want to write code or work on ait again jesus christ how did we let the code get this bad i genuienyl cant even write good code anymore its not worth the effort i just want to finish this god damn networking problem
    // at least it's better than 1.16.5 :skull: and god yes just release this man already from the hell he's in bro needs a break :sob:
    // lord baby jesus give us strength, this is some loqor ass code im boutta write. if it fixes network i do not care.
    // SWITCH CASE GODDAMNIT - Actually Loqor
    public static void sync(PropertiesHolder holder, String key, UUID tardis) {
        if (TardisUtil.isClient()) return;
        Object val = holder.getData().get(key);
        switch (val.getClass().getName()) {
            case "java.lang.Integer":
                ServerTardisManager.getInstance().sendToSubscribers(tardis, key, "int", String.valueOf(val));
                break;
            case "java.lang.Double":
                ServerTardisManager.getInstance().sendToSubscribers(tardis, key, "double", String.valueOf(val));
                break;
            case "java.lang.Float":
                ServerTardisManager.getInstance().sendToSubscribers(tardis, key, "float", String.valueOf(val));
                break;
            case "java.lang.Boolean":
                ServerTardisManager.getInstance().sendToSubscribers(tardis, key, "boolean", String.valueOf(val));
                break;
            case "java.lang.String":
                ServerTardisManager.getInstance().sendToSubscribers(tardis, key, "string", String.valueOf(val));
                break;
            case "java.lang.Identifier":
                ServerTardisManager.getInstance().sendToSubscribers(tardis, key, "identifier", getIdentifier(holder, key).toString());
                break;
        }
        /*if (val instanceof Integer) {
            ServerTardisManager.getInstance().sendToSubscribers(tardis, key, "int", String.valueOf(val));
            return;
        }

        if (val instanceof Double) {
            ServerTardisManager.getInstance().sendToSubscribers(tardis, key, "double", String.valueOf(val));
            return;
        }

        if (val instanceof Float) {
            ServerTardisManager.getInstance().sendToSubscribers(tardis, key, "float", String.valueOf(val));
            return;
        }

        if (val instanceof Boolean) {
            ServerTardisManager.getInstance().sendToSubscribers(tardis, key, "boolean", String.valueOf(val));
            return;
        }

        if (val instanceof String) {
            ServerTardisManager.getInstance().sendToSubscribers(tardis, key, "string", (String) val);
            return;
        }

        if (val instanceof Identifier || val instanceof LinkedTreeMap<?,?>) {
            ServerTardisManager.getInstance().sendToSubscribers(tardis, key, "identifier", getIdentifier(holder, key).toString());
            return;
        }*/
    }

    public static HashMap<String, Object> createDefaultProperties() {
        HashMap<String, Object> map = new HashMap<>();

        map.put(AUTO_LAND, false);
        map.put(FIND_GROUND, true);
        map.put(PREVIOUSLY_LOCKED, false);
        map.put(HANDBRAKE, true);
        map.put(HAIL_MARY, false);
        map.put(HUM_ENABLED, true);
        map.put(ALARM_ENABLED, false);
        map.put(IS_FALLING, false);
        map.put(ANTIGRAVS_ENABLED, false);
        map.put(IS_IN_ACTIVE_DANGER, false);
        map.put(HADS_ENABLED, false);
        map.put(FuelData.FUEL_COUNT, 1000d);
        map.put(FuelData.REFUELING, false);
        map.put(HAS_POWER, true);
        map.put(SIEGE_MODE, false);
        map.put(SIEGE_HELD, false);
        map.put(IS_IN_REAL_FLIGHT, false);
        map.put(DEMAT_TICKS,0);
        map.put(MAT_TICKS,0);
        map.put(SPEED,0);
        map.put(IS_CLOAKED, false);

        unlockAllFreebies(map);

        return map;
    }
}
