package mdteam.ait.tardis.handler;

import mdteam.ait.tardis.Exclude;
import mdteam.ait.tardis.TardisTickable;
import mdteam.ait.tardis.handler.loyalty.LoyaltyHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHolder;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TardisHandlersManager extends TardisLink {
    @Exclude
    private List<TardisLink> tickables = new ArrayList<>();
    // Yup
    private final DoorHandler door;
    private final PropertiesHolder properties;
    private final WaypointHandler waypoints;
    private final LoyaltyHandler loyalties;
    private final OvergrownHandler overgrown;
    private ServerHumHandler hum = null;
    private ServerAlarmHandler alarms;

    public TardisHandlersManager(UUID tardisId) {
        super(tardisId);

        this.door = new DoorHandler(tardisId);
        this.properties = new PropertiesHolder(tardisId);
        this.waypoints = new WaypointHandler(tardisId);
        this.loyalties = new LoyaltyHandler(tardisId);
        this.overgrown = new OvergrownHandler(tardisId);
        this.hum = new ServerHumHandler(tardisId);
        alarms = new ServerAlarmHandler(tardisId);

        generateTickables();
    }

    private void generateTickables() {
        if (tickables == null) tickables = new ArrayList<>();

        tickables.clear();

        addTickable(door);
        addTickable(properties);
        addTickable(waypoints);
        addTickable(loyalties);
        addTickable(overgrown);
        addTickable(hum);
        addTickable(alarms);
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
            ticker.startTick(server);
        }
    }

    public PropertiesHolder getProperties() {
        return properties;
    }

    public WaypointHandler getWaypoints() {
        return waypoints;
    }

    public LoyaltyHandler getLoyalties() {
        return loyalties;
    }
    public DoorHandler getDoor() {
        return door;
    }
    public OvergrownHandler getOvergrownHandler() {
        return overgrown;
    }
    public ServerHumHandler getHum() {
        if (this.hum == null) {
            this.hum = new ServerHumHandler(this.tardisId);
        }

        return this.hum;
    }

    public ServerAlarmHandler getAlarms() {
        return alarms;
    }
}
