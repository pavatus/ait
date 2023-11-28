package the.mdteam.ait;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import the.mdteam.ait.wrapper.ServerTardis;

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

    private final Multimap<UUID, ServerPlayerEntity> subscribers = ArrayListMultimap.create();

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
    }

    public Tardis create(AbsoluteBlockPos.Directed pos, ExteriorEnum exteriorType, TardisDesktopSchema schema) {
        UUID uuid = UUID.randomUUID();

        Tardis tardis = new Tardis(uuid, pos, schema, exteriorType);
        this.lookup.put(uuid, tardis);

        tardis.getTravel().placeExterior();
        return tardis;
    }

    public Tardis getTardis(UUID uuid) {
        // System.out.println("SERVER: getting (direct) tardis with uuid " + uuid);
        if (this.lookup.containsKey(uuid))
            return this.lookup.get(uuid);

        return this.loadTardis(uuid);
    }

    @Override
    public void getTardis(UUID uuid, Consumer<Tardis> consumer) {
        System.out.println("SERVER: getting tardis with uuid " + uuid);
        super.getTardis(uuid, consumer);
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

            AITMod.LOGGER.info("Loaded TARDIS: {}", tardis.getUuid());
            AITMod.LOGGER.info("Schema ID: {}", tardis.getDesktop().getSchema().id());
            AITMod.LOGGER.info("Corners: {}", tardis.getDesktop().getCorners());
            AITMod.LOGGER.info("Door Pos: {}", tardis.getDesktop().getInteriorDoorPos());
            AITMod.LOGGER.info("Exterior Type: {}", tardis.getExteriorType());
            AITMod.LOGGER.info("Travel state: {}", tardis.getTravel().getState());
            AITMod.LOGGER.info("Pos: {}", tardis.getTravel().getPosition());
            AITMod.LOGGER.info("Destination: {}", tardis.getTravel().getDestination());

            return tardis;
        } catch (IOException e) {
            AITMod.LOGGER.warn("Failed to load tardis with uuid {}!", file);
            AITMod.LOGGER.warn(e.getMessage());
        }

        return null;
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

        for (ServerPlayerEntity player : this.subscribers.get(tardis.getUuid())) {
            this.sendTardis(player, tardis);
        }
    }

    public void sendToSubscribers(UUID uuid) {
        for (ServerPlayerEntity player : this.subscribers.get(uuid)) {
            this.sendTardis(player, uuid);
        }
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
        System.out.println(this.lookup);
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
        return instance;
    }


    public void loadTardises() {
        try {
            for (String name : Stream.of(new File(TardisUtil.getServer().getSavePath(WorldSavePath.ROOT) + "ait/").listFiles())
                    .filter(file -> !file.isDirectory())
                    .map(File::getName)
                    .collect(Collectors.toSet())) {

                System.out.println(name.substring(name.lastIndexOf(".") + 1));

                if (!name.substring(name.lastIndexOf(".") + 1).equalsIgnoreCase("json"))
                    continue;

                System.out.println(name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf(".")));

                UUID uuid = UUID.fromString(name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf(".")));

                System.out.println(uuid);

                this.loadTardis(uuid);
            }
            System.out.println(this.lookup);
        } catch (Exception e) {}
    }

    public static void sendPacketToAll(Identifier channel, PacketByteBuf data) {
        for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player,channel,data);
        }
    }
}
