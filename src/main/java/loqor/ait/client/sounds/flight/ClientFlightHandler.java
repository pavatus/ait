package loqor.ait.client.sounds.flight;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.util.SoundHandler;

public class ClientFlightHandler extends SoundHandler {

    public static double MAX_DISTANCE = 16; // distance from console before the sound stops
    public static LoopingSound FLIGHT;

    public LoopingSound getFlightLoop(ClientTardis tardis) {
        if (FLIGHT == null)
            FLIGHT = createFlightSound(tardis);

        return FLIGHT;
    }

    private LoopingSound createFlightSound(ClientTardis tardis) {
        SoundEvent sound = AITSounds.FLIGHT_LOOP;

        if (tardis != null && !tardis.crash().isNormal())
            sound = AITSounds.UNSTABLE_FLIGHT_LOOP;

        return new InteriorFlightSound(sound, SoundCategory.BLOCKS, 1f);
    }

    public static ClientFlightHandler create() {
        ClientFlightHandler handler = new ClientFlightHandler();

        handler.generate(ClientTardisUtil.getCurrentTardis());
        return handler;
    }

    private void generate(ClientTardis tardis) {
        if (FLIGHT == null)
            FLIGHT = createFlightSound(tardis);

        this.ofSounds(FLIGHT);
    }

    private void playFlightSound(ClientTardis tardis) {
        this.startIfNotPlaying(this.getFlightLoop(tardis));
        this.getFlightLoop(tardis).tick();
    }

    private boolean shouldPlaySounds(ClientTardis tardis) {
        return tardis != null && tardis.engine().hasPower()
                && (tardis.travel().inFlight() || hasThrottleAndHandbrakeDown(tardis))
                && ClientTardisUtil.distanceFromConsole() < MAX_DISTANCE;
    }

    public boolean hasThrottleAndHandbrakeDown(ClientTardis tardis) {
        return tardis != null && tardis.travel().speed() > 0 && tardis.travel().handbrake();
    }

    public void tick(MinecraftClient client) {
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

        if (this.sounds == null)
            this.generate(tardis);

        if (this.shouldPlaySounds(tardis)) {
            this.playFlightSound(tardis);
        } else {
            this.stopSounds();
        }
    }
}
