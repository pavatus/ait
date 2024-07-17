package loqor.ait.core.sounds.sonic;

import loqor.ait.AITMod;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.UUID;

/**
 * @author Loqor
 * the reasoning behind this class is basically just a simple handler that is external to the entire tardis system
 * its specifically and only for updating client information from the server
 */

public class ServerSonicSoundHandler {
    public static final Identifier SEND = new Identifier(AITMod.MOD_ID, "send_sonic_use");
    private boolean shouldPlay;
    private UUID playerUUID;

    public ServerSonicSoundHandler() {}

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

        ServerPlayerEntity entity = server.getPlayerManager().getPlayer(this.getPlayerUUID());

        if (entity == null) return;

        ServerWorld world = entity.getServerWorld();

        List<ServerPlayerEntity> list = world.getPlayers();

        for (ServerPlayerEntity player : list) {
            ServerPlayNetworking.send(player, SEND, buf);
        }
    }
}
