package loqor.ait.client.sounds.rain;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.ServerLavaHandler;
import loqor.ait.tardis.data.ServerRainHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.SoundHandler;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;

public class ClientRainSoundHandler extends SoundHandler {
    public static LoopingSound RAIN_SOUND;

    protected ClientRainSoundHandler() {
    }

    public LoopingSound getRainSound() {
        if (RAIN_SOUND == null)
            RAIN_SOUND = new PositionedLoopingSound(SoundEvents.WEATHER_RAIN,
                    SoundCategory.WEATHER,
                    tardis().getDesktop().doorPos().getPos(), 0.1f);

        return RAIN_SOUND;
    }

    public static ClientRainSoundHandler create() {
        if (MinecraftClient.getInstance().player == null) return null;

        ClientRainSoundHandler handler = new ClientRainSoundHandler();
        handler.generate();
        return handler;
    }

    private void generate() {

        if (tardis() == null) return;

        if (RAIN_SOUND == null && tardis().getDesktop().doorPos().getPos() != null)
            RAIN_SOUND = new PositionedLoopingSound(SoundEvents.WEATHER_RAIN,
                    SoundCategory.WEATHER,
                    tardis().getDesktop().doorPos().getPos(), 0.1f);

        this.sounds = new ArrayList<>();
        this.sounds.add(
                RAIN_SOUND
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

    public boolean isLanded() {
        Tardis tardis = this.tardis();
        return (tardis != null && tardis.travel().getState() == TravelHandlerBase.State.LANDED);
    }

    public void tick(MinecraftClient client) {
        if (this.sounds == null)
            this.generate();

        if (isLanded() && isPlayerInATardis() && tardis().getHandlers().<ServerRainHandler>get(TardisComponent.Id.RAINING).isEnabled()) {
            this.startIfNotPlaying(getRainSound());
        } else {
            this.stopSound(RAIN_SOUND);
        }
    }
}