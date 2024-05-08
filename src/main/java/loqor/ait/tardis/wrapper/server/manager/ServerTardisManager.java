package loqor.ait.tardis.wrapper.server.manager;

import com.google.gson.GsonBuilder;
import loqor.ait.AITMod;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.compat.immersive.PortalsHandler;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.data.SerialDimension;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.events.ServerCrashEvent;
import loqor.ait.core.util.DeltaTimeManager;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.StatsData;
import loqor.ait.tardis.util.NetworkUtil;
import loqor.ait.tardis.util.TardisChunkUtil;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServerTardisManager extends TardisManager<ServerTardis> {

	private static ServerTardisManager instance;
	// Changed from MultiMap to HashMap to fix some concurrent issues, maybe
	private final ConcurrentHashMap<UUID, List<UUID>> subscribers = new ConcurrentHashMap<>(); // fixme most of the issues with tardises on client when the world gets reloaded is because the subscribers dont get readded so the client stops getting informed, either save this somehow or make sure the client reasks on load.
	private final ConcurrentHashMap<UUID, List<UUID>> buffers = new ConcurrentHashMap<>(); // buffer for sending tardises

	public ServerTardisManager() {
		ServerPlayNetworking.registerGlobalReceiver(
				ASK, (server, player, handler, buf, responseSender) -> {
					UUID uuid = buf.readUuid();

					if (player == null || uuid == null)
						return;

					this.sendTardis(player, uuid);
					addSubscriberToTardis(player, uuid);
				}
		);

		ServerPlayNetworking.registerGlobalReceiver(
				ASK_POS, (server, player, handler, buf, responseSender) -> {
					BlockPos pos = AbsoluteBlockPos.fromNbt(buf.readNbt());

					UUID uuid = null;
					for (Tardis tardis : this.lookup.values()) {
						if (!tardis.getTravel().getPosition().equals(pos))
							continue;

						uuid = tardis.getUuid();
					}

					if (uuid == null)
						return;

					this.sendTardis(player, uuid);
					addSubscriberToTardis(player, uuid);
				}
		);

		ServerLifecycleEvents.SERVER_STOPPING.register(this::onShutdown);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			this.saveTardis(server);
			this.reset();
		});

		ServerCrashEvent.EVENT.register(((server, report) -> this.reset())); // just panic and reset + save

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerTardis tardis : this.lookup.values()) {
				tardis.tick(server);
			}

			this.tickBuffer(server);
		});

		ServerTickEvents.START_SERVER_TICK.register(server -> {
			for (ServerTardis tardis : this.lookup.values()) {
				tardis.startTick(server);
			}
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			this.onPlayerJoin(handler.getPlayer());
		});
	}

	/**
	 * Adds a subscriber to the Tardis
	 *
	 * @param serverPlayerEntity PLAYER
	 * @param tardisUUID         TARDIS UUID
	 */
	private void addSubscriberToTardis(ServerPlayerEntity serverPlayerEntity, UUID tardisUUID) {
		if (this.subscribers.containsKey(tardisUUID)) {
			this.subscribers.get(tardisUUID).add(serverPlayerEntity.getUuid());
		} else {
			List<UUID> subscriber_list = new CopyOnWriteArrayList<>();
			subscriber_list.add(serverPlayerEntity.getUuid());
			this.subscribers.put(tardisUUID, subscriber_list);
		}
	}

	public ServerTardis create(AbsoluteBlockPos.Directed pos, ExteriorCategorySchema exteriorType, ExteriorVariantSchema variantType, TardisDesktopSchema schema) {
		return createWithStats(pos, exteriorType, variantType, schema, stats -> {});
	}

	public ServerTardis createWithPlayerCreator(AbsoluteBlockPos.Directed pos, ExteriorCategorySchema exteriorType, ExteriorVariantSchema variantType, TardisDesktopSchema schema, String playerCreatorName) {
		return createWithStats(pos, exteriorType, variantType, schema, stats -> {
			stats.setPlayerCreatorName(playerCreatorName);
			stats.markPlayerCreatorName();
		});
	}

	private ServerTardis createWithStats(AbsoluteBlockPos.Directed pos, ExteriorCategorySchema exteriorType, ExteriorVariantSchema variantType, TardisDesktopSchema schema, Consumer<StatsData> consumer) {
		UUID uuid = UUID.randomUUID();

		ServerTardis tardis = new ServerTardis(uuid, pos, schema, exteriorType, variantType); // todo removed "locked" param
		tardis.init(false);

		// todo this can be moved to init
		tardis.getTravel().placeExterior();
		tardis.getTravel().runAnimations();

		StatsData stats = tardis.handler(TardisComponent.Id.STATS);
		stats.markCreationDate();

		this.lookup.put(uuid, tardis);
		consumer.accept(stats);
		return tardis;
	}

	@Override
	public ServerTardis getTardis(UUID uuid) {
		if (uuid == null)
			return null; // ugh

		if (this.lookup.containsKey(uuid))
			return this.lookup.get(uuid);

		return this.loadTardis(TardisUtil.getServer(), uuid); // FIXME TardisUtil.getServer is bad
	}

	@Override
	public void loadTardis(UUID uuid, Consumer<ServerTardis> consumer) {
		consumer.accept(this.loadTardis(TardisUtil.getServer(), uuid)); // FIXME TardisUtil.getServer is bad
	}

	@Override
	protected GsonBuilder getGsonBuilder(GsonBuilder builder) {
		builder.registerTypeAdapter(SerialDimension.class, SerialDimension.serializer())
				.registerTypeAdapter(Tardis.class, ServerTardis.creator());

		return builder;
	}

	public static void init() {
		instance = new ServerTardisManager();
	}

	public void sendToSubscribers(Tardis tardis) {
		for (ServerPlayerEntity player : NetworkUtil.getNearbyTardisPlayers(tardis)) {
			this.sendTardis(player, tardis);
		}
	}

	// TODO - yes this is much better than sending the entire tardis class, but it still sends the entire component class. If everything is saved in a PropertiesHolder then this is a non-issue though.
	public void sendToSubscribers(TardisComponent component) {
		for (ServerPlayerEntity player : NetworkUtil.getNearbyTardisPlayers(this.getTardis(component.tardis().getUuid()))) {
			this.updateTardis(player, component.tardis().getUuid(), component);
		}
	}

	/**
	 * For sending changes to do with the PropertiesHandler
	 *
	 * @param uuid  The TARDIS UUID to sync to
	 * @param key   The key where the value is stored in the PropertiesHolder
	 * @param type  The class of the variable, serialised so it can be read later
	 *              eg a double would be "double", boolean would be "boolean", etc
	 * @param value The new value to be synced to client
	 */
	public void sendToSubscribers(UUID uuid, String key, String type, String value) {
		for (ServerPlayerEntity player : NetworkUtil.getNearbyTardisPlayers(this.getTardis(uuid))) {
			this.updateTardisProperty(player, uuid, key, type, value);
		}
	}

	public void updateTardisProperty(@NotNull ServerPlayerEntity player, UUID tardis, String key, String type, String value) {
		PacketByteBuf data = PacketByteBufs.create();

		data.writeUuid(tardis);
		data.writeEnumConstant(TardisComponent.Id.PROPERTIES);

		data.writeString(key);
		data.writeString(type);
		data.writeString(value);

		ServerPlayNetworking.send(player, UPDATE, data);
		checkForceSync(player, tardis);
	}

	private void updateTardis(@NotNull ServerPlayerEntity player, UUID uuid, TardisComponent component) {
		this.updateTardis(player, uuid, component.getId(), this.gson.toJson(component));
	}

	private void updateTardis(@NotNull ServerPlayerEntity player, UUID uuid, TardisComponent.Id header, String json) {
		PacketByteBuf data = PacketByteBufs.create();
		data.writeUuid(uuid);
		data.writeEnumConstant(header);
		data.writeString(json);

		ServerPlayNetworking.send(player, UPDATE, data);
		checkForceSync(player, uuid);
	}

	private void sendTardis(@NotNull ServerPlayerEntity player, UUID uuid) {
		Tardis tardis = this.getTardis(uuid);
		if(tardis == null) return;
		this.sendTardis(player, tardis);
	}

	private void sendTardis(@NotNull ServerPlayerEntity player, Tardis tardis) {
		if (tardis == null || this.gson == null)
			return;

		this.sendTardis(player, tardis.getUuid(), this.gson.toJson(tardis, ServerTardis.class));
	}

	// FIXME should this really be used this often? ((see #checkForceSync))
	private void sendTardis(@NotNull ServerPlayerEntity player, UUID uuid, String json) {
		if (this.isInBuffer(player, uuid))
			return;

		if (isAskOnDelay(player)) {
			this.addToBuffer(player, uuid);
			return;
		}

		PacketByteBuf data = PacketByteBufs.create();
		data.writeUuid(uuid);
		data.writeString(json);

		ServerPlayNetworking.send(player, SEND, data);

		createAskDelay(player);
		createForceSyncDelay(player);
	}

	public void remove(UUID uuid) {
		this.buffers.remove(uuid);
		this.lookup.remove(uuid);
		this.subscribers.remove(uuid);
	}

	/**
	 * A delay to stop the client getting overloaded with tons of tardises all at once, splitting it up over a few seconds to save server performance.
	 */
	private void createAskDelay(ServerPlayerEntity player) {
		DeltaTimeManager.createDelay(player.getUuidAsString() + "-ask-delay", (long) ((AITMod.AIT_CONFIG.ASK_DELAY()) * 1000L)); // A delay between asking for tardises to be synced
	}

	private boolean isAskOnDelay(ServerPlayerEntity player) {
		return DeltaTimeManager.isStillWaitingOnDelay(player.getUuidAsString() + "-ask-delay");
	}

	/**
	 * A delay to force resync the server when its been a while since theyve seen a tardis to fix sync issues
	 */
	private void createForceSyncDelay(ServerPlayerEntity player) {
		DeltaTimeManager.createDelay(player.getUuidAsString() + "-force-sync-delay", (long) ((AITMod.AIT_CONFIG.force_sync_delay()) * 1000L)); // A delay between asking for tardises to be synced
	}

	private boolean isForceSyncOnDelay(ServerPlayerEntity player) {
		return DeltaTimeManager.isStillWaitingOnDelay(player.getUuidAsString() + "-force-sync-delay");
	}

	private void checkForceSync(ServerPlayerEntity player, UUID tardis) {
		if (!isForceSyncOnDelay(player)) {
			this.sendTardis(player, tardis);
		}
		createForceSyncDelay(player);
	}

	public void onPlayerJoin(ServerPlayerEntity player) {
		if (player.getWorld().getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD) {
			// if the player is a tardis already, sync the one at their location
			Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos(), true);
			if (found == null) return;

			this.sendTardis(player, found);
		}
	}

	@Override
	public void reset() {
		this.subscribers.clear();
		this.buffers.clear();
		super.reset();
	}

	public static Path getRootSavePath(MinecraftServer server) {
		return server.getSavePath(WorldSavePath.ROOT).resolve(".ait");
	}

	public static Path getSavePath(MinecraftServer server, UUID uuid, String suffix) throws IOException {
		Path result = getRootSavePath(server).resolve(uuid.toString() + "." + suffix);
		Files.createDirectories(result.getParent());

		return result;
	}

	public static ServerTardisManager getInstance() {
		return instance;
	}

	@Deprecated
	private void loadTardises(MinecraftServer server) {
		// TODO: migrate to NIO
		File[] saved = ServerTardisManager.getRootSavePath(server).toFile().listFiles();

		if (saved == null)
			return;

		for (String name : Stream.of(saved)
				.filter(file -> !file.isDirectory())
				.map(File::getName)
				.collect(Collectors.toSet())
		) {
			if (!name.substring(name.lastIndexOf(".") + 1).equalsIgnoreCase("json"))
				continue;

			UUID uuid = UUID.fromString(name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf(".")));
			this.loadTardis(server, uuid);
			this.backupTardis(server, uuid);
		}
	}

	private ServerTardis loadTardis(MinecraftServer server, UUID uuid) {
		try {
			Path file = ServerTardisManager.getSavePath(server, uuid, "json");
			String json = Files.readString(file);

			ServerTardis tardis = this.gson.fromJson(json, ServerTardis.class);
			tardis.init(true);

			this.lookup.put(tardis.getUuid(), tardis);
			return tardis;
		} catch (IOException e) {
			AITMod.LOGGER.warn("Failed to load tardis with uuid {}!", uuid);
			AITMod.LOGGER.warn(e.getMessage());
		}

		return null;
	}

	public void backupTardis(MinecraftServer server, UUID uuid) {
		try {
			Path file = ServerTardisManager.getSavePath(server, uuid, "json");
			Path backup = ServerTardisManager.getSavePath(server, uuid, "old");

			String json = Files.readString(file);
			Files.writeString(backup, json);
		} catch (IOException e) {
			AITMod.LOGGER.warn("Failed to backup tardis with uuid {}!", uuid);
			AITMod.LOGGER.warn(e.getMessage());
		}
	}

	public void saveTardis(MinecraftServer server) {
		for (ServerTardis tardis : this.lookup.values()) {
			this.saveTardis(server, tardis);
		}

		// this might fix server crash bugs
		if (this.lookup.isEmpty()) {
			this.loadTardises(server);

			if (!this.lookup.isEmpty())
				this.saveTardis(server);
		}
	}

	public void saveTardis(MinecraftServer server, ServerTardis tardis) {
		try {
			Path savePath = getSavePath(server, tardis.getUuid(), "json");
			Files.writeString(savePath, this.gson.toJson(tardis, ServerTardis.class));
		} catch (IOException e) {
			AITMod.LOGGER.warn("Couldn't save Tardis {}", tardis.getUuid());
			AITMod.LOGGER.warn(e.getMessage());
		}
	}

	private boolean isInBuffer(ServerPlayerEntity player, UUID tardis) {
		if (!this.buffers.containsKey(player.getUuid()))
			return false;

		return this.buffers.get(player.getUuid()).contains(tardis);
	}

	private void addToBuffer(ServerPlayerEntity player, UUID tardis) {
		if (this.buffers.containsKey(player.getUuid())) {
			this.buffers.get(player.getUuid()).add(tardis);
			return;
		}

		this.buffers.put(player.getUuid(), new ArrayList<>(Collections.singletonList(tardis)));
	}

	private void tickBuffer(MinecraftServer server) {
		if (this.buffers.isEmpty())
			return;

		for (Iterator<UUID> it = this.buffers.keys().asIterator(); it.hasNext(); ) {
			UUID playerId = it.next();
			ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerId);

			if (player == null
					|| !this.buffers.containsKey(playerId)
					|| isAskOnDelay(player))
				continue;

			List<UUID> tardisIds = this.buffers.get(playerId);

			if (tardisIds == null || tardisIds.isEmpty())
				continue;

			List<UUID> copyOfTardisIds = new CopyOnWriteArrayList<>(tardisIds);

			for (UUID tardisId : copyOfTardisIds) {
				tardisIds.remove(tardisId);
				this.sendTardis(player, tardisId);
			}

			if (tardisIds.isEmpty())
				this.buffers.remove(playerId);
		}
	}

	public void onShutdown(MinecraftServer server) {
		// force all dematting to go flight and all matting to go land
		for (Tardis tardis : this.getLookup().values()) {
			// stop forcing all chunks
			TardisChunkUtil.stopForceExteriorChunk(tardis);
			TardisTravel.State state = tardis.getTravel().getState();

			if (state == TardisTravel.State.DEMAT) {
				tardis.getTravel().toFlight();
			} else if (state == TardisTravel.State.MAT) {
				tardis.getTravel().forceLand();
			}

			tardis.getDoor().closeDoors();
			if (DependencyChecker.hasPortals())
				PortalsHandler.removePortals(tardis);
		}
	}
}
