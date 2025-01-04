package loqor.ait.client.renderers.machines;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

import loqor.ait.client.models.machines.GenericSubSystemModel;
import loqor.ait.client.renderers.MultiBlockStructureRenderer;
import loqor.ait.client.util.ClientLightUtil;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.generic.GenericStructureSystemBlockEntity;

public class GenericSubSystemRenderer<T extends GenericStructureSystemBlockEntity> implements BlockEntityRenderer<T>, ClientLightUtil.Renderable<T> {
    private GenericSubSystemModel model;

    public GenericSubSystemRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new GenericSubSystemModel();
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        // todo - no model yet :(
        SubSystem system = entity.system();
        if (entity.hasSystem() && system != null && !system.isUsable() && system instanceof StructureHolder holder) {
            MultiBlockStructureRenderer.instance().render(holder.getStructure(), entity.getPos(), entity.getWorld(), matrices, vertexConsumers, true);
        }

        matrices.translate(0.5f, -0.5f, 0.5f);
        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(GenericSubSystemModel.TEXTURE)),
                light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrices.pop();
    }

    @Override
    public void render(T entity, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
