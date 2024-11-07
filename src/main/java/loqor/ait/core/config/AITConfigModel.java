package loqor.ait.core.config;

import java.util.List;

import io.wispforest.owo.config.annotation.*;

import loqor.ait.AITMod;
import loqor.ait.core.AITDimensions;

@SuppressWarnings("unused")
@Modmenu(modId = AITMod.MOD_ID)
@Config(name = "aitconfig", wrapperName = "AITConfig")
public class AITConfigModel {

    @SectionHeader("Server")
    public boolean MINIFY_JSON = false;

    public boolean GHOST_MONUMENT = true;
    public boolean LOCK_DIMENSIONS = true;

    @Expanded @RestartRequired
    public List<String> WORLDS_BLACKLIST = List.of(
            AITDimensions.TIME_VORTEX_WORLD.getValue().toString());

    public int TRAVEL_PER_TICK = 2;

    public boolean SEND_BULK = true;
    public int MAX_TARDISES = -1;

    @SectionHeader("Client")
    public float INTERIOR_HUM_VOLUME = 0.2f;

    public boolean CUSTOM_MENU = true;
    public boolean SHOW_EXPERIMENTAL_WARNING = false;
    public boolean ENVIRONMENT_PROJECTOR = true;
    public boolean DISABLE_LOYALTY_FOG = false;


    public TemperatureType TEMPERATURE_TYPE = TemperatureType.CELCIUS;
    public enum TemperatureType {
        CELCIUS,
        FAHRENHEIT,
        KELVIN;
    }
}
