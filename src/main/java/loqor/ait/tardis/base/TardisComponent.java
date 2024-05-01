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

import java.util.function.BiConsumer;

/**
 * Base class for all tardis components.
 *
 * @implNote There should be NO logic run in the constructor. If you need to have such logic, implement it in {@link TardisComponent#init(Tardis, boolean)} method!
 */
public abstract class TardisComponent {

	@Exclude
	private Tardis tardis;
	private final Id id;

	/**
	 * Do NOT under any circumstances run logic in this constructor.
	 * Default field values should be inlined. All logic should be done in {@link TardisComponent#init(Tardis, boolean)}
	 * @implNote The {@link TardisComponent#tardis()} will always be null at the time this constructor gets called.
	 */
	public TardisComponent(Id id) {
		this.id = id;
	}

	/**
	 * Syncs this object and all its properties to the client.
	 * @implNote Server-side only.
	 */
	protected void sync() {
		if (!(this.tardis instanceof ServerTardis))
			return;

		ServerTardisManager.getInstance().sendToSubscribers(this);
	}

	/**
	 * Don't forget to run the super when overriding this method, otherwise you might be left tardisless!
	 * @param tardis the tardis getting linked
	 * @param deserialized {@code true} if the component is deserialized and not initialized from scratch.
	 *                     Use this if you want to do something when the component is created (e.g. generate an interior).
	 */
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
		DESKTOP(TardisDesktop.class, (tardis, component) -> tardis.setDesktop((TardisDesktop) component)),
		TRAVEL(TardisTravel.class, (tardis, component) -> tardis.setTravel((TardisTravel) component)),
		EXTERIOR(TardisExterior.class, (tardis, component) -> tardis.setExterior((TardisExterior) component)),

		DOOR(DoorData.class),
		SONIC(SonicHandler.class),
		PERMISSIONS(PermissionHandler.class),
		LOYALTY(LoyaltyHandler.class),

		// FIXME in future: currently handled by properties
		BIOME(BiomeHandler.class, null),
		SHIELDS(ShieldData.class, null),
		CONSOLE(TardisConsole.class, null),
		STATS(StatsData.class, null),
		CRASH_DATA(TardisCrashData.class, null),
		WAYPOINTS(WaypointHandler.class, null),
		OVERGROWN(OvergrownData.class, null),
		HUM(ServerHumHandler.class, null),
		ALARMS(ServerAlarmHandler.class, null),
		INTERIOR(InteriorChangingHandler.class, null),
		SEQUENCE(SequenceHandler.class, null),
		FUEL(FuelData.class, null),
		HADS(HADSData.class, null),
		FLIGHT(FlightData.class, null),
		SIEGE(SiegeData.class, null),
		CLOAK(CloakData.class, null),

		// Special handling
		HANDLERS(TardisHandlersManager.class, null),
		PROPERTIES(PropertiesHolder.class, null);

		private final BiConsumer<ClientTardis, TardisComponent> setter;
		private final Class<? extends TardisComponent> clazz;

		Id(Class<? extends TardisComponent> clazz) {
			this(clazz, ClientTardis::set);
		}

		Id(Class<? extends TardisComponent> clazz, BiConsumer<ClientTardis, TardisComponent> setter) {
			this.clazz = clazz;
			this.setter = setter;
		}

		public Class<? extends TardisComponent> clazz() {
			return clazz;
		}

		public void set(ClientTardis tardis, TardisComponent component) {
			this.setter.accept(tardis, component);
		}

		public boolean mutable() {
			return this.setter != null;
		}
	}
}