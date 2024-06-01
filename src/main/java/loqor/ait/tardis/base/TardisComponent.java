package loqor.ait.tardis.base;

import loqor.ait.AITMod;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.*;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.data.*;
import loqor.ait.tardis.data.loyalty.LoyaltyHandler;
import loqor.ait.tardis.data.permissions.PermissionHandler;
import loqor.ait.tardis.data.properties.PropertiesHolder;
import loqor.ait.tardis.util.Ordered;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;

import java.util.function.BiConsumer;

/**
 * Base class for all tardis components.
 *
 * @implNote There should be NO logic run in the constructor. If you need to have such logic,
 * implement it in an appropriate init method!
 */
public abstract class TardisComponent extends Initializable<TardisComponent.InitContext> {

	@Exclude
	protected Tardis tardis;
	private final Id id;

	/**
	 * Do NOT under any circumstances run logic in this constructor.
	 * Default field values should be inlined. All logic should be done in an appropriate init method.
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
		if (this.isClient()) {
			AITMod.LOGGER.warn("Attempted to sync a component ON a client!", new IllegalAccessException());
			return;
		}

		ServerTardisManager.getInstance().sendToSubscribers(this);
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

	public boolean isClient() {
		return this.tardis() instanceof ClientTardis;
	}

	public boolean isServer() {
		return this.tardis() instanceof ServerTardis;
	}

	public TardisManager<?, ?> parentManager() {
		return TardisManager.getInstance(this.tardis);
	}

	public static void init(TardisComponent component, Tardis tardis, boolean deserialized) {
		TardisComponent.init(component, tardis, new InitContext(deserialized));
	}

	public static void init(TardisComponent component, Tardis tardis, InitContext context) {
		component.setTardis(tardis);
		Initializable.init(component, context);
	}

	public enum Id implements Ordered {
		DESKTOP(TardisDesktop.class, ClientTardis::setDesktop),
		TRAVEL(TardisTravel.class, null),
		EXTERIOR(TardisExterior.class, ClientTardis::setExterior),

		DOOR(DoorData.class),
		SONIC(SonicHandler.class),
		PERMISSIONS(PermissionHandler.class),
		LOYALTY(LoyaltyHandler.class),
		ENGINE(EngineHandler.class),

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
		PROPERTIES(PropertiesHolder.class, null),

		TESTING(PropertyTestHandler.class, null);

		private final BiConsumer<ClientTardis, TardisComponent> setter;
		private final Class<? extends TardisComponent> clazz;

		<T extends TardisComponent>Id(Class<T> clazz) {
			this(clazz, ClientTardis::set);
		}

		@SuppressWarnings("unchecked")
		<T extends TardisComponent>Id(Class<T> clazz, BiConsumer<ClientTardis, T> setter) {
			this.clazz = clazz;
			this.setter = (BiConsumer<ClientTardis, TardisComponent>) setter;
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

		public TardisComponent get(ClientTardis tardis) {
			return switch (this) {
				case DESKTOP -> tardis.getDesktop();
				case EXTERIOR -> tardis.getExterior();
				case TRAVEL -> tardis.travel();
				default -> tardis.handler(this);
			};
		}
	}

	public record InitContext(boolean deserialized) implements Initializable.Context {

		public boolean created() {
			return !deserialized;
		}
	}
}