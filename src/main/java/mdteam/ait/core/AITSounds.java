package mdteam.ait.core;

import mdteam.ait.AITMod;
import mdteam.ait.core.sounds.SoundRegistryContainer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class AITSounds implements SoundRegistryContainer {
    public static final SoundEvent STIMPAK_USE = SoundEvent.of(new Identifier(AITMod.MOD_ID, "stimpak_use"));
    public static final SoundEvent DEMATERIALIZE = SoundEvent.of(new Identifier(AITMod.MOD_ID, "dematerialize"));
    public static final SoundEvent TITLE_MUSIC = SoundEvent.of(new Identifier(AITMod.MOD_ID, "title_music"));
    public static final SoundEvent BLAST_DOOR_OPEN = SoundEvent.of(new Identifier(AITMod.MOD_ID, "blast_door_open"));
    public static final SoundEvent SECRET_MUSIC = SoundEvent.of(new Identifier(AITMod.MOD_ID, "secret_music"));
}
