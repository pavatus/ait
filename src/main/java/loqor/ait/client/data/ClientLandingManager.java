package loqor.ait.client.data;

import java.util.HashMap;
import java.util.Optional;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import loqor.ait.tardis.data.landing.LandingPadManager;
import loqor.ait.tardis.data.landing.LandingPadRegion;

public class ClientLandingManager {
    private final MinecraftClient client;

    static {
        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> {
            ClientLandingManager.getInstance().regions.clear();
        }));
    }

    private final HashMap<RegistryKey<World>, HashMap<Long, LandingPadRegion>> regions; // world -> chunk long -> region

    public ClientLandingManager() {
        this.regions = new HashMap<>();
        this.client = MinecraftClient.getInstance();
    }

    private HashMap<Long, LandingPadRegion> getRegions(RegistryKey<World> world) {
        return this.regions.computeIfAbsent(world, w -> new HashMap<>());
    }
    private Optional<LandingPadRegion> getRegion(RegistryKey<World> world, long chunk) {
        return Optional.ofNullable(this.getRegions(world).get(chunk));
    }
    public Optional<LandingPadRegion> getRegion(ClientWorld world, BlockPos pos) {
        return this.getRegion(world.getRegistryKey(), new ChunkPos(pos).toLong());
    }

    private void addRegion(RegistryKey<World> world, long chunk, LandingPadRegion region) {
        this.getRegions(world).put(chunk, region);
    }
    private void addRegion(RegistryKey<World> world, LandingPadRegion region) {
        this.addRegion(world, region.toLong(), region);
    }
    public void addRegion(ClientWorld world, LandingPadRegion region) {
        this.addRegion(world.getRegistryKey(), region);
    }

    private void removeRegion(RegistryKey<World> world, long chunk) {
        this.getRegions(world).remove(chunk);
    }
    private void removeRegion(RegistryKey<World> world, LandingPadRegion region) {
        this.removeRegion(world, region.toLong());
    }
    public void removeRegion(ClientWorld world, LandingPadRegion region) {
        this.removeRegion(world.getRegistryKey(), region);
    }

    private void clearWorld(RegistryKey<World> world) {
        this.getRegions(world).clear();
    }

    private void receive(NbtCompound data) {
        RegistryKey<World> world = RegistryKey.of(RegistryKeys.WORLD, new Identifier(data.getString("World")));
        long chunk = data.getLong("Chunk");

        int type = data.getInt("Type");
        LandingPadManager.Network.Action action = LandingPadManager.Network.Action.values()[type];

        if (action == LandingPadManager.Network.Action.CLEAR) {
            this.clearWorld(world);
            return;
        }

        if (action == LandingPadManager.Network.Action.REMOVE) {
            this.removeRegion(world, chunk);
            return;
        }

        if (action == LandingPadManager.Network.Action.ADD) {
            NbtCompound region = data.getCompound("Region");

            this.addRegion(world, chunk, new LandingPadRegion(region, null));
        }
    }

    public void receive(PacketByteBuf buf) {
        this.receive(buf.readNbt());
    }

    private static ClientLandingManager instance;

    public static ClientLandingManager getInstance() {
        if (instance == null) {
            instance = new ClientLandingManager();
        }
        return instance;
    }
}
