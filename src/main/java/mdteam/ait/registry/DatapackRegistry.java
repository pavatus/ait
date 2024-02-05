package mdteam.ait.registry;

import mdteam.ait.registry.datapack.Identifiable;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A registry which is compatible with datapack registering
 */
public abstract class DatapackRegistry<T extends Identifiable> {
    protected final HashMap<Identifier, T> REGISTRY = new HashMap<>();

    public DatapackRegistry() {
        this.init();
    }
    public T register(T schema) {
        return register(schema, schema.id());
    }
    public T register(T schema, Identifier id) {
        REGISTRY.put(id, schema);
        return schema;
    }
    public T get(Identifier id) {
        return REGISTRY.get(id);
    }
    // todo idk how well this works..
    public T get(int index) {
        return toList().get(index);
    }

    public List<T> toList() {
        return List.copyOf(REGISTRY.values());
    }
    public ArrayList<T> toArrayList() {
        return new ArrayList<>(REGISTRY.values());
    }
    public Iterator<T> iterator() {
        return REGISTRY.values().iterator();
    }
    public int size() {
        return REGISTRY.size();
    }

    public void syncToEveryone() {
        if (TardisUtil.getServer() == null) return;

        for (ServerPlayerEntity player : TardisUtil.getServer().getPlayerManager().getPlayerList()) {
            syncToClient(player);
        }
    }

    public abstract void syncToClient(ServerPlayerEntity player);
    public abstract void readFromServer(PacketByteBuf buf);

    public void init() {
        this.clearCache();
    }

    public void clearCache() {
        REGISTRY.clear();
    }
}
