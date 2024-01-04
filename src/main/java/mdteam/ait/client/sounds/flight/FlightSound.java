package mdteam.ait.client.sounds.flight;

import mdteam.ait.client.sounds.PositionedLoopingSound;
import mdteam.ait.tardis.util.FlightUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class FlightSound extends PositionedLoopingSound {
    private static final Random rnd = new Random();
    private static final int PITCH_CHANGE_TICK = FlightUtil.covertSecondsToTicks(4);
    private int ticks = 0;

    public FlightSound(SoundEvent soundEvent, SoundCategory soundCategory, BlockPos pos, float volume) {
        super(soundEvent, soundCategory, pos, volume);
    }

    @Override
    public void tick() {
        super.tick();

        ticks++;
        if (ticks >= PITCH_CHANGE_TICK) {
            pitch = getRandomPitch();
            ticks = 0;
        }
    }

    private static float getRandomPitch() {
        return rnd.nextFloat(0.95f, 1.1f);
    }
}
