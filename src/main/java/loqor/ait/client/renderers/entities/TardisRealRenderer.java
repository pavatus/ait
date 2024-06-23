package loqor.ait.client.renderers.entities;

import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.machines.ShieldsModel;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import loqor.ait.core.entities.TardisRealEntity;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.BiomeHandler;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

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
		Tardis tardis = entity.getTardis();

		if (tardis == null)
			return;

		TardisExterior tardisExterior = tardis.getExterior();
		ClientExteriorVariantSchema exteriorVariantSchema = ClientExteriorVariantRegistry.withParent(tardisExterior.getVariant());

		if (exteriorVariantSchema == null)
			return;

		Class<? extends ExteriorModel> modelClass = exteriorVariantSchema.model().getClass();

		if (model != null && !model.getClass().isInstance(modelClass))
			model = null;

		if (getModel(entity) == null || entity.getPlayer().isEmpty())
			return;

		Vec3d vec3d = entity.getRotationVec(tickDelta);
		Vec3d vec3d2 = entity.lerpVelocity(tickDelta);
		double d = vec3d2.horizontalLengthSquared();
		double e = vec3d.horizontalLengthSquared();

		matrices.push();
		if (d > 0.0 && e > 0.0) {
			double l = (vec3d2.x * vec3d.x + vec3d2.z * vec3d.z) / Math.sqrt(d * e);
			double m = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x;
			double v = Math.signum(m) * Math.acos(l);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) v));
		}

		if (!entity.isOnGround()) {
			if (!tardis.getDoor().isOpen()) {
				this.model.getPart().setAngles((float) 0, ((entity.getRotation(tickDelta)) * 4), 0);
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) (entity.getVelocity().horizontalLength() * 45f)));
			} else {
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-180f));
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) (-entity.getVelocity().horizontalLength() * 45f)));
			}
		} else {
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(
					(entity.getPlayer().get().getHorizontalFacing() == Direction.NORTH
							|| entity.getPlayer().get().getHorizontalFacing() == Direction.SOUTH) ?
							entity.getPlayer().get().getHorizontalFacing().asRotation() :
							-entity.getPlayer().get().getHorizontalFacing().asRotation()));
		}

		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180f));

		if (getModel(entity) == null)
			return;

		getModel(entity).renderRealWorld(entity, getModel(entity).getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(getTexture(entity))), entity.getWorld().getRegistryKey() == AITDimensions.TIME_VORTEX_WORLD ? LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE : light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);

		if (exteriorVariantSchema.emission() != null && tardis.engine().hasPower()) {
			boolean alarms = PropertiesHandler.getBool(tardis.getHandlers().getProperties(), PropertiesHandler.ALARM_ENABLED);
			getModel(entity).renderRealWorld(entity, getModel(entity).getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(getEmission(entity), true)), LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, 1, alarms ? 0.3f : 1, alarms ? 0.3f : 1, 1);
		}

		BiomeHandler biome = tardis.handler(TardisComponent.Id.BIOME);
		Identifier biomeTexture = exteriorVariantSchema.getBiomeTexture(biome.getBiomeKey());

		if (biomeTexture != null && !this.getTexture(entity).equals(biomeTexture)) {
			model.renderRealWorld(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(biomeTexture)), light, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1);
		}

		int maxLight = 0xF000F0;

		matrices.pop();
		if (tardis.areVisualShieldsActive()) {
			matrices.push();

			float delta = ((tickDelta + entity.age) * 0.03f);
			ShieldsModel shieldsModel = new ShieldsModel(ShieldsModel.getTexturedModelData().createModel());
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(new Identifier("textures/misc/forcefield.png"), delta % 1.0F, (delta * 0.1F) % 1.0F));
			shieldsModel.render(matrices, vertexConsumer, maxLight, OverlayTexture.DEFAULT_UV, 0f, 0.25f, 0.5f, 1f);
			matrices.pop();
		}
	}

	private ExteriorModel getModel(TardisRealEntity entity) {
		if (entity.getTardis() == null)
			return model;

		if (model == null)
			model = Objects.requireNonNull(ClientExteriorVariantRegistry.withParent(entity.getTardis().getExterior().getVariant())).model();

		return model;
	}

	public Identifier getEmission(TardisRealEntity entity) {
		if (entity.getTardis() == null)
			return getTexture(entity);

		return Objects.requireNonNull(ClientExteriorVariantRegistry.withParent(entity.getTardis().getExterior().getVariant())).emission();
	}
}