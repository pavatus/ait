package loqor.ait.tardis.data.landing;

import java.util.Optional;

import net.minecraft.server.world.ServerWorld;
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
    private LandingPadManager.Spot current;

    static {
        TardisEvents.MAT.register(LandingPadHandler::onMaterialise);
        TardisEvents.DEMAT.register(LandingPadHandler::onDematerialise); // TODO - on enter flight event instead
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

    private void update() {
        if (!(this.tardis() instanceof ServerTardis)) return;
        if (this.current != null) {
            // how..
            return;
        }

        DirectedGlobalPos.Cached destination = this.tardis().travel().destination();

        World world = TardisUtil.getOverworld().getServer().getWorld(destination.getDimension()); // #getWorld from destination is always null..?

        LandingPadManager.Spot spot = findSpot(world, destination.getPos()).orElse(null);

        if (spot == null) return;

        this.claim(spot, true);
        this.tardis().travel().destination(destination.pos(this.current.getPos()));
    }

    private Optional<LandingPadManager.Spot> findSpot(World world, BlockPos pos) {
        LandingPadManager.Region region = findRegion(world, pos).orElse(null);

        if (region == null) return Optional.empty();

        return region.getNextSpot();
    }
    private Optional<LandingPadManager.Region> findRegion(World world, BlockPos pos) {
        return LandingPadManager.getInstance((ServerWorld) world).getRegion(pos);
    }

    public LandingPadManager.Spot release(boolean updateSpot) {
        LandingPadManager.Spot spot = this.current;

        if (updateSpot) {
            this.current.release(false);
        }

        this.current = null;
        return spot;
    }
    public void claim(LandingPadManager.Spot spot, boolean updateSpot) {
        this.current = spot;

        if (updateSpot) {
            this.current.claim(this.tardis(), false);
        }
    }
}
