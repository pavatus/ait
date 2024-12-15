package loqor.ait.client.renderers.machines;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import loqor.ait.AITMod;
import loqor.ait.client.models.machines.PowerConverterModel;
import loqor.ait.client.util.ClientLightUtil;
import loqor.ait.core.blocks.PowerConverterBlock;

public class PowerConverterRenderer<T extends PowerConverterBlock.BlockEntity> implements BlockEntityRenderer<T>, ClientLightUtil.Renderable<PowerConverterBlock.BlockEntity> {

    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/machines/power_converter.png"));;
    private final PowerConverterModel model;

    public PowerConverterRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new PowerConverterModel();
    }

    @Override
    public void render(PowerConverterBlock.BlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.scale(1.35f, 1.35f, 1.35f);
        matrices.translate(0.38, 1.5f, 0.38);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)),
                light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrices.pop();
    }

    @Override
    public void render(PowerConverterBlock.BlockEntity entity, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
