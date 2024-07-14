package loqor.ait.tardis.data;

import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import net.minecraft.server.MinecraftServer;

public class ServerRainHandler extends TardisComponent implements TardisTickable {

	public ServerRainHandler() {
		super(Id.RAINING);
	}

	public void enable() {
		this.set(true);
	}

	public void disable() {
		this.set(false);
	}

	private void set(boolean var) {
		PropertiesHandler.set(this.tardis(), PropertiesHandler.RAIN_FALLING, var);
	}

	public boolean isEnabled() {
		return PropertiesHandler.getBool(this.tardis().getHandlers().getProperties(), PropertiesHandler.RAIN_FALLING);
	}

	public void toggle() {
		if (isEnabled()) disable();
		else enable();
	}

	@Override
	public void tick(MinecraftServer server) {

		if (this.isEnabled() &&
				tardis.travel().position().getWorld() != null && !tardis.travel().position().getWorld().isRaining()) {
			this.disable();
			return;
		}

		if (this.isEnabled())
			return;

		if (tardis.travel().getState() != TravelHandlerBase.State.LANDED)
			return;

		if (tardis.travel().position().getWorld() != null && tardis.travel().position().getWorld().isRaining()) {
			this.enable();
		}
	}
}
