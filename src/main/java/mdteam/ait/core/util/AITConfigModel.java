package mdteam.ait.core.util;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;
import io.wispforest.owo.config.ui.component.ConfigSlider;
import mdteam.ait.AITMod;

@Modmenu(modId = AITMod.MOD_ID)
@Config(name = "aitconfig", wrapperName = "AITConfig")
public class AITConfigModel {
    // todo these shouldnt really be all caps underlined
    @SectionHeader("Server")
    public int SEARCH_HEIGHT = 64;
    public double ASK_DELAY = 1.0;
    @SectionHeader("Client")
    public float INTERIOR_HUM_VOLUME = 0.2f;
    public boolean CUSTOM_MENU = true;
}
