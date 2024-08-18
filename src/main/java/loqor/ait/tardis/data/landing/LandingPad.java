package loqor.ait.tardis.data.landing;

import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.*;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;

public class LandingPad { // split this up into multiple files..?
    public static class Manager extends PersistentState {
        private final ServerWorld world;
        private final HashMap<Long, LandingPad.Region> regions;

        public Manager(ServerWorld world) {
            this.world = world;
            this.regions = new HashMap<>();
        }

        private Optional<LandingPad.Region> getRegion(Long pos) {
            return Optional.ofNullable(regions.get(pos));
        }
        private Optional<LandingPad.Region> getRegion(ChunkPos pos) {
            return this.getRegion(pos.toLong());
        }
        public Optional<LandingPad.Region> getRegion(BlockPos pos) {
            return this.getRegion(new ChunkPos(pos));
        }

        private LandingPad.Region claim(ChunkPos pos) {
            Long longPos = pos.toLong();

            if (regions.containsKey(longPos)) {
                throw new IllegalStateException("Region already occupied");
            }

            LandingPad.Region created = new LandingPad.Region(pos);
            regions.put(longPos, created);

            Network.toAll(Network.Action.ADD, this.world, created);

            return created;
        }
        public LandingPad.Region claim(BlockPos pos) {
            return this.claim(new ChunkPos(pos));
        }

        private LandingPad.Region release(Long pos) {
            if (!this.regions.containsKey(pos)) {
                return null; // lol no exception
            }


            LandingPad.Region released = this.regions.remove(pos);

            released.onRemoved();

            Network.toAll(Network.Action.REMOVE, this.world, released);

            return released;
        }
        private LandingPad.Region release(ChunkPos pos) {
            return this.release(pos.toLong());
        }
        public LandingPad.Region release(BlockPos pos) {
            return this.release(new ChunkPos(pos));
        }

        @Override
        public NbtCompound writeNbt(NbtCompound nbt) {
            NbtList list = new NbtList();

            for(LandingPad.Region region : regions.values()) {
                list.add(region.serialize());
            }

            nbt.put("Regions", list);

            return nbt;
        }

        public static Manager getInstance(ServerWorld world) {
            PersistentStateManager manager = world.getPersistentStateManager();

            Manager state = manager.getOrCreate(
                    (data) -> Manager.loadNbt(world, data),
                    () -> new Manager(world),
                    AITMod.MOD_ID + "_landing_pad"
            );

            state.markDirty();

            return state;
        }

        private static Manager loadNbt(ServerWorld world, NbtCompound data) {
            Manager created = new Manager(world);

            NbtList list = data.getList("Regions", NbtElement.COMPOUND_TYPE);

            for (int i = 0; i < list.size(); i++) {
                NbtCompound regionData = list.getCompound(i);
                LandingPad.Region pad = new LandingPad.Region(regionData, world);
                created.regions.put(pad.toLong(), pad);
            }

            return created;
        }

        public static class Network { // TODO - optimise network logic, rn it just sends EVERYTHING to EVERYONE (very bad)
            public static final Identifier SYNC = new Identifier(AITMod.MOD_ID, "landingpad_sync");

            private static void toPlayer(Action action, RegistryKey<World> world, Long chunk, LandingPad.Region region, ServerPlayerEntity player) {
                NbtCompound data = new NbtCompound();

                data.putInt("Type", action.ordinal());
                data.putString("World", world.getValue().toString());
                data.putLong("Chunk", chunk);

                if (action == Action.ADD)
                    data.put("Region", region.serialize());

                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeNbt(data);

                ServerPlayNetworking.send(player, SYNC, buf);
            }
            private static void toPlayer(Action action, RegistryKey<World> world, LandingPad.Region region, ServerPlayerEntity player) {
                toPlayer(action, world, region.toLong(), region, player);
            }
            public static void toPlayer(Action action, ServerWorld world, LandingPad.Region region, ServerPlayerEntity player) {
                toPlayer(action, world.getRegistryKey(), region, player);
            }

            public static void toWorld(Action action, ServerWorld world, LandingPad.Region region) {
                for (ServerPlayerEntity player : world.getPlayers()) {
                    toPlayer(action, world.getRegistryKey(), region, player);
                }
            }
            public static void toTracking(Action action, ServerWorld world, LandingPad.Region region) {
                for (ServerPlayerEntity player : PlayerLookup.tracking(world, new ChunkPos(region.toLong()))) {
                    toPlayer(action, world.getRegistryKey(), region.toLong(), region, player);
                }
            }
            public static void toAll(Action action, ServerWorld world, LandingPad.Region region) {
                for (ServerPlayerEntity player : world.getServer().getPlayerManager().getPlayerList()) {
                    toPlayer(action, world.getRegistryKey(), region, player);
                }
            }

