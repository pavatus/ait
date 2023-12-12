package mdteam.ait.core;

import mdteam.ait.AITMod;
import mdteam.ait.core.sounds.MatSound;
import mdteam.ait.core.sounds.SoundRegistryContainer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class AITSounds implements SoundRegistryContainer {
    public static final SoundEvent SECRET_MUSIC = SoundEvent.of(new Identifier(AITMod.MOD_ID, "secret_music"));

    // TARDIS
    public static final SoundEvent DEMAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/demat"));
    public static final SoundEvent MAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/mat"));
    public static final SoundEvent HOP_DEMAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/hop_takeoff"));
    public static final SoundEvent HOP_MAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/hop_land"));
    public static final SoundEvent FAIL_DEMAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/fail_takeoff"));
    public static final SoundEvent FAIL_MAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/fail_land"));
    public static final SoundEvent EMERG_MAT = SoundEvent.of(new Identifier(AITMod.MOD_ID, "tardis/emergency_land"));

    // Controls
    public static final SoundEvent DEMAT_LEVER_PULL = SoundEvent.of(new Identifier(AITMod.MOD_ID, "controls/demat_lever_pull"));
    public static final SoundEvent HANDBRAKE_LEVER_PULL = SoundEvent.of(new Identifier(AITMod.MOD_ID, "controls/handbrake_pull"));

    // FIXME: move somwehre else
    public static final MatSound DEMAT_ANIM = new MatSound(DEMAT, 240, 240, 240, 0.1f, 0.3f);
    public static final MatSound MAT_ANIM = new MatSound(MAT, 460, 240, 240, 0.2f, 0.4f);
    public static final MatSound LANDED_ANIM = new MatSound(null, 0,0,0,0,0);
}
