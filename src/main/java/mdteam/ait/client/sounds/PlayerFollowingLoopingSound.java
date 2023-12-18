package mdteam.ait.client.sounds;

import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

// fixme long name

public class PlayerFollowingLoopingSound extends LoopingSound {
    public PlayerFollowingLoopingSound(SoundEvent soundEvent, SoundCategory soundCategory) {
        super(soundEvent, soundCategory);

        ClientPlayerEntity client = MinecraftClient.getInstance().player;
        this.x = client.getX();
        this.y = client.getY();
        this.z = client.getZ();
        this.repeat = true;
    }

    @Override
    public void tick() {
        super.tick();

        this.setCoordsToPlayerCoords();
    }

    private void setCoordsToPlayerCoords() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;

        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }
}
