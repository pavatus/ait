package the.mdteam.ait;

import mdteam.ait.api.tardis.ITardis;
import mdteam.ait.api.tardis.ITardisManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ClientTardisManager extends TardisManager {

    private static final ClientTardisManager instance = new ClientTardisManager();
    public static final Identifier ASK = new Identifier("ait", "ask_tardis");

    public ClientTardisManager() {
        ClientPlayNetworking.registerGlobalReceiver(ServerTardisManager.SEND, (client, handler, buf, responseSender) -> {
            UUID uuid = buf.readUuid();
            ITardis tardis = this.gson.fromJson(buf.readString(), Tardis.class);

            this.lookup.put(uuid, tardis);
        });
    }

    public static ITardisManager getInstance() {
        return instance;
    }
}
