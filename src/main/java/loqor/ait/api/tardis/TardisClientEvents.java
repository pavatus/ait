package loqor.ait.api.tardis;

import loqor.ait.client.screens.interior.InteriorSettingsScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class TardisClientEvents {

    @Environment(EnvType.CLIENT)
    public static final Event<SettingsSetup> SETTINGS_SETUP = EventFactory.createArrayBacked(SettingsSetup.class, callbacks -> screen -> {
        for (SettingsSetup callback : callbacks) {
            callback.onSetup(screen);
        }
    });

    @FunctionalInterface
    @Environment(EnvType.CLIENT)
    public interface SettingsSetup {
        void onSetup(InteriorSettingsScreen screen);
    }

}
