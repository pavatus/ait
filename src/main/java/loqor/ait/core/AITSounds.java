package loqor.ait.core;

import java.util.List;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;

public class AITSounds {
    // public static final SoundEvent SECRET_MUSIC = register("music/secret_music"); // removed for its large size - if you bring it back, COMPRESS IT
    public static final SoundEvent EVEN_MORE_SECRET_MUSIC = register("music/even_more_secret_music");
    public static final SoundEvent DRIFTING_MUSIC = register("music/drifting_by_radio");
    // public static final SoundEvent MERCURY_MUSIC = register("music/mercury_by_nitrogenesis"); // removed for its large size - if you bring it back, COMPRESS IT

    // TARDIS
    public static final SoundEvent DEMAT = register("tardis/demat");
    public static final SoundEvent MAT = register("tardis/mat");
    public static final SoundEvent HOP_DEMAT = register("tardis/hop_takeoff");
    public static final SoundEvent HOP_MAT = register("tardis/hop_land");
    public static final SoundEvent FAIL_DEMAT = register("tardis/fail_takeoff");
    public static final SoundEvent FAIL_MAT = register("tardis/fail_land");
    public static final SoundEvent PHASING_DEMAT = register("tardis/engine_phasing_demat");
    public static final SoundEvent EMERG_MAT = register("tardis/emergency_land");
    public static final SoundEvent FLIGHT_LOOP = register("tardis/flight_loop");
    public static final SoundEvent UNSTABLE_FLIGHT_LOOP = register("tardis/unstable_flight_loop");
    public static final SoundEvent LAND_THUD = register("tardis/land_thud");
    public static final SoundEvent SHUTDOWN = register("tardis/console_shutdown");
    public static final SoundEvent POWERUP = register("tardis/console_powerup");
    public static final SoundEvent WAYPOINT_ACTIVATE = register("tardis/waypoint_activate");

    public static final SoundEvent SIEGE_ENABLE = register("tardis/siege_enable");
    public static final SoundEvent SIEGE_DISABLE = register("tardis/siege_disable");

    public static final SoundEvent EIGHTH_DEMAT = register("tardis/eighth_demat");
    public static final SoundEvent EIGHTH_FLIGHT = register("tardis/eighth_flight");
    public static final SoundEvent EIGHTH_MAT = register("tardis/eighth_mat");
    public static final SoundEvent GHOST_MAT = register("tardis/ghost_mat");

    // TARDIS SFX
    public static final SoundEvent TARDIS_REJECTION_SFX = register("tardis/tardis_rejection_sfx");

    // Controls
    public static final SoundEvent DEMAT_LEVER_PULL = register("controls/demat_lever_pull");
    public static final SoundEvent HANDBRAKE_LEVER_PULL = register("controls/handbrake_lever_pull");
    public static final SoundEvent HANDBRAKE_UP = register("controls/handbrake_up");
    public static final SoundEvent HANDBRAKE_DOWN = register("controls/handbrake_down");
    public static final SoundEvent CRANK = register("controls/crank");
    public static final SoundEvent KNOCK = register("controls/knock");
    public static final SoundEvent SNAP = register("controls/snap");
    public static final SoundEvent BWEEP = register("controls/bweep");

    // Console
    public static final SoundEvent CONSOLE_AMBIENT = register("tardis/console_ambient");
    public static final SoundEvent CONSOLE_BOOTUP = register("tardis/console_bootup");

    // Hums
    public static final SoundEvent TOYOTA_HUM = register("tardis/hums/toyota_hum");
    public static final SoundEvent CORAL_HUM = register("tardis/hums/coral_hum");
    public static final SoundEvent EIGHT_HUM = register("tardis/hums/eight_hum");
    public static final SoundEvent COPPER_HUM = register("tardis/hums/copper_hum");
    public static final SoundEvent EXILE_HUM = register("tardis/hums/exile_hum");
    public static final SoundEvent PRIME_HUM = register("tardis/hums/prime_hum");
    public static final SoundEvent TOKAMAK_HUM = register("tardis/hums/tokamak_hum");

    public static final SoundEvent CLOISTER = register("tardis/cloister");
    public static final SoundEvent GROAN = register("tardis/groan");

    // Creaks
    public static final SoundEvent CREAK_ONE = register("tardis/creaks/creak_one");
    public static final SoundEvent CREAK_TWO = register("tardis/creaks/creak_two");
    public static final SoundEvent CREAK_THREE = register("tardis/creaks/creak_three");
    public static final SoundEvent CREAK_FOUR = register("tardis/creaks/creak_four");
    public static final SoundEvent CREAK_FIVE = register("tardis/creaks/creak_five");
    public static final SoundEvent CREAK_SIX = register("tardis/creaks/creak_six");
    public static final SoundEvent CREAK_SEVEN = register("tardis/creaks/creak_seven");
    public static final SoundEvent WHISPER = register("tardis/creaks/whisper");

    // Vortex Sounds
    public static final SoundEvent VORTEX_SOUND = register("tardis/vortex_sound");
    public static final SoundEvent RAIN = register("tardis/exterior/rain");
    public static final SoundEvent THUNDER = register("tardis/exterior/thunder");

    // Tools
    public static final SoundEvent DING = register("tools/goes_ding");
    public static final SoundEvent STASER = register("tools/staser");

    // Sonic
    public static final SoundEvent SONIC_USE = register("sonic/use");
    public static final SoundEvent SONIC_SWITCH = register("sonic/switch");

    // Secret
    public static final SoundEvent DOOM_DOOR_OPEN = register("tardis/secret/doom_door_open");
    public static final SoundEvent DOOM_DOOR_CLOSE = register("tardis/secret/doom_door_close");
    public static final SoundEvent ERROR = register("error");

    public static void init() {

    }
    private static SoundEvent register(String name) {
        return register(new Identifier(AITMod.MOD_ID, name));
    }
    private static SoundEvent register(Identifier id) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    public static List<SoundEvent> getSounds(String modid) {
        return Registries.SOUND_EVENT.stream().filter(sound -> sound.getId().getNamespace().equals(modid)).toList();
    }
}
