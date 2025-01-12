package loqor.ait.core.tardis.control.impl.waypoint;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITSounds;
import loqor.ait.core.item.WaypointItem;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.TardisDesktop;
import loqor.ait.core.tardis.control.Control;
import loqor.ait.data.Waypoint;

public class LoadWaypointControl extends Control {
    public LoadWaypointControl() {
        super("load_waypoint");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console,
            boolean leftClick) {
        if (leftClick) {
            tardis.waypoint().spawnItem(console);
            return true;
        }

        ItemStack itemStack = player.getMainHandStack();
        if (!(itemStack.getItem() instanceof WaypointItem))
            return false;

        if (WaypointItem.getPos(itemStack) == null)
            WaypointItem.setPos(itemStack, tardis.travel().position());

        tardis.waypoint().markHasCartridge();
        tardis.waypoint().set(Waypoint.fromStack(itemStack), console, true);
        player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

        TardisDesktop.playSoundAtConsole(tardis.asServer().getInteriorWorld(),console, AITSounds.SLOT_IN, SoundCategory.PLAYERS, 6f,
                1);
        return true;
    }
}
