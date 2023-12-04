package mdteam.ait.client.models.doors;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class BoothDoorModel extends DoorModel {

	public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/blockentities/doors/k2_booth_door.png");
	public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, "textures/blockentities/doors/k2_booth_door_emission.png");
	private final ModelPart booth_door;
	public BoothDoorModel(ModelPart root) {
		this.booth_door = root.getChild("booth_door");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData booth_door = modelPartData.addChild("booth_door", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 1.0F));

		ModelPartData floor = booth_door.addChild("floor", ModelPartBuilder.create().uv(49, 23).cuboid(-9.0F, -1.01F, -9.0F, 18.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData base_r1 = floor.addChild("base_r1", ModelPartBuilder.create().uv(31, 12).cuboid(-9.5F, -1.0F, 6.5F, 19.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData walls = booth_door.addChild("walls", ModelPartBuilder.create().uv(40, 18).cuboid(7.0F, -41.0F, -9.0F, 2.0F, 40.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = walls.addChild("cube_r1", ModelPartBuilder.create().uv(31, 18).cuboid(7.0F, -40.0F, -9.0F, 2.0F, 40.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData roof = booth_door.addChild("roof", ModelPartBuilder.create().uv(49, 18).cuboid(-9.5F, -42.0F, -9.5F, 19.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r2 = roof.addChild("cube_r2", ModelPartBuilder.create().uv(49, 26).cuboid(-9.0F, -50.0F, 7.0F, 18.0F, 8.0F, 3.0F, new Dilation(0.5F))
		.uv(31, 0).cuboid(-9.0F, -50.0F, 7.0F, 18.0F, 8.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData door = booth_door.addChild("door", ModelPartBuilder.create().uv(0, 0).cuboid(-13.5F, -41.0F, -0.5F, 14.0F, 40.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 42).cuboid(-13.5F, -22.0F, -1.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(6.5F, 0.0F, -9.5F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void renderWithAnimations(DoorBlockEntity door, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		matrices.push();
		this.booth_door.getChild("door").yaw = -door.getLeftDoorRotation();
		matrices.scale(0.93f, 0.93f, 0.93f);
		matrices.translate(0, -1.5f, 0);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));

		super.renderWithAnimations(door, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);
		matrices.pop();
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.booth_door.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public Identifier getTexture() {
		return TEXTURE;
	}

	@Override
	public Identifier getEmission() {
		return EMISSION;
	}

	@Override
	public ModelPart getPart() {
		return this.booth_door;
	}
}