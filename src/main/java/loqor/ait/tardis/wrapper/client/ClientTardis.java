package loqor.ait.tardis.wrapper.client;

import com.google.gson.InstanceCreator;
import loqor.ait.AITMod;
import loqor.ait.client.util.ClientShakeUtil;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.DoorData;
import loqor.ait.tardis.data.SonicHandler;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.minecraft.client.MinecraftClient;

import java.lang.reflect.Type;

public class ClientTardis extends Tardis {

	/**
	 * @deprecated NEVER EVER use this constructor. It's for GSON to call upon deserialization!
	 */
	@Deprecated
	@SuppressWarnings("unused")
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

	public void setSonic(SonicHandler sonic) {
		sonic.setTardis(this);
		this.handlers.set(TardisComponent.Id.SONIC, sonic);
	}

	public void setExterior(TardisExterior exterior) {
		exterior.setTardis(this);
		this.exterior = exterior;
	}

	public void setDoor(DoorData door) {
		door.setTardis(this);
		AITMod.LOGGER.info("previous: {}-{}; rec new door: {}-{}; self = {}", this.getDoor(), this.getDoor().getDoorState(), door, door.getDoorState(), this);
		this.handlers.set(TardisComponent.Id.DOOR, door);
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