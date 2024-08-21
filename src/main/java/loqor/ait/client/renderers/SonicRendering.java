package loqor.ait.client.renderers;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Colors;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;

import loqor.ait.client.renderers.entities.ControlEntityRenderer;

public class SonicRendering {
    private final MinecraftClient client;
    private final Profiler profiler;

    public SonicRendering(MinecraftClient client) {
        this.client = client;
        this.profiler = client.getProfiler();
    }
    public SonicRendering() {
        this(MinecraftClient.getInstance());
    }

    public void renderWorld(WorldRenderContext context) {
        if (!ControlEntityRenderer.isPlayerHoldingScanningSonic()) return;

        profiler.swap("sonic");
        profiler.push("world");
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
