package mdteam.ait.tardis.handler.properties;

import com.google.gson.internal.LinkedTreeMap;
import mdteam.ait.AITMod;
import mdteam.ait.datagen.datagen_providers.AITLanguageProvider;
import mdteam.ait.registry.DesktopRegistry;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.desktops.CaveDesktop;
import mdteam.ait.tardis.handler.FuelHandler;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class PropertiesHandler { // todo move more things over to properties
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

    /**
     * used for setting things to a boolean
     */
    public static void setBool(PropertiesHolder holder, String key, boolean val) {
        if (holder.getData().containsKey(key)) {
            holder.getData().replace(key, val);
            return;
        }

        holder.getData().put(key, val);
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
        setIdentifier(holder, key, val.id());
    }

    public static Identifier getIdentifier(PropertiesHolder holder, String key) {
        if (!holder.getData().containsKey(key)) {
            AITMod.LOGGER.error(key + " did not have an identifier! Have fun w that null lol");
            return null;
        }

        // because gson saves it weird
        if (holder.getData().get(key) instanceof LinkedTreeMap map)
            return Identifier.of((String) map.get("namespace"), (String) map.get("path"));

        return (Identifier) holder.getData().get(key);
    }
    // why do i keep rewriting the same method???
    public static void setIdentifier(PropertiesHolder holder, String key, Identifier val) {
        if (holder.getData().containsKey(key)) {
            holder.getData().replace(key, val);
            return;
        }

        holder.getData().put(key, val);
    }

    public static boolean getBool(PropertiesHolder holder, String key) {
        if (!holder.getData().containsKey(key)) return false;

        if (!(holder.getData().get(key) instanceof Boolean)) {
            AITMod.LOGGER.warn("Tried to grab key " + key + " which was not a boolean!");
            return false;
        }

        return (boolean) holder.getData().get(key);
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

    public static PropertiesHolder getSubProperties(PropertiesHolder holder, String key) {
        if (!holder.getData().containsKey(key)) {
            AITMod.LOGGER.error(key + " is not a properties holder!! im being kind, heres an empty properties instead of a null crash. to loqor - this wont b saved prolly");
            return new PropertiesHolder(holder.tardis().getUuid());
        }

        return (PropertiesHolder) holder.getData().get(key);
    }
    public static void setSubProperties(PropertiesHolder holder, String key, PropertiesHolder val) {
        if (holder.getData().containsKey(key)) {
            holder.getData().replace(key, val);
            return;
        }
        holder.getData().put(key, val);
    }

    public static void setSchemaUnlocked(PropertiesHolder holder, TardisDesktopSchema schema, boolean val) {
        setBool(holder, schema.id().getPath() + "_unlocked", val);
    }
    public static boolean isSchemaUnlocked(PropertiesHolder holder, TardisDesktopSchema schema) {
        return getBool(holder, schema.id().getPath() + "_unlocked");
    }

    public static void setAutoPilot(PropertiesHolder handler, boolean val) {
        setBool(handler, AUTO_LAND, val);
    }

    public static boolean willAutoPilot(PropertiesHolder holder) {
        return getBool(holder, AUTO_LAND);
    }
    private static void unlockAllFreebies(HashMap<String, Object> map) {
        for (Iterator<TardisDesktopSchema> it = DesktopRegistry.iterator(); it.hasNext(); ) {
            TardisDesktopSchema schema = it.next();

            map.put(schema.id().getPath() + "_unlocked", schema.freebie());
        }
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
        map.put(FuelHandler.FUEL_COUNT, 1000d);
        map.put(FuelHandler.REFUELING, false);
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
