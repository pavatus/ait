package loqor.ait.tardis.handler.landing;


import org.jetbrains.annotations.Nullable;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import loqor.ait.api.KeyedTardisComponent;
import loqor.ait.api.TardisEvents;
import loqor.ait.data.DirectedGlobalPos;
import loqor.ait.data.bsp.Exclude;
import loqor.ait.tardis.ServerTardis;
import loqor.ait.tardis.handler.properties.Property;
import loqor.ait.tardis.handler.properties.Value;
import loqor.ait.tardis.handler.properties.bool.BoolValue;
import loqor.ait.tardis.handler.travel.TravelHandler;
import loqor.ait.tardis.util.TardisUtil;

public class LandingPadHandler extends KeyedTardisComponent {
    public static final Property<String> CODE = new Property<>(Property.Type.STR, "code", "");
    private final Value<String> code = LandingPadHandler.CODE.create(this);
    @Exclude
    private LandingPadSpot current;

    static {
        TardisEvents.BEFORE_LAND.register((tardis, destination) ->
                new TardisEvents.Result<>(tardis.landingPad().update(destination)));

        TardisEvents.DEMAT.register(tardis -> {
            tardis.landingPad().release();
            return TardisEvents.Interaction.PASS;
        });

        TardisEvents.MAT.register(tardis -> {
            boolean success = tardis.landingPad().checkCode();

            if (!success) return TardisEvents.Interaction.FAIL;

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

        code.of(this, LandingPadHandler.CODE);
    }

    public Value<String> code() {
        return code;
    }

    private DirectedGlobalPos.Cached update(DirectedGlobalPos.Cached pos) {
        TravelHandler travel = this.tardis().travel();
        DirectedGlobalPos.Cached destination = travel.destination();
        ServerWorld world = destination.getWorld();

        LandingPadSpot spot = findFreeSpot(world, destination.getPos());

        if (spot == null)
            return null;

        BoolValue hSearch = this.tardis().travel().horizontalSearch();
        boolean old = hSearch.get();
        hSearch.set(false);

        travel.destination(destination.pos(spot.getPos()));
        destination = travel.destination();

        hSearch.set(old);

        this.claim(spot);

        TardisEvents.LANDING_PAD_ADJUST.invoker().onLandingPadAdjust(this.tardis(), this.current);
        TardisUtil.sendMessageToInterior(this.tardis().asServer(), Text.translatable("message.ait.landingpad.adjust"));

        return destination;
    }
    private boolean checkCode() {
        ServerWorld world = tardis.travel().destination().getWorld();
        BlockPos pos = tardis.travel().destination().getPos();

        LandingPadRegion region = LandingPadManager.getInstance(world)
                .getRegionAt(pos);

        if (region == null)
            return true;

        return hasMatchingCode(region);
    }
    private boolean hasMatchingCode(LandingPadRegion region) {
        String tardisCode = tardis.landingPad().code().get();
        String regionCode = region.getLandingCode();

        return tardisCode.equalsIgnoreCase(regionCode) || regionCode.isBlank();
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
