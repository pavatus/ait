package mdteam.ait.tardis.handler;

import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.tardis.ClientTardisManager;
import mdteam.ait.tardis.ServerTardisManager;
import mdteam.ait.tardis.Tardis;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import static mdteam.ait.tardis.TardisTravel.State.LANDED;

public class DoorHandler {
    private final UUID tardisId;
    private float left, right;
    private boolean locked;

    public DoorHandler(UUID tardis) {
        this.tardisId = tardis;
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
    }
    public void setLockedAndDoors(boolean var) {
        this.setLocked(var);

        this.setLeftRot(0);
        this.setRightRot(0);
    }
    public boolean locked() {
        return this.locked;
    }

    public void sync() {
        if (isClient()) return;

        ServerTardisManager.getInstance().sendToSubscribers(this.tardis());
    }

    public Tardis tardis() {
        if (isClient()) {
            return ClientTardisManager.getInstance().getLookup().get(tardisId);
        }

        return ServerTardisManager.getInstance().getTardis(tardisId);
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
        //ExteriorBlockEntity exterior = TardisUtil.getExterior(tardis);

        if (door == null)
            return false;

        // fixme this is loqors code so there might be a better way
        if (tardis.getExterior().getType().isDoubleDoor()) {
            if (door.getRightDoorRotation() == 1.2f && door.getLeftDoorRotation() == 1.2f) {
                door.setLeftDoorRot(0);
                door.setRightDoorRot(0);
            } else {
                door.setRightDoorRot(door.getLeftDoorRotation() == 0 ? 0 : 1.2f);
                door.setLeftDoorRot(1.2f);
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
            door.setLeftDoorRot(door.getLeftDoorRotation() == 0 ? 1.2f : 0);
            /*if (exterior != null) {
                exterior.setLeftDoorRot(door.getLeftDoorRotation() == 0 ? 1.2f : 0);
            }*/
        }

        world.playSound(null, door.getPos(), SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.6f, 1f);

        if(door != null)
            tardis.getDoor().sync();

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

        tardis.getDoor().sync();

        return true;
    }

    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
    public static boolean isServer() {
        return !isClient();
    }
}
