package loqor.ait.registry;

import loqor.ait.AITMod;
import loqor.ait.core.util.StackUtil;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.util.Optional;
import java.util.Set;

public class MachineRecipeRegistry extends DatapackRegistry<MachineRecipeSchema> {
	//public static final Identifier SYNC_TO_CLIENT = new Identifier(AITMod.MOD_ID, "sync_machines");
	private static MachineRecipeRegistry INSTANCE;

	public void syncToEveryone() {
		/*if (TardisUtil.getServer() == null) return;

		for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
			syncToClient(player);
		}*/
	}

	public void syncToClient(ServerPlayerEntity player) {
		/*PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(REGISTRY.size());
		for (MachineRecipeSchema schema : REGISTRY.values()) {
			buf.encodeAsJson(DatapackMachineRecipe.CODEC, schema);
		}
		ServerPlayNetworking.send(player, SYNC_TO_CLIENT, buf);*/
	}

	public void readFromServer(PacketByteBuf buf) {
		/*REGISTRY.clear();
		int size = buf.readInt();

		for (int i = 0; i < size; i++) {
			register(buf.decodeAsJson(DatapackMachineRecipe.CODEC));
		}

		AITMod.LOGGER.info("Read {} datapack machine recipes from server", size);*/
	}

	public Optional<MachineRecipeSchema> findMatching(Set<ItemStack> set) {
		for (MachineRecipeSchema schema : REGISTRY.values()) {
			if (StackUtil.equals(set, schema.input()))
				return Optional.of(schema);
		}

		return Optional.empty();
	}

	public Optional<MachineRecipeSchema> findMatching(ItemStack result) {
		for (MachineRecipeSchema schema : REGISTRY.values()) {
			if (ItemStack.areItemsEqual(schema.output(), result))
				return Optional.of(schema);
		}

		return Optional.empty();
	}

	public static MachineRecipeRegistry getInstance() {
		if (INSTANCE == null) {
			AITMod.LOGGER.debug("MachineRecipeRegistry was not initialized, creating a new instance");
			INSTANCE = new MachineRecipeRegistry();
		}

		return INSTANCE;
	}

	public void init() {
		super.init();

		// Reading from Datapacks
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return new Identifier(AITMod.MOD_ID, "machine_recipe");
			}

			@Override
			public void reload(ResourceManager manager) {
				MachineRecipeRegistry.getInstance().clearCache();

				for (Identifier id : manager.findResources("machine_recipe", filename -> filename.getPath().endsWith(".json")).keySet()) {
					try (InputStream stream = manager.getResource(id).get().getInputStream()) {
						MachineRecipeSchema created = DatapackMachineRecipe.fromInputStream(stream);

						if (created == null) {
							stream.close();
							continue;
						}

						MachineRecipeRegistry.getInstance().register(created);
						stream.close();
						AITMod.LOGGER.info("Loaded datapack machine recipe " + created.id().toString());
					} catch (Exception e) {
						AITMod.LOGGER.error("Error occurred while loading resource json " + id.toString(), e);
					}
				}

				//syncToEveryone();
			}
		});
	}
}
