package mdteam.ait.tardis.control.impl;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import mdteam.ait.tardis.data.CloakData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class CloakControl extends Control {

    public CloakControl() {
        super("protocol_3");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {

        CloakData cloak = tardis.getHandlers().getCloak();

        cloak.toggle();

        if (cloak.isEnabled()) {
            world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_SCULK_SENSOR_CLICKING, SoundCategory.BLOCKS, 1.0F, 1.0F);
        } else {
            world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_SCULK_SENSOR_CLICKING_STOP, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

        return true;
    }

    @Override
    public SoundEvent getSound() {
        return SoundEvents.INTENTIONALLY_EMPTY;
    }
}