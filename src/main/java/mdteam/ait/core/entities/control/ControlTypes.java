package mdteam.ait.core.entities.control;

import net.minecraft.entity.EntityDimensions;
import org.joml.Vector3f;

public class ControlTypes {

    private final Control control;
    private final EntityDimensions scale;
    private final Vector3f offset;

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
        return control;
    }

    public EntityDimensions getScale() {
        return scale;
    }

    public Vector3f getOffsetFromCenter() {
        return offset;
    }

//    public void setControlName(String name) {
//        this.controlName = name;
//    }

//    public void setScale(EntityDimensions dimensions) {
//        this.scale = dimensions;
//    }

//    public void setOffset(Vector3f offset) {
//        this.offset = offset;
//    }

}
