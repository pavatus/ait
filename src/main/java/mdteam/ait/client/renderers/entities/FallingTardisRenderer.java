package mdteam.ait.client.renderers.entities;

import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.models.exteriors.SiegeModeModel;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.entities.FallingTardisEntity;
import mdteam.ait.tardis.TardisExterior;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class FallingTardisRenderer extends EntityRenderer<FallingTardisEntity> {
    private ExteriorModel model;
    public FallingTardisRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(FallingTardisEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        if (entity.getTardis() == null) {
            return;
        }

        TardisExterior tardisExterior = entity.getTardis().getExterior();
        ClientExteriorVariantSchema exteriorVariant = ClientExteriorVariantRegistry.withParent(tardisExterior.getVariant());

        if (tardisExterior == null) return;

        assert exteriorVariant != null;
        Class<? extends ExteriorModel> modelClass = exteriorVariant.model().getClass();

        if (model != null && !(model.getClass().isInstance(modelClass))) // fixme this is bad it seems to constantly create a new one anyway but i didnt realise.
            model = null;


        int maxLight = 0xFFFFFF;
        matrices.push();
        // matrices.translate(0.5, 0, 0.5);
        float f = entity.getBlockState().get(ExteriorBlock.FACING).asRotation();
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));

        if (getModel(entity) == null) return;

        if (entity.tardis().isSiegeMode()) {
            model = new SiegeModeModel(SiegeModeModel.getTexturedModelData().createModel());
            model.renderFalling(entity, getModel(entity).getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(SiegeModeModel.TEXTURE)), light,1,1,1,1,1);
            matrices.pop();
            return;
        }

        getModel(entity).renderFalling(entity, getModel(entity).getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(getTexture(entity))), light,1,1,1,1,1);

        if (exteriorVariant.emission() != null)
            getModel(entity).renderFalling(entity, getModel(entity).getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(getEmission(entity), true)), light,1,1,1,1,1);

        matrices.pop();
    }

    private ExteriorModel getModel(FallingTardisEntity entity) {
        if (model == null && entity.getTardis() != null) {
            model = ClientExteriorVariantRegistry.withParent(entity.getTardis().getExterior().getVariant()).model();
        }

        return model;
    }

    @Override
    public Identifier getTexture(FallingTardisEntity entity) {
        if (entity.getTardis() == null) return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE; // random texture just so i dont crash

        return ClientExteriorVariantRegistry.withParent(entity.getTardis().getExterior().getVariant()).texture();
    }

    public Identifier getEmission(FallingTardisEntity entity) {
        if (entity.getTardis() == null) return getTexture(entity);

        return ClientExteriorVariantRegistry.withParent(entity.getTardis().getExterior().getVariant()).emission();
    }
}
