package mdteam.ait.core.components.block.exterior;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

import static mdteam.ait.AITMod.EXTERIORNBT;
public class ExteriorNBTComponent implements ExteriorDataComponent, AutoSyncedComponent {
    public BlockEntity blockEntity;
    public float leftDoorRotation, rightDoorRotation;

    public ExteriorNBTComponent(BlockEntity blockentity) {
        if (!(blockentity instanceof ExteriorBlockEntity))
            return;

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
        EXTERIORNBT.sync(this.blockEntity);
    }

    @Override
    public void setRightDoorRotation(float newRot) {
        this.rightDoorRotation = newRot;
        EXTERIORNBT.sync(this.blockEntity);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if(tag.contains("leftDoorRotation")) this.leftDoorRotation = tag.getFloat("leftDoorRotation"); EXTERIORNBT.sync(this.blockEntity);
        if(tag.contains("rightDoorRotation")) this.rightDoorRotation = tag.getFloat("rightDoorRotation"); EXTERIORNBT.sync(this.blockEntity);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putFloat("leftDoorRotation", this.leftDoorRotation);
        tag.putFloat("rightDoorRotation", this.rightDoorRotation);
    }
}
