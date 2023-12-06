package mdteam.ait.client.renderers.exteriors;

import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import mdteam.ait.tardis.TardisExterior;

public class ExteriorRenderer<T extends ExteriorBlockEntity> implements BlockEntityRenderer<T> {
    private ExteriorModel model;
    public ExteriorRenderer(BlockEntityRendererFactory.Context ctx) {}
    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getTardis() == null)
            return;

        TardisExterior tardisExterior = entity.getTardis().getExterior();
        Class<? extends ExteriorModel> modelClass = tardisExterior.getType().getModelClass();

        if (model != null && !(model.getClass().isInstance(modelClass))) // fixme this is bad it seems to constantly create a new one anyway but i didnt realise.
            model = null;

        if (model == null)
            this.model = entity.getTardis().getExterior().getType().createModel();

        BlockState blockState = entity.getCachedState();
        float f = blockState.get(ExteriorBlock.FACING).asRotation();
        int maxLight = 0xFFFFFF;
       // float exteriorState = getMaterialStateForAlpha(entity.getMaterialState());
        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        if(model != null) {
            //@TODO use another instance of the model for alpha transparency to get rid of the weird z-fighting alpha stuff like on the fallout exterior (recommended by Bug1312) - Loqor
            model.renderWithAnimations(entity,this.model.getPart(),matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(model.getTexture())), light, overlay, 1, 1, 1, 1);
            model.renderWithAnimations(entity,this.model.getPart(),matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(model.getEmission(), false)), maxLight, overlay, 1, 1, 1, 1);
        }
        matrices.pop();
    }
}
