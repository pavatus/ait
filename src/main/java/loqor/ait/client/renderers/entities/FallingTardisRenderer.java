package loqor.ait.client.renderers.entities;

import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.SiegeModeModel;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.entities.FallingTardisEntity;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.BiomeHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationPropertyHelper;

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

		if (exteriorVariant == null) return;
		Class<? extends ExteriorModel> modelClass = exteriorVariant.model().getClass();

		if (model != null && !(model.getClass().isInstance(modelClass))) // fixme this is bad it seems to constantly create a new one anyway but i didnt realise.
			model = null;

		if (MinecraftClient.getInstance().player == null) return;

		if (getModel(entity) == null) return;

		matrices.push();
		int k = entity.getBlockState().get(ExteriorBlock.ROTATION);
		float h = RotationPropertyHelper.toDegrees(k);
		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(!exteriorVariant.equals(ClientExteriorVariantRegistry.DOOM) ? 180f + h : MinecraftClient.getInstance().player.getHeadYaw() + 180f));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));

		if (entity.getTardis().isSiegeMode()) {
			model = new SiegeModeModel(SiegeModeModel.getTexturedModelData().createModel());
			model.renderFalling(entity, getModel(entity).getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(SiegeModeModel.TEXTURE)), light, 1, 1, 1, 1, 1);
		} else {
			getModel(entity).renderFalling(entity, getModel(entity).getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(getTexture(entity))), light, 1, 1, 1, 1, 1);
			if (exteriorVariant.emission() != null)
				getModel(entity).renderFalling(entity, getModel(entity).getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(getEmission(entity), true)), light, 1, 1, 1, 1, 1);
			if(entity.getTardis().<BiomeHandler>handler(TardisComponent.Id.BIOME).getBiomeKey() != null && !exteriorVariant.equals(ClientExteriorVariantRegistry.CORAL_GROWTH)) {
				Identifier biomeTexture = BiomeHandler.biomeTypeFromKey(entity.getTardis().<BiomeHandler>handler(TardisComponent.Id.BIOME).getBiomeKey(), exteriorVariant.texture(), entity.getTardis());
				if (!exteriorVariant.texture().equals(biomeTexture)) {
					model.renderFalling(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(biomeTexture)), light, 1, 1, 1, 1, 1);
				}
			}
		}
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
		if (entity.getTardis() == null)
			return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE; // random texture just so i dont crash

		return ClientExteriorVariantRegistry.withParent(entity.getTardis().getExterior().getVariant()).texture();
	}

	public Identifier getEmission(FallingTardisEntity entity) {
		if (entity.getTardis() == null) return getTexture(entity);

		return ClientExteriorVariantRegistry.withParent(entity.getTardis().getExterior().getVariant()).emission();
	}
}
