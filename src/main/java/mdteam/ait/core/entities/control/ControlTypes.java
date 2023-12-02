package mdteam.ait.core.entities.control;

import net.minecraft.entity.EntityDimensions;
import org.joml.Vector3f;

public class ControlTypes {

    private String controlName;
    private EntityDimensions scale;
    private Vector3f offset;

    public ControlTypes(String controlName, EntityDimensions scaling, Vector3f offset) {
        this.controlName = controlName;
        this.scale = scaling;
        this.offset= offset;
    }

    public String getControlName() {
        return controlName;
    }

    public EntityDimensions getScale() {
        return scale;
    }

    public Vector3f getOffsetFromCenter() {
        return offset;
    }

    public void setControlName(String name) {
        this.controlName = name;
    }

    public void setScale(EntityDimensions dimensions) {
        this.scale = dimensions;
    }

    public void setOffset(Vector3f offset) {
        this.offset = offset;
    }

}
