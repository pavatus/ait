package loqor.ait.core.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class FakeBlockEvents {

    public static final Event<Check> CHECK = EventFactory.createArrayBacked(Check.class, callbacks -> (player, state, pos) -> {
        for (Check check : callbacks) {
            check.check(player, state, pos);
        }
    });

    @FunctionalInterface
    public interface Check {
        void check(ServerPlayerEntity player, BlockState state, BlockPos pos);
    }
}
