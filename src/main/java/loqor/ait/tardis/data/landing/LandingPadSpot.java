package loqor.ait.tardis.data.landing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.link.v2.TardisRef;

import java.util.Optional;
import java.util.UUID;

public class LandingPadSpot {

    public static final Codec<LandingPadSpot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("tardis").forGetter(o -> {
                TardisRef ref = o.getReference();

                if (ref != null && ref.getId() != null)
                    return Optional.of(ref.getId().toString());

                return Optional.empty();
            }),
            BlockPos.CODEC.fieldOf("pos").forGetter(LandingPadSpot::getPos)
    ).apply(instance, LandingPadSpot::create));

    private final BlockPos pos;
    private TardisRef tardis;

    private static LandingPadSpot create(Optional<String> tardis, BlockPos pos) {
        UUID id = null;

        if (tardis.isPresent())
            id = UUID.fromString(tardis.get());

        return new LandingPadSpot(id, pos);
    }

    private LandingPadSpot(UUID tardis, BlockPos pos) {
        if (tardis != null)
            this.tardis = new TardisRef(tardis, uuid -> null);

        this.pos = pos;
    }

    public LandingPadSpot(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public TardisRef getReference() {
        return this.tardis;
    }

    public void claim(Tardis tardis) {
        if (this.isOccupied() && !this.tardis.contains(tardis))
            throw new IllegalStateException("Spot already occupied");

        this.tardis = new TardisRef(tardis, uuid -> null);
    }

    public TardisRef release() {
        TardisRef result = this.tardis;
        this.tardis = null;
        return result;
    }

    public boolean isOccupied() {
        return this.tardis != null && this.tardis.getId() != null;
    }
}
