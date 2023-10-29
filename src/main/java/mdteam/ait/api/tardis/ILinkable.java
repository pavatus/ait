package mdteam.ait.api.tardis;

import mdteam.ait.AITMod;

public interface ILinkable {

    ITardis getTardis();
    void setTardis(ITardis tardis);

    default IDesktop getDesktop() { return this.getTardis().getDesktop(); }
    default void setDesktop(IDesktop desktop) { }

    /**
     * This method forces the {@link ILinkable} to update its desktop!
     */
    default void linkDesktop() {
        if (this.getTardis() == null)
            return;

        IDesktop desktop = this.getTardis().getDesktop();

        if (desktop != null)
            this.setDesktop(desktop);
    }

    default ITravel getTravel() { return this.getTardis().getTravel(); }
    default void setTravel(ITravel travel) { }

    /**
     * This method forces the {@link ILinkable} to update its travel!
     */
    default void linkTravel() {
        if (this.getTardis() == null)
            return;

        ITravel travel = this.getTardis().getTravel();

        if (travel != null)
            this.setTravel(travel);
    }

    /**
     * If false, calling {@link ILinkable#setTardis(ITardis)} might throw an exception!
     */
    default boolean linkable() {
        return true;
    }
}
