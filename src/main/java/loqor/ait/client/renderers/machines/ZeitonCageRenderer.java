package loqor.ait.client.renderers.machines;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import loqor.ait.AITMod;
import loqor.ait.client.models.machines.ZeitonCageModel;
import loqor.ait.core.blockentities.ZeitonCageBlockEntity;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class ZeitonCageRenderer<T extends ZeitonCageBlockEntity> implements BlockEntityRenderer<T> {

    public static final Identifier ZEITON_CAGE_TEXTURE = new Identifier(AITMod.MOD_ID,
            ("textures/blockentities/machines/zeiton_cage.png"));
    private final ZeitonCageModel zeitonCageModel;

    public ZeitonCageRenderer(BlockEntityRendererFactory.Context ctx) {
        this.zeitonCageModel = new ZeitonCageModel(ZeitonCageModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(ZeitonCageBlockEntity entity, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {

        matrices.push();
        matrices.translate(0.5f, 1.5f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

        this.zeitonCageModel.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(ZEITON_CAGE_TEXTURE)),
                light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrices.pop();
    }
}
