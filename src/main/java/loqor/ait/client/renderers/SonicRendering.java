package loqor.ait.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;

import loqor.ait.AITMod;
import loqor.ait.client.renderers.entities.ControlEntityRenderer;

public class SonicRendering {
    private static final Identifier SELECTED = new Identifier(AITMod.MOD_ID, "textures/marker/landing.png");

    private final MinecraftClient client;
    private final Profiler profiler;

    public SonicRendering(MinecraftClient client) {
        this.client = client;
        this.profiler = client.getProfiler();
    }
    public SonicRendering() {
        this(MinecraftClient.getInstance());
    }

    public static void renderFloorTexture(BlockPos pos, Identifier texture, @Nullable Identifier previous, boolean spinning) {
        renderFloorTexture(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), texture, previous, spinning);
    }
    public static void renderFloorTexture(Vec3d pos, Identifier texture, @Nullable Identifier previous, boolean spinning) {
        Profiler profiler = MinecraftClient.getInstance().world.getProfiler();

        profiler.push("get");
        MinecraftClient client = MinecraftClient.getInstance();
        Camera camera = client.gameRenderer.getCamera();
        MatrixStack matrices = new MatrixStack();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();

        profiler.swap("transform");
        Vec3d target = new Vec3d(pos.getX(), pos.getY(), pos.getZ() + 1f);
        Vec3d transform = target.subtract(camera.getPos());

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180f));
        matrices.translate(transform.x + 0.5f, transform.y + 0.05f, transform.z - 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90f));

        if (spinning) {
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(client.player.age / 200f * 360f));
        }
        matrices.translate(-0.5f, 0.5f, 0f);

        profiler.swap("vertexes");

        if (!buffer.isBuilding()) buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);

        buffer.vertex(positionMatrix, 0, 0, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f).next();
        buffer.vertex(positionMatrix, 0, -1, 0).color(1f, 1f, 1f, 1f).texture(0f, 1f).next();
        buffer.vertex(positionMatrix, 1, -1, 0).color(1f, 1f, 1f, 1f).texture(1f, 1f).next();
        buffer.vertex(positionMatrix, 1, 0, 0).color(1f, 1f, 1f, 1f).texture(1f, 0f).next();

        boolean shouldRender = !texture.equals(previous);

        if (shouldRender) {
            profiler.swap("draw");
            RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            RenderSystem.setShaderTexture(0, texture);
            RenderSystem.disableCull();
            RenderSystem.depthFunc(GL11.GL_ALWAYS);

            Tessellator.getInstance().draw();

            RenderSystem.depthFunc(GL11.GL_LEQUAL);
            RenderSystem.enableCull();
        }

        profiler.pop();
    }

    public void renderWorld(WorldRenderContext context) {
        if (!ControlEntityRenderer.isPlayerHoldingScanningSonic()) return;

        Profiler worldProfiler = context.profiler();
        worldProfiler.push("sonic");
        worldProfiler.push("world");

        renderSelectedBlock(context);

        worldProfiler.pop();
        worldProfiler.pop();
    }
    private void renderSelectedBlock(WorldRenderContext context) {
        profiler.push("target");

        HitResult crosshair = client.crosshairTarget;
        if (crosshair == null) {
            profiler.pop();
            profiler.pop();
            return;
        }
        Vec3d targetVec = crosshair.getPos();
        BlockPos targetPos = new BlockPos((int) targetVec.x, (int) targetVec.y, (int) targetVec.z);
        BlockState state = client.world.getBlockState(targetPos.down());
        if (state.isAir()) {
            profiler.pop();
            return;
        }

        renderFloorTexture(targetPos, SELECTED, null, false);

        profiler.pop();
    }
    public void renderGui(DrawContext context, float delta) {
        if (client.world == null) return;
        if (!ControlEntityRenderer.isPlayerHoldingScanningSonic()) return;

        profiler.swap("sonic");
        profiler.push("gui");

        profiler.push("target");
        HitResult crosshair = client.crosshairTarget;
        if (crosshair == null) {
            profiler.pop();
            profiler.pop();
            return;
        }
        Vec3d targetVec = crosshair.getPos();
        BlockPos targetPos = new BlockPos((int) targetVec.x, (int) targetVec.y, (int) targetVec.z);
        BlockState state = client.world.getBlockState(targetPos);

        profiler.swap("redstone");
        renderRedstone(context, state, targetPos);

        profiler.pop();
        profiler.pop();
    }
    private void renderRedstone(DrawContext context, BlockState state, BlockPos pos) {
        profiler.push("power");
        renderPower(context, pos);
        profiler.pop();
    }
    private void renderPower(DrawContext context, BlockPos pos) {
        int power = this.client.world.getReceivedRedstonePower(pos);
        if (power == 0) return;

        context.drawCenteredTextWithShadow(client.textRenderer, "" + power, getCentreX(), (int) (getMaxY() * 0.4), Colors.WHITE);
    }


    private int getMaxY() {
        return client.getWindow().getScaledHeight() ;
    }
    private int getCentreY() {
        return getMaxY() / 2;
    }
    private int getMaxX() {
        return client.getWindow().getScaledWidth();
    }
    private int getCentreX() {
        return getMaxX() / 2;
    }
    private int getTextWidth(String text) {
        return client.textRenderer.getWidth(text);
    }

    private static SonicRendering INSTANCE;

    public static SonicRendering getInstance() {
        if (INSTANCE == null)
            INSTANCE = new SonicRendering();

        return INSTANCE;
    }
}
