package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.control.Control;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;

import static mdteam.ait.tardis.handler.DoorHandler.toggleLock;
import static mdteam.ait.tardis.handler.DoorHandler.useDoor;

public class DoorControl extends Control {
    public DoorControl() {
        super("door");
    }

    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return true;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        return (!player.isSneaking()) ? useDoor(tardis, world, tardis.getDesktop().getConsolePos(), player) : toggleLock(tardis,world,player);
    }
}