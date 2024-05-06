package loqor.ait.tardis.data;

import loqor.ait.AITMod;
import loqor.ait.registry.impl.HumsRegistry;
import loqor.ait.tardis.sound.HumSound;
import loqor.ait.tardis.util.TardisUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ServerHumHandler extends TardisLink {
	public static final Identifier SEND = new Identifier(AITMod.MOD_ID, "send_hum");
	public static final Identifier RECEIVE = new Identifier(AITMod.MOD_ID, "receive_hum");
	private HumSound current;

	public ServerHumHandler() {
		super(Id.HUM);
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

		for (ServerPlayerEntity player : TardisUtil.getPlayersInInterior(this.tardis())) {
			ServerPlayNetworking.send(player, SEND, buf);
		}
	}
}
