package loqor.ait.tardis.data;

import loqor.ait.core.AITSounds;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.base.TardisLink;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;

import java.util.UUID;

public class SiegeData extends TardisLink {
	public static final String HELD_KEY = "siege_held_uuid";

	public SiegeData() {
		super(Id.SIEGE);
	}

	public boolean isSiegeMode() {
		return PropertiesHandler.getBool(tardis().properties(), PropertiesHandler.SIEGE_MODE);
	}

	public boolean isSiegeBeingHeld() {
		return tardis().properties().getData().containsKey(HELD_KEY) &&
				PropertiesHandler.get(tardis().properties(), HELD_KEY) != null;
	}

	public UUID getHeldPlayerUUID() {
		if (!isSiegeBeingHeld())
			return null;

		return PropertiesHandler.getUUID(tardis().properties(), HELD_KEY);
	}

	public void setSiegeBeingHeld(UUID playerId) {
		if (playerId != null)
			tardis().alarm().enable();

		PropertiesHandler.set(tardis(), HELD_KEY, playerId);
	}

	public int getTimeInSiegeMode() {
		return PropertiesHandler.getInt(tardis().properties(), PropertiesHandler.SIEGE_TIME);
	}

	public void setSiegeMode(boolean siege) {
		Tardis tardis = this.tardis();

		if (tardis.getFuel() <= (0.01 * FuelData.TARDIS_MAX_FUEL))
			return; // The required amount of fuel to enable/disable siege mode

		if (siege) {
			tardis.disablePower();
			TardisUtil.giveEffectToInteriorPlayers(tardis, new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0, false, false));
			FlightUtil.playSoundAtConsole(tardis, AITSounds.SIEGE_ENABLE, SoundCategory.BLOCKS, 3f, 1f);
		} else {
			tardis.alarm().disable();

			if (tardis.getExterior().findExteriorBlock().isEmpty())
				tardis.getTravel().placeExterior();

			FlightUtil.playSoundAtConsole(tardis, AITSounds.SIEGE_DISABLE, SoundCategory.BLOCKS, 3f, 1f);
		}

		tardis.removeFuel((0.01 * FuelData.TARDIS_MAX_FUEL) * (tardis.tardisHammerAnnoyance + 1));
		PropertiesHandler.set(tardis, PropertiesHandler.SIEGE_MODE, siege);
		this.sync();
	}

	private static boolean hasLeatherArmour(ServerPlayerEntity player) {
		int count = 0;

		for (ItemStack item : player.getArmorItems()) {
			if (item.isOf(Items.LEATHER_HELMET) || item.isOf(Items.LEATHER_CHESTPLATE) || item.isOf(Items.LEATHER_LEGGINGS) || item.isOf(Items.LEATHER_BOOTS)) {
				count++;
			}
		}

		return count == 4;
	}

	@Override
	public void tick(MinecraftServer server) {
		super.tick(server);

		if (this.isSiegeBeingHeld() && tardis().getExterior().findExteriorBlock().isPresent())
			this.setSiegeBeingHeld(null);

		int siegeTime = this.getTimeInSiegeMode() + 1;
		PropertiesHandler.set(tardis(), PropertiesHandler.SIEGE_TIME, tardis().isSiegeMode() ? siegeTime : 0, false);

		if (!tardis().isSiegeMode())
			return;

		// todo add more downsides the longer you are in siege mode as it is meant to fail systems and kill you and that
		// for example, this starts to freeze the player (like we see in the episode) after a minute (change the length if too short) and only if its on the ground, to stop people from just slaughtering lol
		if (this.getTimeInSiegeMode() > (60 * 20) && !this.isSiegeBeingHeld()) {
			for (ServerPlayerEntity player : TardisUtil.getPlayersInInterior(tardis())) {
				if (!player.isAlive()) continue;
				if (hasLeatherArmour(player)) {
					if (player.getFrozenTicks() > player.getMinFreezeDamageTicks())
						player.setFrozenTicks(0);
					continue;
				}
				if (player.getFrozenTicks() < player.getMinFreezeDamageTicks())
					player.setFrozenTicks(player.getMinFreezeDamageTicks());
				player.setFrozenTicks(player.getFrozenTicks() + 2);
			}
		} else {
			for (PlayerEntity player : TardisUtil.getPlayersInInterior(tardis())) {
				// something tells meee this will cause laggg
				if (player.getFrozenTicks() > player.getMinFreezeDamageTicks())
					player.setFrozenTicks(0);
			}
		}
	}
}
