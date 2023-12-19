package mdteam.ait.tardis.handler.alarm;

import mdteam.ait.client.sounds.LoopingSound;
import mdteam.ait.client.sounds.PlayerFollowingLoopingSound;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.SoundHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;

import java.util.ArrayList;
import java.util.List;

// All this is CLIENT ONLY!!
// Loqor, if you dont understand DONT TOUCH or ask me! - doozoo
// todo create a ServerAlarmHandler if necessary eg in future when we do more of the stuff on the trello to do with alarms.
public class ClientAlarmHandler extends SoundHandler {
    public static LoopingSound CLOISTER;
    protected ClientAlarmHandler() {}

    public LoopingSound getCloister() {
        if (CLOISTER == null) CLOISTER = new PlayerFollowingLoopingSound(AITSounds.CLOISTER, SoundCategory.AMBIENT, 5f);

        return CLOISTER;
    }
    public static ClientAlarmHandler create() {
        if (MinecraftClient.getInstance().player == null) return null;

        ClientAlarmHandler handler = new ClientAlarmHandler();
        handler.generate();
        return handler;
    }

    private void generate() {
        if (CLOISTER == null) CLOISTER = new PlayerFollowingLoopingSound(AITSounds.CLOISTER, SoundCategory.AMBIENT);

        this.sounds = new ArrayList<>();
        this.sounds.addAll(List.of(
                CLOISTER
        ));
    }

    public boolean isPlayerInATardis() {
        if (MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return false;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos());

        return found != null;
    }

    public Tardis tardis() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos());
        return found;
    }

    public boolean isEnabled() {
        return PropertiesHandler.getBool(this.tardis().getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED);
    }

    public void tick(MinecraftClient client) {
        if (this.sounds == null) this.generate();

        if (isPlayerInATardis() && isEnabled()) {
            this.startIfNotPlaying(this.getCloister());
        } else {
            this.stopSounds();
        }
    }
}
