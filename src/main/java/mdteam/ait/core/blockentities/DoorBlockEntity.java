package mdteam.ait.core.blockentities;

import mdteam.ait.api.tardis.ILinkable;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import the.mdteam.ait.Tardis;
import the.mdteam.ait.TardisDesktop;
import the.mdteam.ait.TardisManager;

import static mdteam.ait.AITMod.INTERIORDOORNBT;

public class DoorBlockEntity extends BlockEntity implements ILinkable {

    private Tardis tardis;

    public DoorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);

        // even though TardisDesktop links the door, we need to link it here as well to avoid desync
        this.setTardis(TardisUtil.findTardisByInterior(pos));
    }

    public void useOn(World world, boolean sneaking) {
        if(this.getLeftDoorRotation() == 0) {
            this.setLeftDoorRot(1.2f);
        } else {
            this.setLeftDoorRot(0);
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
        if (this.tardis == null)
            return;

        if (!(entity instanceof ServerPlayerEntity player))
            return;

        if (this.getLeftDoorRotation() > 0 || this.getRightDoorRotation() > 0) {
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
