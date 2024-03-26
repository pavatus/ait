package loqor.ait.registry;

import loqor.ait.AITMod;
import loqor.ait.tardis.exterior.category.*;
import loqor.ait.tardis.util.TardisUtil;
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

public class CategoryRegistry extends DatapackRegistry<ExteriorCategorySchema> {
	public static final Identifier SYNC_TO_CLIENT = new Identifier(AITMod.MOD_ID, "sync_categories");

	private static CategoryRegistry INSTANCE;

	public static ExteriorCategorySchema registerStatic(ExteriorCategorySchema schema) {
		return getInstance().register(schema);
	}

	public void syncToEveryone() {
		if (TardisUtil.getServer() == null) return;

		for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
			syncToClient(player);
		}
	}

	public void syncToClient(ServerPlayerEntity player) {
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(REGISTRY.size());
		for (ExteriorCategorySchema schema : REGISTRY.values()) {
			buf.encodeAsJson(DatapackCategory.CODEC, schema);
		}
		ServerPlayNetworking.send(player, SYNC_TO_CLIENT, buf);
	}

	public void readFromServer(PacketByteBuf buf) {
		REGISTRY.clear();
		int size = buf.readInt();

		for (int i = 0; i < size; i++) {
			register(buf.decodeAsJson(DatapackCategory.CODEC));
		}

		AITMod.LOGGER.info("Read {} categories from server", size);
	}

	public static CategoryRegistry getInstance() {
		if (INSTANCE == null) {
			AITMod.LOGGER.info("CategoryRegistry was not initialized, Creating a new instance");
			INSTANCE = new CategoryRegistry();
			INSTANCE.init();
		}

		return INSTANCE;
	}

	// todo move to an "AITExteriors" type thing, same for all other registries. this is fine for now cus i cant b bothered
	public static ExteriorCategorySchema CLASSIC;
	public static ExteriorCategorySchema CAPSULE;
	public static ExteriorCategorySchema POLICE_BOX;
	public static ExteriorCategorySchema TARDIM;
	public static ExteriorCategorySchema CUBE; // dont use
	public static ExteriorCategorySchema BOOTH;
	public static ExteriorCategorySchema EASTER_HEAD;
	public static ExteriorCategorySchema CORAL_GROWTH;
	public static ExteriorCategorySchema DOOM;
	public static ExteriorCategorySchema PLINTH;
	public static ExteriorCategorySchema RENEGADE;

	private static void registerDefaults() {
		CLASSIC = registerStatic(new ClassicCategory());
		CAPSULE = registerStatic(new CapsuleCategory());
		POLICE_BOX = registerStatic(new PoliceBoxCategory());
		TARDIM = registerStatic(new TardimCategory());
		// CUBE = register(new CubeExterior()); // fixme how could i do this, remove the cube instead of fixing a bug : (
		BOOTH = registerStatic(new BoothCategory());
		EASTER_HEAD = registerStatic(new EasterHeadCategory());
		CORAL_GROWTH = registerStatic(new GrowthCategory());
		DOOM = registerStatic(new DoomCategory());
		PLINTH = registerStatic(new PlinthCategory());
		RENEGADE = registerStatic(new RenegadeCategory());
	}

	public void init() {
		super.init();

        /*if (INSTANCE != null) {
            AITMod.LOGGER.error("Tried to init Categories but it was already initialized");
            return;
        }*/

		registerDefaults();

		// Reading from Datapacks
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return new Identifier(AITMod.MOD_ID, "categories");
			}

			@Override
			public void reload(ResourceManager manager) {
				getInstance().clearCache();
				registerDefaults();

				for (Identifier id : manager.findResources("categories", filename -> filename.getPath().endsWith(".json")).keySet()) {
					try (InputStream stream = manager.getResource(id).get().getInputStream()) {
						ExteriorCategorySchema created = DatapackCategory.fromInputStream(stream);

						if (created == null) {
							stream.close();
							continue;
						}

						getInstance().register(created);
						stream.close();
						AITMod.LOGGER.info("Loaded datapack category " + created.id().toString());
					} catch (Exception e) {
						AITMod.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
					}
				}

				syncToEveryone();
			}
		});
	}
}
