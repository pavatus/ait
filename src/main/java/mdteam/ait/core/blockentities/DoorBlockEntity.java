package mdteam.ait.core.blockentities;

import mdteam.ait.api.tardis.ILinkable;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisDesktop;
import the.mdteam.ait.TardisManager;

import java.util.Objects;

import static mdteam.ait.AITMod.INTERIORDOORNBT;
import static the.mdteam.ait.TardisTravel.State.LANDED;

public class DoorBlockEntity extends BlockEntity implements ILinkable {

    private Tardis tardis;

    public DoorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);

        // even though TardisDesktop links the door, we need to link it here as well to avoid desync
        this.setTardis(TardisUtil.findTardisByInterior(pos));
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {

        if(player == null)
            return;

        if(player.getMainHandStack().getItem() == AITItems.GOLDEN_TARDIS_KEY) {
            ItemStack key = player.getMainHandStack();
            NbtCompound tag = key.getOrCreateNbt();
            if(!tag.contains("tardis")) {
                return;
            }
            if(Objects.equals(this.tardis.getUuid().toString(), tag.getUuid("tardis").toString())) {
                this.tardis.setLockedTardis(!this.tardis.getLockedTardis());
                this.setLeftDoorRot(0);
                this.setRightDoorRot(0);
                String lockedState = this.tardis.getLockedTardis() ? "\uD83D\uDD12" : "\uD83D\uDD13";
                player.sendMessage(Text.literal(lockedState), true);
                world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_BREAK, SoundCategory.BLOCKS, 0.6F, 1F);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.literal("TARDIS does not identify with key"), true);
            }
            return;
        }

        if(this.tardis.getTravel().getState() == LANDED) {
            if (!this.tardis.getLockedTardis()) {
                this.setLeftDoorRot(this.getLeftDoorRotation() == 0 ? 1.2f : 0);
                world.playSound(null, pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 0.6f, 1f);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_CHAIN_STEP, SoundCategory.BLOCKS, 0.6F, 1F);
                player.sendMessage(Text.literal("\uD83D\uDD12"), true);
            }
        }

        if (sneaking)
            return;

        world.playSound(null, pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS,0.6f, 1f);
        AbsoluteBlockPos exteriorPos = this.tardis.getTravel().getPosition();
        ExteriorBlockEntity exterior = TardisUtil.getExterior(this.tardis);

        if (exterior != null) {
            exteriorPos.getChunk();

            exterior.setLeftDoorRot(this.getLeftDoorRotation());
            exterior.setRightDoorRot(this.getRightDoorRotation());
        }
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
