package mdteam.ait.client.models.consoles;

import mdteam.ait.core.entities.BaseControlEntity;
import mdteam.ait.core.entities.ConsoleControlEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

@Environment(value= EnvType.CLIENT)
public class ControlModel extends SinglePartEntityModel<ConsoleControlEntity> {
    private final ModelPart root;

    public ControlModel(ModelPart root) {
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(ConsoleControlEntity controlEntity, float f, float g, float h, float i, float j) {

    }
}
