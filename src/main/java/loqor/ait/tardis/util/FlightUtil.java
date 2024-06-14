package loqor.ait.tardis.util;

import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.sounds.MatSound;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class FlightUtil {

	private static final int BASE_FLIGHT_TICKS = 5 * 20; //  seconds minimum
	private static final double FORCE_LAND_TIMER = 15;

	public static void init() {

	}

	/**
	 * Sets the destination, turns on autopilot and demats
	 */
	public static void travelTo(Tardis tardis, AbsoluteBlockPos.Directed pos) {
		TardisTravel travel = tardis.travel();

		tardis.flight().handbrake().set(false);
		tardis.flight().autoLand().set(true);

		travel.setDestination(pos, true);

		if (travel.getState() == TardisTravel.State.LANDED) {
			travel.dematerialise(true);
		} else if (travel.getState() == TardisTravel.State.FLIGHT) {
			travel.materialise();
		}
	}

	public static int convertSecondsToTicks(int seconds) {
		return seconds * 20;
	}

	public static int convertSecondsToTicks(double seconds) {
		return (int) (seconds * 20);
	}

	public static int getFlightDuration(AbsoluteBlockPos.Directed source, AbsoluteBlockPos.Directed destination) {
		float distance = MathHelper.sqrt((float) source.getSquaredDistance(destination));
		boolean hasDirChanged = !(source.getRotation() == destination.getRotation());
		boolean hasDimChanged = !(source.getDimension().equals(destination.getDimension()));

		return (int) (BASE_FLIGHT_TICKS + (distance / 10f) + (hasDirChanged ? convertSecondsToTicks(5) : 0) + (hasDimChanged ? convertSecondsToTicks(30) : 0));
	}

	public static AbsoluteBlockPos.Directed getPositionFromPercentage(AbsoluteBlockPos.Directed source, AbsoluteBlockPos.Directed destination, int percentage) {
		// https://stackoverflow.com/questions/33907276/calculate-point-between-two-coordinates-based-on-a-percentage

		float per = percentage / 100f;
		BlockPos diff = destination.subtract(source);
		return new AbsoluteBlockPos.Directed(source.add(new BlockPos((int) (diff.getX() * per), (int) (diff.getY() * per), (int) (diff.getZ() * per))), destination.getDimension(), destination.getRotation());
	}

	public static DirectedGlobalPos getPositionFromPercentage(DirectedGlobalPos source, DirectedGlobalPos destination, int percentage) {
		// https://stackoverflow.com/questions/33907276/calculate-point-between-two-coordinates-based-on-a-percentage

		float per = percentage / 100f;
		BlockPos diff = destination.getPos().subtract(source.getPos());
		return destination.offset((int) (diff.getX() * per), (int) (diff.getY() * per), (int) (diff.getZ() * per));
	}

	public static int getSoundLength(MatSound sound) {
		if (sound == null)
			return (int) FORCE_LAND_TIMER;
		return sound.maxTime() / 20;
	}

	public static int getDurationAsPercentage(int ticks, int target) {
		return (MathHelper.clamp(ticks, 1, target) * 100) / target;
	}

	public static String getMaterialiseDelayId(Tardis tardis) {
		return tardis.getUuid().toString() + "_materialise_delay";
	}

	public static String getDematerialiseDelayId(Tardis tardis) {
		return tardis.getUuid().toString() + "_dematerialise_delay";
	}

	public static boolean isMaterialiseOnCooldown(Tardis tardis) {
		return DeltaTimeManager.isStillWaitingOnDelay(getMaterialiseDelayId(tardis));
	}

	public static boolean isDematerialiseOnCooldown(Tardis tardis) {
		return DeltaTimeManager.isStillWaitingOnDelay(getDematerialiseDelayId(tardis));
	}

	public static void createMaterialiseDelay(Tardis tardis) {
		DeltaTimeManager.createDelay(getMaterialiseDelayId(tardis), 5000L);
	}

	public static void createDematerialiseDelay(Tardis tardis) {
		DeltaTimeManager.createDelay(getDematerialiseDelayId(tardis), 5000L);
	}

	public static void playSoundAtConsole(Tardis tardis, SoundEvent sound, SoundCategory category, float volume, float pitch) {
		ServerWorld dim = (ServerWorld) TardisUtil.getTardisDimension();
		tardis.getDesktop().getConsoles().forEach(console -> dim.playSound(null, console.position(), sound, category, volume, pitch));
	}

	public static void playSoundAtConsole(Tardis tardis, SoundEvent sound, SoundCategory category) {
		playSoundAtConsole(tardis, sound, category, 1f, 1f);
	}

	public static void playSoundAtConsole(Tardis tardis, SoundEvent sound) {
		playSoundAtConsole(tardis, sound, SoundCategory.MASTER);
	}
}
