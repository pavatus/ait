package loqor.ait.tardis.data;

import loqor.ait.tardis.Exclude;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTickable;
import loqor.ait.tardis.control.sequences.SequenceHandler;
import loqor.ait.tardis.data.loyalty.LoyaltyHandler;
import loqor.ait.tardis.data.permissions.PermissionHandler;
import loqor.ait.tardis.data.properties.PropertiesHolder;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class TardisHandlersManager extends TardisLink {
	@Exclude
	private List<TardisLink> tickables = new ArrayList<>();
	// TODO - refactor of this class, i have ideas
	private DoorData door;
	private PropertiesHolder properties;
	private WaypointHandler waypoints;
	private LoyaltyHandler loyalties;
	private OvergrownData overgrown;
	private ServerHumHandler hum;
	private ServerAlarmHandler alarms;
	private InteriorChangingHandler interior;
	private SequenceHandler sequenceHandler;
	private FuelData fuel;
	private HADSData hads;
	private FlightData flight;
	private SiegeData siege;
	private CloakData cloak;
	private StatsData stats;
	private TardisCrashData crashData;
	private SonicHandler sonic;
	private ShieldData shields;
	private PermissionHandler permissions;
	private BiomeHandler biome;
	public TardisHandlersManager(Tardis tardis) {
		super(tardis, TypeId.HANDLERS);

		this.door = new DoorData(tardis);
		this.properties = new PropertiesHolder(tardis);
		this.waypoints = new WaypointHandler(tardis);
		this.loyalties = new LoyaltyHandler(tardis);
		this.overgrown = new OvergrownData(tardis);
		this.hum = new ServerHumHandler(tardis);
		this.alarms = new ServerAlarmHandler(tardis);
		this.interior = new InteriorChangingHandler(tardis);
		this.sequenceHandler = new SequenceHandler(tardis);
		this.fuel = new FuelData(tardis);
		this.hads = new HADSData(tardis);
		this.flight = new FlightData(tardis);
		this.siege = new SiegeData(tardis);
		this.cloak = new CloakData(tardis);
		this.stats = new StatsData(tardis);
		this.crashData = new TardisCrashData(tardis);
		this.sonic = new SonicHandler(tardis);
		this.shields = new ShieldData(tardis);
		this.permissions = new PermissionHandler(tardis);
		this.biome = new BiomeHandler(tardis);

		generateTickables();
	}

	private void generateTickables() {
		if (tickables == null) tickables = new ArrayList<>();

		tickables.clear();

		addTickable(getDoor());
		addTickable(getProperties());
		addTickable(getWaypoints());
		addTickable(getLoyalties());
		addTickable(getOvergrown());
		addTickable(getHum());
		addTickable(getAlarms());
		addTickable(getInteriorChanger());
		addTickable(getSequenceHandler());
		addTickable(getFuel());
		addTickable(getHADS());
		addTickable(getFlight());
		addTickable(getSiege());
		addTickable(getStats());
		addTickable(getCrashData());
		addTickable(getSonic());
		addTickable(getShields());
		addTickable(getBiomeHandler());
	}

	protected void addTickable(TardisLink var) {
		tickables.add(var);
	}

	/**
	 * Called on the END of a servers tick
	 *
	 * @param server the current server
	 */
	public void tick(MinecraftServer server) {
		if (tickables == null) generateTickables();

		for (TardisTickable ticker : tickables) {
			if (ticker == null) {
				generateTickables();
				return;
			} // RAHHH I DONT CARE ABOUT PERFORMACNE, REGERNEATE IT ALLLL
			ticker.tick(server);
		}
	}

	/**
	 * Called on the START of a servers tick
	 *
	 * @param server the current server
	 */
	public void startTick(MinecraftServer server) {
		if (tickables == null) generateTickables();

		for (TardisTickable ticker : tickables) {
			if (ticker == null) {
				generateTickables();
				return;
			}
			ticker.startTick(server);
		}
	}

	public PropertiesHolder getProperties() {
		if (this.properties == null && this.findTardis().isPresent()) {
			this.properties = new PropertiesHolder(this.findTardis().get());
		}
		return properties;
	}

	public void setProperties(PropertiesHolder properties) {
		this.properties = properties;
	}

	public WaypointHandler getWaypoints() {
		if (this.waypoints == null && this.findTardis().isPresent()) {
			this.waypoints = new WaypointHandler(this.findTardis().get());
		}
		return waypoints;
	}

	public void setWaypoints(WaypointHandler waypoints) {
		this.waypoints = waypoints;
	}

	public LoyaltyHandler getLoyalties() {
		if (this.loyalties == null && this.findTardis().isPresent()) {
			this.loyalties = new LoyaltyHandler(this.findTardis().get());
		}
		return loyalties;
	}

	public void setLoyalties(LoyaltyHandler loyalties) {
		this.loyalties = loyalties;
	}

	public DoorData getDoor() {
		if (this.door == null && this.findTardis().isPresent()) {
			this.door = new DoorData(this.findTardis().get());
		}
		return door;
	}

	public void setDoor(DoorData door) {
		this.door = door;
	}

	public OvergrownData getOvergrown() {
		if (this.overgrown == null && this.findTardis().isPresent()) {
			this.overgrown = new OvergrownData(this.findTardis().get());
		}
		return overgrown;
	}

	public void setOvergrown(OvergrownData overgrown) {
		this.overgrown = overgrown;
	}

	public ServerHumHandler getHum() {
		if (this.hum == null && this.findTardis().isPresent()) {
			this.hum = new ServerHumHandler(this.findTardis().get());
		}

		return this.hum;
	}

	public ServerAlarmHandler getAlarms() {
		if (this.alarms == null && this.findTardis().isPresent()) {
			this.alarms = new ServerAlarmHandler(this.findTardis().get());
		}
		return alarms;
	}

	public void setAlarms(ServerAlarmHandler alarms) {
		this.alarms = alarms;
	}

	public InteriorChangingHandler getInteriorChanger() {
		if (this.interior == null && this.findTardis().isPresent()) {
			this.interior = new InteriorChangingHandler(this.findTardis().get());
		}
		return interior;
	}

	public BiomeHandler getBiomeHandler() {
		if (this.biome == null && this.findTardis().isPresent()) {
			this.biome = new BiomeHandler(this.findTardis().get());
		}
		return biome;
	}

	public SequenceHandler getSequenceHandler() {
		if (this.sequenceHandler == null && this.findTardis().isPresent()) {
			this.sequenceHandler = new SequenceHandler(this.findTardis().get());
		}
		return sequenceHandler;
	}

	public FuelData getFuel() {
		if (this.fuel == null && this.findTardis().isPresent()) {
			this.fuel = new FuelData(this.findTardis().get());
		}
		return fuel;
	}

	public void setFuel(FuelData fuel) {
		this.fuel = fuel;
	}

	public HADSData getHADS() {
		if (this.hads == null && this.findTardis().isPresent()) {
			this.hads = new HADSData(this.findTardis().get());
		}
		return hads;
	}

	public void setHADS(HADSData hads) {
		this.hads = hads;
	}

	public FlightData getFlight() {
		if (this.flight == null && this.findTardis().isPresent()) {
			this.flight = new FlightData(this.findTardis().get());
		}

		return flight;
	}

	public void setFlight(FlightData flight) {
		this.flight = flight;
	}

	public SiegeData getSiege() {
		if (this.siege == null && this.findTardis().isPresent()) {
			this.siege = new SiegeData(this.findTardis().get());
		}
		return this.siege;
	}

	public void setSiege(SiegeData siege) {
		this.siege = siege;
	}

	public CloakData getCloak() {
		if (this.cloak == null && this.findTardis().isPresent()) {
			this.cloak = new CloakData(this.findTardis().get());
		}
		return this.cloak;
	}

	public StatsData getStats() {
		if (this.stats == null && this.findTardis().isPresent()) {
			this.stats = new StatsData(this.findTardis().get());
			addTickable(this.stats);
		}
		return this.stats;
	}

	public void setCrashData(TardisCrashData crashData) {
		this.crashData = crashData;
	}

	public TardisCrashData getCrashData() {
		if (this.crashData == null && this.findTardis().isPresent()) {
			this.crashData = new TardisCrashData(this.findTardis().get());
		}
		return this.crashData;
	}

	public SonicHandler getSonic() {
		if (this.sonic == null && this.findTardis().isPresent()) {
			this.sonic = new SonicHandler(this.findTardis().get());
		}
		return this.sonic;
	}

	public void setSonic(SonicHandler sonicHandler) {
		this.sonic = sonicHandler;
	}

	public ShieldData getShields() {
		if (this.shields == null && this.findTardis().isPresent()) {
			this.shields = new ShieldData(this.findTardis().get());
		}
		return this.shields;
	}

	public void setShields(ShieldData shieldData) {
		this.shields = shieldData;
	}

	public PermissionHandler getPermissions() {
		if (this.permissions == null && this.findTardis().isPresent())
			this.permissions = new PermissionHandler(this.findTardis().get());

		return this.permissions;
	}

	public void setPermissions(PermissionHandler permissions) {
		this.permissions = permissions;
	}
}
