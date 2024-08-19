package loqor.ait.tardis.data.landing;

import java.util.*;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import loqor.ait.core.data.base.Exclude;

public class LandingPadRegion implements LandingPadSpot.Listener {
    private final ChunkPos chunk;
    private final int maxSpots;
    private final int maxPerRow;
    private final List<LandingPadSpot> spots;
    private final Queue<LandingPadSpot> free;
    @Exclude
    private final List<Listener> listeners; // todo a list probably isnt the best for this
    private int defaultY = 64;

    public LandingPadRegion(ChunkPos chunk) {
        this.chunk = chunk;
        // for now just assume 16x16
        this.maxSpots = getMaxSpots(16, 16);
        this.maxPerRow = 16 / 2;
        this.spots = new ArrayList<>();
        this.free = new LinkedList<>();
        this.listeners = new ArrayList<>();
    }

    public LandingPadRegion(NbtCompound data, World world) {
        this(new ChunkPos(data.getLong("Chunk")));

        this.deserialize(data, world);
    }

    private static int getMaxSpots(int sizeX, int sizeY) {
        return (sizeX * sizeY) / 4;
    }

    public Optional<LandingPadSpot> getNextSpot() {
        if (!this.free.isEmpty()) {
            return Optional.of(this.free.peek());
        }

        if (this.spots.size() >= this.maxSpots) {
            return Optional.empty();
        }

        // loqor says to do this :(
        this.createAllSpots();

        return this.getNextSpot();
    }

    private LandingPadSpot createSpot() {
        LandingPadSpot created;

        if (this.spots.isEmpty()) {
            created = new LandingPadSpot(new BlockPos(this.chunk.getStartX() + 1, this.defaultY, this.chunk.getStartZ() + 1));
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

    private LandingPadSpot generateSpot(boolean isFree) {
        LandingPadSpot created = this.createSpot();

        this.spots.add(created);
        created.addListener(this);

        if (isFree)
            this.free.add(created);

        for (Listener listener : this.listeners) {
            listener.onAdd(this, created);
        }

        return created;
    }

    public Collection<LandingPadSpot> getSpots() {
        return this.spots;
    }
    private boolean hasMaxSpots() {
        return this.spots.size() >= this.maxSpots;
    }
    private boolean isFull() {
        return this.hasMaxSpots() && this.free.isEmpty();
    }

    private void createAllSpots() {
        int toCreate = this.maxSpots - this.spots.size();

        for (int i = 0; i < toCreate; i++) {
            this.generateSpot(true);
        }
    }

    public void setDefaultY(int y) {
        this.defaultY = y;
    }

    @Override
    public void onClaim(LandingPadSpot spot) {
        for (Listener listener : this.listeners) {
            listener.onClaim(this, spot);
        }
    }

    @Override
    public void onFree(LandingPadSpot spot) {
        this.free.add(spot);

        for (Listener listener : this.listeners) {
            listener.onFree(this, spot);
        }
    }

    public void onRemoved() {
        for (LandingPadSpot spot : this.spots) {
            spot.release(true);
        }

        for (Listener listener : this.listeners) {
            listener.onRegionRemoved(this);
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

        for (LandingPadSpot spot : this.spots) {
            spots.add(spot.serialize());
        }

        data.put("Spots", spots);

        data.putInt("DefaultY", this.defaultY);

        return data;
    }

    private void deserialize(NbtCompound data, World world) {
        NbtList spots = data.getList("Spots", NbtElement.COMPOUND_TYPE);

        for (NbtElement nbt : spots) {
            LandingPadSpot created = new LandingPadSpot((NbtCompound) nbt, world);
            created.addListener(this);
            this.spots.add(created);

            if (world != null && !world.isClient())
                created.verify(world);
        }

        this.defaultY = data.getInt("DefaultY");
    }

    public interface Listener {
        void onAdd(LandingPadRegion region, LandingPadSpot spot);

        void onRegionRemoved(LandingPadRegion region);

        void onClaim(LandingPadRegion region, LandingPadSpot spot);

        void onFree(LandingPadRegion region, LandingPadSpot spot);
    }
}
