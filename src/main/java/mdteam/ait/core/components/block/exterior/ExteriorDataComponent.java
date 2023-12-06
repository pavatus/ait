package mdteam.ait.core.components.block.exterior;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface ExteriorDataComponent extends Component {
    float getLeftDoorRotation();

    float getRightDoorRotation();

    void setLeftDoorRotation(float newRot);

    void setRightDoorRotation(float newRot);
}
