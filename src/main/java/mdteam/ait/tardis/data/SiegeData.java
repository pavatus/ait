package mdteam.ait.tardis.data;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;

import java.util.UUID;

public class SiegeData extends TardisLink {
    private boolean isSiegeMode = false;
    private UUID heldPlayer;

    public SiegeData(Tardis tardis) {
        super(tardis, "siege");
    }

    public boolean isSiegeMode() {
        return this.isSiegeMode;
    }
    public boolean isSiegeBeingHeld() {
        return this.heldPlayer != null;
    }
    public UUID getHeldPlayerUUID() {
        if (!isSiegeBeingHeld()) return null;

        return this.heldPlayer;
    }
    public ServerPlayerEntity getHeldPlayer() {
        if (isClient()) return null;

        return TardisUtil.getServer().getPlayerManager().getPlayer(getHeldPlayerUUID());
    }
    public void setSiegeBeingHeld(UUID playerId) {
        if (playerId != null) tardis().getHandlers().getAlarms().enable();

        this.heldPlayer = playerId;
        this.sync();
    }
    public int getTimeInSiegeMode() {
        return PropertiesHandler.getInt(tardis().getHandlers().getProperties(), PropertiesHandler.SIEGE_TIME);
    }
    // todo this is getting bloateed, merge some if statements together
    public void setSiegeMode(boolean b) {
        if (tardis().getFuel() <= (0.01 * FuelData.TARDIS_MAX_FUEL)) return; // The required amount of fuel to enable/disable siege mode
        if (b) tardis().disablePower();
        if (!b) tardis().getHandlers().getAlarms().disable();
        if (tardis().isSiegeBeingHeld()) return;
        if (!b && tardis().getExterior().findExteriorBlock().isEmpty())
            tardis().getTravel().placeExterior();
        if (b) TardisUtil.giveEffectToInteriorPlayers(tardis(), new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0 , false, false));
        if (b) TardisUtil.getTardisDimension().playSound(null, tardis().getDesktop().getConsolePos(), AITSounds.SIEGE_ENABLE, SoundCategory.BLOCKS, 3f, 1f);
        if (!b) TardisUtil.getTardisDimension().playSound(null, tardis().getDesktop().getConsolePos(), AITSounds.SIEGE_DISABLE, SoundCategory.BLOCKS, 3f, 1f);

        tardis().removeFuel(0.01 * FuelData.TARDIS_MAX_FUEL);

        this.isSiegeMode = b;
        this.sync();
    }

    @Override
    public void tick(MinecraftServer server) {
        super.tick(server);
        if (tardis().getExterior().findExteriorBlock().isPresent()) {
            tardis().setSiegeBeingHeld(null);
        }

        int siegeTime = tardis().getTimeInSiegeMode() + 1;
        PropertiesHandler.set(tardis(), PropertiesHandler.SIEGE_TIME, tardis().isSiegeMode() ? siegeTime : 0);
        // this.markDirty(); // DO NOT UNCOMMENT THAT LAG GOES CRAZYYYY!!!

        // todo add more downsides the longer you are in siege mode as it is meant to fail systems and kill you and that
        // for example, this starts to freeze the player (like we see in the episode) after a minute (change the length if too short) and only if its on the ground, to stop people from just slaughtering lol
        if (tardis().getTimeInSiegeMode() > (60 * 20) && !tardis().isSiegeBeingHeld()) {
            for (PlayerEntity player : TardisUtil.getPlayersInInterior(tardis())) {
                if (!player.isAlive()) continue;
                if (player.getFrozenTicks() < player.getMinFreezeDamageTicks()) player.setFrozenTicks(player.getMinFreezeDamageTicks());
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
