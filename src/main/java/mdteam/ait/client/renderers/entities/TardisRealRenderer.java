package mdteam.ait.client.renderers.entities;

import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.client.registry.ClientExteriorVariantRegistry;
import mdteam.ait.client.registry.exterior.ClientExteriorVariantSchema;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.core.entities.TardisRealEntity;
import mdteam.ait.tardis.TardisExterior;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.util.Objects;

public class TardisRealRenderer extends EntityRenderer<TardisRealEntity> {
	private ExteriorModel model;

	public TardisRealRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	@Override
	public Identifier getTexture(TardisRealEntity entity) {
		if (entity.getTardis() == null)
			return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE; // random texture just so i dont crash

		return Objects.requireNonNull(ClientExteriorVariantRegistry.withParent(entity.getTardis().getExterior().getVariant())).texture();
	}

	@Override
	public void render(TardisRealEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
		if (entity.getTardis() == null) return;
		TardisExterior tardisExterior = entity.getTardis().getExterior();
		ClientExteriorVariantSchema exteriorVariantSchema = ClientExteriorVariantRegistry.withParent(tardisExterior.getVariant());

		if (exteriorVariantSchema == null) return;
		Class<? extends ExteriorModel> modelClass = exteriorVariantSchema.model().getClass();

		if (model != null && !model.getClass().isInstance(modelClass)) model = null;

		if (getModel(entity) == null || entity.getPlayer().isEmpty()) return;

		matrices.push();
		//Vec3d rotationVector = entity.getRotationVector();
		//float pitch = (float) Math.toRadians(rotationVector.getX());
		//float yawE = (float) Math.toRadians(rotationVector.getY());
		//float roll = (float) Math.toRadians(rotationVector.getZ());

		//Quaternionf quaternion = new Quaternionf();
		//quaternion.rotationXYZ(pitch, yawE, roll);
		//matrices.multiply(quaternion);
		//matrices.scale(1.0f, 1.0f, -1.0f);
		//matrices.scale(1.0f, -1.0f, 1.0f);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getPlayer().get().isOnGround() ? entity.getPlayer().get().getHorizontalFacing().getOpposite().asRotation() : entity.getRotation(tickDelta) * 40));
		matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(entity.getPitch()));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180f));


		if (getModel(entity) == null) return;
		getModel(entity).renderRealWorld(entity, getModel(entity).getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(getTexture(entity))), light, 1, 1, 1, 1, 1);

		if (exteriorVariantSchema.emission() != null) {
			getModel(entity).renderRealWorld(entity, getModel(entity).getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(getEmission(entity), true)), light, 1, 1, 1, 1, 1);
		}

		matrices.pop();
	}

	private ExteriorModel getModel(TardisRealEntity entity) {
		if (entity.getTardis() == null) return model;
		if (model == null) {
			model = Objects.requireNonNull(ClientExteriorVariantRegistry.withParent(entity.getTardis().getExterior().getVariant())).model();
		}

		return model;
	}

	public Identifier getEmission(TardisRealEntity entity) {
		if (entity.getTardis() == null) return getTexture(entity);

		return Objects.requireNonNull(ClientExteriorVariantRegistry.withParent(entity.getTardis().getExterior().getVariant())).emission();
	}
}
