/*
 * Decompiled with CFR 0.2.2 (FabricMC 7c48b8c4).
 */
package loqor.ait.client.renderers.entities;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.client.models.decoration.GallifreyFallsFrameModel;
import loqor.ait.client.models.decoration.GallifreyFallsModel;
import loqor.ait.core.entities.GallifreyFallsPaintingEntity;

@Environment(value=EnvType.CLIENT)
public class GallifreyFallsPaintingEntityRenderer
        extends EntityRenderer<GallifreyFallsPaintingEntity> {
    public static final Identifier PAINTING_TEXTURE = new Identifier(AITMod.MOD_ID, "textures/painting/gallifrey_falls.png");
    public static final Identifier FRAME_TEXTURE = new Identifier(AITMod.MOD_ID, "textures/painting/frame.png");
    GallifreyFallsFrameModel frame;
    GallifreyFallsModel painting;
    public GallifreyFallsPaintingEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        frame = new GallifreyFallsFrameModel(GallifreyFallsFrameModel.getTexturedModelData().createModel());
        painting = new GallifreyFallsModel(GallifreyFallsModel.getTexturedModelData().createModel());
    }

    @Override
    public void render(GallifreyFallsPaintingEntity paintingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        RenderSystem.enableScissor(100, 100, 900,900);
        painting.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(PAINTING_TEXTURE)), i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableScissor();
        super.render(paintingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(GallifreyFallsPaintingEntity entity) {
        return null;
    }

}
