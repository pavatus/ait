package loqor.ait.tardis.wrapper.server;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

//TODO: istg duzo :))))
public class ServerTardisTravel extends TardisTravel implements TardisTickable {

	public ServerTardisTravel(Tardis tardis, AbsoluteBlockPos.Directed pos) {
		super(tardis, pos);
	}

	@Override
	public void setDestination(AbsoluteBlockPos.Directed pos, boolean withChecks) {
		super.setDestination(pos, withChecks);
		this.sync();
	}

	@Override
	public void setPosition(AbsoluteBlockPos.Directed pos) {
		super.setPosition(pos);
		this.sync();
	}

	@Override
	public void setState(State state) {
		super.setState(state);
		this.sync();
	}

	public static double getSoundEventLengthInSeconds(SoundEvent sound) {
		try {
			// @TODO is no worky??
			AudioInputStream stream =
					(AudioInputStream) TardisUtil.getServer().getResourceManager().getResource(sound.getId().withPrefixedPath("assets/ait/sounds/")).get().getInputStream();
			AudioFormat format = stream.getFormat();
			long frames = stream.getFrameLength();
			return (frames + 0.0) / format.getFrameRate();
		} catch (Exception e) {
			e.printStackTrace();
			AITMod.LOGGER.error("Could not get sound length for " + sound + "! Returning 5.0");
			return 5.0D;
		}
	}

	@Override
	public void dematerialise(boolean withRemat) {
		super.dematerialise(withRemat);
		this.sync();
	}

	@Override
	public void materialise() {
		super.materialise();
	}

	@Override
	public void startTick(MinecraftServer server) {
	}

	@Override
	public void tick(MinecraftServer server) {
		super.tick(server);
	}

	@Override
	public void tick(ServerWorld world) {
	}

	@Override
	public void tick(MinecraftClient client) {
	}
}
