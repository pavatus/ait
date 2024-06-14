package loqor.ait.client.sounds.alarm;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PlayerFollowingLoopingSound;
import loqor.ait.client.util.ClientShakeUtil;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.SoundHandler;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;

import java.util.ArrayList;

// All this is CLIENT ONLY!!
// Loqor, if you dont understand DONT TOUCH or ask me! - doozoo
public class ClientAlarmHandler extends SoundHandler {
	public static LoopingSound CLOISTER_INTERIOR;

	protected ClientAlarmHandler() {
	}

	public LoopingSound getInteriorCloister() {
		if (CLOISTER_INTERIOR == null)
			CLOISTER_INTERIOR = new PlayerFollowingLoopingSound(AITSounds.CLOISTER, SoundCategory.AMBIENT, 10f);

		return CLOISTER_INTERIOR;
	}

	public static ClientAlarmHandler create() {
		if (MinecraftClient.getInstance().player == null) return null;

		ClientAlarmHandler handler = new ClientAlarmHandler();
		handler.generate();
		return handler;
	}

	private void generate() {
		if (CLOISTER_INTERIOR == null)
			CLOISTER_INTERIOR = new PlayerFollowingLoopingSound(AITSounds.CLOISTER, SoundCategory.AMBIENT, 10f);

		this.sounds = new ArrayList<>();
		this.sounds.add(
				CLOISTER_INTERIOR
		);
	}

	public boolean isPlayerInATardis() {
		if (MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
			return false;
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos(), false);

		return found != null;
	}

	public Tardis tardis() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos(), false);
		return found;
	}

	public boolean isEnabled() {
		return PropertiesHandler.getBool(this.tardis().properties(), PropertiesHandler.ALARM_ENABLED);
	}

	public void tick(MinecraftClient client) {
		if (this.sounds == null) this.generate();

		if (isPlayerInATardis() && isEnabled()) {
			this.startIfNotPlaying(getInteriorCloister());

			ClientShakeUtil.shake(0.15f);
		} else {
			this.stopSounds();
		}
	}
}