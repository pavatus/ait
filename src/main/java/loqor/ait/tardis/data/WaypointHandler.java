package loqor.ait.tardis.data;

import loqor.ait.core.data.Waypoint;
import loqor.ait.core.item.WaypointItem;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelUtil;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class WaypointHandler extends TardisComponent {
	public static final String HAS_CARTRIDGE = "has_cartridge";
	private Waypoint current; // The current waypoint in the slot ( tried to make it optional, but that caused a gson crash )

	public WaypointHandler() {
		super(Id.WAYPOINTS);
	}

	public boolean hasCartridge() {
		return PropertiesHandler.getBool(this.tardis().properties(), HAS_CARTRIDGE);
	}

	public void markHasCartridge() {
		PropertiesHandler.set(this.tardis(), HAS_CARTRIDGE, true);
	}

	private void clearCartridge() {
		PropertiesHandler.set(this.tardis(), HAS_CARTRIDGE, false);
	}

	/**
	 * Sets the new waypoint
	 *
	 * @return The optional of the previous waypoiint
	 */
	public Optional<Waypoint> set(Waypoint var, BlockPos console, boolean spawnItem) {
		Optional<Waypoint> prev = Optional.ofNullable(this.current);

		this.current = var;

		if (spawnItem && prev.isPresent())
			this.spawnItem(console, prev.get());

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

		this.tardis().travel().destination(this.get().getPos());
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
