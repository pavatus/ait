package loqor.ait.registry.datapack;

import loqor.ait.core.data.base.Identifiable;
import loqor.ait.registry.Registry;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

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

		int randomized = random.nextInt(
				elements.size()
		);

		return elements.get(randomized);
	}

	public T getRandom(Random random) {
		return DatapackRegistry.getRandom(this.toList(), random, this::fallback);
	}

	public T getRandom() {
		return this.getRandom(RANDOM);
	}

	public T get(Identifier id) {
		return REGISTRY.get(id);
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
		for (ServerPlayerEntity player : TardisUtil.getPlayerManager().getPlayerList()) {
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
