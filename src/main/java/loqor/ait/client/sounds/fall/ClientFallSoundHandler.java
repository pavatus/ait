package loqor.ait.client.sounds.fall;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PlayerFollowingLoopingSound;
import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.tardis.util.SoundHandler;

public class ClientFallSoundHandler extends SoundHandler {

    public static LoopingSound FLYING;

    public LoopingSound getFlyingSound() {
        if (FLYING == null)
            FLYING = createFlyingSound();

        return FLYING;
    }

    private LoopingSound createFlyingSound() {
        return new PlayerFollowingLoopingSound(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.AMBIENT, 10f);
    }

    public static ClientFallSoundHandler create() {
        ClientFallSoundHandler handler = new ClientFallSoundHandler();

        handler.generate();
        return handler;
    }

    private void generate() {
        if (FLYING == null)
            FLYING = createFlyingSound();

        this.ofSounds(FLYING);
    }

    private boolean shouldPlaySound(ClientTardis tardis) {
        return tardis != null && tardis.flight().falling().get();
    }

    public void tick(MinecraftClient client) {
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

        if (this.sounds == null)
            this.generate();

        if (this.shouldPlaySound(tardis)) {
            this.startIfNotPlaying(this.getFlyingSound());
        } else {
            this.stopSounds();
        }
    }
}
