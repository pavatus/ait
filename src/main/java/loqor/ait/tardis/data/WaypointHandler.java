package loqor.ait.tardis.data;

import loqor.ait.core.item.WaypointItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.AbsoluteBlockPos;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.util.Waypoint;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public class WaypointHandler extends TardisLink {
	public static final String HAS_CARTRIDGE = "has_cartridge";
	private Waypoint current; // The current waypoint in the slot ( tried to make it optional, but that caused a gson crash )

	public WaypointHandler(Tardis tardis) {
		super(tardis, TypeId.WAYPOINT);
	}

	public boolean hasCartridge() {
		if (this.findTardis().isEmpty()) return false;
		return PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), HAS_CARTRIDGE);
	}

	public void markHasCartridge() {
		if (this.findTardis().isEmpty()) return;
		PropertiesHandler.set(this.findTardis().get(), HAS_CARTRIDGE, true);
	}

	private void clearCartridge() {
		if (this.findTardis().isEmpty()) return;
		PropertiesHandler.set(this.findTardis().get(), HAS_CARTRIDGE, false);
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
		if (findTardis().isEmpty() || !this.hasWaypoint())
			return; // todo move this check to the DEMAT event so the fail to takeoff happens

		PropertiesHandler.setAutoPilot(this.findTardis().get().getHandlers().getProperties(), true);
		FlightUtil.travelTo(findTardis().get(), this.get());
	}

	public void setDestination() {
		if (findTardis().isEmpty() || !this.hasWaypoint()) return;

		this.findTardis().get().getTravel().setDestination(this.get(), true);
	}

	public void spawnItem() {
		if (!this.hasWaypoint()) return;

		spawnItem(this.get());
		this.clear(false);
	}

	public void spawnItem(Waypoint waypoint) {
		if (findTardis().isEmpty() || !this.hasCartridge()) return;

		Tardis tardis = this.findTardis().get();

		if (tardis.getDesktop().findCurrentConsole().isEmpty()) return;

		spawnItem(waypoint, tardis.getDesktop().findCurrentConsole().get().position());
		this.clearCartridge();
	}

	public static ItemStack createWaypointItem(Waypoint waypoint) {
		return WaypointItem.create(waypoint);
	}

	public static ItemEntity spawnItem(Waypoint waypoint, AbsoluteBlockPos pos) {
		ItemEntity entity = new ItemEntity(pos.getWorld(), pos.getX(), pos.getY(), pos.getZ(), createWaypointItem(waypoint));
		pos.getWorld().spawnEntity(entity);
		return entity;
	}
}
