package mdteam.ait.tardis.data;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.server.MinecraftServer;

public class CloakData extends TardisLink {
	public static String CLOAKED = "is_cloaked";

	public CloakData(Tardis tardis) {
		super(tardis, "cloak");
	}

	public void enable() {
		if (this.findTardis().isEmpty()) return;

		PropertiesHandler.set(this.findTardis().get(), CLOAKED, true);
	}

	public void disable() {
		if (this.findTardis().isEmpty()) return;

		PropertiesHandler.set(this.findTardis().get(), CLOAKED, false);
	}

	public boolean isEnabled() {
		if (this.findTardis().isEmpty()) return false;

		return PropertiesHandler.getBool(this.findTardis().get().getHandlers().getProperties(), CLOAKED);
	}

	public void toggle() {
		if (isEnabled()) disable();
		else enable();
	}

	@Override
	public void tick(MinecraftServer server) {
		super.tick(server);

		if (findTardis().isEmpty() || this.isEnabled() && !findTardis().get().hasPower()) {
			this.disable();
			return;
		}

		if (this.findTardis().get().getExterior().getExteriorPos() == null) return;

		if (!this.isEnabled()) return;

		this.findTardis().get().removeFuel(2 * (this.findTardis().get().tardisHammerAnnoyance + 1)); // idle drain of 2 fuel per tick
	}
}
