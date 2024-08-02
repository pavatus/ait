package loqor.ait.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import loqor.ait.AITMod;
import loqor.ait.client.models.decoration.TardisStarModel;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.wrapper.client.ClientTardis;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class TardisStar {

    public static final Identifier TARDIS_STAR_TEXTURE = new Identifier(AITMod.MOD_ID, "textures/environment/tardis_star.png");
    public static int delta = 0;

    public static void renderStar(WorldRenderContext context, Tardis tardis) {
        Camera camera = context.camera();

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrixStack.translate(0, context.world().getBottomY() - 60 - camera.getPos().getY(), 0);
        matrixStack.scale(15f, 15f, 15f);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float) context.world().getTickOrder() / 2000L) * 360f));

        VertexConsumerProvider provider = context.consumers();

        if (provider == null)
            return;

        RenderSystem.enableDepthTest();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, TARDIS_STAR_TEXTURE);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        TardisStarModel.getTexturedModelData().createModel().render(matrixStack,
                provider.getBuffer(AITRenderLayers.tardisEmissiveCullZOffset(TARDIS_STAR_TEXTURE, true)),
                LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV);
    }
}
