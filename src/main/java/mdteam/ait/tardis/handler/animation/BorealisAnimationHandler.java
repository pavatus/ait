package mdteam.ait.tardis.handler.animation;

import mdteam.ait.tardis.console.BorealisConsole;
import mdteam.ait.tardis.console.ConsoleSchema;
import mdteam.ait.tardis.handler.AnimationsHandler;

import java.util.UUID;

public class BorealisAnimationHandler extends AnimationsHandler  {
    public BorealisAnimationHandler(UUID tardisId) {
        super(tardisId, BorealisConsole.REFERENCE);
    }
}
