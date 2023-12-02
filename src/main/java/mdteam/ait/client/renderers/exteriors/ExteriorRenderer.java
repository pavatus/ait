package mdteam.ait.client.renderers.exteriors;

import com.google.common.collect.ImmutableMap;
import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.models.exteriors.FalloutExteriorModel;
import mdteam.ait.client.models.exteriors.ToyotaExteriorModel;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import mdteam.ait.tardis.TardisExterior;

import java.util.Map;

public class ExteriorRenderer<T extends ExteriorBlockEntity> implements BlockEntityRenderer<T> {
    private final Map<ExteriorEnum, ModelPart> exteriormap;
    private ExteriorModel model;

    // FIXME theres gotta be a better way
    public Map<ExteriorEnum, ModelPart> getModels() {
        ImmutableMap.Builder<ExteriorEnum, ModelPart> builder = ImmutableMap.builder();
        builder.put(ExteriorEnum.SHELTER, FalloutExteriorModel.getTexturedModelData().createModel());
        builder.put(ExteriorEnum.TOYOTA, ToyotaExteriorModel.getTexturedModelData().createModel());
        return builder.build();
    }
    public ExteriorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.exteriormap = this.getModels();
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getTardis() == null)
            return;

        TardisExterior tardisExterior = entity.getTardis().getExterior();
        Class<? extends ExteriorModel> modelClass = tardisExterior.getType().getModelClass();

        if (model != null && !(model.getClass().isInstance(modelClass)))
            model = null;

        if (model == null)
            this.model = entity.getTardis().getExterior().getType().createModel();

        BlockState blockState = entity.getCachedState();
        float f = blockState.get(ExteriorBlock.FACING).asRotation();
        int maxLight = 0x0;//0xF000F0;
       // float exteriorState = getMaterialStateForAlpha(entity.getMaterialState());
        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        if(model != null) {
            model.renderWithAnimations(entity,this.model.getPart(),matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(model.getTexture())), light, overlay, 1, 1, 1, 1);
            model.renderWithAnimations(entity,this.model.getPart(),matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(model.getEmission())), light, overlay, 1, 1, 1, 1);
        }
        matrices.pop();
    }

    public float getMaterialStateForAlpha(MaterialStateEnum materialState) {
        switch(materialState) {
            case DEMAT:
                return -0.125F;
            case IN_VORTEX:
                return 0;
            case REMAT:
                return 0.125F;
            default:
                return 1;
        }
    }
}
