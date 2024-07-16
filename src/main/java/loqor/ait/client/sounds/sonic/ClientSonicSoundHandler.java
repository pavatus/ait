package loqor.ait.client.sounds.sonic;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PlayerFollowingLoopingSound;
import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.AITSounds;
import loqor.ait.core.item.SonicItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.ServerLavaHandler;
import loqor.ait.tardis.data.ServerRainHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.SoundHandler;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;

public class ClientSonicSoundHandler extends SoundHandler {
    public static PlayerFollowingLoopingSound SONIC_SOUND;
    protected ClientSonicSoundHandler() {
    }

    public PlayerFollowingLoopingSound getSonicSound() {

        if (SONIC_SOUND == null)
            SONIC_SOUND = new PlayerFollowingLoopingSound(AITSounds.SONIC_USE,
                    SoundCategory.PLAYERS,
                    1f, 1f);

        return SONIC_SOUND;
    }

    public static ClientSonicSoundHandler create() {
        if (MinecraftClient.getInstance().player == null) return null;

        ClientSonicSoundHandler handler = new ClientSonicSoundHandler();
        handler.generate();
        return handler;
    }

    private void generate() {

        if (SONIC_SOUND == null)
            SONIC_SOUND = new PlayerFollowingLoopingSound(AITSounds.SONIC_USE,
                SoundCategory.PLAYERS,
                1f, 1f);

        this.sounds = new ArrayList<>();
        this.sounds.add(
                SONIC_SOUND
        );
    }

    public ItemStack isPlayerHoldingSonic(PlayerEntity player) {
        if (player.getMainHandStack() != null && player.getMainHandStack().getItem() instanceof SonicItem) {
            return player.getMainHandStack();
        }
        return null;
    }

    public boolean sonicIsInUse(ItemStack item) {
        if (item == null) return false;
        NbtCompound nbt = item.getOrCreateNbt();

        if (nbt.contains(SonicItem.MODE_KEY))
            return nbt.getInt(SonicItem.MODE_KEY) != 0;

        return false;
    }

    public void tick(MinecraftClient client) {
        if (this.sounds == null)
            this.generate();

        if (client.player == null) return;

        ItemStack item = isPlayerHoldingSonic(client.player);

        if (sonicIsInUse(item)) {
            this.startIfNotPlaying(getSonicSound());
            getSonicSound().setPitch(-(client.player.getPitch() / 90f) + 1f);
        } else {
            this.stopSound(SONIC_SOUND);
        }
    }
}