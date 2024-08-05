package loqor.ait.tardis.data;

import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import net.minecraft.server.MinecraftServer;

public class ServerRainHandler extends KeyedTardisComponent implements TardisTickable {
	private static final BoolProperty RAIN_FALLING = new BoolProperty("raining", false);
	private final BoolValue rainFalling = RAIN_FALLING.create(this);

	public ServerRainHandler() {
		super(Id.RAINING);
	}
	@Override
	public void onLoaded() {
		rainFalling.of(this, RAIN_FALLING);
	}

	public void enable() {
		this.set(true);
	}

	public void disable() {
		this.set(false);
	}

	private void set(boolean var) {
		rainFalling.set(var);
	}

	public boolean isEnabled() {
		return rainFalling.get();
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
