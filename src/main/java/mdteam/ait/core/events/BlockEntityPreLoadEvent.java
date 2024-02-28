package mdteam.ait.core.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class BlockEntityPreLoadEvent {

	private static boolean first = true;

	public static final Event<Load> LOAD = EventFactory.createArrayBacked(Load.class, callbacks -> () -> {
		if (!first)
			return;

		for (Load callback : callbacks) {
			callback.onBlockEntityPreLoad();
		}

		first = false;
	});

	@FunctionalInterface
	public interface Load {
		void onBlockEntityPreLoad();
	}
}