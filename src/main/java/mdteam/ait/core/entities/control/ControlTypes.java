package mdteam.ait.core.entities.control;

import mdteam.ait.tardis.Tardis;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.joml.Vector3f;

public class ControlTypes {
    private Control control;
    private EntityDimensions scale;
    private Vector3f offset;

    public ControlTypes(Control control, EntityDimensions scaling, Vector3f offset) {
        this.control = control;
        this.scale = scaling;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "ControlTypes{" +
                "control=" + control +
                ", scale=" + scale +
                ", offset=" + offset +
                '}';
    }

    public Control control() {
        return this.control;
    }

    public void setControl(String id) {
        this.control = new Control(id) {
            @Override
            public boolean runClient(Tardis tardis, ClientPlayerEntity player, ClientWorld world) {
                return true;
            }

            @Override
            public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world) {
                return true;
            }
        };
    }

    public EntityDimensions getScale() {
        return this.scale;
    }

    public void setScale(EntityDimensions scale) {
        this.scale = scale;
    }

    public Vector3f getOffsetFromCenter() {
        return this.offset;
    }

    public void setOffset(Vector3f offset) {
        this.offset = offset;
    }

    //@TODO uhh, yeah we need to do this serialization; not being able to execute stuff on client is getting annoying :/ - Loqor

    public NbtCompound serializeTypes(NbtCompound nbt) {
        nbt.putString("id", this.control().id);
        nbt.putFloat("height", this.getScale().height);
        nbt.putFloat("width", this.getScale().width);
        nbt.putFloat("x", this.getOffsetFromCenter().x());
        nbt.putFloat("y", this.getOffsetFromCenter().y());
        nbt.putFloat("z", this.getOffsetFromCenter().z());
        return nbt;
    }

    public ControlTypes deserializeTypes(NbtCompound nbt) {
        if(nbt.contains("id"))
            this.setControl(nbt.getString("identity"));
        if(nbt.contains("width") && nbt.contains("height"))
            this.setScale(EntityDimensions.changing(nbt.getFloat("width"), nbt.getFloat("height")));
        if(nbt.contains("x") && nbt.contains("y") && nbt.contains("z"))
            this.setOffset(new Vector3f(nbt.getFloat("x"), nbt.getFloat("y"), nbt.getFloat("z")));
        return new ControlTypes(this.control(), this.getScale(), this.getOffsetFromCenter());
    }

}
