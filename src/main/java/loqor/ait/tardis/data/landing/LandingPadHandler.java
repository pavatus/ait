package loqor.ait.tardis.data.landing;

import java.util.Optional;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;

public class LandingPadHandler extends KeyedTardisComponent {
    @Exclude(strategy = Exclude.Strategy.NETWORK)
    private LandingPadSpot current;

    static {
        TardisEvents.MAT.register(LandingPadHandler::onMaterialise);
        TardisEvents.DEMAT.register(LandingPadHandler::onDematerialise); // TODO - on enter flight event instead
        TardisEvents.LANDED.register(LandingPadHandler::onLand);
    }

    public LandingPadHandler() {
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

    private static TardisEvents.Interaction onLand(Tardis tardis) {
        return tardis.landingPad().onLand();
    }

    private TardisEvents.Interaction onLand() {
        if (this.current == null) return TardisEvents.Interaction.PASS;

        this.current.updatePosition();

        return TardisEvents.Interaction.PASS;
    }

    private void update() {
        if (!(this.tardis() instanceof ServerTardis)) return;

        DirectedGlobalPos.Cached destination = this.tardis().travel().destination();

        World world = TardisUtil.getOverworld().getServer().getWorld(destination.getDimension()); // #getWorld from destination is always null..?

        LandingPadSpot spot = null;
        boolean success = false;

        while (!success) {
            spot = findSpot(world, destination.getPos()).orElse(null);

            if (spot == null) return;

            spot.verify(world);
            if (spot.isOccupied()) continue;

            success = true;
        }

        this.claim(spot, true);
        this.tardis().travel().destination(destination.pos(this.current.getPos()));

        this.onAdjust(spot);
    }

    private void onAdjust(LandingPadSpot spot) {
        TardisEvents.LANDING_PAD_ADJUST.invoker().onLandingPadAdjust(this.tardis(), spot);

        TardisUtil.sendMessageToInterior(this.tardis(), Text.translatable("tardis.message.landingpad.adjust"));
    }

    private Optional<LandingPadSpot> findSpot(World world, BlockPos pos) {
        LandingPadRegion region = findRegion(world, pos).orElse(null);

        if (region == null) return Optional.empty();

        return region.getNextSpot();
    }

    private Optional<LandingPadRegion> findRegion(World world, BlockPos pos) {
        return LandingPadManager.getInstance((ServerWorld) world).getRegion(pos);
    }

    public LandingPadSpot release(boolean updateSpot) {
        LandingPadSpot spot = this.current;

        if (updateSpot) {
            this.current.release(false);
        }

        this.current = null;
        return spot;
    }

    public void claim(LandingPadSpot spot, boolean updateSpot) {
        this.current = spot;

        if (updateSpot) {
            this.current.claim(this.tardis(), false);
        }
    }
}
