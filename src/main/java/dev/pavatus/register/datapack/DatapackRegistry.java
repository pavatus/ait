package dev.pavatus.register.datapack;

import java.util.*;
import java.util.function.Supplier;

import dev.pavatus.lib.util.ServerLifecycleHooks;
import dev.pavatus.register.Registry;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import loqor.ait.api.Identifiable;

/**
 * A registry which is compatible with datapack registering
 */
public abstract class DatapackRegistry<T extends Identifiable> implements Registry {

    protected static final Random RANDOM = new Random();
    protected final HashMap<Identifier, T> REGISTRY = new HashMap<>();

    public abstract T fallback();

    public T register(T schema) {
        return register(schema, schema.id());
    }

    public T register(T schema, Identifier id) {
        REGISTRY.put(id, schema);
        return schema;
    }

    protected static <T> T getRandom(List<T> elements, Random random, Supplier<T> fallback) {
        if (elements.isEmpty())
            return fallback.get();

        int randomized = random.nextInt(elements.size());

        return elements.get(randomized);
    }

    public T getRandom(Random random) {
        return DatapackRegistry.getRandom(this.toList(), random, this::fallback);
    }

    public T getRandom() {
        return this.getRandom(RANDOM);
    }

    public List<T> getRandom(int count) {
        List<T> list = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            list.add(this.getRandom());
        }

        return list;
    }

    public T get(Identifier id) {
        return REGISTRY.get(id);
    }
    public T getOrElse(Identifier id, T fallback) {
        return REGISTRY.getOrDefault(id, fallback);
    }
    public T getOrFallback(Identifier id) {
        return this.getOrElse(id, this.fallback());
    }
    public Optional<T> getOptional(Identifier id) {
        return Optional.ofNullable(this.get(id));
    }

    public List<T> toList() {
        return List.copyOf(REGISTRY.values());
    }

    public Iterator<T> iterator() {
        return REGISTRY.values().iterator();
    }

    public int size() {
        return REGISTRY.size();
    }

    public void syncToEveryone() {
        for (ServerPlayerEntity player : ServerLifecycleHooks.get().getPlayerManager().getPlayerList()) {
            this.syncToClient(player);
        }
    }

    public abstract void syncToClient(ServerPlayerEntity player);

    public abstract void readFromServer(PacketByteBuf buf);

    @Override
    public void onCommonInit() {
        this.clearCache();
    }

    public void clearCache() {
        REGISTRY.clear();
    }
}
