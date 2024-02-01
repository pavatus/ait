package mdteam.ait;

import com.neptunedevelopmentteam.neptunelib.config.ConfigComment;
import com.neptunedevelopmentteam.neptunelib.config.NeptuneConfig;
import com.neptunedevelopmentteam.neptunelib.config.NeptuneSubConfig;
import com.neptunedevelopmentteam.neptunelib.core.init_handlers.CustomName;

public class AITCustomConfig extends NeptuneConfig {

    public AITServerConfig SERVER = new AITServerConfig();
    public AITClientConfig CLIENT = new AITClientConfig();
    @CustomName("Tardis Resync Delay")
    @ConfigComment("Delay in seconds between resyncing the tardis")
    public double ASK_DELAY = 1.0;

    @CustomName("Force Sync Delay")
    @ConfigComment("Delay in seconds between forcing a resync")
    public double FORCE_SYNC_DELAY = 10;

    public static class AITServerConfig implements NeptuneSubConfig {
        @CustomName("Search Height")
        @ConfigComment("Height of the search area in the server")
        public int SEARCH_HEIGHT = 64;
    }

    public static class AITClientConfig implements NeptuneSubConfig {
        @CustomName("Interior Hum Volume")
        @ConfigComment("Volume of the interior hum")
        public float INTERIOR_HUM_VOLUME = 0.2f;

        @CustomName("Allow Custom Menu")
        @ConfigComment("Show the custom menu if true")
        public boolean CUSTOM_MENU = true;
    }
}
