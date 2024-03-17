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

	public void addPlayer(ServerPlayerEntity player) {
		this.setLoyalty(player, Loyalty.NEUTRAL);
	}

	public void subtractLoyaltyLevel(ServerPlayerEntity player, int loyaltyValue) {
		int playerLevel = this.get(player).level;
		int newLevel = Math.min(Math.max(playerLevel - loyaltyValue, 0), Loyalty.OWNER.level);
		this.setLoyalty(player, this.levelToLoyaltyValue(newLevel));
	}

	public void addLoyaltyLevel(ServerPlayerEntity player, int loyaltyValue) {
		int playerLevel = this.get(player).level;
		int newLevel = Math.min(Math.max(playerLevel + loyaltyValue, 0), Loyalty.OWNER.level);
		this.setLoyalty(player, this.levelToLoyaltyValue(newLevel));
	}

	public void setLoyalty(ServerPlayerEntity player, Loyalty loyalty) {
		this.data().put(player.getUuid(), loyalty);

		this.sync();
	}

	public Loyalty get(ServerPlayerEntity player) {
		return this.data().get(player.getUuid());
	}

	public Loyalty levelToLoyaltyValue(int level) {
		if (level > 0 && level <= 25) {
			return Loyalty.NEUTRAL;
		}
		if (level > 25 && level <= 50) {
			return Loyalty.COMPANION;
		}
		if (level > 50 && level <= 75) {
			return Loyalty.PILOT;
		}
		if (level > 75 && level <= 100) {
			return Loyalty.OWNER;
		}
		return Loyalty.REJECT;
	}
}
