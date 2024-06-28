package loqor.ait.tardis.data;

import loqor.ait.core.AITSounds;
import loqor.ait.core.item.SiegeTardisItem;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.tardis.data.properties.v2.Property;
import loqor.ait.tardis.data.properties.v2.bool.BoolProperty;
import loqor.ait.tardis.data.properties.v2.bool.BoolValue;
import loqor.ait.tardis.util.TardisUtil;
import loqor.ait.tardis.wrapper.server.manager.ServerTardisManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;
import java.util.UUID;

public class SiegeData extends KeyedTardisComponent implements TardisTickable {
	public static final String HELD_KEY = "siege_held_uuid";

	private static final BoolProperty ACTIVE = new BoolProperty("siege_mode", Property.warnCompat("siege_mode", false));

	private final BoolValue active = ACTIVE.create(this);

	public SiegeData() {
		super(Id.SIEGE);
	}

	static {
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayerEntity player = handler.getPlayer();

			ServerTardisManager.getInstance().forEach(tardis -> {
				if (!tardis.siege().isActive())
					return;

				if (!Objects.equals(tardis.siege().getHeldPlayerUUID(), player.getUuid()))
					return;

				SiegeTardisItem.placeTardis(tardis, SiegeTardisItem.fromEntity(player));
			});
		});
	}

	@Override
	public void onLoaded() {
		active.of(this, ACTIVE);
	}

	public boolean isActive() {
		return active.get();
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

	public void setActive(boolean siege) {
		if (this.tardis.getFuel() <= (0.01 * FuelData.TARDIS_MAX_FUEL))
			return; // The required amount of fuel to enable/disable siege mode

		SoundEvent sound;

		if (siege) {
			sound = AITSounds.SIEGE_ENABLE;
			this.tardis.engine().disablePower();

			TardisUtil.giveEffectToInteriorPlayers(this.tardis, new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0, false, false));
		} else {
			sound = AITSounds.SIEGE_DISABLE;
			this.tardis.alarm().disable();

			if (this.tardis.getExterior().findExteriorBlock().isEmpty())
				this.tardis.travel2().placeExterior(false);
		}

		for (BlockPos console : this.tardis.getDesktop().getConsolePos()) {
			TardisDesktop.playSoundAtConsole(console, sound, SoundCategory.BLOCKS, 3f, 1f);
		}

		this.tardis.removeFuel((0.01 * FuelData.TARDIS_MAX_FUEL) * (this.tardis.tardisHammerAnnoyance + 1));
		this.active.set(siege);
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
		if (this.isSiegeBeingHeld() && tardis().getExterior().findExteriorBlock().isPresent())
			this.setSiegeBeingHeld(null);

		int siegeTime = this.getTimeInSiegeMode() + 1;
		PropertiesHandler.set(tardis(), PropertiesHandler.SIEGE_TIME, this.active.get() ? siegeTime : 0, false);

		if (!this.active.get())
			return;

		// todo add more downsides the longer you are in siege mode as it is meant to fail systems and kill you and that
		// for example, this starts to freeze the player (like we see in the episode) after a minute (change the length if too short) and only if its on the ground, to stop people from just slaughtering lol
		if (this.getTimeInSiegeMode() > (60 * 20) && !this.isSiegeBeingHeld()) {
			for (ServerPlayerEntity player : TardisUtil.getPlayersInInterior(this.tardis)) {
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
			for (PlayerEntity player : TardisUtil.getPlayersInInterior(this.tardis)) {
				// something tells meee this will cause laggg
				if (player.getFrozenTicks() > player.getMinFreezeDamageTicks())
					player.setFrozenTicks(0);
			}
		}
	}
}
