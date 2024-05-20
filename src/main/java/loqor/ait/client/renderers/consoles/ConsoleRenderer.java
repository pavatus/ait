package loqor.ait.client.renderers.consoles;

import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.registry.impl.console.variant.ClientConsoleVariantRegistry;
import loqor.ait.core.data.schema.console.ClientConsoleVariantSchema;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.tardis.data.SonicHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class ConsoleRenderer<T extends ConsoleBlockEntity> implements BlockEntityRenderer<T> {
	private ConsoleModel console;

	public ConsoleRenderer(BlockEntityRendererFactory.Context ctx) {
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (entity.findTardis().isEmpty()) return;

		ClientConsoleVariantSchema variant = ClientConsoleVariantRegistry.withParent(entity.getVariant());

		if (variant == null) return;

		Class<? extends ConsoleModel> modelClass = variant.model().getClass();

		if (console != null && console.getClass() != modelClass)
			console = null;

		if (console == null)
			this.console = variant.model();
		// BlockState blockState = entity.getCachedState();
		// float f = blockState.get(ConsoleBlock.FACING).asRotation();
		int maxLight = LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE;

		matrices.push();
		//matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(f));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
		if (console != null) {
			if (entity.findTardis().isEmpty()) return; // for some it forgets the tardis can be null, fucking weird
			console.animateBlockEntity(entity);
			console.renderWithAnimations(entity, this.console.getPart(), matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(variant.texture())), light, overlay, 1, 1, 1, 1);

			if (entity.findTardis().get().hasPower())
				console.renderWithAnimations(entity, this.console.getPart(), matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(variant.emission(), true)), maxLight, overlay, 1, 1, 1, 1);
		}
		matrices.pop();
		if (!entity.findTardis().get().sonic().hasSonic(SonicHandler.HAS_CONSOLE_SONIC)) return;
		ItemStack stack = entity.findTardis().get().sonic().get(SonicHandler.HAS_CONSOLE_SONIC);
		if(stack == null) return;
		matrices.push();
		matrices.translate(variant.sonicItemTranslations().x(), variant.sonicItemTranslations().y(), variant.sonicItemTranslations().z());
		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(variant.sonicItemRotations()[0]));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(variant.sonicItemRotations()[1]));
		matrices.scale(0.9f, 0.9f, 0.9f);
		int lightAbove = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
		MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
		matrices.pop();
	}

}
