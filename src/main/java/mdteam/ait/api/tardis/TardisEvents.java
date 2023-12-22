package mdteam.ait.api.tardis;

import mdteam.ait.tardis.Tardis;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class TardisEvents {

    public static final Event<Demat> DEMAT = EventFactory.createArrayBacked(Demat.class, callbacks -> (tardis) -> {
        for (Demat callback : callbacks) {
            callback.onDemat(tardis);
        }
    });
    public static final Event<Mat> MAT = EventFactory.createArrayBacked(Mat.class, callbacks -> (tardis) -> {
        for (Mat callback : callbacks) {
            callback.onMat(tardis);
        }
    });
    public static final Event<Landed> LANDED = EventFactory.createArrayBacked(Landed.class, callbacks -> (tardis) -> {
        for (Landed callback : callbacks) {
            callback.onLanded(tardis);
        }
    });

    // Interfaces go down here
    // todo add functionality for cancelling things ( start by removing the void i think lol ) ( look at PlayerBlockBreakEvents )
    // todo add more events, i dont really know what should and shouldnt be an event rn

    /**
     * Called when a TARDIS successfully ( passed all checks ) starts to take off, before anything else is ran
     */
    @FunctionalInterface
    public interface Demat {
        /**
         * Called when a TARDIS successfully ( passed all checks ) starts to take off, before anything else is ran.
         * @param tardis the tardis taking off
         */
        void onDemat(Tardis tardis);
    }

    /**
     * Called when a TARDIS successfully ( passed all checks ) starts to land, before anything else is ran
     */
    @FunctionalInterface
    public interface Mat {
        /**
         * Called when a TARDIS successfully ( passed all checks ) starts to land, before anything else is ran.
         * @param tardis the tardis landing
         */
        void onMat(Tardis tardis);
    }

    /**
     * Called when a TARDIS has finished landing
     */
    @FunctionalInterface
    public interface Landed {
        /**
         * Called when a TARDIS has finished landing
         * @param tardis the landed tardis
         */
        void onLanded(Tardis tardis);
    }
}
