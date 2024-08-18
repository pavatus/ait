package loqor.ait.tardis.data.landing;

import java.util.Optional;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.KeyedTardisComponent;

public class LandingPadHandler extends KeyedTardisComponent {
    private LandingPadManager.LandingPadSpot current;

    public LandingPadHandler() {
        super(Id.LANDING_PAD);

        TardisEvents.MAT.register(this::onMaterialise);
        TardisEvents.DEMAT.register(this::onDematerialise);
    }

    private TardisEvents.Interaction onDematerialise(Tardis tardis) {
        if (this.current == null) return TardisEvents.Interaction.PASS;

        this.release();

        return TardisEvents.Interaction.PASS;
    }

    private TardisEvents.Interaction onMaterialise(Tardis tardis) {
        this.update();

        return TardisEvents.Interaction.PASS;
    }

    private void update() {
        DirectedGlobalPos.Cached destination = this.tardis().travel().destination();
        LandingPadManager.LandingPadSpot spot = findSpot(destination.getWorld(), destination.getPos()).orElse(null);

        if (spot == null) return;

        this.claim(spot);
        this.tardis().travel().destination(destination.pos(this.current.getPos()));
    }

    private Optional<LandingPadManager.LandingPadSpot> findSpot(World world, BlockPos pos) {
        LandingPadManager.LandingPadRegion region = findRegion(world, pos).orElse(null);

        if (region == null) return Optional.empty();

        return region.getNextSpot();
    }
    private Optional<LandingPadManager.LandingPadRegion> findRegion(World world, BlockPos pos) {
        return LandingPadManager.getInstance(world).getRegion(pos);
    }

    private LandingPadManager.LandingPadSpot release() {
        LandingPadManager.LandingPadSpot spot = this.current;
        this.current.release();
        this.current = null;
        return spot;
    }
    private void claim(LandingPadManager.LandingPadSpot spot) {
        this.current = spot;
        this.current.claim(this.tardis());
    }
}
