package dev.amble.ait.module.planet.client.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.VertexSorter;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

import dev.amble.ait.AITMod;
import dev.amble.ait.client.renderers.AITRenderLayers;
import dev.amble.ait.module.planet.client.models.CelestialBodyModel;


public class CelestialBodyRenderer {

    public static void renderFarAwayBody(Vec3d targetPosition, Vector3f scale, Identifier texture, boolean hasClouds, boolean hasAtmosphere, Vector3f atmosphereColor) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Camera camera = mc.gameRenderer.getCamera();
        VertexConsumerProvider.Immediate provider = mc.getBufferBuilders().getEntityVertexConsumers();

        Vec3d cameraPos = camera.getPos();

        Vec3d targetPos = new Vec3d(camera.getPos().getX() + targetPosition.getX(),
                camera.getPos().getY() + targetPosition.getY(),
                camera.getPos().getZ() + targetPosition.getZ());

        Vec3d diff = targetPos.subtract(cameraPos);

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrixStack.translate(diff.x, diff.y, diff.z);
        matrixStack.scale(scale.x, scale.y, scale.z);

        BackgroundRenderer.clearFog();
        RenderSystem.depthFunc(GL11.GL_NOTEQUAL);
        RenderSystem.setProjectionMatrix(matrixStack.peek().getPositionMatrix().perspective(90, 1, 0.05f, 10000000), VertexSorter.BY_Z);

        CelestialBodyModel.getTexturedModelData().createModel().render(matrixStack,
                provider.getBuffer(AITRenderLayers.getBeaconBeam(texture, false)),
                0xf000f0, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1f);
        provider.draw();

        if (hasAtmosphere) {
            atmosphereRenderer(matrixStack, atmosphereColor, provider, false, hasClouds);
            provider.draw();
        }
        provider.draw();
        RenderSystem.restoreProjectionMatrix();
        RenderSystem.depthFunc(GL11.GL_EQUAL);
    }

    public static void renderStarBody(Vec3d targetPosition, Vector3f scale, Vector2f rotation, Identifier texture, boolean hasAtmosphere, Vector3f atmosphereColor) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Camera camera = mc.gameRenderer.getCamera();
        VertexConsumerProvider.Immediate provider = mc.getBufferBuilders().getEntityVertexConsumers();

        Vec3d cameraPos = camera.getPos();

        Vec3d targetPos = new Vec3d(targetPosition.getX(),targetPosition.getY(),targetPosition.getZ());

        Vec3d diff = targetPos.subtract(cameraPos);

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrixStack.translate(diff.x, diff.y, diff.z);
        matrixStack.scale(scale.x, scale.y, scale.z);

        BackgroundRenderer.clearFog();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(GL11.GL_NOTEQUAL);


        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation.getY()));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180 + rotation.getX()));

        CelestialBodyModel.getTexturedModelData().createModel().render(matrixStack,
                provider.getBuffer(AITRenderLayers.getBeaconBeam(texture, false)),
                0xf000f00, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
        provider.draw();

        if (hasAtmosphere) {
            atmosphereRenderer(matrixStack, atmosphereColor, provider, true,false);
            provider.draw();
        }
        RenderSystem.depthFunc(GL11.GL_EQUAL);
        matrixStack.pop();
    }

    public static void renderComprehendableBody(Vec3d targetPosition, Vector3f scale, Vector2f rotation, Identifier texture, boolean hasClouds, boolean hasAtmosphere, Vector3f atmosphereColor) {
        MinecraftClient mc = MinecraftClient.getInstance();
        Camera camera = mc.gameRenderer.getCamera();
        VertexConsumerProvider.Immediate provider = mc.getBufferBuilders().getEntityVertexConsumers();

        Vec3d cameraPos = camera.getPos();

        Vec3d targetPos = new Vec3d(targetPosition.getX(),targetPosition.getY(),targetPosition.getZ());

        Vec3d diff = targetPos.subtract(cameraPos);

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrixStack.translate(diff.x, diff.y, diff.z);
        matrixStack.scale(scale.x, scale.y, scale.z);

        BackgroundRenderer.clearFog();
        RenderSystem.depthMask(true);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation.getY()));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180 + rotation.getX()));

        CelestialBodyModel.getTexturedModelData().createModel().render(matrixStack,
                provider.getBuffer(AITRenderLayers.getEntityTranslucentCull(texture)),
                0xf000f0, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1f);
        provider.draw();

        if (hasAtmosphere) {
            atmosphereRenderer(matrixStack, atmosphereColor, provider, false, hasClouds);
            provider.draw();
        }
        matrixStack.pop();
    }

    public static void atmosphereRenderer(MatrixStack matrixStack, Vector3f color, VertexConsumerProvider.Immediate provider, boolean isStar, boolean hasClouds) {
        ModelPart model = CelestialBodyModel.getTexturedModelData().createModel();
        for (int i = 0; i < 12; i++) {
            float alpha = (float) (0.06f - Math.log(i + 1) * 0.001f);
            matrixStack.push();
            float gg = 1.0f + ((i != 0 ? i : i + 1) * 0.01f);
            matrixStack.scale(gg, gg, gg);
            float delta = (MinecraftClient.getInstance().getTickDelta() + MinecraftClient.getInstance().player.age) * 0.00001f;
            RenderLayer renderLayer = RenderLayer.getEnergySwirl(new Identifier("textures/environment/clouds.png"), delta % 1.0F, (delta * 0.1F) % 1.0F);
            Identifier texture = i == 1 && hasClouds ? new Identifier("textures/environment/clouds.png") : AITMod.id("textures/environment/atmosphere.png");
            RenderSystem.setShaderTexture(0, texture);
            if (i != 1) {
                //System.out.println("min" + (Math.min(color.z + (0.05f * i + i), 1.0f)));
                model.render(matrixStack,
                        provider.getBuffer(isStar && (i == 2 || i == 3 || i == 4) ?
                                AITRenderLayers.getEyes(texture) : AITRenderLayers.getBeaconBeam(texture, true)),
                        0, OverlayTexture.DEFAULT_UV,  1 + Math.min(color.x + (0.015f * i), 5.0f), 1 + Math.min(color.y + (0.015f * i), 5.0f), 1 + Math.min(color.z + (0.015f * i), 5.0f), alpha);
            } else if (hasClouds) {
                model.render(matrixStack,
                        provider.getBuffer(renderLayer),
                        0xf000f00, OverlayTexture.DEFAULT_UV, 1, 1, 1, 0.2f);
                matrixStack.scale(1.01f, 1.01f, 1.01f);
                model.render(matrixStack,
                        provider.getBuffer(renderLayer),
                        0xf000f00, OverlayTexture.DEFAULT_UV, 1, 1, 1, 0.2f);
            }
            matrixStack.pop();
        }
    }
}
