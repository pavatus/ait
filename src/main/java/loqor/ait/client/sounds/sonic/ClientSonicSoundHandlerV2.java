package loqor.ait.client.sounds.sonic;

import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.core.AITSounds;
import loqor.ait.core.item.SonicItem;
import loqor.ait.core.sounds.sonic.SonicSoundHandler;
import loqor.ait.tardis.util.SoundHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class ClientSonicSoundHandlerV2 extends SoundHandler {
    public static PositionedLoopingSound SONIC_SOUND;
    private boolean shouldPlay;
    private PlayerEntity player;

    protected ClientSonicSoundHandlerV2() {
        ClientPlayNetworking.registerGlobalReceiver(SonicSoundHandler.SEND,
                (client, handler, buf, responseSender) -> {
                    if (client.world == null) return;
                    boolean shouldPlay = buf.readBoolean();
                    PlayerEntity entity = client.world.getPlayerByUuid(buf.readUuid());

                    this.shouldPlay(shouldPlay);
                    this.setPlayer(entity);
                });
    }

    public PositionedLoopingSound getSonicSound() {

        if (SONIC_SOUND == null)
            SONIC_SOUND = new PositionedLoopingSound(AITSounds.SONIC_USE,
                    SoundCategory.PLAYERS,
                    new BlockPos(0, 0, 0), 1f, 1f);

        return SONIC_SOUND;
    }

    public static ClientSonicSoundHandlerV2 create() {
        if (MinecraftClient.getInstance().player == null) return null;

        ClientSonicSoundHandlerV2 handler = new ClientSonicSoundHandlerV2();
        handler.generate();
        return handler;
    }

    private void generate() {

        if (SONIC_SOUND == null)
            SONIC_SOUND = new PositionedLoopingSound(AITSounds.SONIC_USE,
                SoundCategory.PLAYERS,
                    new BlockPos(0, 0, 0), 1f, 1f);

        this.sounds = new ArrayList<>();
        this.sounds.add(
                SONIC_SOUND
        );
    }

    public boolean shouldPlay() {
        return this.shouldPlay;
    }

    public boolean playerUsingSonic() {
        ItemStack handStack = this.getPlayer().getMainHandStack();
        if (handStack == null || !(handStack.getItem() instanceof SonicItem)) return false;
        NbtCompound nbt = this.getPlayer().getMainHandStack().getOrCreateNbt();

        if (nbt.contains(SonicItem.MODE_KEY)) {
            return nbt.getInt(SonicItem.MODE_KEY) != 0;
        }

        return false;
    }

    public void shouldPlay(boolean shouldPlay) {
        this.shouldPlay = shouldPlay;
    }

    public void setPlayer(PlayerEntity entity) {
        this.player = entity;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    public void tick(MinecraftClient client) {
        if (this.sounds == null)
            this.generate();

        if (this.getPlayer() != null && this.shouldPlay() && playerUsingSonic()) {
            this.startIfNotPlaying(getSonicSound());
            getSonicSound().setPosition(this.getPlayer().getBlockPos());
            getSonicSound().setPitch(-(this.getPlayer().getPitch() / 90f) + 1f);
        } else {
            this.stopSound(SONIC_SOUND);
        }
    }
}