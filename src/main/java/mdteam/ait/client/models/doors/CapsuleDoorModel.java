package mdteam.ait.client.models.doors;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class CapsuleDoorModel extends DoorModel {

	public ModelPart body;
	public CapsuleDoorModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.body = root.getChild("body");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create(), ModelTransform.of(0.0F, 3.0F, -15.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData top = body.addChild("top", ModelPartBuilder.create().uv(31, 49).cuboid(-12.0F, -36.1F, -12.0F, 24.0F, 0.0F, 10.0F, new Dilation(0.0F))
		.uv(31, 38).cuboid(-12.0F, -33.89F, -12.0F, 24.0F, 0.0F, 10.0F, new Dilation(0.0F))
		.uv(60, 22).cuboid(-4.9706F, -36.0F, -12.0F, 9.9411F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, 0.0F));

		ModelPartData octagon_r1 = top.addChild("octagon_r1", ModelPartBuilder.create().uv(29, 11).cuboid(-4.9706F, -2.0F, -12.0F, 3.0F, 2.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -34.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData octagon_r2 = top.addChild("octagon_r2", ModelPartBuilder.create().uv(76, 70).cuboid(-4.9706F, -2.0F, -12.0F, 9.9411F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -34.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		ModelPartData octagon_r3 = top.addChild("octagon_r3", ModelPartBuilder.create().uv(76, 77).cuboid(-4.9706F, -2.0F, -12.0F, 9.9411F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -34.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		ModelPartData middle = body.addChild("middle", ModelPartBuilder.create().uv(0, 0).cuboid(-12.0F, -34.0F, -4.0F, 24.0F, 32.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, 0.0F));

		ModelPartData octagon_r4 = middle.addChild("octagon_r4", ModelPartBuilder.create().uv(55, 60).cuboid(-2.2365F, -34.0F, 9.5F, 8.0F, 32.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, -0.7854F, 3.1416F));

		ModelPartData octagon_r5 = middle.addChild("octagon_r5", ModelPartBuilder.create().uv(0, 65).cuboid(-5.7635F, -34.0F, 9.5F, 8.0F, 32.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.7854F, 3.1416F));

		ModelPartData bottom = body.addChild("bottom", ModelPartBuilder.create().uv(76, 60).cuboid(-4.9706F, -2.0F, -12.0F, 9.9411F, 2.0F, 7.0F, new Dilation(0.0F))
		.uv(60, 11).cuboid(-12.0F, 0.01F, -12.0F, 24.0F, 0.0F, 10.0F, new Dilation(0.0F))
		.uv(53, 0).cuboid(-12.0F, -2.1F, -12.0F, 24.0F, 0.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, 0.0F));

		ModelPartData octagon_r6 = bottom.addChild("octagon_r6", ModelPartBuilder.create().uv(0, 38).cuboid(-4.9706F, -2.0F, -12.0F, 3.0F, 2.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData octagon_r7 = bottom.addChild("octagon_r7", ModelPartBuilder.create().uv(90, 35).cuboid(-4.9706F, -2.0F, -12.0F, 9.9411F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		ModelPartData octagon_r8 = bottom.addChild("octagon_r8", ModelPartBuilder.create().uv(76, 84).cuboid(-4.9706F, -2.0F, -12.0F, 9.9411F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		ModelPartData doors = body.addChild("doors", ModelPartBuilder.create(), ModelTransform.of(0.0F, -2.0F, -17.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData right_door = doors.addChild("right_door", ModelPartBuilder.create().uv(36, 65).cuboid(0.4706F, -11.0F, -0.5F, 6.0F, 32.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-6.5F, 0.0F, -8.5F));

		ModelPartData left_door = doors.addChild("left_door", ModelPartBuilder.create().uv(21, 65).cuboid(-6.5294F, -11.0F, -0.5F, 6.0F, 32.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(6.5F, 0.0F, -8.5F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void renderWithAnimations(DoorBlockEntity door, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		matrices.push();
		matrices.translate(0, -1.5f, 0);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));

		this.body.getChild("doors").getChild("left_door").yaw = door.getLeftDoorRotation();
		this.body.getChild("doors").getChild("right_door").yaw = -door.getRightDoorRotation();

		super.renderWithAnimations(door, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);
		matrices.pop();
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		this.body.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getPart() {
		return this.body;
	}
}