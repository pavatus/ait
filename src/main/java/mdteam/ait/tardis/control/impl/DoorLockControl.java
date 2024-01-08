package mdteam.ait.tardis.control.impl;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import static mdteam.ait.tardis.data.DoorData.toggleLock;

public class DoorLockControl extends Control {
    public DoorLockControl() {
        super("door_lock");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        return toggleLock(tardis, player);
    }

    @Override
    public SoundEvent getSound() {
        return SoundEvents.BLOCK_LEVER_CLICK;
    }
}