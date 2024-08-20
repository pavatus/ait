package loqor.ait.tardis.data.landing;

import org.jetbrains.annotations.Nullable;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.util.TardisUtil;

public class LandingPadHandler extends KeyedTardisComponent {

    @Exclude
    private LandingPadSpot current;

    static {
        TardisEvents.BEFORE_LAND.register((tardis, destination)
                -> new TardisEvents.Result<>(tardis.landingPad().update(destination)));

        TardisEvents.DEMAT.register(tardis -> {
            tardis.landingPad().release();
            return TardisEvents.Interaction.PASS;
        });
    }

    public LandingPadHandler() {
        super(Id.LANDING_PAD);
    }

    private DirectedGlobalPos.Cached update(DirectedGlobalPos.Cached pos) {
        DirectedGlobalPos.Cached destination = this.tardis().travel().destination();
        ServerWorld world = destination.getWorld();

        LandingPadSpot spot = findFreeSpot(world, destination.getPos());
        if (spot == null) return null;

        this.claim(spot);

        TardisEvents.LANDING_PAD_ADJUST.invoker().onLandingPadAdjust(this.tardis(), this.current);
        TardisUtil.sendMessageToInterior(this.tardis(), Text.translatable("message.ait.landingpad.adjust"));

        return destination.pos(this.current.getPos());
    }

    private static @Nullable LandingPadSpot findFreeSpot(ServerWorld world, BlockPos pos) {
        LandingPadRegion region = LandingPadManager.getInstance(world).getRegionAt(pos);

        if (region == null)
            return null;

        return region.getFreeSpot();
    }

    public LandingPadSpot release() {
        LandingPadSpot spot = this.current;
        this.current = null;

        if (spot != null) {
            spot.release();

            this.syncSpot();
        }

        return spot;
    }

    public void claim(LandingPadSpot spot) {
        this.current = spot;
        this.current.claim(this.tardis());

        this.syncSpot();
    }

    private void syncSpot() {
        DirectedGlobalPos.Cached cached = this.tardis().travel().position();

        LandingPadManager.Network.syncTracked(LandingPadManager.Network.Action.ADD, cached.getWorld(), new ChunkPos(cached.getPos()));
    }
}
