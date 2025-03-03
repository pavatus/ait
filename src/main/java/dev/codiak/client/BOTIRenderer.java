package dev.codiak.client;

import static net.minecraft.screen.PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.codiak.tiles.BOTIBlockEntity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

import dev.amble.ait.client.boti.BOTIChunkVBO;

public class BOTIRenderer<T extends BOTIBlockEntity> implements BlockEntityRenderer<T> {
    BOTIChunkVBO VBO;

    public BOTIRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(BOTIBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        this.renderThis(matrices);
    }

    public void renderThis(MatrixStack matrixStack) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
        if(VBO == null) {
            VBO = new BOTIChunkVBO();
            VBO.setTargetPos(new BlockPos(0, 0, 0));
        }
        else {
            matrixStack.push();
            VBO.blocks.forEach((pos, state) -> {
                matrixStack.push();
                matrixStack.translate(pos.getX(), pos.getY(), pos.getZ());
                assert MinecraftClient.getInstance().world != null;
                MinecraftClient.getInstance().getBlockRenderManager().renderBlock(
                        state,
                        new BlockPos(0, 0, 0),
                        MinecraftClient.getInstance().world,
                        matrixStack,
                        MinecraftClient.getInstance().getBufferBuilders()
                                .getEffectVertexConsumers().getBuffer(RenderLayers.getBlockLayer(state)),
                        true,
                        MinecraftClient.getInstance().world.getRandom()
                );
                matrixStack.pop();
            });
            matrixStack.pop();
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, BLOCK_ATLAS_TEXTURE);
        tessellator.draw();
    }
}
