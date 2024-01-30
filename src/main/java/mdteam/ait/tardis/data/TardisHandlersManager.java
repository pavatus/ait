package mdteam.ait.tardis.data;

import mdteam.ait.tardis.Exclude;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTickable;
import mdteam.ait.tardis.data.loyalty.LoyaltyHandler;
import mdteam.ait.tardis.data.properties.PropertiesHolder;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class TardisHandlersManager extends TardisLink {
    @Exclude
    private List<TardisLink> tickables = new ArrayList<>();
    // Yup
    private DoorData door;
    private PropertiesHolder properties;
    private WaypointHandler waypoints;
    private LoyaltyHandler loyalties;
    private OvergrownData overgrown;
    private ServerHumHandler hum = null;
    private ServerAlarmHandler alarms;
    private InteriorChangingHandler interior;
    private FuelData fuel;
    private HADSData hads;
    private FlightData flight;
    private SiegeData siege;
    private CloakData cloak;
    private StatsData stats;
    // private final SequenceHandler sequence;

    public TardisHandlersManager(Tardis tardis) {
        super(tardis, "handlers");

        this.door = new DoorData(tardis);
        this.properties = new PropertiesHolder(tardis);
        this.waypoints = new WaypointHandler(tardis);
        this.loyalties = new LoyaltyHandler(tardis);
        this.overgrown = new OvergrownData(tardis);
        this.hum = new ServerHumHandler(tardis);
        alarms = new ServerAlarmHandler(tardis);
        interior = new InteriorChangingHandler(tardis);
        this.fuel = new FuelData(tardis);
        this.hads = new HADSData(tardis);
        this.flight = new FlightData(tardis);
        this.siege = new SiegeData(tardis);
        this.cloak = new CloakData(tardis);
        this.stats = new StatsData(tardis);
        // this.sequence = new SequenceHandler(tardisId);

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
        addTickable(getFuel());
        addTickable(getHADS());
        addTickable(getFlight());
        addTickable(getSiege());
        addTickable(getStats());
        // addTickable(getSequencing()); // todo sequences
    }

    protected void addTickable(TardisLink var) {
        tickables.add(var);
    }

    /**
     * Called on the END of a servers tick
     * @param server
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
     * @param server
     */
    public void startTick(MinecraftServer server) {
        if (tickables == null) generateTickables();

        for (TardisTickable ticker:  tickables) {
            if (ticker == null) {
                generateTickables();
                return;
            }
            ticker.startTick(server);
        }
    }

    public PropertiesHolder getProperties() {
        if (this.properties == null && getTardis().isPresent()) {
            this.properties = new PropertiesHolder(this.getTardis().get());
        }
        return properties;
    }

    public void setProperties(PropertiesHolder properties) {
        this.properties = properties;
    }

    public WaypointHandler getWaypoints() {
        if (this.waypoints == null && getTardis().isPresent()) {
            this.waypoints = new WaypointHandler(this.getTardis().get());
        }
        return waypoints;
    }
    public void setWaypoints(WaypointHandler waypoints) {
        this.waypoints = waypoints;
    }

    public LoyaltyHandler getLoyalties() {
        if (this.loyalties == null && getTardis().isPresent()) {
            this.loyalties = new LoyaltyHandler(this.getTardis().get());
        }
        return loyalties;
    }
    public void setLoyalties(LoyaltyHandler loyalties) {
        this.loyalties = loyalties;
    }
    public DoorData getDoor() {
        if (this.door == null && getTardis().isPresent()) {
            this.door = new DoorData(this.getTardis().get());
        }
        return door;
    }
    public void setDoor(DoorData door) {
        this.door = door;
    }
    public OvergrownData getOvergrown() {
        if (this.overgrown == null && getTardis().isPresent()) {
            this.overgrown = new OvergrownData(this.getTardis().get());
        }
        return overgrown;
    }
    public void setOvergrown(OvergrownData overgrown) {
        this.overgrown = overgrown;
    }
    public ServerHumHandler getHum() {
        if (this.hum == null && getTardis().isPresent()) {
            this.hum = new ServerHumHandler(this.getTardis().get());
        }

        return this.hum;
    }

    public ServerAlarmHandler getAlarms() {
        if (this.alarms == null && getTardis().isPresent()) {
            this.alarms = new ServerAlarmHandler(this.getTardis().get());
        }
        return alarms;
    }
    public void setAlarms(ServerAlarmHandler alarms) {
        this.alarms = alarms;
    }
    public InteriorChangingHandler getInteriorChanger() {
        if (this.interior == null && getTardis().isPresent()) {
            this.interior = new InteriorChangingHandler(this.getTardis().get());
        }
        return interior;
    }

    public FuelData getFuel() {
        if (this.fuel == null && getTardis().isPresent()) {
            this.fuel = new FuelData(this.getTardis().get());
        }
        return fuel;
    }
    public void setFuel(FuelData fuel) {
        this.fuel = fuel;
    }

    public HADSData getHADS() {
        if (this.hads == null && getTardis().isPresent()) {
            this.hads = new HADSData(this.getTardis().get());
        }
        return hads;
    }
    public void setHADS(HADSData hads) {
        this.hads = hads;
    }
    public FlightData getFlight() {
        if (this.flight == null && getTardis().isPresent()) {
            this.flight = new FlightData(this.getTardis().get());
        }

        return flight;
    }
    public void setFlight(FlightData flight) {
        this.flight = flight;
    }
    public SiegeData getSiege() {
        if (this.siege == null && getTardis().isPresent()) {
            this.siege = new SiegeData(this.getTardis().get());
        }
        return this.siege;
    }
    public void setSiege(SiegeData siege) {
        this.siege = siege;
    }
    public CloakData getCloak() {
        if (this.cloak == null && getTardis().isPresent()) {
            this.cloak = new CloakData(this.getTardis().get());
        }
        return this.cloak;
    }
    public StatsData getStats() {
        if (this.stats == null && getTardis().isPresent()) {
            this.stats = new StatsData(this.getTardis().get());
            addTickable(this.stats);
        }
        return this.stats;
    }
    // public SequenceHandler getSequencing() {return this.sequence;}
}
