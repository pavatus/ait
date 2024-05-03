package loqor.ait.client.renderers.machines;

import loqor.ait.AITMod;
import loqor.ait.client.models.machines.PlugboardModel;
import loqor.ait.core.blockentities.PlugBoardBlockEntity;
import loqor.ait.core.blocks.PlugBoardBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.shape.VoxelShape;

public class PlugBoardRenderer<T extends PlugBoardBlockEntity> implements BlockEntityRenderer<T> {

	private final MinecraftClient client = MinecraftClient.getInstance();
	PlugboardModel model;
	public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/blockentities/machines/plugboard.png");

	public PlugBoardRenderer(BlockEntityRendererFactory.Context ctx) {
		this.model = new PlugboardModel(PlugboardModel.getTexturedModelData().createModel());
	}

	@Override
	public void render(PlugBoardBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		BlockState state = entity.getCachedState();
		for (int i = 0; i < 12; i++) {
			ItemStack link = entity.getLink(i);

			if (link == null)
				continue;

			matrices.push();
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

			PlugBoardBlock.Handle<?> handle = PlugBoardBlock.HANDLES[i];
			VoxelShape shape = handle.shape().get(entity.getCachedState().get(PlugBoardBlock.FACING));

			matrices.translate(
					shape.getMin(Direction.Axis.X),
					shape.getMin(Direction.Axis.Y),
					shape.getMin(Direction.Axis.Z)
			);

			this.client.getItemRenderer().renderItem(
					link, ModelTransformationMode.FIXED, light, overlay,
					matrices, vertexConsumers, entity.getWorld(), 0
			);

			matrices.pop();
		}
		matrices.push();
		matrices.translate(0.5, 1.5, 0.5);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.get(PlugBoardBlock.FACING).asRotation()));
		this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay, 1, 1, 1, 1);
		matrices.pop();
	}
}