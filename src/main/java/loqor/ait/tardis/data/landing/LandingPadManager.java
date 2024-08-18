package loqor.ait.tardis.data.landing;

import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.*;
import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.util.TardisUtil;

public class LandingPadManager extends PersistentState {
    private final ServerWorld world;
    private final HashMap<Long, Region> regions;

    public LandingPadManager(ServerWorld world) {
        this.world = world;
        this.regions = new HashMap<>();
    }

    private Optional<Region> getRegion(Long pos) {
        return Optional.ofNullable(regions.get(pos));
    }
    private Optional<Region> getRegion(ChunkPos pos) {
        return this.getRegion(pos.toLong());
    }
    public Optional<Region> getRegion(BlockPos pos) {
        return this.getRegion(new ChunkPos(pos));
    }

    private Region claim(ChunkPos pos) {
        Long longPos = pos.toLong();

        if (regions.containsKey(longPos)) {
            throw new IllegalStateException("Region already occupied");
        }

        Region created = new Region(pos);
        regions.put(longPos, created);

        return created;
    }
    public Region claim(BlockPos pos) {
        return this.claim(new ChunkPos(pos));
    }

    private Region release(Long pos) {
        if (!this.regions.containsKey(pos)) {
            return null; // lol no exception
        }


        Region released = this.regions.remove(pos);

        released.onRemoved();

        return released;
    }
    private Region release(ChunkPos pos) {
        return this.release(pos.toLong());
    }
    public Region release(BlockPos pos) {
        return this.release(new ChunkPos(pos));
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();

        for(Region region : regions.values()) {
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

        for (int i = 0; i < list.size(); i++) {
            NbtCompound regionData = list.getCompound(i);
            Region pad = new Region(regionData, world);
            created.regions.put(pad.toLong(), pad);
        }

        return created;
    }

    public static class Region implements SpotListener {
        private final ChunkPos chunk;
        private final int maxSpots;
        private final int maxPerRow;
        private final List<Spot> spots;
        private final Queue<Spot> free;
        @Exclude
        private final List<RegionListener> listeners; // todo a list probably isnt the best for this

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

            for (RegionListener listener : this.listeners) {
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

            for (RegionListener listener : this.listeners) {
                listener.onRegionRemoved();
            }
        }

        public void addListener(RegionListener listener) {
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
                created.verify(world);
            }
        }
    }

    public static class Spot {
        @Exclude
        private final List<SpotListener> listeners; // todo a list probably isnt the best for this
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

            for (SpotListener listener : this.listeners) {
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

            for (SpotListener listener : this.listeners) {
                listener.onFree(this);
            }

            return Optional.ofNullable(current);
        }
        public boolean isOccupied() {
            return this.tardis != null;
        }

        public void addListener(SpotListener listener) {
            this.listeners.add(listener);
        }

        public void verify(World world) {
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

    public interface SpotListener {
        void onClaim(Spot spot);
        void onFree(Spot spot);
    }
    public interface RegionListener {
        void onAdd(Spot spot);
        void onRegionRemoved();
    }
}
