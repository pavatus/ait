package loqor.ait.tardis;

import loqor.ait.core.data.AbsoluteBlockPos;

public record TravelContext(TardisTravel travel, AbsoluteBlockPos.Directed from, AbsoluteBlockPos.Directed to) {
}