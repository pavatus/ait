package loqor.ait.tardis;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import loqor.ait.AITMod;
import loqor.ait.core.data.Corners;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.data.schema.console.ConsoleTypeSchema;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.util.gson.ItemStackSerializer;
import loqor.ait.core.util.gson.NbtSerializer;
import loqor.ait.tardis.data.permissions.Permission;
import loqor.ait.tardis.data.permissions.PermissionLike;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class TardisManager<T extends Tardis, C> {

	public static final Identifier ASK = new Identifier(AITMod.MOD_ID, "ask_tardis");
	public static final Identifier ASK_POS = new Identifier("ait", "ask_pos_tardis");

	public static final Identifier SEND = new Identifier(AITMod.MOD_ID, "send_tardis");
	public static final Identifier UPDATE = new Identifier(AITMod.MOD_ID, "update_tardis");

	protected final Map<UUID, T> lookup = new HashMap<>();
	protected final Gson gson;

	protected TardisManager() {
		GsonBuilder builder = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
					@Override
					public boolean shouldSkipField(FieldAttributes field) {
						return field.getAnnotation(Exclude.class) != null;
					}

					@Override
					public boolean shouldSkipClass(Class<?> clazz) {
						return false;
					}
				})
				.registerTypeAdapter(TardisDesktopSchema.class, TardisDesktopSchema.serializer())
				.registerTypeAdapter(ExteriorVariantSchema.class, ExteriorVariantSchema.serializer())
				.registerTypeAdapter(DoorSchema.class, DoorSchema.serializer())
				.registerTypeAdapter(ExteriorCategorySchema.class, ExteriorCategorySchema.serializer())
				.registerTypeAdapter(ConsoleTypeSchema.class, ConsoleTypeSchema.serializer())
				.registerTypeAdapter(ConsoleVariantSchema.class, ConsoleVariantSchema.serializer())
				.registerTypeAdapter(Corners.class, Corners.serializer())
				.registerTypeAdapter(PermissionLike.class, Permission.serializer())
				.registerTypeAdapter(NbtCompound.class, new NbtSerializer())
				.registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
				.registerTypeAdapter(TardisHandlersManager.class, TardisHandlersManager.serializer());

		if (!AITMod.AIT_CONFIG.MINIFY_JSON())
            builder.setPrettyPrinting();

		// TODO replace the type adapters with CODECs. Why do the same job twice?
		this.gson = this.getGsonBuilder(builder).create();
	}

	protected GsonBuilder getGsonBuilder(GsonBuilder builder) {
		return builder;
	}

	public static TardisManager<?, ?> getInstance(Entity entity) {
		return TardisManager.getInstance(entity.getWorld());
	}

	public static TardisManager<?, ?> getInstance(BlockEntity entity) {
		return TardisManager.getInstance(entity.getWorld());
	}

	public static TardisManager<?, ?> getInstance(World world) {
		return TardisManager.getInstance(!world.isClient());
	}

	public static TardisManager<?, ?> getInstance(Tardis tardis) {
		return TardisManager.getInstance((tardis instanceof ServerTardis));
	}

	public static TardisManager<?, ?> getInstance(boolean isServer) {
		return isServer ? ServerTardisManager.getInstance() : ClientTardisManager.getInstance();
	}

	public static <C, R> R with(BlockEntity entity, ContextManager<C, R> consumer) {
		return TardisManager.with(entity.getWorld(), consumer);
	}

	public static <C, R> R with(Entity entity, ContextManager<C, R> consumer) {
		return TardisManager.with(entity.getWorld(), consumer);
	}

	public static <C, R> R with(World world, ContextManager<C, R> consumer) {
		return TardisManager.with(world.isClient(), consumer, world::getServer);
	}

	@SuppressWarnings("unchecked")
	public static <C, R> R with(boolean isClient, ContextManager<C, R> consumer, Supplier<MinecraftServer> server) {
		TardisManager<?, C> manager = (TardisManager<?, C>) TardisManager.getInstance(!isClient);

		if (isClient) {
			return consumer.run((C) MinecraftClient.getInstance(), manager);
		} else {
			return consumer.run((C) server.get(), manager);
		}
	}

	public void getTardis(C c, UUID uuid, Consumer<T> consumer) {
		if (uuid == null)
			return; // ugh

		T result = this.lookup.get(uuid);

		if (result == null) {
			this.loadTardis(c, uuid, consumer);
			return;
		}

		consumer.accept(result);
	}

	protected T readTardis(String json, Class<T> clazz) {
		T tardis = gson.fromJson(json, clazz);
		Tardis.init(tardis, true);

		return tardis;
	}

	/**
	 * By all means a bad practice. Use {@link #getTardis(Object, UUID, Consumer)} instead.
	 * Ensures to return a {@link Tardis} instance as fast as possible.
	 * <p>
	 * By using this method you accept the risk of the tardis not being on the client.
	 *
	 * @deprecated Have you read the comment?
	 */
	@Nullable
	@Deprecated
	public abstract T demandTardis(C c, UUID uuid);

	public abstract void loadTardis(C c, UUID uuid, @Nullable Consumer<T> consumer);

	public void reset() {
		this.lookup.clear();
	}

	/**
	 * @deprecated This method allows you to get currently loaded TARDIS'.
	 */
	@Deprecated
	public Map<UUID, T> getLookup() {
		return this.lookup;
	}

	public Gson getGson() {
		return this.gson;
	}

	@FunctionalInterface
	public interface ContextManager<C, R> {
		R run(C c, TardisManager<?, C> manager);
	}
}
