package mdteam.ait.client.renderers.doors;

import mdteam.ait.client.models.doors.DoorModel;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.client.renderers.exteriors.VariantEnum;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blocks.ExteriorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import mdteam.ait.tardis.TardisExterior;

public class DoorRenderer<T extends DoorBlockEntity> implements BlockEntityRenderer<T> {

    private DoorModel model;

    public DoorRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getTardis() == null)
            return;

        TardisExterior tardisExterior = entity.getTardis().getExterior();
        Class<? extends DoorModel> modelClass = tardisExterior.getType().getDoorClass();

        if (model != null && !(model.getClass().isInstance(modelClass)))
            model = null;

        if (model == null)
            this.model = entity.getTardis().getExterior().getType().createDoorModel();

        BlockState blockState = entity.getCachedState();
        float f = blockState.get(ExteriorBlock.FACING).asRotation();
        int maxLight = 0xFFFFFF;
        matrices.push();
        matrices.translate(0.5, 0, 0.5);
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        Identifier texture = model.getVariousTextures(entity.getTardis().getExterior().getType(), VariantEnum.DEFAULT);
        if(model != null) {
            model.renderWithAnimations(entity,this.model.getPart(),matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(texture)), light, overlay, 1, 1, 1, 1);
            if(model.getVariousEmission(texture, entity.getTardis().getExterior().getType()) != null) model.renderWithAnimations(entity,this.model.getPart(),matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(model.getVariousEmission(texture, entity.getTardis().getExterior().getType()), false)), maxLight, overlay, 1, 1, 1, 1);
        }
        matrices.pop();
    }
}
