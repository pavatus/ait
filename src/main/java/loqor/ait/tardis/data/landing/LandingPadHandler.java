package loqor.ait.tardis.data.landing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

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

public class LandingPadHandler extends PersistentState {
    private final HashMap<Long, LandingPadRegion> regions;

    public LandingPadHandler() {
        this.regions = new HashMap<>();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        regions.values().forEach(pad -> nbt.put(pad.toLong().toString(), pad.serialize()));

        return nbt;
    }

    public static LandingPadHandler getInstance(MinecraftServer server) {
        PersistentStateManager manager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        LandingPadHandler state = manager.getOrCreate(
                LandingPadHandler::loadNbt,
                LandingPadHandler::new,
                AITMod.MOD_ID + "_landing_pad"
        );

        state.markDirty();

        return state;
    }

    private static LandingPadHandler loadNbt(NbtCompound data) {
        LandingPadHandler created = new LandingPadHandler();

        data.getKeys().forEach(key -> {
            LandingPadRegion pad = new LandingPadRegion(data.getCompound(key));
            created.regions.put(pad.toLong(), pad);
        });

        return created;
    }
    public static class LandingPadRegion {
        private final ChunkPos chunk;
        private final int maxSpots;
        private final int maxPerRow;
        private final List<LandingPadSpot> spots;

        public LandingPadRegion(ChunkPos chunk) {
            this.chunk = chunk;
            // for now just assume 16x16
            this.maxSpots = getMaxSpots(16, 16);
            this.maxPerRow = 16 / 2;
            this.spots = new ArrayList<>();
        }
        public LandingPadRegion(NbtCompound data) {
            this(new ChunkPos(data.getLong("Chunk")));
        }

        private static int getMaxSpots(int sizeX, int sizeY) {
            return (sizeX * sizeY) / 4;
        }

        private Optional<LandingPadSpot> getNextSpot() {
            for (LandingPadSpot spot : this.spots) {
                if (!spot.isOccupied()) {
                    return Optional.of(spot);
                }
            }

            if (this.spots.size() >= this.maxSpots) {
                return Optional.empty();
            }

            return Optional.of(this.createSpot());
        }
        private LandingPadSpot createSpot() {
            LandingPadSpot created;

            if (this.spots.isEmpty()) {
                created = new LandingPadSpot(new BlockPos(this.chunk.getStartX() + 1, 64, this.chunk.getStartZ() + 1));
                this.spots.add(created);

                return created;
            }

            LandingPadSpot last = this.spots.get(this.spots.size() - 1);

            float rowCount = ((float) this.spots.size() / this.maxPerRow);
            boolean isNewRow = rowCount == Math.round(rowCount);

            if (!isNewRow) {
                created = new LandingPadSpot(new BlockPos(last.getPos().getX() + 2, last.getPos().getY(), last.getPos().getZ()));
                this.spots.add(created);
                return created;
            }

            LandingPadSpot first = this.spots.get(0);

            created = new LandingPadSpot(new BlockPos(first.getPos().getX(), first.getPos().getY(), last.getPos().getZ() + 2));
            this.spots.add(created);

            return created;
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
        private final BlockPos pos;
        private Tardis tardis;

        public LandingPadSpot(BlockPos pos) {
            this.pos = pos;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public boolean isOccupied() {
            return this.tardis != null;
        }

        public Optional<Tardis> getTardis() {
            return Optional.ofNullable(this.tardis);
        }

        public void setOccupant(@Nullable Tardis tardis) {
            this.tardis = tardis;
        }

        private void checkOccupant(World world) {
            if (checkOccupantFlight(world)) return;

            if (!(world.getBlockEntity(this.getPos()) instanceof ExteriorBlockEntity exterior)) {
                this.setOccupant(null);
                return;
            }

            if (exterior.tardis().isEmpty()) {
                // idk??
                return;
            }

            Tardis found = exterior.tardis().get();
            if (this.tardis == null) {
                this.setOccupant(found);
            }

            if (!(this.tardis.equals(found))) {
                this.setOccupant(found);
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
                this.setOccupant(null);
                return false;
            }

            return false;
        }
    }
}
