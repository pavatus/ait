package mdteam.ait.config;

import com.neptunedevelopmentteam.neptunelib.config.ConfigComment;
import com.neptunedevelopmentteam.neptunelib.config.NeptuneConfig;
import com.neptunedevelopmentteam.neptunelib.config.NeptuneSubConfig;
import com.neptunedevelopmentteam.neptunelib.core.init_handlers.CustomName;

public class AITCustomConfig extends NeptuneConfig {

    public AITServerConfig SERVER = new AITServerConfig();
    public AITClientConfig CLIENT = new AITClientConfig();

    public static class AITServerConfig implements NeptuneSubConfig {
        @CustomName("Search Height")
        @ConfigComment("How many blocks up and down a TARDIS should ground search for before giving up")
        public int SEARCH_HEIGHT = 64;
        @CustomName("Ask Delay")
        @ConfigComment("How many seconds a client should be on delay for before asking for another TARDIS\nHigher -> Better server performance\nLower -> Less client sync issues")
        public double ASK_DELAY = 1.0;

        @CustomName("Force Sync Delay")
        @ConfigComment("How many seconds a client should be away from a tardis for before forcing a resync\nHigher -> Better server performance\nLower -> Less client sync issues")
        public double FORCE_SYNC_DELAY = 10;
    }

    public static class AITClientConfig implements NeptuneSubConfig {
        @CustomName("Interior Hum Volume")
        @ConfigComment("Volume of the interior hum")
        public float INTERIOR_HUM_VOLUME = 0.2f;

        @CustomName("Allow Custom Menu")
        @ConfigComment("Whether the custom title screen should show")
        public boolean CUSTOM_MENU = true;
    }
}
