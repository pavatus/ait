package loqor.ait.tardis.data;

import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandler;
import net.minecraft.server.MinecraftServer;

public class CloakData extends TardisComponent implements TardisTickable {
	public static String CLOAKED = "is_cloaked";

	public CloakData() {
		super(Id.CLOAK);
	}

	public void enable() {
		PropertiesHandler.set(this.tardis(), CLOAKED, true);
	}

	public void disable() {
		PropertiesHandler.set(this.tardis(), CLOAKED, false);
	}

	public boolean isEnabled() {
		return PropertiesHandler.getBool(this.tardis().getHandlers().getProperties(), CLOAKED);
	}

	public void toggle() {
		if (isEnabled()) disable();
		else enable();
	}

	@Override
	public void tick(MinecraftServer server) {
		if (!this.isEnabled())
			return;

		if (!this.tardis.engine().hasPower())
			this.disable();

		TravelHandler travel = this.tardis.travel();

		if (travel.position().getPos() == null)
			return;

		this.tardis.removeFuel(2 * travel.instability()); // idle drain of 2 fuel per tick
	}
}
