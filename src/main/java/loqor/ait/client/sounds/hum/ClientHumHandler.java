package loqor.ait.client.sounds.hum;

import loqor.ait.client.sounds.LoopingSound;
import loqor.ait.client.sounds.PlayerFollowingLoopingSound;
import loqor.ait.core.AITDimensions;
import loqor.ait.registry.impl.HumsRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.ServerHumHandler;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.sound.HumSound;
import loqor.ait.tardis.util.SoundHandler;
import loqor.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static loqor.ait.AITMod.AIT_CONFIG;

// All this is CLIENT ONLY!!
// Loqor, if you dont understand DONT TOUCH or ask me! - doozoo
public class ClientHumHandler extends SoundHandler {
	private LoopingSound current;
	private static final Random random = new Random();

	protected ClientHumHandler() {

		ClientPlayNetworking.registerGlobalReceiver(ServerHumHandler.SEND,
				(client, handler, buf, responseSender) -> {
					Identifier id = buf.readIdentifier();

					SoundInstance sound = findSoundById(id);

					if (sound.getId() == SoundEvents.INTENTIONALLY_EMPTY.getId()) return;
					if (!(sound instanceof LoopingSound)) return; // it aint a hum.

					this.setHum((LoopingSound) sound);
				});
	}

	public LoopingSound getHum() {
		if (this.current == null) {
			if (this.tardis() == null) return null;
			this.current = (LoopingSound) findSoundByEvent(this.tardis().<ServerHumHandler>handler(TardisComponent.Id.HUM).getHum().sound());
		}

		return this.current;
	}

	public void setHum(LoopingSound hum) {
		LoopingSound previous = this.getHum();

		this.current = hum;

		this.stopSound(previous);
	}

	public void setServersHum(HumSound hum) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeUuid(this.tardis().getUuid());
		buf.writeString(hum.id().getNamespace());
		buf.writeString(hum.name());

		ClientPlayNetworking.send(ServerHumHandler.RECEIVE, buf);
	}

	public static ClientHumHandler create() {
		if (MinecraftClient.getInstance().player == null) return null;

		ClientHumHandler handler = new ClientHumHandler();
		handler.generateHums();
		return handler;
	}

	private void generateHums() {
		this.sounds = new ArrayList<>();
		this.sounds.addAll(registryToList());
	}

	/**
	 * Converts all the {@link HumSound}'s in the {@link HumsRegistry} to {@link LoopingSound} so they are usable
	 *
	 * @return A list of {@link LoopingSound} from the {@link HumsRegistry}
	 */
	private List<LoopingSound> registryToList() {
		List<LoopingSound> list = new ArrayList<>();

		for (HumSound sound : HumsRegistry.REGISTRY) {
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

	public Tardis tardis() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player == null) return null;
		Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos(), false);
		return found;
	}

	public boolean isEnabled() {
		return PropertiesHandler.getBool(this.tardis().properties(), PropertiesHandler.HUM_ENABLED);
	}

	public void tick(MinecraftClient client) {
		if (this.sounds == null) this.generateHums();

		if (client.player == null) return;

		if (this.current != null && !isPlayerInATardis()) {
			this.current = null;
			return;
		}

		if (isPlayerInATardis() && isEnabled() && tardis().hasPower()) {
			this.startIfNotPlaying(this.getHum());
		} else {
			this.stopSounds();
		}
	}
}
