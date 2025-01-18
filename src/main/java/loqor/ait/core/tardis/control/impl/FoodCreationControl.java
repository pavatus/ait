package loqor.ait.core.tardis.control.impl;

import dev.drtheo.scheduler.api.Scheduler;
import dev.drtheo.scheduler.api.TimeUnit;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;

import loqor.ait.core.AITItems;
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
            if (player.isAlive() && player.getServerWorld().equals(world)) {
                ItemStack coffeeItem = new ItemStack(AITItems.COFFEE);
                tardis.removeFuel(1500);
                if (!player.getInventory().insertStack(coffeeItem)) {
                    player.dropItem(coffeeItem, false);
                }
            }
        }, TimeUnit.TICKS, 180);

        return true;
    }
}
