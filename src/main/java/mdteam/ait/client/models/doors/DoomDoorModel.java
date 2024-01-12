package mdteam.ait.client.models.doors;

import mdteam.ait.client.models.exteriors.ExteriorModel;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.tardis.handler.DoorHandler;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;

public class DoomDoorModel extends DoorModel {
	private final ModelPart doom;
	public DoomDoorModel(ModelPart root) {
		this.doom = root.getChild("doom");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData doom = modelPartData.addChild("doom", ModelPartBuilder.create().uv(0, 0).cuboid(-32.0F, -96.0F, -1.0F, 64.0F, 96.0F, 0.0F, new Dilation(0.005F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 96);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		doom.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void renderWithAnimations(DoorBlockEntity door, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		matrices.push();
		matrices.translate(0, -1.125f, 0);
		matrices.scale(0.75f, 0.75f, 0.75f);
		super.renderWithAnimations(door, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);
		matrices.pop();
	}

	@Override
	public Animation getAnimationForDoorState(DoorHandler.DoorStateEnum state) {
		return Animation.Builder.create(0).build();
	}

	@Override
	public ModelPart getPart() {
		return doom;
	}
}