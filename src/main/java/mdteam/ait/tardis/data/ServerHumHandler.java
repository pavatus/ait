package mdteam.ait.tardis.data;

import mdteam.ait.AITMod;
import mdteam.ait.registry.HumsRegistry;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.sound.HumSound;
import mdteam.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

// Loqor, if you dont understand DONT TOUCH or ask me! - doozoo
public class ServerHumHandler extends TardisLink {
	public static final Identifier SEND = new Identifier(AITMod.MOD_ID, "send_hum");
	public static final Identifier RECEIVE = new Identifier(AITMod.MOD_ID, "receive_hum");
	private HumSound current;

	public ServerHumHandler(Tardis tardisId) {
		super(tardisId, "hum");
	}

	public HumSound getHum() {
		if (current == null) {
			this.current = HumsRegistry.TOYOTA;
		}

		return this.current;
	}

	public void setHum(HumSound hum) {
		this.current = hum;

		this.updateClientHum();
	}

	private void updateClientHum() {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeIdentifier(this.current.sound().getId());
		if (this.findTardis().isEmpty()) return;

		for (PlayerEntity player : TardisUtil.getPlayersInInterior(this.findTardis().get())) { // is bad? fixme
			ServerPlayNetworking.send((ServerPlayerEntity) player, SEND, buf);
		}
	}
}
