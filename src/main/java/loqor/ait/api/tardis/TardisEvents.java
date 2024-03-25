package loqor.ait.api.tardis;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.util.AbsoluteBlockPos;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class TardisEvents {

	// Flight

	public static final Event<Demat> DEMAT = EventFactory.createArrayBacked(Demat.class, callbacks -> (tardis) -> {
		for (Demat callback : callbacks) {
			return callback.onDemat(tardis);
		}

		return false;
	});
	public static final Event<Mat> MAT = EventFactory.createArrayBacked(Mat.class, callbacks -> (tardis) -> {
		for (Mat callback : callbacks) {
			return callback.onMat(tardis);
		}

		return false;
	});
	public static final Event<Landed> LANDED = EventFactory.createArrayBacked(Landed.class, callbacks -> (tardis) -> {
		for (Landed callback : callbacks) {
			callback.onLanded(tardis);
		}
	});
	public static final Event<Crash> CRASH = EventFactory.createArrayBacked(Crash.class, callbacks -> (tardis) -> {
		for (Crash callback : callbacks) {
			callback.onCrash(tardis);
		}
	});

	// Power / Fuel
	public static final Event<NoFuel> OUT_OF_FUEL = EventFactory.createArrayBacked(NoFuel.class, callbacks -> (tardis) -> {
		for (NoFuel callback : callbacks) {
			callback.onNoFuel(tardis);
		}
	});
	public static final Event<LosePower> LOSE_POWER = EventFactory.createArrayBacked(LosePower.class, callbacks -> (tardis) -> {
		for (LosePower callback : callbacks) {
			callback.onLosePower(tardis);
		}
	});
	public static final Event<RegainPower> REGAIN_POWER = EventFactory.createArrayBacked(RegainPower.class, callbacks -> (tardis) -> {
		for (RegainPower callback : callbacks) {
			callback.onRegainPower(tardis);
		}
	});

	// Door
	public static final Event<OpenDoor> DOOR_OPEN = EventFactory.createArrayBacked(OpenDoor.class, callbacks -> ((tardis) -> {
		for (OpenDoor callback : callbacks) {
			callback.onOpen(tardis);
		}
	}));
	public static final Event<CloseDoor> DOOR_CLOSE = EventFactory.createArrayBacked(CloseDoor.class, callbacks -> (((tardis) -> {
		for (CloseDoor callback : callbacks) {
			callback.onClose(tardis);
		}
	})));
	public static final Event<MoveDoor> DOOR_MOVE = EventFactory.createArrayBacked(MoveDoor.class, callbacks -> (((tardis, prev) -> {
		for (MoveDoor callback : callbacks) {
			callback.onMove(tardis, prev);
		}
	})));

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
		 *
		 * @param tardis the tardis taking off
		 * @return whether the demat should be cancelled ( true cancels it )
		 */
		boolean onDemat(Tardis tardis);
	}

	/**
	 * Called when a TARDIS successfully ( passed all checks ) starts to land, before anything else is ran
	 */
	@FunctionalInterface
	public interface Mat {
		/**
		 * Called when a TARDIS successfully ( passed all checks ) starts to land, before anything else is ran.
		 *
		 * @param tardis the tardis landing
		 * @return whether the mat should be cancelled ( true cancels it )
		 */
		boolean onMat(Tardis tardis);
	}

	/**
	 * Called when a TARDIS has finished landing
	 */
	@FunctionalInterface
	public interface Landed {
		/**
		 * Called when a TARDIS has finished landing
		 *
		 * @param tardis the landed tardis
		 */
		void onLanded(Tardis tardis);
	}

	/**
	 * Called when a TARDIS starts crashing
	 */
	@FunctionalInterface
	public interface Crash {
		void onCrash(Tardis tardis);
	}

	/**
	 * Called whenever the fuel is set to 0
	 */
	@FunctionalInterface
	public interface NoFuel {
		void onNoFuel(Tardis tardis);
	}

	@FunctionalInterface
	public interface LosePower {
		/**
		 * Called when a tardis' loses power
		 *
		 * @param tardis
		 */
		void onLosePower(Tardis tardis);
	}

	@FunctionalInterface
	public interface RegainPower {
		/**
		 * Called when a tardis regains power
		 *
		 * @param tardis
		 */
		void onRegainPower(Tardis tardis);
	}

	/**
	 * Called when a TARDIS Door opens ( called when its state is set to any of the "open" states, but only if it was closed before )
	 */
	@FunctionalInterface
	public interface OpenDoor {
		void onOpen(Tardis tardis);
	}

	/**
	 * Called when a TARDIS Door closes
	 */
	@FunctionalInterface
	public interface CloseDoor {
		void onClose(Tardis tardis);
	}

	/**
	 * Called when the interior door position is changed, meaning it was probably moved
	 */
	@FunctionalInterface
	public interface MoveDoor {
		void onMove(Tardis tardis, AbsoluteBlockPos.Directed previous);
	}
}
