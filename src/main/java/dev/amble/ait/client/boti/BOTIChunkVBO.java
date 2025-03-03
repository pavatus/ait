package dev.amble.ait.client.boti;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.amble.ait.AITMod;
import dev.amble.ait.api.link.v2.block.AbstractLinkableBlockEntity;
import dev.amble.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BOTIChunkVBO {
    private VertexBuffer vertexBuffer;
    private int vertexCount = 0;
    private final Map<BlockPos, BlockEntity> blockEntities = new HashMap<>();
    private BlockPos targetPos;

    public BOTIChunkVBO() {
        this.vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
    }

    public void setTargetPos(BlockPos targetPos) {
        this.targetPos = targetPos;
    }

    public void updateChunkModel(ExteriorBlockEntity exteriorBlockEntity, NbtCompound chunkData) {
        if (exteriorBlockEntity == null || exteriorBlockEntity.getWorld() == null || !exteriorBlockEntity.getWorld().isClient()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        BlockRenderManager blockRenderManager = mc.getBlockRenderManager();
        ChunkPos chunkPos = new ChunkPos(targetPos);
        int baseY = targetPos.getY() & ~15;
        this.blockEntities.clear();

        BufferBuilder bufferBuilder = new BufferBuilder(4096 * 6 * 4); // Estimate capacity
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
        int vertexCounter = 0;

        for (int i = 0; i < 4096; i++) {
            int x = i & 15;
            int y = (i >> 4) & 15;
            int z = (i >> 8) & 15;
            BlockPos pos = new BlockPos(chunkPos.getStartX() + x, baseY + y, chunkPos.getStartZ() + z);

            BlockState state = getBlockStateFromChunkNBT(chunkData, pos);
            state = state != null ? state : Blocks.AIR.getDefaultState();

            String key = x + "_" + y + "_" + z;
            if (chunkData.contains("block_entities") && chunkData.getCompound("block_entities").contains(key)) {
                try {
                    NbtCompound nbt = chunkData.getCompound("block_entities").getCompound(key);
                    BlockEntity blockEntity = BlockEntity.createFromNbt(pos, state, nbt);
                    if (blockEntity != null) {
                        if (blockEntity instanceof AbstractLinkableBlockEntity abstractLinkableBlockEntity &&
                                exteriorBlockEntity.tardis() != null) {
                            abstractLinkableBlockEntity.link(exteriorBlockEntity.tardis().get());
                        }
                        BlockPos relativePos = pos.subtract(new BlockPos(chunkPos.getStartX() + 8, baseY, chunkPos.getStartZ() + 8));
                        this.blockEntities.put(relativePos, blockEntity);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to load block entity at " + pos + ": " + e.getMessage());
                }
            }

            if (!state.isAir() && !state.hasBlockEntity()) {
                try {
                    BakedModel model = blockRenderManager.getModel(state);
                    if (model != null) {
                        Random random = Random.create();
                        vertexCounter += addQuadsToBuffer(model.getQuads(state, null, random), bufferBuilder, x, y, z);
                        vertexCounter += addQuadsToBuffer(model.getQuads(state, Direction.UP, random), bufferBuilder, x, y, z);
                        vertexCounter += addQuadsToBuffer(model.getQuads(state, Direction.DOWN, random), bufferBuilder, x, y, z);
                        vertexCounter += addQuadsToBuffer(model.getQuads(state, Direction.NORTH, random), bufferBuilder, x, y, z);
                        vertexCounter += addQuadsToBuffer(model.getQuads(state, Direction.SOUTH, random), bufferBuilder, x, y, z);
                        vertexCounter += addQuadsToBuffer(model.getQuads(state, Direction.EAST, random), bufferBuilder, x, y, z);
                        vertexCounter += addQuadsToBuffer(model.getQuads(state, Direction.WEST, random), bufferBuilder, x, y, z);
                    }
                } catch (Exception e) {
                    System.out.println("Failed to generate quads for block at " + x + "," + y + "," + z + ": " + e.getMessage());
                }
            }
        }

        if (vertexCounter > 0) {
            this.vertexCount = vertexCounter;
            BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.end();
            vertexBuffer.bind();
            vertexBuffer.upload(builtBuffer);
            VertexBuffer.unbind();
            System.out.println("VBO updated with " + vertexCount + " vertices for position " + targetPos);
        } else {
            System.out.println("No vertex data generated for chunk at " + targetPos + " from data: " + chunkData);
            cleanup();
        }
    }

    private int addQuadsToBuffer(List<BakedQuad> quads, BufferBuilder buffer, int xOffset, int yOffset, int zOffset) {
        if (quads == null || quads.isEmpty()) return 0;

        int verticesAdded = 0;
        for (BakedQuad quad : quads) {
            if (quad == null) continue;

            int[] vertexData = quad.getVertexData();
            if (vertexData == null || vertexData.length < 32) continue;

            try {
                for (int i = 0; i < 4; i++) { // 4 vertices per quad
                    int baseIndex = i * 8;
                    float x = Float.intBitsToFloat(vertexData[baseIndex]) + xOffset;
                    float y = Float.intBitsToFloat(vertexData[baseIndex + 1]) + yOffset;
                    float z = Float.intBitsToFloat(vertexData[baseIndex + 2]) + zOffset;
                    float color = Float.intBitsToFloat(vertexData[baseIndex + 3]); // Assuming packed color
                    float u = Float.intBitsToFloat(vertexData[baseIndex + 4]);
                    float v = Float.intBitsToFloat(vertexData[baseIndex + 5]);
                    int light = vertexData[baseIndex + 6];
                    int normal = vertexData[baseIndex + 7];

                    buffer.vertex(x, y, z)
                            .color(color, color, color, 1.0f) // Adjust color handling if needed
                            .texture(u, v)
                            .light(light)
                            .normal(
                                    (normal >> 16) & 0xFF,
                                    (normal >> 8) & 0xFF,
                                    normal & 0xFF
                            )
                            .next();
                    verticesAdded++;
                }
            } catch (Exception e) {
                AITMod.LOGGER.error("Failed to process quad: {}", e.getMessage());
            }
        }
        System.out.println("Added " + verticesAdded + " vertices to buffer");
        return verticesAdded;
    }

    public void render(MatrixStack matrices, int light, int overlay) {
        if (vertexCount == 0) {
            System.out.println("VBO not initialized or empty for position " + targetPos);
            return;
        }

        matrices.push();
        RenderSystem.enableDepthTest();

        RenderSystem.setShader(GameRenderer::getRenderTypeSolidProgram);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

        ShaderProgram shader = RenderSystem.getShader();
        if (shader != null) {
            if (shader.getUniform("Light0") != null) {
                shader.getUniform("Light0").set((float) (light & 0xFFFF) / 0xFFFF);
            }
            if (shader.getUniform("Light1") != null) {
                shader.getUniform("Light1").set((float) (light >> 16 & 0xFFFF) / 0xFFFF);
            }
        }

        System.out.println("Rendering VBO with " + vertexCount + " vertices at " + targetPos);

        vertexBuffer.bind();
        VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.setupState();
        vertexBuffer.draw();
        VertexBuffer.unbind();
        VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL.clearState();

        matrices.pop();
    }

    public void cleanup() {
        if (vertexBuffer != null) {
            vertexBuffer.close();
            vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
            vertexCount = 0;
        }
    }

    public Map<BlockPos, BlockEntity> getBlockEntities() {
        return blockEntities;
    }

    private BlockState getBlockStateFromChunkNBT(NbtCompound chunkData, BlockPos pos) {
        // ... (your existing implementation)
        return Blocks.AIR.getDefaultState();
    }
}