package loqor.ait.core;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.sounds.MatSound;
import loqor.ait.core.util.SoundRegistryContainer;

public class AITSounds implements SoundRegistryContainer {
    public static final SoundEvent SECRET_MUSIC = SoundEvent.of(new Identifier(AITMod.MOD_ID, "music/secret_music"));
    public static final SoundEvent EVEN_MORE_SECRET_MUSIC = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "music/even_more_secret_music"));
    public static final SoundEvent DRIFTING_MUSIC = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "music/drifting_by_radio"));
    public static final SoundEvent MERCURY_MUSIC = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "music/mercury_by_nitrogenesis"));

    // TARDIS
    public static final SoundEvent DEMAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/demat"));
    public static final SoundEvent MAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/mat"));
    public static final SoundEvent HOP_DEMAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/hop_takeoff"));
    public static final SoundEvent HOP_MAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/hop_land"));
    public static final SoundEvent FAIL_DEMAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/fail_takeoff"));
    public static final SoundEvent FAIL_MAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/fail_land"));
    public static final SoundEvent EMERG_MAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/emergency_land"));
    public static final SoundEvent FLIGHT_LOOP = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/flight_loop"));
    public static final SoundEvent UNSTABLE_FLIGHT_LOOP = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "tardis/unstable_flight_loop"));
    public static final SoundEvent LAND_THUD = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/land_thud"));
    public static final SoundEvent SHUTDOWN = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/console_shutdown"));
    public static final SoundEvent POWERUP = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/console_powerup"));
    public static final SoundEvent WAYPOINT_ACTIVATE = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "tardis/waypoint_activate"));

    public static final SoundEvent SIEGE_ENABLE = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/siege_enable"));
    public static final SoundEvent SIEGE_DISABLE = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/siege_disable"));

    public static final SoundEvent EIGHT_DEMAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/eighth_demat"));
    public static final SoundEvent EIGHT_MAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/eighth_mat"));
    public static final SoundEvent GHOST_MAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/ghost_mat"));

    // Controls
    public static final SoundEvent DEMAT_LEVER_PULL = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "controls/demat_lever_pull"));
    public static final SoundEvent HANDBRAKE_LEVER_PULL = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "controls/handbrake_lever_pull"));
    public static final SoundEvent HANDBRAKE_UP = SoundEvent.of(new Identifier(AITMod.MOD_ID, "controls/handbrake_up"));
    public static final SoundEvent HANDBRAKE_DOWN = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "controls/handbrake_down"));
    public static final SoundEvent CRANK = SoundEvent.of(new Identifier(AITMod.MOD_ID, "controls/crank"));
    public static final SoundEvent KNOCK = SoundEvent.of(new Identifier(AITMod.MOD_ID, "controls/knock"));
    public static final SoundEvent SNAP = SoundEvent.of(new Identifier(AITMod.MOD_ID, "controls/snap"));
    public static final SoundEvent BWEEP = SoundEvent.of(new Identifier(AITMod.MOD_ID, "controls/bweep"));

    // Hums
    public static final SoundEvent TOYOTA_HUM = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/hums/toyota_hum"));
    public static final SoundEvent CORAL_HUM = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/hums/coral_hum"));
    public static final SoundEvent EIGHT_HUM = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/hums/eight_hum"));
    public static final SoundEvent COPPER_HUM = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/hums/copper_hum"));
    public static final SoundEvent EXILE_HUM = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/hums/exile_hum"));
    public static final SoundEvent PRIME_HUM = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/hums/prime_hum"));
    public static final SoundEvent TOKAMAK_HUM = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "tardis/hums/tokamak_hum"));

    public static final SoundEvent CLOISTER = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/cloister"));
    public static final SoundEvent GROAN = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/groan"));

    // Creaks
    public static final SoundEvent CREAK_ONE = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/creaks/creak_one"));
    public static final SoundEvent CREAK_TWO = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/creaks/creak_two"));
    public static final SoundEvent CREAK_THREE = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "tardis/creaks/creak_three"));
    public static final SoundEvent CREAK_FOUR = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "tardis/creaks/creak_four"));
    public static final SoundEvent CREAK_FIVE = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "tardis/creaks/creak_five"));
    public static final SoundEvent CREAK_SIX = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/creaks/creak_six"));
    public static final SoundEvent CREAK_SEVEN = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "tardis/creaks/creak_seven"));
    public static final SoundEvent WHISPER = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/creaks/whisper"));

    // Vortex Sounds
    public static final SoundEvent VORTEX_SOUND = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/vortex_sound"));

    // Tools
    public static final SoundEvent DING = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tools/goes_ding"));

    // Sonic
    public static final SoundEvent SONIC_USE = SoundEvent.of(new Identifier(AITMod.MOD_ID, "sonic/use"));
    public static final SoundEvent SONIC_SWITCH = SoundEvent.of(new Identifier(AITMod.MOD_ID, "sonic/switch"));

    // FIXME: move somwehre else + these values suck
    public static final MatSound DEMAT_ANIM = new MatSound(DEMAT, 240, 240, 240, 210, 0.2f, 0.4f); // fixme especially
                                                                                                    // this one it
                                                                                                    // flickers bad
    public static final MatSound MAT_ANIM = new MatSound(MAT, 460, 240, 240, 400, 0.2f, 0.4f);
    public static final MatSound FLIGHT_ANIM = new MatSound(FLIGHT_LOOP, 120, 60, 60, 60, 0, 0); // tf is this even
    public static final MatSound EIGHT_DEMAT_ANIM = new MatSound(EIGHT_DEMAT, 8 * 20, 8 * 20, 8 * 20, 9 * 20, 0.1f,
            0.3f);
    public static final MatSound EIGHT_MAT_ANIM = new MatSound(EIGHT_MAT, 11 * 20, 11 * 20, 9 * 20, 9 * 20, 0.2f, 0.4f);
    public static final MatSound GHOST_MAT_ANIM = new MatSound(GHOST_MAT, 590, 320, 320, 320, 0.2f, 0.4f);

    // Secret
    public static final SoundEvent DOOM_DOOR_OPEN = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "tardis/secret/doom_door_open"));
    public static final SoundEvent DOOM_DOOR_CLOSE = SoundEvent
            .of(new Identifier(AITMod.MOD_ID, "tardis/secret/doom_door_close"));

    @Override
    public Registry<SoundEvent> getRegistry() {
        return Registries.SOUND_EVENT;
    }

    @Override
    public Class<SoundEvent> getTargetFieldType() {
        return SoundEvent.class;
    }
}
