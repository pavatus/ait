package mdteam.ait.tardis.control;

import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.control.Control;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityDimensions;
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

    public Control getControl() {
        return this.control;
    }

    public void setControl(String id) {
        this.control = new Control(id) {
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

    public Vector3f getOffset() {
        return this.offset;
    }

    public void setOffset(Vector3f offset) {
        this.offset = offset;
    }

}
