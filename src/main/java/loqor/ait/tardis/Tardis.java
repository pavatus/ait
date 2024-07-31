package loqor.ait.tardis;

import loqor.ait.core.data.base.Exclude;
import loqor.ait.registry.impl.DesktopRegistry;
import loqor.ait.registry.impl.exterior.ExteriorVariantRegistry;
import loqor.ait.registry.unlockable.Unlockable;
import loqor.ait.tardis.base.Initializable;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.data.*;
import loqor.ait.tardis.data.loyalty.LoyaltyHandler;
import loqor.ait.tardis.data.properties.PropertiesHolder;
import loqor.ait.tardis.data.travel.TravelHandler;

import java.util.Objects;
import java.util.UUID;

public abstract class Tardis extends Initializable<TardisComponent.InitContext> {

	@Exclude(strategy = Exclude.Strategy.NETWORK)
	protected int version = 1;

	private UUID uuid;
	protected TardisDesktop desktop;
	protected TardisExterior exterior;
	protected TardisHandlersManager handlers;

	protected Tardis(UUID uuid, TardisDesktop desktop, TardisExterior exterior) {
		this.uuid = uuid;
		this.desktop = desktop;
		this.exterior = exterior;
		this.handlers = new TardisHandlersManager();
	}

	protected Tardis() { }

	@Override
	protected void onInit(TardisComponent.InitContext ctx) {
		TardisComponent.init(desktop, this, ctx);
		TardisComponent.init(exterior, this, ctx);
		TardisComponent.init(handlers, this, ctx);
	}

	public static void init(Tardis tardis, TardisComponent.InitContext ctx) {
		Initializable.init(tardis, ctx);
	}

	public UUID getUuid() {
		return uuid;
	}

	public TardisDesktop getDesktop() {
		return desktop;
	}

	public TardisExterior getExterior() {
		return exterior;
	}

	public DoorData door() {
		return this.getHandlers().get(TardisComponent.Id.DOOR);
	}

	public SonicHandler sonic() {
		return this.handler(TardisComponent.Id.SONIC);
	}

	public boolean getLockedTardis() {
		return this.door().locked();
	}

	public TravelHandler travel() {
		return this.handler(TardisComponent.Id.TRAVEL);
	}

	public RealFlightHandler flight() {
		return this.handler(TardisComponent.Id.FLIGHT);
	}

	public LoyaltyHandler loyalty() {
		return this.handler(TardisComponent.Id.LOYALTY);
	}

	public ServerAlarmHandler alarm() {
		return this.handler(TardisComponent.Id.ALARMS);
	}

	public StatsData stats() {
		return this.handler(TardisComponent.Id.STATS);
	}

	public EngineHandler engine() {
		return this.handler(TardisComponent.Id.ENGINE);
	}

	/**
	 * Retrieves the TardisHandlersManager instance associated with the given UUID.
	 *
	 * @return TardisHandlersManager instance or null if it doesn't exist
	 */
	public TardisHandlersManager getHandlers() {
		return handlers;
	}

	public <T extends TardisComponent> T handler(TardisComponent.IdLike type) {
		return this.handlers.get(type);
	}

    @Override
	public int hashCode() {
		return uuid.hashCode();
	}

	public double addFuel(double fuel) {
		return this.<FuelData>handler(TardisComponent.Id.FUEL).addFuel(fuel);
	}

	public void removeFuel(double fuel) {
		this.<FuelData>handler(TardisComponent.Id.FUEL).removeFuel(fuel);
	}

	public double getFuel() {
		return this.<FuelData>handler(TardisComponent.Id.FUEL).getCurrentFuel();
	}

	public void setFuelCount(double i) {
		this.<FuelData>handler(TardisComponent.Id.FUEL).setCurrentFuel(i);
	}

	public boolean isRefueling() {
		return this.<FuelData>handler(TardisComponent.Id.FUEL).isRefueling();
	}

	public void setRefueling(boolean b) {
		this.<FuelData>handler(TardisComponent.Id.FUEL).setRefueling(b);
	}

	public boolean isInDanger() {
		return this.<HADSData>handler(TardisComponent.Id.HADS).isInDanger();
	}

	public FuelData fuel() {
		return this.handler(TardisComponent.Id.FUEL);
	}

	public PropertiesHolder properties() {
		return this.handler(TardisComponent.Id.PROPERTIES);
	}

	public TardisCrashData crash() {
		return this.handler(TardisComponent.Id.CRASH_DATA);
	}

	public SequenceHandler sequence() {
		return this.handler(TardisComponent.Id.SEQUENCE);
	}

	public WaypointHandler waypoint() {
		return this.handler(TardisComponent.Id.WAYPOINTS);
	}

	public boolean isUnlocked(Unlockable unlockable) {
        return unlockable.freebie() || this.stats().isUnlocked(unlockable);
	}

	// for now this just checks that the exterior is the coral growth, which is bad. but its fine for first beta
	// this should stop basic features of the tardis from happening
	public boolean isGrowth() {
		return hasGrowthExterior() || hasGrowthDesktop();
	}

	public boolean hasGrowthExterior() {
		return Objects.equals(getExterior().getVariant(), ExteriorVariantRegistry.CORAL_GROWTH);
	}

	public boolean hasGrowthDesktop() {
		return Objects.equals(getDesktop().getSchema(), DesktopRegistry.DEFAULT_CAVE);
	}

	public boolean areShieldsActive() {
		return this.<ShieldData>handler(TardisComponent.Id.SHIELDS).areShieldsActive();
	}

	public boolean areVisualShieldsActive() {
		return this.<ShieldData>handler(TardisComponent.Id.SHIELDS).areVisualShieldsActive();
	}

	public SiegeData siege() {
		return this.handler(TardisComponent.Id.SIEGE);
	}

	public boolean isSiegeBeingHeld() {
		return this.<SiegeData>handler(TardisComponent.Id.SIEGE).isSiegeBeingHeld();
	}

	public void setSiegeBeingHeld(UUID b) {
		this.<SiegeData>handler(TardisComponent.Id.SIEGE).setSiegeBeingHeld(b);
	}

	@Override
	public String toString() {
		return uuid.toString();
	}
}