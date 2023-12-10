package mdteam.ait.tardis;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.GsonBuilder;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.consoles.ConsoleEnum;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.data.SerialDimension;
import mdteam.ait.tardis.wrapper.server.ServerTardisDesktop;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.mixin.networking.client.accessor.MinecraftClientAccessor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import mdteam.ait.tardis.wrapper.server.ServerTardis;
import org.apache.logging.log4j.core.jmx.Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerTardisManager extends TardisManager {

    public static final Identifier SEND = new Identifier("ait", "send_tardis");
    public static final Identifier UPDATE = new Identifier("ait", "update_tardis");
    private static final ServerTardisManager instance = new ServerTardisManager();
    private final Multimap<UUID, ServerPlayerEntity> subscribers = ArrayListMultimap.create(); // fixme most of the issues with tardises on client when the world gets reloaded is because the subscribers dont get readded so the client stops getting informed, either save this somehow or make sure the client reasks on load.

    public ServerTardisManager() {
        ServerPlayNetworking.registerGlobalReceiver(
                ClientTardisManager.ASK, (server, player, handler, buf, responseSender) -> {
                    UUID uuid = buf.readUuid();
                    this.sendTardis(player, uuid);

                    this.subscribers.put(uuid, player);
                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                ClientTardisManager.ASK_POS, (server, player, handler, buf, responseSender) -> {
                    BlockPos pos = buf.readBlockPos();
                    UUID uuid = null;

                    for (Tardis tardis : this.getLookup().values()) {
                        if (!tardis.getTravel().getPosition().equals(pos)) continue;

                        uuid = tardis.getUuid();
                    }

                    if (uuid == null)
                        return;

                    this.sendTardis(player, uuid);
                    this.subscribers.put(uuid, player);
                }
        );

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> this.reset());
        ServerLifecycleEvents.SERVER_STARTED.register(server -> this.loadTardises());
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            // fixme would this cause lag?
            for (Tardis tardis : ServerTardisManager.getInstance().getLookup().values()) {
                tardis.tick(server);
            }
        });
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            // fixme lag?
            for (Tardis tardis : ServerTardisManager.getInstance().getLookup().values()) {
                tardis.tick(world);
            }
        });
    }

    public ServerTardis create(AbsoluteBlockPos.Directed pos, ExteriorEnum exteriorType, ConsoleEnum consoleType, TardisDesktopSchema schema, boolean locked) {
        UUID uuid = UUID.randomUUID();

        ServerTardis tardis = new ServerTardis(uuid, pos, schema, exteriorType, consoleType, locked);
        this.lookup.put(uuid, tardis);

        tardis.getTravel().runAnimations();
        tardis.getTravel().placeExterior();
        return tardis;
    }

    public Tardis getTardis(UUID uuid) {
        if (this.lookup.containsKey(uuid))
            return this.lookup.get(uuid);

        return this.loadTardis(uuid);
    }

    @Override
    public void loadTardis(UUID uuid, Consumer<Tardis> consumer) {
        consumer.accept(this.loadTardis(uuid));
    }

    private Tardis loadTardis(UUID uuid) {
        File file = ServerTardisManager.getSavePath(uuid);
        file.getParentFile().mkdirs();

        try {
            if (!file.exists())
                throw new IOException("Tardis file " + file + " doesn't exist!");

            String json = Files.readString(file.toPath());
            Tardis tardis = this.gson.fromJson(json, Tardis.class);
            this.lookup.put(tardis.getUuid(), tardis);

            return tardis;
        } catch (IOException e) {
            AITMod.LOGGER.warn("Failed to load tardis with uuid {}!", file);
            AITMod.LOGGER.warn(e.getMessage());
        }

        return null;
    }

    @Override
    public GsonBuilder init(GsonBuilder builder) {
        builder.registerTypeAdapter(SerialDimension.class, SerialDimension.serializer());
        return builder;
    }

    public void saveTardis(Tardis tardis) {
        File savePath = ServerTardisManager.getSavePath(tardis);
        savePath.getParentFile().mkdirs();

        try {
            Files.writeString(savePath.toPath(), this.gson.toJson(tardis, Tardis.class));
        } catch (IOException e) {
            AITMod.LOGGER.warn("Couldn't save Tardis {}", tardis.getUuid());
            AITMod.LOGGER.warn(e.getMessage());
        }
    }

    public void saveTardis() {
        for (Tardis tardis : this.lookup.values()) {
            this.saveTardis(tardis);
        }
    }

    public void sendToSubscribers(Tardis tardis) {
        var temp = this.subscribers; // fixme this fixes ConcurrentModificationException jankily ( i think it does anyway )

        for (ServerPlayerEntity player : temp.get(tardis.getUuid())) {
            this.sendTardis(player, tardis);
        }
    }

    // fixme i think its easier if all clients just get updated about the tardises
    public void subscribeEveryone(Tardis tardis) {
        for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
            this.subscribers.put(tardis.getUuid(), player);
        }
        //System.out.println(this.subscribers);
    }

    private void sendTardis(ServerPlayerEntity player, UUID uuid) {
        this.sendTardis(player, this.getTardis(uuid));
    }

    private void sendTardis(ServerPlayerEntity player, Tardis tardis) {
        this.sendTardis(player, tardis.getUuid(), this.gson.toJson(tardis, ServerTardis.class));
    }

    private void sendTardis(ServerPlayerEntity player, UUID uuid, String json) {
        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(uuid);
        data.writeString(json);

        ServerPlayNetworking.send(player, SEND, data);
    }

    @Override
    public void reset() {
        this.subscribers.clear();

        this.saveTardis();
        super.reset();
    }

    private static File getSavePath(UUID uuid) {
        // TODO: maybe, make WorldSavePath.AIT?
        return new File(TardisUtil.getServer().getSavePath(WorldSavePath.ROOT) + "ait/" + uuid + ".json");
    }

    private static File getSavePath(Tardis tardis) {
        return ServerTardisManager.getSavePath(tardis.getUuid());
    }

    public static ServerTardisManager getInstance() {
        //System.out.println("getInstance() = " + instance);
        return instance;
    }


    public void loadTardises() {
        File[] saved = new File(TardisUtil.getServer().getSavePath(
                WorldSavePath.ROOT) + "ait/").listFiles();

        if (saved == null)
            return;

        for (String name : Stream.of(saved)
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet())) {

            if (!name.substring(name.lastIndexOf(".") + 1).equalsIgnoreCase("json"))
                continue;

            UUID uuid = UUID.fromString(name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf(".")));
            this.loadTardis(uuid);
        }
    }
}
