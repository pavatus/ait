package loqor.ait.client.renderers.machines;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.renderers.MultiBlockStructureRenderer;
import loqor.ait.client.util.ClientLightUtil;
import loqor.ait.core.engine.StructureHolder;
import loqor.ait.core.engine.SubSystem;
import loqor.ait.core.engine.block.generic.GenericStructureSystemBlockEntity;

public class GenericSubSystemRenderer<T extends GenericStructureSystemBlockEntity> implements BlockEntityRenderer<T>, ClientLightUtil.Renderable<T> {

    public static final Identifier ENGINE_TEXTURE = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/machines/engine.png"));
    public static final Identifier EMISSIVE_ENGINE_TEXTURE = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/machines/engine_emission.png"));

    public GenericSubSystemRenderer(BlockEntityRendererFactory.Context ctx) {
        // todo model
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        // todo - no model yet :(
        SubSystem system = entity.system();
        if (entity.hasSystem() && !system.isUsable() && system instanceof StructureHolder holder) {
            MultiBlockStructureRenderer.instance().render(holder.getStructure(), entity.getPos(), entity.getWorld(), null);
        }

        matrices.pop();
    }

    @Override
    public void render(T entity, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
