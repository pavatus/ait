package loqor.ait.core.tardis.control.impl;

import dev.drtheo.scheduler.api.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import loqor.ait.core.AITSounds;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.control.Control;

public class FoodCreationControl extends Control {

    public FoodCreationControl() {
        super("food_creation");
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console,
                             boolean leftClick) {
        if (player == null || world.isClient()) {
            return false;
        }
        world.playSound(null, console, AITSounds.COFFEE_MACHINE, SoundCategory.BLOCKS, 1.0f, 1.0f);


        Scheduler.get().runTaskLater(() -> {
            if (world.getBlockState(console).isAir()) {
                return;
            }

            ItemStack coffeeItem = new ItemStack((tardis.extra().getRefreshmentItem()).getItem());


            Vec3d spawnPosition = Vec3d.ofCenter(console).add(0, 1.5, 1);
            ItemEntity coffeeEntity = new ItemEntity(world, spawnPosition.x, spawnPosition.y, spawnPosition.z, coffeeItem);

            tardis.removeFuel(1500);
            world.spawnEntity(coffeeEntity);
        }, TimeUnit.TICKS, 180);

        return true;
    }
}
