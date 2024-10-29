package loqor.ait.core.tardis.handler;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.api.TardisComponent;
import loqor.ait.core.tardis.manager.ServerTardisManager;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.data.HumSound;
import loqor.ait.registry.impl.HumsRegistry;

public class ServerHumHandler extends TardisComponent {
    public static final Identifier SEND = new Identifier(AITMod.MOD_ID, "send_hum");
    public static final Identifier RECEIVE = new Identifier(AITMod.MOD_ID, "receive_hum");
    private HumSound current;

    static {
        ServerPlayNetworking.registerGlobalReceiver(ServerHumHandler.RECEIVE,
                ServerTardisManager.receiveTardis((tardis, server, player, handler, buf, responseSender) -> {
                    HumSound hum = HumSound.fromName(buf.readString(), buf.readString());

                    if (tardis == null || hum == null)
                        return;

                    tardis.<ServerHumHandler>handler(TardisComponent.Id.HUM).setHum(hum);
                }));
    }

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
