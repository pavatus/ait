package loqor.ait.client.renderers.machines;

import loqor.ait.AITMod;
import loqor.ait.client.models.machines.EngineModel;
import loqor.ait.client.renderers.AITRenderLayers;
import loqor.ait.core.AITDimensions;
import loqor.ait.core.blockentities.EngineBlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class EngineRenderer<T extends EngineBlockEntity> implements BlockEntityRenderer<T> {

	public static final Identifier ENGINE_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/machines/engine.png"));
	public static final Identifier EMISSIVE_ENGINE_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/machines/engine_emission.png"));
	private final EngineModel engineModel;

	public EngineRenderer(BlockEntityRendererFactory.Context ctx) {
		this.engineModel = new EngineModel(EngineModel.getTexturedModelData().createModel());
	}

	@Override
	public void render(EngineBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if(entity.hasWorld() && entity.getWorld().getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD && entity.findTardis().isEmpty())
			return;

		matrices.push();
		matrices.translate(0.5f, 1.5f, 0.5f);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

		this.engineModel.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(ENGINE_TEXTURE)), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		if (entity.getWorld().getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD) {
			if (entity.findTardis().get().engine().hasPower()) {
				this.engineModel.render(matrices, vertexConsumers.getBuffer(AITRenderLayers.tardisRenderEmissionCull(EMISSIVE_ENGINE_TEXTURE, true)), 0xF000F00, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
			}
		}

		matrices.pop();
	}
}