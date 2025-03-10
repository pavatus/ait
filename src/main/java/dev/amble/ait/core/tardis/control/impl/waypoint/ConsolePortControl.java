package dev.amble.ait.core.tardis.control.impl.waypoint;

import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import dev.amble.ait.AITMod;
import dev.amble.ait.core.AITSounds;
import dev.amble.ait.core.item.WaypointItem;
import dev.amble.ait.core.tardis.Tardis;
import dev.amble.ait.core.tardis.TardisDesktop;
import dev.amble.ait.core.tardis.control.Control;
import dev.amble.ait.data.Waypoint;
import dev.amble.ait.module.gun.core.item.StaserBoltMagazine;

public class ConsolePortControl extends Control {

    private SoundEvent currentMusic = null;

    public ConsolePortControl() {
        super(AITMod.id("console_port"));
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console,
                             boolean leftClick) {
        if (leftClick) {
            if (!tardis.extra().getInsertedDisc().isEmpty()) {
                ejectDisc(tardis, player, world, console);
                return true;
            }

            tardis.waypoint().spawnItem(console);
            return true;
        }

        ItemStack itemStack = player.getMainHandStack();

        if (itemStack.getItem() instanceof MusicDiscItem musicDisc) {
            if (!tardis.extra().getInsertedDisc().isEmpty()) return false;

            tardis.extra().setInsertedDisc(itemStack.copy());
            currentMusic = musicDisc.getSound();

            world.playSound(null, console, currentMusic, SoundCategory.RECORDS, 6f, 1);
            player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

            return true;
        }

        if (itemStack.getItem() instanceof StaserBoltMagazine) {
            NbtCompound nbt = itemStack.getOrCreateNbt();
            double currentFuel = nbt.getDouble(StaserBoltMagazine.FUEL_KEY);
            double maxFuel = StaserBoltMagazine.MAX_FUEL;

            if (currentFuel < maxFuel) {
                double newFuel = Math.min(currentFuel + 500, maxFuel);
                nbt.putDouble(StaserBoltMagazine.FUEL_KEY, newFuel);
                tardis.removeFuel(500);

                TardisDesktop.playSoundAtConsole(world, console, AITSounds.SLOT_IN, SoundCategory.PLAYERS, 6f, 1);
                return true;
            }
        }


        if (itemStack.getItem() instanceof WaypointItem) {
            if (WaypointItem.getPos(itemStack) == null)
                WaypointItem.setPos(itemStack, tardis.travel().position());

            tardis.waypoint().markHasCartridge();
            tardis.waypoint().set(Waypoint.fromStack(itemStack), console, true);
            player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

            TardisDesktop.playSoundAtConsole(world, console, AITSounds.SLOT_IN, SoundCategory.PLAYERS, 6f, 1);
            return true;
        }

        return false;
    }


    private void ejectDisc(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        if (tardis.extra().getInsertedDisc().isEmpty()) return;
        world.playSound(null, console, AITSounds.SLOT_IN, SoundCategory.PLAYERS, 6f, 1);
        StopSoundS2CPacket stopPacket = new StopSoundS2CPacket(null, SoundCategory.RECORDS);
        for (ServerPlayerEntity otherPlayer : world.getPlayers()) {
            otherPlayer.networkHandler.sendPacket(stopPacket);
        }
        player.giveItemStack(tardis.extra().getInsertedDisc());
        tardis.extra().setInsertedDisc(ItemStack.EMPTY);
        currentMusic = null;
    }
}
