package loqor.ait.tardis.data.loyalty;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TardisLink;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class LoyaltyHandler extends TardisLink {
    private final Map<UUID, Loyalty> data;

    public LoyaltyHandler(Tardis tardis, HashMap<UUID, Loyalty> data) {
        super(tardis, "loyalty");
        this.data = data;
    }

    public LoyaltyHandler(Tardis tardis) {
        this(tardis, new HashMap<>());
    }

    public Map<UUID, Loyalty> data() {
        return this.data;
    }

    public Loyalty get(PlayerEntity player) {
        return this.data.getOrDefault(player.getUuid(), new Loyalty(Loyalty.Type.NEUTRAL));
    }

    public void set(PlayerEntity player, Loyalty loyalty) {
        this.data.put(player.getUuid(), loyalty);
        this.sync();
    }

    public void update(ServerPlayerEntity player, Function<Loyalty, Loyalty> consumer) {
        Loyalty current = this.get(player);
        current = consumer.apply(current);

        this.set(player, current);
    }

    public void subLevel(ServerPlayerEntity player, int level) {
        this.update(player, loyalty -> loyalty.subtract(level));
    }

    public void addLevel(ServerPlayerEntity player, int level) {
        this.update(player, loyalty -> loyalty.add(level));
    }
}
