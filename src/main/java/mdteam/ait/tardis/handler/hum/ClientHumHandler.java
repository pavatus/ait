package mdteam.ait.tardis.handler.hum;

import mdteam.ait.AITMod;
import mdteam.ait.client.sounds.LoopingSound;
import mdteam.ait.client.sounds.PlayerFollowingLoopingSound;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.util.SoundHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// All this is CLIENT ONLY!!
// Loqor, if you dont understand DONT TOUCH or ask me! - doozoo
public class ClientHumHandler extends SoundHandler {
    private LoopingSound current;

    public static LoopingSound TOYOTA_HUM;
    public static LoopingSound CORAL_HUM;

    protected ClientHumHandler() {

        ClientPlayNetworking.registerGlobalReceiver(ServerHumHandler.SEND,
                (client, handler, buf, responseSender) -> {
                    Identifier id = buf.readIdentifier();

                    SoundInstance sound = findSoundById(id);

                    if (sound.getId() == SoundEvents.INTENTIONALLY_EMPTY.getId()) return;
                    if (!(sound instanceof LoopingSound)) return; // it aint a hum.

                    this.setHum((LoopingSound) sound);
                });
    }

    public LoopingSound getHum() {
        if (this.current == null) {
            this.current = (LoopingSound) findSoundByEvent(this.tardis().getHum().getHum());
        }

        return this.current;
    }

    public void setHum(LoopingSound hum) {
        LoopingSound previous = this.getHum();

        this.current = hum;

        this.stopSound(previous);
    }

    public static ClientHumHandler create() {
        if (MinecraftClient.getInstance().player == null) return null;

        ClientHumHandler handler = new ClientHumHandler();
        handler.generateHums();
        return handler;
    }

    private void generateHums() {
        if (TOYOTA_HUM == null) TOYOTA_HUM = new PlayerFollowingLoopingSound(AITSounds.TOYOTA_HUM, SoundCategory.AMBIENT);
        if (CORAL_HUM == null) CORAL_HUM = new PlayerFollowingLoopingSound(AITSounds.CORAL_HUM, SoundCategory.AMBIENT);

        this.sounds = new ArrayList<>();
        this.sounds.addAll(List.of(
                TOYOTA_HUM,
                CORAL_HUM
        ));
    }

    public boolean isPlayerInATardis() {
        if (MinecraftClient.getInstance().world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) return false;
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
        return PropertiesHandler.getBool(this.tardis().getProperties(), PropertiesHandler.HUM_ENABLED);
    }

    public void tick(MinecraftClient client) {
        if (this.sounds == null) this.generateHums();

        if (this.current != null && !isPlayerInATardis()) {
            this.current = null;
            return;
        }

        if (isPlayerInATardis() && isEnabled()) {
            this.startIfNotPlaying(this.getHum());
        } else {
            this.stopSounds();
        }
    }
}
