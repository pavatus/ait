package loqor.ait.client.renderers.exteriors;

import loqor.ait.AITMod;
import loqor.ait.client.models.exteriors.ExteriorModel;
import loqor.ait.client.models.exteriors.SiegeModeModel;
import loqor.ait.client.models.machines.ShieldsModel;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.client.util.ClientLightUtil;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.data.AbsoluteBlockPos;
import loqor.ait.core.data.schema.exterior.ClientExteriorVariantSchema;
import loqor.ait.registry.impl.exterior.ClientExteriorVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.TardisExterior;
import loqor.ait.tardis.base.TardisComponent;
import loqor.ait.tardis.data.BiomeHandler;
import loqor.ait.tardis.data.OvergrownData;
import loqor.ait.tardis.data.SonicHandler;
import loqor.ait.tardis.link.v2.TardisRef;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.profiler.Profiler;

public class ExteriorRenderer<T extends ExteriorBlockEntity> implements BlockEntityRenderer<T> {

	private ExteriorModel model;
	private static final SiegeModeModel siege = new SiegeModeModel(SiegeModeModel.getTexturedModelData().createModel());
	private static final ShieldsModel shieldsModel = new ShieldsModel(ShieldsModel.getTexturedModelData().createModel());;

	public ExteriorRenderer(BlockEntityRendererFactory.Context ctx) { }

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		Profiler profiler = entity.getWorld().getProfiler();
		profiler.push("exterior");

		profiler.push("find_tardis");
		TardisRef optionalTardis = entity.tardis();

		if (optionalTardis == null || optionalTardis.isEmpty())
			return;

		Tardis tardis = optionalTardis.get();
		profiler.swap("render");

		this.renderExterior(profiler, tardis, entity, tickDelta, matrices, vertexConsumers, light, overlay);
		profiler.pop();

