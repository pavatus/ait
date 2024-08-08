package loqor.ait.tardis.data;

import loqor.ait.core.data.Waypoint;
import loqor.ait.core.item.WaypointItem;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.data.travel.TravelUtil;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class WaypointHandler extends KeyedTardisComponent {
	public static final BoolProperty HAS_CARTRIDGE = new BoolProperty("has_cartridge", false);
	private final BoolValue hasCartridge = HAS_CARTRIDGE.create(this);
	private Waypoint current; // The current waypoint in the slot ( tried to make it optional, but that caused a gson crash )

	public WaypointHandler() {
		super(Id.WAYPOINTS);
	}

	@Override
	public void onLoaded() {
		hasCartridge.of(this, HAS_CARTRIDGE);
	}

	public boolean hasCartridge() {
		return hasCartridge.get();
	}

	public void markHasCartridge() {
		hasCartridge.set(true);
	}

	private void clearCartridge() {
		hasCartridge.set(false);
	}

	/**
	 * Sets the new waypoint
	 *
	 * @return The optional of the previous waypoiint
	 */
	public Optional<Waypoint> set(Waypoint var, BlockPos console, boolean spawnItem) {
		Optional<Waypoint> prev = Optional.ofNullable(this.current);

		if (spawnItem && this.current != null)
			this.spawnItem(console, prev.get());

		this.current = var;
		return prev;
	}

	public Waypoint get() {
		return this.current;
	}

	public boolean hasWaypoint() {
		return this.current != null;
	}

	public void clear(BlockPos console, boolean spawnItem) {
		this.set(null, console, spawnItem);
	}

	public void gotoWaypoint() {
		if (!this.hasWaypoint())
			return; // todo move this check to the DEMAT event so the fail to takeoff happens

		this.tardis().travel().autopilot(true);
		TravelUtil.travelTo(tardis(), this.get().getPos());
	}

	public void setDestination() {
		if (!this.hasWaypoint())
			return;

		this.tardis().travel().forceDestination(this.get().getPos());
	}

	public void spawnItem(BlockPos console) {
		if (!this.hasWaypoint())
			return;

		this.spawnItem(console, this.get());
		this.clear(console, false);
	}

	public void spawnItem(BlockPos console, Waypoint waypoint) {
		if (!this.hasCartridge())
			return;

		ItemEntity entity = new ItemEntity(TardisUtil.getTardisDimension(),
				console.getX(), console.getY(), console.getZ(), createWaypointItem(waypoint)
		);

		TardisUtil.getTardisDimension().spawnEntity(entity);
		this.clearCartridge();
	}

	public static ItemStack createWaypointItem(Waypoint waypoint) {
		return WaypointItem.create(waypoint);
	}
}
