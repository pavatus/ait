package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import static mdteam.ait.tardis.handler.DoorHandler.toggleLock;
import static mdteam.ait.tardis.handler.DoorHandler.useDoor;

public class FastReturnControl extends Control {
    public FastReturnControl() {
        super("fast_return");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

        TardisTravel travel = tardis.getTravel();

        boolean bl = travel.getDestination() == travel.getLastPosition();

        if(travel.getLastPosition() != null) {
            travel.setDestination(bl ? travel.getPosition() : travel.getLastPosition(),
                    PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.AUTO_LAND));
            messagePlayer(player, bl);
            tardis.markDirty();
        } else {
            player.sendMessage(Text.literal("Fast Return: Last Position Nonexistent!"), true);
        }

        return true;
    }

    public void messagePlayer(ServerPlayerEntity player, boolean isLastPosition) {
        // fixme translatable
        player.sendMessage(Text.literal("Fast Return: " + (!isLastPosition ? "LAST POSITION SET" : "CURRENT POSITION SET")), true);
    }
}