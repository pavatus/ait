package mdteam.ait.tardis.wrapper.client.manager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.GsonBuilder;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.SerialDimension;
import mdteam.ait.tardis.wrapper.server.manager.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import mdteam.ait.tardis.wrapper.client.ClientTardis;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;
import java.util.function.Consumer;

public class ClientTardisManager extends TardisManager {

    public static final Identifier ASK = new Identifier("ait", "ask_tardis");
    public static final Identifier ASK_POS = new Identifier("ait", "ask_pos_tardis");
    private static final ClientTardisManager instance = new ClientTardisManager();
    private final Multimap<UUID, Consumer<Tardis>> subscribers = ArrayListMultimap.create();
    private final Deque<PacketByteBuf> buffers = new ArrayDeque<>();

    public ClientTardisManager() {
        if(FabricLauncherBase.getLauncher().getEnvironmentType() == EnvType.CLIENT) {
            ClientPlayNetworking.registerGlobalReceiver(ServerTardisManager.SEND,
                    (client, handler, buf, responseSender) -> this.sync(buf)
            );

            ClientPlayNetworking.registerGlobalReceiver(ServerTardisManager.UPDATE,
                    (client, handler, buf, responseSender) -> {
                        UUID uuid = buf.readUuid();

                        if (!this.lookup.containsKey(uuid))
                            return;

                        this.sync(uuid, buf);
            });

            ClientTickEvents.END_WORLD_TICK.register(world -> {
                for (int i = 0; i < this.buffers.size(); i++) {
                    ClientPlayNetworking.send(ASK, this.buffers.pop());
                }
            });

            ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> this.reset());
        }
    }

    @Override
    public void loadTardis(UUID uuid, Consumer<Tardis> consumer) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(uuid);

        this.subscribers.put(uuid, consumer);
        this.buffers.add(data);
    }

    private void sync(UUID uuid, String json) {
        Tardis tardis = this.gson.fromJson(json, ClientTardis.class);

        ExteriorBlockEntity entity = TardisUtil.findExteriorEntity(tardis);
        if (entity != null) {
            if (!entity.getAnimation().hasAnimationStarted()) // fixme this breaks the demat animation as for some reason the tardis gets spammed with updates on demat ( ? )
                entity.getAnimation().setupAnimation(tardis.getTravel().getState()); // UNLESS YOU RIGHT CLICK ON THE EXTERIOR??  WHICH FIXES IT?? AND ALSO MAKES THE CONSOLE ANIMATIONS WORK?? WTFF!!!
        }

        this.lookup.put(uuid, tardis);
        for (Consumer<Tardis> consumer : this.subscribers.removeAll(uuid)) {
            consumer.accept(tardis);
        }
    }

    @Override
    public GsonBuilder init(GsonBuilder builder) {
        builder.registerTypeAdapter(SerialDimension.class, new SerialDimension.ClientSerializer());
        return builder;
    }

    private void sync(UUID uuid, PacketByteBuf buf) {
        this.sync(uuid, buf.readString());
    }

    private void sync(PacketByteBuf buf) {
        this.sync(buf.readUuid(), buf);
    }


    // https://discord.com/channels/859856751070937098/863115541166424124/1179521521555865852
    public void ask(BlockPos pos) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeBlockPos(pos);

        ClientPlayNetworking.send(ASK_POS, data);
    }

    public void ask(UUID uuid) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(uuid);

        ClientPlayNetworking.send(ASK, data);
    }

    @Override
    public void reset() {
        this.subscribers.clear();
        super.reset();
    }

    public static ClientTardisManager getInstance() {
        return instance;
    }
}
