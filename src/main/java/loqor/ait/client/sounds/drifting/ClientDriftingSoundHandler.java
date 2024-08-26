package loqor.ait.client.sounds.drifting;

import loqor.ait.client.sounds.PlayerFollowingSound;
import loqor.ait.client.sounds.SoundHandler;
import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.AITSounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;

// Client only class. One of the last surviving remnants of Duzocode.
public class ClientDriftingSoundHandler extends SoundHandler {

    public static SoundInstance DRIFTING;
    private long counter;

    public SoundInstance getDrifting() {
        if (DRIFTING == null)
            DRIFTING = createAlarmSound();

        return DRIFTING;
    }

    private SoundInstance createAlarmSound() {
        return new PlayerFollowingSound(AITSounds.DRIFTING_MUSIC, SoundCategory.AMBIENT, 1f);
    }

    public static ClientDriftingSoundHandler create() {
        ClientDriftingSoundHandler handler = new ClientDriftingSoundHandler();

        handler.generate();
        return handler;
    }

    private void generate() {
        if (DRIFTING == null)
            DRIFTING = createAlarmSound();

        this.ofSounds(DRIFTING);
    }

    private boolean shouldPlaySound(ClientTardis tardis) {
        return tardis != null && !tardis.engine().hasPower();
    }

    public void tick(MinecraftClient client) {
        this.counter++;
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

        // check the ticks every 2 minutes
        if (client.player == null || this.counter % 2 * 60 * 20 != 0)
            return;

        if (this.sounds == null)
            this.generate();

        if (this.shouldPlaySound(tardis)) {
            this.startIfNotPlaying(this.getDrifting());
        } else {
            this.counter = 0;
            this.stopSounds();
        }
    }
}
