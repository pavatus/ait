package loqor.ait.tardis.data.travel;

import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class TravelUtil {

    private static final int BASE_FLIGHT_TICKS = 5 * 20;

    public static void randomPos(Tardis tardis, int limit, int max) {
        TravelHandler travel = tardis.travel();
        final DirectedGlobalPos.Cached[] dest = {travel.destination()};
        ServerWorld world = dest[0].getWorld();

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i <= limit; i++) {
                dest[0] = dest[0].pos(
                        world.random.nextBoolean() ? world.random.nextInt(max) : -world.random.nextInt(max), dest[0].getPos().getY(),
                        world.random.nextBoolean() ? world.random.nextInt(max) : -world.random.nextInt(max)
                );
                travel.destination(dest[0]);
            }
            return null;
        });

        future.join();
    }

    public static void travelTo(Tardis tardis, DirectedGlobalPos.Cached pos) {
        TravelHandler travel = tardis.travel();

        travel.autopilot(true);
        travel.destination(pos);

        if (travel.getState() == TravelHandlerBase.State.LANDED)
            travel.dematerialize();
    }

    public static DirectedGlobalPos.Cached getPositionFromPercentage(DirectedGlobalPos.Cached source, DirectedGlobalPos.Cached destination, int percentage) {
        // https://stackoverflow.com/questions/33907276/calculate-point-between-two-coordinates-based-on-a-percentage
        if (percentage == 0)
            return source;

        if (percentage == 100)
            return destination;

        float per = percentage / 100f;
        BlockPos pos = source.getPos();
        BlockPos diff = destination.getPos().subtract(pos);

        return destination.pos(pos.add(
                (int) (diff.getX() * per),
                (int) (diff.getY() * per),
                (int) (diff.getZ() * per)
        ));
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
