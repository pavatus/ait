package loqor.ait.core.data;

// todo for now this is identical to abpd but will eventually hold more
public class Waypoint {
	private String name;
	private final DirectedGlobalPos.Cached pos;

	public Waypoint(DirectedGlobalPos.Cached pos) {
		this.pos = pos;
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

	public DirectedGlobalPos.Cached getPos() {
		return pos;
	}

	public static Waypoint fromDirected(DirectedGlobalPos.Cached pos) {
		return new Waypoint(pos);
	}
}
