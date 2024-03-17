package loqor.ait.tardis.variant.door;

import loqor.ait.AITMod;
import loqor.ait.core.AITSounds;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class DoomDoorVariant extends DoorSchema {

	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "door/doom");

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
