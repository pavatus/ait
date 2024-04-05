package loqor.ait.tardis.data.loyalty;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.TardisLink;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.UUID;

public class LoyaltyData extends TardisLink {
    private final HashMap<UUID, Loyalty> data;

    public LoyaltyData(Tardis tardisId, HashMap<UUID, Loyalty> data) {
        super(tardisId, "loyalty");
        this.data = data;
    }

    public LoyaltyData(Tardis tardis) {
        this(tardis, new HashMap<>());
    }

    public HashMap<UUID, Loyalty> data() {
        return this.data;
    }

    public void add(ServerPlayerEntity player) {
        this.set(player, Loyalty.NEUTRAL);
    }
    public Loyalty get(ServerPlayerEntity player) {
        return this.data().get(player.getUuid());
    }

    public void set(ServerPlayerEntity player, Loyalty loyalty) {
        this.data().put(player.getUuid(), loyalty);

        this.sync();
    }

    public void subtractLoyalty(ServerPlayerEntity player, int loyaltyValue) {
        int playerLevel = this.get(player).level;
        int newLevel = Math.min(Math.max(playerLevel - loyaltyValue, 0), Loyalty.OWNER.level);
        this.set(player,  Loyalty.get(newLevel));
    }

    public void addLoyalty(ServerPlayerEntity player, int loyaltyValue) {
        int playerLevel = this.get(player).level;
        int newLevel = Math.min(Math.max(playerLevel + loyaltyValue, 0), Loyalty.OWNER.level);
        this.set(player, Loyalty.get(newLevel));
    }
}
