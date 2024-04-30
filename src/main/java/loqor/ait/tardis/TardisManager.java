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
import loqor.ait.tardis.data.TardisHandlersManager;
import loqor.ait.tardis.data.permissions.Permission;
import loqor.ait.tardis.data.permissions.PermissionLike;
import loqor.ait.tardis.link.Linkable;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class TardisManager<T extends Tardis> {

	public static final Identifier ASK = new Identifier(AITMod.MOD_ID, "ask_tardis");
	public static final Identifier ASK_POS = new Identifier("ait", "ask_pos_tardis");

	public static final Identifier SEND = new Identifier(AITMod.MOD_ID, "send_tardis");
	public static final Identifier UPDATE = new Identifier(AITMod.MOD_ID, "update_tardis");

	protected final Map<UUID, T> lookup = new HashMap<>();
	protected final Gson gson;

	public TardisManager() {
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

	public static TardisManager<?> getInstance(Entity entity) {
		return TardisManager.getInstance(entity.getWorld());
	}

	public static TardisManager<?> getInstance(BlockEntity entity) {
		return TardisManager.getInstance(entity.getWorld());
	}

	public static TardisManager<?> getInstance(World world) {
		return TardisManager.getInstance(!world.isClient());
	}

	@Deprecated
	public static TardisManager<?> getInstance() {
		return TardisManager.getInstance(TardisUtil.isServer());
	}

	public static TardisManager<?> getInstance(boolean isServer) {
		return isServer ? ServerTardisManager.getInstance() : ClientTardisManager.getInstance();
	}

	public void link(UUID uuid, Linkable linkable) {
		this.getTardis(uuid, linkable::setTardis);
	}

	public void getTardis(UUID uuid, Consumer<T> consumer) {
		if (this.lookup.containsKey(uuid)) {
			consumer.accept(this.lookup.get(uuid));
			return;
		}

		this.loadTardis(uuid, consumer);
	}

	/**
	 * Gets the ClientTardis if it's in the lookup, should only be called if you are 100% sure the client has this tardis.
	 *
	 * @deprecated This method is stupid. Use {@link TardisManager#getTardis(UUID, Consumer)} instead.
	 * Why is it stupid? Because {@link TardisManager#getTardis(UUID, Consumer)} will yield the same results,
	 * given that the client has this tardis!
	 *
	 * @param uuid The UUID of the tardis
	 * @param performAsk Whether to ask for this tardis if we don't have it
	 */
	@Deprecated
	public Tardis getTardis(UUID uuid, boolean performAsk) {
		if (!this.hasTardis(uuid)) {
			AITMod.LOGGER.error("Called getTardis() on a tardis that hasnt been synced!");

			if (performAsk)
				this.getTardis(uuid, (t) -> {
				});

			return null;
		}

		return this.lookup.get(uuid);
	}

	/**
	 * @deprecated This method SHOULD NOT be in {@link ClientTardisManager}. It should be exclusive to {@link ServerTardisManager}
	 */
	@Deprecated
	public Tardis getTardis(UUID uuid) {
		return this.getTardis(uuid, false);
	}

	public boolean hasTardis(UUID uuid) {
		return this.lookup.containsKey(uuid);
	}

	public abstract void loadTardis(UUID uuid, Consumer<T> consumer);

	public void reset() {
		this.lookup.clear();
	}

	@Deprecated // only use if necessary, otherwise use loadTardis / getTardis etc
	public Map<UUID, T> getLookup() {
		return this.lookup;
	}

	public Gson getGson() {
		return this.gson;
	}
}
