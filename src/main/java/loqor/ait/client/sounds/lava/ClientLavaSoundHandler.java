package loqor.ait.client.sounds.lava;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import loqor.ait.api.TardisComponent;
import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.client.sounds.SoundHandler;
import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.tardis.handler.ExteriorEnvironmentHandler;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;

public class ClientLavaSoundHandler extends SoundHandler {

    public static LoopingSound LAVA_SOUND;

    public LoopingSound getLavaSound(ClientTardis tardis) {
        if (LAVA_SOUND == null)
            LAVA_SOUND = this.createLavaSound(tardis);

        return LAVA_SOUND;
    }

    private PositionedLoopingSound createLavaSound(ClientTardis tardis) {
        if (tardis == null || tardis.getDesktop().getDoorPos().getPos() == null)
            return null;

        return new PositionedLoopingSound(SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS,
                tardis.getDesktop().getDoorPos().getPos(), 0.2f);
    }

    public static ClientLavaSoundHandler create() {
        ClientLavaSoundHandler handler = new ClientLavaSoundHandler();

        handler.generate(ClientTardisUtil.getCurrentTardis());
        return handler;
    }

    private void generate(ClientTardis tardis) {
        if (LAVA_SOUND == null)
            LAVA_SOUND = createLavaSound(tardis);

        this.ofSounds(LAVA_SOUND);
    }

    private boolean shouldPlaySounds(ClientTardis tardis) {
        return tardis != null && tardis.travel().getState() == TravelHandlerBase.State.LANDED
                && tardis.<ExteriorEnvironmentHandler>handler(TardisComponent.Id.ENVIRONMENT).hasLava();
    }

    private boolean areDoorsOpen(ClientTardis tardis) {
        return tardis != null && tardis.door().isOpen();
    }

    public void tick(MinecraftClient client) {
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

        if (this.sounds == null)
            this.generate(tardis);

        if (this.shouldPlaySounds(tardis)) {
            if (areDoorsOpen(tardis)) {
                this.getLavaSound(tardis).setVolume(1.0f);
            } else {
                this.getLavaSound(tardis).setVolume(0.2f);
            }

            this.startIfNotPlaying(this.getLavaSound(tardis));
        } else {
            this.stopSound(LAVA_SOUND);
        }
    }
}
