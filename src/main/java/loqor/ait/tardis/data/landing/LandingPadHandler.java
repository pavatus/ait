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
import loqor.ait.tardis.data.properties.Property;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;

public class LandingPadHandler extends KeyedTardisComponent {
    public static final Property<String> LANDING_CODE = new Property<>(Property.Type.STR, "landing_code", "");
    private final Value<String> landingCode = LandingPadHandler.LANDING_CODE.create(this);
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

    @Override
    public void postInit(InitContext ctx) {
        super.postInit(ctx);

        if (!(this.tardis() instanceof ServerTardis)) return;

        // find old spot and claim
        DirectedGlobalPos.Cached pos = this.tardis().travel().position();

        LandingPadRegion region = LandingPadManager.getInstance(pos.getWorld()).getRegionAt(pos.getPos());
        if (region == null) return;

        LandingPadSpot found = region.getSpotAt(pos.getPos()).orElse(null);

        if (found == null) return;

        this.claim(found);
    }

    @Override
    public void onLoaded() {
        super.onLoaded();

        landingCode.of(this, LandingPadHandler.LANDING_CODE);
    }

    public Value<String> landingCode() {
        return landingCode;
    }

    private DirectedGlobalPos.Cached update(DirectedGlobalPos.Cached pos) {
        TravelHandler travel = this.tardis().travel();
        DirectedGlobalPos.Cached destination = travel.destination();
        ServerWorld world = destination.getWorld();

        LandingPadSpot spot = findFreeSpot(world, destination.getPos());
        if (spot == null) return null;

        BoolValue hSearch = this.tardis().travel().horizontalSearch();
        boolean old = hSearch.get();
        hSearch.set(false);

        travel.destination(destination.pos(spot.getPos()));
        destination = travel.destination();

        hSearch.set(old);

        this.claim(spot);

        TardisEvents.LANDING_PAD_ADJUST.invoker().onLandingPadAdjust(this.tardis(), this.current);
        TardisUtil.sendMessageToInterior(this.tardis(), Text.translatable("message.ait.landingpad.adjust"));

        return destination;
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
