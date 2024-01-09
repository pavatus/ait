package mdteam.ait.mixin.client.rendering;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import mdteam.ait.AITMod;
import mdteam.ait.core.AITDimensions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class TARDISSkyboxMixin {

    @Shadow protected abstract void renderEndSky(MatrixStack matrices);

    @Unique
    private static final Identifier TARDIS_SKY = new Identifier(AITMod.MOD_ID, "textures/environment/tardis_sky.png");

    @Inject(method="renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At("HEAD"), cancellable = true)
    public void renderSky(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback, CallbackInfo ci) {
        ci.cancel();
        ClientWorld world = MinecraftClient.getInstance().world;
        if(world == null) return;
        if(world.getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD) {
            if (world.getDimensionEffects().getSkyType() == DimensionEffects.SkyType.END) {
                this.renderTardisSky(matrices);
            }
        } else {
            if (world.getDimensionEffects().getSkyType() == DimensionEffects.SkyType.END) {
                this.renderEndSky(matrices);
            }
        }
    }

    @Unique
    private void renderTardisSky(MatrixStack matrices) {
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        RenderSystem.setShaderTexture(0, TARDIS_SKY);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        for(int i = 0; i < 6; ++i) {
            matrices.push();
            if (i == 1) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            }

            if (i == 2) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
            }

            if (i == 3) {
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            }

            if (i == 4) {
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
            }

            if (i == 5) {
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90.0F));
            }

            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).texture(0.0F, 0.0F).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).texture(0.0F, 1).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).texture(1, 1).color(0, 0, 0, 255).next();
            bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).texture(1, 0.0F).color(0, 0, 0, 255).next();
            tessellator.draw();
            matrices.pop();
        }

        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

}
