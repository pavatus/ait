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
import java.util.function.Supplier;

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
	 * @deprecated Use properties v2.
	 */
	@Deprecated
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

	public enum Id implements IdLike {
		// Base parts.
		DESKTOP(TardisDesktop.class, null, ClientTardis::setDesktop),
		TRAVEL(TardisTravel.class, null),
		CONSOLE(TardisConsole.class, null, null),
		EXTERIOR(TardisExterior.class, null, ClientTardis::setExterior),
		HANDLERS(TardisHandlersManager.class, null, null),

		DOOR(DoorData.class, DoorData::new),
		SONIC(SonicHandler.class, SonicHandler::new),
		PERMISSIONS(PermissionHandler.class, PermissionHandler::new),
		LOYALTY(LoyaltyHandler.class, LoyaltyHandler::new),
		ENGINE(EngineHandler.class, EngineHandler::new),

		// FIXME in future: currently handled by properties
		BIOME(BiomeHandler.class, BiomeHandler::new, null),
		SHIELDS(ShieldData.class, ShieldData::new, null),

		STATS(StatsData.class, StatsData::new, null),
		CRASH_DATA(TardisCrashData.class, TardisCrashData::new, null),
		WAYPOINTS(WaypointHandler.class, WaypointHandler::new, null),
		OVERGROWN(OvergrownData.class, OvergrownData::new, null),
		HUM(ServerHumHandler.class, ServerHumHandler::new, null),
		ALARMS(ServerAlarmHandler.class, ServerAlarmHandler::new, null),
		INTERIOR(InteriorChangingHandler.class, InteriorChangingHandler::new, null),
		SEQUENCE(SequenceHandler.class, SequenceHandler::new, null),
		FUEL(FuelData.class, FuelData::new, null),
		HADS(HADSData.class, HADSData::new, null),
		FLIGHT(FlightData.class, FlightData::new, null),
		SIEGE(SiegeData.class, SiegeData::new, null),
		CLOAK(CloakData.class, CloakData::new, null),

		PROPERTIES(PropertiesHolder.class, PropertiesHolder::new, null);

		private final BiConsumer<ClientTardis, TardisComponent> setter;
		private final Supplier<TardisComponent> creator;

		private final Class<? extends TardisComponent> clazz;

		private int index;

		<T extends TardisComponent>Id(Class<T> clazz, Supplier<T> creator) {
			this(clazz, creator, ClientTardis::set);
		}

		@SuppressWarnings("unchecked")
		<T extends TardisComponent>Id(Class<T> clazz, Supplier<T> creator, BiConsumer<ClientTardis, T> setter) {
			this.clazz = clazz;
			this.creator = (Supplier<TardisComponent>) creator;
			this.setter = (BiConsumer<ClientTardis, TardisComponent>) setter;
		}

		@Override
		public Class<? extends TardisComponent> clazz() {
			return clazz;
		}

		@Override
		public void set(ClientTardis tardis, TardisComponent component) {
			this.setter.accept(tardis, component);
		}

		@Override
		public TardisComponent create() {
			return this.creator.get();
		}

		@Override
		public boolean creatable() {
			return this.creator != null;
		}

		@Override
		public boolean mutable() {
			return this.setter != null;
		}

		@Override
		public int index() {
			return index;
		}

		@Override
		public void index(int i) {
			this.index = i;
		}

		public TardisComponent get(ClientTardis tardis) {
			return switch (this) {
				case DESKTOP -> tardis.getDesktop();
				case EXTERIOR -> tardis.getExterior();
				case TRAVEL -> tardis.travel();
				default -> tardis.handler(this);
			};
		}

		public static IdLike[] ids() {
			return Id.values();
		}
	}

	public interface IdLike extends Ordered {

		Class<? extends TardisComponent> clazz();
		void set(ClientTardis tardis, TardisComponent component);

		TardisComponent create();
		boolean creatable();

		boolean mutable();

		String name();

		int index();
		void index(int i);
	}

	public record InitContext(boolean deserialized) implements Initializable.Context {

		public boolean created() {
			return !deserialized;
		}
	}
}