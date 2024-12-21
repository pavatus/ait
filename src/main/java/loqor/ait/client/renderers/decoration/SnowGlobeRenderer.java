package loqor.ait.client.renderers.decoration;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import loqor.ait.core.blockentities.SnowGlobeBlockEntity;
import loqor.ait.core.blocks.SnowGlobeBlock;
import loqor.ait.data.schema.exterior.ClientExteriorVariantSchema;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;

public class SnowGlobeRenderer<T extends SnowGlobeBlockEntity> implements BlockEntityRenderer<T> {
    public SnowGlobeRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(SnowGlobeBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5, 1.5f, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
        float k = entity.getCachedState().get(SnowGlobeBlock.FACING).asRotation();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(k));
        matrices.scale(0.25f, 0.25f, 0.25f);
        ClientExteriorVariantSchema schema = ClientExteriorVariantRegistry.BOX_DEFAULT;
        schema.model().render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(schema.texture())), 0xf000f0, overlay, 1, 1, 1, 1);
        matrices.pop();
    }
}
