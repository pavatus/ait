package loqor.ait.client.sounds.vortex;

import java.util.Objects;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.AITSounds;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.SoundHandler;
import loqor.ait.tardis.wrapper.client.ClientTardis;

public class ClientVortexSoundsHandler extends SoundHandler {
    public static LoopingSound VORTEX_SOUND;

    public LoopingSound getVortexSound(ClientTardis tardis) {
        if (VORTEX_SOUND == null)
            VORTEX_SOUND = this.createVortexSound(tardis);

        if (shouldCheckSound())
            this.checkSound(tardis);

        return VORTEX_SOUND;
    }

    private void checkSound(ClientTardis tardis) { // TODO - could register to DOOR_MOVE event if it fires client-side too
        boolean valid = Objects.equals(
                tardis.getDesktop().doorPos().getPos(),
                VORTEX_SOUND.getPosition()
        );

        DeltaTimeManager.createDelay("check-vortex-delay", 5000L);

        if (valid) return;

        this.stopSound(VORTEX_SOUND);
        VORTEX_SOUND = this.createVortexSound(tardis);
    }
    private boolean shouldCheckSound() {
        return !DeltaTimeManager.isStillWaitingOnDelay("check-vortex-delay");
    }

    private PositionedLoopingSound createVortexSound(ClientTardis tardis) {
        if (tardis == null || tardis.getDesktop().doorPos().getPos() == null)
            return null;

        return new PositionedLoopingSound(AITSounds.VORTEX_SOUND, SoundCategory.AMBIENT,
                tardis.getDesktop().doorPos().getPos(), 0.1f);
    }

    public static ClientVortexSoundsHandler create() {
        ClientVortexSoundsHandler handler = new ClientVortexSoundsHandler();

        handler.generate(ClientTardisUtil.getCurrentTardis());
        return handler;
    }

    private void generate(ClientTardis tardis) {
        if (VORTEX_SOUND == null)
            VORTEX_SOUND = this.createVortexSound(tardis);

        this.ofSounds(VORTEX_SOUND);
    }

    private boolean shouldPlaySound(ClientTardis tardis) {
        return tardis != null && tardis.travel().getState() == TravelHandlerBase.State.FLIGHT;
    }

    public void tick(MinecraftClient client) {
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

        if (this.sounds == null)
            this.generate(tardis);

        if (this.shouldPlaySound(tardis)) {
            this.startIfNotPlaying(this.getVortexSound(tardis));
        } else {
            this.stopSound(VORTEX_SOUND);
        }
    }
}
