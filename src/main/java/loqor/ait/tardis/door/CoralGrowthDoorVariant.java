package loqor.ait.tardis.door;

import loqor.ait.AITMod;
import loqor.ait.core.data.schema.door.DoorSchema;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class CoralGrowthDoorVariant extends DoorSchema {

	public static final Identifier REFERENCE = new Identifier(AITMod.MOD_ID, "door/coral_growth");

	public CoralGrowthDoorVariant() {
		super(REFERENCE);
	}

	@Override
	public boolean isDouble() {
		return true;
	}

	@Override
	public SoundEvent openSound() {
		return SoundEvents.BLOCK_WOODEN_DOOR_OPEN;
	}

	@Override
	public SoundEvent closeSound() {
		return SoundEvents.BLOCK_WOODEN_DOOR_CLOSE;
	}

	@Override
	public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
		return switch (direction) {
			case DOWN, UP -> pos;
			case NORTH -> pos.add(0, 0.1, -0.36);
			case SOUTH -> pos.add(0, 0.1, 0.36);
			case WEST -> pos.add(-0.36, 0.1, 0);
			case EAST -> pos.add(0.36, 0.1, 0);
		};
	}
}
