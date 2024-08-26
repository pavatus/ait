package loqor.ait.client.sounds.hum;

import static loqor.ait.AITMod.AIT_CONFIG;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import loqor.ait.api.TardisComponent;
import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PlayerFollowingLoopingSound;
import loqor.ait.client.tardis.ClientTardis;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.tardis.handler.ServerHumHandler;
import loqor.ait.core.tardis.util.SoundHandler;
import loqor.ait.data.HumSound;
import loqor.ait.registry.impl.HumsRegistry;

public class ClientHumHandler extends SoundHandler {

    private LoopingSound current;

    protected ClientHumHandler() {
        ClientPlayNetworking.registerGlobalReceiver(ServerHumHandler.SEND, (client, handler, buf, responseSender) -> {
            Identifier id = buf.readIdentifier();
            SoundInstance sound = findSoundById(id);

            if (sound.getId() == SoundEvents.INTENTIONALLY_EMPTY.getId())
                return;

            if (!(sound instanceof LoopingSound hum))
                return; // it aint a hum.

            this.setHum(ClientTardisUtil.getCurrentTardis(), hum);
        });
    }

    public LoopingSound getHum(ClientTardis tardis) {
        if (this.current == null)
            this.current = (LoopingSound) findSoundByEvent(
                    tardis.<ServerHumHandler>handler(TardisComponent.Id.HUM).getHum().sound());

        return this.current;
    }

    public void setHum(ClientTardis tardis, LoopingSound hum) {
        LoopingSound previous = this.getHum(tardis);
        this.current = hum;

        this.stopSound(previous);
    }

    public void setServersHum(ClientTardis tardis, HumSound hum) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeUuid(tardis.getUuid());
        buf.writeString(hum.id().getNamespace());
        buf.writeString(hum.name());

        ClientPlayNetworking.send(ServerHumHandler.RECEIVE, buf);
    }

    public static ClientHumHandler create() {
        ClientHumHandler handler = new ClientHumHandler();

        handler.generateHums();
        return handler;
    }

    private void generateHums() {
        this.sounds = registryToList();
    }

    /**
     * Converts all the {@link HumSound}'s in the {@link HumsRegistry} to
     * {@link LoopingSound} so they are usable
     *
     * @return A list of {@link LoopingSound} from the {@link HumsRegistry}
     */
    private List<LoopingSound> registryToList() {
        List<LoopingSound> list = new ArrayList<>();

        for (HumSound sound : HumsRegistry.REGISTRY) {
            list.add(new PlayerFollowingLoopingSound(sound.sound(), SoundCategory.AMBIENT,
                    AIT_CONFIG.INTERIOR_HUM_VOLUME()));
        }

        return list;
    }

    private boolean shouldPlaySounds(ClientTardis tardis) {
        return tardis != null && tardis.engine().hasPower();
    }

    public void tick(MinecraftClient client) {
        ClientTardis tardis = ClientTardisUtil.getCurrentTardis();

        if (this.sounds == null)
            this.generateHums();

        if (this.shouldPlaySounds(tardis)) {
            this.startIfNotPlaying(this.getHum(tardis));
        } else {
            this.stopSounds();
        }
    }
}
