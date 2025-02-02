package loqor.ait.core.tardis.control.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import dev.pavatus.lib.data.CachedDirectedGlobalPos;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITSounds;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.lock.LockedDimensionRegistry;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.core.tardis.util.AsyncLocatorUtil;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.data.schema.console.variant.renaissance.*;

public class DimensionControl extends Control {

    private SoundEvent soundEvent = AITSounds.DIMENSION;

    public DimensionControl() {
        super("dimension");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console, boolean leftClick) {
        if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
            this.addToControlSequence(tardis, player, console);
            return false;
        }

        TravelHandler travel = tardis.travel();
        CachedDirectedGlobalPos dest = travel.destination();


        if (world.getBlockEntity(console) instanceof ConsoleBlockEntity consoleBlockEntity) {
            if (isRenaissanceVariant(consoleBlockEntity)) {
                this.soundEvent = AITSounds.RENAISSANCE_DIMENSION_ALT;
            } else {
                this.soundEvent = AITSounds.DIMENSION;
            }
        }

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> {
            List<ServerWorld> dims = WorldUtil.getOpenWorlds();

            int current = WorldUtil
                    .worldIndex(dest.getWorld() == null ? world.getServer().getOverworld() : dest.getWorld());

            int next = DimensionControl.cycle(dims, current, !leftClick);
            return dims.get(next);
        }).thenAccept(destWorld -> {
            travel.forceDestination(cached -> cached.world(destWorld));

            messagePlayer(player, destWorld, LockedDimensionRegistry.getInstance().isUnlocked(tardis, destWorld));
        });

        AsyncLocatorUtil.LOCATING_EXECUTOR_SERVICE.submit(() -> future);
        return true;
    }

    private void messagePlayer(ServerPlayerEntity player, ServerWorld world, boolean unlocked) {
        MutableText message = Text.translatable("message.ait.tardis.control.dimension.info")
                .append(WorldUtil.worldText(world.getRegistryKey())).formatted(unlocked ? Formatting.WHITE : Formatting.GRAY);

        if (!(unlocked)) message.append(Text.literal(" \uD83D\uDD12"));

        player.sendMessage(message, true);
    }

    private static int cycle(List<ServerWorld> worlds, int current, boolean increase) {
        final int lastIndex = worlds.size() - 1;

        if (increase) {
            current += 1;
            return current > lastIndex ? 0 : current;
        }

        current -= 1;
        return current < 0 ? lastIndex : current;
    }

    @Override
    public SoundEvent getSound() {
        return this.soundEvent;
    }

    private boolean isRenaissanceVariant(ConsoleBlockEntity consoleBlockEntity) {
        return consoleBlockEntity.getVariant() instanceof RenaissanceTokamakVariant ||
                consoleBlockEntity.getVariant() instanceof RenaissanceVariant ||
                consoleBlockEntity.getVariant() instanceof RenaissanceIndustriousVariant ||
                consoleBlockEntity.getVariant() instanceof RenaissanceIdentityVariant ||
                consoleBlockEntity.getVariant() instanceof RenaissanceFireVariant;
    }
}
