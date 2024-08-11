package loqor.ait.tardis.control.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.data.DirectedGlobalPos;
import loqor.ait.core.util.WorldUtil;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.travel.TravelHandler;
import loqor.ait.tardis.util.AsyncLocatorUtil;

public class DimensionControl extends Control {

    public DimensionControl() {
        super("dimension");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
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

            int next = DimensionControl.cycle(dims, current, !player.isSneaking());
            return dims.get(next);
        }).thenAccept(destWorld -> {
            travel.forceDestination(cached -> cached.world(destWorld));
            messagePlayer(player, destWorld);
        });

        AsyncLocatorUtil.LOCATING_EXECUTOR_SERVICE.submit(() -> future);
        return true;
    }

    private void messagePlayer(ServerPlayerEntity player, ServerWorld world) {
        Text message = Text.translatable("message.ait.tardis.control.dimension.info")
                .append(WorldUtil.worldText(world.getRegistryKey()));

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
