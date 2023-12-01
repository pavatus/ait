package mdteam.ait.client.animation.console;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import org.joml.Math;
import the.mdteam.ait.TardisTravel;

public abstract class ConsoleAnimation {

    protected float alpha = 1;
    protected ConsoleBlockEntity console;
    protected int timeLeft, maxTime, startTime;
    protected float alphaChangeAmount = 0.005f;

    public ConsoleAnimation(ConsoleBlockEntity console) {
        this.console = console;
    }

    // fixme bug that sometimes happens where server doesnt have animation
    protected void runAlphaChecks(TardisTravel.State state) {
        if (this.console.getWorld().isClient)
            return;

        if (alpha <= 0f && state == TardisTravel.State.DEMAT) {
            console.getTardis().getTravel().toFlight();
        }
        if (alpha >= 1f && state == TardisTravel.State.MAT) {
            console.getTardis().getTravel().setState(TardisTravel.State.LANDED);
            //console.getTardis().getTravel().runAnimations(console);
        }
    }

    public abstract void tick();

    public abstract void setupAnimation(TardisTravel.State state);
}