package loqor.ait.client.sounds.rwf;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PlayerFollowingLoopingSound;
import loqor.ait.client.sounds.flight.InteriorFlightSound;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.AITSounds;
import loqor.ait.core.entities.TardisRealEntity;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.SoundHandler;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;

import java.util.ArrayList;
import java.util.Properties;

public class ClientRWFSoundsHandler extends SoundHandler {
	public static LoopingSound RWFSOUND;

	protected ClientRWFSoundsHandler() {
	}

	public LoopingSound getFlightLoop() {
		if (RWFSOUND == null)
			RWFSOUND = createFlightSound();

		return RWFSOUND;
	}

	private LoopingSound createFlightSound() {
		if (tardis().crash().isToxic() || tardis().crash().isUnstable()) {
			return new PlayerFollowingLoopingSound(AITSounds.UNSTABLE_FLIGHT_LOOP, SoundCategory.PLAYERS, 1f);
		}
		return new PlayerFollowingLoopingSound(AITSounds.FLIGHT_LOOP, SoundCategory.PLAYERS, 1f);
	}

	public static ClientRWFSoundsHandler create() {
		if (MinecraftClient.getInstance().player == null) return null;

		ClientRWFSoundsHandler handler = new ClientRWFSoundsHandler();
		handler.generate();
		return handler;
	}

	private void generate() {
		if (tardis() == null) return;

		if (RWFSOUND == null) RWFSOUND = createFlightSound();

		this.sounds = new ArrayList<>();
		this.sounds.add(
				RWFSOUND
		);
	}

	public Tardis tardis() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;

		if (player == null)
			return null;

		return player.getVehicle() instanceof TardisRealEntity realEntity && realEntity.getTardis() != null ? realEntity.getTardis() : null;
	}

	private void playFlightSound() {
		this.startIfNotPlaying(this.getFlightLoop());
		this.getFlightLoop().tick();
	}

	private boolean shouldPlaySounds(MinecraftClient client) {
		if(client.player == null || tardis() == null) return false;

		return PropertiesHandler.getBool(tardis().properties(), PropertiesHandler.IS_IN_REAL_FLIGHT) && !client.player.getVehicle().isOnGround();
	}

	public void tick(MinecraftClient client) {
		if (this.sounds == null) this.generate();

		if(client.player == null) return;

		if (shouldPlaySounds(client) &&
				client.player.getWorld().getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD) {
			this.playFlightSound();
		} else {
			this.stopSounds();
		}
	}
}
