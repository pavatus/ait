package mdteam.ait.tardis;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.data.AbsoluteBlockPos;

import java.util.UUID;
import java.util.function.Function;

public class Tardis {

    private final TardisTravel travel;
    private final UUID uuid;
    private TardisDesktop desktop;
    private final TardisExterior exterior;
    private final TardisConsole console;
    private DoorHandler door;

    public Tardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorEnum exteriorType, ConsoleEnum consoleType) {
        this(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), (tardis) -> new TardisExterior(tardis, exteriorType), (tardis) -> new TardisConsole(tardis, consoleType), new DoorHandler(uuid));
    }

    protected Tardis(UUID uuid, Function<Tardis, TardisTravel> travel, Function<Tardis, TardisDesktop> desktop, Function<Tardis, TardisExterior> exterior, Function<Tardis, TardisConsole> console, DoorHandler door) {
        this.uuid = uuid;
        this.travel = travel.apply(this);
        this.desktop = desktop.apply(this);
        this.exterior = exterior.apply(this);
        this.console = console.apply(this);
        this.door = door; // this is going to have saving problems but loqor set it to default not locked so i trust him on this one
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
        return this.door;
    }

    public void setLockedTardis(boolean bool) {
        this.door.setLocked(bool);
    }

    public boolean getLockedTardis() {
        return this.door.locked();
    }

    public TardisTravel getTravel() {
        return travel;
    }
}
