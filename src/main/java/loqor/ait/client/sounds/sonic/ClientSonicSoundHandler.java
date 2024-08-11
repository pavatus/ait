package loqor.ait.client.sounds.sonic;

import java.util.List;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;

import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.core.AITSounds;
import loqor.ait.core.item.SonicItem;
import loqor.ait.core.sounds.sonic.ServerSonicSoundHandler;
import loqor.ait.tardis.util.SoundHandler;

public class ClientSonicSoundHandler extends SoundHandler {

    public static PositionedLoopingSound SONIC_SOUND;

    private boolean shouldPlay;
    private PlayerEntity player;

    protected ClientSonicSoundHandler() {
        ClientPlayNetworking.registerGlobalReceiver(ServerSonicSoundHandler.SEND,
                (client, handler, buf, responseSender) -> {
                    if (client.world == null)
                        return;
                    boolean shouldPlay = buf.readBoolean();
                    PlayerEntity entity = client.world.getPlayerByUuid(buf.readUuid());

                    this.shouldPlay(shouldPlay);
                    this.setPlayer(entity);
                });
    }

    public PositionedLoopingSound getSonicSound() {
        if (SONIC_SOUND == null)
            SONIC_SOUND = this.createSonicSound();

        return SONIC_SOUND;
    }

    private PositionedLoopingSound createSonicSound() {
        return new PositionedLoopingSound(AITSounds.SONIC_USE, SoundCategory.PLAYERS, new BlockPos(0, 0, 0), 1f, 1f);
    }

    public static ClientSonicSoundHandler create() {
        ClientSonicSoundHandler handler = new ClientSonicSoundHandler();

        handler.generate();
        return handler;
    }

    private void generate() {
        if (SONIC_SOUND == null)
            SONIC_SOUND = this.createSonicSound();

        this.sounds = List.of(SONIC_SOUND);
    }

    public boolean shouldPlay() {
        return this.shouldPlay;
    }

    public boolean playerUsingSonic() {
        ItemStack handStack = this.getPlayer().getMainHandStack();

        if (handStack == null || !(handStack.getItem() instanceof SonicItem))
            return false;

        return SonicItem.findModeInt(handStack) != 0;
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
            this.startIfNotPlaying(this.getSonicSound());

            this.getSonicSound().setPosition(this.getPlayer().getBlockPos());
            this.getSonicSound().setPitch(-(this.getPlayer().getPitch() / 90f) + 1f);
        } else {
            this.stopSound(SONIC_SOUND);
        }
    }
}
