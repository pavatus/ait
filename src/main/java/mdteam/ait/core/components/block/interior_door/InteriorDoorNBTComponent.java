package mdteam.ait.core.components.block.interior_door;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

import static mdteam.ait.AITMod.INTERIORDOORNBT;
import static mdteam.ait.AITMod.INTERIORDOORNBT;

public class InteriorDoorNBTComponent implements InteriorDataComponent, AutoSyncedComponent {
    public BlockEntity blockEntity;
    public float leftDoorRotation, rightDoorRotation;

    public InteriorDoorNBTComponent(BlockEntity blockentity) {
        this.blockEntity = blockentity;
        this.leftDoorRotation = 0;
        this.rightDoorRotation = 0;
    }

    @Override
    public float getLeftDoorRotation() {
        return this.leftDoorRotation;
    }

    @Override
    public float getRightDoorRotation() {
        return this.rightDoorRotation;
    }

    @Override
    public void setLeftDoorRotation(float newRot) {
        this.leftDoorRotation = newRot;
        INTERIORDOORNBT.sync(this.blockEntity);
    }

    @Override
    public void setRightDoorRotation(float newRot) {
        this.rightDoorRotation = newRot;
        INTERIORDOORNBT.sync(this.blockEntity);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if(tag.contains("leftDoorRotation")) this.leftDoorRotation = tag.getFloat("leftDoorRotation"); INTERIORDOORNBT.sync(this.blockEntity);
        if(tag.contains("rightDoorRotation")) this.rightDoorRotation = tag.getFloat("rightDoorRotation"); INTERIORDOORNBT.sync(this.blockEntity);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("leftDoorRotation", this.leftDoorRotation);
        tag.putFloat("rightDoorRotation", this.rightDoorRotation);
    }
}
