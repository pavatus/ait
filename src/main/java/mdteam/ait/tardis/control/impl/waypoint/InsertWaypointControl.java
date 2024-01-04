package mdteam.ait.tardis.control.impl.waypoint;

import mdteam.ait.core.item.WaypointItem;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.util.Waypoint;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

import static mdteam.ait.core.item.WaypointItem.getPos;
import static mdteam.ait.core.item.WaypointItem.setPos;

public class InsertWaypointControl extends Control {
    public InsertWaypointControl() {
        super("insert_waypoint");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        ItemStack itemStack = player.getMainHandStack();
        if (!(itemStack.getItem() instanceof WaypointItem)) return false;

        if (getPos(itemStack) == null) setPos(itemStack, tardis.getTravel().getPosition());

        tardis.getHandlers().getWaypoints().markHasCartridge();
        tardis.getHandlers().getWaypoints().set(Waypoint.fromDirected(getPos(itemStack)), true);
        player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

        world.playSound(null, tardis.getDesktop().getConsolePos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 6f, 1);
        return true;
    }
}
