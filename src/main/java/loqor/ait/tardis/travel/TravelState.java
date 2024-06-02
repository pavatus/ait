package loqor.ait.tardis.travel;

import loqor.ait.tardis.TardisTravel2;

public interface TravelState {

    default void onEnable(TardisTravel2 travel) { }

    default void onDisable(TardisTravel2 travel) { }

    default void onHandbrake(TardisTravel2 travel, boolean handbrake) { }

    TardisTravel2.State getNext();
}