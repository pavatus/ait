package loqor.ait.core.advancement;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import net.minecraft.server.network.ServerPlayerEntity;

import loqor.ait.AITMod;
import loqor.ait.api.TardisEvents;
import loqor.ait.core.effects.ZeitonHighEffect;
import loqor.ait.core.engine.impl.EngineSystem;
import loqor.ait.core.tardis.util.TardisUtil;

public class TardisCriterions {
    public static SimpleCriterion ROOT = SimpleCriterion.create("root").register();;
    public static SimpleCriterion TAKEOFF = SimpleCriterion.create("takeoff").register();;
    public static SimpleCriterion CRASH = SimpleCriterion.create("crash").register();;
    public static SimpleCriterion VEGETATION = SimpleCriterion.create("break_vegetation").register();;
    public static SimpleCriterion PLACE_CORAL = SimpleCriterion.create("place_coral").register();
    public static SimpleCriterion ENTER_TARDIS = SimpleCriterion.create("enter_tardis").register();
    public static SimpleCriterion REDECORATE = SimpleCriterion.create("redecorate").register();
    public static SimpleCriterion FORCED_ENTRY = SimpleCriterion.create("forced_entry").register();
    public static SimpleCriterion SONIC_WOOD = SimpleCriterion.create("sonic_wood").register();
    public static SimpleCriterion PILOT_HIGH = SimpleCriterion.create("pilot_high").register();
    public static SimpleCriterion REACH_PILOT = SimpleCriterion.create("reach_pilot").register();
    public static SimpleCriterion REACH_OWNER = SimpleCriterion.create("reach_owner").register();
    public static SimpleCriterion ENABLE_SUBSYSTEM = SimpleCriterion.create("enable_subsystem").register();
    public static SimpleCriterion REPAIR_SUBSYSTEM = SimpleCriterion.create("repair_subsystem").register();


    public static void init() {
        AITMod.LOGGER.info("Initializing Tardis Criterions");

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> ROOT.trigger(handler.getPlayer()));

        TardisEvents.CRASH.register(tardis -> {
            for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis.asServer())) {
                TardisCriterions.CRASH.trigger(player);
            }
        });

        TardisEvents.DEMAT.register(tardis -> {
            for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis.asServer())) {
                TardisCriterions.TAKEOFF.trigger(player);

                if (ZeitonHighEffect.isHigh(player)) {
                    TardisCriterions.PILOT_HIGH.trigger(player);
                }
            }

            return TardisEvents.Interaction.PASS;
        });

        TardisEvents.ENTER_TARDIS.register((tardis, entity) -> {
            if (!(entity instanceof ServerPlayerEntity player)) return;

            TardisCriterions.ENTER_TARDIS.trigger(player);
        });

        TardisEvents.FORCED_ENTRY.register((tardis, entity) -> {
            if (!(entity instanceof ServerPlayerEntity player)) return;

            TardisCriterions.FORCED_ENTRY.trigger(player);
        });

        TardisEvents.SUBSYSTEM_ENABLE.register(system -> {
            if (system instanceof EngineSystem) return;

            for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(system.tardis().asServer())) {
                TardisCriterions.ENABLE_SUBSYSTEM.trigger(player);
            }
        });
        TardisEvents.SUBSYSTEM_REPAIR.register(system -> {
            for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(system.tardis().asServer())) {
                TardisCriterions.REPAIR_SUBSYSTEM.trigger(player);
            }
        });
    }
}
