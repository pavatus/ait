package loqor.ait.tardis;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.google.gson.*;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import loqor.ait.AITMod;
import loqor.ait.core.data.Corners;
import loqor.ait.core.data.DirectedBlockPos;
import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.data.base.Exclude;
import loqor.ait.core.data.schema.console.ConsoleTypeSchema;
import loqor.ait.core.data.schema.console.ConsoleVariantSchema;
import loqor.ait.core.data.schema.door.DoorSchema;
import loqor.ait.core.data.schema.exterior.ExteriorCategorySchema;
import loqor.ait.core.data.schema.exterior.ExteriorVariantSchema;
import loqor.ait.core.util.gson.*;
import loqor.ait.registry.impl.TardisComponentRegistry;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.landing.LandingPadManager;
import loqor.ait.tardis.data.permissions.Permission;
import loqor.ait.tardis.data.permissions.PermissionLike;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.data.properties.doubl3.DoubleValue;
import loqor.ait.tardis.data.properties.integer.IntValue;
import loqor.ait.tardis.data.properties.integer.ranged.RangedIntValue;
import loqor.ait.tardis.util.TardisMap;
import loqor.ait.tardis.wrapper.client.manager.ClientTardisManager;
import loqor.ait.tardis.wrapper.server.ServerTardis;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;

public abstract class TardisManager<T extends Tardis, C> {

    public static final Identifier ASK = new Identifier(AITMod.MOD_ID, "ask_tardis");
    public static final Identifier ASK_POS = new Identifier("ait", "ask_pos_tardis");

    public static final Identifier SEND = new Identifier(AITMod.MOD_ID, "send_tardis");
    public static final Identifier SEND_BULK = new Identifier(AITMod.MOD_ID, "send_tardis_bulk");
    public static final Identifier REMOVE = new Identifier(AITMod.MOD_ID, "remove_tardis");

    protected final TardisMap<T> lookup = new TardisMap<>();

    protected final Gson networkGson;
    protected final Gson fileGson;

    protected TardisManager() {
        this.networkGson = this.getNetworkGson(this.createGsonBuilder(Exclude.Strategy.NETWORK)).create();
        this.fileGson = this.getFileGson(this.createGsonBuilder(Exclude.Strategy.FILE)).create();
    }

    protected GsonBuilder createGsonBuilder(Exclude.Strategy strategy) {
        return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                Exclude exclude = field.getAnnotation(Exclude.class);

                if (exclude == null)
                    return false;

                Exclude.Strategy excluded = exclude.strategy();
                return excluded == Exclude.Strategy.ALL || excluded == strategy;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).registerTypeAdapter(TardisDesktopSchema.class, TardisDesktopSchema.serializer())
                .registerTypeAdapter(ExteriorVariantSchema.class, ExteriorVariantSchema.serializer())
                .registerTypeAdapter(DoorSchema.class, DoorSchema.serializer())
                .registerTypeAdapter(ExteriorCategorySchema.class, ExteriorCategorySchema.serializer())
                .registerTypeAdapter(ConsoleTypeSchema.class, ConsoleTypeSchema.serializer())
                .registerTypeAdapter(ConsoleVariantSchema.class, ConsoleVariantSchema.serializer())
                .registerTypeAdapter(Corners.class, Corners.serializer())
                .registerTypeAdapter(PermissionLike.class, Permission.serializer())
                .registerTypeAdapter(DirectedGlobalPos.class, DirectedGlobalPos.serializer())
                .registerTypeAdapter(DirectedBlockPos.class, DirectedBlockPos.serializer())
                .registerTypeAdapter(NbtCompound.class, new NbtSerializer())
                .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
                .registerTypeAdapter(Identifier.class, new IdentifierSerializer())
                .registerTypeAdapter(GlobalPos.class, new GlobalPosSerializer())
                .registerTypeAdapter(BlockPos.class, new BlockPosSerializer())
                .registerTypeAdapter(RegistryKey.class, new RegistryKeySerializer())
                .registerTypeAdapter(TardisHandlersManager.class, TardisHandlersManager.serializer())
                .registerTypeAdapter(TardisComponent.IdLike.class, TardisComponentRegistry.idSerializer())
                .registerTypeAdapter(TardisDesktop.class, TardisDesktop.updater())
                .registerTypeAdapter(LandingPadManager.LandingPadSpot.class, LandingPadManager.LandingPadSpot.serializer());
    }

    protected GsonBuilder getNetworkGson(GsonBuilder builder) {
        return builder;
    }

    protected GsonBuilder getFileGson(GsonBuilder builder) {
        if (!AITMod.AIT_CONFIG.MINIFY_JSON())
            builder.setPrettyPrinting();

        return builder.registerTypeAdapter(Value.class, Value.serializer())
                .registerTypeAdapter(BoolValue.class, BoolValue.serializer())
                .registerTypeAdapter(IntValue.class, IntValue.serializer())
                .registerTypeAdapter(RangedIntValue.class, RangedIntValue.serializer())
                .registerTypeAdapter(DoubleValue.class, DoubleValue.serializer());
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

    @SuppressWarnings("unchecked")
    protected T readTardis(Gson gson, String raw) {
        T tardis = (T) gson.fromJson(raw, Tardis.class);
        Tardis.init(tardis, TardisComponent.InitContext.deserialize());

        return tardis;
    }

    @SuppressWarnings("unchecked")
    protected T readTardis(Gson gson, JsonObject json) {
        T tardis = (T) gson.fromJson(json, Tardis.class);
        Tardis.init(tardis, TardisComponent.InitContext.deserialize());

        return tardis;
    }

    /**
     * By all means a bad practice. Use {@link #getTardis(Object, UUID, Consumer)}
     * instead. Ensures to return a {@link Tardis} instance as fast as possible.
     * <p>
     * By using this method you accept the risk of the tardis not being on the
     * client.
     *
     * @deprecated Have you read the comment?
     */
    @Nullable @Deprecated
    public abstract T demandTardis(C c, UUID uuid);

    public abstract void loadTardis(C c, UUID uuid, @Nullable Consumer<T> consumer);

    public void reset() {
        this.lookup.clear();
    }

    public Collection<UUID> ids() {
        return this.lookup.keySet();
    }

    public void forEach(Consumer<T> consumer) {
        this.lookup.forEach((uuid, t) -> consumer.accept(t));
    }

    public T find(Predicate<T> predicate) {
        for (T t : this.lookup.values()) {
            if (predicate.test(t))
                return t;
        }

        return null;
    }

    public Gson getNetworkGson() {
        return this.networkGson;
    }

    public Gson getFileGson() {
        return fileGson;
    }

    @FunctionalInterface
    public interface ContextManager<C, R> {
        R run(C c, TardisManager<?, C> manager);
    }
}
