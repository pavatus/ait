package loqor.ait.client.sounds.rain;

import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.ServerRainHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.SoundHandler;
import loqor.ait.tardis.wrapper.client.ClientTardis;

public class ClientRainSoundHandler extends SoundHandler {

    public static LoopingSound RAIN_SOUND;

    public LoopingSound getRainSound(ClientTardis tardis) {
        if (RAIN_SOUND == null)
            RAIN_SOUND = this.createRainSound(tardis);

        return RAIN_SOUND;
    }

    private LoopingSound createRainSound(ClientTardis tardis) {
        if (tardis.getDesktop().doorPos().getPos() == null)
            return null;

        return new PositionedLoopingSound(SoundEvents.WEATHER_RAIN, SoundCategory.WEATHER,
                tardis.getDesktop().doorPos().getPos(), 0.1f);
    }

    public static ClientRainSoundHandler create() {
        ClientRainSoundHandler handler = new ClientRainSoundHandler();

        handler.generate(ClientTardisUtil.getCurrentTardis());
        return handler;
    }

    private void generate(ClientTardis tardis) {
        if (RAIN_SOUND == null)
            RAIN_SOUND = createRainSound(tardis);

        this.sounds = List.of(RAIN_SOUND);
    }

    private boolean shouldPlaySounds(ClientTardis tardis) {
        return tardis != null && tardis.travel().getState() == TravelHandlerBase.State.LANDED
                && tardis.<ServerRainHandler>handler(TardisComponent.Id.RAINING).isEnabled();
    }

    public void tick(MinecraftClient client) {
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

        if (this.sounds == null)
            this.generate(tardis);

        if (this.shouldPlaySounds(tardis)) {
            this.startIfNotPlaying(this.getRainSound(tardis));
        } else {
            this.stopSound(RAIN_SOUND);
        }
    }
}
