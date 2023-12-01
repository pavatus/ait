package mdteam.ait.client.animation.console;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.entity.AnimationState;
import org.joml.Math;
import the.mdteam.ait.TardisTravel;

public abstract class ConsoleAnimation extends AnimationState {

    protected ConsoleBlockEntity console;
    protected int timeLeft, maxTime, startTime;

    public ConsoleAnimation(ConsoleBlockEntity console) {
        this.console = console;
    }

    public abstract void tick();

    public abstract void setupAnimation(TardisTravel.State state);
}