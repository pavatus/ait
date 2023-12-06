package mdteam.ait.core.entities.control.impl;

import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.entities.control.Control;
import mdteam.ait.core.helper.TardisUtil;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import mdteam.ait.tardis.Tardis;

import static mdteam.ait.tardis.TardisTravel.State.LANDED;

public class DoorControl extends Control {
    public DoorControl() {
        super("door");
    }

    @Override
    public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
        return true;
    }

    @Override
    public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
        return (!player.isSneaking()) ? useDoor(tardis, world, tardis.getDesktop().getConsolePos(), player) : toggleLock(tardis,world,player);
    }

    public static boolean useDoor(Tardis tardis, ServerWorld world, @Nullable BlockPos pos, @Nullable ServerPlayerEntity player) {
        if (tardis.getTravel().getState() != LANDED)
            return false;

        if (tardis.getLockedTardis()) {
            if (pos != null)
                world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_STEP, SoundCategory.BLOCKS, 0.6F, 1F);
            if (player != null)
                player.sendMessage(Text.literal("\uD83D\uDD12"), true);
            return false;
        }

        DoorBlockEntity door = TardisUtil.getDoor(tardis); // i need to remember these utils exist fr fr

        if (door == null)
            return false;

        // fixme this is loqors code so there might be a better way
        if(tardis.getExterior().getType().isDoubleDoor()) {
            if (door.getRightDoorRotation() == 1.2f && door.getLeftDoorRotation() == 1.2f) {
                door.setLeftDoorRot(0);
                door.setRightDoorRot(0);
            } else {
                door.setRightDoorRot(door.getLeftDoorRotation() == 0 ? 0 : 1.2f);
                door.setLeftDoorRot(1.2f);
            }
        } else
            door.setLeftDoorRot(door.getLeftDoorRotation() == 0 ? 1.2f : 0);

        world.playSound(null, door.getPos(), SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.6f, 1f);

        return true;
    }
    public static boolean toggleLock(Tardis tardis, ServerWorld world, @Nullable ServerPlayerEntity player) {
        return lockTardis(!tardis.getLockedTardis(),tardis,world,player);
    }
    public static boolean lockTardis(boolean locked, Tardis tardis, ServerWorld world, @Nullable ServerPlayerEntity player) {
        if (tardis.getTravel().getState() != LANDED) return false;

        tardis.setLockedTardis(locked);

        DoorBlockEntity door = TardisUtil.getDoor(tardis);

        if (door == null)
            return false; // could have a case where the door is null but the thing above works fine meaning this false is wrong fixme

        door.setLeftDoorRot(0);
        door.setRightDoorRot(0);

        String lockedState = tardis.getLockedTardis() ? "\uD83D\uDD12" : "\uD83D\uDD13";
        if (player != null)
            player.sendMessage(Text.literal(lockedState), true);

        world.playSound(null, door.getPos(), SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);
        return true;
    }
}