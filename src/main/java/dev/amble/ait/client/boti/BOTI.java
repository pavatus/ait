package dev.amble.ait.client.boti;


import java.util.LinkedList;
import java.util.Queue;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;

import dev.amble.ait.core.blockentities.DoorBlockEntity;
import dev.amble.ait.core.blockentities.ExteriorBlockEntity;
import dev.amble.ait.core.engine.block.multi.MultiBlockStructure;
import dev.amble.ait.core.entities.GallifreyFallsPaintingEntity;
import dev.amble.ait.core.entities.RiftEntity;


public class BOTI {

    /*static {
        ClientPlayNetworking.registerGlobalReceiver(INTERIOR_UPDATE, (client, handler, buf, responseSender) -> {
            if (client.world == null) return;
            //System.out.println("WHAT");
            BlockPos pos = buf.readBlockPos();
            BlockState state = client.world.getBlockState(pos);
            if (BOTI.BLOCK_RENDER_QUEUE.isEmpty())
                BOTI.BLOCK_RENDER_QUEUE.add(new Pair<>(pos, state));
        });
    }*/
    public static final Queue<RiftEntity> RIFT_RENDERING_QUEUE = new LinkedList<>();
    public static BOTIHandler BOTI_HANDLER = new BOTIHandler();
    public static BOTIVBO BOTIVBO = new BOTIVBO();
    public static AITBufferBuilderStorage AIT_BUF_BUILDER_STORAGE = new AITBufferBuilderStorage();
    public static Queue<DoorBlockEntity> DOOR_RENDER_QUEUE = new LinkedList<>();
    public static Queue<GallifreyFallsPaintingEntity> PAINTING_RENDER_QUEUE = new LinkedList<>();
    public static Queue<ExteriorBlockEntity> EXTERIOR_RENDER_QUEUE = new LinkedList<>();
    //public static Queue<Pair<BlockPos, BlockState>> BLOCK_RENDER_QUEUE = new LinkedList<>();

    /*public static void renderBlockVertexBuffer(MatrixStack stack) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuffer();
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(builder);
        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
        //System.out.println(BLOCK_RENDER_QUEUE);
        for (Pair<BlockPos, BlockState> block : BLOCK_RENDER_QUEUE) {
            //System.out.println("WHAT");
            if (!builder.isBuilding()) {
                stack.push();
                stack.scale(100, 100, 100);
                stack.translate(block.getLeft().getX() & 0xF, block.getLeft().getY() & 0xF, block.getLeft().getZ() & 0xF);
                client.getBlockRenderManager().renderBlock(block.getRight(), block.getLeft(),
                        client.world, stack, immediate.getBuffer(RenderLayers.getBlockLayer(block.getRight()))
                        , true, client.world.random);
                BlockEntity entity = client.world.getBlockEntity(block.getLeft());
                if (entity != null) {
                    client.getBlockEntityRenderDispatcher().render(entity, client.getTickDelta(), stack, immediate);
                }
                stack.pop();
            }
        }
        //immediate.draw();
        tessellator.draw();
        //BLOCK_RENDER_QUEUE.clear();
    }*/

    public static void structureRendering(MatrixStack stack, MultiBlockStructure structure, int light, int overlay, float ticks) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;
        stack.push();
        client.getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

        for (RenderLayer layer : RenderLayer.getBlockLayers()) {
            BOTIVBO.begin(layer);
            layer.startDrawing();
            BufferBuilder builder = BOTIVBO.getBufferBuilder(layer);

            if (!builder.isBuilding())
                builder.begin(VertexFormat.DrawMode.QUADS, dev.amble.ait.client.boti.BOTIVBO.format);

            boolean hasRenderedLayer = false;

            MatrixStack secondaryStack = new MatrixStack();

            for (MultiBlockStructure.BlockOffset blockOffset : structure) {
                BlockState state = client.world.getBlockState(blockOffset.offset());
                if (state.equals(Blocks.AIR.getDefaultState())) continue;
                FluidState fluidState = client.world.getFluidState(blockOffset.offset());
                secondaryStack.push();
                secondaryStack.translate(blockOffset.offset().getX() & 0xF, blockOffset.offset().getY() & 0xF, blockOffset.offset().getZ() & 0xF);
                if (state.getFluidState().isEmpty()) {
                    client.getBlockRenderManager().renderBlock(state, blockOffset.offset(),
                            client.world, secondaryStack, builder, true, client.world.getRandom());
                    BlockEntity entity = client.world.getBlockEntity(blockOffset.offset());
                    if (entity != null) {
                        BlockEntityRenderer<BlockEntity> blockEntityRenderer = client.getBlockEntityRenderDispatcher().get(entity);
                        if (blockEntityRenderer != null) {
                            blockEntityRenderer.render(entity, client.getTickDelta(),
                                    secondaryStack, VertexConsumerProvider.immediate(builder), light, overlay);
                        }
                        hasRenderedLayer = true;
                    }
                } else {
                    if (!builder.isBuilding())
                        builder.begin(VertexFormat.DrawMode.QUADS, dev.amble.ait.client.boti.BOTIVBO.format);
                    client.getBlockRenderManager().renderFluid(blockOffset.offset(), client.world, builder, state, fluidState);
                    hasRenderedLayer = true;
                }
                secondaryStack.pop();
            }

            if (hasRenderedLayer) {
                BOTIVBO.upload(layer);
            }
            BOTIVBO.unbind(layer);
            layer.endDrawing();
        }
        stack.push();
        BOTIVBO.draw();
        stack.pop();
        stack.pop();
    }

    public static void copyFramebuffer(Framebuffer src, Framebuffer dest) {
        GlStateManager._glBindFramebuffer(GlConst.GL_READ_FRAMEBUFFER, src.fbo);
        GlStateManager._glBindFramebuffer(GlConst.GL_DRAW_FRAMEBUFFER, dest.fbo);
        GlStateManager._glBlitFrameBuffer(0, 0, src.textureWidth, src.textureHeight, 0, 0, dest.textureWidth, dest.textureHeight, GlConst.GL_DEPTH_BUFFER_BIT | GlConst.GL_COLOR_BUFFER_BIT, GlConst.GL_NEAREST);
    }

    public static void copyColor(Framebuffer src, Framebuffer dest) {
        GlStateManager._glBindFramebuffer(GlConst.GL_READ_FRAMEBUFFER, src.fbo);
        GlStateManager._glBindFramebuffer(GlConst.GL_DRAW_FRAMEBUFFER, dest.fbo);
        GlStateManager._glBlitFrameBuffer(0, 0, src.textureWidth, src.textureHeight, 0, 0, dest.textureWidth, dest.textureHeight, GlConst.GL_COLOR_BUFFER_BIT, GlConst.GL_NEAREST);
    }

    public static void copyDepth(Framebuffer src, Framebuffer dest) {
        GlStateManager._glBindFramebuffer(GlConst.GL_READ_FRAMEBUFFER, src.fbo);
        GlStateManager._glBindFramebuffer(GlConst.GL_DRAW_FRAMEBUFFER, dest.fbo);
        GlStateManager._glBlitFrameBuffer(0, 0, src.textureWidth, src.textureHeight, 0, 0, dest.textureWidth, dest.textureHeight, GlConst.GL_DEPTH_BUFFER_BIT, GlConst.GL_NEAREST);
    }

    public static void setFramebufferColor(Framebuffer src, float r, float g, float b, float a) {
        src.setClearColor(r, g, b, a);
    }
}
