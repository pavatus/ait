package loqor.ait.core.tardis.control;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.Tardis;

public class LightManipulatorControl extends Control {

    public LightManipulatorControl() {
        super("light_manipulator");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console, boolean leftClick) {
        MinecraftClient.getModStatus();
        return true;
    }

    @Override
    public boolean requiresPower() {
        return true;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.ALARM;
    }
}
