package loqor.ait.api.tardis;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;

import static loqor.ait.tardis.util.TardisUtil.isClient;

@Deprecated
public interface ILinkable {
	Tardis getTardis();

	void setTardis(Tardis tardis);

	default TardisDesktop getDesktop() {
		return this.getTardis().getDesktop();
	}

	default void setDesktop(TardisDesktop desktop) {
	}

	/**
	 * This method forces the {@link ILinkable} to update its desktop!
	 */
    /*default void linkDesktop() {
        if (this.getTardis() == null)
            return;

        TardisDesktop desktop = this.getTardis().getDesktop();

        if (desktop != null)
            this.setDesktop(desktop);
    }*/
	default void linkDesktop() {
		if (this.getTardis() == null)
			return;
		if (this.getDesktop() != null)
			this.setDesktop(this.getDesktop());
	}

	default TardisTravel getTravel() {
		return this.getTardis().getTravel();
	}

	default void setTravel(TardisTravel travel) {
	}

	/**
	 * This method forces the {@link ILinkable} to update its travel!
	 */
	default void linkTravel() {
		if (this.getTardis() == null)
			return;

		TardisTravel travel = this.getTardis().getTravel();

		if (travel != null)
			this.setTravel(travel);
	}

	/**
	 * If false, calling {@link ILinkable#setTardis(Tardis)} might throw an exception!
	 */
	default boolean linkable() {
		return true;
	}

	default void sync() {
		if (isClient()) return;

		ServerTardisManager.getInstance().sendToSubscribers(this.getTardis());
	}
}
