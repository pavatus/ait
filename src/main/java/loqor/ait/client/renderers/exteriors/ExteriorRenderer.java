package loqor.ait.client.renderers.exteriors;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.SiegeModeModel;
import loqor.ait.client.models.machines.ShieldsModel;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.BiomeHandler;
import loqor.ait.tardis.data.OvergrownData;
import loqor.ait.tardis.data.SonicHandler;
import loqor.ait.tardis.data.StatsData;
import loqor.ait.tardis.data.properties.PropertiesHandler;
import loqor.ait.core.data.AbsoluteBlockPos;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class ExteriorRenderer<T extends ExteriorBlockEntity> implements BlockEntityRenderer<T> {
	private ExteriorModel model;
	private SiegeModeModel siege;
	private ShieldsModel shieldsModel;

	public ExteriorRenderer(BlockEntityRendererFactory.Context ctx) {}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (entity.findTardis().isEmpty())
			return;

		AbsoluteBlockPos.Directed exteriorPos = entity.findTardis().get().getHandlers().getExteriorPos();

		if (exteriorPos == null)
			return;

		ClientExteriorVariantSchema exteriorVariant = ClientExteriorVariantRegistry.withParent(entity.findTardis().get().getExterior().getVariant());
		TardisExterior tardisExterior = entity.findTardis().get().getExterior();

		if (tardisExterior == null || exteriorVariant == null) return;

		Class<? extends ExteriorModel> modelClass = exteriorVariant.model().getClass();

		if (model != null && !(model.getClass().isInstance(modelClass)))
			model = null;

		if (model == null)
			this.model = exteriorVariant.model();

		BlockState blockState = entity.getCachedState();
		float f = blockState.get(ExteriorBlock.FACING).asRotation();
		int maxLight = 0xF000F0;

		matrices.push();
		matrices.translate(0.5, 0, 0.5);

		if (MinecraftClient.getInstance().player == null) return;

		Identifier texture = exteriorVariant.texture();
		Identifier emission = exteriorVariant.emission();

		float wrappedDegrees = MathHelper.wrapDegrees(MinecraftClient.getInstance().player.getHeadYaw() +
				(exteriorPos.getDirection() == Direction.NORTH || exteriorPos.getDirection() == Direction.SOUTH ? f + 180f : f));

		if (exteriorVariant.equals(ClientExteriorVariantRegistry.DOOM)) {
			texture = DoomConstants.getTextureForRotation(wrappedDegrees, entity.findTardis().get());
			emission = DoomConstants.getEmissionForRotation(DoomConstants.getTextureForRotation(wrappedDegrees, entity.findTardis().get()), entity.findTardis().get());
		}

		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(!exteriorVariant.equals(ClientExteriorVariantRegistry.DOOM) ? f :
				MinecraftClient.getInstance().player.getHeadYaw() + ((wrappedDegrees > -135 && wrappedDegrees < 135) ? 180f : 0f)));

		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
		try {
			if (entity.findTardis().get().isSiegeMode()) {
				if (siege == null) siege = new SiegeModeModel(SiegeModeModel.getTexturedModelData().createModel());
				siege.renderWithAnimations(entity, this.siege.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(SiegeModeModel.TEXTURE)), maxLight, overlay, 1, 1, 1, 1);
				matrices.pop();
				return;
			}
		} catch (Exception e) {
			AITMod.LOGGER.error("Failed to render siege mode", e);
		}

		String name = entity.findTardis().get().<StatsData>handler(TardisComponent.Id.STATS).getName();
		if (name.equalsIgnoreCase("grumm") || name.equalsIgnoreCase("dinnerbone")) {
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-180f));
		}

		if (model != null) {
			model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(texture)), light, overlay, 1, 1, 1, 1);
			// @TODO uhhh, should we make it so the biome textures are the overgrowth per biome, or should they be separate? - Loqor
			if (entity.findTardis().get().<OvergrownData>handler(TardisComponent.Id.OVERGROWN).isOvergrown()) {
				model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(entity.findTardis().get().<OvergrownData>handler(TardisComponent.Id.OVERGROWN).getOvergrownTexture())), light, overlay, 1, 1, 1, 1);
			}
			if(entity.findTardis().get().<BiomeHandler>handler(TardisComponent.Id.BIOME).getBiomeKey() != null && !exteriorVariant.equals(ClientExteriorVariantRegistry.CORAL_GROWTH)) {
				Identifier biomeTexture = BiomeHandler.biomeTypeFromKey(entity.findTardis().get().<BiomeHandler>handler(TardisComponent.Id.BIOME).getBiomeKey(), exteriorVariant.texture(), entity.findTardis().get());
				if (!texture.equals(biomeTexture)) {
					model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(biomeTexture)), light, overlay, 1, 1, 1, 1);
				}
			}
			if (emission != null && entity.findTardis().get().hasPower()) {
				boolean alarms = PropertiesHandler.getBool(entity.findTardis().get().properties(), PropertiesHandler.ALARM_ENABLED);

				model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(emission, true)), maxLight, overlay, 1, alarms ? 0.3f : 1, alarms ? 0.3f : 1, 1);
			}
		}
		matrices.pop();

		if (entity.findTardis().get().areVisualShieldsActive()) {
			matrices.push();
			matrices.translate(0.5F, 0.0F, 0.5F);
			float delta = ((tickDelta + MinecraftClient.getInstance().player.age) * 0.03f);
			if(shieldsModel == null) shieldsModel = new ShieldsModel(ShieldsModel.getTexturedModelData().createModel());
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(this.getEnergySwirlTexture(), delta % 1.0F, (delta * 0.1F) % 1.0F));
			shieldsModel.render(matrices, vertexConsumer, maxLight, overlay, 0f, 0.25f, 0.5f, 1f);
			matrices.pop();
		}

		if (!entity.findTardis().get().sonic().hasSonic(SonicHandler.HAS_EXTERIOR_SONIC)) return;
		ItemStack stack = entity.findTardis().get().sonic().get(SonicHandler.HAS_EXTERIOR_SONIC);
		if (stack == null || entity.getWorld() == null) return;
		matrices.push();
		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f + exteriorVariant.sonicItemRotations()[0]), (float) entity.getPos().toCenterPos().x - entity.getPos().getX(), (float) entity.getPos().toCenterPos().y - entity.getPos().getY(), (float) entity.getPos().toCenterPos().z - entity.getPos().getZ());
		matrices.translate(exteriorVariant.sonicItemTranslations().x(), exteriorVariant.sonicItemTranslations().y(), exteriorVariant.sonicItemTranslations().z());
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(exteriorVariant.sonicItemRotations()[1]));
		matrices.scale(0.9f, 0.9f, 0.9f);
		int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
		matrices.pop();
	}

	@Override
	public boolean rendersOutsideBoundingBox(T blockEntity) {
		return true;
	}

	public Identifier getEnergySwirlTexture() {
		return new Identifier("textures/misc/forcefield.png");
	}
}
