package loqor.ait.client.sounds.vortex;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.SoundHandler;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;

import java.util.ArrayList;

public class ClientVortexSoundsHandler extends SoundHandler {
    public static LoopingSound VORTEX_SOUND;

    protected ClientVortexSoundsHandler() {
    }

    public LoopingSound getVortexSound() {
        if (VORTEX_SOUND == null)
            VORTEX_SOUND = new PositionedLoopingSound(AITSounds.VORTEX_SOUND,
                    SoundCategory.AMBIENT,
                    tardis().getDesktop().doorPos().getPos(), 0.1f);

        return VORTEX_SOUND;
    }

    public static ClientVortexSoundsHandler create() {
        if (MinecraftClient.getInstance().player == null) return null;

        ClientVortexSoundsHandler handler = new ClientVortexSoundsHandler();
        handler.generate();
        return handler;
    }

    private void generate() {

        if (tardis() == null) return;

        if (VORTEX_SOUND == null && tardis().getDesktop().doorPos().getPos() != null)
            VORTEX_SOUND = new PositionedLoopingSound(AITSounds.VORTEX_SOUND,
                    SoundCategory.AMBIENT,
                    tardis().getDesktop().doorPos().getPos(), 0.1f);

        this.sounds = new ArrayList<>();
        this.sounds.add(
                VORTEX_SOUND
        );
    }

    public boolean isPlayerInATardis() {
        if (MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
            return false;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos(), false);

        return found != null;
    }

    public Tardis tardis() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
            return null;

        return TardisUtil.findTardisByInterior(player.getBlockPos(), false);
    }

    public boolean isInFlight() {
        Tardis tardis = this.tardis();
        return (tardis != null && tardis.travel().getState() == TravelHandlerBase.State.FLIGHT);
    }

    public void tick(MinecraftClient client) {
        if (this.sounds == null)
            this.generate();

        if (isPlayerInATardis() && isInFlight()) {
            this.startIfNotPlaying(getVortexSound());
        } else {
            this.stopSound(VORTEX_SOUND);
        }
    }
}