		profiler.pop();
	}

	private void renderExterior(Profiler profiler, Tardis tardis, T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (tardis.siege().isActive()) {
			profiler.push("siege");

			siege.renderWithAnimations(entity, siege.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(SiegeModeModel.TEXTURE)), light, overlay, 1, 1, 1, 1);

			matrices.pop();
			profiler.pop();
			return;
		}

		AbsoluteBlockPos.Directed exteriorPos = tardis.getExteriorPos();

		if (exteriorPos == null) {
			profiler.pop();
			return;
		}

		ClientExteriorVariantSchema exteriorVariant = ClientExteriorVariantRegistry.withParent(tardis.getExterior().getVariant());
		TardisExterior tardisExterior = tardis.getExterior();

		if (tardisExterior == null || exteriorVariant == null) {
			profiler.pop();
			return;
		}

		Class<? extends ExteriorModel> modelClass = exteriorVariant.model().getClass();

		if (model != null && !(model.getClass().isInstance(modelClass)))
			model = null;

		if (model == null)
			this.model = exteriorVariant.model();

		BlockState blockState = entity.getCachedState();
		int k = blockState.get(ExteriorBlock.ROTATION);
		float h = RotationPropertyHelper.toDegrees(k);

		final float alpha = entity.getAlpha();

		if (tardis.areVisualShieldsActive()) {
			profiler.push("shields");

			float delta = (tickDelta + MinecraftClient.getInstance().player.age) * 0.03f;
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEnergySwirl(
					this.getEnergySwirlTexture(), delta % 1.0F, (delta * 0.1F) % 1.0F)
			);

			matrices.push();
			matrices.translate(0.5F, 0.0F, 0.5F);

			shieldsModel.render(matrices, vertexConsumer, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay,
					0f, 0.25f, 0.5f, alpha
			);

			matrices.pop();
			profiler.pop();
		}

		matrices.push();
		matrices.translate(0.5, 0, 0.5);

		if (MinecraftClient.getInstance().player == null) {
			profiler.pop();
			return;
		}

		Identifier texture = exteriorVariant.texture();
		Identifier emission = exteriorVariant.emission();

		float wrappedDegrees = MathHelper.wrapDegrees(MinecraftClient.getInstance().player.getHeadYaw() + h);

		if (exteriorVariant.equals(ClientExteriorVariantRegistry.DOOM)) {
			texture = DoomConstants.getTextureForRotation(wrappedDegrees, tardis);
			emission = DoomConstants.getEmissionForRotation(DoomConstants.getTextureForRotation(wrappedDegrees, tardis), tardis);
		}

		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(!exteriorVariant.equals(ClientExteriorVariantRegistry.DOOM) ? h + 180f :
				MinecraftClient.getInstance().player.getHeadYaw() + ((wrappedDegrees > -135 && wrappedDegrees < 135) ? 180f : 0f)));

		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));

		if (model == null) {
			profiler.pop();
			return;
		}

		String name = tardis.stats().getName();
		if (name.equalsIgnoreCase("grumm") || name.equalsIgnoreCase("dinnerbone")) {
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-180f));
		}

		model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(
				AITRenderLayers.getEntityTranslucentCull(texture)
		), light, overlay, 1, 1, 1, alpha);

		// @TODO uhhh, should we make it so the biome textures are the overgrowth per biome, or should they be separate? - Loqor
		if (tardis.<OvergrownData>handler(TardisComponent.Id.OVERGROWN).isOvergrown()) {
			model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityTranslucentCull(tardis.<OvergrownData>handler(TardisComponent.Id.OVERGROWN).getOvergrownTexture())), light, overlay, 1, 1, 1, alpha);
		}

		if (emission != null) {
			profiler.push("emission");
			boolean alarms = tardis.alarm().isEnabled();

			ClientLightUtil.renderEmissivable(
					tardis.engine().hasPower(), model::renderWithAnimations, emission, entity, this.model.getPart(),
					matrices, vertexConsumers, light, overlay, 1, alarms ? 0.3f : 1, alarms ? 0.3f : 1, alpha
			);

			profiler.pop();
		}

		profiler.push("biome");

		if (tardis.<BiomeHandler>handler(TardisComponent.Id.BIOME).getBiomeKey() != null && !exteriorVariant.equals(ClientExteriorVariantRegistry.CORAL_GROWTH)) {
			Identifier biomeTexture = exteriorVariant.getBiomeTexture(BiomeHandler.getBiomeTypeFromKey(tardis.<BiomeHandler>handler(TardisComponent.Id.BIOME).getBiomeKey()));
			if (biomeTexture != null && !texture.equals(biomeTexture)) {
				model.renderWithAnimations(entity, this.model.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.getEntityCutoutNoCullZOffset(biomeTexture)), light, overlay, 1, 1, 1, alpha);
			}
		}

		profiler.pop();
		matrices.pop();

		if (!tardis.sonic().hasSonic(SonicHandler.HAS_EXTERIOR_SONIC)) {
			profiler.pop();
			return;
		}

		profiler.push("sonic");
		ItemStack stack = tardis.sonic().get(SonicHandler.HAS_EXTERIOR_SONIC);

		if (stack == null || entity.getWorld() == null) {
			profiler.pop();
			return;
		}

		matrices.push();
		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180f + h + exteriorVariant.sonicItemRotations()[0]), (float) entity.getPos().toCenterPos().x - entity.getPos().getX(), (float) entity.getPos().toCenterPos().y - entity.getPos().getY(), (float) entity.getPos().toCenterPos().z - entity.getPos().getZ());
		matrices.translate(exteriorVariant.sonicItemTranslations().x(), exteriorVariant.sonicItemTranslations().y(), exteriorVariant.sonicItemTranslations().z());
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(exteriorVariant.sonicItemRotations()[1]));
		matrices.scale(0.9f, 0.9f, 0.9f);

		int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);

		matrices.pop();
		profiler.pop();
	}

	@Override
	public boolean rendersOutsideBoundingBox(T blockEntity) {
		return true;
	}

	public Identifier getEnergySwirlTexture() {
		return new Identifier(AITMod.MOD_ID, "textures/environment/shields.png");
	}
}
