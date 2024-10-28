package loqor.ait.core.tardis.control.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import loqor.ait.AITMod;
import loqor.ait.core.lock.LockedDimension;
import loqor.ait.core.lock.LockedDimensionRegistry;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.core.tardis.util.AsyncLocatorUtil;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.data.DirectedGlobalPos;

public class DimensionControl extends Control {

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
        DirectedGlobalPos.Cached dest = travel.destination();

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
        Text message = Text.translatable("message.ait.tardis.control.dimension.info")
                .append(WorldUtil.worldText(world.getRegistryKey())).formatted(unlocked ? Formatting.WHITE : Formatting.GRAY);

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
}
