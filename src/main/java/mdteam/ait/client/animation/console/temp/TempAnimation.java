package mdteam.ait.client.animation.console.temp;

import mdteam.ait.client.animation.console.ConsoleAnimation;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.sounds.MatSound;
import the.mdteam.ait.TardisTravel;

public class TempAnimation extends ConsoleAnimation {

    public TempAnimation(ConsoleBlockEntity console) {
        super(console);
    }

    @Override
    public void tick() {
        if (console.getTardis() == null)
            return;
        TardisTravel.State state = console.getTardis().getTravel().getState();
    }

    @Override
    public void setupAnimation(TardisTravel.State state) {
        MatSound sound = console.getConsole().getSound(state);

        timeLeft = sound.timeLeft();
        maxTime = sound.maxTime();
        startTime = sound.startTime();
    }
}
