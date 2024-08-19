package loqor.ait.tardis.data.landing;

import java.util.HashMap;
import java.util.Optional;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import loqor.ait.AITMod;

public class LandingPadManager extends PersistentState implements LandingPadRegion.Listener {
    private final ServerWorld world;
    private final HashMap<Long, LandingPadRegion> regions;

    public LandingPadManager(ServerWorld world) {
        this.world = world;
        this.regions = new HashMap<>();
    }

    private Optional<LandingPadRegion> getRegion(Long pos) {
        return Optional.ofNullable(regions.get(pos));
    }

    private Optional<LandingPadRegion> getRegion(ChunkPos pos) {
        return this.getRegion(pos.toLong());
    }

    public Optional<LandingPadRegion> getRegion(BlockPos pos) {
        return this.getRegion(new ChunkPos(pos));
    }

    private LandingPadRegion claim(ChunkPos pos) {
        Long longPos = pos.toLong();

        if (regions.containsKey(longPos)) {
            throw new IllegalStateException("Region already occupied");
        }

        LandingPadRegion created = new LandingPadRegion(pos);
        regions.put(longPos, created);

        created.addListener(this);

        return created;
    }

    public LandingPadRegion claim(BlockPos pos) {
        return this.claim(new ChunkPos(pos));
    }

    private LandingPadRegion release(Long pos) {
        if (!this.regions.containsKey(pos)) {
            return null; // lol no exception
        }


        LandingPadRegion released = this.regions.remove(pos);

        released.onRemoved();

        return released;
    }

    private LandingPadRegion release(ChunkPos pos) {
        return this.release(pos.toLong());
    }

    public LandingPadRegion release(BlockPos pos) {
        return this.release(new ChunkPos(pos));
    }

    @Override
    public void onAdd(LandingPadRegion region, LandingPadSpot spot) {
        Network.toTracking(Network.Action.ADD, this.world, region);
    }

    @Override
    public void onRegionRemoved(LandingPadRegion region) {
        Network.toTracking(Network.Action.REMOVE, this.world, region);
    }

    @Override
    public void onClaim(LandingPadRegion region, LandingPadSpot spot) {
        Network.toTracking(Network.Action.ADD, this.world, region);
    }

    @Override
    public void onFree(LandingPadRegion region, LandingPadSpot spot) {
        Network.toTracking(Network.Action.ADD, this.world, region);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();

        for (LandingPadRegion region : regions.values()) {
            list.add(region.serialize());
        }

        nbt.put("Regions", list);

        return nbt;
    }

    public static LandingPadManager getInstance(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();

        LandingPadManager state = manager.getOrCreate(
                (data) -> LandingPadManager.loadNbt(world, data),
                () -> new LandingPadManager(world),
                AITMod.MOD_ID + "_landing_pad"
        );

        state.markDirty();

        return state;
    }

    private static LandingPadManager loadNbt(ServerWorld world, NbtCompound data) {
        LandingPadManager created = new LandingPadManager(world);

        NbtList list = data.getList("Regions", NbtElement.COMPOUND_TYPE);

        for (NbtElement nbt : list) {
            LandingPadRegion pad = new LandingPadRegion((NbtCompound) nbt, world);
            created.regions.put(pad.toLong(), pad);
        }

        return created;
    }


    public static class Network {
        public static final Identifier SYNC = new Identifier(AITMod.MOD_ID, "landingpad_sync");
        public static final Identifier REQUEST = new Identifier(AITMod.MOD_ID, "landingpad_request");

        private static void toPlayer(Network.Action action, RegistryKey<World> world, Long chunk, LandingPadRegion region, ServerPlayerEntity player) {
            NbtCompound data = new NbtCompound();

            data.putInt("Type", action.ordinal());
            data.putString("World", world.getValue().toString());
            data.putLong("Chunk", chunk);

            if (action == Network.Action.ADD)
                data.put("Region", region.serialize());

            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeNbt(data);

            ServerPlayNetworking.send(player, SYNC, buf);
        }

        private static void toPlayer(Network.Action action, RegistryKey<World> world, LandingPadRegion region, ServerPlayerEntity player) {
            toPlayer(action, world, region.toLong(), region, player);
        }

        public static void toPlayer(Network.Action action, ServerWorld world, LandingPadRegion region, ServerPlayerEntity player) {
            toPlayer(action, world.getRegistryKey(), region, player);
        }
        public static void toPlayer(Action action, ServerPlayerEntity player) {
            ServerWorld world = player.getServerWorld();
            LandingPadManager manager = LandingPadManager.getInstance(world);
            LandingPadRegion region = manager.getRegion(player.getBlockPos()).orElse(null);

            if (region == null) return;

            toPlayer(action, world, region, player);
        }

        public static void toWorld(Network.Action action, ServerWorld world, LandingPadRegion region) {
            for (ServerPlayerEntity player : world.getPlayers()) {
                toPlayer(action, world.getRegistryKey(), region, player);
            }
        }

        public static void toTracking(Network.Action action, ServerWorld world, LandingPadRegion region) {
            for (ServerPlayerEntity player : PlayerLookup.tracking(world, new ChunkPos(region.toLong()))) {
                toPlayer(action, world.getRegistryKey(), region.toLong(), region, player);
            }
        }

        public static void toAll(Network.Action action, ServerWorld world, LandingPadRegion region) {
            for (ServerPlayerEntity player : world.getServer().getPlayerManager().getPlayerList()) {
                toPlayer(action, world.getRegistryKey(), region, player);
            }
        }

        public static void toPlayer(LandingPadManager manager, ServerPlayerEntity player) {
            toPlayer(Network.Action.CLEAR, manager.world.getRegistryKey(), Long.valueOf("1"), null, player);

            for (LandingPadRegion region : manager.regions.values()) {
                toPlayer(Network.Action.ADD, manager.world.getRegistryKey(), region, player);
            }
        }

        public static void toWorld(LandingPadManager manager) {
            for (ServerPlayerEntity player : manager.world.getPlayers()) {
                toPlayer(manager, player);
            }
        }

        public static void toAll(LandingPadManager manager) {
            for (ServerPlayerEntity player : manager.world.getServer().getPlayerManager().getPlayerList()) {
                toPlayer(manager, player);
            }
        }

        static {
            ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                LandingPadManager manager = LandingPadManager.getInstance(handler.getPlayer().getServerWorld());

                toPlayer(manager, handler.getPlayer());
            });
            ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
                LandingPadManager manager = LandingPadManager.getInstance(destination);

                toPlayer(manager, player);
            });
        }

        public enum Action {
            ADD,
            REMOVE,
            CLEAR
        }
    }
}
