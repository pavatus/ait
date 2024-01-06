package mdteam.ait.compat.regen;

import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.RegenerationData;
import mc.craig.software.regen.common.regen.acting.Acting;
import mc.craig.software.regen.common.regen.acting.ActingForwarder;
import mc.craig.software.regen.common.regen.state.RegenStates;
import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.TardisEvents;
import mdteam.ait.compat.DependencyChecker;
import mdteam.ait.compat.immersive.PortalsHandler;
import mdteam.ait.registry.DesktopRegistry;
import mdteam.ait.registry.ExteriorRegistry;
import mdteam.ait.registry.ExteriorVariantRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class RegenHandler implements Acting {

    public static void init() {
        if (!DependencyChecker.hasRegeneration()) {
            AITMod.LOGGER.info("no regeneration stuff for u pal"); // shouldnt be possible to get here anyway
            return;
        }
        AITMod.LOGGER.info("AIT - Setting up Regeneration");
        ActingForwarder.register(new RegenHandler(), ActingForwarder.Side.COMMON);
    }

    @Override
    public void onRegenTick(IRegen iRegen) {
        LivingEntity livingEntity = iRegen.getLiving();

        World world = livingEntity.getWorld();

        if(world.isClient()) return;

        if(iRegen.regenState() == RegenStates.REGENERATING) {
            Tardis tardis = TardisUtil.findTardisByInterior(livingEntity.getBlockPos());
            if (tardis == null) return;
            if(tardis.getTravel().getState() == TardisTravel.State.FLIGHT) {
                tardis.getTravel().crash();
            }
        }
    }

    @Override
    public void onEnterGrace(IRegen iRegen) {
    }

    @Override
    public void onHandsStartGlowing(IRegen iRegen) {
    }

    @Override
    public void onGoCritical(IRegen iRegen) {
    }

    @Override
    public void onRegenTrigger(IRegen iRegen) {
    }

    @Override
    public void onRegenFinish(IRegen iRegen) {
        LivingEntity livingEntity = iRegen.getLiving();

        World world = livingEntity.getWorld();

        if(world.isClient()) return;

        Tardis tardis = TardisUtil.findTardisByInterior(livingEntity.getBlockPos());
        if (tardis == null) return;
        tardis.getHandlers().getInteriorChanger().queueInteriorChange(DesktopRegistry.get(Random.create().nextBetween(0, DesktopRegistry.size())));
        tardis.getExterior().setType(ExteriorRegistry.REGISTRY.get(Random.create().nextBetween(0, ExteriorRegistry.REGISTRY.size())) == ExteriorRegistry.CORAL_GROWTH ? ExteriorRegistry.REGISTRY.get(0): ExteriorRegistry.REGISTRY.get(Random.create().nextBetween(0, ExteriorRegistry.REGISTRY.size())));
    }

    @Override
    public void onPerformingPost(IRegen iRegen) {
    }
}
