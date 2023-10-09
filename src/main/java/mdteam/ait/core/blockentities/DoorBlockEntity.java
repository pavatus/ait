package mdteam.ait.core.blockentities;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.MaterialStateEnum;
import mdteam.ait.core.AITBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static mdteam.ait.AITMod.TARDISNBT;

public class DoorBlockEntity extends BlockEntity {
    public DoorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);
        setExterior(getExterior());
        setMaterialState(getMaterialState());
        setLeftDoorRot(getLeftDoorRotation());
        setRightDoorRot(getRightDoorRotation());
    }

    public static void tick(World world1, BlockPos pos, BlockState state1, DoorBlockEntity be) {

    }

    public void useOn(BlockHitResult hit, BlockState state, PlayerEntity player, World world, boolean sneaking) {
        if(getLeftDoorRotation() == 0) {
            setLeftDoorRot(1.2f);
        } else {
            setLeftDoorRot(0);
        }
        world.playSound(null,this.pos, SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS,0.6f, 1f);
    }

    public void setExterior(ExteriorEnum exterior) {
        TARDISNBT.get(this).setExterior(exterior);
    }

    public ExteriorEnum getExterior() {
        return TARDISNBT.get(this).getExterior();
    }

    public void setExteriorBlock(ExteriorBlockEntity exterior) {
        TARDISNBT.get(this).setExteriorBlock(exterior);
    }

    public ExteriorBlockEntity getExteriorBlock() {
        return TARDISNBT.get(this).getExteriorBlock();
    }

    public void setLeftDoorRot(float rotation) {
        TARDISNBT.get(this).setLeftDoorRotation(rotation);
    }

    public void setRightDoorRot(float rotation) {
        TARDISNBT.get(this).setRightDoorRotation(rotation);
    }

    public float getLeftDoorRotation() {
        return TARDISNBT.get(this).getLeftDoorRotation();
    }

    public float getRightDoorRotation() {
        return TARDISNBT.get(this).getRightDoorRotation();
    }

    public void setMaterialState(MaterialStateEnum materialState) {
        TARDISNBT.get(this).setMaterialState(materialState);
    }

    public MaterialStateEnum getMaterialState() {
        return TARDISNBT.get(this).getCurrentMaterialState();
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
    }
}