            public static void toPlayer(Manager manager, ServerPlayerEntity player) {
                toPlayer(Action.CLEAR, manager.world.getRegistryKey(), Long.valueOf("1"), null, player);

                for (LandingPad.Region region : manager.regions.values()) {
                    toPlayer(Action.ADD, manager.world.getRegistryKey(), region, player);
                }
            }
            public static void toWorld(Manager manager) {
                for (ServerPlayerEntity player : manager.world.getPlayers()) {
                    toPlayer(manager, player);
                }
            }
            public static void toAll(Manager manager) {
                for (ServerPlayerEntity player : manager.world.getServer().getPlayerManager().getPlayerList()) {
                    toPlayer(manager, player);
                }
            }
            static {
                ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
                    Manager manager = Manager.getInstance(handler.getPlayer().getServerWorld());

                    toPlayer(manager, handler.getPlayer());
                });
                ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
                    Manager manager = Manager.getInstance(destination);

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

    public static class Region implements Spot.Listener {
        private final ChunkPos chunk;
        private final int maxSpots;
        private final int maxPerRow;
        private final List<Spot> spots;
        private final Queue<Spot> free;
        @Exclude
        private final List<Listener> listeners; // todo a list probably isnt the best for this

        public Region(ChunkPos chunk) {
            this.chunk = chunk;
            // for now just assume 16x16
            this.maxSpots = getMaxSpots(16, 16);
            this.maxPerRow = 16 / 2;
            this.spots = new ArrayList<>();
            this.free = new LinkedList<>();
            this.listeners = new ArrayList<>();
        }
        public Region(NbtCompound data, @Nullable ServerWorld world) {
            this(new ChunkPos(data.getLong("Chunk")));

            this.deserialize(data, world);
        }

        private static int getMaxSpots(int sizeX, int sizeY) {
            return (sizeX * sizeY) / 4;
        }

        public Optional<Spot> getNextSpot() {
            if (!this.free.isEmpty()) {
                return Optional.of(this.free.poll());
            }

            if (this.spots.size() >= this.maxSpots) {
                return Optional.empty();
            }

            return Optional.of(this.generateSpot(false));
        }
        private Spot createSpot() {
            Spot created;

            if (this.spots.isEmpty()) {
                created = new Spot(new BlockPos(this.chunk.getStartX() + 1, 64, this.chunk.getStartZ() + 1));
                return created;
            }

            Spot last = this.spots.get(this.spots.size() - 1);

            float rowCount = ((float) this.spots.size() / this.maxPerRow);
            boolean isNewRow = rowCount == Math.round(rowCount);

            if (!isNewRow) {
                created = new Spot(new BlockPos(last.getPos().getX() + 2, last.getPos().getY(), last.getPos().getZ()));
                return created;
            }

            Spot first = this.spots.get(0);

            created = new Spot(new BlockPos(first.getPos().getX(), first.getPos().getY(), last.getPos().getZ() + 2));

            return created;
        }
        private Spot generateSpot(boolean isFree) {
            Spot created = this.createSpot();

            this.spots.add(created);
            created.addListener(this);

            if (isFree)
                this.free.add(created);

            for (Listener listener : this.listeners) {
                listener.onAdd(created);
            }

            return created;
        }

        @Override
        public void onClaim(Spot spot) {

        }
        @Override
        public void onFree(Spot spot) {
            this.free.add(spot);
        }

        public void onRemoved() {
            for (Spot spot : this.spots) {
                spot.release(true);
            }

            for (Listener listener : this.listeners) {
                listener.onRegionRemoved();
            }
        }

        public void addListener(Listener listener) {
            this.listeners.add(listener);
        }

        public Long toLong() {
            return this.chunk.toLong();
        }

        public NbtCompound serialize() {
            NbtCompound data = new NbtCompound();

            data.putLong("Chunk", chunk.toLong());

            NbtList spots = new NbtList();

            for (Spot spot : this.spots) {
                spots.add(spot.serialize());
            }

            data.put("Spots", spots);

            return data;
        }
        private void deserialize(NbtCompound data, @Nullable ServerWorld world) {
            NbtCompound spots = data.getCompound("Spots");

            for (String key : spots.getKeys()) {
                Spot created = new Spot(spots.getCompound(key));
                created.addListener(this);
                this.spots.add(created);

                if (world != null)
                    created.verify(world);
            }
        }

        public interface Listener {
            void onAdd(Spot spot);
            void onRegionRemoved();
        }
    }

    public static class Spot {
        @Exclude
        private final List<Listener> listeners; // todo a list probably isnt the best for this
        private final BlockPos pos;
        private Tardis tardis;

