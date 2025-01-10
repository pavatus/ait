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
    public static final SoundEvent MERCURY_MUSIC = register("music/mercury_nitrogenez");
    public static final SoundEvent WONDERFUL_TIME_IN_SPACE = register("music/wonderful_time_in_space");

    // TARDIS

    public static final SoundEvent DEMAT = register("tardis/demat");
    public static final SoundEvent MAT = register("tardis/mat");
    public static final SoundEvent HOP_DEMAT = register("tardis/hop_takeoff");
    public static final SoundEvent HOP_MAT = register("tardis/hop_land");
    public static final SoundEvent FAIL_DEMAT = register("tardis/fail_takeoff");
    public static final SoundEvent FAIL_MAT = register("tardis/fail_land");
    public static final SoundEvent PHASING_DEMAT = register("tardis/engine_phasing_demat");
    public static final SoundEvent PHASING_REMAT = register("tardis/engine_phasing_remat");
    public static final SoundEvent EMERG_MAT = register("tardis/emergency_land");
    public static final SoundEvent FLIGHT_LOOP = register("tardis/flight_loop");
    public static final SoundEvent UNSTABLE_FLIGHT_LOOP = register("tardis/unstable_flight_loop");
    public static final SoundEvent LAND_THUD = register("tardis/land_thud");
    public static final SoundEvent SHUTDOWN = register("tardis/console_shutdown");
    public static final SoundEvent POWERUP = register("tardis/console_powerup");
    public static final SoundEvent WAYPOINT_ACTIVATE = register("tardis/waypoint_activate");

    public static final SoundEvent SIEGE_ENABLE = register("tardis/siege_enable");
    public static final SoundEvent SIEGE_DISABLE = register("tardis/siege_disable");

    //EIGHTH SOUNDS
    public static final SoundEvent EIGHTH_DEMAT = register("tardis/eighth_demat");
    public static final SoundEvent EIGHTH_FLIGHT = register("tardis/eighth_flight");
    public static final SoundEvent EIGHTH_MAT = register("tardis/eighth_mat");
    //GHOST MAT
    public static final SoundEvent GHOST_MAT = register("tardis/ghost_mat");
    //GHOSTBUSTERS SOUNDS
    public static final SoundEvent GB_DEMAT = register("tardis/gb_demat");
    public static final SoundEvent GB_FLIGHT = register("tardis/gb_flight");
    public static final SoundEvent GB_MAT = register("tardis/gb_mat");
    //PROTON SOUNDS
    public static final SoundEvent PROTON_FLIGHT = register("tardis/proton_flight");
    public static final SoundEvent PROTON_DEMAT = register("tardis/proton_demat");
    public static final SoundEvent PROTON_MAT = register("tardis/proton_mat");
    //TYPE 70 SOUNDS
    public static final SoundEvent TYPE70_DEMAT = register("tardis/type70_demat");
    public static final SoundEvent TYPE70_MAT = register("tardis/type70_mat");
    //CLASSIC SOUNDS
    public static final SoundEvent CLASSIC_DEMAT = register("tardis/classic_demat");
    public static final SoundEvent CLASSIC_MAT = register("tardis/classic_mat");
    public static final SoundEvent REGEN_DEMAT = register("tardis/regen_demat");
    //DRUMLESS SOUND
    public static final SoundEvent DRUMLESS_DEMAT = register("tardis/drumless_demat");
    //MINECART SOUNDS
    public static final SoundEvent MINECART_DEMAT = register("tardis/minecart_demat");
    public static final SoundEvent MINECART_FLIGHT = register("tardis/minecart_flight");
    public static final SoundEvent MINECART_MAT = register("tardis/minecart_mat");
    //MASTER SOUNDS
    public static final SoundEvent MASTER_DEMAT = register("tardis/master_demat");
    public static final SoundEvent MASTER_MAT = register("tardis/master_mat");
    //STYLE SOUNDS
    public static final SoundEvent STYLE_DEMAT = register("tardis/style_demat");
    public static final SoundEvent STYLE_REMAT = register("tardis/style_mat");
    //HALFLIFE
    //public static final SoundEvent HALFLIFE_DEMAT = register("tardis/halflife_demat");
    //public static final SoundEvent HALFLIFE_REMAT = register("tardis/halflife_mat");
    //public static final SoundEvent HALFLIFE_REMAT = register("tardis/halflife_mat");
    //PORTAL
    //public static final SoundEvent PORTAL_DEMAT = register("tardis/portal_demat");
    //public static final SoundEvent PORTAL_REMAT = register("tardis/portal_mat");
    //public static final SoundEvent PORTAL_REMAT = register("tardis/portal_mat");





    // TARDIS SFX
    public static final SoundEvent TARDIS_REJECTION_SFX = register("tardis/tardis_rejection_sfx");
    public static final SoundEvent MOODY1 = register("tardis/moody/moody1");
    public static final SoundEvent MOODY2 = register("tardis/moody/moody2");
    public static final SoundEvent MOODY3 = register("tardis/moody/moody3");
    public static final SoundEvent MOODY4 = register("tardis/moody/moody4");
    public static final SoundEvent MOODY5 = register("tardis/moody/moody5");


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
    public static final SoundEvent CHRISTMAS_HUM = register("tardis/hums/advent/christmas_hum");
    public static final SoundEvent EIGHT_HUM = register("tardis/hums/eight_hum");
    public static final SoundEvent COPPER_HUM = register("tardis/hums/copper_hum");
    public static final SoundEvent EXILE_HUM = register("tardis/hums/exile_hum");
    public static final SoundEvent PRIME_HUM = register("tardis/hums/prime_hum");
    public static final SoundEvent RENAISSANCE_HUM = register("tardis/hums/renaissance_hum");

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

    // Outside Sounds
    public static final SoundEvent VORTEX_SOUND = register("tardis/vortex_sound");
    public static final SoundEvent RAIN = register("tardis/exterior/rain");
    public static final SoundEvent THUNDER = register("tardis/exterior/thunder");

    // Tools
    public static final SoundEvent DING = register("tools/goes_ding");
    public static final SoundEvent STASER = register("tools/staser");

    // Fabricator
    public static final SoundEvent FABRICATOR_START = register("tools/fabricator/start");
    public static final SoundEvent FABRICATOR_END = register("tools/fabricator/end");
    public static final SoundEvent FABRICATOR_LOOP = register("tools/fabricator/loop");

    // Sonic
    public static final SoundEvent SONIC_USE = register("sonic/use");
    public static final SoundEvent SONIC_SWITCH = register("sonic/switch");

    // Secret
    public static final SoundEvent DOOM_DOOR_OPEN = register("tardis/secret/doom_door_open");
    public static final SoundEvent DOOM_DOOR_CLOSE = register("tardis/secret/doom_door_close");
    public static final SoundEvent ERROR = register("error");

    // Engine
    public static final SoundEvent ENGINE_REFUEL = register("tardis/engine_refuel");

    // Engine related stuff
    public static final SoundEvent POWER_CONVERT = register("tardis/power_convert");



    public static void init() {

    }
    private static SoundEvent register(String name) {
        return register(AITMod.id(name));
    }
    private static SoundEvent register(Identifier id) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    public static List<SoundEvent> getSounds(String modid) {
        return Registries.SOUND_EVENT.stream().filter(sound -> sound.getId().getNamespace().equals(modid)).toList();
    }
}
