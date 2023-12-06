package mdteam.ait.core;

import mdteam.ait.client.screens.MonitorScreen;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.BiFunction;

public class AITScreenHandlers implements ScreenHandlerContext {

    //fixme this is using owolib stuff im just not doing it right; screenhandlercontext should be an interface that we've made not a default one - Loqor

    //public static final ScreenHandlerType<MonitorScreen> MONITOR_HANDLER = new ScreenHandlerType<>(MonitorScreen::createDefault, ScreenHandlerType.GENERIC_9X3.getRequiredFeatures());

    @Override
    public <T> Optional<T> get(BiFunction<World, BlockPos, T> getter) {
        return Optional.empty();
    }
}
