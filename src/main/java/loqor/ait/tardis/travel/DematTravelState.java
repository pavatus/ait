package loqor.ait.tardis.travel;

import loqor.ait.tardis.TardisTravel2;

public class DematTravelState extends TardisTravel2.TravelState {
    @Override
    public TardisTravel2.State getNext() {
        return TardisTravel2.State.FLIGHT;
    }
}
