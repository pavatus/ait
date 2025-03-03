package dev.amble.ait.client.boti;

import java.util.HashMap;
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
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.*;

import dev.amble.ait.core.blockentities.ExteriorBlockEntity;


public class BOTIChunkVBO {
    private VertexBuffer vertexBuffer;
    public int vertexCount = 0;
    public static int chunksToRender = 4;
    private final Map<BlockPos, BlockEntity> blockEntities = new HashMap<>();
    public Map<BlockPos, BlockState> blocks = new HashMap<>();
    private BlockPos targetPos;
    private boolean dirty = true;

    public BOTIChunkVBO() {
        this.vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
    }

    public static int blocksToRender() {
        return chunksToRender >> 4;
    }

    public void setTargetPos(BlockPos targetPos) {
        this.targetPos = targetPos;
//        markDirty();
    }

    public void markDirty() {
        this.dirty = true;
    }

    public void updateBlockMap(Map<BlockPos, BlockState> map) {
        this.blocks = map;
    }
    public void updateChunkModel(ExteriorBlockEntity exteriorBlockEntity, NbtCompound chunkData) {
        if (!dirty || exteriorBlockEntity == null || exteriorBlockEntity.getWorld() == null || !exteriorBlockEntity.getWorld().isClient()) return;

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
            states[x][y][z] = getBlockStateFromChunkNBT(chunkData, pos);
            states[x][y][z] = states[x][y][z] != null ? states[x][y][z] : Blocks.AIR.getDefaultState();
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

            if (isOccluded(states, x, y, z)) continue;

            if (!state.isAir() && !state.hasBlockEntity()) {
            }
        }

        if (vertexCounter > 0) {
            this.vertexCount = vertexCounter;
            BufferBuilder.BuiltBuffer builtBuffer = bufferBuilder.end();
            vertexBuffer.bind();
            vertexBuffer.upload(builtBuffer);
            VertexBuffer.unbind();
            dirty = false;
            System.out.println("VBO updated with " + vertexCount + " vertices for position " + targetPos);
        } else {
            System.out.println("No vertex data generated for chunk at " + targetPos + " from data: " + chunkData);
            cleanup();
        }
    }

    private boolean isOccluded(BlockState[][][] states, int x, int y, int z) {
        if (x == 0 || x == 15 || y == 0 || y == 15 || z == 0 || z == 15) return false;
        /*return !states[x-1][y][z].isAir() && !states[x+1][y][z].isAir() &&
                !states[x][y-1][z].isAir() && !states[x][y+1][z].isAir() &&
                !states[x][y][z-1].isAir() && !states[x][y][z+1].isAir();*/
        return false;
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
        if (!dirty) return;

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

            if (isOccluded(states, x, y, z)) continue;

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
//            dirty = false;
//            System.out.println("VBO updated with " + vertexCount + " vertices for position " + targetPos);
//        } else {
//            System.out.println("No vertex data generated for chunk at " + targetPos);
//            cleanup();
//        }
    }
}