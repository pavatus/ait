package loqor.ait.core.advancement;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.server.network.ServerPlayerEntity;

import loqor.ait.api.TardisEvents;
import loqor.ait.core.tardis.util.TardisUtil;

public class TardisCriterions {
    public static TakeOffCriterion TAKEOFF = Criteria.register(new TakeOffCriterion());
    public static CrashCriterion CRASH = Criteria.register(new CrashCriterion());
    public static BreakVegetationCriterion VEGETATION = Criteria.register(new BreakVegetationCriterion());
    public static PlaceCoralCriterion PLACE_CORAL = Criteria.register(new PlaceCoralCriterion());
    public static EnterTardisCriterion ENTER_TARDIS = Criteria.register(new EnterTardisCriterion());
    public static RedecorateCriterion REDECORATE = Criteria.register(new RedecorateCriterion());
    public static ForcedEntryCriterion FORCED_ENTRY = Criteria.register(new ForcedEntryCriterion());

    public static void init() {
        TardisEvents.CRASH.register(tardis -> {
            for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis.asServer())) {
                TardisCriterions.CRASH.trigger(player);
            }
        });

        TardisEvents.DEMAT.register(tardis -> {
            for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis.asServer())) {
                TardisCriterions.TAKEOFF.trigger(player);
            }

            return TardisEvents.Interaction.PASS;
        });

        TardisEvents.ENTER_TARDIS.register((tardis, entity) -> {
            if (!(entity instanceof ServerPlayerEntity player)) return;

            TardisCriterions.ENTER_TARDIS.trigger(player);
        });

        TardisEvents.RECONFIGURE_DESKTOP.register(tardis -> {
            for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis.asServer())) {
                TardisCriterions.REDECORATE.trigger(player);
            }
        });

        TardisEvents.FORCED_ENTRY.register((tardis, entity) -> {
            if (!(entity instanceof ServerPlayerEntity player)) return;

            TardisCriterions.FORCED_ENTRY.trigger(player);
        });
    }
}
