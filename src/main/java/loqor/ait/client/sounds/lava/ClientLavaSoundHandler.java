package loqor.ait.client.sounds.lava;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.ServerLavaHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.SoundHandler;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.List;

public class ClientLavaSoundHandler extends SoundHandler {

    public static LoopingSound LAVA_SOUND;

    public LoopingSound getLavaSound(ClientTardis tardis) {
        if (LAVA_SOUND == null)
            LAVA_SOUND = this.createLavaSound(tardis);

        return LAVA_SOUND;
    }

    private PositionedLoopingSound createLavaSound(ClientTardis tardis) {
        if (tardis.getDesktop().doorPos().getPos() == null)
            return null;

        return new PositionedLoopingSound(SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS,
                tardis.getDesktop().doorPos().getPos(), 0.2f);
    }

    public static ClientLavaSoundHandler create() {
        ClientLavaSoundHandler handler = new ClientLavaSoundHandler();

        handler.generate(ClientTardisUtil.getCurrentTardis());
        return handler;
    }

    private void generate(ClientTardis tardis) {
        if (LAVA_SOUND == null)
            LAVA_SOUND = createLavaSound(tardis);

        this.sounds = List.of(LAVA_SOUND);
    }

    private boolean shouldPlaySounds(ClientTardis tardis) {
        return tardis != null && tardis.travel().getState() == TravelHandlerBase.State.LANDED
                && tardis.<ServerLavaHandler>handler(TardisComponent.Id.LAVA_OUTSIDE).isEnabled();
    }

    public void tick(MinecraftClient client) {
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

        if (this.sounds == null)
            this.generate(tardis);

        if (this.shouldPlaySounds(tardis)) {
            this.startIfNotPlaying(this.getLavaSound(tardis));
        } else {
            this.stopSound(LAVA_SOUND);
        }
    }
}