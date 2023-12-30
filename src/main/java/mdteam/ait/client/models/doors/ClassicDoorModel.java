package mdteam.ait.client.models.doors;

import mdteam.ait.client.animation.exterior.door.DoorAnimations;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.tardis.handler.DoorHandler;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

// Made with Blockbench 4.9.2
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class ClassicDoorModel extends DoorModel {
	private final ModelPart classic;
	public ClassicDoorModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.classic = root.getChild("classic");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData classic = modelPartData.addChild("classic", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 26.0F, 9.0F));

		ModelPartData Posts = classic.addChild("Posts", ModelPartBuilder.create().uv(220, 162).cuboid(-17.0F, -62.0F, -17.0F, 4.0F, 60.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = Posts.addChild("cube_r1", ModelPartBuilder.create().uv(203, 162).cuboid(-17.0F, -64.0F, -18.0F, 4.0F, 60.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData Doors = classic.addChild("Doors", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData right_door = Doors.addChild("right_door", ModelPartBuilder.create().uv(0, 189).cuboid(-0.5F, -25.5F, -0.5F, 13.0F, 53.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 244).cuboid(2.5F, -10.5F, 0.5F, 8.0F, 10.0F, 3.0F, new Dilation(0.0F))
		.uv(203, 76).cuboid(-0.5F, -25.5F, -1.0F, 13.0F, 53.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(-12.5F, -29.5F, -14.5F));

		ModelPartData left_door = Doors.addChild("left_door", ModelPartBuilder.create().uv(194, 21).cuboid(-12.5F, -25.5F, -0.5F, 13.0F, 53.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 29).cuboid(-11.5F, -0.5F, -1.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(5, 29).cuboid(-8.0F, -7.0F, -0.51F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F))
		.uv(1, 258).cuboid(-10.5F, -10.5F, 0.5F, 8.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(12.5F, -29.5F, -14.5F));

		ModelPartData Walls = classic.addChild("Walls", ModelPartBuilder.create().uv(29, 106).cuboid(-13.0F, -58.0F, -15.0F, 26.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(109, 0).cuboid(-13.0F, -58.0F, -15.5F, 26.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData PCB = classic.addChild("PCB", ModelPartBuilder.create().uv(237, 162).cuboid(-14.0F, -61.0F, -18.0F, 28.0F, 4.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, 0.0F));
		return TexturedModelData.of(modelData, 512, 512);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		classic.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public Animation getAnimationForDoorState(DoorHandler.DoorStateEnum state) {
		return switch (state) {
			case CLOSED -> DoorAnimations.INTERIOR_BOTH_CLOSE_ANIMATION;
			case FIRST -> DoorAnimations.INTERIOR_FIRST_OPEN_ANIMATION;
			case SECOND -> DoorAnimations.INTERIOR_SECOND_OPEN_ANIMATION;
			case BOTH -> DoorAnimations.INTERIOR_BOTH_OPEN_ANIMATION;
		};
	}

	@Override
	public ModelPart getPart() {
		return classic;
	}

	@Override
	public void renderWithAnimations(DoorBlockEntity doorEntity, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		/*this.classic.getChild("Doors").getChild("right_door").yaw = -doorEntity.getRightDoorRotation();
		this.classic.getChild("Doors").getChild("left_door").yaw = doorEntity.getLeftDoorRotation();*/

		matrices.push();
		matrices.scale(0.68F, 0.68f, 0.68f);
		matrices.translate(0, -1.5, 0.35);
		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180));

		super.renderWithAnimations(doorEntity, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);
		matrices.pop();
	}

	@Override
	public Vec3d adjustPortalPos(Vec3d pos, Direction direction) {
		return switch (direction) {
			case DOWN, UP -> pos;
			case NORTH -> pos.add(0,0.29,-0.641);
			case SOUTH -> pos.add(0,0.29,0.641);
			case WEST -> pos.add(-0.641,0.29,0);
			case EAST -> pos.add(0.641,0.29,0);
		};
	}
}