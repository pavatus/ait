package mdteam.ait.tardis.handler;

import mdteam.ait.tardis.Tardis;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static mdteam.ait.tardis.TardisTravel.State.LANDED;

public class DoorHandler extends TardisHandler {
    private float left, right;
    private boolean locked;

    public DoorHandler(UUID tardis) {
        super(tardis);
    }

    public void setLeftRot(float var) {
        this.left = var;

        sync();
    }
    public void setRightRot(float var) {
        this.right = var;

        sync();
    }
    public float right() {
        return this.right;
    }
    public float left() {
        return this.left;
    }

    public void setLocked(boolean var) {
        this.locked = var;

        sync();
    }
    public void setLockedAndDoors(boolean var) {
        this.setLocked(var);

        this.setLeftRot(0);
        this.setRightRot(0);
    }
    public boolean locked() {
        return this.locked;
    }

    public boolean isDoubleDoor() {
        return tardis().getExterior().getType().isDoubleDoor();
    }

    public boolean isRightOpen() {
        return this.right() == 1.2f;
    }
    public boolean isLeftOpen() {
        return this.left() == 1.2f;
    }

    public boolean isOpen() {
        if (isDoubleDoor()) {
            return this.isRightOpen() || this.isLeftOpen();
        }

        return this.left() == 1.2f;
    }
    public boolean isClosed() {
        return !isOpen();
    }
    public boolean isBothOpen() {
        return this.isRightOpen() && this.isLeftOpen();
    }
    public void openDoors() {
        setLeftRot(1.2f);

        if (isDoubleDoor()) {
            setRightRot(1.2f);
        }
    }
    public void closeDoors() {
        setLeftRot(0);
        setRightRot(0);
    }

    public static boolean useDoor(Tardis tardis, ServerWorld world, @Nullable BlockPos pos, @Nullable ServerPlayerEntity player) {
        if (tardis.getLockedTardis()) {
            if (pos != null)
                world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_STEP, SoundCategory.BLOCKS, 0.6F, 1F);
            if (player != null)
                player.sendMessage(Text.literal("\uD83D\uDD12"), true);
            return false;
        }

        if (tardis.getTravel().getState() != LANDED)
            return false;

        DoorHandler door = tardis.getDoor();

        if (door == null) return false; // how would that happen anyway

        // fixme this is loqors code so there might be a better way
        if (tardis.getExterior().getType().isDoubleDoor()) {
            if (door.isBothOpen()) {
                world.playSound(null, door.getExteriorPos(), SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundCategory.BLOCKS, 0.6F, 1F);
                world.playSound(null, door.getDoorPos(), SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundCategory.BLOCKS, 0.6F, 1F);
                door.closeDoors();
            } else {
                world.playSound(null, door.getExteriorPos(), SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.6F, 1F);
                world.playSound(null, door.getDoorPos(), SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.6F, 1F);
                door.setRightRot(door.left() == 0 ? 0 : 1.2f);
                door.setLeftRot(1.2f);
            }
            /*if(exterior != null)
                if (exterior.getRightDoorRotation() == 1.2f && exterior.getLeftDoorRotation() == 1.2f) {
                    exterior.setLeftDoorRot(0);
                    exterior.setRightDoorRot(0);
                } else {
                    exterior.setRightDoorRot(door.getLeftDoorRotation() == 0 ? 0 : 1.2f);
                    exterior.setLeftDoorRot(1.2f);
                }*/
        } else {
            world.playSound(null, door.getExteriorPos(), SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.6F, 1F);
            world.playSound(null, door.getDoorPos(), SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.6F, 1F);
            door.setLeftRot(door.left() == 0 ? 1.2f : 0);
            /*if (exterior != null) {
                exterior.setLeftDoorRot(door.getLeftDoorRotation() == 0 ? 1.2f : 0);
            }*/
        }

        tardis.getDoor().sync();

        return true;
    }
    public static boolean toggleLock(Tardis tardis, ServerWorld world, @Nullable ServerPlayerEntity player) {
        return lockTardis(!tardis.getLockedTardis(),tardis,world,player, false);
    }
    public static boolean lockTardis(boolean locked, Tardis tardis, ServerWorld world, @Nullable ServerPlayerEntity player, boolean forced) {
        if (!forced) {
            if (tardis.getTravel().getState() != LANDED) return false;
        }
        tardis.setLockedTardis(locked);

        DoorHandler door = tardis.getDoor();

        if (door == null)
            return false; // could have a case where the door is null but the thing above works fine meaning this false is wrong fixme

        door.setLeftRot(0);
        door.setRightRot(0);

        String lockedState = tardis.getLockedTardis() ? "\uD83D\uDD12" : "\uD83D\uDD13";
        if (player != null)
            player.sendMessage(Text.literal(lockedState), true);

        world.playSound(null, door.getExteriorPos(), SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);
        world.playSound(null, door.getDoorPos(), SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);

        tardis.getDoor().sync();

        return true;
    }
}
