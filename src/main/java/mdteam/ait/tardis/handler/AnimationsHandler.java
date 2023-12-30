package mdteam.ait.tardis.handler;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.advancement.TakeOffCriterion;
import mdteam.ait.tardis.console.ConsoleSchema;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.UUID;

/**
 * A class which holds the states for the controls which the client can read from during rendering to adjust the model
 */
public abstract class AnimationsHandler extends TardisLink {
    protected Identifier console;

    protected AnimationsHandler(UUID tardisId, ConsoleSchema console) {
        this(tardisId, console.id());
    }
    protected AnimationsHandler(UUID tardisId, Identifier id) {
        super(tardisId);
        this.console = id;
    }
}
