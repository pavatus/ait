package loqor.ait.tardis.data.landing;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.*;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.link.v2.TardisRef;
import loqor.ait.tardis.util.TardisUtil;

public class LandingPadSpot {
    @Exclude
    private final List<Listener> listeners; // todo a list probably isnt the best for this
    private BlockPos pos;
    private TardisRef tardis;

    public LandingPadSpot(BlockPos pos) {
        this.pos = pos;
        this.listeners = new ArrayList<>();
    }

    public LandingPadSpot(NbtCompound data) {
        this(NbtHelper.toBlockPos(data.getCompound("Pos")));

        this.deserialize(data);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Optional<TardisRef> getReference() {
        return Optional.ofNullable(this.tardis);
    }

    public void claim(Tardis tardis, boolean updateTardis) {
        if (this.isOccupied() && !this.tardis.equals(tardis)) {
            throw new IllegalStateException("Spot already occupied");
        }

        this.tardis = TardisRef.createAs(TardisUtil.getOverworld(), tardis);

        if (updateTardis)
            this.tardis.get().landingPad().claim(this, false);

        for (Listener listener : this.listeners) {
            listener.onClaim(this);
        }
    }

    public Optional<Tardis> release(boolean updateTardis) {
        Tardis current = this.tardis.get();

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

        if (!(world.getBlockEntity(this.getPos()) instanceof ExteriorBlockEntity exterior)) return;
        if (exterior.tardis().isEmpty()) return;

        this.claim(exterior.tardis().get(), true);
    }

    public NbtCompound serialize() {
        NbtCompound data = new NbtCompound();

        if (this.tardis != null)
            data.putUuid("Tardis", this.tardis.get().getUuid());

        data.put("Pos", NbtHelper.fromBlockPos(this.pos));

        return data;
    }

    private void deserialize(MinecraftServer server, NbtCompound data) {
    }

    private void deserialize(NbtCompound data) {
        // uhh
        this.deserialize(TardisUtil.getOverworld().getServer(), data);
    }

    public static Serializer serializer() {
        return new Serializer();
    }

    public void updatePosition() {
        if (!this.isOccupied()) return;

        this.pos = this.getReference().orElseThrow().get().travel().position().getPos();
    }

    public interface Listener {
        void onClaim(LandingPadSpot spot);

        void onFree(LandingPadSpot spot);
    }

    public static class Serializer implements
            JsonSerializer<LandingPadSpot>,
            JsonDeserializer<LandingPadSpot> {

        @Override
        public LandingPadSpot deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            NbtCompound data = context.deserialize(json, NbtCompound.class);
            return new LandingPadSpot(data);
        }

        @Override
        public JsonElement serialize(LandingPadSpot src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.serialize());
        }
    }
}
