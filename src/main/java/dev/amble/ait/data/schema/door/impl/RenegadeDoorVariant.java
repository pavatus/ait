package dev.amble.ait.data.schema.door.impl;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import dev.amble.ait.AITMod;
import dev.amble.ait.data.schema.door.DoorSchema;

public class RenegadeDoorVariant extends DoorSchema {

    public static final Identifier REFERENCE = AITMod.id("door/renegade");

    public RenegadeDoorVariant() {
        super(REFERENCE);
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public SoundEvent openSound() {
        return SoundEvents.BLOCK_GRINDSTONE_USE;
    }

    @Override
    public SoundEvent closeSound() {
        return SoundEvents.BLOCK_GRINDSTONE_USE;
    }

    @Override
    public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
        return switch (direction) {
            case DOWN, UP -> pos;
            case NORTH -> pos.add(0, 0.15, -0.4);
            case SOUTH -> pos.add(0, 0.15, 0.4);
            case WEST -> pos.add(-0.4, 0.15, 0);
            case EAST -> pos.add(0.4, 0.15, 0);
        };
    }
}
