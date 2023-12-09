package mdteam.ait.core.blockentities;

import mdteam.ait.api.tardis.ILinkable;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.core.entities.control.impl.DoorControl;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.core.item.KeyItem;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.tardis.handler.DoorHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import mdteam.ait.tardis.TardisManager;

import java.util.Objects;

import static mdteam.ait.AITMod.INTERIORDOORNBT;

public class DoorBlockEntity extends BlockEntity implements ILinkable {

    private Tardis tardis;

    public DoorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);

        // even though TardisDesktop links the door, we need to link it here as well to avoid desync
        this.setTardis(TardisUtil.findTardisByInterior(pos));
        if(this.getTardis() != null)
            this.setDesktop(this.getDesktop());
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {

        if(player == null)
            return;

        if(player.getMainHandStack().getItem() instanceof KeyItem) {
            ItemStack key = player.getMainHandStack();
            NbtCompound tag = key.getOrCreateNbt();
            if(!tag.contains("tardis")) {
                return;
            }
            if(Objects.equals(this.tardis.getUuid().toString(), tag.getUuid("tardis").toString())) {
                DoorHandler.toggleLock(this.tardis, (ServerWorld) world, (ServerPlayerEntity) player);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.literal("TARDIS does not identify with key"), true);
            }
            return;
        }

        DoorHandler.useDoor(this.getTardis(), (ServerWorld) world, this.getPos(), (ServerPlayerEntity) player);

        if (sneaking)
            return;

        AbsoluteBlockPos exteriorPos = this.tardis.getTravel().getPosition();
        ExteriorBlockEntity exterior = TardisUtil.getExterior(this.tardis);

        if (exterior != null) {
            exteriorPos.getChunk();

            // exterior.setLeftDoorRot(this.getLeftDoorRotation());
            // exterior.setRightDoorRot(this.getRightDoorRotation());
        }
    }

    public void setLeftDoorRot(float rotation) {
        // INTERIORDOORNBT.get(this).setLeftDoorRotation(rotation);

        if (this.tardis == null) return;

        this.tardis.getDoor().setLeftRot(rotation);
    }

    public void setRightDoorRot(float rotation) {
        // INTERIORDOORNBT.get(this).setRightDoorRotation(rotation);

        if (this.tardis == null) return;

        this.tardis.getDoor().setRightRot(rotation);
    }

    public float getLeftDoorRotation() {
        // return INTERIORDOORNBT.get(this).getLeftDoorRotation();

        if (this.tardis == null) return 5;

        return this.tardis.getDoor().left();
    }

    public float getRightDoorRotation() {
        // return INTERIORDOORNBT.get(this).getRightDoorRotation();

        if (this.tardis == null) return 5;

        return this.tardis.getDoor().right();
    }

    public Direction getFacing() {
        return this.getCachedState().get(HorizontalDirectionalBlock.FACING);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if(this.tardis != null) {
            nbt.putUuid("tardis", this.tardis.getUuid());
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if(nbt.contains("tardis")) {
            TardisManager.getInstance().link(nbt.getUuid("tardis"), this);
        }
    }

    public void onEntityCollision(Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player) || this.getWorld() != TardisUtil.getTardisDimension())
            return;

        if (this.getTardis() != null && this.getLeftDoorRotation() > 0 || this.getRightDoorRotation() > 0) {
            if(!this.getTardis().getLockedTardis())
                TardisUtil.teleportOutside(this.tardis, player);
        }
    }

    @Override
    public Tardis getTardis() {
        return tardis;
    }

    @Override
    public void setTardis(Tardis tardis) {
        this.tardis = tardis;

        // force re-link a desktop if it's not null
        this.linkDesktop();
    }

    @Override
    public void setDesktop(TardisDesktop desktop) {
        desktop.setInteriorDoorPos(new AbsoluteBlockPos.Directed(
                this.pos, TardisUtil.getTardisDimension(), this.getFacing())
        );
    }
}