package dev.amble.ait.client.boti;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.systems.RenderSystem;

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
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import dev.amble.ait.api.link.v2.block.AbstractLinkableBlockEntity;
import dev.amble.ait.core.blockentities.ExteriorBlockEntity;

public class BOTIChunkVBO {
    private VertexBuffer vertexBuffer;
    public int vertexCount = 0;
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

        BufferBuilder bufferBuilder = new BufferBuilder(4096 * 6 * 4);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
        int vertexCounter = 0;

        for (int i = 0; i < 4096; i++) {
            int x = i & 15;
            int y = (i >> 4) & 15;
            int z = (i >> 8) & 15;
            BlockPos pos = new BlockPos(chunkPos.getStartX() + x, baseY + y, chunkPos.getStartZ() + z);

            BlockState state = getBlockStateFromChunkNBT(chunkData, pos);
            state = state != null ? state : Blocks.AIR.getDefaultState();
            state = Blocks.STONE.getDefaultState(); // Hardcoded for now
            String key = x + "_" + y + "_" + z;
            if (chunkData.contains("block_entities") && chunkData.getCompound("block_entities").contains(key)) {
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
            }

            if (!state.isAir() && !state.hasBlockEntity()) {
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
            if (vertexData.length < 32) continue;

            for (int i = 0; i < 4; i++) {
                int baseIndex = i * 8;
                float x = Float.intBitsToFloat(vertexData[baseIndex]) + xOffset;
                float y = Float.intBitsToFloat(vertexData[baseIndex + 1]) + yOffset;
                float z = Float.intBitsToFloat(vertexData[baseIndex + 2]) + zOffset;
                int packedColor = vertexData[baseIndex + 3];
                float r = ((packedColor >> 16) & 0xFF) / 255.0f;
                float g = ((packedColor >> 8) & 0xFF) / 255.0f;
                float b = (packedColor & 0xFF) / 255.0f;
                float a = ((packedColor >> 24) & 0xFF) / 255.0f;
                float u = Float.intBitsToFloat(vertexData[baseIndex + 4]);
                float v = Float.intBitsToFloat(vertexData[baseIndex + 5]);
                int light = vertexData[baseIndex + 6];
                int normal = vertexData[baseIndex + 7];
                float nx = ((normal >> 16) & 0xFF) / 255.0f * 2.0f - 1.0f;
                float ny = ((normal >> 8) & 0xFF) / 255.0f * 2.0f - 1.0f;
                float nz = (normal & 0xFF) / 255.0f * 2.0f - 1.0f;

                buffer.vertex(x, y, z)
                        .color(r, g, b, a)
                        .texture(u, v)
                        .light(light)
                        .normal(nx, ny, nz)
                        .next();
                verticesAdded++;
                if (verticesAdded <= 4) { // Log first quad only
                    System.out.println("Vertex: (" + x + ", " + y + ", " + z + "), Color: (" + r + ", " + g + ", " + b + ", " + a + ")");
                }
            }
        }
        System.out.println("Added " + verticesAdded + " vertices to buffer");
        return verticesAdded;
    }

    public void render(MatrixStack matrices, int light, int overlay) {
        if (vertexCount == 0) {
            return;
        }

        matrices.push();
        MinecraftClient mc = MinecraftClient.getInstance();
        Vec3d camPos = mc.gameRenderer.getCamera().getPos();
        matrices.translate(-camPos.x, -camPos.y, -camPos.z); // Align with camera
        matrices.translate(0, 0, -5); // 5 blocks in front of player

        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend(); // Prevent blending issues
        RenderSystem.depthFunc(515); // GL_LEQUAL
        RenderSystem.setShader(GameRenderer::getRenderTypeSolidProgram);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

        ShaderProgram shader = RenderSystem.getShader();
        if (shader != null) {
            int blockLight = light & 0xFFFF;
            int skyLight = (light >> 16) & 0xFFFF;
            float lightValue = Math.max(blockLight, skyLight) / 15.0f;
            if (shader.getUniform("Light0") != null) shader.getUniform("Light0").set(lightValue);
            if (shader.getUniform("Light1") != null) shader.getUniform("Light1").set(lightValue);
        }

        System.out.println("Rendering VBO with " + vertexCount + " vertices at " + targetPos);
        System.out.println("Camera at: " + camPos);

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
        if (chunkData.contains("block_states")) {
            NbtCompound blockStates = chunkData.getCompound("block_states");
            if (blockStates.contains("palette") && blockStates.contains("data")) {
                NbtList palette = blockStates.getList("palette", NbtCompound.COMPOUND_TYPE);
                long[] data = blockStates.getLongArray("data");
                if (data.length == 0 || palette.isEmpty()) {
                    System.out.println("Empty data or palette in chunk NBT for pos " + pos);
                    return Blocks.AIR.getDefaultState();
                }

                int bitsPerEntry = blockStates.contains("bitsPerEntry") ? blockStates.getInt("bitsPerEntry") :
                        Math.max(2, (int) Math.ceil(Math.log(palette.size()) / Math.log(2)));
                int x = pos.getX() & 15;
                int y = pos.getY() & 15;
                int z = pos.getZ() & 15;
                int index = y * 256 + z * 16 + x;

                int entriesPerLong = 64 / bitsPerEntry;
                int longIndex = index / entriesPerLong;
                if (longIndex >= data.length) {
                    System.out.println("Long index out of bounds: " + longIndex + " >= " + data.length);
                    return Blocks.AIR.getDefaultState();
                }
                int offset = (index % entriesPerLong) * bitsPerEntry;
                long value = (data[longIndex] >> offset) & ((1L << bitsPerEntry) - 1);

                if (value >= 0 && value < palette.size()) {
                    NbtCompound stateTag = palette.getCompound((int) value);
                    return BlockState.CODEC.parse(NbtOps.INSTANCE, stateTag)
                            .result().orElse(Blocks.AIR.getDefaultState());
                } else {
                    System.out.println("State value out of palette bounds at " + pos + ": " + value);
                    return Blocks.AIR.getDefaultState();
                }
            }
        }
        return Blocks.AIR.getDefaultState();
    }

    public void updateChunkModelTestQuads() {
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockRenderManager blockRenderManager = mc.getBlockRenderManager();
        this.blockEntities.clear();

        // Render one block instead of a full chunk
        BufferBuilder bufferBuilder = new BufferBuilder(24); // 6 quads * 4 vertices
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
        int vertexCounter = 0;

        BlockState state = Blocks.STONE.getDefaultState();
        BakedModel model = blockRenderManager.getModel(state);
        if (model != null) {
            Random random = Random.create();
            vertexCounter += addQuadsToBuffer(model.getQuads(state, null, random), bufferBuilder, 0, 0, 0);
        }

        if (vertexCounter > 0) {
            this.vertexCount = vertexCounter;
            BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.end();
            vertexBuffer.bind();
            vertexBuffer.upload(builtBuffer);
            VertexBuffer.unbind();
            System.out.println("VBO updated with " + vertexCount + " vertices for position " + targetPos);
        } else {
            System.out.println("No vertex data generated for chunk at " + targetPos);
            cleanup();
        }
    }
}