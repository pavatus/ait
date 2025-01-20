package loqor.ait.data.schema.door.impl;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import loqor.ait.data.schema.door.DoorSchema;

public class DoomDoorVariant extends DoorSchema {

    public static final Identifier REFERENCE = AITMod.id("door/doom");

    public DoomDoorVariant() {
        super(REFERENCE);
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public SoundEvent openSound() {
        return AITSounds.DOOM_DOOR_OPEN;
    }

    @Override
    public SoundEvent closeSound() {
        return AITSounds.DOOM_DOOR_CLOSE;
    }
}
