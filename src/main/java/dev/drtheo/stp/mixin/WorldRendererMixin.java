package dev.drtheo.stp.mixin;

import com.google.common.collect.Queues;
import dev.drtheo.stp.STPWorldRenderer;
import dev.drtheo.stp.SeamlessTp;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BuiltChunkStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements STPWorldRenderer {

    @Shadow private @Nullable ChunkBuilder chunkBuilder;

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;

    @Shadow private @Nullable ClientWorld world;

    @Shadow public abstract void reload();

    @Shadow @Final private ObjectArrayList<WorldRenderer.ChunkInfo> chunkInfos;

    @Shadow @Final private Set<BlockEntity> noCullingBlockEntities;

    @Shadow private int cameraChunkZ;

    @Shadow private int cameraChunkY;

    @Shadow private int cameraChunkX;

    @Shadow private double lastCameraChunkUpdateX;

    @Shadow private double lastCameraChunkUpdateY;

    @Shadow private double lastCameraChunkUpdateZ;

    @Shadow private @Nullable BuiltChunkStorage chunks;

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract void enqueueChunksInViewDistance(Camera camera, Queue<WorldRenderer.ChunkInfo> queue);

    @Shadow protected abstract void collectRenderableChunks(LinkedHashSet<WorldRenderer.ChunkInfo> chunks, WorldRenderer.ChunkInfoList chunkInfoList, Vec3d cameraPos, Queue<WorldRenderer.ChunkInfo> queue, boolean chunkCullingEnabled);

    @Shadow @Final private AtomicReference<WorldRenderer.RenderableChunks> renderableChunks;

    @Shadow @Final private AtomicBoolean updateFinished;

    @Shadow protected abstract void updateChunks(Camera camera);

    @Override
    public void stp$setWorld(ClientWorld world) {
        if (world == null) {
            SeamlessTp.LOGGER.info("Set world to null in world renderer!");
            this.lastCameraChunkUpdateX = Double.MIN_VALUE;
            this.lastCameraChunkUpdateY = Double.MIN_VALUE;
            this.lastCameraChunkUpdateZ = Double.MIN_VALUE;
            this.cameraChunkX = Integer.MIN_VALUE;
            this.cameraChunkY = Integer.MIN_VALUE;
            this.cameraChunkZ = Integer.MIN_VALUE;

            this.entityRenderDispatcher.setWorld(null);
            this.world = null;

            if (this.chunks != null) {
                this.chunks.clear();
                this.chunks = null;
            }

            if (this.chunkBuilder != null)
                this.chunkBuilder.stop();

            this.chunkBuilder = null;
            this.noCullingBlockEntities.clear();
            this.renderableChunks.set(null);
            this.chunkInfos.clear();
            return;
        }

        this.entityRenderDispatcher.setWorld(world);
        this.world = world;
        this.reload();

        Camera camera = this.client.gameRenderer.getCamera();

        double d = this.client.player.getX();
        double e = this.client.player.getY();
        double f = this.client.player.getZ();
        int i = ChunkSectionPos.getSectionCoord(d);
        int j = ChunkSectionPos.getSectionCoord(e);
        int k = ChunkSectionPos.getSectionCoord(f);
        if (this.cameraChunkX != i || this.cameraChunkY != j || this.cameraChunkZ != k) {
            this.lastCameraChunkUpdateX = d;
            this.lastCameraChunkUpdateY = e;
            this.lastCameraChunkUpdateZ = f;
            this.cameraChunkX = i;
            this.cameraChunkY = j;
            this.cameraChunkZ = k;
            this.chunks.updateCameraPosition(d, f);
        }
        Vec3d vec3d = camera.getPos();
        BlockPos blockPos = camera.getBlockPos();
        this.chunkBuilder.setCameraPosition(vec3d);

        //boolean bl = this.client.chunkCullingEnabled && !(client.player.isSpectator()
        //        && this.world.getBlockState(blockPos).isOpaqueFullCube(this.world, blockPos));
        boolean bl = false;

        ArrayDeque<WorldRenderer.ChunkInfo> queue = Queues.newArrayDeque();
        this.enqueueChunksInViewDistance(camera, queue);

        WorldRenderer.RenderableChunks renderableChunks = new WorldRenderer.RenderableChunks(this.chunks.chunks.length);

        this.collectRenderableChunks(renderableChunks.chunks, renderableChunks.chunkInfoList, vec3d, queue, bl);

        this.renderableChunks.set(renderableChunks);
        this.updateFinished.set(true);

        this.chunkInfos.addAll(renderableChunks.chunks);
        this.updateChunks(camera);
    }
}
