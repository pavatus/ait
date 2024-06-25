package loqor.ait.tardis.data;

import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.travel.TravelHandlerBase;

public class TravelHandlerV2 extends TravelHandlerBase implements TardisTickable {

    @Override
    protected boolean checkDestination(int limit, boolean fullCheck) {
        return false;
    }
}
