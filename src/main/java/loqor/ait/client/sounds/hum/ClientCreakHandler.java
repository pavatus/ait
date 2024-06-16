package loqor.ait.client.sounds.hum;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PlayerFollowingLoopingSound;
import loqor.ait.client.sounds.PlayerFollowingSound;
import loqor.ait.client.util.ClientTardisUtil;
import loqor.ait.core.AITDimensions;
import loqor.ait.registry.impl.CreakRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.sound.CreakSound;
import loqor.ait.tardis.util.SoundHandler;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static loqor.ait.AITMod.AIT_CONFIG;

public class ClientCreakHandler extends SoundHandler {

	private static final Random random = new Random();

	public static ClientCreakHandler create() {
		if (MinecraftClient.getInstance().player == null) return null;

		ClientCreakHandler handler = new ClientCreakHandler();
		handler.generateCreaks();
		return handler;
	}

	private void generateCreaks() {
		this.sounds = new ArrayList<>();
		this.sounds.addAll(registryToList());
	}

	/**
	 * Converts all the {@link CreakSound}'s in the {@link CreakRegistry} to {@link LoopingSound} so they are usable
	 *
	 * @return A list of {@link LoopingSound} from the {@link CreakRegistry}
	 */
	private List<LoopingSound> registryToList() {
		List<LoopingSound> list = new ArrayList<>();

		for (CreakSound sound : CreakRegistry.REGISTRY) {
			list.add(new PlayerFollowingLoopingSound(sound.sound(), SoundCategory.AMBIENT, AIT_CONFIG.INTERIOR_HUM_VOLUME()));
		}

		return list;
	}

	public boolean isPlayerInATardis() {
		if (MinecraftClient.getInstance().world == null || MinecraftClient.getInstance().world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
			return false;

		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos(), false);

		return found != null;
	}

	public BlockPos randomNearConsolePos(BlockPos consolePos) {
		return consolePos.add(random.nextInt(8) - 1, 0, random.nextInt(8) - 1);
	}

	public Tardis tardis() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return null;
		return TardisUtil.findTardisByInterior(player.getBlockPos(), false);
	}

	public void playRandomCreak() {
		CreakSound chosen = CreakRegistry.getRandomCreak();
		ClientTardis current = (ClientTardis) ClientTardisUtil.getCurrentTardis();

		if (current == null)
			return;

		if (current.siege().isActive() && chosen.equals(CreakRegistry.WHISPER)) {
			current.getDesktop().getConsolePos().forEach(console ->
					startIfNotPlaying(new PositionedSoundInstance(
							chosen.sound(), SoundCategory.HOSTILE, 0.5f, 1.0f,
							net.minecraft.util.math.random.Random.create(),
							randomNearConsolePos(console))
					)
			);

			return;
		} else if (chosen.equals(CreakRegistry.WHISPER)) {
			return;
		}

		PlayerFollowingSound following = new PlayerFollowingSound(chosen.sound(), SoundCategory.AMBIENT, AIT_CONFIG.INTERIOR_HUM_VOLUME());
		startIfNotPlaying(following);
	}

	public void tick(MinecraftClient client) {
		if (this.sounds == null)
			this.generateCreaks();

		if (client.player == null)
			return;

		ClientTardis current = (ClientTardis) ClientTardisUtil.getCurrentTardis();

		if (!isPlayerInATardis()) {
			this.stopSounds();
			return;
		}

		if ((current.engine().hasPower() && (!current.inFlight() || current.flight().autoLand().get()))) { // todo should they play even with power? just make them more rare??
			this.stopSounds();
			return;
		}

		// theyre in a tardis and theres no power so play creaks boi
		if (random.nextInt(512) == 32) {
			this.playRandomCreak();
		}
	}
}