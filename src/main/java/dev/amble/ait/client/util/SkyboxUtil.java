package dev.amble.ait.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import dev.amble.ait.AITMod;
import dev.amble.ait.module.planet.client.renderers.CelestialBodyRenderer;
import dev.amble.ait.module.planet.client.renderers.SpaceSkyRenderer;


public class SkyboxUtil extends WorldRenderer {

    private static final Identifier TARDIS_SKY = AITMod.id("textures/environment/tardis_sky.png");
    private static final Identifier MOON_SKY = AITMod.id("textures/environment/tardis_sky.png");
    private static final Identifier SPACE_SKY = AITMod.id("textures/environment/space_sky.png");
    private static final Identifier EARTH = AITMod.id("textures/environment/earth.png");
    private static final Identifier MOON = AITMod.id("textures/environment/moon.png");
    private static final Identifier MARS = AITMod.id("textures/environment/mars.png");
    private static final Identifier SATURN = AITMod.id("textures/environment/saturn.png");
    private static final Identifier SATURN_RING = AITMod.id("textures/environment/saturn_ring.png");

    public static final Quaternionf[] LOOKUP = new Quaternionf[]{null, RotationAxis.POSITIVE_X.rotationDegrees(90.0f),
            RotationAxis.POSITIVE_X.rotationDegrees(-90.0f), RotationAxis.POSITIVE_X.rotationDegrees(180.0f),
            RotationAxis.POSITIVE_Z.rotationDegrees(90.0f), RotationAxis.POSITIVE_Z.rotationDegrees(-90.0f), null};

    public SkyboxUtil(MinecraftClient client, EntityRenderDispatcher entityRenderDispatcher, BlockEntityRenderDispatcher blockEntityRenderDispatcher, BufferBuilderStorage bufferBuilders) {
        super(client, entityRenderDispatcher, blockEntityRenderDispatcher, bufferBuilders);
    }

    public static void renderTardisSky(WorldRenderContext context) {
        SkyboxUtil.renderTardisSky(context.matrixStack());
    }

    public static void renderTardisSky(MatrixStack matrices) {
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);

        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.setShaderTexture(0, TARDIS_SKY);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        for (int i = 0; i < 6; i++) {
            matrices.push();

            Quaternionf rot = LOOKUP[i];

            if (rot != null) {
                matrices.multiply(rot);
            }

            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            bufferBuilder.vertex(matrix4f, -100.0f, -100.0f, -100.0f).texture(0.0f, 0.0f).color(40, 40, 40, 255).next();

            bufferBuilder.vertex(matrix4f, -100.0f, -100.0f, 100.0f).texture(0.0f, 16.0f).color(40, 40, 40, 255).next();

            bufferBuilder.vertex(matrix4f, 100.0f, -100.0f, 100.0f).texture(16.0f, 16.0f).color(40, 40, 40, 255).next();

            bufferBuilder.vertex(matrix4f, 100.0f, -100.0f, -100.0f).texture(16.0f, 0.0f).color(40, 40, 40, 255).next();

            tessellator.draw();
            matrices.pop();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    public static void renderMoonSky(MatrixStack matrices, Runnable fogCallback, VertexBuffer starsBuffer, ClientWorld world, float tickDelta, Matrix4f projectionMatrix) {
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-35f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(22f));

        SpaceSkyRenderer cubeMap = new SpaceSkyRenderer(AITMod.id("textures/environment/space_sky/panorama"));
        cubeMap.draw(tessellator, bufferBuilder, matrices);
        matrices.pop();

        Identifier id = AITMod.id("textures/environment/tardis_star.png");
        CelestialBodyRenderer.renderStarBody(new Vec3d(100, 50, 0),
                new Vector3f(4f, 4f, 4f), new Vector2f(0, 0),
                id, true, new Vector3f(1, 1, 1f));

        Identifier id1 = EARTH;
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(20f));
        CelestialBodyRenderer.renderFarAwayBody(new Vec3d(100, -22f, 0),
                new Vector3f(10f, 10f, 10f),
                id1, true, true, new Vector3f(0.18f, 0.35f, 0.60f));
        matrices.pop();
    }

    public static void renderSpaceSky(MatrixStack matrices, Runnable fogCallback, VertexBuffer starsBuffer, ClientWorld world, float tickDelta, Matrix4f projectionMatrix) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-405f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(300f));
        matrices.scale(5, 5, 5);

        SpaceSkyRenderer cubeMap = new SpaceSkyRenderer(AITMod.id("textures/environment/space_sky/panorama"));
        cubeMap.draw(tessellator, bufferBuilder, matrices);
        matrices.pop();


        // Planet Rendering
        renderStarBody(matrices, AITMod.id("textures/environment/tardis_star.png"), new Vec3d(31240, 1000, 0), new Vector3f(650f, 650f, 650f), new Vector2f(12, 45), false, new Vector3f(0.5f, 0, 0f));
        renderCelestialBody(matrices, EARTH, new Vec3d(0, 0, 0), new Vector3f(900f, 900f, 900f), new Vector2f(-22.5f, 45f), true, true, new Vector3f(0.18f, 0.35f, 0.60f));
        renderCelestialBody(matrices, MOON, new Vec3d(8240, 459f, 0), new Vector3f(150f, 150f, 150f), new Vector2f(22.5f, 45f), false, true, new Vector3f(0.5f, 0.5f, 0.5f));
        renderCelestialBody(matrices, MARS, new Vec3d(-2500, 1400, 10000), new Vector3f(500f, 500f, 500f), new Vector2f(55, 12), false, true, new Vector3f(0.5f, 1, 1));
        renderCelestialBody(matrices, SATURN_RING, new Vec3d(4500, 1200, 4500), new Vector3f(1000f, 1, 1000f), new Vector2f(0, 0), false, false, new Vector3f(0.5f, 1, 1));
        renderCelestialBody(matrices, SATURN, new Vec3d(4500, 1400, 4500), new Vector3f(500f, 500f, 500f), new Vector2f(22.5f, 22.5f), false, true, new Vector3f(0.55f, 0.4f, 0.2f));
    }

    /**
     * Renders a celestial body in space.
     */
    private static void renderCelestialBody(MatrixStack matrices, Identifier texture, Vec3d position, Vector3f scale, Vector2f rotation, boolean clouds, boolean atmosphere, Vector3f color) {
        matrices.push();
        CelestialBodyRenderer.renderComprehendableBody(position, scale, rotation, texture, clouds, atmosphere, color);
        matrices.pop();
    }

    private static void renderStarBody(MatrixStack matrices, Identifier texture, Vec3d position, Vector3f scale, Vector2f rotation, boolean atmosphere, Vector3f color) {
        matrices.push();
        CelestialBodyRenderer.renderStarBody(position, scale, rotation, texture, atmosphere, color);
        matrices.pop();
    }


}
