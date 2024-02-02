package mdteam.ait.tardis.data;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
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

    public SiegeData(Tardis tardis) {
        super(tardis, "siege");
    }

    public boolean isSiegeMode() {
        if(getTardis().isEmpty()) return false;
        return PropertiesHandler.getBool(getTardis().get().getHandlers().getProperties(), PropertiesHandler.SIEGE_MODE);
    }
    public boolean isSiegeBeingHeld() {
        if (getTardis().isEmpty()) return false;
        return getTardis().get().getHandlers().getProperties().getData().containsKey(HELD_KEY) &&
                PropertiesHandler.get(getTardis().get().getHandlers().getProperties(), HELD_KEY) != null;
    }
    public UUID getHeldPlayerUUID() {
        if (!isSiegeBeingHeld()) return null;
        if (getTardis().isEmpty()) return null;

        return PropertiesHandler.getUUID(getTardis().get().getHandlers().getProperties(), HELD_KEY);
    }
    public ServerPlayerEntity getHeldPlayer() {
        if (isClient()) return null;

        return TardisUtil.getServer().getPlayerManager().getPlayer(getHeldPlayerUUID());
    }
    public void setSiegeBeingHeld(UUID playerId) {
        if(getTardis().isEmpty()) return;
        if (playerId != null) getTardis().get().getHandlers().getAlarms().enable();

        PropertiesHandler.set(getTardis().get(), HELD_KEY, playerId);
    }
    public int getTimeInSiegeMode() {
        if(getTardis().isEmpty()) return 0;
        return PropertiesHandler.getInt(getTardis().get().getHandlers().getProperties(), PropertiesHandler.SIEGE_TIME);
    }
    // todo this is getting bloateed, merge some if statements together
    public void setSiegeMode(boolean b) {
        if(getTardis().isEmpty()) return;
        if (getTardis().get().getFuel() <= (0.01 * FuelData.TARDIS_MAX_FUEL)) return; // The required amount of fuel to enable/disable siege mode
        if (b) getTardis().get().disablePower();
        if (!b) getTardis().get().getHandlers().getAlarms().disable();
        if (getTardis().get().isSiegeBeingHeld()) return;
        if (!b && getTardis().get().getExterior().findExteriorBlock().isEmpty())
            getTardis().get().getTravel().placeExterior();
        if (b) TardisUtil.giveEffectToInteriorPlayers(getTardis().get(), new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0 , false, false));
        if (b) TardisUtil.getTardisDimension().playSound(null, getTardis().get().getDesktop().getConsolePos(), AITSounds.SIEGE_ENABLE, SoundCategory.BLOCKS, 3f, 1f);
        if (!b) TardisUtil.getTardisDimension().playSound(null, getTardis().get().getDesktop().getConsolePos(), AITSounds.SIEGE_DISABLE, SoundCategory.BLOCKS, 3f, 1f);

        getTardis().get().removeFuel(0.01 * FuelData.TARDIS_MAX_FUEL);

        PropertiesHandler.set(getTardis().get(), PropertiesHandler.SIEGE_MODE, b);
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
        if(getTardis().isEmpty()) return;
        if (getTardis().get().isSiegeBeingHeld() && getTardis().get().getExterior().findExteriorBlock().isPresent()) {
            getTardis().get().setSiegeBeingHeld(null);
        }

        int siegeTime = getTardis().get().getTimeInSiegeMode() + 1;
        PropertiesHandler.set(getTardis().get(), PropertiesHandler.SIEGE_TIME, getTardis().get().isSiegeMode() ? siegeTime : 0, false);

        if (!getTardis().get().isSiegeMode()) return;

        // todo add more downsides the longer you are in siege mode as it is meant to fail systems and kill you and that
        // for example, this starts to freeze the player (like we see in the episode) after a minute (change the length if too short) and only if its on the ground, to stop people from just slaughtering lol
        if (getTardis().get().getTimeInSiegeMode() > (60 * 20) && !getTardis().get().isSiegeBeingHeld()) {
            for (ServerPlayerEntity player : TardisUtil.getPlayersInInterior(getTardis().get())) {
                if (!player.isAlive()) continue;
                if (hasLeatherArmour(player)) {
                    if (player.getFrozenTicks() > player.getMinFreezeDamageTicks())
                        player.setFrozenTicks(0);
                    continue;
                }
                if (player.getFrozenTicks() < player.getMinFreezeDamageTicks()) player.setFrozenTicks(player.getMinFreezeDamageTicks());
                player.setFrozenTicks(player.getFrozenTicks() + 2);
            }
        } else {
            for (PlayerEntity player : TardisUtil.getPlayersInInterior(getTardis().get())) {
                // something tells meee this will cause laggg
                if (player.getFrozenTicks() > player.getMinFreezeDamageTicks())
                    player.setFrozenTicks(0);
            }
        }
    }
}
