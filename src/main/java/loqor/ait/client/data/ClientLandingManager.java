package loqor.ait.client.data;

import loqor.ait.tardis.util.NetworkUtil;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

import loqor.ait.tardis.data.landing.LandingPadManager;
import loqor.ait.tardis.data.landing.LandingPadRegion;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ClientLandingManager {

    private static ClientLandingManager instance;

    public static void init() {
        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client)
                -> ClientLandingManager.getInstance().invalidate()));

        ClientChunkEvents.CHUNK_LOAD.register((world, chunk)
                -> ClientLandingManager.getInstance().request(world, chunk));

        ClientChunkEvents.CHUNK_UNLOAD.register((world, chunk)
                -> ClientLandingManager.getInstance().remove(chunk.getPos()));

        ClientPlayNetworking.registerGlobalReceiver(LandingPadManager.Network.SYNC, (client, handler, buf, responseSender) -> {
            ClientLandingManager.getInstance().receive(buf);
        });

        instance = new ClientLandingManager();
    }

    public static ClientLandingManager getInstance() {
        return instance;
    }

    private final Map<ChunkPos, LandingPadRegion> regions = new HashMap<>();

    public @Nullable LandingPadRegion getRegion(ChunkPos pos) {
        return this.regions.get(pos);
    }

    public void receive(PacketByteBuf buf) {
        LandingPadManager.Network.Action action = buf.readEnumConstant(LandingPadManager.Network.Action.class);
        ChunkPos chunkPos = buf.readChunkPos();

        switch (action) {
            case ADD -> this.regions.put(chunkPos, NetworkUtil.receive(LandingPadRegion.CODEC, buf));
            case REMOVE -> this.remove(chunkPos);
            case CLEAR -> this.invalidate();
        }
    }

    private void invalidate() {
        this.regions.clear();
    }

    private void remove(ChunkPos pos) {
        System.out.println("Removing: " + pos);
        this.regions.remove(pos);
    }

    private void request(RegistryKey<World> world, long chunk) {
        NbtCompound data = new NbtCompound();

        data.putString("World", world.getValue().toString());
        data.putLong("Chunk", chunk);

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(data);

        ClientPlayNetworking.send(LandingPadManager.Network.REQUEST, buf);
    }

    private void request(ClientWorld world, WorldChunk chunk) {
        this.request(world.getRegistryKey(), chunk.getPos().toLong());
    }
}
