package mdteam.ait.api.tardis;

import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.util.Scheduler;

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

    IState getState();
    void setState(IState state);

    void toggleHandbrake();

    void placeExterior();
    void deleteExterior();

    interface IState {

        void onEnable();
        void onDisable();

        /**
         * Inits the {@link Scheduler} that will trigger upon completion of this state.
         */
        void schedule(TravelContext context);
        IState getNext();

        /**
         * If true, to advance to the next state will require manual call
         * @return is static
         */
        boolean isStatic();
        void next(TravelContext context);
    }

    record TravelContext(ITravel travel, AbsoluteBlockPos.Directed from, AbsoluteBlockPos.Directed to) { }
}
