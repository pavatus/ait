package loqor.ait.client.renderers;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import org.joml.Matrix4f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import loqor.ait.AITMod;
import loqor.ait.client.models.decoration.TardisStarModel;
import loqor.ait.tardis.Tardis;

public class TardisStar {

    public static final Identifier TARDIS_STAR_TEXTURE = new Identifier(AITMod.MOD_ID,
            "textures/environment/tardis_star.png");
    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

    public static void render(WorldRenderContext context, Tardis tardis) {
        renderStar(context, tardis);
        renderShine(context, tardis);
    }

    public static void renderStar(WorldRenderContext context, Tardis tardis) {
        Camera camera = context.camera();
        VertexConsumerProvider provider = context.consumers();

        Vec3d cameraPos = camera.getPos();
        Vec3d targetPos = new Vec3d(tardis.engine().getCorePos().x(),
                context.world().getBottomY() - (tardis.isGrowth() ? 120 : 90), tardis.engine().getCorePos().y());

        Vec3d diff = targetPos.subtract(cameraPos);

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        if (tardis.isGrowth()) {
            matrixStack.translate(tardis.getDesktop().doorPos().getPos().getX(), diff.y,
                    tardis.getDesktop().doorPos().getPos().getX());
        } else {
            matrixStack.translate(
                    tardis.engine().getCorePos().x != 0
                            ? diff.x - .5
                            : tardis.getDesktop().doorPos().getPos().getX() - .5,
                    diff.y,
                    tardis.engine().getCorePos().y != 0
                            ? diff.z - .5
                            : tardis.getDesktop().doorPos().getPos().getX() - .5);
        }
        matrixStack.scale(20f, 20f, 20f);

        matrixStack.multiply(RotationAxis.POSITIVE_Y
                .rotationDegrees(((float) MinecraftClient.getInstance().player.age / 200.0f) * 360f));

        TardisStarModel.getTexturedModelData().createModel().render(matrixStack,
                provider.getBuffer(AITRenderLayers.tardisEmissiveCullZOffset(TARDIS_STAR_TEXTURE, true)),
                LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, tardis.isGrowth() ? 0.1f : 1,
                tardis.isGrowth() ? 0.1f : 1, tardis.isGrowth() ? 0.1f : 1, 0.5f);

        matrixStack.scale(0.9f, 0.9f, 0.9f);
        TardisStarModel.getTexturedModelData().createModel().render(matrixStack,
                provider.getBuffer(AITRenderLayers.tardisEmissiveCullZOffset(TARDIS_STAR_TEXTURE, true)),
                LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, 1, tardis.isGrowth() ? 0.2f : 1,
                tardis.isGrowth() ? 0f : 1, 1f);
    }

    public static void renderShine(WorldRenderContext context, Tardis tardis) {
        if (tardis.isGrowth())
            return;

        MatrixStack matrixStack = new MatrixStack();
        VertexConsumerProvider provider = context.consumers();

        Vec3d cameraPos = context.camera().getPos();
        Vec3d targetPos = new Vec3d(tardis.engine().getCorePos().x(),
                context.world().getBottomY() - (tardis.isGrowth() ? 120 : 90), tardis.engine().getCorePos().y());

        Vec3d diff = targetPos.subtract(cameraPos);

        float l = (MinecraftClient.getInstance().getTickDelta() / 50120L);
        float sinFunc = (float) Math.sin((MinecraftClient.getInstance().player.age / 400f * 220f) * 0.2f + 0.2f);
        Random random = Random.create(432L);
        VertexConsumer vertexConsumer4 = provider.getBuffer(AITRenderLayers.getLightning());
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(context.camera().getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(context.camera().getYaw() + 180.0F));
        if (tardis.isGrowth()) {
            matrixStack.translate(tardis.getDesktop().doorPos().getPos().getX(), diff.y,
                    tardis.getDesktop().doorPos().getPos().getX());
        } else {
            matrixStack.translate(
                    tardis.engine().getCorePos().x != 0
                            ? diff.x - .5
                            : tardis.getDesktop().doorPos().getPos().getX() - .5,
                    diff.y,
                    tardis.engine().getCorePos().y != 0
                            ? diff.z - .5
                            : tardis.getDesktop().doorPos().getPos().getX() - .5);
        }
        if (!tardis.isRefueling())
            matrixStack.scale(4, 4, 4);
        else
            matrixStack.scale(4 + sinFunc, 4 + sinFunc, 4 + sinFunc);

        matrixStack.multiply(RotationAxis.POSITIVE_Y
                .rotationDegrees(((float) MinecraftClient.getInstance().player.age / 200f) * 360f));

        float m = Math.min(l > 0.8f ? (l - 0.8f) / 0.2f : 0.0f, 1.0f);

        for (int n = 0; n < 30; n++) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees((random.nextFloat() * 360.0f)));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((random.nextFloat() * 360.0f)));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((random.nextFloat() * 360.0f)));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees((random.nextFloat() * 360.0f)));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((random.nextFloat() * 360.0f)));
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((random.nextFloat() * 360.0f + l * 90.0f)));

            float o = random.nextFloat() * 10.0f + 10.0f + m * 10.0f;
            float p = random.nextFloat() * 0.5f + 1.0f + m * 2.0f;

            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            int q = (int) (255f * (1.0f - m));

            TardisStar.putDeathLightSourceVertex(tardis, vertexConsumer4, matrix4f, q);
            TardisStar.putDeathLightNegativeXTerminalVertex(tardis, vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightPositiveXTerminalVertex(tardis, vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightSourceVertex(tardis, vertexConsumer4, matrix4f, q);
            TardisStar.putDeathLightPositiveXTerminalVertex(tardis, vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightPositiveZTerminalVertex(tardis, vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightSourceVertex(tardis, vertexConsumer4, matrix4f, q);
            TardisStar.putDeathLightPositiveZTerminalVertex(tardis, vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightNegativeXTerminalVertex(tardis, vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightSourceVertex(tardis, vertexConsumer4, matrix4f, q);
            TardisStar.putDeathLightPositiveZTerminalVertex(tardis, vertexConsumer4, matrix4f, o, p);
            TardisStar.putDeathLightPositiveZTerminalVertex(tardis, vertexConsumer4, matrix4f, o, p);
        }
    }

    public static void putDeathLightSourceVertex(Tardis tardis, VertexConsumer buffer, Matrix4f matrix, int alpha) {
        buffer.vertex(matrix, 0.0f, 0.0f, 0.0f).color(255, 255, 255, alpha).next();
    }

    public static void putDeathLightNegativeXTerminalVertex(Tardis tardis, VertexConsumer buffer, Matrix4f matrix,
            float radius, float width) {
        buffer.vertex(matrix, -HALF_SQRT_3 * width, radius, -0.5f * width)
                .color(255, tardis.isGrowth() ? 30 : 154, 0, 0).next();
    }

    public static void putDeathLightPositiveXTerminalVertex(Tardis tardis, VertexConsumer buffer, Matrix4f matrix,
            float radius, float width) {
        buffer.vertex(matrix, HALF_SQRT_3 * width, radius, -0.5f * width).color(255, tardis.isGrowth() ? 30 : 154, 0, 0)
                .next();
    }

    public static void putDeathLightPositiveZTerminalVertex(Tardis tardis, VertexConsumer buffer, Matrix4f matrix,
            float radius, float width) {
        buffer.vertex(matrix, 0.0f, radius, width).color(255, tardis.isGrowth() ? 30 : 154, 0, 0).next();
    }
}
