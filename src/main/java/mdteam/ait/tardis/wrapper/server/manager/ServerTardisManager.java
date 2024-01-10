package mdteam.ait.tardis.wrapper.server.manager;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerTardisManager extends TardisManager {

    public static final Identifier SEND = new Identifier("ait", "send_tardis");
    public static final Identifier UPDATE = new Identifier("ait", "update_tardis");
    private static final ServerTardisManager instance = new ServerTardisManager();
    // Changed from MultiMap to HashMap to fix some concurrent issues, maybe
    private final ConcurrentHashMap<UUID, List<UUID>> subscribers = new ConcurrentHashMap<>(); // fixme most of the issues with tardises on client when the world gets reloaded is because the subscribers dont get readded so the client stops getting informed, either save this somehow or make sure the client reasks on load.

    public final ConcurrentHashMap<UUID, List<UUID>> exterior_subscribers = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<UUID, List<UUID>> interior_subscribers = new ConcurrentHashMap<>();

    public ServerTardisManager() {
        ServerPlayNetworking.registerGlobalReceiver(
                ClientTardisManager.ASK, (server, player, handler, buf, responseSender) -> {
                    UUID uuid = buf.readUuid();
                    if (player == null) return;
                    addSubscriberToTardis(player, uuid);
                    this.sendTardis(player, uuid);

                }
        );

        ServerPlayNetworking.registerGlobalReceiver(
                ClientTardisManager.LET_KNOW_UNLOADED, (server, player, handler, buf, responseSender) -> {
                    UUID uuid = buf.readUuid();
                    if (player == null) return;
                    removeSubscriberToTardis(player, uuid);
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
                    addSubscriberToTardis(player, uuid);
                    this.sendTardis(player, uuid);
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

                tardis.getDoor().closeDoors();
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
    public void addSubscriberToTardis(ServerPlayerEntity serverPlayerEntity, UUID tardisUUID) {
        if (this.subscribers.containsKey(tardisUUID)) {
            this.subscribers.get(tardisUUID).add(serverPlayerEntity.getUuid());
        } else {
            List<UUID> subscriber_list = new CopyOnWriteArrayList<>();
            subscriber_list.add(serverPlayerEntity.getUuid());
            this.subscribers.put(tardisUUID, subscriber_list);
        }

    }

    /**
     * Adds an exterior subscriber to the Tardis.
     *
     * @param  player  the server player entity to add as a subscriber
     * @param  uuid    the UUID of the subscriber
     */
    public void addExteriorSubscriberToTardis(ServerPlayerEntity player, UUID uuid) {
        if (this.exterior_subscribers.containsKey(uuid)) {
            this.exterior_subscribers.get(uuid).add(player.getUuid());
        } else {
            List<UUID> subscriber_list = new CopyOnWriteArrayList<>();
            subscriber_list.add(player.getUuid());
            this.exterior_subscribers.put(uuid, subscriber_list);
        }
    }

    /**
     * Adds an interior subscriber to the Tardis.
     *
     * @param  player  the ServerPlayerEntity to add as a subscriber
     * @param  uuid    the UUID of the subscriber
     */
    public void addInteriorSubscriberToTardis(ServerPlayerEntity player, UUID uuid) {
        if (this.interior_subscribers.containsKey(uuid)) {
            this.interior_subscribers.get(uuid).add(player.getUuid());
        } else {
            List<UUID> subscriber_list = new CopyOnWriteArrayList<>();
            subscriber_list.add(player.getUuid());
            this.interior_subscribers.put(uuid, subscriber_list);
        }
    }

    /**
     * Removes the specified player from the exterior subscribers of the Tardis with the given UUID.
     *
     * @param  player  the player to remove
     * @param  uuid    the UUID of the Tardis
     */
    public void removeExteriorSubscriberToTardis(ServerPlayerEntity player, UUID uuid) {
        if (!this.exterior_subscribers.containsKey(uuid)) return;
        List<UUID> old_uuids = this.exterior_subscribers.get(uuid);
        int i_to_remove = -1;
        for (int i = 0; i < old_uuids.size(); i++) {
            if (old_uuids.get(i).equals(player.getUuid())) {
                i_to_remove = i;
                break;
            }
        }
        if (i_to_remove == -1) return;
        old_uuids.remove(i_to_remove);
        if (old_uuids.isEmpty()) {
            this.exterior_subscribers.remove(uuid);
        } else {
            this.exterior_subscribers.put(uuid, old_uuids);
        }
    }

    /**
     * Removes the interior subscriber with the specified UUID for the given player.
     *
     * @param  player  the server player entity
     * @param  uuid    the UUID of the interior subscriber to be removed
     */
    public void removeInteriorSubscriberToTardis(ServerPlayerEntity player, UUID uuid) {
        if (!this.interior_subscribers.containsKey(uuid)) return;
        List<UUID> old_uuids = this.interior_subscribers.get(uuid);
        int i_to_remove = -1;
        for (int i = 0; i < old_uuids.size(); i++) {
            if (old_uuids.get(i).equals(player.getUuid())) {
                i_to_remove = i;
                break;
            }
        }
        if (i_to_remove == -1) return;
        old_uuids.remove(i_to_remove);
        if (old_uuids.isEmpty()) {
            this.interior_subscribers.remove(uuid);
        } else {
            this.interior_subscribers.put(uuid, old_uuids);
        }
    }

    public void removePlayerFromAllTardis(ServerPlayerEntity serverPlayerEntity) {
        for (Map.Entry<UUID, List<UUID>> entry : this.exterior_subscribers.entrySet()) {
            removeSubscriberToTardis(serverPlayerEntity, entry.getKey());
        }
        for (Map.Entry<UUID, List<UUID>> entry : this.interior_subscribers.entrySet()) {
            removeSubscriberToTardis(serverPlayerEntity, entry.getKey());
        }
        for (Map.Entry<UUID, List<UUID>> entry : this.subscribers.entrySet()) {
            removeSubscriberToTardis(serverPlayerEntity, entry.getKey());
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

    public ServerTardis create(AbsoluteBlockPos.Directed pos, ExteriorSchema exteriorType, ExteriorVariantSchema variantType, TardisDesktopSchema schema, boolean locked) {
        UUID uuid = UUID.randomUUID();

        ServerTardis tardis = new ServerTardis(uuid, pos, schema, exteriorType, variantType, locked);
        // tardis.setFuelCount(1000); // Default fuel count is 100 - cant be set here causes issues. set in PropertiesHandler instead
        //this.saveTardis(tardis);
        this.lookup.put(uuid, tardis);

        tardis.getTravel().placeExterior();
        tardis.getTravel().runAnimations();
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
        /*File savePath = ServerTardisManager.getSavePath(tardis);
        savePath.getParentFile().mkdirs();

        try {
            Files.writeString(savePath.toPath(), this.gson.toJson(tardis, ServerTardis.class));
        } catch (IOException e) {
            AITMod.LOGGER.warn("Couldn't save Tardis {}", tardis.getUuid());
            AITMod.LOGGER.warn(e.getMessage());
        }*/
        this.saveTardisAsync(tardis);
    }

    public void saveTardisAsync(Tardis tardis) {
        CompletableFuture.runAsync(() -> {
            File savePath = ServerTardisManager.getSavePath(tardis);
            savePath.getParentFile().mkdirs();

            try {
                Files.writeString(savePath.toPath(),
                        this.gson.toJson(tardis, ServerTardis.class));
            } catch (IOException e) {
                AITMod.LOGGER.warn("Couldn't save Tardis {}", tardis.getUuid());
                AITMod.LOGGER.warn(e.getMessage());
            }
        });
    }

    public void saveTardises() {
        List<Tardis> tardises = new CopyOnWriteArrayList<>(getLookup().values());

        // Split the tardises into multiple groups
        int numThreads = Runtime.getRuntime().availableProcessors();
        int batchSize = (int) Math.ceil((double) tardises.size() / numThreads);

        // Create an ExecutorService with a thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        List<Callable<Void>> tasks = new ArrayList<>();

        // Create a Callable for each batch of tardises
        for (int i = 0; i < tardises.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, tardises.size());
            List<Tardis> batch = tardises.subList(i, endIndex);

            // Create a Callable to save the batch of tardises
            Callable<Void> task = () -> {
                for (Tardis tardis : batch) {
                    // Save the tardis
                    this.saveTardis(tardis);
                }
                return null;
            };

            tasks.add(task);
        }

        try {
            // Submit all the tasks to the ExecutorService
            List<Future<Void>> futures = executorService.invokeAll(tasks);

            // Wait for all the tasks to complete
            for (Future<Void> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            // Handle any exceptions that may occur
            AITMod.LOGGER.warn("Failed to save tardises []" + e.getMessage());
        } finally {
            // Shutdown the ExecutorService
            executorService.shutdown();
        }
    }

    public void saveTardis() {
        for (Tardis tardis : this.lookup.values()) {
            this.saveTardis(tardis);
        }
    }


    public void sendToSubscribers(Tardis tardis) {
        if (tardis == null) return;
        if (!this.subscribers.containsKey(tardis.getUuid())) return;
//        if (!this.subscribers.containsKey(tardis.getUuid())) this.subscribeEveryone(tardis);
        MinecraftServer mc = TardisUtil.getServer();

        Map<UUID, List<UUID>> subscribersCopy = new HashMap<>(this.subscribers);
        List<UUID> tardisSubscribers = new CopyOnWriteArrayList<>(subscribersCopy.getOrDefault(tardis.getUuid(), Collections.emptyList()));

        for (UUID uuid : tardisSubscribers) {
            ServerPlayerEntity player = mc.getPlayerManager().getPlayer(uuid);
            this.sendTardis(player, tardis);
        }
    }


    private void sendTardis(ServerPlayerEntity player, UUID uuid) {
        if (player == null) return;
        this.sendTardis(player, this.getTardis(uuid));
    }

    private void sendTardis(ServerPlayerEntity player, Tardis tardis) {
        if (player == null) return;
        this.sendTardis(player, tardis.getUuid(), this.gson.toJson(tardis, ServerTardis.class));
    }

    private void sendTardis(ServerPlayerEntity player, UUID uuid, String json) {
        if (player == null) return;
        PacketByteBuf data = PacketByteBufs.create();
        data.writeUuid(uuid);
        data.writeString(json);

        ServerPlayNetworking.send(player, SEND, data);
    }

    @Override
    public void reset() {
        this.subscribers.clear();

        this.saveTardises();
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
        Path savePath = TardisUtil.getServer().getSavePath(WorldSavePath.ROOT).resolve("ait");

        File[] saved = savePath.toFile().listFiles((dir, name) ->
                name.toLowerCase().endsWith(".json") && !new File(dir, name).isDirectory());

        if (saved == null) {
            return;
        }

        for (String name : Stream.of(saved)
                .map(File::getName)
                .collect(Collectors.toSet())) {

            UUID uuid = UUID.fromString(name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf(".")));
            this.loadTardis(uuid);
        }
    }
}
