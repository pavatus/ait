package loqor.ait.client.models.machines;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class EngineModel extends SinglePartEntityModel {
	private final ModelPart bone;
	public EngineModel(ModelPart root) {
		this.bone = root.getChild("bone");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 25).cuboid(-8.0F, -7.0F, -8.0F, 16.0F, 8.0F, 16.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-8.0F, -47.0F, -8.0F, 16.0F, 8.0F, 16.0F, new Dilation(0.0F))
		.uv(41, 50).cuboid(-5.0F, -15.0F, -5.0F, 10.0F, 8.0F, 10.0F, new Dilation(0.0F))
		.uv(0, 50).cuboid(-5.0F, -39.0F, -5.0F, 10.0F, 8.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 23.0F, 0.0F));

		ModelPartData bone2 = bone.addChild("bone2", ModelPartBuilder.create().uv(57, 17).cuboid(-4.0F, -27.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(36, 69).cuboid(-1.0F, -31.0F, -1.0F, 2.0F, 16.0F, 2.0F, new Dilation(0.0F))
		.uv(27, 69).cuboid(-3.5F, -31.0F, -1.0F, 2.0F, 16.0F, 2.0F, new Dilation(0.0F))
		.uv(18, 69).cuboid(1.5F, -31.0F, -1.0F, 2.0F, 16.0F, 2.0F, new Dilation(0.0F))
		.uv(9, 69).cuboid(-1.0F, -31.0F, 1.5F, 2.0F, 16.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 69).cuboid(-1.0F, -31.0F, -3.5F, 2.0F, 16.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public ModelPart getPart() {
		return bone;
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
}