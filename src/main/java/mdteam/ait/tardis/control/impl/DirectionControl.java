package mdteam.ait.tardis.control.impl;

import mdteam.ait.core.util.SpecificDirection;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;

public class DirectionControl extends Control {
	public DirectionControl() {
		super("direction");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
		TardisTravel travel = tardis.getTravel();
		AbsoluteBlockPos.Directed dest = travel.getDestination();

		if (tardis.getHandlers().getSequenceHandler().hasActiveSequence()) {
			if (tardis.getHandlers().getSequenceHandler().controlPartOfSequence(this)) {
				this.addToControlSequence(tardis);
				return false;
			}
		}

		travel.setDestination(new AbsoluteBlockPos.Directed(dest, getNextDirection(dest.getSpecific())), false);

		messagePlayer(player, travel.getDestination().getSpecific());

		return true;
	}

	private void messagePlayer(ServerPlayerEntity player, SpecificDirection direction) {
		String arrow = "";
		// if (direction == Direction.NORTH)
		// 	arrow = "↑";
		// else if (direction == Direction.EAST)
		// 	arrow = "→";
		// else if (direction == Direction.SOUTH)
		// 	arrow = "↓";
		// else if (direction == Direction.WEST)
		// 	arrow = "←";
		player.sendMessage(Text.literal("Direction: " + direction.asName() + " | " + arrow), true); // fixme translatable is preferred
	}

	private SpecificDirection getNextDirection(SpecificDirection current) {
		return SpecificDirection.fromRotation(current.toRotation() + 45);
	}
}
