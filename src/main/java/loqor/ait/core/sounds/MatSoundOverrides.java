package loqor.ait.core.sounds;

import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;
import loqor.ait.data.enummap.EnumMap;

public record MatSoundOverrides(EnumMap.Compliant<TravelHandlerBase.State, MatSound> lookup) {

}
