package loqor.ait.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@Environment(EnvType.CLIENT)
public class ClientWorldEvents {
    public static final Event<ChangeWorld> CHANGE_WORLD = EventFactory.createArrayBacked(ChangeWorld.class,
            callbacks -> () -> {
                for (ChangeWorld callback : callbacks) {
                    callback.onChange();
                }
            });

    @FunctionalInterface
    public interface ChangeWorld {
        void onChange();
    }
}
