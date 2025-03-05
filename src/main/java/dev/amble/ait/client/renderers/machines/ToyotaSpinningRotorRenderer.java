package dev.amble.ait.client.renderers.machines;


import net.minecraft.block.BlockState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import dev.amble.ait.AITMod;
import dev.amble.ait.client.models.machines.ToyotaSpinningRotorModel;
import dev.amble.ait.client.util.ClientLightUtil;
import dev.amble.ait.core.blockentities.ToyotaSpinningRotorBlockEntity;

public class ToyotaSpinningRotorRenderer<T extends ToyotaSpinningRotorBlockEntity> implements BlockEntityRenderer<T>, ClientLightUtil.Renderable<ToyotaSpinningRotorBlockEntity> {

    public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID,
            "textures/blockentities/machines/toyota_spinning_rotor.png");

    public static final Identifier EMISSIVE_TEXTURE = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/machines/toyota_spinning_rotor_emission.png"));

    private final ToyotaSpinningRotorModel model;

    public ToyotaSpinningRotorRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new ToyotaSpinningRotorModel(ToyotaSpinningRotorModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(ToyotaSpinningRotorBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState blockState = entity.getCachedState();



        matrices.push();
        matrices.scale(1f, 1f, 1f);
        matrices.translate(0.5f, 1.5f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));

        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)),
                light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        ClientLightUtil.renderEmissive(this, EMISSIVE_TEXTURE, entity, this.model.getPart(), matrices, vertexConsumers, light, overlay, 1, 1, 1, 1.0F);


        matrices.pop();

    }

    @Override
    public void render(ToyotaSpinningRotorBlockEntity entity, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
