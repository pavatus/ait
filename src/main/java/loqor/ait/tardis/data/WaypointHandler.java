package loqor.ait.tardis.data;

import loqor.ait.core.item.WaypointItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.core.data.Waypoint;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public class WaypointHandler extends TardisLink {
	public static final String HAS_CARTRIDGE = "has_cartridge";
	private Waypoint current; // The current waypoint in the slot ( tried to make it optional, but that caused a gson crash )

	public WaypointHandler() {
		super(Id.WAYPOINTS);
	}

	public boolean hasCartridge() {
		return PropertiesHandler.getBool(this.tardis().getHandlers().getProperties(), HAS_CARTRIDGE);
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
	 * @param var
	 * @return The optional of the previous waypoiint
	 */
	public Optional<Waypoint> set(Waypoint var, boolean spawnItem) {
		Optional<Waypoint> prev = Optional.ofNullable(this.current);

		this.current = var;

		if (spawnItem && prev.isPresent()) {
			this.spawnItem(prev.get());
		}

		return prev;
	}

	public Waypoint get() {
		return this.current;
	}

	public boolean hasWaypoint() {
		return this.current != null;
	}

	public void clear(boolean spawnItem) {
		this.set(null, spawnItem);
	}

	public void gotoWaypoint() {
		if (!this.hasWaypoint())
			return; // todo move this check to the DEMAT event so the fail to takeoff happens

		PropertiesHandler.setAutoPilot(this.tardis().getHandlers().getProperties(), true);
		FlightUtil.travelTo(tardis(), this.get());
	}

	public void setDestination() {
		if (!this.hasWaypoint())
			return;

		this.tardis().getTravel().setDestination(this.get(), true);
	}

	public void spawnItem() {
		if (!this.hasWaypoint()) return;

		spawnItem(this.get());
		this.clear(false);
	}

	public void spawnItem(Waypoint waypoint) {
		if (!this.hasCartridge())
			return;

		Tardis tardis = this.tardis();

		if (tardis.getDesktop().findCurrentConsole().isEmpty())
			return;

		spawnItem(waypoint, tardis.getDesktop().findCurrentConsole().get().position());
		this.clearCartridge();
	}

	public static ItemStack createWaypointItem(Waypoint waypoint) {
		return WaypointItem.create(waypoint);
	}

	public static void spawnItem(Waypoint waypoint, AbsoluteBlockPos pos) {
		ItemEntity entity = new ItemEntity(pos.getWorld(), pos.getX(), pos.getY(), pos.getZ(), createWaypointItem(waypoint));
		pos.getWorld().spawnEntity(entity);
	}
}
