package loqor.ait.client.sounds.flight;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;

import loqor.ait.client.sounds.SoundHandler;
import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.client.util.ClientTardisUtil;

public class ClientFlightHandler extends SoundHandler {

    public static double MAX_DISTANCE = 16; // distance from console before the sound stops
    public static InteriorFlightSound FLIGHT;

    public InteriorFlightSound getFlightLoop(ClientTardis tardis) {
        if (FLIGHT == null)
            FLIGHT = createFlightSound(tardis);

        return FLIGHT;
    }

    private InteriorFlightSound createFlightSound(ClientTardis tardis) {
        return new InteriorFlightSound(tardis.getExterior().getVariant().flight(), SoundCategory.BLOCKS);
    }

    public static ClientFlightHandler create() {

        return new ClientFlightHandler();
    }

    private void generate(ClientTardis tardis) {
        if (FLIGHT == null)
            FLIGHT = createFlightSound(tardis);

        this.ofSounds(FLIGHT);
    }

    private void playFlightSound(ClientTardis tardis) {
        this.startIfNotPlaying(this.getFlightLoop(tardis));

        InteriorFlightSound sound = this.getFlightLoop(tardis);
        sound.tick();

        if (sound.isDirty()) {
            if (sound.getData().id().equals(tardis.getExterior().getVariant().flight().id())) return;
            this.stopSounds();
            FLIGHT = null;
            this.generate(tardis);
            sound.setDirty(false);
        }
    }

    private boolean shouldPlaySounds(ClientTardis tardis) {
        return tardis != null && tardis.engine().hasPower()
                && (tardis.travel().inFlight() || hasThrottleAndHandbrakeDown(tardis));
    }

    public boolean hasThrottleAndHandbrakeDown(ClientTardis tardis) {
        return tardis != null && tardis.travel().speed() > 0 && tardis.travel().handbrake();
    }

    public void tick(MinecraftClient client) {
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

        if (tardis == null) {
            this.stopSounds();
            return;
        }

        if (this.sounds == null)
            this.generate(tardis);

        if (this.shouldPlaySounds(tardis)) {
            this.playFlightSound(tardis);
        } else {
            this.stopSounds();
        }
    }
}
