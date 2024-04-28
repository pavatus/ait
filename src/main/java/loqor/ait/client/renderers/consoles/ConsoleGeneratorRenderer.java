package loqor.ait.client.renderers.consoles;

import loqor.ait.AITMod;
import loqor.ait.client.models.consoles.ConsoleGeneratorModel;
import loqor.ait.client.models.consoles.ConsoleModel;
import loqor.ait.client.registry.ClientConsoleVariantRegistry;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.core.blockentities.ConsoleGeneratorBlockEntity;
import loqor.ait.tardis.Tardis;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class ConsoleGeneratorRenderer<T extends ConsoleGeneratorBlockEntity> implements BlockEntityRenderer<T> {

	private final ConsoleGeneratorModel generator;
    private final EntityRenderDispatcher dispatcher;

	public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/blockentities/consoles/console_generator/console_generator.png");

	public ConsoleGeneratorRenderer(BlockEntityRendererFactory.Context ctx) {
		this.dispatcher = ctx.getEntityRenderDispatcher();
		this.generator = new ConsoleGeneratorModel(ConsoleGeneratorModel.getTexturedModelData().createModel());
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

		if (ClientConsoleVariantRegistry.getInstance().get(entity.getConsoleVariant().id()) == null) return;

		int maxLight = 0xF000F0;

        ConsoleModel console = ClientConsoleVariantRegistry.getInstance().get(entity.getConsoleVariant().id()).model();
        Identifier consoleTexture = ClientConsoleVariantRegistry.getInstance().get(entity.getConsoleVariant().id()).texture();

        if(entity.findTardis().isPresent()) {
			Tardis tardis = entity.findTardis().get();
			if(!tardis.isUnlocked(entity.getConsoleVariant())) {
				matrices.push();

				matrices.translate(0.5F, 2.75F, 0.5F);
				matrices.multiply(this.dispatcher.getRotation());
				matrices.scale(-0.1F, -0.1F, 0.1F);
				Matrix4f matrix4f = matrices.peek().getPositionMatrix();
				Text text = Text.literal("\uD83D\uDD12");
				TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
				float h = (float) (-textRenderer.getWidth(text) / 2);
				textRenderer.draw(text, h + 0.35f, 0.0F, 0xFFFFFFFF, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0x000000, maxLight);

				matrices.pop();
			}
        }

		matrices.push();

		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
		matrices.translate(0.5f, -1.5f, -0.5f);

		this.generator.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, overlay, 1, 1, 1, 1);

		matrices.pop();

		matrices.push();

		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
		if (entity.getWorld() == null) return;
		matrices.translate(0.5f, -1.5f + entity.getWorld().random.nextFloat() * 0.02, -0.5f);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MinecraftClient.getInstance().getTickDelta() % 180));

		if(entity.findTardis().isPresent()) {
			Tardis tardis = entity.findTardis().get();

			if (tardis.isUnlocked(entity.getConsoleVariant())) {
				console.render(matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(consoleTexture, true)), maxLight, OverlayTexture.DEFAULT_UV, 0.3607843137f, 0.9450980392f, 1, entity.getWorld().random.nextInt(32) != 6 ? 0.4f : 0.05f);
				return;
			}

			console.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(consoleTexture)), maxLight, OverlayTexture.DEFAULT_UV, 0.2f, 0.2f, 0.2f, entity.getWorld().random.nextInt(32) != 6 ? 0.4f : 0.05f);
		}
		matrices.pop();
	}
}
