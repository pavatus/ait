package dev.amble.ait.core.tardis.control.impl;

import java.util.Random;

import dev.drtheo.scheduler.api.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;

import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import dev.amble.ait.core.AITSounds;
import dev.amble.ait.core.engine.SubSystem;
import dev.amble.ait.core.tardis.Tardis;
import dev.amble.ait.core.tardis.control.Control;
import dev.amble.ait.core.tardis.handler.travel.TravelHandlerBase;

public class EngineOverload extends Control {

    private static final Random RANDOM = new Random();
    private static final String[] SPINNER = {"/", "-", "\\", "|"};

    public EngineOverload() {
        super("engine_overload");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
        boolean isInFlight = tardis.travel().getState() == TravelHandlerBase.State.FLIGHT;

        if (!isInFlight) {
            tardis.travel().forceDemat();
            tardis.travel().finishDemat();
        }

        if (tardis.fuel().getCurrentFuel() < 25000) {
            world.playSound(null, player.getBlockPos(), AITSounds.CLOISTER, SoundCategory.BLOCKS, 1.0F, 1.0F);
            player.sendMessage(Text.literal("§cERROR, TARDIS REQUIRES 25000 FUEL TO EXECUTE THIS ACTION."), true);
            return false;
        }


        runDumpingArtronSequence(player, () -> {
            world.getServer().execute(() -> {
                tardis.removeFuel(25000);
                tardis.travel().decreaseFlightTime(99999999);

                if (!isInFlight) {
                    tardis.travel().forceRemat();
                    tardis.travel().finishRemat();
                } else {
                    tardis.travel().forceRemat();
                    tardis.travel().finishRemat();
                    tardis.travel().cancelDemat();
                    tardis.travel().handbrake(true);
                }

                tardis.alarm().enabled();
                tardis.subsystems().demat().removeDurability(500);
                tardis.subsystems().chameleon().removeDurability(75);
                tardis.subsystems().shields().removeDurability(325);
                tardis.subsystems().lifeSupport().removeDurability(100);
                tardis.subsystems().engine().removeDurability(750);

                spawnParticles(world, console);
            });
        });

        return true;
    }

    private void runDumpingArtronSequence(ServerPlayerEntity player, Runnable onFinish) {

        for (int i = 0; i < 3; i++) {
            int delay = i + 1;
            Scheduler.get().runTaskLater(() -> {
                String frame = SPINNER[delay % SPINNER.length];
                player.sendMessage(Text.literal("§6DUMPING ARTRON " + frame), true);
            }, TimeUnit.SECONDS, delay);
        }


        Scheduler.get().runTaskLater(() -> runFlashingFinalMessage(player, onFinish), TimeUnit.SECONDS, 3);
    }

    private void runFlashingFinalMessage(ServerPlayerEntity player, Runnable onFinish) {
        for (int i = 0; i < 6; i++) {
            int delay = i + 1;
            Scheduler.get().runTaskLater(() -> {
                String flashColor = (delay % 2 == 0) ? "§c" : "§f";
                player.sendMessage(Text.literal(flashColor + "ARTRON DUMPED, ENGINES OVERLOADED, TRIGGERING EMERGENCY ARTRON RELEASE"), true);
            }, TimeUnit.SECONDS, delay);
        }


        Scheduler.get().runTaskLater(onFinish, TimeUnit.SECONDS, 3);
    }

    private void spawnParticles(ServerWorld world, BlockPos console) {
        for (int i = 0; i < 50; i++) {
            double offsetX = (RANDOM.nextDouble() - 0.5) * 2.0;
            double offsetY = RANDOM.nextDouble() * 1.5;
            double offsetZ = (RANDOM.nextDouble() - 0.5) * 2.0;

            world.spawnParticles(ParticleTypes.LAVA, console.getX() + 0.5 + offsetX, console.getY() + 1.5 + offsetY, console.getZ() + 0.5 + offsetZ, 2, 0, 0.05, 0, 0.1);
            world.spawnParticles(ParticleTypes.SMALL_FLAME, console.getX() + 0.5 + offsetX, console.getY() + 1.5 + offsetY, console.getZ() + 0.5 + offsetZ, 2, 0, 0.05, 0, 0.1);
            world.spawnParticles(ParticleTypes.CLOUD, console.getX() + 0.5 + offsetX, console.getY() + 1.5 + offsetY, console.getZ() + 0.5 + offsetZ, 2, 0, 0.05, 0, 0.1);
            world.spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, console.getX() + 0.5 + offsetX, console.getY() + 1.5 + offsetY, console.getZ() + 0.5 + offsetZ, 2, 0, 0.05, 0, 0.1);
            world.spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, console.getX() + 0.5 + offsetX, console.getY() + 1.5 + offsetY, console.getZ() + 0.5 + offsetZ, 2, 0, 0.05, 0, 0.1);
            world.spawnParticles(ParticleTypes.SMOKE, console.getX() + 0.5 + offsetX, console.getY() + 1.5 + offsetY, console.getZ() + 0.5 + offsetZ, 2, 0, 0.05, 0, 0.1);
            world.spawnParticles(ParticleTypes.FLAME, console.getX() + 0.5 + offsetX, console.getY() + 1.2 + offsetY, console.getZ() + 0.5 + offsetZ, 2, 0, 0.05, 0, 0.1);
            world.spawnParticles(ParticleTypes.SMOKE, console.getX() + 0.5 + offsetX, console.getY() + 1.5 + offsetY, console.getZ() + 0.5 + offsetZ, 2, 0, 0.05, 0, 0.1);
            world.spawnParticles(ParticleTypes.EXPLOSION, console.getX() + 0.5 + offsetX, console.getY() + 1.5 + offsetY, console.getZ() + 0.5 + offsetZ, 2, 0, 0.05, 0, 0.1);
        }
    }

    @Override
    protected SubSystem.IdLike requiredSubSystem() {
        return SubSystem.Id.ENGINE;
    }

    @Override
    public SoundEvent getSound() {
        return AITSounds.DING;
    }
}
