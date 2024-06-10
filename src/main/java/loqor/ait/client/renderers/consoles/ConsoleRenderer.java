package loqor.ait.client.renderers.consoles;

import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.client.util.ClientLightUtil;
import loqor.ait.core.blockentities.ConsoleBlockEntity;
import loqor.ait.core.data.schema.console.ClientConsoleVariantSchema;
import loqor.ait.registry.impl.console.variant.ClientConsoleVariantRegistry;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.data.SonicHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

import java.util.Optional;

public class ConsoleRenderer<T extends ConsoleBlockEntity> implements BlockEntityRenderer<T> {
	private ConsoleModel console;

	public ConsoleRenderer(BlockEntityRendererFactory.Context ctx) { }

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		this.renderConsole(entity, matrices, vertexConsumers, light, overlay);
		entity.getWorld().getProfiler().swap("console");
	}

	private void renderConsole(T entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		Optional<Tardis> optionalTardis = entity.findTardis();

		if (optionalTardis.isEmpty())
			return;

		Tardis tardis = optionalTardis.get();

		ClientConsoleVariantSchema variant = ClientConsoleVariantRegistry.withParent(entity.getVariant());

		if (variant == null)
			return;

		Class<? extends ConsoleModel> modelClass = variant.model().getClass();

		if (console != null && console.getClass() != modelClass)
			console = null;

		if (console == null)
			this.console = variant.model();

		matrices.push();
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));

		console.animateBlockEntity(entity);
		console.renderWithAnimations(entity, this.console.getPart(), matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentCull(variant.texture())), light, overlay, 1, 1, 1, 1);

		boolean hasPower = tardis.engine().hasPower();

		ClientLightUtil.renderEmissivable(
				hasPower, console::renderWithAnimations, variant.noEmission(), variant.emission(), entity,
				this.console.getPart(), matrices, vertexConsumers, light, overlay, 1, 1, 1, 1
		);

		matrices.pop();

		if (!tardis.sonic().hasSonic(SonicHandler.HAS_CONSOLE_SONIC))
			return;

		ItemStack stack = tardis.sonic().get(SonicHandler.HAS_CONSOLE_SONIC);

		if (stack == null)
			return;

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
