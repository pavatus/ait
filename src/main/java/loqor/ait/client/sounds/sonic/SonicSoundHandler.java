package loqor.ait.client.sounds.sonic;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.client.network.AbstractClientPlayerEntity;

public class SonicSoundHandler {
    private final HashMap<UUID, SonicSound> sounds;

    public SonicSoundHandler() {
        this.sounds = new HashMap<>();
    }

    public SonicSound get(AbstractClientPlayerEntity player) {
        return this.sounds.computeIfAbsent(
                player.getUuid(),
                uuid -> new SonicSound(player)
        );
    }

    public void onUse(AbstractClientPlayerEntity user) {
        this.get(user).onUse();
    }
    public void onFinishUse(AbstractClientPlayerEntity user) {
        this.get(user).onFinishUse();
    }
}
