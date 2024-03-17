package loqor.ait.compat.regen;

import loqor.ait.AITMod;
import loqor.ait.core.item.TardisItemBuilder;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.exterior.category.ExteriorCategorySchema;
import loqor.ait.tardis.util.TardisUtil;
import mc.craig.software.regen.common.regen.IRegen;
import mc.craig.software.regen.common.regen.acting.Acting;
import mc.craig.software.regen.common.regen.acting.ActingForwarder;
import loqor.ait.compat.DependencyChecker;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.Optional;

public class RegenHandler implements Acting {
	private static final Random random = Random.create();

	public static void init() {
		if (!DependencyChecker.hasRegeneration()) {
			AITMod.LOGGER.info("no regeneration stuff for u pal"); // shouldnt be possible to get here anyway
			return;
		}
		AITMod.LOGGER.info("AIT - Setting up Regeneration");
		ActingForwarder.register(new RegenHandler(), ActingForwarder.Side.COMMON);
	}

	// im bored so here i put them in different methods
	private static Optional<Tardis> findTardis(LivingEntity livingEntity) {
		Tardis found = TardisUtil.findTardisByInterior(livingEntity.getBlockPos(), !livingEntity.getWorld().isClient());
		return Optional.ofNullable(found);
	}

	private static void forceTakeOff(Tardis tardis) {
		PropertiesHandler.setAutoPilot(tardis.getHandlers().getProperties(), true);
		tardis.getTravel().dematerialise(true);
		PropertiesHandler.setAutoPilot(tardis.getHandlers().getProperties(), false);
	}

	private static void extendFlight(Tardis tardis, IRegen regen) {
		tardis.getHandlers().getFlight().increaseFlightTime(regen.transitionType().getAnimationLength());
	}

	private static void changeToRandomExterior(Tardis tardis) {
		ExteriorCategorySchema exteriorType = TardisItemBuilder.findRandomExterior();
		tardis.getExterior().setType(exteriorType);
		tardis.getExterior().setVariant(TardisItemBuilder.findRandomVariant(exteriorType));
	}

	private static void changeToRandomInterior(Tardis tardis) {
		tardis.getHandlers().getInteriorChanger().queueInteriorChange(TardisItemBuilder.findRandomDesktop(tardis));
	}

	@Override
	public void onRegenTick(IRegen iRegen) {
	}

	@Override
	public void onEnterGrace(IRegen iRegen) {
	}

	@Override
	public void onHandsStartGlowing(IRegen iRegen) {
		LivingEntity livingEntity = iRegen.getLiving();

		World world = livingEntity.getWorld();

		if (world.isClient()) return;

		findTardis(livingEntity).ifPresent((RegenHandler::forceTakeOff));
	}

	@Override
	public void onGoCritical(IRegen iRegen) {

	}

	@Override
	public void onRegenTrigger(IRegen iRegen) {
		LivingEntity entity = iRegen.getLiving();
		World world = entity.getWorld();

		if (world.isClient()) return;

		findTardis(entity).ifPresent(tardis -> {
			tardis.getHandlers().getAlarms().enable();
		});
	}

	@Override
	public void onRegenFinish(IRegen iRegen) {
		LivingEntity livingEntity = iRegen.getLiving();

		World world = livingEntity.getWorld();

		if (world.isClient()) return;

		findTardis(livingEntity).ifPresent((tardis -> {
			tardis.getHandlers().getAlarms().enable();
			tardis.getTravel().crash();
			extendFlight(tardis, iRegen);

			if (random.nextBoolean()) changeToRandomInterior(tardis);
			changeToRandomExterior(tardis);
		}));
	}

	@Override
	public void onPerformingPost(IRegen iRegen) {
	}
}
