package loqor.ait.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Matrix4f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.profiler.Profiler;

import loqor.ait.AITMod;
import loqor.ait.client.data.ClientLandingManager;
import loqor.ait.client.renderers.entities.ControlEntityRenderer;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.landing.LandingPad;

public class LandingRegionRenderer {
    private static final int DARK_CYAN = ColorHelper.Argb.getArgb(255, 0, 155, 155);
    private static final Identifier AVAILABLE = new Identifier(AITMod.MOD_ID, "textures/marker/available.png");
    private static final Identifier LANDING = new Identifier(AITMod.MOD_ID, "textures/marker/landing.png");
    private static final Identifier OCCUPIED = new Identifier(AITMod.MOD_ID, "textures/marker/occupied.png");

    private final MinecraftClient client;

    public LandingRegionRenderer(MinecraftClient client) {
        this.client = client;
    }

    private static Identifier getTexture(LandingPad.Spot spot) {
        if (!spot.isOccupied()) return AVAILABLE;

        Tardis tardis = spot.getTardis().get();

        if (tardis.travel().isLanded()) return OCCUPIED;

        return LANDING;
    }


    public boolean shouldRender() {
        return ControlEntityRenderer.isPlayerHoldingScanningSonic() && ClientLandingManager.getInstance().getRegion(client.world, client.player.getBlockPos()).isPresent(); // TODO - constant calling of getRegion is extremely bad. work now, performance later :)
    }

    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        Profiler profiler = client.world.getProfiler();

        profiler.push("landing_pad");

