package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.util.AbsoluteBlockPos;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;

public class DirectionControl extends Control {
    public DirectionControl() {
        super("direction");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        TardisTravel travel = tardis.getTravel();
        AbsoluteBlockPos.Directed dest = travel.getDestination();

        travel.setDestination(new AbsoluteBlockPos.Directed(dest,getNextDirection(dest.getDirection())), false);

        messagePlayer(player, travel.getDestination().getDirection());

        return true;
    }

    private void messagePlayer(ServerPlayerEntity player, Direction direction) {
        String arrow = "";
        if(direction == Direction.NORTH)
            arrow = "↑";
        else if(direction == Direction.EAST)
            arrow = "→";
        else if(direction == Direction.SOUTH)
            arrow = "↓";
        else if(direction == Direction.WEST)
            arrow = "←";
        player.sendMessage(Text.literal("Direction: " + direction.toString().substring(0, 1).toUpperCase() + direction.toString().substring(1) + " | " + arrow), true); // fixme translatable is preferred
    }
    private Direction getNextDirection(Direction current) {
        return switch (current) {
            case DOWN -> Direction.NORTH;
            case UP -> Direction.NORTH;
            case NORTH -> Direction.EAST;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
            case EAST -> Direction.SOUTH;
        };
    }
}
