package loqor.ait.core.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// todo for now this is identical to abpd but will eventually hold more
public class Waypoint extends AbsoluteBlockPos.Directed {
	private String name;

	public Waypoint(int x, int y, int z, SerialDimension dimension, int rotation) {
		super(x, y, z, dimension, rotation);
	}

	public Waypoint(BlockPos pos, SerialDimension dimension, int direction) {
		super(pos, dimension, direction);
	}

	public Waypoint(AbsoluteBlockPos pos, int rotation) {
		super(pos, rotation);
	}

	public Waypoint(int x, int y, int z, World world, int rotation) {
		super(x, y, z, world, rotation);
	}

	public Waypoint(BlockPos pos, World world, int rotation) {
		super(pos, world, rotation);
	}

	public Waypoint setName(String name) {
		this.name = name;
		return this;
	}

	public String name() {
		return this.name;
	}

	public boolean hasName() {
		return this.name != null;
	}

	public static Waypoint fromDirected(AbsoluteBlockPos.Directed pos) {
		return new Waypoint(pos, pos.getRotation());
	}
}
