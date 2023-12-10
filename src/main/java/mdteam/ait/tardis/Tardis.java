package mdteam.ait.tardis;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.handler.PropertiesHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;
import java.util.function.Function;

public class Tardis {

    private final TardisTravel travel;
    private final UUID uuid;
    private TardisDesktop desktop;
    private final TardisExterior exterior;
    private final TardisConsole console;
    private final DoorHandler door;
    private final PropertiesHandler properties;

    public Tardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorEnum exteriorType, ConsoleEnum consoleType) {
        this(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), (tardis) -> new TardisExterior(tardis, exteriorType), (tardis) -> new TardisConsole(tardis, consoleType, consoleType.getControlTypesList()), false);
    }

    protected Tardis(UUID uuid, Function<Tardis, TardisTravel> travel, Function<Tardis, TardisDesktop> desktop, Function<Tardis, TardisExterior> exterior, Function<Tardis, TardisConsole> console, boolean locked) {
        this.uuid = uuid;
        this.travel = travel.apply(this);
        this.door = new DoorHandler(uuid);
        this.door.setLocked(locked);
        this.properties = new PropertiesHandler(uuid);
        this.desktop = desktop.apply(this);
        this.exterior = exterior.apply(this);
        this.console = console.apply(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setDesktop(TardisDesktop desktop) {
        this.desktop = desktop;
    }

    public TardisDesktop getDesktop() {
        return desktop;
    }

    public TardisExterior getExterior() {
        return exterior;
    }

    public TardisConsole getConsole() {
        return console;
    }
    public DoorHandler getDoor() {
        return door;
    }

    public void setLockedTardis(boolean bool) {
        this.getDoor().setLocked(bool);
    }

    public boolean getLockedTardis() {
        return this.getDoor().locked();
    }

    public TardisTravel getTravel() {
        return travel;
    }

    public PropertiesHandler getProperties() { return properties; }

    /**
     * Called at the end of a servers tick
     * @param server the server being ticked
     */
    public void tick(MinecraftServer server) {}

    /**
     * Called at the end of a worlds tick
     * @param world the world being ticked
     */
    public void tick(ServerWorld world) {}
}