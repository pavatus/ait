package loqor.ait.registry;

import loqor.ait.AITMod;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisDesktopSchema;
import loqor.ait.tardis.data.loyalty.Loyalty;
import loqor.ait.tardis.desktops.DatapackDesktop;
import loqor.ait.tardis.desktops.DefaultCaveDesktop;
import loqor.ait.tardis.desktops.DevDesktop;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.util.function.Consumer;

public class DesktopRegistry extends DatapackRegistry<TardisDesktopSchema> {
	public static final Identifier SYNC_TO_CLIENT = new Identifier(AITMod.MOD_ID, "sync_desktops");
	private static DesktopRegistry INSTANCE;

	public void syncToEveryone() {
		if (TardisUtil.getServer() == null) return;

		for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
			syncToClient(player);
		}
	}

	public void syncToClient(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(REGISTRY.size());
		for (TardisDesktopSchema schema : REGISTRY.values()) {
			buf.encodeAsJson(DatapackDesktop.CODEC, schema);
		}
		ServerPlayNetworking.send(player, SYNC_TO_CLIENT, buf);
	}

	public void readFromServer(PacketByteBuf buf) {
		REGISTRY.clear();
		int size = buf.readInt();

		for (int i = 0; i < size; i++) {
			register(buf.decodeAsJson(DatapackDesktop.CODEC));
		}

		AITMod.LOGGER.info("Read {} desktops from server", size);
	}

	public void unlock(Tardis tardis, Loyalty loyalty, Consumer<TardisDesktopSchema> consumer) {
		for (TardisDesktopSchema schema : REGISTRY.values()) {
			if (!schema.getRequirement().biggerEquals(loyalty))
				continue;

			if (tardis.isDesktopUnlocked(schema))
				continue;

			AITMod.LOGGER.debug("Unlocked desktop " + schema.id() + " for tardis [" + tardis.getUuid() + "]");
			tardis.unlockDesktop(schema);

			if (consumer != null)
				consumer.accept(schema);
		}
	}

	public static DesktopRegistry getInstance() {
		if (INSTANCE == null) {
			AITMod.LOGGER.debug("DesktopRegistry was not initialized, Creating a new instance");
			INSTANCE = new DesktopRegistry();
		}

		return INSTANCE;
	}

	public static TardisDesktopSchema DEFAULT_CAVE;
	public static TardisDesktopSchema DEV;

	private void initAitDesktops() {
		// AIT's Default (non-datapack) Desktops
		DEFAULT_CAVE = register(new DefaultCaveDesktop());
		DEV = register(new DevDesktop());
	}

	public void init() {
		super.init();

		// Reading from Datapacks
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return new Identifier(AITMod.MOD_ID, "desktop");
			}

			@Override
			public void reload(ResourceManager manager) {
				DesktopRegistry.getInstance().clearCache();

				for (Identifier id : manager.findResources("desktop", filename -> filename.getPath().endsWith(".json")).keySet()) {
					try (InputStream stream = manager.getResource(id).get().getInputStream()) {
						TardisDesktopSchema created = DatapackDesktop.fromInputStream(stream);

						if (created == null) {
							stream.close();
							continue;
						}

						DesktopRegistry.getInstance().register(created);
						stream.close();

						AITMod.LOGGER.info("Loaded datapack desktop " + created.id().toString());
					} catch (Exception e) {
						AITMod.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
					}
				}

				syncToEveryone();
				giveOutDesktops();
			}
		});
	}

	public void clearCache() {
		REGISTRY.clear();
		initAitDesktops(); // i know we're "clearing" but we need the AIT Desktops no?
	}

	/**
	 * Unlocks all free desktops for all tardises, usually when someone calls /reload as this wont be ran when the world starts
	 * bad but oh well
	 */
	private void giveOutDesktops() {
		if (ServerTardisManager.getInstance() == null)
			return;

		for (ServerTardis tardis : ServerTardisManager.getInstance().getLookup().values()) {
			this.unlock(tardis, Loyalty.MIN, null);
		}
	}
}
