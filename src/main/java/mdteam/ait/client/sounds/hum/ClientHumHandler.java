package mdteam.ait.client.sounds.hum;

import mdteam.ait.client.sounds.LoopingSound;
import mdteam.ait.client.sounds.PlayerFollowingLoopingSound;
import mdteam.ait.client.sounds.PlayerFollowingSound;
import mdteam.ait.core.AITDimensions;
import mdteam.ait.core.AITSounds;
import mdteam.ait.registry.CreakRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.handler.ServerHumHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import mdteam.ait.tardis.sound.CreakSound;
import mdteam.ait.tardis.sound.HumSound;
import mdteam.ait.tardis.util.SoundHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static mdteam.ait.AITMod.AIT_CONFIG;

// All this is CLIENT ONLY!!
// Loqor, if you dont understand DONT TOUCH or ask me! - doozoo
public class ClientHumHandler extends SoundHandler {
    private LoopingSound current;

    public static LoopingSound TOYOTA_HUM;
    public static LoopingSound CORAL_HUM;
    public static LoopingSound ERROR_SOUND;
    private static final Random random = new Random();

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
            if (this.tardis() == null) return null;
            this.current = (LoopingSound) findSoundByEvent(this.tardis().getHandlers().getHum().getHum().sound());
        }

        return this.current;
    }

    public void setHum(LoopingSound hum) {
        LoopingSound previous = this.getHum();

        this.current = hum;

        this.stopSound(previous);
    }

    public void setServersHum(HumSound hum) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(this.tardis().getUuid());
        buf.writeString(hum.id().getNamespace());
        buf.writeString(hum.name());

        ClientPlayNetworking.send(ServerHumHandler.RECEIVE, buf);
    }

    public static ClientHumHandler create() {
        if (MinecraftClient.getInstance().player == null) return null;

        ClientHumHandler handler = new ClientHumHandler();
        handler.generateHums();
        return handler;
    }

    // todo, add all the sounds from the HumsRegistry here !!
    private void generateHums() {
        if (TOYOTA_HUM == null) TOYOTA_HUM = new PlayerFollowingLoopingSound(AITSounds.TOYOTA_HUM, SoundCategory.AMBIENT, AIT_CONFIG.INTERIOR_HUM_VOLUME());
        if (CORAL_HUM == null) CORAL_HUM = new PlayerFollowingLoopingSound(AITSounds.CORAL_HUM, SoundCategory.AMBIENT, AIT_CONFIG.INTERIOR_HUM_VOLUME());
        if (ERROR_SOUND == null) ERROR_SOUND = new PlayerFollowingLoopingSound(SoundEvents.ENTITY_CAMEL_DEATH, SoundCategory.AMBIENT, AIT_CONFIG.INTERIOR_HUM_VOLUME());

        this.sounds = new ArrayList<>();
        this.sounds.addAll(List.of(
                TOYOTA_HUM,
                CORAL_HUM,
                ERROR_SOUND
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
        if (player == null) return null;
        Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos());
        return found;
    }

    public boolean isEnabled() {
        return PropertiesHandler.getBool(this.tardis().getHandlers().getProperties(), PropertiesHandler.HUM_ENABLED);
    }
    public void playRandomCreak() {
        CreakSound chosen = CreakRegistry.getRandomCreak();
        PlayerFollowingSound following = new PlayerFollowingSound(chosen.sound(), SoundCategory.AMBIENT, AIT_CONFIG.INTERIOR_HUM_VOLUME());
        startIfNotPlaying(following);
    }

    public void tick(MinecraftClient client) {
        if (this.sounds == null) this.generateHums();

        if (client.player == null) return;

        if (this.current != null && !isPlayerInATardis()) {
            this.current = null;
            return;
        }

        if (isPlayerInATardis() && isEnabled() && tardis().hasPower()) {
            this.startIfNotPlaying(this.getHum());
        } else {
            if (!isPlayerInATardis())
                this.stopSounds();
            else
                if (random.nextInt(256) == 32) {
                    this.playRandomCreak();
                }
        }
    }
}
