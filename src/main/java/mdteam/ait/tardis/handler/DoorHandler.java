package mdteam.ait.tardis.handler;

import mdteam.ait.core.AITSounds;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static mdteam.ait.tardis.TardisTravel.State.LANDED;

public class DoorHandler extends TardisLink {
    private boolean locked, left, right;

    public DoorHandler(UUID tardis) {
        super(tardis);
    }

    // Remember to markDirty for these setters!!
    public void setLeftRot(boolean var) {
        this.left = var;
    }

    public void setRightRot(boolean var) {
        this.right = var;
    }

    public boolean isRightOpen() {
        return this.right;
    }

    public boolean isLeftOpen() {
        return this.left;
    }

    public void setLocked(boolean var) {
        this.locked = var;

        markDirty();
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
        if (isClient()) {
            return false;
        }

        if (tardis.getHandlers().getOvergrownHandler().isOvergrown()) {
            // Bro cant escape
            if (player == null) return false;

            // if holding an axe then break off the vegetation
            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
            if (stack.getItem() instanceof AxeItem) {
                player.swingHand(Hand.MAIN_HAND);
                tardis.getHandlers().getOvergrownHandler().removeVegetation();
                stack.setDamage(stack.getDamage() - 1);

                if (pos != null)
                    world.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.BLOCKS, 1f, 1f);
                world.playSound(null, tardis.getDoor().getDoorPos(), SoundEvents.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, SoundCategory.BLOCKS);

                return false;
            }

            if (pos != null) // fixme will play sound twice on interior door
                world.playSound(null, pos, AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, 1f);

            world.playSound(null, tardis.getDoor().getDoorPos(), AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, 1f);

            return false;
        }

        if (tardis.getLockedTardis()) {
            //if (pos != null)
                //world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_STEP, SoundCategory.BLOCKS, 0.6F, 1F);
            if (player != null) {
                player.sendMessage(Text.literal("\uD83D\uDD12"), true);
                world.playSound(null, pos, AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, 1f);
                world.playSound(null, tardis.getDoor().getDoorPos(), AITSounds.KNOCK, SoundCategory.BLOCKS, 3f, 1f);
            }
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
        } else {
            world.playSound(null, door.getExteriorPos(), tardis.getExterior().getType().getDoorOpenSound(), SoundCategory.BLOCKS, 0.6F, 1F);
            world.playSound(null, door.getDoorPos(), tardis.getExterior().getType().getDoorOpenSound(), SoundCategory.BLOCKS, 0.6F, 1F);
            door.setLeftRot(!door.isLeftOpen());
        }

        tardis.getDoor().markDirty();

        return true;
    }

    public static boolean toggleLock(Tardis tardis, ServerWorld world, @Nullable ServerPlayerEntity player) {
        return lockTardis(!tardis.getLockedTardis(), tardis, world, player, false);
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
            PropertiesHandler.setBool(tardis.getHandlers().getProperties(), PropertiesHandler.PREVIOUSLY_LOCKED, locked);
        }

        String lockedState = tardis.getLockedTardis() ? "\uD83D\uDD12" : "\uD83D\uDD13";
        if (player != null)
            player.sendMessage(Text.literal(lockedState), true);

        world.playSound(null, door.getExteriorPos(), SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);
        world.playSound(null, door.getDoorPos(), SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);

        tardis.getDoor().markDirty();

        return true;
    }
}
