package loqor.ait.tardis.control.impl.waypoint;

import loqor.ait.core.item.WaypointItem;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.util.FlightUtil;
import loqor.ait.tardis.util.Waypoint;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class LoadWaypointControl extends Control {
	public LoadWaypointControl() {
		super("load_waypoint");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, boolean leftClick) {
		if (leftClick) {
			tardis.getHandlers().getWaypoints().spawnItem();
			return true;
		}

		ItemStack itemStack = player.getMainHandStack();
		if (!(itemStack.getItem() instanceof WaypointItem)) return false;

		if (WaypointItem.getPos(itemStack) == null) WaypointItem.setPos(itemStack, tardis.getTravel().getPosition());

		tardis.getHandlers().getWaypoints().markHasCartridge();
		tardis.getHandlers().getWaypoints().set(Waypoint.fromDirected(WaypointItem.getPos(itemStack)).setName(itemStack.getName().getString()), true);
		player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

		FlightUtil.playSoundAtConsole(tardis, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 6f, 1);
		return true;
	}
}
