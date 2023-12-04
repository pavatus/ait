package mdteam.ait.tardis;

import mdteam.ait.data.AbsoluteBlockPos;

public record TravelContext(TardisTravel travel, AbsoluteBlockPos.Directed from, AbsoluteBlockPos.Directed to) { }