package loqor.ait.data;

import net.minecraft.item.ItemStack;

import loqor.ait.core.item.WaypointItem;

public class Waypoint {

    private String name;
    private final DirectedGlobalPos.Cached pos;

    public Waypoint(String name, DirectedGlobalPos.Cached pos) {
        this.name = name;
        this.pos = pos;
    }

    private Waypoint(DirectedGlobalPos.Cached pos) {
        this(null, pos);
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
        return new Waypoint(stack.getName().getString(), WaypointItem.getPos(stack));
    }
}
