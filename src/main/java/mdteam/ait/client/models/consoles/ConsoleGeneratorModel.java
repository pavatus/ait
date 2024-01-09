package mdteam.ait.client.models.consoles;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class ConsoleGeneratorModel extends SinglePartEntityModel {
	private final ModelPart generator;
	public ConsoleGeneratorModel(ModelPart root) {
		this.generator = root.getChild("generator");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData generator = modelPartData.addChild("generator", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData bone19 = generator.addChild("bone19", ModelPartBuilder.create().uv(79, 40).cuboid(4.0F, -2.0F, -3.5F, 2.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bone20 = bone19.addChild("bone20", ModelPartBuilder.create().uv(31, 79).cuboid(4.0F, -2.0F, -3.5F, 2.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone21 = bone20.addChild("bone21", ModelPartBuilder.create().uv(19, 77).cuboid(4.0F, -2.0F, -3.5F, 2.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone22 = bone21.addChild("bone22", ModelPartBuilder.create().uv(0, 77).cuboid(4.0F, -2.0F, -3.5F, 2.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone23 = bone22.addChild("bone23", ModelPartBuilder.create().uv(73, 76).cuboid(4.0F, -2.0F, -3.5F, 2.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone24 = bone23.addChild("bone24", ModelPartBuilder.create().uv(68, 32).cuboid(4.0F, -2.0F, -3.5F, 2.0F, 0.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone = generator.addChild("bone", ModelPartBuilder.create().uv(49, 66).cuboid(9.25F, -2.0F, -6.5F, 2.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bone2 = bone.addChild("bone2", ModelPartBuilder.create().uv(31, 63).cuboid(9.25F, -2.0F, -6.5F, 2.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone3 = bone2.addChild("bone3", ModelPartBuilder.create().uv(0, 61).cuboid(9.25F, -2.0F, -6.5F, 2.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone4 = bone3.addChild("bone4", ModelPartBuilder.create().uv(49, 50).cuboid(9.25F, -2.0F, -6.5F, 2.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone5 = bone4.addChild("bone5", ModelPartBuilder.create().uv(31, 47).cuboid(9.25F, -2.0F, -6.5F, 2.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone6 = bone5.addChild("bone6", ModelPartBuilder.create().uv(0, 45).cuboid(9.25F, -2.0F, -6.5F, 2.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone13 = generator.addChild("bone13", ModelPartBuilder.create().uv(68, 17).cuboid(6.75F, -2.0F, -4.5F, 1.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.5F, 0.0F));

		ModelPartData bone14 = bone13.addChild("bone14", ModelPartBuilder.create().uv(68, 0).cuboid(6.75F, -2.0F, -4.5F, 1.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone15 = bone14.addChild("bone15", ModelPartBuilder.create().uv(67, 66).cuboid(6.75F, -2.0F, -4.5F, 1.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone16 = bone15.addChild("bone16", ModelPartBuilder.create().uv(67, 47).cuboid(6.75F, -2.0F, -4.5F, 1.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone17 = bone16.addChild("bone17", ModelPartBuilder.create().uv(18, 61).cuboid(6.75F, -2.0F, -4.5F, 1.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone18 = bone17.addChild("bone18", ModelPartBuilder.create().uv(18, 45).cuboid(6.75F, -2.0F, -4.5F, 1.0F, 0.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone25 = generator.addChild("bone25", ModelPartBuilder.create().uv(0, 23).cuboid(-0.4F, -2.0F, -1.5F, 3.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, 0.0F));

		ModelPartData bone26 = bone25.addChild("bone26", ModelPartBuilder.create().uv(0, 19).cuboid(-0.4F, -2.0F, -1.5F, 3.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone27 = bone26.addChild("bone27", ModelPartBuilder.create().uv(0, 15).cuboid(-0.4F, -2.0F, -1.5F, 3.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone28 = bone27.addChild("bone28", ModelPartBuilder.create().uv(0, 8).cuboid(-0.4F, -2.0F, -1.5F, 3.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone29 = bone28.addChild("bone29", ModelPartBuilder.create().uv(0, 4).cuboid(-0.4F, -2.0F, -1.5F, 3.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone30 = bone29.addChild("bone30", ModelPartBuilder.create().uv(0, 0).cuboid(-0.4F, -2.0F, -1.5F, 3.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone7 = generator.addChild("bone7", ModelPartBuilder.create().uv(34, 32).cuboid(-0.75F, -1.0F, -6.5F, 10.0F, 1.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.1F, 0.0F));

		ModelPartData bone8 = bone7.addChild("bone8", ModelPartBuilder.create().uv(34, 17).cuboid(-0.75F, -1.0F, -6.5F, 10.0F, 1.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone9 = bone8.addChild("bone9", ModelPartBuilder.create().uv(34, 2).cuboid(-0.75F, -1.0F, -6.5F, 10.0F, 1.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone10 = bone9.addChild("bone10", ModelPartBuilder.create().uv(0, 30).cuboid(-0.75F, -1.0F, -6.5F, 10.0F, 1.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone11 = bone10.addChild("bone11", ModelPartBuilder.create().uv(0, 15).cuboid(-0.75F, -1.0F, -6.5F, 10.0F, 1.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone12 = bone11.addChild("bone12", ModelPartBuilder.create().uv(0, 0).cuboid(-0.75F, -1.0F, -6.5F, 10.0F, 1.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.push();

		generator.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);

		matrices.pop();
	}

	@Override
	public ModelPart getPart() {
		return generator;
	}

	@Override
	public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {

	}
}