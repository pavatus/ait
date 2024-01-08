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
        if (this.properties == null) {
            this.properties = new PropertiesHolder(this.getTardis());
        }
        return properties;
    }

    public void setProperties(PropertiesHolder properties) {
        this.properties = properties;
    }

    public WaypointHandler getWaypoints() {
        if (this.waypoints == null) {
            this.waypoints = new WaypointHandler(this.getTardis());
        }
        return waypoints;
    }
    public void setWaypoints(WaypointHandler waypoints) {
        this.waypoints = waypoints;
    }

    public LoyaltyHandler getLoyalties() {
        if (this.loyalties == null) {
            this.loyalties = new LoyaltyHandler(this.getTardis());
        }
        return loyalties;
    }
    public void setLoyalties(LoyaltyHandler loyalties) {
        this.loyalties = loyalties;
    }
    public DoorData getDoor() {
        if (this.door == null) {
            this.door = new DoorData(this.getTardis());
        }
        return door;
    }
    public void setDoor(DoorData door) {
        this.door = door;
    }
    public OvergrownData getOvergrown() {
        if (this.overgrown == null) {
            this.overgrown = new OvergrownData(this.getTardis());
        }
        return overgrown;
    }
    public void setOvergrown(OvergrownData overgrown) {
        this.overgrown = overgrown;
    }
    public ServerHumHandler getHum() {
        if (this.hum == null) {
            this.hum = new ServerHumHandler(this.getTardis());
        }

        return this.hum;
    }

    public ServerAlarmHandler getAlarms() {
        if (this.alarms == null) {
            this.alarms = new ServerAlarmHandler(this.getTardis());
        }
        return alarms;
    }
    public void setAlarms(ServerAlarmHandler alarms) {
        this.alarms = alarms;
    }
    public InteriorChangingHandler getInteriorChanger() {
        if (this.interior == null) {
            this.interior = new InteriorChangingHandler(this.getTardis());
        }
        return interior;
    }

    public FuelData getFuel() {
        if (this.fuel == null) {
            this.fuel = new FuelData(this.getTardis());
        }
        return fuel;
    }
    public void setFuel(FuelData fuel) {
        this.fuel = fuel;
    }

    public HADSData getHADS() {
        if (this.hads == null) {
            this.hads = new HADSData(this.getTardis());
        }
        return hads;
    }
    public void setHADS(HADSData hads) {
        this.hads = hads;
    }
    public FlightData getFlight() {
        if (this.flight == null) {
            this.flight = new FlightData(this.getTardis());
        }

        return flight;
    }
    public void setFlight(FlightData flight) {
        this.flight = flight;
    }
    public SiegeData getSiege() {
        if (this.siege == null) {
            this.siege = new SiegeData(this.getTardis());
        }
        return this.siege;
    }
    public void setSiege(SiegeData siege) {
        this.siege = siege;
    }
    public CloakData getCloak() {
        if (this.cloak == null) {
            this.cloak = new CloakData(this.getTardis());
        }
        return this.cloak;
    }
    public void setCloak(CloakData cloak) {
        this.cloak = cloak;
    }
    // public SequenceHandler getSequencing() {return this.sequence;}
}
