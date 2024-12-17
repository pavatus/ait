package loqor.ait.client.sounds;

import static loqor.ait.core.AITSounds.CONSOLE_AMBIENT;

import java.util.Objects;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;

import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.AITSounds;

public class ClientConsoleAmbientSoundsHandler extends SoundHandler {
    public static LoopingSound CONSOLE_AMBIENT;

    public LoopingSound getAmbientSounds(ClientTardis tardis) {
        if (CONSOLE_AMBIENT == null)
            CONSOLE_AMBIENT = this.createConsoleAmbient(tardis);

        return CONSOLE_AMBIENT;
    }

    private void validateVortexSound(ClientTardis tardis) {
        boolean valid = Objects.equals(
                tardis.getDesktop().doorPos().getPos(),
                this.getAmbientSounds(tardis).getPosition()
        );

        if (valid) return;

        this.stopSound(CONSOLE_AMBIENT);
        CONSOLE_AMBIENT = this.createConsoleAmbient(tardis);
    }

    private PositionedLoopingSound createConsoleAmbient(ClientTardis tardis) {
        if (tardis == null || tardis.getDesktop().doorPos().getPos() == null)
            return null;

        return new PositionedLoopingSound(AITSounds.VORTEX_SOUND, SoundCategory.AMBIENT,
                tardis.getDesktop().doorPos().getPos(), 0.7f);
    }

    public static ClientConsoleAmbientSoundsHandler create() {
        ClientConsoleAmbientSoundsHandler handler = new ClientConsoleAmbientSoundsHandler();

        handler.generate(ClientTardisUtil.getCurrentTardis());
        return handler;
    }

    private void generate(ClientTardis tardis) {
        if (CONSOLE_AMBIENT == null)
            CONSOLE_AMBIENT = this.createConsoleAmbient(tardis);

        this.ofSounds(CONSOLE_AMBIENT);
    }

    private boolean shouldPlaySound(ClientTardis tardis) {
        return tardis != null && tardis.fuel().hasPower();
    }

    public void tick(MinecraftClient client) {
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

        if (this.sounds == null)
            this.generate(tardis);

        if (this.shouldPlaySound(tardis)) {
            if (this.isPlaying(CONSOLE_AMBIENT)) return;

            this.validateVortexSound(tardis);
            this.startSound(this.getAmbientSounds(tardis));
            return;
        }

        this.stopSound(CONSOLE_AMBIENT);
    }
}
