package mdteam.ait.client.renderers;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.DisplayConsole;
import mdteam.ait.core.blockentities.DisplayConsoleBlockEntity;
import mdteam.ait.core.blocks.DisplayConsoleBlock;
import mdteam.ait.core.blocks.RadioBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class DisplayConsoleRenderer <T extends DisplayConsoleBlockEntity> implements BlockEntityRenderer<T> {

    public ModelPart displayConsole;
    public static final Identifier CONSOLE_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/display_console.png"));
    public static final Identifier CONSOLE_TEXTURE_EMISSION = new Identifier(AITMod.MOD_ID, ("textures/blockentities/display_console_emission.png"));

    public DisplayConsoleRenderer(BlockEntityRendererFactory.Context ctx) {
        this.displayConsole = DisplayConsole.getTexturedModelData().createModel();
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        BlockState blockState = entity.getCachedState();
        float f = blockState.get(DisplayConsoleBlock.FACING).asRotation();
        int maxLight = 0xF000F0;
        matrices.push();

        matrices.translate(0.5, 1.35, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.scale(0.9f, 0.9f, 0.9f);
        this.displayConsole.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(CONSOLE_TEXTURE)), light, overlay, 1, 1, 1, 1);
        this.displayConsole.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(CONSOLE_TEXTURE_EMISSION)), maxLight, overlay, 1, 1, 1, 1);
        matrices.pop();
    }

}
