package dev.codiak.client;

import dev.codiak.BEs.BOTIBlockEntity;

import net.minecraft.client.render.VertexConsumerProvider;
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
        if(VBO == null) {
            VBO = new BOTIChunkVBO();
            VBO.setTargetPos(new BlockPos(0, 0, 0));
            VBO.updateChunkModelTestQuads();
        }
        else VBO.render(matrices, light, overlay);
//        matrices.pop();
    }
}
