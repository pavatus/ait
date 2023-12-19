package mdteam.ait.tardis;

import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.VariantEnum;
import mdteam.ait.tardis.handler.OvergrownHandler;
import mdteam.ait.tardis.handler.TardisHandlersManager;
import mdteam.ait.tardis.handler.hum.ServerHumHandler;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHolder;
import mdteam.ait.tardis.handler.WaypointHandler;
import mdteam.ait.tardis.handler.loyalty.LoyaltyHandler;
import mdteam.ait.tardis.wrapper.server.*;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.UUID;
import java.util.function.Function;

public class Tardis {
    // this is starting to get a little bloated..

    private final TardisTravel travel;
    private final UUID uuid;
    private TardisDesktop desktop;
    private final TardisExterior exterior;
    private final TardisConsole console;
    private TardisHandlersManager handlers;

    public Tardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorEnum exteriorType, VariantEnum variant, ConsoleEnum consoleType) {
        this(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), (tardis) -> new TardisExterior(tardis, exteriorType, variant), (tardis) -> new TardisConsole(tardis, consoleType, consoleType.getControlTypesList()), false);
    }

    protected Tardis(UUID uuid, Function<Tardis, TardisTravel> travel, Function<Tardis, TardisDesktop> desktop, Function<Tardis, TardisExterior> exterior, Function<Tardis, TardisConsole> console, boolean locked) {
        this.uuid = uuid;
        this.travel = travel.apply(this);
        this.desktop = desktop.apply(this);
        this.exterior = exterior.apply(this);
        this.console = console.apply(this);
        this.handlers = new TardisHandlersManager(uuid);
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
        return this.getHandlers().getDoor();
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
    public TardisHandlersManager getHandlers() {
        if (handlers == null) {
            handlers = new TardisHandlersManager(getUuid());
        }

        return handlers;
    }

    /**
     * Called at the end of a servers tick
     *
     * @param server the server being ticked
     */
    public void tick(MinecraftServer server) {
        this.getHandlers().getOvergrownHandler().tick(server);
    }

    /**
     * Called at the end of a worlds tick
     *
     * @param world the world being ticked
     */
    public void tick(ServerWorld world) {
    }

    /**
     * Called at the end of a clients tick, ONLY FOR CLIENT STUFF!!
     * @param client the remote being ticked
     */
    public void tick(MinecraftClient client) { // fixme should likely be in ClientTardis instead, same with  other server-only things should be in ServerTardis

    }

    /**
     * Called at the START of a servers tick, ONLY to be used for syncing data to avoid comodification errors
     * @param server
     */
    public void startTick(MinecraftServer server) {
        if (!(this instanceof ServerTardis)) return;

        // safe casts trust
        ((ServerTardisExterior) this.exterior).startTick(server);
        ((ServerTardisTravel) this.travel).startTick(server);
        ((ServerTardisDesktop) this.desktop).startTick(server);
        ((ServerTardisConsole) this.console).startTick(server);
    }
}