package mdteam.ait.core.components.block.radio;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;

import static mdteam.ait.AITMod.RADIONBT;

public class RadioNBTComponent implements RotationNBTComponent, AutoSyncedComponent {
    private double tuner;
    private double volume;
    private boolean isOn;

    private final BlockEntity blockEntity;

    public RadioNBTComponent(BlockEntity blockentity) {
        this.blockEntity = blockentity;
    }

    @Override
    public double getTuner() {
        return this.tuner;
    }

    @Override
    public double getVolume() {
        return this.volume;
    }

    @Override
    public boolean isOn() {
        return this.isOn;
    }

    @Override
    public void setTuner(double tuner) {
        this.tuner = tuner;
        RADIONBT.sync(this.blockEntity);
    }

    @Override
    public void setVolume(double volume) {
        this.volume = volume;
        RADIONBT.sync(this.blockEntity);
    }

    @Override
    public void turnOn(boolean isOn) {
        this.isOn = isOn;
        RADIONBT.sync(this.blockEntity);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains("tuner"))
            this.tuner = tag.getFloat("tuner");

        if (tag.contains("volume"))
            this.volume = tag.getFloat("volume");

        if (tag.contains("isOn"))
            this.isOn = tag.getBoolean("isOn");

        RADIONBT.sync(this.blockEntity);
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putDouble("tuner", this.tuner);
        tag.putDouble("volume", this.volume);
        tag.putBoolean("isOn", this.isOn);
    }
}
