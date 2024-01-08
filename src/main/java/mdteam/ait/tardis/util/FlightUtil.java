package mdteam.ait.tardis.util;

import mdteam.ait.core.managers.DeltaTimeManager;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class FlightUtil {
    private static final int BASE_FLIGHT_TICKS = 5 * 20; //  seconds minimum
    private static final double FORCE_LAND_TIMER = 15;


    public static void init() {

    }

    // todo use me in places where similar things are used

    /**
     * Sets the destination, turns on autopilot and demats
     * @param tardis
     * @param pos
     */
    public static void travelTo(Tardis tardis, AbsoluteBlockPos.Directed pos) {
        PropertiesHandler.setAutoPilot(tardis.getHandlers().getProperties(), true);
        tardis.getTravel().setDestination(pos, true);

        if (tardis.getTravel().getState() == TardisTravel.State.LANDED) {
            tardis.getTravel().dematerialise(true);
        } else if (tardis.getTravel().getState() == TardisTravel.State.FLIGHT) {
            tardis.getTravel().materialise();
        }
    }

    public static int convertSecondsToTicks(int seconds) {
        return seconds * 20;
    }

    public static int getFlightDuration(AbsoluteBlockPos.Directed source, AbsoluteBlockPos.Directed destination) {
        float distance = MathHelper.sqrt((float) source.getSquaredDistance(destination));
        boolean hasDirChanged = !(source.getDirection().equals(destination.getDirection()));
        boolean hasDimChanged = !(source.getDimension().equals(destination.getDimension()));

        return (int) (BASE_FLIGHT_TICKS + (distance / 10f) + (hasDirChanged ? convertSecondsToTicks(5) : 0) + (hasDimChanged ? convertSecondsToTicks(30) : 0));
    }

    public static AbsoluteBlockPos.Directed getPositionFromPercentage(AbsoluteBlockPos.Directed source, AbsoluteBlockPos.Directed destination, int percentage) {
        // https://stackoverflow.com/questions/33907276/calculate-point-between-two-coordinates-based-on-a-percentage

        float per = percentage / 100f;
        BlockPos diff = destination.subtract(source);
        return new AbsoluteBlockPos.Directed(source.add(new BlockPos((int) (diff.getX() * per), (int) (diff.getY() * per), (int) (diff.getZ() * per))), destination.getDimension(), destination.getDirection());
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
        TardisUtil.getTardisDimension().playSound(null, tardis.getDesktop().getConsolePos(), sound, category, volume, pitch );
    }
    public static void playSoundAtConsole(Tardis tardis, SoundEvent sound, SoundCategory category) {
        playSoundAtConsole(tardis, sound, category, 1f, 1f);
    }
    public static void playSoundAtConsole(Tardis tardis, SoundEvent sound) {
        playSoundAtConsole(tardis, sound, SoundCategory.MASTER);
    }
}
