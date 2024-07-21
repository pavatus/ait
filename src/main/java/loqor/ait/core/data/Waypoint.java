package loqor.ait.core.data;

import loqor.ait.core.item.WaypointItem;
import net.minecraft.item.ItemStack;

// todo for now this is identical to abpd but will eventually hold more
public class Waypoint {
	private String name;
	private final DirectedGlobalPos.Cached pos;

	public Waypoint(DirectedGlobalPos.Cached pos) {
		this.pos = pos;
	}

	public Waypoint withName(String name) {
		this.name = name;
		return this;
	}

	public String name() {
		return this.name;
	}

	public boolean hasName() {
		return this.name != null;
	}

	public DirectedGlobalPos.Cached getPos() {
		return pos;
	}

	public static Waypoint fromPos(DirectedGlobalPos.Cached pos) {
		return new Waypoint(pos);
	}

	public static Waypoint fromStack(ItemStack stack) {
		return fromPos(WaypointItem.getPos(stack))
				.withName(stack.getName().getString());
	}
}
