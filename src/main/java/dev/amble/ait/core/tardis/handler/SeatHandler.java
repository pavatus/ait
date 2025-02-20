package dev.amble.ait.core.tardis.handler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.BlockPos;

import dev.amble.ait.core.blockentities.ExteriorBlockEntity;

public class SeatHandler {
    public static void init() {
        ServerEntityEvents.ENTITY_UNLOAD.register((entity, world) -> {
            if (entity instanceof ArmorStandEntity seat) {
                BlockPos blockPos = seat.getBlockPos();
                BlockEntity block = world.getBlockEntity(blockPos);

                if (block instanceof ExteriorBlockEntity exterior) {
                    seat.remove(Entity.RemovalReason.DISCARDED);
                    exterior.setSeatEntity(null);
                }
            }
        });
    }
}
