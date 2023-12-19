package mdteam.ait.client.renderers.entities;

import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.blocks.ExteriorBlock;
import mdteam.ait.core.entities.FallingTardisEntity;
import mdteam.ait.tardis.TardisExterior;
import mdteam.ait.tardis.handler.properties.PropertiesHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.FallingBlockEntity;
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

        if (entity.tardis() == null) {
            return;
        }

        TardisExterior tardisExterior = entity.tardis().getExterior();

        if (tardisExterior == null) return;

        Class<? extends ExteriorModel> modelClass = tardisExterior.getType().getModelClass();

        if (model != null && !(model.getClass().isInstance(modelClass))) // fixme this is bad it seems to constantly create a new one anyway but i didnt realise.
            model = null;


        int maxLight = 0xFFFFFF;
        matrices.push();
        // matrices.translate(0.5, 0, 0.5);
        float f = entity.getBlockState().get(ExteriorBlock.FACING).asRotation();
        matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));

        if (getModel(entity) == null) return;

        getModel(entity).renderFalling(entity, getModel(entity).getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(getTexture(entity))), light,1,1,1,1,1);

        if (model.getVariousEmission(getTexture(entity), tardisExterior.getType()) != null)
            getModel(entity).renderFalling(entity, getModel(entity).getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(getEmission(entity), true)), light,1,1,1,1,1);

        matrices.pop();
    }

    private ExteriorModel getModel(FallingTardisEntity entity) {
        if (model == null && entity.tardis() != null) {
            model = entity.tardis().getExterior().getType().createModel();
        }

        return model;
    }

    @Override
    public Identifier getTexture(FallingTardisEntity entity) {
        if (entity.tardis() == null) return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE; // random texture just so i dont crash

        return model.getVariousTextures(entity.tardis().getExterior().getType(), entity.tardis().getExterior().getVariant());
    }

    public Identifier getEmission(FallingTardisEntity entity) {
        if (entity.tardis() == null) return getTexture(entity);

        return model.getVariousEmission(getTexture(entity), entity.tardis().getExterior().getType());
    }
}
