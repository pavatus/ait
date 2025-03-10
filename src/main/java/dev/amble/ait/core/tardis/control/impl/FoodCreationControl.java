package dev.amble.ait.core.tardis.control.impl;

import dev.drtheo.scheduler.api.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import dev.amble.ait.AITMod;
import dev.amble.ait.core.AITSounds;
import dev.amble.ait.core.tardis.Tardis;
import dev.amble.ait.core.tardis.control.Control;

public class FoodCreationControl extends Control {

    public FoodCreationControl() {
        super(AITMod.id("food_creation"));
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console,
                             boolean leftClick) {
        if (tardis.fuel().getCurrentFuel() < 500)
            return false;

        world.playSound(null, console, AITSounds.COFFEE_MACHINE, SoundCategory.BLOCKS, 1.0f, 1.0f);

        Scheduler.get().runTaskLater(() -> {
            if (world.getBlockState(console).isAir())
                return;

            ItemStack coffeeItem = tardis.extra().getRefreshmentItem();

            Vec3d spawnPosition = Vec3d.ofCenter(console).add(0, 1.5, 1);
            ItemEntity coffeeEntity = new ItemEntity(world, spawnPosition.x, spawnPosition.y, spawnPosition.z, coffeeItem);

            tardis.removeFuel(500);
            world.spawnEntity(coffeeEntity);
        }, TimeUnit.TICKS, 45);

        return true;
    }
}
