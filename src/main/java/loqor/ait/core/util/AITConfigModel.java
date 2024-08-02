package loqor.ait.core.util;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;
import loqor.ait.AITMod;

@SuppressWarnings("unused")
@Modmenu(modId = AITMod.MOD_ID)
@Config(name = "aitconfig", wrapperName = "AITConfig")
public class AITConfigModel {

	@SectionHeader("Server")
	public boolean MINIFY_JSON = false;

	@SectionHeader("Client")
	public float INTERIOR_HUM_VOLUME = 0.2f;
	public boolean CUSTOM_MENU = true;
	public boolean SHOW_EXPERIMENTAL_WARNING = false;
	public boolean ENVIRONMENT_PROJECTOR = true;
	public boolean DISABLE_CONSOLE_ANIMATIONS = false;
	public boolean DISABLE_LOYALTY_FOG = false;
}
