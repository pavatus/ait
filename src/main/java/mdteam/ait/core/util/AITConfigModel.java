package mdteam.ait.core.util;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = "ait")
@Config(name = "ait-config", wrapperName = "AITConfig")
public class AITConfigModel {
    public int SEARCH_HEIGHT = 32;
}
