package loqor.ait.client.renderers.machines;

import loqor.ait.core.blockentities.PlugBoardBlockEntity;
import loqor.ait.core.blocks.PlugBoardBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.shape.VoxelShape;

public class PlugBoardRenderer<T extends PlugBoardBlockEntity> implements BlockEntityRenderer<T> {

	private final MinecraftClient client = MinecraftClient.getInstance();

	public PlugBoardRenderer(BlockEntityRendererFactory.Context ctx) {

	}

	@Override
	public void render(PlugBoardBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
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
	}
}