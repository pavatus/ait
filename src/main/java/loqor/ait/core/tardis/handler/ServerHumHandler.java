package loqor.ait.tardis.handler;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.api.TardisComponent;
import loqor.ait.data.HumSound;
import loqor.ait.registry.impl.HumsRegistry;
import loqor.ait.tardis.util.TardisUtil;

public class ServerHumHandler extends TardisComponent {
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

        for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(this.tardis().asServer())) {
            ServerPlayNetworking.send(player, SEND, buf);
        }
    }
}
