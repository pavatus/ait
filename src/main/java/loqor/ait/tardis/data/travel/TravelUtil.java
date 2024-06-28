package loqor.ait.tardis.data.travel;

import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class TravelUtil {

    private static final int BASE_FLIGHT_TICKS = 5 * 20;

    public static DirectedGlobalPos.Cached randomPos(Tardis tardis, int limit, int max) {
        TravelHandler travel = tardis.travel2();
        DirectedGlobalPos.Cached dest = travel.destination();
        ServerWorld world = dest.getWorld();

        for (int i = 0; i <= limit; i++) {
            dest = dest.pos(
                    world.random.nextBoolean() ? world.random.nextInt(max) : -world.random.nextInt(max), 0,
                    world.random.nextBoolean() ? world.random.nextInt(max) : -world.random.nextInt(max)
            );
        }

        return dest;
    }

    public static void travelTo(Tardis tardis, DirectedGlobalPos.Cached pos) {
        TravelHandler travel = tardis.travel2();

        travel.autopilot(true);
        travel.destination(pos);

        if (travel.getState() == TravelHandlerBase.State.LANDED)
            travel.dematerialize();
    }

    public static DirectedGlobalPos.Cached getPositionFromPercentage(DirectedGlobalPos.Cached source, DirectedGlobalPos.Cached destination, int percentage) {
        // https://stackoverflow.com/questions/33907276/calculate-point-between-two-coordinates-based-on-a-percentage

        float per = percentage / 100f;
        BlockPos diff = destination.getPos().subtract(source.getPos());
        return destination.offset((int) (diff.getX() * per), (int) (diff.getY() * per), (int) (diff.getZ() * per));
    }

    public static int getFlightDuration(DirectedGlobalPos.Cached source, DirectedGlobalPos.Cached destination) {
        float distance = MathHelper.sqrt((float) source.getPos().getSquaredDistance(destination.getPos()));
        boolean hasDirChanged = !(source.getRotation() == destination.getRotation());
        boolean hasDimChanged = !(source.getDimension().equals(destination.getDimension()));

        return (int) (BASE_FLIGHT_TICKS + (distance / 10f) + (hasDirChanged ? 100 : 0) + (hasDimChanged ? 600 : 0));
    }

    public static DirectedGlobalPos.Cached jukePos(DirectedGlobalPos.Cached pos, int min, int max, int multiplier) {
        Random random = TardisUtil.random();
        multiplier *= random.nextInt(0, 2) == 0 ? 1 : -1;

        return pos.offset(random.nextInt(min, max) * multiplier, 0,
                random.nextInt(min, max) * multiplier
        );
    }

    public static DirectedGlobalPos.Cached jukePos(DirectedGlobalPos.Cached pos, int min, int max) {
        return jukePos(pos, min, max, 1);
    }

    private static String getMaterialiseDelayId(Tardis tardis) {
        return tardis.getUuid().toString() + "_materialise_delay";
    }

    private static String getDematerialiseDelayId(Tardis tardis) {
        return tardis.getUuid().toString() + "_dematerialise_delay";
    }

    public static boolean matCooldownn(Tardis tardis) {
        return DeltaTimeManager.isStillWaitingOnDelay(getMaterialiseDelayId(tardis));
    }

    public static boolean dematCooldown(Tardis tardis) {
        return DeltaTimeManager.isStillWaitingOnDelay(getDematerialiseDelayId(tardis));
    }

    public static void runMatCooldown(Tardis tardis) {
        DeltaTimeManager.createDelay(getMaterialiseDelayId(tardis), 5000L);
    }

    public static void runDematCooldown(Tardis tardis) {
        DeltaTimeManager.createDelay(getDematerialiseDelayId(tardis), 5000L);
    }
}
