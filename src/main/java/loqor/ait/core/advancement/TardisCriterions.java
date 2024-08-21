package loqor.ait.core.advancement;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.server.network.ServerPlayerEntity;

import loqor.ait.api.tardis.TardisEvents;
import loqor.ait.tardis.util.TardisUtil;

public class TardisCriterions {
    public static TakeOffCriterion TAKEOFF = Criteria.register(new TakeOffCriterion());
    public static CrashCriterion CRASH = Criteria.register(new CrashCriterion());
    public static BreakVegetationCriterion VEGETATION = Criteria.register(new BreakVegetationCriterion());
    public static PlaceCoralCriterion PLACE_CORAL = Criteria.register(new PlaceCoralCriterion());

    public static void init() {
        TardisEvents.CRASH.register(tardis -> {
            for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis)) {
                TardisCriterions.CRASH.trigger(player);
            }
        });

        TardisEvents.DEMAT.register(tardis -> {
            for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis)) {
                TardisCriterions.TAKEOFF.trigger(player);
            }

            return TardisEvents.Interaction.PASS;
        });
    }
}
