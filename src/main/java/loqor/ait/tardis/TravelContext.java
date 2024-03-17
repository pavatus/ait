package loqor.ait.tardis;

import loqor.ait.tardis.util.AbsoluteBlockPos;

public record TravelContext(TardisTravel travel, AbsoluteBlockPos.Directed from, AbsoluteBlockPos.Directed to) {
}