        public Spot(BlockPos pos) {
            this.pos = pos;
            this.listeners = new ArrayList<>();
        }
        public Spot(NbtCompound data) {
            this(NbtHelper.toBlockPos(data.getCompound("Pos")));

            this.deserialize(data);
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public Optional<Tardis> getTardis() {
            return Optional.ofNullable(this.tardis);
        }

        public void claim(Tardis tardis, boolean updateTardis) {
            if (this.isOccupied() && !this.tardis.equals(tardis)) {
                throw new IllegalStateException("Spot already occupied");
            }

            this.tardis = tardis;

            if (updateTardis)
                this.tardis.landingPad().claim(this, false);

            for (Listener listener : this.listeners) {
                listener.onClaim(this);
            }
        }
        public Optional<Tardis> release(boolean updateTardis) {
            Tardis current = this.tardis;

            if (current != null) {
                if (updateTardis)
                    current.landingPad().release(false);
            }

            this.tardis = null;

            for (Listener listener : this.listeners) {
                listener.onFree(this);
            }

            return Optional.ofNullable(current);
        }
        public boolean isOccupied() {
            return this.tardis != null;
        }

        public void addListener(Listener listener) {
            this.listeners.add(listener);
        }

        public void verify(World world) {
            if (world == null) return;
            if (this.isOccupied()) return;

            if(!(world.getBlockEntity(this.getPos()) instanceof ExteriorBlockEntity exterior)) return;
            if (exterior.tardis().isEmpty()) return;

            this.claim(exterior.tardis().get(), true);
        }
        public NbtCompound serialize() {
            NbtCompound data = new NbtCompound();

            if (this.tardis != null)
                data.putUuid("Tardis", this.tardis.getUuid());

            data.put("Pos", NbtHelper.fromBlockPos(this.pos));

            return data;
        }
        private void deserialize(MinecraftServer server, NbtCompound data) {
            // causes crash - is set elsewhere instead
            /*
            if (data.contains("Tardis")) {
                ServerTardisManager.getInstance().getTardis(server, data.getUuid("Tardis"), tardis -> {
                    this.tardis = tardis;
                });
            }
             */
        }
        private void deserialize(NbtCompound data) {
            // uhh
            this.deserialize(TardisUtil.getOverworld().getServer(), data);
        }

        public static Serializer serializer() {
            return new Serializer();
        }

        public interface Listener {
            void onClaim(Spot spot);
            void onFree(Spot spot);
        }

        public static class Serializer implements
                JsonSerializer<Spot>,
                JsonDeserializer<Spot> {

            @Override
            public Spot deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                NbtCompound data = context.deserialize(json, NbtCompound.class);
                return new Spot(data);
            }

            @Override
            public JsonElement serialize(Spot src, Type typeOfSrc, JsonSerializationContext context) {
                return context.serialize(src.serialize());
            }
        }
    }

    public static class Handler extends KeyedTardisComponent {
        @Exclude(strategy = Exclude.Strategy.NETWORK)
        private Spot current;

        static {
            TardisEvents.MAT.register(Handler::onMaterialise);
            TardisEvents.DEMAT.register(Handler::onDematerialise); // TODO - on enter flight event instead
        }

        public Handler() {
            super(Id.LANDING_PAD);
        }

        private static TardisEvents.Interaction onDematerialise(Tardis tardis) {
            return tardis.landingPad().onDematerialise();
        }
        private TardisEvents.Interaction onDematerialise() {
            if (this.current == null) return TardisEvents.Interaction.PASS;

            this.release(true);

            return TardisEvents.Interaction.PASS;
        }

        private static TardisEvents.Interaction onMaterialise(Tardis tardis) {
            return tardis.landingPad().onMaterialise();
        }

        private TardisEvents.Interaction onMaterialise() {
            this.update();

            return TardisEvents.Interaction.PASS;
        }

        private void update() {
            if (!(this.tardis() instanceof ServerTardis)) return;

            DirectedGlobalPos.Cached destination = this.tardis().travel().destination();

            World world = TardisUtil.getOverworld().getServer().getWorld(destination.getDimension()); // #getWorld from destination is always null..?

            Spot spot = findSpot(world, destination.getPos()).orElse(null);

            if (spot == null) return;

            this.claim(spot, true);
            this.tardis().travel().destination(destination.pos(this.current.getPos()));

            this.onAdjust(spot);
        }

        private void onAdjust(Spot spot) {
            TardisEvents.LANDING_PAD_ADJUST.invoker().onLandingPadAdjust(this.tardis(), spot);

            TardisUtil.sendMessageToInterior(this.tardis(), Text.translatable("tardis.message.landingpad.adjust"));
        }

        private Optional<Spot> findSpot(World world, BlockPos pos) {
            Region region = findRegion(world, pos).orElse(null);

            if (region == null) return Optional.empty();

            return region.getNextSpot();
        }
        private Optional<Region> findRegion(World world, BlockPos pos) {
            return Manager.getInstance((ServerWorld) world).getRegion(pos);
        }

        public Spot release(boolean updateSpot) {
            Spot spot = this.current;

            if (updateSpot) {
                this.current.release(false);
            }

            this.current = null;
            return spot;
        }
        public void claim(Spot spot, boolean updateSpot) {
            this.current = spot;

            if (updateSpot) {
                this.current.claim(this.tardis(), false);
            }
        }
    }
}
