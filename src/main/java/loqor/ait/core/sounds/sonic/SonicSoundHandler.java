package loqor.ait.core.sounds.sonic;

import loqor.ait.AITMod;
import loqor.ait.client.sounds.PositionedLoopingSound;
import loqor.ait.registry.impl.HumsRegistry;
import loqor.ait.tardis.base.TardisComponent;

import loqor.ait.tardis.sound.HumSound;
import loqor.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class SonicSoundHandler {
    public static final Identifier SEND = new Identifier(AITMod.MOD_ID, "send_sonic_use");
    private boolean shouldPlay;
    private UUID playerUUID;

    public SonicSoundHandler() {}

    public boolean shouldPlay() {
        return this.shouldPlay;
    }

    public void setPlaying(boolean shouldPlay, MinecraftServer server) {
        this.shouldPlay = shouldPlay;

        this.updateClientSonicStuff(server);
    }

    public void setPlayerIdAndServer(UUID playerid, MinecraftServer server) {
        this.playerUUID = playerid;

        this.updateClientSonicStuff(server);
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    private void updateClientSonicStuff(MinecraftServer server) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBoolean(this.shouldPlay());
        buf.writeUuid(this.getPlayerUUID());

        for (ServerPlayerEntity entity : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(entity, SEND, buf);
        }
    }
}
