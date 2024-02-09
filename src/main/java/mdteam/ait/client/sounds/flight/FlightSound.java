package mdteam.ait.client.sounds.flight;

import mdteam.ait.client.sounds.ClientSoundManager;
import mdteam.ait.client.sounds.PlayerFollowingLoopingSound;
import mdteam.ait.client.sounds.PositionedLoopingSound;
import mdteam.ait.client.util.ClientTardisUtil;
import mdteam.ait.config.AITCustomConfig;
import mdteam.ait.tardis.util.FlightUtil;
import mdteam.ait.tardis.wrapper.client.ClientTardis;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

import java.util.Random;
import java.util.UUID;

import static mdteam.ait.AITMod.AIT_CUSTOM_CONFIG;

public class FlightSound extends PlayerFollowingLoopingSound {
    private static final Random rnd = new Random();
    private static final int PITCH_CHANGE_TICK = FlightUtil.convertSecondsToTicks(4);
    private int ticks = 0;

    public FlightSound(SoundEvent soundEvent, SoundCategory soundCategory, float volume) {
        super(soundEvent, soundCategory, volume);
    }

    @Override
    public void tick() {
        super.tick();

        ticks++;
        if (ticks >= PITCH_CHANGE_TICK) {
            pitch = getRandomPitch();
            ticks = 0;
        }

        volume = (float) ((1f - (ClientTardisUtil.distanceFromConsole() / ClientFlightHandler.MAX_DISTANCE))); // laag?
    }

    private static float getRandomPitch() {
        if (ClientTardisUtil.getCurrentTardis() == null) return 1f;

        int speed = ClientTardisUtil.getCurrentTardis().getTravel().getSpeed();

        if (ClientSoundManager.getFlight().hasThrottleAndHandbrakeDown()) {
            // todo i hate switch
            return switch (speed) {
                default -> 1.0f;
                case 1 -> 0.5f;
                case 2 -> 0.55f;
                case 3 -> 0.6f;
            };
        }

        return switch (speed) {
            default -> 1.0f;
            case 1 -> rnd.nextFloat(0.9f,0.95f);
            case 2 -> rnd.nextFloat(0.95f,1.0f);
            case 3 -> rnd.nextFloat(1.0f,1.25f);
        };
    }
}
