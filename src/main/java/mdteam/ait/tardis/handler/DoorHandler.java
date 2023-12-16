package mdteam.ait.tardis.handler;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static mdteam.ait.tardis.TardisTravel.State.LANDED;

public class DoorHandler extends TardisLink {
    private boolean locked, left, right;

    public DoorHandler(UUID tardis) {
        super(tardis);
    }

    public void setLeftRot(boolean var) {
        this.left = var;

        this.sync();
    }
    public void setRightRot(boolean var) {
        this.right = var;

        this.sync();
    }
    public boolean isRightOpen() {
        return this.right;
    }
    public boolean isLeftOpen() {
        return this.left;
    }

    public void setLocked(boolean var) {
        this.locked = var;

        this.sync();
    }
    public void setLockedAndDoors(boolean var) {
        this.setLocked(var);

        this.setLeftRot(false);
        this.setRightRot(false);
    }
    public boolean locked() {
        return this.locked;
    }

    public boolean isDoubleDoor() {
        return tardis().getExterior().getType().isDoubleDoor();
    }

    public boolean isOpen() {
        if (isDoubleDoor()) {
            return this.right || this.left;
        }

        return this.isLeftOpen();
    }
    public boolean isClosed() {
        return !isOpen();
    }
    public boolean isBothOpen() {
        return this.isRightOpen() && this.isLeftOpen();
    }
    public boolean isBothClosed() {
        return !isBothOpen();
    }
    public void openDoors() {
        setLeftRot(true);

        if (isDoubleDoor()) {
            setRightRot(true);
        }
    }
    public void closeDoors() {
        setLeftRot(false);
        setRightRot(false);
    }

    public static boolean useDoor(Tardis tardis, ServerWorld world, @Nullable BlockPos pos, @Nullable ServerPlayerEntity player) {
        if(isClient()) {
            return false;
        }
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
        // PLEASE FIXME ALL THIS CODE IS SO JANK I CANT
        if (tardis.getExterior().getType().isDoubleDoor()) {
            if (door.isBothOpen()) {
                world.playSound(null, door.getExteriorPos(), tardis.getExterior().getType().getDoorCloseSound(), SoundCategory.BLOCKS, 0.6F, 1F);
                world.playSound(null, door.getDoorPos(), tardis.getExterior().getType().getDoorCloseSound(), SoundCategory.BLOCKS, 0.6F, 1F);
                door.closeDoors();
            } else {
                world.playSound(null, door.getExteriorPos(), tardis.getExterior().getType().getDoorOpenSound(), SoundCategory.BLOCKS, 0.6F, 1F);
                world.playSound(null, door.getDoorPos(), tardis.getExterior().getType().getDoorOpenSound(), SoundCategory.BLOCKS, 0.6F, 1F);

                if (door.isLeftOpen() && player.isSneaking()) {
                    door.closeDoors();
                } else if (door.isBothClosed() && player.isSneaking()) {
                    door.openDoors();
                } else {
                    door.setRightRot(door.isLeftOpen());
                    door.setLeftRot(true);
                }
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
            world.playSound(null, door.getExteriorPos(), tardis.getExterior().getType().getDoorOpenSound(), SoundCategory.BLOCKS, 0.6F, 1F);
            world.playSound(null, door.getDoorPos(), tardis.getExterior().getType().getDoorOpenSound(), SoundCategory.BLOCKS, 0.6F, 1F);
            door.setLeftRot(!door.isLeftOpen());
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

        door.setLeftRot(false);
        door.setRightRot(false);

        if (!forced) {
            PropertiesHandler.set(tardis.getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED, locked);
        }

        String lockedState = tardis.getLockedTardis() ? "\uD83D\uDD12" : "\uD83D\uDD13";
        if (player != null)
            player.sendMessage(Text.literal(lockedState), true);

        world.playSound(null, door.getExteriorPos(), SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);
        world.playSound(null, door.getDoorPos(), SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);

        tardis.getDoor().sync();

        return true;
    }
}
