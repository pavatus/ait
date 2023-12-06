package mdteam.ait.client.screens.screen_handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class MonitorHandler extends ScreenHandler {
    protected MonitorHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }

    @Override
    public <R extends Record> void addServerboundMessage(Class<R> messageClass, Consumer<R> handler) {
        super.addServerboundMessage(messageClass, handler);
    }

    @Override
    public <R extends Record> void addClientboundMessage(Class<R> messageClass, Consumer<R> handler) {
        super.addClientboundMessage(messageClass, handler);
    }
}
