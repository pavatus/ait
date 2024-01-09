package mdteam.ait.client.models.exteriors;

import mdteam.ait.client.animation.exterior.door.easter_head.EasterHeadAnimations;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import mdteam.ait.core.entities.TardisRealEntity;
import mdteam.ait.tardis.handler.DoorHandler;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
public class EasterHeadModel extends ExteriorModel {
	private final ModelPart head;
	public EasterHeadModel(ModelPart root) {
		this.head = root.getChild("head");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-12.0F, 0.2078F, -24.2482F, 24.0F, 14.0F, 24.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 9.7922F, 12.2483F));

		ModelPartData cube_r1 = head.addChild("cube_r1", ModelPartBuilder.create().uv(78, 57).cuboid(-9.0F, -2.0F, -11.5F, 18.0F, 19.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -9.7922F, -0.2483F, -0.4363F, 0.0F, 0.0F));

		ModelPartData door = head.addChild("door", ModelPartBuilder.create().uv(0, 72).cuboid(-10.0F, -22.7922F, -20.2482F, 20.0F, 23.0F, 20.0F, new Dilation(0.0F))
		.uv(81, 92).cuboid(10.0F, -30.7922F, -10.2483F, 6.0F, 31.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 39).cuboid(-12.0F, -32.7922F, -22.2482F, 24.0F, 10.0F, 22.0F, new Dilation(0.0F))
		.uv(81, 92).mirrored().cuboid(-16.0F, -30.7922F, -10.2483F, 6.0F, 31.0F, 8.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r2 = door.addChild("cube_r2", ModelPartBuilder.create().uv(0, 39).cuboid(5.0F, 12.0F, 2.0F, 3.0F, 8.0F, 4.0F, new Dilation(0.0F))
		.uv(97, 0).cuboid(-5.0F, 0.0F, 0.0F, 10.0F, 20.0F, 7.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-8.0F, 12.0F, 2.0F, 3.0F, 8.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -22.7922F, -20.2482F, -0.2618F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 256, 256);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void renderWithAnimations(ExteriorBlockEntity exterior, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		matrices.push();

		matrices.translate(0,-1.5f,0);

		this.head.getChild("door").pitch = (exterior.getTardis().getDoor().isOpen()) ? -45f : 0f;

		super.renderWithAnimations(exterior, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);

		matrices.pop();
	}

	@Override
	public void renderRealWorld(TardisRealEntity realEntity, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		matrices.push();

		matrices.translate(0,-1.5f,0);

		this.head.getChild("door").pitch = (realEntity.getTardis().getDoor().isOpen()) ? -45f : 0f;

		super.renderRealWorld(realEntity, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);

		matrices.pop();
	}

	@Override
	public ModelPart getPart() {
		return head;
	}

	@Override
	public Animation getAnimationForDoorState(DoorHandler.DoorStateEnum state) {
		return switch (state) {
			case CLOSED -> EasterHeadAnimations.EASTER_HEAD_EXTERIOR_CLOSE_ANIMATION;
			case FIRST -> EasterHeadAnimations.EASTER_HEAD_EXTERIOR_OPEN_ANIMATION;
			default -> Animation.Builder.create(0).build();
        };
	}
}