package loqor.ait.core.util;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;
import loqor.ait.AITMod;
import loqor.ait.core.AITDimensions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

@SuppressWarnings("unused")
@Modmenu(modId = AITMod.MOD_ID)
@Config(name = "aitconfig", wrapperName = "AITConfig")
public class AITConfigModel {

	@SectionHeader("Server")
	public boolean MINIFY_JSON = false;
	public RegistryKey<World>[] WORLDS_BLACKLIST = new RegistryKey[] { AITDimensions.TIME_VORTEX_WORLD, AITDimensions.TARDIS_DIM_WORLD };

	@SectionHeader("Client")
	public float INTERIOR_HUM_VOLUME = 0.2f;
	public boolean CUSTOM_MENU = true;
	public boolean SHOW_EXPERIMENTAL_WARNING = false;
	public boolean ENVIRONMENT_PROJECTOR = true;
	public boolean DISABLE_CONSOLE_ANIMATIONS = false;
	public boolean DISABLE_LOYALTY_FOG = false;
}
