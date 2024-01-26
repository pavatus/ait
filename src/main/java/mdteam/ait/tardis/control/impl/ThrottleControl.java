package mdteam.ait.tardis.control.impl;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.handler.DoorHandler;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisTravel;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class ThrottleControl extends Control {
    public ThrottleControl() {
        super("throttle");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, boolean leftClick) {

        if(tardis.isInDanger())
            return false;

        TardisTravel travel = tardis.getTravel();

        if (!leftClick) {
            if (player.isSneaking()) {
                travel.increaseSpeed();
            } else {
                travel.setSpeed(TardisTravel.MAX_SPEED);
            }
        } else {
            if (player.isSneaking()) {
                travel.decreaseSpeed();
            } else {
                travel.setSpeed(0);
            }
        }

        return true;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.DEMAT_LEVER_PULL;
    }

    @Override
    public boolean shouldFailOnNoPower() {
        return false;
    }
}
