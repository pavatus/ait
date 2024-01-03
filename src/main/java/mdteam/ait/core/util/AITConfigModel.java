package mdteam.ait.core.util;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.ui.component.ConfigSlider;
import mdteam.ait.AITMod;

@Modmenu(modId = AITMod.MOD_ID)
@Config(name = "ait-config", wrapperName = "AITConfig")
public class AITConfigModel {
    public int SEARCH_HEIGHT = 32;
    public float INTERIOR_HUM_VOLUME = 0.2f;
    public boolean CUSTOM_MENU = true;
}
