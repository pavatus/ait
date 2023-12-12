package mdteam.ait.tardis.travel;

import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.core.util.data.AbsoluteBlockPos;

public record TravelContext(TardisTravel travel, AbsoluteBlockPos.Directed from, AbsoluteBlockPos.Directed to) { }