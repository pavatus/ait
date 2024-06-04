package loqor.ait.tardis.travel;

import loqor.ait.tardis.TardisTravel2;
import loqor.ait.tardis.data.SonicHandler;

public class LandedTravelState implements TravelState {

    /*@Override
    public void onHandbrake(TardisTravel2 travel, boolean handbrake) {
        if (handbrake)
            return;

        if (travel.speed().get() == 0)
            return;

        if (travel.tardis().sonic().hasSonic(SonicHandler.HAS_EXTERIOR_SONIC))
            return;

        travel.setState(TardisTravel2.State.DEMAT);
    }*/

    @Override
    public TardisTravel2.State getNext() {
        return TardisTravel2.State.DEMAT;
    }
}