        profiler.push("region");
        renderRegion(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        profiler.swap("chunk");
        renderChunk(matrices, vertexConsumers, cameraX, cameraY, cameraZ);

        profiler.pop();
        profiler.pop();
    }
    private void renderRegion(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        LandingPad.Region region = ClientLandingManager.getInstance().getRegion(client.world, client.player.getBlockPos()).orElseThrow();

        if (region.getSpots().isEmpty()) {
            region.getNextSpot();
        }

        for (LandingPad.Spot spot : region.getSpots()) {
            renderSpot(spot, matrices, vertexConsumers, cameraX, cameraY, cameraZ);
}
    }
    private void renderSpot(LandingPad.Spot spot, MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        Camera camera = client.gameRenderer.getCamera();
        BlockPos spotPos = spot.getPos();
        Vec3d target = new Vec3d(spotPos.getX(), spotPos.getY(), spotPos.getZ() + 1f);
        Vec3d transform = target.subtract(camera.getPos());

        matrices = new MatrixStack();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
        matrices.translate(transform.x + 0.5f, transform.y, transform.z - 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(client.player.age / 200f * 360f));
        matrices.translate(-0.5f, 0.5f, 0f);

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
        buffer.vertex(positionMatrix, 0, 0, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();
        buffer.vertex(positionMatrix, 0, -1, 0).color(1f, 1f, 1f, 1f).texture(0f, 1f).next();
        buffer.vertex(positionMatrix, 1, -1, 0).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(positionMatrix, 1, 0, 0).color(1f, 1f, 1f, 1f).texture(1f, 0f).next();

        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.setShaderTexture(0, getTexture(spot));
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableCull();
        tessellator.draw();

        RenderSystem.enableCull();
    }

    private void renderChunk(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        int k;
        int j;
        Entity entity = this.client.gameRenderer.getCamera().getFocusedEntity();
        float f = (float)((double)this.client.world.getBottomY() - cameraY);
        float g = (float)((double)this.client.world.getTopY() - cameraY);
        ChunkPos chunkPos = entity.getChunkPos();
        float h = (float)((double)chunkPos.getStartX() - cameraX);
        float i = (float)((double)chunkPos.getStartZ() - cameraZ);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getDebugLineStrip(1.0));
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        for (j = 2; j < 16; j += 2) {
            k = DARK_CYAN;
            vertexConsumer.vertex(matrix4f, h + (float)j, f, i).color(1.0f, 1.0f, 0.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, h + (float)j, f, i).color(k).next();
            vertexConsumer.vertex(matrix4f, h + (float)j, g, i).color(k).next();
            vertexConsumer.vertex(matrix4f, h + (float)j, g, i).color(1.0f, 1.0f, 0.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, h + (float)j, f, i + 16.0f).color(1.0f, 1.0f, 0.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, h + (float)j, f, i + 16.0f).color(k).next();
            vertexConsumer.vertex(matrix4f, h + (float)j, g, i + 16.0f).color(k).next();
            vertexConsumer.vertex(matrix4f, h + (float)j, g, i + 16.0f).color(1.0f, 1.0f, 0.0f, 0.0f).next();
        }
        for (j = 2; j < 16; j += 2) {
            k = DARK_CYAN;
            vertexConsumer.vertex(matrix4f, h, f, i + (float)j).color(1.0f, 1.0f, 0.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, h, f, i + (float)j).color(k).next();
            vertexConsumer.vertex(matrix4f, h, g, i + (float)j).color(k).next();
            vertexConsumer.vertex(matrix4f, h, g, i + (float)j).color(1.0f, 1.0f, 0.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, h + 16.0f, f, i + (float)j).color(1.0f, 1.0f, 0.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, h + 16.0f, f, i + (float)j).color(k).next();
            vertexConsumer.vertex(matrix4f, h + 16.0f, g, i + (float)j).color(k).next();
            vertexConsumer.vertex(matrix4f, h + 16.0f, g, i + (float)j).color(1.0f, 1.0f, 0.0f, 0.0f).next();
        }
        for (j = this.client.world.getBottomY(); j <= this.client.world.getTopY(); j += 2) {
            float l = (float)((double)j - cameraY);
            int m = DARK_CYAN;
            vertexConsumer.vertex(matrix4f, h, l, i).color(1.0f, 1.0f, 0.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, h, l, i).color(m).next();
            vertexConsumer.vertex(matrix4f, h, l, i + 16.0f).color(m).next();
            vertexConsumer.vertex(matrix4f, h + 16.0f, l, i + 16.0f).color(m).next();
            vertexConsumer.vertex(matrix4f, h + 16.0f, l, i).color(m).next();
            vertexConsumer.vertex(matrix4f, h, l, i).color(m).next();
            vertexConsumer.vertex(matrix4f, h, l, i).color(1.0f, 1.0f, 0.0f, 0.0f).next();
        }
        vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getDebugLineStrip(2.0));
        for (j = 0; j <= 16; j += 16) {
            for (int k2 = 0; k2 <= 16; k2 += 16) {
                vertexConsumer.vertex(matrix4f, h + (float)j, f, i + (float)k2).color(0.25f, 0.25f, 1.0f, 0.0f).next();
                vertexConsumer.vertex(matrix4f, h + (float)j, f, i + (float)k2).color(0.25f, 0.25f, 1.0f, 1.0f).next();
                vertexConsumer.vertex(matrix4f, h + (float)j, g, i + (float)k2).color(0.25f, 0.25f, 1.0f, 1.0f).next();
                vertexConsumer.vertex(matrix4f, h + (float)j, g, i + (float)k2).color(0.25f, 0.25f, 1.0f, 0.0f).next();
            }
        }
        for (j = this.client.world.getBottomY(); j <= this.client.world.getTopY(); j += 16) {
            float l = (float)((double)j - cameraY);
            vertexConsumer.vertex(matrix4f, h, l, i).color(0.25f, 0.25f, 1.0f, 0.0f).next();
            vertexConsumer.vertex(matrix4f, h, l, i).color(0.25f, 0.25f, 1.0f, 1.0f).next();
            vertexConsumer.vertex(matrix4f, h, l, i + 16.0f).color(0.25f, 0.25f, 1.0f, 1.0f).next();
            vertexConsumer.vertex(matrix4f, h + 16.0f, l, i + 16.0f).color(0.25f, 0.25f, 1.0f, 1.0f).next();
            vertexConsumer.vertex(matrix4f, h + 16.0f, l, i).color(0.25f, 0.25f, 1.0f, 1.0f).next();
            vertexConsumer.vertex(matrix4f, h, l, i).color(0.25f, 0.25f, 1.0f, 1.0f).next();
            vertexConsumer.vertex(matrix4f, h, l, i).color(0.25f, 0.25f, 1.0f, 0.0f).next();
        }
    }

    public void tryRender(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        if (!this.shouldRender()) return;

        this.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
    }

    private static LandingRegionRenderer INSTANCE;
    public static LandingRegionRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LandingRegionRenderer(MinecraftClient.getInstance());
        }
        return INSTANCE;
    }
}
