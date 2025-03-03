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
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.*;

import dev.amble.ait.core.blockentities.ExteriorBlockEntity;


public class BOTIChunkVBO {
    public VertexBuffer vertexBuffer;
    public BufferBuilder bufferBuilder;
    public int vertexCount = 0;
    public static int chunksToRender = 4;
    private final Map<BlockPos, BlockEntity> blockEntities = new HashMap<>();
    public Map<BlockPos, BlockState> blocks = new HashMap<>();
    private BlockPos targetPos;
    private boolean workingInThread = true;
    private boolean dirty = true;

    public BOTIChunkVBO() {
        this.vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
    }

    public static int blocksToRender() {
        return chunksToRender >> 4;
    }

    public void setTargetPos(BlockPos targetPos) {
        this.targetPos = targetPos;
    }

    public void markWorkingInThread() {
        this.workingInThread = true;
    }

    public void unmarkWorkingInThread() {
        this.workingInThread = false;
    }

    public boolean isWorkingInThread() {
        return this.workingInThread;
    }

    public void markDirty() {
        this.dirty = true;
    }

    public void unmarkDirty() {
        this.dirty = false;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void updateBlockMap(Map<BlockPos, BlockState> map) {
        this.blocks = map;
    }

    public void updateChunkModel(ExteriorBlockEntity exteriorBlockEntity) {
        if (!workingInThread || exteriorBlockEntity == null || exteriorBlockEntity.getWorld() == null || !exteriorBlockEntity.getWorld().isClient()) return;
        // can't just do `return;` because then it'll throw "unreachable statement" so I have to wrap it in `if(true)`
//        if(true) return; // Nothing in here should be called as it'll fuck EVERYTHING up
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockRenderManager blockRenderManager = mc.getBlockRenderManager();
        ChunkPos chunkPos = new ChunkPos(targetPos);
        int baseY = targetPos.getY() & ~15;
        this.blockEntities.clear();

//        if(bufferBuilder == null) bufferBuilder = new BufferBuilder(4096 * 6 * 4);
//        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
//        AtomicInteger vertexCounter = new AtomicInteger();
//
//            this.blocks.forEach((pos, state) -> {
//
//            if (!state.isAir() && !state.hasBlockEntity()) {
//                BakedModel model = MinecraftClient.getInstance().getBlockRenderManager().getModel(state);
//                if (model != null) {
//                    Random random = Random.create();
//                    int x = pos.getX();
//                    int y = pos.getY();
//                    int z = pos.getZ();
//                    vertexCounter.addAndGet(addQuadsToBuffer(model.getQuads(state, null, random), bufferBuilder, x, y, z));
//                    vertexCounter.addAndGet(addQuadsToBuffer(model.getQuads(state, Direction.UP, random), bufferBuilder, x, y, z));
//                    vertexCounter.addAndGet(addQuadsToBuffer(model.getQuads(state, Direction.DOWN, random), bufferBuilder, x, y, z));
//                    vertexCounter.addAndGet(addQuadsToBuffer(model.getQuads(state, Direction.NORTH, random), bufferBuilder, x, y, z));
//                    vertexCounter.addAndGet(addQuadsToBuffer(model.getQuads(state, Direction.SOUTH, random), bufferBuilder, x, y, z));
//                    vertexCounter.addAndGet(addQuadsToBuffer(model.getQuads(state, Direction.EAST, random), bufferBuilder, x, y, z));
//                    vertexCounter.addAndGet(addQuadsToBuffer(model.getQuads(state, Direction.WEST, random), bufferBuilder, x, y, z));
//                }
//            }
//        });
//
//        if (vertexCounter.get() > 0) {
//            this.vertexCount = vertexCounter.get();
        if(!this.isWorkingInThread() && this.isDirty()) {
            if(bufferBuilder != null) {
                BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.end();
                vertexBuffer.bind();
                vertexBuffer.upload(builtBuffer);
                VertexBuffer.unbind();
                unmarkDirty();
                System.out.println("VBO updated with " + vertexCount + " vertices for position " + targetPos);
            }
        }
//        } else {
//            System.out.println("No vertex data generated for chunk at " + targetPos);
//            cleanup();
//        }
    }
    public int addQuadsToBuffer(List<BakedQuad> quads, BufferBuilder buffer, int xOffset, int yOffset, int zOffset) {
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
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(mc.gameRenderer.getCamera().getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(mc.gameRenderer.getCamera().getYaw() + 180.0F));
        matrices.translate(camPos.x, camPos.y - 5f, camPos.z); // Align with camera

        RenderSystem.enableCull();
        RenderSystem.enableBlend(); // Prevent blending issues
        RenderSystem.defaultBlendFunc();
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

        RenderSystem.disableBlend();
        RenderSystem.disableCull();
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

    public void updateTestChunkModel() {
        if (!workingInThread) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        BlockRenderManager blockRenderManager = mc.getBlockRenderManager();
        ChunkPos chunkPos = new ChunkPos(targetPos);
        int baseY = targetPos.getY() & ~15;
        this.blockEntities.clear();

        // Precompute block states
        BlockState[][][] states = new BlockState[16][16][16];
        for (int i = 0; i < 4096; i++) {
            int x = i & 15;
            int y = (i >> 4) & 15;
            int z = (i >> 8) & 15;
            BlockPos pos = new BlockPos(chunkPos.getStartX() + x, baseY + y, chunkPos.getStartZ() + z);
            states[x][y][z] = Blocks.STONE.getDefaultState(); // Hardcoded for now.

            // TODO:
            // Use actual block states from the getBlockStateFromChunkNBT method
        }

        BufferBuilder bufferBuilder = new BufferBuilder(4096 * 6 * 4);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
        int vertexCounter = 0;

        for (int i = 0; i < 4096; i++) {
            int x = i & 15;
            int y = (i >> 4) & 15;
            int z = (i >> 8) & 15;
            BlockState state = states[x][y][z];

//            if (isOccluded(states, x, y, z)) continue;

            if (!state.isAir() && !state.hasBlockEntity()) {
                this.blocks.put(new BlockPos(x, y, z), state);
            }
        }

//        if (!blocks.isEmpty()) {
//            this.vertexCount = vertexCounter;
//            BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.end();
//            vertexBuffer.bind();
//            vertexBuffer.upload(builtBuffer);
//            VertexBuffer.unbind();
//                    dirty = false;
//            System.out.println("VBO updated with " + vertexCount + " vertices for position " + targetPos);
//        } else {
//            System.out.println("No vertex data generated for chunk at " + targetPos);
//            cleanup();
//        }
    }
}