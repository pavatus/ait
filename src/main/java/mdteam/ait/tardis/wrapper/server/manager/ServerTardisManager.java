package mdteam.ait.tardis.wrapper.server.manager;

import com.google.gson.GsonBuilder;
import mdteam.ait.AITMod;
import mdteam.ait.tardis.exterior.ExteriorSchema;
import mdteam.ait.tardis.util.TardisUtil;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import mdteam.ait.tardis.util.SerialDimension;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktopSchema;
import mdteam.ait.tardis.TardisManager;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.variant.exterior.ExteriorVariantSchema;
import mdteam.ait.tardis.wrapper.client.manager.ClientTardisManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import mdteam.ait.tardis.wrapper.server.ServerTardis;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerTardisManager extends TardisManager {

    public static final Identifier SEND = new Identifier("ait", "send_tardis");
    public static final Identifier UPDATE = new Identifier("ait", "update_tardis");
    private static final ServerTardisManager instance = new ServerTardisManager();
    // Changed from MultiMap to HashMap to fix some concurrent issues, maybe
    private final Map<UUID, List<UUID>> subscribers = new HashMap<>(); // fixme most of the issues with tardises on client when the world gets reloaded is because the subscribers dont get readded so the client stops getting informed, either save this somehow or make sure the client reasks on load.

    public ServerTardisManager() {
        ServerPlayNetworking.registerGlobalReceiver(
                ClientTardisManager.ASK, (server, player, handler, buf, responseSender) -> {
                    UUID uuid = buf.readUuid();
                    this.sendTardis(player, uuid);
                    addSubscriberToTardis(player, uuid);

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
                    addSubscriberToTardis(player, uuid);
                    this.subscribeEveryone(getTardis(uuid));
                }
        );

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            // force all dematting to go flight and all matting to go land
            for (Tardis tardis : this.getLookup().values()) {
                if (tardis.getTravel().getState() == TardisTravel.State.DEMAT) {
                    tardis.getTravel().toFlight();
                } else if (tardis.getTravel().getState() == TardisTravel.State.MAT) {
                    tardis.getTravel().forceLand();
                }
            }

            this.reset();
        });
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
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (Tardis tardis : ServerTardisManager.getInstance().getLookup().values()) {
                tardis.startTick(server);
            }
        });
    }

    /**
     * Adds a subscriber to the Tardis
     * @param serverPlayerEntity PLAYER
     * @param tardisUUID TARDIS UUID
     */
    private void addSubscriberToTardis(ServerPlayerEntity serverPlayerEntity, UUID tardisUUID) {
        if (this.subscribers.containsKey(tardisUUID)) {
            this.subscribers.get(tardisUUID).add(serverPlayerEntity.getUuid());
        } else {
            List<UUID> subscriber_list = new ArrayList<>();
            subscriber_list.add(serverPlayerEntity.getUuid());
            this.subscribers.put(tardisUUID, subscriber_list);
        }

    }

    /**
     * Removes a subscriber from the TARDIS
     * @param serverPlayerEntity the player to remove from the subscribers list
     * @param tardisUUID the UUID of the TARDIS
     */
    private void removeSubscriberToTardis(ServerPlayerEntity serverPlayerEntity, UUID tardisUUID) {
        if (!this.subscribers.containsKey(tardisUUID)) return; // If the Tardis does not have any subscribers ignore this

        List<UUID> old_uuids = this.subscribers.get(tardisUUID);
        int i_to_remove = -1;

        for (int i = 0; i < old_uuids.size(); i++) {
            if (old_uuids.get(i).equals(serverPlayerEntity.getUuid())) {
                i_to_remove = i;
                break;
            }
        }

        if (i_to_remove == -1) return; // If the player is not in the list ignore this

        old_uuids.remove(i_to_remove);
        if (old_uuids.isEmpty()) {
            this.subscribers.remove(tardisUUID);
        } else {
            this.subscribers.put(tardisUUID, old_uuids); // update the subscriber list in case any other subscriber was added or removed during this operation
        }
    }

    /**
     * Removes all subscribers from the TARDIS
     * @param tardisUUID the TARDIS UUID
     */
    private void removeAllSubscribersFromTardis(UUID tardisUUID) {
        this.subscribers.replace(tardisUUID, new ArrayList<>());
    }

    public ServerTardis create(AbsoluteBlockPos.Directed pos, ExteriorSchema exteriorType, ExteriorVariantSchema variantType, TardisDesktopSchema schema, boolean locked) {
        UUID uuid = UUID.randomUUID();

        ServerTardis tardis = new ServerTardis(uuid, pos, schema, exteriorType, variantType, locked);
        //this.saveTardis(tardis);
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
            ServerTardis tardis = this.gson.fromJson(json, ServerTardis.class);
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
            Files.writeString(savePath.toPath(), this.gson.toJson(tardis, ServerTardis.class));
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

    // fixme this shit broken bro something about being edited while iterating through it

    public void sendToSubscribers(Tardis tardis) {
        if (tardis == null) return;

        if (!this.subscribers.containsKey(tardis.getUuid())) this.subscribeEveryone(tardis);
        MinecraftServer mc = TardisUtil.getServer();

        if(!this.subscribers.isEmpty()) {
            for (UUID uuid : this.subscribers.get(tardis.getUuid())) {
                ServerPlayerEntity player = mc.getPlayerManager().getPlayer(uuid);
                this.sendTardis(player, tardis);
            }
        }
    }

    // fixme i think its easier if all clients just get updated about the tardises
    // @TODO not send everything to everyone
    public void subscribeEveryone(Tardis tardis) {
        for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
            if (this.subscribers.containsKey(player.getUuid())) continue;

            addSubscriberToTardis(player, tardis.getUuid());
        }
    }

    // fixme im desperate ok
    public void subscribeEveryoneToEverything() {
        for (Tardis tardis : this.lookup.values()) {
            this.subscribeEveryone(tardis);
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
