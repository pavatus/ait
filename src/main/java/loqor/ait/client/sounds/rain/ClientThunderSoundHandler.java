package loqor.ait.client.sounds.rain;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;

import loqor.ait.api.TardisComponent;
import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.client.sounds.SoundHandler;
import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.handler.ExteriorEnvironmentHandler;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;

public class ClientThunderSoundHandler extends SoundHandler {

    public static LoopingSound THUNDER_SOUND;

    public LoopingSound getThunderSound(ClientTardis tardis) {
        if (THUNDER_SOUND == null)
            THUNDER_SOUND = this.createThunderSound(tardis);

        return THUNDER_SOUND;
    }

    private LoopingSound createThunderSound(ClientTardis tardis) {
        if (tardis == null || tardis.getDesktop().getDoorPos().getPos() == null)
            return null;

        return new PositionedLoopingSound(AITSounds.THUNDER, SoundCategory.WEATHER,
                tardis.getDesktop().getDoorPos().getPos(), 0.3f);
    }

    public static ClientThunderSoundHandler create() {
        ClientThunderSoundHandler handler = new ClientThunderSoundHandler();

        handler.generate(ClientTardisUtil.getCurrentTardis());
        return handler;
    }

    private void generate(ClientTardis tardis) {
        if (THUNDER_SOUND == null)
            THUNDER_SOUND = createThunderSound(tardis);

        this.ofSounds(THUNDER_SOUND);
    }

    private boolean shouldPlaySounds(ClientTardis tardis) {
        return tardis != null && tardis.travel().getState() == TravelHandlerBase.State.LANDED
                && tardis.<ExteriorEnvironmentHandler>handler(TardisComponent.Id.ENVIRONMENT).isThundering();
    }

    private boolean isDoorOpen(ClientTardis tardis) {
        return tardis != null && tardis.door().isOpen();
    }

    public void tick(MinecraftClient client) {
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

        if (this.sounds == null)
            this.generate(tardis);

        if (this.shouldPlaySounds(tardis)) {
            LoopingSound thunderSound = this.getThunderSound(tardis);

            if (thunderSound != null) {
                thunderSound.setVolume(this.isDoorOpen(tardis) ? 2.5f : 0.4f);
                this.startIfNotPlaying(thunderSound);
            }
        } else {
            this.stopSound(THUNDER_SOUND);
        }
    }
}
