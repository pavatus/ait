package mdteam.ait.api.tardis;

import mdteam.ait.data.AbsoluteBlockPos;

import java.util.UUID;

public interface ITravel {

    void setPosition(AbsoluteBlockPos.Directed pos);
    AbsoluteBlockPos.Directed getPosition();

    void materialise();
    void dematerialise(boolean withRemat);
    default void dematerialise() {
        this.dematerialise(false);
    }

    void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks);
    AbsoluteBlockPos.Directed getDestination();

    State getState();
    void setState(State state);

    void toggleHandbrake();

    void placeExterior();
    void deleteExterior();

    enum State {
        FAIL_TAKEOFF,
        HOP_TAKEOFF,
        HOP_LAND,
        DEMAT,
        MAT,
        LANDED,
        FLIGHT
    }
}
