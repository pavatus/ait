package loqor.ait.tardis.travel;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel2;
import loqor.ait.tardis.data.TardisCrashData;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.TardisUtil;

import java.util.Random;

public class FlightTravelState implements TravelState<FlightTravelState.Context> {

    private static final Random random = new Random();

    @Override
    public void onSpeedChange(Context context, int speed) {
        TardisTravel2 travel = context.travel();
        Tardis tardis = context.tardis();

        if (tardis.crash().getState() == TardisCrashData.State.UNSTABLE) {
            int multiplier = random.nextInt(0, 2) == 0 ? 1 : -1;
            int offset = random.nextInt(1, 10) * multiplier;

            travel.destination().flatMap(destination -> {
                destination = destination.offset(offset, 0, offset);

                if (destination.getDimension() == TardisUtil.getTardisDimension().getRegistryKey())
                    return destination.world(TardisUtil.getOverworld().getRegistryKey());

                return destination;
            });
        }

        if (!PropertiesHandler.getBool(tardis.properties(), PropertiesHandler.IS_IN_REAL_FLIGHT))
            travel.setState(TardisTravel2.State.REMAT);

        // Should we just disable autopilot if the speed goes above 1?
        if (speed > 1 && travel.autoLand().get())
            travel.speed().set(speed - 1);
    }

    @Override
    public TardisTravel2.State getNext() {
        return TardisTravel2.State.REMAT;
    }

    public static class Context extends AbstractContext {

        protected Context(TardisTravel2 travel) {
            super(travel);
        }
    }
}
