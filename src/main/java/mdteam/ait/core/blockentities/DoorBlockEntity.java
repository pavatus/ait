package mdteam.ait.core.blockentities;

import mdteam.ait.tardis.linkable.LinkableBlockEntity;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.blocks.types.HorizontalDirectionalBlock;
import mdteam.ait.core.util.TardisUtil;
import mdteam.ait.core.util.data.AbsoluteBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;

import static mdteam.ait.AITMod.INTERIORDOORNBT;

public class DoorBlockEntity extends LinkableBlockEntity {

    public DoorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void useOn(World world, boolean sneaking) {
        if (this.getLeftDoorRotation() == 0) {
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

    public void onEntityCollision(Entity entity) {
        if (!(entity instanceof ServerPlayerEntity player) || this.getWorld() != TardisUtil.getTardisDimension())
            return;

        if (this.getLeftDoorRotation() > 0 || this.getRightDoorRotation() > 0) {
            TardisUtil.teleportOutside(this.tardis, player);
        }
    }

    @Override
    public void setTardis(Tardis tardis) {
        super.setTardis(tardis);
        this.linkDesktop(); // force re-link a desktop if it's not null
    }

    @Override
    public void setDesktop(TardisDesktop desktop) {
        desktop.setInteriorDoorPos(new AbsoluteBlockPos.Directed(
                this.pos, TardisUtil.getTardisDimension(), this.getFacing())
        );
    }
}
