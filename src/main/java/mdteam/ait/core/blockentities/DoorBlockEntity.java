package mdteam.ait.core.blockentities;

import io.wispforest.owo.ops.WorldOps;
import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.MaterialStateEnum;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.blocks.DoorBlock;
import mdteam.ait.core.helper.AbsoluteBlockPos;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.core.helper.TeleportHelper;
import mdteam.ait.core.helper.desktop.TARDISDesktop;
import mdteam.ait.core.tardis.Tardis;
import mdteam.ait.core.tardis.TardisHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Boxes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.swing.text.html.BlockView;
import java.awt.*;
import java.util.UUID;

import static mdteam.ait.AITMod.EXTERIORNBT;
import static mdteam.ait.AITMod.INTERIORDOORNBT;
import static mdteam.ait.core.helper.desktop.TARDISDesktop.offsetDoorPosition;
import static mdteam.ait.core.helper.desktop.TARDISDesktop.teleportToExterior;

public class DoorBlockEntity extends BlockEntity {

    private Tardis tardis;

    public DoorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);
        setTardis(getTardis());
        getTardis().getDesktop().setInteriorDoorPosition(getPos());
        setLeftDoorRot(getLeftDoorRotation());
        setRightDoorRot(getRightDoorRotation());
    }

    public static void tick(World world1, BlockPos pos, BlockState state1, ExteriorBlockEntity be) {

    }

    public void useOn(BlockHitResult hit, BlockState state, PlayerEntity player, World world, boolean sneaking) {
        if(getLeftDoorRotation() == 0) {
            setLeftDoorRot(1.2f);
        } else {
            setLeftDoorRot(0);
        }
        world.playSound(null, pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS,0.6f, 1f);
        if(!sneaking) {
            if (getTardis().getPosition().getDimension().getBlockEntity(getTardis().getPosition().toBlockPos()) instanceof ExteriorBlockEntity exteriorBlockEntity) {
                getTardis().getPosition().getDimension().getChunk(exteriorBlockEntity.getPos());
                exteriorBlockEntity.setLeftDoorRot(getLeftDoorRotation());
                exteriorBlockEntity.setRightDoorRot(getRightDoorRotation());
            }
        }
    }

    public void setTardis(Tardis tardis) {
        this.tardis = tardis;
    }

    public Tardis getTardis() {
        if(tardis == null)
            return TardisHandler.getTardisByInteriorPos(pos);
        else return tardis;
    }

    public TARDISDesktop getDesktop() {
        return this.getTardis().getDesktop();
    }

    public void setLeftDoorRot(float rotation) {
        INTERIORDOORNBT.get(this).setLeftDoorRotation(rotation);
    }

    public void setRightDoorRot(float rotation) {
        INTERIORDOORNBT.get(this).setRightDoorRotation(rotation);
    }

    public float getLeftDoorRotation() {
        return INTERIORDOORNBT.get(this).getLeftDoorRotation();
    }

    public float getRightDoorRotation() {
        return INTERIORDOORNBT.get(this).getRightDoorRotation();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if(tardis != null) {
            nbt.putUuid("tardis", tardis.getUuid());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        getTardis().getDesktop().setInteriorDoorPosition(getPos());
        if(nbt.contains("tardis")) {
            this.tardis = TardisHandler.getTardis(nbt.getUuid("tardis"));
        }
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (world.isClient()) return;
        if (getTardis() == null) return;
        ServerPlayerEntity player = AITMod.mcServer.getPlayerManager().getPlayer(entity.getUuid());
        ServerWorld newServerWorld = AITMod.mcServer.getWorld(getTardis().getPosition().getDimension().getRegistryKey());
        if (newServerWorld != null) {
            if (player != null)
                if (getLeftDoorRotation() > 0 || getRightDoorRotation() > 0) {
                    player.teleport(newServerWorld, offsetDoorPosition(getTardis().getPosition().toBlockPos(), newServerWorld).getX(),
                            offsetDoorPosition(getTardis().getPosition().toBlockPos(), newServerWorld).getY(),
                            offsetDoorPosition(getTardis().getPosition().toBlockPos(), newServerWorld).getZ(),
                            getTardis().getPosition().getDirection().asRotation(), player.getPitch());
                    //WorldOps.teleportToWorld(player, newServerWorld,
                    //        offsetDoorPosition(getTardis().getPosition().toBlockPos(), newServerWorld).toCenterPos(),
                    //        getTardis().getPosition().getDirection().asRotation(), player.getPitch());
                }
        }
    }
}
