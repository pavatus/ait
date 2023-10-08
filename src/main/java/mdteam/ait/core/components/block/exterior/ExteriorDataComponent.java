package mdteam.ait.core.components.block.exterior;

import dev.onyxstudios.cca.api.v3.component.Component;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.client.renderers.exteriors.MaterialStateEnum;

import java.util.UUID;

public interface ExteriorDataComponent extends Component {

    ExteriorEnum getExterior();

    void setExterior(ExteriorEnum exterior);

    float getLeftDoorRotation();

    float getRightDoorRotation();

    void setLeftDoorRotation(float newRot);

    void setRightDoorRotation(float newRot);

    MaterialStateEnum getCurrentMaterialState();

    void setMaterialState(MaterialStateEnum newMaterialState);

}
