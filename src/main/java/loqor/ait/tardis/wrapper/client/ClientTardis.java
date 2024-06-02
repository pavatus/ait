package loqor.ait.tardis.wrapper.client;

import com.google.gson.InstanceCreator;
import loqor.ait.client.util.ClientShakeUtil;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisComponent;
import net.minecraft.client.MinecraftClient;

import java.lang.reflect.Type;
import java.util.UUID;

public class ClientTardis extends Tardis {

	private ClientTardis() {
        super();
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

	public static Object creator() {
		return new ClientTardisCreator();
	}

	static class ClientTardisCreator implements InstanceCreator<ClientTardis> {

		@Override
		public ClientTardis createInstance(Type type) {
			return new ClientTardis();
		}
	}
}