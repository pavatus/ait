package loqor.ait.tardis.data;

import loqor.ait.core.AITSounds;
import loqor.ait.core.item.SiegeTardisItem;
import loqor.ait.tardis.TardisDesktop;
import loqor.ait.tardis.base.KeyedTardisComponent;
import loqor.ait.tardis.base.TardisTickable;
import loqor.ait.tardis.data.properties.Property;
import loqor.ait.tardis.data.properties.Value;
import loqor.ait.tardis.data.properties.bool.BoolProperty;
import loqor.ait.tardis.data.properties.bool.BoolValue;
import loqor.ait.tardis.data.properties.integer.IntProperty;
import loqor.ait.tardis.data.properties.integer.IntValue;
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

public class SiegeHandler extends KeyedTardisComponent implements TardisTickable {
	private static final Property<UUID> HELD_KEY = new Property<>(Property.Type.UUID, "siege_held_uuid", new UUID(0, 0));
	private final Value<UUID> heldKey = HELD_KEY.create(this);
	private static final BoolProperty ACTIVE = new BoolProperty("siege_mode", false);
	private static final IntProperty SIEGE_TIME = new IntProperty("siege_time", 0);
	private final IntValue siegeTime = SIEGE_TIME.create(this);
	private final BoolValue active = ACTIVE.create(this);

	public SiegeHandler() {
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
		siegeTime.of(this, SIEGE_TIME);
		heldKey.of(this, HELD_KEY);
	}

	public boolean isActive() {
		return active.get();
	}

	public boolean isSiegeBeingHeld() {
		return heldKey.get() != null;
	}

	public UUID getHeldPlayerUUID() {
		if (!isSiegeBeingHeld())
			return null;

		return heldKey.get();
	}

	public void setSiegeBeingHeld(UUID playerId) {
		if (playerId != null)
			this.tardis.alarm().enabled().set(true);

		heldKey.set(playerId);
	}

	public int getTimeInSiegeMode() {
		return this.siegeTime.get();
	}

	public void setActive(boolean siege) {
		if (this.tardis.getFuel() <= (0.01 * FuelHandler.TARDIS_MAX_FUEL))
			return; // The required amount of fuel to enable/disable siege mode

		SoundEvent sound;

		if (siege) {
			sound = AITSounds.SIEGE_ENABLE;
			this.tardis.engine().disablePower();

			TardisUtil.giveEffectToInteriorPlayers(this.tardis, new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0, false, false));
		} else {
			sound = AITSounds.SIEGE_DISABLE;
			this.tardis.alarm().enabled().set(false);

			if (this.tardis.getExterior().findExteriorBlock().isEmpty())
				this.tardis.travel().placeExterior(false);
		}

		for (BlockPos console : this.tardis.getDesktop().getConsolePos()) {
			TardisDesktop.playSoundAtConsole(console, sound, SoundCategory.BLOCKS, 3f, 1f);
		}

		this.tardis.removeFuel((0.01 * FuelHandler.TARDIS_MAX_FUEL)
				* (this.tardis.travel().getHammerUses() + 1));

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

		int siege_time = this.getTimeInSiegeMode() + 1;
		this.siegeTime.set(this.active.get() ? siege_time : 0);

		if (!this.active.get())
			return;

		// todo add more downsides the longer you are in siege mode as it is meant to fail systems and kill you and that
		if (this.getTimeInSiegeMode() > (60 * 20) && !this.isSiegeBeingHeld()) {
			for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(this.tardis)) {
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
			for (PlayerEntity player : TardisUtil.getPlayersInsideInterior(this.tardis)) {
				if (player.getFrozenTicks() > player.getMinFreezeDamageTicks())
					player.setFrozenTicks(0);
			}
		}
	}
}
