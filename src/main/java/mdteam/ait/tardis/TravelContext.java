package mdteam.ait.tardis;

import mdteam.ait.tardis.util.AbsoluteBlockPos;

public record TravelContext(TardisTravel travel, AbsoluteBlockPos.Directed from, AbsoluteBlockPos.Directed to) { }