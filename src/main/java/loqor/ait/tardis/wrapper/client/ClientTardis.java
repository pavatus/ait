package loqor.ait.tardis.wrapper.client;

import com.google.gson.InstanceCreator;
import loqor.ait.AITMod;
import loqor.ait.client.util.ClientShakeUtil;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisComponent;
import net.minecraft.client.MinecraftClient;

import java.lang.reflect.Type;
import java.util.UUID;

public class ClientTardis extends Tardis {

	@Exclude private final UUID check;

	private ClientTardis(UUID check) {
        super();
		this.check = check;
    }

	public void setDesktop(TardisDesktop desktop) {
		desktop.setTardis(this);
		this.desktop = desktop;
	}

	public void setTravel(TardisTravel travel) {
		travel.setTardis(this);
		this.travel = travel;
	}

	public void setExterior(TardisExterior exterior) {
		exterior.setTardis(this);
		this.exterior = exterior;
	}

	@SuppressWarnings("deprecation") // intended
	public void set(TardisComponent component) {
		component.setTardis(this);
		component.onLoaded();

		this.handlers.set(component.getId(), component);
	}

	public void tick(MinecraftClient client) {
		// referencing client stuff where it COULD be server causes problems
		if (ClientShakeUtil.shouldShake(this)) {
			ClientShakeUtil.shakeFromConsole();
		}

		if (this.equals(ClientTardisUtil.getCurrentTardis())) {
			ClientTardisUtil.tickPowerDelta();
			ClientTardisUtil.tickAlarmDelta();
		}
	}

	@Override
	public <T extends TardisComponent> T handler(TardisComponent.IdLike type) {
		if (this.handlers == null) {
			AITMod.LOGGER.error("Asked for a handler too early on {}", this);
			return null;
		}

		return super.handler(type);
	}

	@Override
	public String toString() {
		return super.toString() + " (" + Integer.toHexString(check.hashCode()) + ")";
	}

	public static Object creator() {
		return new ClientTardisCreator();
	}

	static class ClientTardisCreator implements InstanceCreator<ClientTardis> {

		@Override
		public ClientTardis createInstance(Type type) {
			return new ClientTardis(UUID.randomUUID());
		}
	}
}