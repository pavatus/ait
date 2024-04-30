package loqor.ait.core.util;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;
import loqor.ait.AITMod;

@Modmenu(modId = AITMod.MOD_ID)
@Config(name = "aitconfig", wrapperName = "AITConfig")
public class AITConfigModel {
	// todo these SHOULD really be all caps underlined
	@SectionHeader("Server")
	public int SEARCH_HEIGHT = 64;
	public double ASK_DELAY = 1.0;
	public double force_sync_delay = 10;
	public boolean MINIFY_JSON = false;
	@SectionHeader("Client")
	public float INTERIOR_HUM_VOLUME = 0.2f;
	public boolean CUSTOM_MENU = true;
}
