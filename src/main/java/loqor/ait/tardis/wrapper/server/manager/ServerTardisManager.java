package loqor.ait.tardis.wrapper.server.manager;

import com.google.gson.GsonBuilder;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.compat.immersive.PortalsHandler;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.data.SerialDimension;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.events.ServerCrashEvent;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisTravel;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.Value;
import loqor.ait.tardis.manager.BufferedTardisManager;
import loqor.ait.tardis.manager.TardisBuilder;
import loqor.ait.tardis.manager.TardisFileManager;
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
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public class ServerTardisManager extends BufferedTardisManager<ServerTardis, ServerPlayerEntity, MinecraftServer> {

	private static ServerTardisManager instance;
	private final TardisFileManager<ServerTardis> fileManager = new TardisFileManager<>();

	public static void init() {
		instance = new ServerTardisManager();
	}

	private ServerTardisManager() {
		ServerPlayNetworking.registerGlobalReceiver(
				ASK, (server, player, handler, buf, responseSender) -> {
					UUID uuid = buf.readUuid();

					if (player == null || uuid == null)
						return;

					this.getTardis(server, uuid, tardis ->
							this.sendAndSubscribe(player, tardis)
					);
				}
		);

		ServerPlayNetworking.registerGlobalReceiver(
				ASK_POS, (server, player, handler, buf, responseSender) -> {
					BlockPos pos = AbsoluteBlockPos.fromNbt(buf.readNbt());

					ServerTardis found = null;
					for (ServerTardis tardis : this.lookup.values()) {
						if (!tardis.travel().getPosition().equals(pos))
							continue;

						found = tardis;
					}

					if (found == null)
						return;

					this.sendAndSubscribe(player, found);
				}
		);

		ServerLifecycleEvents.SERVER_STARTING.register(server -> this.fileManager.setLocked(false));
		ServerLifecycleEvents.SERVER_STOPPING.register(this::onShutdown);

		ServerCrashEvent.EVENT.register(((server, report) -> this.reset())); // just panic and reset

		ServerTickEvents.START_SERVER_TICK.register(server -> {
			for (ServerTardis tardis : this.lookup.values()) {
				tardis.startTick(server);
			}
		});

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();

			if (player.getWorld().getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD) {
				// if the player is in a tardis already, sync the one at their location
				Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos(), true);

				if (found == null)
					return;

				this.sendTardis(player, found);
			}
		});

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerTardis tardis : this.lookup.values()) {
				tardis.tick(server);
			}

			this.tickBuffer(id -> server.getPlayerManager().getPlayer(id));
		});
	}

	public ServerTardis create(TardisBuilder builder) {
		ServerTardis tardis = builder.build();
		this.lookup.put(tardis.getUuid(), tardis);
		return tardis;
	}

	@Override
	protected void sendTardis(@NotNull ServerPlayerEntity player, UUID uuid, String json) {
		PacketByteBuf data = PacketByteBufs.create();
		data.writeUuid(uuid);
		data.writeString(json);

		ServerPlayNetworking.send(player, SEND, data);
	}

	@Override
	protected void updateTardisProperty(@NotNull ServerPlayerEntity player, ServerTardis tardis, TardisComponent.Id id, String key, String type, String value) {
		PacketByteBuf data = PacketByteBufs.create();

		data.writeUuid(tardis.getUuid());
		data.writeEnumConstant(id);

		data.writeString(key);
		data.writeString(type);
		data.writeString(value);

		ServerPlayNetworking.send(player, UPDATE, data);
	}

	@Override
	protected void updateTardisProperty(@NotNull ServerPlayerEntity player, ServerTardis tardis, TardisComponent.Id id, Value<?> property) {
		PacketByteBuf data = PacketByteBufs.create();

		data.writeUuid(tardis.getUuid());
		data.writeByte(Property.Mode.forValue(property));

		data.writeEnumConstant(id);
		data.writeString(property.getProperty().getName());
		property.write(data);

		ServerPlayNetworking.send(player, UPDATE_PROPERTY, data);
	}

	@Override
	protected void updateTardis(@NotNull ServerPlayerEntity player, ServerTardis tardis, TardisComponent.Id id, String json) {
		PacketByteBuf data = PacketByteBufs.create();
		data.writeUuid(tardis.getUuid());
		data.writeEnumConstant(id);

		data.writeString(json);
		ServerPlayNetworking.send(player, UPDATE, data);
	}

	@Override
	protected ServerTardis loadTardis(MinecraftServer server, UUID uuid) {
		return this.fileManager.loadTardis(server, this, uuid, this::readTardis, this.lookup::put);
	}

	@Override
	protected GsonBuilder createGsonBuilder(Exclude.Strategy strategy) {
		return super.createGsonBuilder(strategy)
				.registerTypeAdapter(SerialDimension.class, SerialDimension.serializer())
				.registerTypeAdapter(Tardis.class, ServerTardis.creator());
	}

	@Override
	public void getTardis(MinecraftServer server, UUID uuid, Consumer<ServerTardis> consumer) {
		if (uuid == null)
			return; // ugh

		ServerTardis result = this.lookup.get(uuid);

		if (result == null) {
			this.loadTardis(server, uuid, consumer);
			return;
		}

		consumer.accept(result);
	}

	public void sendToSubscribers(Tardis tardis) {
		for (ServerPlayerEntity player : NetworkUtil.getNearbyTardisPlayers(tardis)) {
			this.sendTardis(player, tardis);
		}
	}

	public void sendToSubscribers(TardisComponent component) {
		for (ServerPlayerEntity player : NetworkUtil.getNearbyTardisPlayers(component.tardis())) {
			this.updateTardis(player, (ServerTardis) component.tardis(), component);
		}
	}

	/**
	 * For sending changes to do with the PropertiesHandler
	 *
	 * @param tardis The TARDIS to sync to
	 * @param key    The key where the value is stored in the PropertiesHolder
	 * @param type   The class of the variable serialised, so it can be read later
	 *               eg a double would be "double", boolean would be "boolean", etc
	 * @param value  The new value to be synced to client
	 */
	public void sendPropertyToSubscribers(ServerTardis tardis, TardisComponent component, String key, String type, String value) {
		for (ServerPlayerEntity player : NetworkUtil.getNearbyTardisPlayers(tardis)) {
			this.updateTardisProperty(player, tardis, component, key, type, value);
		}
	}

	public void sendPropertyV2ToSubscribers(ServerTardis tardis, Value<?> property) {
		for (ServerPlayerEntity player : NetworkUtil.getNearbyTardisPlayers(tardis)) {
			this.updateTardisPropertyV2(player, tardis, property);
		}
	}

	public void remove(MinecraftServer server, UUID uuid) {
		this.fileManager.delete(server, uuid);
		super.remove(uuid);
	}

	/**
	 * @deprecated Use {@link #remove(MinecraftServer, UUID)} instead.
	 * @implNote Doesn't actually delete the file, due to the lack of the server instance.
	 */
	@Override
	@Deprecated
	public void remove(UUID uuid) {
		super.remove(uuid);
	}

	public void onShutdown(MinecraftServer server) {
		// force all dematting to go flight and all matting to go land
		for (Tardis tardis : this.lookup.values()) {
			// stop forcing all chunks
			TardisChunkUtil.stopForceExteriorChunk(tardis);
			TardisTravel.State state = tardis.travel().getState();

			if (state == TardisTravel.State.DEMAT) {
				tardis.travel().toFlight();
			} else if (state == TardisTravel.State.MAT) {
				tardis.travel().forceLand();
			}

			tardis.getDoor().closeDoors();
			if (DependencyChecker.hasPortals())
				PortalsHandler.removePortals(tardis);
		}

		this.fileManager.setLocked(true);
		this.fileManager.saveTardis(server, this);
		this.reset();
	}

	public static ServerTardisManager getInstance() {
		return instance;
	}
}
