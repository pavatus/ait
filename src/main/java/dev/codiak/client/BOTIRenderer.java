package dev.codiak.client;

import static net.minecraft.screen.PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;

import java.util.ArrayList;
import java.util.List;

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
//        matrices.push();
//        if(VBO == null) {
//            VBO = new BOTIChunkVBO();
//            VBO.setTargetPos(new BlockPos(0, 2, 0));
//            VBO.updateTestChunkModel();
//        }
//        else VBO.render(matrices, light, overlay);

//        matrices.push();
//        matrices.scale(1, 1, 1);
        this.renderThis(matrices);
//        matrices.pop();
//        matrices.pop();
    }

    public void renderThis(MatrixStack matrixStack) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
        List<BlockPos> positions = new ArrayList<>();

//        for (int x = 0; x < 16; ++x) {
//            for (int y = 0; y < 16; ++y) {
//                for (int z = 0; z < 16; ++z) {
//                    positions.add(new BlockPos(x, y, z));
//                }
//            }
//        }
//
//        for (BlockPos pos : positions) {
//            matrixStack.push();
//            matrixStack.translate(pos.getX(), pos.getY(), pos.getZ());
//            MinecraftClient.getInstance().getBlockRenderManager().renderBlock(
//                    Blocks.DIAMOND_BLOCK.getDefaultState(),
//                    new BlockPos(0, 0, 0),
//                    MinecraftClient.getInstance().world,
//                    matrixStack,
//                    MinecraftClient.getInstance().getBufferBuilders()
//                            .getEffectVertexConsumers().getBuffer(RenderLayers.getBlockLayer(Blocks.DIAMOND_BLOCK.getDefaultState())),
//                    true,
//                    MinecraftClient.getInstance().world.getRandom()
//            );
//            matrixStack.pop();
//        }


        if(VBO == null) {
            VBO = new BOTIChunkVBO();
            VBO.setTargetPos(new BlockPos(0, 2, 0));
            VBO.updateTestChunkModel();
        }
        else {//VBO.render(matrixStack, 147212134, OverlayTexture.DEFAULT_UV);
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
//            matrixStack.translate(pos.getX(), pos.getY(), pos.getZ());
//            MinecraftClient.getInstance().getBlockRenderManager().renderBlock(
//                    Blocks.DIAMOND_BLOCK.getDefaultState(),
//                    new BlockPos(0, 0, 0),
//                    MinecraftClient.getInstance().world,
//                    matrixStack,
//                    MinecraftClient.getInstance().getBufferBuilders()
//                            .getEffectVertexConsumers().getBuffer(RenderLayers.getBlockLayer(Blocks.DIAMOND_BLOCK.getDefaultState())),
//                    true,
//                    MinecraftClient.getInstance().world.getRandom()
//            );
            matrixStack.pop();
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, BLOCK_ATLAS_TEXTURE);
        tessellator.draw();
    }
}
