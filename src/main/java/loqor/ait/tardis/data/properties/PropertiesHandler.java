package loqor.ait.tardis.data.properties;

import com.google.gson.internal.LinkedTreeMap;
import loqor.ait.core.blockentities.EngineCoreBlockEntity;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.unlockable.Unlockable;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.FuelData;
import loqor.ait.tardis.data.TardisCrashData;
import loqor.ait.AITMod;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.data.ShieldData;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

public class PropertiesHandler {
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
	public static final String CONSOLE_DISABLED = "console_disabled";
	public static final String LEAVE_BEHIND = "leave_behind";
	public static final String HOSTILE_PRESENCE_TOGGLE = "hostile_presence_toggle";
	public static final Identifier LEAVEBEHIND = new Identifier(AITMod.MOD_ID, "leavebehind");
	public static final Identifier HOSTILEALARMS = new Identifier(AITMod.MOD_ID, "hostilealarms");
	public static final Identifier SHIELDS = new Identifier(AITMod.MOD_ID, "shields");
	public static final Identifier VISUAL_SHIELDS = new Identifier(AITMod.MOD_ID, "visual_shields");

	// Should these methods be in the holder instead?

	public static void set(Tardis tardis, String key, Object val, boolean performSync) {
		if (!hasChanged(tardis.properties(), key, val))
			return;

		set(tardis.properties(), key, val);

		if (performSync)
			sync(tardis.properties(), key, (ServerTardis) tardis);
	}

	public static void set(Tardis tardis, String key, Object val) {
		set(tardis, key, val, !(tardis instanceof ClientTardis));
	}

	private static boolean hasChanged(PropertiesHolder holder, String key, Object newVal) {
		if (!holder.getData().containsKey(key)) return true;
		return !Objects.equals(holder.getData().get(key), newVal);
	}

	public static void set(PropertiesHolder holder, String key, Object val) {
		if (holder.getData().containsKey(key)) {
			holder.getData().replace(key, val);
			return;
		}

		holder.getData().put(key, val);
	}

	public static Object get(PropertiesHolder holder, String key) {
		if (!holder.getData().containsKey(key))
			return null;

		return holder.getData().get(key);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getOrDefault(Tardis tardis, String key, T def) {
		Object result = get(tardis.properties(), key);
		return result != null ? (T) result : def;
	}

	public static <T> T get(Tardis tardis, String key) {
		return getOrDefault(tardis, key, null);
	}

	public static TardisDesktopSchema getDesktop(PropertiesHolder holder, String key) {
		if (!holder.getData().containsKey(key)) {
			AITMod.LOGGER.error(key + " did not have a schema! Resetting to default..");
			setDesktop(holder, key, DesktopRegistry.getInstance().get(new Identifier(AITMod.MOD_ID, "cave")));
		}

		return DesktopRegistry.getInstance().get(getIdentifier(holder, key));
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
		if (holder.getData().get(key) instanceof LinkedTreeMap<?, ?> map) {
			if (map.get("namespace") == null || map.get("path") == null) {
				AITMod.LOGGER.error("namespace/path was null! Panic - I'm giving back the default desktop id, lets hope this doesnt cause a crash..");
				return DesktopRegistry.getInstance().toList().get(0).id();
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

	public static UUID getUUID(PropertiesHolder holder, String key) {
		if (!holder.getData().containsKey(key)) return null;

		if (!(holder.getData().get(key) instanceof UUID)) {
			AITMod.LOGGER.error("Tried to grab key " + key + " which was not an UUID!");
			return null;
		}

		return (UUID) holder.getData().get(key);
	}

	public static void setUnlocked(Tardis tardis, Unlockable unlockable, boolean value) {
		set(tardis, unlockable.id().getPath() + "_unlocked", value, true);
	}

	/**
	 * @apiNote ONLY USE THIS IF YOU KNOW WHAT YOU'RE DOING
	 */
	@Deprecated
	public static boolean isUnlocked(Tardis tardis, Identifier id) {
		return getBool(tardis.properties(), id.getPath() + "_unlocked");
	}

	public static boolean isUnlocked(Tardis tardis, Unlockable unlockable) {
		return isUnlocked(tardis, unlockable.id());
	}

	public static void setAutoPilot(PropertiesHolder handler, boolean val) {
		set(handler, AUTO_LAND, val);
	}

	public static boolean willAutoPilot(PropertiesHolder holder) {
		return getBool(holder, AUTO_LAND);
	}

	private static void unlockAllFreebies(HashMap<String, Object> map) {
		for (Iterator<TardisDesktopSchema> it = DesktopRegistry.getInstance().iterator(); it.hasNext(); ) {
			TardisDesktopSchema schema = it.next();
			map.put(schema.id().getPath() + "_unlocked", schema.freebie());
		}
	}

	// FIXME wow this sucks.
	private static void sync(PropertiesHolder holder, String key, ServerTardis tardis) {
		Object val = holder.getData().get(key);
		
		if (val == null)
			return;
		
		switch (val.getClass().getName()) {
			case "java.lang.Integer" ->
					ServerTardisManager.getInstance().sendPropertyToSubscribers(tardis, holder, key, "int", String.valueOf(val));
			case "java.lang.Double" ->
					ServerTardisManager.getInstance().sendPropertyToSubscribers(tardis, holder, key, "double", String.valueOf(val));
			case "java.lang.Float" ->
					ServerTardisManager.getInstance().sendPropertyToSubscribers(tardis, holder, key, "float", String.valueOf(val));
			case "java.lang.Boolean" ->
					ServerTardisManager.getInstance().sendPropertyToSubscribers(tardis, holder, key, "boolean", String.valueOf(val));
			case "java.lang.String" ->
					ServerTardisManager.getInstance().sendPropertyToSubscribers(tardis, holder, key, "string", String.valueOf(val));
			case "java.lang.Identifier" ->
					ServerTardisManager.getInstance().sendPropertyToSubscribers(tardis, holder, key, "identifier", getIdentifier(holder, key).toString());
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
		map.put(FuelData.FUEL_COUNT, 1000d);
		map.put(FuelData.REFUELING, false);
		map.put(HAS_POWER, false);
		map.put(SIEGE_MODE, false);
		map.put(SIEGE_HELD, false);
		map.put(IS_IN_REAL_FLIGHT, false);
		map.put(DEMAT_TICKS, 0);
		map.put(MAT_TICKS, 0);
		map.put(SPEED, 0);
		map.put(IS_CLOAKED, false);
		map.put(CONSOLE_DISABLED, false);
		map.put(LEAVE_BEHIND, false);
		map.put(HOSTILE_PRESENCE_TOGGLE, true);
		map.put(ShieldData.IS_SHIELDED, false);
		map.put(TardisCrashData.TARDIS_RECOVERY_STATE, TardisCrashData.State.NORMAL);
		map.put(TardisCrashData.TARDIS_REPAIR_TICKS, 0);
		map.put(EngineCoreBlockEntity.HAS_ENGINE_CORE, false);

		unlockAllFreebies(map);
		return map;
	}
}
