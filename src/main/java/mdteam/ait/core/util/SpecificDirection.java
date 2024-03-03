package mdteam.ait.core.util;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Direction;

public enum SpecificDirection implements StringIdentifiable {
	NORTH("north", 0, Direction.NORTH),
	NORTH_EAST("north_east", 45, Direction.NORTH),
	EAST("east", 90, Direction.EAST),
	SOUTH_EAST("south_east", 135, Direction.SOUTH),
	SOUTH("south", 180, Direction.SOUTH),
	SOUTH_WEST("south_west", 225, Direction.SOUTH),
	WEST("west", 270, Direction.WEST),
	NORTH_WEST("north_west", 315, Direction.NORTH);

	private final String name;
	private final float rotation;
	private final Direction parent;

	SpecificDirection(String name, float rotation, Direction parent) {
		this.name = name;
		this.rotation = rotation;
		this.parent = parent;
	}

	@Override
	public String asString() {
		return this.name;
	}

	public String asName() {
		String[] words = this.asString().split("_");

		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
		}

		return String.join(" ", words);
	}

	public float toRotation() {
		return this.rotation;
	}

	public Direction toDirection() {
		return this.parent;
	}

	public static SpecificDirection fromDirection(Direction dir) {
		return switch (dir) {
			case NORTH -> SpecificDirection.NORTH;
			case EAST -> SpecificDirection.EAST;
			case SOUTH -> SpecificDirection.SOUTH;
			case WEST -> SpecificDirection.WEST;
			default -> SpecificDirection.NORTH;
		};
	}

	public static SpecificDirection fromRotation(float rotation) {
		float snapped = snapRotation(rotation);

		for (SpecificDirection dir : SpecificDirection.values()) {
			if (dir.toRotation() == snapped) {
				return dir;
			}
		}

		return SpecificDirection.NORTH;
	}
	public static float snapRotation(float rotation) {
		rotation = rotation % 360;
		if (rotation < 0) {
			rotation += 360;
		}

		float minDifference = Float.MAX_VALUE;
		float snappedAngle = 0;

		for (SpecificDirection dir : SpecificDirection.values()) {
			float angle = dir.rotation;

			float difference = Math.abs(rotation - angle);
			if (difference < minDifference) {
				minDifference = difference;
				snappedAngle = angle;
			}
		}

		return snappedAngle;
	}
}
