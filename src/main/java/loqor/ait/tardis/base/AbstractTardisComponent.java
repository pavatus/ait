package loqor.ait.tardis.base;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.SonicHandler;
import loqor.ait.tardis.data.TardisHandlersManager;
import loqor.ait.tardis.data.loyalty.LoyaltyHandler;
import loqor.ait.tardis.data.permissions.PermissionHandler;
import loqor.ait.tardis.data.properties.PropertiesHolder;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Base class for all tardis components.
 *
 * @implNote There should be NO logic run in the constructor. If you need to have such logic, implement it in {@link AbstractTardisComponent#init()} method!
 */
public abstract class AbstractTardisComponent {

	protected UUID tardisId; // I know Theo would prefer this to be a Tardis class but i dont know how to get it working..
	private final TypeId id;

	public AbstractTardisComponent(Tardis tardis, TypeId id) {
		this.tardisId = tardis.getUuid();
		this.id = id;
	}

	/**
	 * Syncs this object and all its properties to the client
	 * Server only
	 */
	protected void sync() {
		if (TardisUtil.isClient())
			return;

		ServerTardisManager.getInstance().sendToSubscribers(this);
	}

	public void init() {
	}

	public Init getInitMode() {
		return Init.ALWAYS;
	}

	/**
	 * Attempts to find the TARDIS related to this component
	 * It looks for the components tardis id stored in the lookup
	 * This is resource intensive and the result of this should be stored in a variable (you should be doing this anyway for things you are repeatedly calling)
	 * @return the found TARDIS, or empty.
	 */
	public Optional<Tardis> findTardis() {
		if (TardisUtil.isClient()) { // todo replace deprecated check
			if (!ClientTardisManager.getInstance().hasTardis(this.tardisId)) {
				if (this.tardisId != null)
					ClientTardisManager.getInstance().loadTardis(this.tardisId, tardis -> {});
				return Optional.empty();
			}
			return Optional.of(ClientTardisManager.getInstance().getTardis(this.tardisId));
		}
		return Optional.ofNullable(ServerTardisManager.getInstance().getTardis(this.tardisId));
	}

	public TypeId getId() {
		return id;
	}

	public void setTardis(Tardis tardis) {
		this.tardisId = tardis.getUuid();
	}

	public enum Init {
		NO_INIT,
		ALWAYS, // always init
		FIRST, // when tardis placed
		DESERIALIZE // when tardis deserialized
	}

	public enum TypeId {
		DESKTOP(Type.DESKTOP),
		DOOR(Type.DOOR),
		EXTERIOR(Type.EXTERIOR),
		TRAVEL(Type.TRAVEL),
		SONIC(Type.SONIC), // partially handled by properties?
		PERMISSIONS(Type.PERMISSIONS),
		HANDLERS(Type.HANDLERS),
		LOYALTY(Type.LOYALTY),

		// FIXME in future: currently handled by properties
		BIOME(null),
		SHIELD(null),
		CONSOLE(null),
		STATS(null),
		CRASH(null),
		WAYPOINT(null),
		OVERGROWN(null),
		HUM(null), // may profit from using the auto sync
		ALARM(null),
		INTERIOR_CHANGE(null),
		SEQUENCE(null),
		FUEL(null),
		HADS(null),
		FLIGHT(null),
		SIEGE(null),
		CLOAK(null),

		// Special handling
		PROPERTIES(Type.PROPERTIES);

		private final Type<?> type;

		TypeId(Type<?> type) {
			this.type = type;
		}

		public Type<?> getType() {
			return type;
		}
	}

	public static class Type<T extends AbstractTardisComponent> {

		public static final Type<TardisDesktop> DESKTOP = new Type<>(TardisDesktop.class, Tardis::getDesktop, ClientTardis::setDesktop);
		public static final Type<DoorData> DOOR = new Type<>(DoorData.class, Tardis::getDoor, ClientTardis::setDoor);
		public static final Type<TardisExterior> EXTERIOR = new Type<>(TardisExterior.class, Tardis::getExterior, ClientTardis::setExterior);
		public static final Type<TardisTravel> TRAVEL = new Type<>(TardisTravel.class, Tardis::getTravel, ClientTardis::setTravel);
		public static final Type<SonicHandler> SONIC = new Type<>(SonicHandler.class, Tardis::getSonic, ClientTardis::setSonic);
		public static final Type<PermissionHandler> PERMISSIONS = new Type<>(PermissionHandler.class,
				t -> t.getHandlers().getPermissions(), (t, c) -> t.getHandlers().setPermissions(c));
		public static final Type<TardisHandlersManager> HANDLERS = new Type<>(TardisHandlersManager.class, Tardis::getHandlers, ClientTardis::setHandlers);
		public static final Type<LoyaltyHandler> LOYALTY = new Type<>(LoyaltyHandler.class,
				t -> t.getHandlers().getLoyalties(), (t, c) -> t.getHandlers().setLoyalties(c));

		// Special
		public static final Type<PropertiesHolder> PROPERTIES = new Type<>(PropertiesHolder.class, null, null); // FIXME Special handling for the properties. Can't rewrite it all now so leaving this for parity.

		private final Class<T> clazz;
		private final Function<Tardis, T> getter;
		private final BiConsumer<ClientTardis, T> setter;

		private Type(Class<T> clazz, Function<Tardis, T> getter, BiConsumer<ClientTardis, T> setter) {
			this.clazz = clazz;
			this.getter = getter;
			this.setter = setter;
		}

		public void set(ClientTardis tardis, T component) {
			this.setter.accept(tardis, clazz.cast(component));
		}

		@SuppressWarnings("unchecked")
		public void unsafeSet(ClientTardis tardis, AbstractTardisComponent component) {
			this.set(tardis, (T) component);
		}

		public T get(Tardis tardis) {
			return this.getter.apply(tardis);
		}

		public Class<T> clazz() {
			return clazz;
		}
	}
}