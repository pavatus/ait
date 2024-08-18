package loqor.ait.tardis.data.landing;

import java.util.*;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.travel.TravelHandler;

public class LandingPadManager extends PersistentState {
    private final HashMap<Long, LandingPadRegion> regions; // TODO - they arent per-world right now

    public LandingPadManager() {
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

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        regions.values().forEach(pad -> nbt.put(pad.toLong().toString(), pad.serialize()));

        return nbt;
    }

    public static LandingPadManager getInstance(MinecraftServer server) {
        PersistentStateManager manager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        LandingPadManager state = manager.getOrCreate(
                LandingPadManager::loadNbt,
                LandingPadManager::new,
                AITMod.MOD_ID + "_landing_pad"
        );

        state.markDirty();

        return state;
    }
    public static LandingPadManager getInstance(World world) {
        return getInstance(world.getServer());
    }

    private static LandingPadManager loadNbt(NbtCompound data) {
        LandingPadManager created = new LandingPadManager();

        data.getKeys().forEach(key -> {
            LandingPadRegion pad = new LandingPadRegion(data.getCompound(key));
            created.regions.put(pad.toLong(), pad);
        });

        return created;
    }
    public static class LandingPadRegion implements LandingPadListener {
        private final ChunkPos chunk;
        private final int maxSpots;
        private final int maxPerRow;
        private final List<LandingPadSpot> spots;
        private final Queue<LandingPadSpot> free;

        public LandingPadRegion(ChunkPos chunk) {
            this.chunk = chunk;
            // for now just assume 16x16
            this.maxSpots = getMaxSpots(16, 16);
            this.maxPerRow = 16 / 2;
            this.spots = new ArrayList<>();
            this.free = new LinkedList<>();
        }
        public LandingPadRegion(NbtCompound data) {
            this(new ChunkPos(data.getLong("Chunk")));
        }

        private static int getMaxSpots(int sizeX, int sizeY) {
            return (sizeX * sizeY) / 4;
        }

        public Optional<LandingPadSpot> getNextSpot() {
            if (!this.free.isEmpty()) {
                return Optional.of(this.free.poll());
            }

            if (this.spots.size() >= this.maxSpots) {
                return Optional.empty();
            }

            return Optional.of(this.generateSpot());
        }
        private LandingPadSpot createSpot() {
            LandingPadSpot created;

            if (this.spots.isEmpty()) {
                created = new LandingPadSpot(new BlockPos(this.chunk.getStartX() + 1, 64, this.chunk.getStartZ() + 1));
                return created;
            }

            LandingPadSpot last = this.spots.get(this.spots.size() - 1);

            float rowCount = ((float) this.spots.size() / this.maxPerRow);
            boolean isNewRow = rowCount == Math.round(rowCount);

            if (!isNewRow) {
                created = new LandingPadSpot(new BlockPos(last.getPos().getX() + 2, last.getPos().getY(), last.getPos().getZ()));
                return created;
            }

            LandingPadSpot first = this.spots.get(0);

            created = new LandingPadSpot(new BlockPos(first.getPos().getX(), first.getPos().getY(), last.getPos().getZ() + 2));

            return created;
        }
        private LandingPadSpot generateSpot() {
            LandingPadSpot created = this.createSpot();

            this.spots.add(created);
            this.free.add(created);
            created.listen(this);

            return created;
        }

        @Override
        public void onClaim(LandingPadSpot spot) {

        }
        @Override
        public void onFree(LandingPadSpot spot) {
            this.free.add(spot);
        }

        public Long toLong() {
            return this.chunk.toLong();
        }

        public NbtCompound serialize() {
            NbtCompound data = new NbtCompound();

            data.putLong("Chunk", chunk.toLong());

            return data;
        }
        private void deserialize(NbtCompound data) {}
    }

    public static class LandingPadSpot {
        private final List<LandingPadListener> listeners; // todo a list probably isnt the best for this
        private final BlockPos pos;
        private Tardis tardis;

        public LandingPadSpot(BlockPos pos) {
            this.pos = pos;
            this.listeners = new ArrayList<>();
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public Optional<Tardis> getTardis() {
            return Optional.ofNullable(this.tardis);
        }

        public void claim(Tardis tardis) {
            if (this.tardis != null) {
                throw new IllegalStateException("Already occupied");
            }

            this.tardis = tardis;

            for (LandingPadListener listener : this.listeners) {
                listener.onClaim(this);
            }
        }
        public Optional<Tardis> release() {
            Tardis current = this.tardis;
            this.tardis = null;

            for (LandingPadListener listener : this.listeners) {
                listener.onFree(this);
            }
            return Optional.ofNullable(current);
        }
        public boolean isOccupied() {
            return this.tardis != null;
        }

        public void listen(LandingPadListener listener) {
            this.listeners.add(listener);
        }

        private void checkOccupant(World world) {
            if (checkOccupantFlight(world)) return;

            if (!(world.getBlockEntity(this.getPos()) instanceof ExteriorBlockEntity exterior)) {
                this.release();
                return;
            }

            if (exterior.tardis().isEmpty()) {
                // idk??
                return;
            }

            Tardis found = exterior.tardis().get();
            if (this.tardis == null) {
                this.claim(found);
            }

            if (!(this.tardis.equals(found))) {
                this.claim(found);
                return;
            }
        }

        /**
         * Checks occupancy flight to ensure theyre coming here
         * @return true if they are
         */
        private boolean checkOccupantFlight(World world) {
            if (this.tardis == null) return false;

            TravelHandler travel = this.tardis.travel();

            boolean inFlight = travel.inFlight();
            if (inFlight) {
                if (this.getPos().equals(travel.destination().getPos())) return true;
                this.release();
                return false;
            }

            return false;
        }
    }

    public interface LandingPadListener {
        void onClaim(LandingPadSpot spot);
        void onFree(LandingPadSpot spot);
    }
}
