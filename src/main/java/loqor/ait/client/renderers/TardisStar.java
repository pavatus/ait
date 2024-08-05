package loqor.ait.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import loqor.ait.AITMod;
import loqor.ait.client.models.decoration.TardisStarModel;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

public class TardisStar {
    public static final Identifier TARDIS_STAR_TEXTURE = new Identifier(AITMod.MOD_ID, "textures/environment/tardis_star.png");
    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0) / 2.0);
    private float value = 0;

    public void renderStar(WorldRenderContext context, Tardis tardis) {
        Camera camera = context.camera();

        VertexConsumerProvider provider = context.consumers();

        if (provider == null)
            return;

        Vec3d cameraPos = camera.getPos();
        Vec3d targetPos = new Vec3d(tardis.engine().engineCorePosition().x, context.world().getBottomY() -90, tardis.engine().engineCorePosition().z);
        Vec3d diff = targetPos.subtract(cameraPos);

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrixStack.translate(
                tardis.engine().hasEngineCore().get() ? diff.x - .5: 0,
                diff.y,
                tardis.engine().hasEngineCore().get() ? diff.z - .5 : 0);
        matrixStack.scale(20f, 20f, 20f);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float)MinecraftClient.getInstance().player.age / 200.0f) * 360f));

        TardisStarModel.getTexturedModelData().createModel().render(matrixStack,
                        provider.getBuffer(AITRenderLayers.tardisEmissiveCullZOffset(TARDIS_STAR_TEXTURE, true)),
                LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, 1, 1, 1, 0.5f);

        matrixStack.scale(0.9f, 0.9f, 0.9f);
        TardisStarModel.getTexturedModelData().createModel().render(matrixStack,
                provider.getBuffer(AITRenderLayers.tardisEmissiveCullZOffset(TARDIS_STAR_TEXTURE, true)),
                LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1f);
    }

    public void renderShine(WorldRenderContext context, Tardis tardis) {
        MatrixStack matrixStack = new MatrixStack();
        VertexConsumerProvider provider = context.consumers();
        Vec3d cameraPos = context.camera().getPos();
        Vec3d targetPos = new Vec3d(tardis.engine().engineCorePosition().x, context.world().getBottomY() - 90, tardis.engine().engineCorePosition().z);
        Vec3d diff = targetPos.subtract(cameraPos);

        if (provider == null)
            return;

        float l = (MinecraftClient.getInstance().getTickDelta() / 50120L);
        float sinFunc = (float) (Math.sin(l) * 0.5f + 0.5f);
        Random random = Random.create(432L);
        VertexConsumer vertexConsumer4 = provider.getBuffer(AITRenderLayers.getLightning());
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(context.camera().getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(context.camera().getYaw() + 180.0F));
        matrixStack.translate(
                tardis.engine().hasEngineCore().get() ? diff.x - .5: 0,
                diff.y,
                tardis.engine().hasEngineCore().get() ? diff.z - .5: 0);
        if (!tardis.isRefueling())
            matrixStack.scale(4, 4, 4);
        else
            matrixStack.scale(4 + sinFunc, 4 + sinFunc, 4 + sinFunc);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float) MinecraftClient.getInstance().player.age / 200f) * 360f));
        float m = Math.min(l > 0.8f ? (l - 0.8f) / 0.2f : 0.0f, 1.0f);
        int n = 0;
        while ((float)n < (0.5) / 2.0f * 120.0f) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees((random.nextFloat() * 360.0f)));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((random.nextFloat() * 360.0f)));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((random.nextFloat() * 360.0f)));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees((random.nextFloat() * 360.0f)));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((random.nextFloat() * 360.0f)));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((random.nextFloat() * 360.0f + l * 90.0f)));
            float o = random.nextFloat() * 10.0f + 10.0f + m * 10.0f;
            float p = random.nextFloat() * 0.5f + 1.0f + m * 2.0f;
            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            int q = (int)(255f * (1.0f - m));
            TardisStar.putDeathLightSourceVertex(vertexConsumer4, matrix4f, q);
            TardisStar.putDeathLightNegativeXTerminalVertex(vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightPositiveXTerminalVertex(vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightSourceVertex(vertexConsumer4, matrix4f, q);
            TardisStar.putDeathLightPositiveXTerminalVertex(vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightPositiveZTerminalVertex(vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightSourceVertex(vertexConsumer4, matrix4f, q);
            TardisStar.putDeathLightPositiveZTerminalVertex(vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightNegativeXTerminalVertex(vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightSourceVertex(vertexConsumer4, matrix4f, q);
            TardisStar.putDeathLightPositiveZTerminalVertex(vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightPositiveZTerminalVertex(vertexConsumer4, matrix4f, o, p);
            n++;
        }
    }

    public static void putDeathLightSourceVertex(VertexConsumer buffer, Matrix4f matrix, int alpha) {
        buffer.vertex(matrix, 0.0f, 0.0f, 0.0f).color(255, 255, 255, alpha).next();
    }

    public static void putDeathLightNegativeXTerminalVertex(VertexConsumer buffer, Matrix4f matrix, float radius, float width) {
        buffer.vertex(matrix, -HALF_SQRT_3 * width, radius, -0.5f * width).color(255, 154, 0, 0).next();
    }

    public static void putDeathLightPositiveXTerminalVertex(VertexConsumer buffer, Matrix4f matrix, float radius, float width) {
        buffer.vertex(matrix, HALF_SQRT_3 * width, radius, -0.5f * width).color(255, 154, 0, 0).next();
    }

    public static void putDeathLightPositiveZTerminalVertex(VertexConsumer buffer, Matrix4f matrix, float radius, float width) {
        buffer.vertex(matrix, 0.0f, radius, 1.0f * width).color(255, 154, 0, 0).next();
    }
}
