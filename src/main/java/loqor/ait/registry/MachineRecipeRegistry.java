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
import java.util.Collection;
import java.util.Optional;

public class MachineRecipeRegistry extends DatapackRegistry<MachineRecipeSchema> {

	private static MachineRecipeRegistry INSTANCE;

	public void syncToEveryone() { }

	public void syncToClient(ServerPlayerEntity player) { }

	public void readFromServer(PacketByteBuf buf) { }

	public Optional<MachineRecipeSchema> findMatching(Collection<ItemStack> set) {
		for (MachineRecipeSchema schema : REGISTRY.values()) {
			if (StackUtil.equals(set, schema.input()))
				return Optional.of(schema.copy());
		}

		return Optional.empty();
	}

	public Optional<MachineRecipeSchema> findMatching(ItemStack result) {
		for (MachineRecipeSchema schema : REGISTRY.values()) {
			if (ItemStack.areItemsEqual(schema.output(), result))
				return Optional.of(schema.copy());
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
