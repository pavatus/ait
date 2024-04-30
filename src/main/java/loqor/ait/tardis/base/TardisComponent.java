package loqor.ait.tardis.base;

import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.*;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.data.*;
import loqor.ait.tardis.data.loyalty.LoyaltyHandler;
import loqor.ait.tardis.data.permissions.PermissionHandler;
import loqor.ait.tardis.data.properties.PropertiesHolder;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Base class for all tardis components.
 *
 * @implNote There should be NO logic run in the constructor. If you need to have such logic, implement it in {@link TardisComponent#init(Tardis, boolean)} method!
 */
public abstract class TardisComponent {

	@Exclude
	private Tardis tardis;
	private final Id id;

	public TardisComponent(Id id) {
		this.id = id;
	}

	/**
	 * Syncs this object and all its properties to the client
	 * Server only
	 */
	protected void sync() {
		if (!(this.tardis instanceof ServerTardis))
			return;

		ServerTardisManager.getInstance().sendToSubscribers(this);
	}

	public void init(Tardis tardis, boolean deserialized) {
		this.setTardis(tardis);
	}

	public Tardis tardis() {
		return this.tardis;
	}

	public Id getId() {
		return id;
	}

	public void setTardis(Tardis tardis) {
		this.tardis = tardis;
	}

	public enum Id {
		DESKTOP(TardisDesktop.class, Type.DESKTOP),
		DOOR(DoorData.class, Type.DOOR),
		EXTERIOR(TardisExterior.class, Type.EXTERIOR),
		TRAVEL(TardisTravel.class, Type.TRAVEL),
		SONIC(SonicHandler.class, Type.SONIC), // partially handled by properties?
		PERMISSIONS(PermissionHandler.class, Type.PERMISSIONS),
		HANDLERS(TardisHandlersManager.class, null), // doesn't require synching itself
		LOYALTY(LoyaltyHandler.class, "loyalties", Type.LOYALTY),

		// FIXME in future: currently handled by properties
		BIOME(BiomeHandler.class, null),
		SHIELDS(ShieldData.class, null),
		CONSOLE(TardisConsole.class, null),
		STATS(StatsData.class, null),
		CRASH_DATA(TardisCrashData.class, "crashData", null),
		WAYPOINTS(WaypointHandler.class, null),
		OVERGROWN(OvergrownData.class, null),
		HUM(ServerHumHandler.class, null), // may profit from using the auto sync
		ALARMS(ServerAlarmHandler.class, null),
		INTERIOR(InteriorChangingHandler.class, null),
		SEQUENCE(SequenceHandler.class, "sequenceHandler", null),
		FUEL(FuelData.class, null),
		HADS(HADSData.class, null),
		FLIGHT(FlightData.class, null),
		SIEGE(SiegeData.class, null),
		CLOAK(CloakData.class, null),

		// Special handling
		PROPERTIES(PropertiesHolder.class, null);

		// Used for updating old configs.
		public static final Map<String, Id> DESERIALIZATION = new HashMap<>();

		static {
			for (Id id : Id.values()) {
				DESERIALIZATION.put(id.name, id);
			}
		}

		private final Class<? extends TardisComponent> clazz;
		/**
		 * Used for translating outdated names.
		 */
		private final String name;
		private final Type<?> type;

		Id(Class<? extends TardisComponent> clazz, Type<?> type) {
			this(clazz, null, type);
		}

		Id(Class<? extends TardisComponent> clazz, String name, Type<?> type) {
			this.clazz = clazz;
			this.name = name == null ? this.toString().toLowerCase() : name;
			this.type = type;
		}

		public Class<? extends TardisComponent> clazz() {
			return clazz;
		}

		public Type<?> getType() {
			return type;
		}
	}

	public static class Type<T extends TardisComponent> {

		public static final Type<TardisDesktop> DESKTOP = new Type<>(Tardis::getDesktop, ClientTardis::setDesktop);
		public static final Type<DoorData> DOOR = new Type<>(Tardis::getDoor, ClientTardis::setDoor);
		public static final Type<TardisExterior> EXTERIOR = new Type<>(Tardis::getExterior, ClientTardis::setExterior);
		public static final Type<TardisTravel> TRAVEL = new Type<>(Tardis::getTravel, ClientTardis::setTravel);
		public static final Type<SonicHandler> SONIC = new Type<>(Tardis::sonic, ClientTardis::setSonic);
		public static final Type<PermissionHandler> PERMISSIONS = new Type<>(t -> t.handler(Id.PERMISSIONS),
				(t, c) -> t.getHandlers().set(Id.PERMISSIONS, c));
		public static final Type<LoyaltyHandler> LOYALTY = new Type<>(t -> t.handler(Id.LOYALTY),
				(t, c) -> t.getHandlers().set(Id.LOYALTY, c));


		private final Function<Tardis, T> getter;
		private final BiConsumer<ClientTardis, T> setter;

		private Type(Function<Tardis, T> getter, BiConsumer<ClientTardis, T> setter) {
			this.getter = getter;
			this.setter = setter;
		}

		public void set(ClientTardis tardis, T component) {
			this.setter.accept(tardis, component);
		}

		@SuppressWarnings("unchecked")
		public void unsafeSet(ClientTardis tardis, TardisComponent component) {
			this.set(tardis, (T) component);
		}

		public T get(Tardis tardis) {
			return this.getter.apply(tardis);
		}
	}
}