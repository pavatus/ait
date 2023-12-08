package mdteam.ait.core.entities.control.impl;

import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.entities.control.Control;
import mdteam.ait.core.helper.TardisUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import mdteam.ait.tardis.Tardis;

import static mdteam.ait.tardis.TardisTravel.State.LANDED;
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