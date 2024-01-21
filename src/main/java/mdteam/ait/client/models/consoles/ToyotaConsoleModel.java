// Made with Blockbench 4.9.3
package mdteam.ait.client.models.consoles;

import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.TardisTravel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class ToyotaConsoleModel extends ConsoleModel {
	private final ModelPart toyota;
	public ToyotaConsoleModel(ModelPart root) {
		this.toyota = root.getChild("toyota");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData toyota = modelPartData.addChild("toyota", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData panel1 = toyota.addChild("panel1", ModelPartBuilder.create().uv(107, 185).cuboid(-14.0F, -14.9306F, -25.1225F, 28.0F, 3.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -2.0F, 0.0F));

		ModelPartData cube_r1 = panel1.addChild("cube_r1", ModelPartBuilder.create().uv(176, 174).cuboid(-1.0F, 0.9F, -1.1F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(9.0F, -17.0F, -23.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r2 = panel1.addChild("cube_r2", ModelPartBuilder.create().uv(192, 123).cuboid(-3.0F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(8.0F, -17.5609F, -17.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r3 = panel1.addChild("cube_r3", ModelPartBuilder.create().uv(149, 156).cuboid(-1.0F, 5.5F, -28.0F, 2.0F, 2.0F, 22.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, -11.9609F, 0.0F, -0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r4 = panel1.addChild("cube_r4", ModelPartBuilder.create().uv(77, 178).cuboid(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-14.0582F, -13.4805F, -24.3496F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r5 = panel1.addChild("cube_r5", ModelPartBuilder.create().uv(0, 181).cuboid(-1.0F, -7.5F, -28.0F, 2.0F, 2.0F, 20.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -15.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r6 = panel1.addChild("cube_r6", ModelPartBuilder.create().uv(108, 83).cuboid(-14.0F, -4.0F, -1.0F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -11.8613F, -21.0F, 1.309F, 0.0F, 0.0F));

		ModelPartData cube_r7 = panel1.addChild("cube_r7", ModelPartBuilder.create().uv(108, 102).cuboid(-14.0F, -14.0F, -1.0F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -15.0F, -21.0F, -1.309F, 0.0F, 0.0F));

		ModelPartData controls = panel1.addChild("controls", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -11.9609F, 0.0F));

		ModelPartData cube_r8 = controls.addChild("cube_r8", ModelPartBuilder.create().uv(62, 184).cuboid(-1.0F, -0.5F, -1.75F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -6.6222F, -11.8592F, 0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r9 = controls.addChild("cube_r9", ModelPartBuilder.create().uv(27, 138).cuboid(-7.0F, 0.35F, -1.0F, 8.0F, 0.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(3.0F, -3.9015F, -23.3038F, 0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r10 = controls.addChild("cube_r10", ModelPartBuilder.create().uv(180, 142).cuboid(-6.5F, -10.75F, -20.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(181, 13).cuboid(5.5F, -10.75F, -20.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(39, 193).cuboid(6.0F, -11.25F, -21.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(7, 194).cuboid(-6.0F, -11.25F, -21.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -0.1F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData dooropen = controls.addChild("dooropen", ModelPartBuilder.create(), ModelTransform.pivot(-5.5F, -4.8F, -22.0F));

		ModelPartData cube_r11 = dooropen.addChild("cube_r11", ModelPartBuilder.create().uv(97, 187).cuboid(-4.75F, -10.75F, -21.5F, 0.0F, 1.0F, 2.0F, new Dilation(0.001F))
		.uv(193, 173).cuboid(-6.25F, -10.75F, -21.5F, 0.0F, 1.0F, 2.0F, new Dilation(0.001F))
		.uv(182, 130).cuboid(-6.25F, -10.75F, -21.5F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F))
		.uv(192, 90).cuboid(-6.0F, -10.75F, -24.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, 4.7F, 22.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData power = controls.addChild("power", ModelPartBuilder.create(), ModelTransform.pivot(5.5F, -4.8F, -22.0F));

		ModelPartData cube_r12 = power.addChild("cube_r12", ModelPartBuilder.create().uv(182, 102).cuboid(4.75F, -10.75F, -21.5F, 0.0F, 1.0F, 2.0F, new Dilation(0.001F))
		.uv(45, 185).cuboid(6.25F, -10.75F, -21.5F, 0.0F, 1.0F, 2.0F, new Dilation(0.001F))
		.uv(175, 85).cuboid(4.25F, -10.75F, -21.5F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F))
		.uv(192, 85).cuboid(5.0F, -10.75F, -24.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-5.0F, 4.7F, 22.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData geigercounter = controls.addChild("geigercounter", ModelPartBuilder.create(), ModelTransform.of(-7.0F, -4.0F, -19.0F, 0.3365F, 0.6699F, 0.2139F));

		ModelPartData cube_r13 = geigercounter.addChild("cube_r13", ModelPartBuilder.create().uv(164, 181).cuboid(-1.6401F, -0.05F, -0.735F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.4F, -1.3891F, 0.25F, 0.0F, 0.8727F, 0.0F));

		ModelPartData cube_r14 = geigercounter.addChild("cube_r14", ModelPartBuilder.create().uv(181, 16).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 2.0F, 3.0F, new Dilation(-0.4F))
		.uv(107, 189).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 2.0F, 3.0F, new Dilation(-0.2F))
		.uv(120, 189).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 2.0F, 3.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, 2.2109F, 0.75F, 0.0F, 0.0873F, 0.0F));

		ModelPartData lockernob1 = controls.addChild("lockernob1", ModelPartBuilder.create(), ModelTransform.pivot(-3.0F, -3.9015F, -23.3038F));

		ModelPartData cube_r15 = lockernob1.addChild("cube_r15", ModelPartBuilder.create().uv(135, 205).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData lockernob2 = controls.addChild("lockernob2", ModelPartBuilder.create(), ModelTransform.pivot(-1.5F, -3.9015F, -23.3038F));

		ModelPartData lockernob3_r1 = lockernob2.addChild("lockernob3_r1", ModelPartBuilder.create().uv(130, 205).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData lockernob3 = controls.addChild("lockernob3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -3.9015F, -23.3038F));

		ModelPartData cube_r16 = lockernob3.addChild("cube_r16", ModelPartBuilder.create().uv(125, 205).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData lockernob4 = controls.addChild("lockernob4", ModelPartBuilder.create(), ModelTransform.pivot(1.5F, -3.9015F, -23.3038F));

		ModelPartData cube_r17 = lockernob4.addChild("cube_r17", ModelPartBuilder.create().uv(115, 205).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData lockernob5 = controls.addChild("lockernob5", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r18 = lockernob5.addChild("cube_r18", ModelPartBuilder.create().uv(110, 205).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -3.9015F, -23.3038F, 0.2618F, 0.0F, 0.0F));

		ModelPartData faucettaps1 = controls.addChild("faucettaps1", ModelPartBuilder.create(), ModelTransform.pivot(-3.3F, -6.2178F, -13.3532F));

		ModelPartData cube_r19 = faucettaps1.addChild("cube_r19", ModelPartBuilder.create().uv(130, 189).cuboid(-19.95F, -6.55F, -7.81F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(142, 178).cuboid(-20.9F, -6.65F, -7.25F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(19.5F, 3.7787F, 8.6032F, 0.2618F, 0.0F, 0.0F));

		ModelPartData faucettaps2 = controls.addChild("faucettaps2", ModelPartBuilder.create(), ModelTransform.pivot(3.3F, -6.2178F, -13.3532F));

		ModelPartData cube_r20 = faucettaps2.addChild("cube_r20", ModelPartBuilder.create().uv(152, 189).cuboid(18.95F, -6.55F, -7.81F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(180, 42).cuboid(19.0F, -6.65F, -7.25F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-19.5F, 3.7787F, 8.6032F, 0.2618F, 0.0F, 0.0F));

		ModelPartData redknob = controls.addChild("redknob", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -4.0309F, -20.8209F));

		ModelPartData cube_r21 = redknob.addChild("cube_r21", ModelPartBuilder.create().uv(105, 205).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData largefaucettap = controls.addChild("largefaucettap", ModelPartBuilder.create().uv(100, 205).cuboid(0.25F, -1.3823F, -2.1733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(55, 205).cuboid(0.25F, -1.3823F, -2.7733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(44, 180).cuboid(0.25F, -1.3823F, -3.3733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(175, 19).cuboid(0.25F, -1.3823F, -4.5733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(96, 178).cuboid(0.25F, -1.3823F, -3.9733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(202, 0).cuboid(0.25F, -0.7823F, -2.1733F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-3.25F, -5.65F, -15.05F, 0.2618F, 0.0F, 0.0F));

		ModelPartData largefaucettap2 = controls.addChild("largefaucettap2", ModelPartBuilder.create().uv(90, 205).cuboid(0.25F, -1.3823F, -2.1733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(50, 205).cuboid(0.25F, -1.3823F, -2.7733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(20, 204).cuboid(0.25F, -1.3823F, -3.3733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(15, 204).cuboid(0.25F, -1.3823F, -4.5733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(171, 181).cuboid(0.25F, -1.3823F, -3.9733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(201, 148).cuboid(0.25F, -0.7823F, -2.1733F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-2.25F, -5.65F, -15.05F, 0.2618F, 0.0F, 0.0F));

		ModelPartData largefaucettap3 = controls.addChild("largefaucettap3", ModelPartBuilder.create().uv(85, 205).cuboid(-0.25F, -1.3823F, -2.1733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(205, 28).cuboid(-0.25F, -1.3823F, -2.7733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(185, 175).cuboid(-0.25F, -1.3823F, -3.3733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(184, 135).cuboid(-0.25F, -1.3823F, -4.5733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(25, 204).cuboid(-0.25F, -1.3823F, -3.9733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(201, 139).cuboid(-0.25F, -0.7823F, -2.1733F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-0.75F, -5.65F, -15.05F, 0.2618F, 0.0F, 0.0F));

		ModelPartData largefaucettap4 = controls.addChild("largefaucettap4", ModelPartBuilder.create().uv(80, 205).cuboid(-0.75F, -1.3823F, -2.1733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(205, 31).cuboid(-0.75F, -1.3823F, -2.7733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(117, 189).cuboid(-0.75F, -1.3823F, -3.3733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(187, 42).cuboid(-0.75F, -1.3823F, -3.9733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(40, 187).cuboid(-0.75F, -1.3823F, -4.5733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(201, 135).cuboid(-0.75F, -0.7823F, -2.1733F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(0.75F, -5.65F, -15.05F, 0.2618F, 0.0F, 0.0F));

		ModelPartData largefaucettap5 = controls.addChild("largefaucettap5", ModelPartBuilder.create().uv(75, 205).cuboid(-1.25F, -1.3823F, -2.1733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(205, 38).cuboid(-1.25F, -1.3823F, -2.7733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(40, 204).cuboid(-1.25F, -1.3823F, -3.3733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(35, 204).cuboid(-1.25F, -1.3823F, -3.9733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(30, 204).cuboid(-1.25F, -1.3823F, -4.5733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(115, 201).cuboid(-1.25F, -0.7823F, -2.1733F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(2.25F, -5.65F, -15.05F, 0.2618F, 0.0F, 0.0F));

		ModelPartData largefaucettap6 = controls.addChild("largefaucettap6", ModelPartBuilder.create().uv(60, 205).cuboid(-1.75F, -1.3823F, -2.1733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(205, 41).cuboid(-1.75F, -1.3823F, -2.7733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(205, 25).cuboid(-1.75F, -1.3823F, -3.3733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(204, 129).cuboid(-1.75F, -1.3823F, -3.9733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(204, 102).cuboid(-1.75F, -1.3823F, -4.5733F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(105, 201).cuboid(-1.75F, -0.7823F, -2.1733F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(3.75F, -5.65F, -15.05F, 0.2618F, 0.0F, 0.0F));

		ModelPartData smalllockernob = controls.addChild("smalllockernob", ModelPartBuilder.create().uv(65, 205).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(110, 201).cuboid(0.0F, 0.75F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.0F, -6.75F, -15.5F, 0.2618F, 0.0F, 0.0F));

		ModelPartData smallswitch = controls.addChild("smallswitch", ModelPartBuilder.create().uv(171, 13).cuboid(0.0F, -1.6136F, -0.2533F, 0.0F, 1.0F, 1.0F, new Dilation(0.001F))
		.uv(91, 153).cuboid(-1.0F, -2.4136F, -0.7533F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(6.5F, -5.4356F, -17.1625F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tinylever = controls.addChild("tinylever", ModelPartBuilder.create(), ModelTransform.pivot(9.25F, -4.1268F, -22.8931F));

		ModelPartData cube_r22 = tinylever.addChild("cube_r22", ModelPartBuilder.create().uv(165, 85).cuboid(-0.5F, -1.5088F, 0.0328F, 1.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tinytinyswitch = controls.addChild("tinytinyswitch", ModelPartBuilder.create().uv(178, 0).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-3.5F, -4.5F, -21.25F));

		ModelPartData tinytinyswitch2 = controls.addChild("tinytinyswitch2", ModelPartBuilder.create().uv(175, 65).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-2.5F, -4.5F, -21.25F));

		ModelPartData tinytinyswitch3 = controls.addChild("tinytinyswitch3", ModelPartBuilder.create().uv(172, 141).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-1.5F, -4.5F, -21.25F));

		ModelPartData tinytinyswitch4 = controls.addChild("tinytinyswitch4", ModelPartBuilder.create().uv(172, 135).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(1.5F, -4.5F, -21.25F));

		ModelPartData tinytinyswitch5 = controls.addChild("tinytinyswitch5", ModelPartBuilder.create().uv(172, 110).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(2.5F, -4.5F, -21.25F));

		ModelPartData tinytinyswitch6 = controls.addChild("tinytinyswitch6", ModelPartBuilder.create().uv(105, 142).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(3.5F, -4.5F, -21.25F));

		ModelPartData dooropenpowerlights = panel1.addChild("dooropenpowerlights", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

		ModelPartData cube_r23 = dooropenpowerlights.addChild("cube_r23", ModelPartBuilder.create().uv(85, 201).cuboid(4.0F, -20.75F, -16.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F))
		.uv(80, 201).cuboid(6.0F, -20.75F, -16.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F))
		.uv(100, 201).cuboid(-6.0F, -20.75F, -16.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F))
		.uv(90, 201).cuboid(-8.0F, -20.75F, -16.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(0.5F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData panel2 = toyota.addChild("panel2", ModelPartBuilder.create().uv(164, 184).cuboid(-14.0F, -14.9306F, -25.1225F, 28.0F, 3.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r24 = panel2.addChild("cube_r24", ModelPartBuilder.create().uv(175, 0).cuboid(-1.0F, -8.023F, -31.6235F, 2.0F, 2.0F, 22.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 2.0391F, 0.0F, -0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r25 = panel2.addChild("cube_r25", ModelPartBuilder.create().uv(25, 180).cuboid(-1.0F, -15.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-14.0582F, 0.5195F, -24.3496F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r26 = panel2.addChild("cube_r26", ModelPartBuilder.create().uv(180, 25).cuboid(-1.0F, -21.023F, -24.3765F, 2.0F, 2.0F, 20.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r27 = panel2.addChild("cube_r27", ModelPartBuilder.create().uv(57, 0).cuboid(-14.0F, -7.6235F, 12.523F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 2.1387F, -21.0F, 1.309F, 0.0F, 0.0F));

		ModelPartData cube_r28 = panel2.addChild("cube_r28", ModelPartBuilder.create().uv(104, 57).cuboid(-7.0F, -0.1235F, -14.523F, 14.0F, 0.0F, 2.0F, new Dilation(0.001F))
		.uv(0, 135).cuboid(-7.0F, -14.1235F, -14.523F, 14.0F, 0.0F, 2.0F, new Dilation(0.001F))
		.uv(0, 138).cuboid(-7.0F, -0.1235F, -14.523F, 14.0F, 0.0F, 0.0F, new Dilation(0.001F))
		.uv(51, 76).cuboid(7.0F, -14.1235F, -14.523F, 0.0F, 14.0F, 2.0F, new Dilation(0.001F))
		.uv(51, 93).cuboid(-7.0F, -14.1235F, -14.523F, 0.0F, 14.0F, 2.0F, new Dilation(0.001F))
		.uv(57, 142).cuboid(-7.0F, -14.1235F, -12.523F, 14.0F, 14.0F, 0.0F, new Dilation(0.001F))
		.uv(0, 161).cuboid(-14.0F, -17.6235F, -14.523F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -1.0F, -21.0F, -1.309F, 0.0F, 0.0F));

		ModelPartData controls3 = panel2.addChild("controls3", ModelPartBuilder.create(), ModelTransform.pivot(1.25F, -4.5F, -8.0F));

		ModelPartData gears = controls3.addChild("gears", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 2.618F, 0.0F, 3.1416F));

		ModelPartData largegear1 = gears.addChild("largegear1", ModelPartBuilder.create(), ModelTransform.pivot(-1.625F, -16.703F, 6.2174F));

		ModelPartData cube_r29 = largegear1.addChild("cube_r29", ModelPartBuilder.create().uv(203, 161).cuboid(-6.25F, -3.95F, -2.75F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(172, 80).cuboid(-6.25F, -3.95F, -2.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F))
		.uv(167, 193).cuboid(-4.25F, -3.95F, -2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(4.625F, 2.051F, -3.0203F, -1.309F, 0.0F, 0.0F));

		ModelPartData cube_r30 = largegear1.addChild("cube_r30", ModelPartBuilder.create().uv(192, 60).cuboid(-2.0F, -2.2F, -0.1F, 4.0F, 4.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.875F, 1.2622F, 0.1258F, -1.309F, 0.0F, 0.0F));

		ModelPartData largegear2 = gears.addChild("largegear2", ModelPartBuilder.create(), ModelTransform.pivot(3.875F, -16.0492F, 2.8403F));

		ModelPartData cube_r31 = largegear2.addChild("cube_r31", ModelPartBuilder.create().uv(167, 188).cuboid(-2.0F, -2.5F, 0.0F, 4.0F, 4.0F, 0.0F, new Dilation(0.001F))
		.uv(203, 164).cuboid(-2.5F, -1.0F, -2.75F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(175, 33).cuboid(-2.5F, -1.0F, -2.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F))
		.uv(181, 193).cuboid(-0.5F, -1.0F, -2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(0.875F, 1.3972F, -0.1433F, -1.309F, 0.0F, 0.0F));

		ModelPartData largegear3 = gears.addChild("largegear3", ModelPartBuilder.create(), ModelTransform.pivot(-1.625F, -15.0494F, -1.3142F));

		ModelPartData cube_r32 = largegear3.addChild("cube_r32", ModelPartBuilder.create().uv(203, 126).cuboid(-6.25F, 2.5F, -2.75F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(71, 157).cuboid(-6.25F, 2.5F, -2.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F))
		.uv(192, 128).cuboid(-4.25F, 2.5F, -2.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(4.625F, 0.5474F, 3.5113F, -1.309F, 0.0F, 0.0F));

		ModelPartData cube_r33 = largegear3.addChild("cube_r33", ModelPartBuilder.create().uv(181, 188).cuboid(-2.0F, -2.25F, -0.5F, 4.0F, 4.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.875F, 1.3885F, 0.3721F, -1.309F, 0.0F, 0.0F));

		ModelPartData tinygear1 = gears.addChild("tinygear1", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, -15.402F, 2.6971F));

		ModelPartData cube_r34 = tinygear1.addChild("cube_r34", ModelPartBuilder.create().uv(203, 177).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -1.309F, 0.0F, 0.0F));

		ModelPartData tinygear2 = gears.addChild("tinygear2", ModelPartBuilder.create(), ModelTransform.pivot(2.5F, -14.5608F, -0.1922F));

		ModelPartData cube_r35 = tinygear2.addChild("cube_r35", ModelPartBuilder.create().uv(203, 174).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -1.309F, 0.0F, 0.0F));

		ModelPartData tinygear3 = gears.addChild("tinygear3", ModelPartBuilder.create(), ModelTransform.pivot(2.5F, -16.2432F, 5.5863F));

		ModelPartData cube_r36 = tinygear3.addChild("cube_r36", ModelPartBuilder.create().uv(203, 171).cuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -1.309F, 0.0F, 0.0F));

		ModelPartData tapnobs = controls3.addChild("tapnobs", ModelPartBuilder.create().uv(135, 178).cuboid(-1.0F, -0.5F, -0.25F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(203, 123).cuboid(-0.5F, -0.5F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(1.0F, -12.6228F, -9.958F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tapnobs2 = controls3.addChild("tapnobs2", ModelPartBuilder.create().uv(128, 178).cuboid(-1.0F, -0.5F, -0.25F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(140, 202).cuboid(-0.5F, -0.5F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(-2.5F, -13.3728F, -7.058F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tapnobs3 = controls3.addChild("tapnobs3", ModelPartBuilder.create().uv(121, 178).cuboid(-1.0F, -0.5F, -0.25F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(135, 202).cuboid(-0.5F, -0.5F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(-2.5F, -11.7728F, -12.808F, 0.2618F, 0.0F, 0.0F));

		ModelPartData keyhole = controls3.addChild("keyhole", ModelPartBuilder.create().uv(199, 128).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.25F, -13.8F, -2.15F));

		ModelPartData tinytapnob = controls3.addChild("tinytapnob", ModelPartBuilder.create().uv(130, 202).cuboid(-0.5F, -0.9167F, -0.6667F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(199, 103).cuboid(-0.5F, -0.1667F, -0.6667F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(125, 202).cuboid(-0.5F, -0.9167F, -0.1667F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.0F, -14.6333F, -2.0833F));

		ModelPartData tinytapnob2 = controls3.addChild("tinytapnob2", ModelPartBuilder.create().uv(202, 98).cuboid(-0.5F, -0.9167F, -0.6667F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(0, 196).cuboid(-0.5F, -0.1667F, -0.6667F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(202, 19).cuboid(-0.5F, -0.9167F, -0.1667F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-9.0F, -12.4333F, -12.0833F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tinytapnob3 = controls3.addChild("tinytapnob3", ModelPartBuilder.create().uv(202, 16).cuboid(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(202, 4).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(171, 6).cuboid(-1.0F, -1.25F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.25F)), ModelTransform.of(-11.0F, -11.4512F, -15.2875F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tinytapnob4 = controls3.addChild("tinytapnob4", ModelPartBuilder.create().uv(127, 195).cuboid(-0.5F, -0.3749F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(165, 141).cuboid(-1.0F, -1.1251F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.25F)), ModelTransform.of(6.5F, -11.0891F, -15.1904F, 0.2618F, 0.0F, 0.0F));

		ModelPartData panel3 = toyota.addChild("panel3", ModelPartBuilder.create().uv(118, 60).cuboid(-13.8726F, -7.6806F, -17.4019F, 28.0F, 3.0F, 0.0F, new Dilation(0.001F))
		.uv(183, 110).cuboid(2.35F, -8.0962F, -17.6985F, 1.0F, 4.0F, 0.0F, new Dilation(0.001F))
		.uv(113, 31).cuboid(0.35F, -5.0962F, -17.6985F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F))
		.uv(48, 138).cuboid(0.35F, -8.0962F, -17.6985F, 1.0F, 3.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(6.75F, -9.25F, 3.75F, 0.0F, -2.0944F, 0.0F));

		ModelPartData cube_r37 = panel3.addChild("cube_r37", ModelPartBuilder.create().uv(174, 104).cuboid(0.6F, -12.4F, -15.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.001F))
		.uv(113, 46).cuboid(2.6F, -12.4F, -15.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(165, 148).cuboid(2.35F, -12.9F, -14.0F, 2.0F, 1.0F, 5.0F, new Dilation(0.0F))
		.uv(100, 153).cuboid(-4.85F, -13.9F, -12.35F, 0.0F, 2.0F, 3.0F, new Dilation(0.001F))
		.uv(86, 178).cuboid(-7.1F, -13.9F, -11.1F, 3.0F, 2.0F, 0.0F, new Dilation(0.001F))
		.uv(137, 57).cuboid(5.85F, -13.9F, -8.0F, 2.0F, 0.0F, 2.0F, new Dilation(0.0F))
		.uv(66, 58).cuboid(6.35F, -13.9F, -7.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(66, 44).cuboid(6.35F, -12.9F, -5.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(113, 27).cuboid(6.35F, -12.9F, -4.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.25F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r38 = panel3.addChild("cube_r38", ModelPartBuilder.create().uv(153, 35).cuboid(-1.0F, -8.023F, -31.6235F, 2.0F, 2.0F, 22.0F, new Dilation(-0.001F)), ModelTransform.of(0.1274F, 9.2891F, 7.7207F, -0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r39 = panel3.addChild("cube_r39", ModelPartBuilder.create().uv(165, 135).cuboid(-1.0F, -15.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-13.9308F, 7.7695F, -16.6289F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r40 = panel3.addChild("cube_r40", ModelPartBuilder.create().uv(176, 135).cuboid(-1.0F, -21.023F, -24.3765F, 2.0F, 2.0F, 20.0F, new Dilation(0.001F)), ModelTransform.of(0.1274F, 6.25F, 7.7207F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r41 = panel3.addChild("cube_r41", ModelPartBuilder.create().uv(0, 0).cuboid(-14.0F, -17.9761F, -0.0344F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.1274F, -0.0613F, -0.0293F, 1.309F, 0.0F, 0.0F));

		ModelPartData cube_r42 = panel3.addChild("cube_r42", ModelPartBuilder.create().uv(108, 64).cuboid(-14.0F, -17.6235F, -14.523F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.1274F, 6.25F, -13.2793F, -1.309F, 0.0F, 0.0F));

		ModelPartData controls5 = panel3.addChild("controls5", ModelPartBuilder.create(), ModelTransform.pivot(0.1274F, 9.2891F, 7.7207F));

		ModelPartData randomsuctioncupthing = controls5.addChild("randomsuctioncupthing", ModelPartBuilder.create().uv(171, 13).cuboid(-7.0F, -20.0F, -18.0F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F))
		.uv(66, 29).cuboid(-6.0F, -22.0F, -17.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(19, 139).cuboid(-6.5F, -22.0F, -17.5F, 2.0F, 0.0F, 2.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData speaker = controls5.addChild("speaker", ModelPartBuilder.create(), ModelTransform.pivot(4.5F, -19.6609F, -15.1033F));

		ModelPartData cube_r43 = speaker.addChild("cube_r43", ModelPartBuilder.create().uv(165, 126).cuboid(-3.0F, -0.1F, -1.9F, 4.0F, 0.0F, 4.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r44 = speaker.addChild("cube_r44", ModelPartBuilder.create().uv(180, 149).cuboid(-2.25F, -1.0F, -2.15F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.3622F, 0.7519F, 0.2533F));

		ModelPartData spinnything1 = controls5.addChild("spinnything1", ModelPartBuilder.create().uv(25, 186).cuboid(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, -21.2016F, -11.2982F, 0.2618F, 0.0F, 0.0F));

		ModelPartData spinnything2 = controls5.addChild("spinnything2", ModelPartBuilder.create().uv(77, 184).cuboid(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -21.2016F, -11.2982F, 0.2618F, 0.0F, 0.0F));

		ModelPartData spinnything3 = controls5.addChild("spinnything3", ModelPartBuilder.create().uv(175, 148).cuboid(-1.0F, -1.0F, -0.5F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(30, 198).cuboid(-0.25F, -0.25F, -2.7F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(192, 42).cuboid(-0.25F, -0.25F, -3.7F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(122, 195).cuboid(-1.25F, -0.25F, -2.7F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(159, 189).cuboid(-1.25F, -0.25F, -3.7F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(25, 198).cuboid(0.75F, -0.25F, -2.7F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(107, 195).cuboid(0.75F, -0.25F, -3.7F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(13, 198).cuboid(1.75F, -0.25F, -2.7F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(112, 195).cuboid(1.75F, -0.25F, -3.7F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(195, 158).cuboid(2.75F, -0.25F, -2.7F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(117, 195).cuboid(2.75F, -0.25F, -3.7F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(140, 205).cuboid(3.75F, -0.25F, -2.7F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(205, 142).cuboid(3.75F, -0.25F, -3.7F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(-3.0F, -21.2016F, -11.2982F, 0.2618F, 0.0F, 0.0F));

		ModelPartData rotaryswitch1 = controls5.addChild("rotaryswitch1", ModelPartBuilder.create(), ModelTransform.pivot(5.6226F, -18.631F, -19.8001F));

		ModelPartData cube_r45 = rotaryswitch1.addChild("cube_r45", ModelPartBuilder.create().uv(202, 13).cuboid(5.0F, -12.65F, -10.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(165, 80).cuboid(5.0F, -12.15F, -9.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-5.75F, 9.3419F, 12.0795F, 0.2618F, 0.0F, 0.0F));

		ModelPartData rotaryswitch2 = controls5.addChild("rotaryswitch2", ModelPartBuilder.create(), ModelTransform.pivot(5.6226F, -18.2427F, -21.249F));

		ModelPartData cube_r46 = rotaryswitch2.addChild("cube_r46", ModelPartBuilder.create().uv(201, 152).cuboid(5.0F, -12.65F, -11.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(64, 157).cuboid(5.0F, -12.15F, -11.0F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-5.75F, 8.9537F, 13.5284F, 0.2618F, 0.0F, 0.0F));

		ModelPartData rotaryswitch3 = controls5.addChild("rotaryswitch3", ModelPartBuilder.create(), ModelTransform.pivot(5.6226F, -17.8545F, -22.6979F));

		ModelPartData cube_r47 = rotaryswitch3.addChild("cube_r47", ModelPartBuilder.create().uv(200, 158).cuboid(5.0F, -12.65F, -13.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(57, 157).cuboid(5.0F, -12.15F, -12.5F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-5.75F, 8.5655F, 14.9772F, 0.2618F, 0.0F, 0.0F));

		ModelPartData siegemode = controls5.addChild("siegemode", ModelPartBuilder.create().uv(104, 60).cuboid(-1.5F, 3.6223F, -5.5237F, 3.0F, 0.0F, 2.0F, new Dilation(0.001F))
		.uv(42, 135).cuboid(-1.5F, 2.6223F, -5.0237F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -12.7879F, -20.2263F));

		ModelPartData cube_r48 = siegemode.addChild("cube_r48", ModelPartBuilder.create().uv(91, 147).cuboid(5.5F, -1.0F, -4.3F, 3.0F, 0.0F, 5.0F, new Dilation(0.001F)), ModelTransform.of(-7.0F, 1.2879F, 0.2263F, 0.7854F, 0.0F, 0.0F));

		ModelPartData siegemodehandle = siegemode.addChild("siegemodehandle", ModelPartBuilder.create().uv(175, 60).cuboid(1.0F, -3.0333F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(99, 181).cuboid(-1.0F, -3.0333F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(33, 135).cuboid(-1.5F, -3.5333F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 3.1557F, -4.5237F));

		ModelPartData panel4 = toyota.addChild("panel4", ModelPartBuilder.create().uv(107, 181).cuboid(-14.0F, -14.9306F, -25.1225F, 28.0F, 3.0F, 0.0F, new Dilation(0.001F))
		.uv(89, 194).cuboid(-3.5F, -19.25F, -12.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(14, 194).cuboid(-2.0F, -19.25F, -12.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(191, 13).cuboid(-0.5F, -19.25F, -12.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(165, 155).cuboid(1.0F, -19.25F, -12.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(165, 148).cuboid(2.5F, -19.25F, -12.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData cube_r49 = panel4.addChild("cube_r49", ModelPartBuilder.create().uv(186, 0).cuboid(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(186, 5).cuboid(-8.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(3.5F, -17.65F, -16.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r50 = panel4.addChild("cube_r50", ModelPartBuilder.create().uv(62, 191).cuboid(6.0F, -22.4F, -19.4F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(86, 142).cuboid(-3.0F, -21.4F, -16.0F, 8.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(192, 98).cuboid(-9.0F, -21.9F, -18.9F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r51 = panel4.addChild("cube_r51", ModelPartBuilder.create().uv(194, 188).cuboid(-5.0F, 2.0F, -7.1F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(51, 119).cuboid(5.5F, 2.0F, -5.85F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(113, 42).cuboid(7.5F, 2.0F, -5.85F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(91, 147).cuboid(-8.5F, 2.0F, -4.1F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(103, 147).cuboid(-7.0F, 2.0F, -4.6F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(194, 192).cuboid(-5.5F, 2.0F, -5.6F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -20.0F, -16.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r52 = panel4.addChild("cube_r52", ModelPartBuilder.create().uv(165, 110).cuboid(-1.0F, -8.023F, -31.6235F, 2.0F, 2.0F, 22.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 2.0391F, 0.0F, -0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r53 = panel4.addChild("cube_r53", ModelPartBuilder.create().uv(178, 76).cuboid(-1.0F, -15.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-14.0582F, 0.5195F, -24.3496F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r54 = panel4.addChild("cube_r54", ModelPartBuilder.create().uv(178, 161).cuboid(-1.0F, -21.023F, -24.3765F, 2.0F, 2.0F, 20.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r55 = panel4.addChild("cube_r55", ModelPartBuilder.create().uv(0, 57).cuboid(-14.0F, -7.6235F, 12.523F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 2.1387F, -21.0F, 1.309F, 0.0F, 0.0F));

		ModelPartData cube_r56 = panel4.addChild("cube_r56", ModelPartBuilder.create().uv(114, 159).cuboid(-14.0F, -17.6235F, -14.523F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -1.0F, -21.0F, -1.309F, 0.0F, 0.0F));

		ModelPartData controls4 = panel4.addChild("controls4", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, 0.0F, 0.0F));

		ModelPartData tinyswitchcover = controls4.addChild("tinyswitchcover", ModelPartBuilder.create().uv(192, 103).cuboid(-1.0F, -1.0262F, -1.9978F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(12.0F, -15.6585F, -22.2118F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tinyswitch = controls4.addChild("tinyswitch", ModelPartBuilder.create(), ModelTransform.pivot(11.5F, -15.6847F, -23.2096F));

		ModelPartData cube_r57 = tinyswitch.addChild("cube_r57", ModelPartBuilder.create().uv(135, 57).cuboid(1.25F, -1.15F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(-1.5F, 0.5F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData throttle = controls4.addChild("throttle", ModelPartBuilder.create(), ModelTransform.of(8.0F, -15.9197F, -23.197F, -0.7854F, 0.0F, 0.0F));

		ModelPartData cube_r58 = throttle.addChild("cube_r58", ModelPartBuilder.create().uv(107, 178).cuboid(-1.25F, -23.5F, -18.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(114, 178).cuboid(1.25F, -23.5F, -18.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(187, 138).cuboid(2.75F, -22.5F, -18.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(46, 193).cuboid(-0.75F, -22.5F, -18.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(113, 60).cuboid(-0.75F, -21.0F, -18.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.001F))
		.uv(98, 153).cuboid(1.75F, -21.0F, -18.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.001F))
		.uv(188, 193).cuboid(2.25F, -21.0F, -18.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(194, 17).cuboid(-0.25F, -21.0F, -18.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(-1.0F, 13.6722F, 22.2068F, 0.2618F, 0.0F, 0.0F));

		ModelPartData handbrake = controls4.addChild("handbrake", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r59 = handbrake.addChild("cube_r59", ModelPartBuilder.create().uv(144, 57).cuboid(0.6F, -0.058F, -0.5647F, 3.0F, 0.0F, 1.0F, new Dilation(0.001F))
		.uv(142, 189).cuboid(-2.5F, 0.041F, -1.5647F, 3.0F, 1.0F, 3.0F, new Dilation(0.1F)), ModelTransform.of(-6.5F, -16.6808F, -22.5199F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tinyswitch2 = controls4.addChild("tinyswitch2", ModelPartBuilder.create().uv(54, 93).cuboid(-1.5F, -1.0441F, -3.9309F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(-4.5F, -18.1413F, -13.0774F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tinyswitch3 = controls4.addChild("tinyswitch3", ModelPartBuilder.create().uv(54, 76).cuboid(-1.5F, -1.0441F, -3.9309F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(-3.5F, -18.1413F, -13.0774F, 0.2618F, 0.0F, 0.0F));

		ModelPartData lockernob = controls4.addChild("lockernob", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r60 = lockernob.addChild("cube_r60", ModelPartBuilder.create().uv(147, 194).cuboid(-2.5F, -22.4F, -15.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData lockernob6 = controls4.addChild("lockernob6", ModelPartBuilder.create(), ModelTransform.pivot(1.5F, 0.0F, 0.0F));

		ModelPartData cube_r61 = lockernob6.addChild("cube_r61", ModelPartBuilder.create().uv(142, 194).cuboid(-2.0F, -22.4F, -15.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData lockernob7 = controls4.addChild("lockernob7", ModelPartBuilder.create(), ModelTransform.pivot(3.0F, 0.0F, 0.0F));

		ModelPartData cube_r62 = lockernob7.addChild("cube_r62", ModelPartBuilder.create().uv(132, 194).cuboid(-1.5F, -22.4F, -15.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData lockernob8 = controls4.addChild("lockernob8", ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 0.0F, 0.0F));

		ModelPartData cube_r63 = lockernob8.addChild("cube_r63", ModelPartBuilder.create().uv(94, 194).cuboid(-1.5F, -22.4F, -15.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData rotatingclockthing = controls4.addChild("rotatingclockthing", ModelPartBuilder.create().uv(180, 135).cuboid(-1.5F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new Dilation(0.001F))
		.uv(180, 123).cuboid(0.0F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new Dilation(0.001F))
		.uv(40, 180).cuboid(1.5F, -1.5F, -1.5F, 0.0F, 3.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(1.0F, -17.5F, -16.5F, 0.2618F, 0.0F, 0.0F));

		ModelPartData coloredlever = controls4.addChild("coloredlever", ModelPartBuilder.create().uv(165, 126).cuboid(-0.5F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-1.75F, -19.25F, -12.0F));

		ModelPartData coloredlever2 = controls4.addChild("coloredlever2", ModelPartBuilder.create().uv(183, 115).cuboid(-0.75F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -19.25F, -12.0F));

		ModelPartData coloredlever3 = controls4.addChild("coloredlever3", ModelPartBuilder.create().uv(183, 119).cuboid(-0.75F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(1.5F, -19.25F, -12.0F));

		ModelPartData coloredlever4 = controls4.addChild("coloredlever4", ModelPartBuilder.create().uv(84, 184).cuboid(-0.5F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(2.75F, -19.25F, -12.0F));

		ModelPartData coloredlever5 = controls4.addChild("coloredlever5", ModelPartBuilder.create().uv(32, 186).cuboid(-0.75F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(4.5F, -19.25F, -12.0F));

		ModelPartData panel5 = toyota.addChild("panel5", ModelPartBuilder.create().uv(180, 48).cuboid(-14.0F, -14.9306F, -25.1225F, 28.0F, 3.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData cube_r64 = panel5.addChild("cube_r64", ModelPartBuilder.create().uv(165, 60).cuboid(-1.0F, -8.023F, -31.6235F, 2.0F, 2.0F, 22.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 2.0391F, 0.0F, -0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r65 = panel5.addChild("cube_r65", ModelPartBuilder.create().uv(171, 0).cuboid(-1.0F, -15.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-14.0582F, 0.5195F, -24.3496F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r66 = panel5.addChild("cube_r66", ModelPartBuilder.create().uv(37, 178).cuboid(-1.0F, -21.023F, -24.3765F, 2.0F, 2.0F, 20.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r67 = panel5.addChild("cube_r67", ModelPartBuilder.create().uv(0, 19).cuboid(-14.0F, -7.6235F, 12.523F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 2.1387F, -21.0F, 1.309F, 0.0F, 0.0F));

		ModelPartData cube_r68 = panel5.addChild("cube_r68", ModelPartBuilder.create().uv(118, 38).cuboid(-14.0F, -17.6235F, -13.273F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F))
		.uv(108, 121).cuboid(-14.0F, -17.6235F, -13.523F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F))
		.uv(51, 123).cuboid(-14.0F, -17.6235F, -13.773F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F))
		.uv(108, 140).cuboid(-14.0F, -17.6235F, -14.023F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F))
		.uv(118, 19).cuboid(-14.0F, -17.6235F, -14.273F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F))
		.uv(0, 142).cuboid(-14.0F, -17.6235F, -14.523F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -1.0F, -21.0F, -1.309F, 0.0F, 0.0F));

		ModelPartData panel6 = toyota.addChild("panel6", ModelPartBuilder.create().uv(180, 52).cuboid(-14.0F, -14.9306F, -25.1225F, 28.0F, 3.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r69 = panel6.addChild("cube_r69", ModelPartBuilder.create().uv(165, 85).cuboid(-1.0F, -8.023F, -31.6235F, 2.0F, 2.0F, 22.0F, new Dilation(-0.001F)), ModelTransform.of(0.0F, 2.0391F, 0.0F, -0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r70 = panel6.addChild("cube_r70", ModelPartBuilder.create().uv(62, 178).cuboid(-1.0F, -15.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-14.0582F, 0.5195F, -24.3496F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r71 = panel6.addChild("cube_r71", ModelPartBuilder.create().uv(82, 178).cuboid(-1.0F, -21.023F, -24.3765F, 2.0F, 2.0F, 20.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r72 = panel6.addChild("cube_r72", ModelPartBuilder.create().uv(0, 38).cuboid(-14.0F, -7.6235F, 12.523F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 2.1387F, -21.0F, 1.309F, 0.0F, 0.0F));

		ModelPartData cube_r73 = panel6.addChild("cube_r73", ModelPartBuilder.create().uv(114, 0).cuboid(-14.0F, -17.6235F, -14.023F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F))
		.uv(57, 159).cuboid(-14.0F, -17.6235F, -14.523F, 28.0F, 18.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -1.0F, -21.0F, -1.309F, 0.0F, 0.0F));

		ModelPartData controls2 = panel6.addChild("controls2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData gallifreyanball = controls2.addChild("gallifreyanball", ModelPartBuilder.create(), ModelTransform.pivot(8.0F, -15.9606F, -21.1224F));

		ModelPartData cube_r74 = gallifreyanball.addChild("cube_r74", ModelPartBuilder.create().uv(192, 74).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.25F)), ModelTransform.of(0.0F, 0.0259F, -0.0966F, 0.2618F, 0.0F, 0.0F));

		ModelPartData gallifreyanball2 = controls2.addChild("gallifreyanball2", ModelPartBuilder.create(), ModelTransform.pivot(-8.0F, -15.9606F, -21.1224F));

		ModelPartData cube_r75 = gallifreyanball2.addChild("cube_r75", ModelPartBuilder.create().uv(192, 69).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.25F)), ModelTransform.of(0.0F, 0.0259F, -0.0966F, 0.2618F, 0.0F, 0.0F));

		ModelPartData smallnob = controls2.addChild("smallnob", ModelPartBuilder.create(), ModelTransform.pivot(-3.25F, -18.7839F, -12.7475F));

		ModelPartData cube_r76 = smallnob.addChild("cube_r76", ModelPartBuilder.create().uv(104, 153).cuboid(-0.25F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.001F))
		.uv(5, 204).cuboid(-0.75F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData smallnob2 = controls2.addChild("smallnob2", ModelPartBuilder.create(), ModelTransform.pivot(3.25F, -18.7839F, -12.7475F));

		ModelPartData cube_r77 = smallnob2.addChild("cube_r77", ModelPartBuilder.create().uv(165, 64).cuboid(-0.75F, -1.0F, 0.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.001F))
		.uv(0, 204).cuboid(-0.25F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tinyswitches = controls2.addChild("tinyswitches", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -18.1802F, -12.5857F));

		ModelPartData cube_r78 = tinyswitches.addChild("cube_r78", ModelPartBuilder.create().uv(75, 201).cuboid(-7.5F, 3.25F, 4.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.25F))
		.uv(165, 104).cuboid(-9.0F, 3.0F, 4.5F, 4.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(7.0F, -2.8198F, -5.4143F, 0.2618F, 0.0F, 0.0F));

		ModelPartData smallnob3 = controls2.addChild("smallnob3", ModelPartBuilder.create().uv(65, 201).cuboid(-6.0F, 2.4F, -1.1F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -19.0F, -23.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData smallnob4 = controls2.addChild("smallnob4", ModelPartBuilder.create().uv(60, 201).cuboid(5.0F, 2.4F, -1.1F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -19.0F, -23.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData smallnob5 = controls2.addChild("smallnob5", ModelPartBuilder.create().uv(55, 201).cuboid(-6.0F, 2.4F, -1.1F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -18.75F, -24.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData smallnob6 = controls2.addChild("smallnob6", ModelPartBuilder.create().uv(50, 201).cuboid(5.0F, 2.4F, -1.1F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -18.75F, -24.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData gallifreyan = panel6.addChild("gallifreyan", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -16.4362F, -18.5378F));

		ModelPartData cube_r79 = gallifreyan.addChild("cube_r79", ModelPartBuilder.create().uv(104, 49).cuboid(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -1.309F, 0.0F, 0.0F));

		ModelPartData bone2 = gallifreyan.addChild("bone2", ModelPartBuilder.create(), ModelTransform.pivot(4.5F, 0.0F, 0.0F));

		ModelPartData cube_r80 = bone2.addChild("cube_r80", ModelPartBuilder.create().uv(82, 194).cuboid(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.25F, 0.0F, 0.0F, -1.309F, 0.0F, 0.0F));

		ModelPartData bone = gallifreyan.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(-4.5F, 0.0F, 0.0F));

		ModelPartData cube_r81 = bone.addChild("cube_r81", ModelPartBuilder.create().uv(75, 194).cuboid(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(-0.25F, 0.0F, 0.0F, -1.309F, 0.0F, 0.0F));

		ModelPartData top = toyota.addChild("top", ModelPartBuilder.create().uv(57, 49).cuboid(-8.0F, -46.0F, -7.0F, 16.0F, 0.0F, 14.0F, new Dilation(0.001F))
		.uv(57, 34).cuboid(-8.0F, -84.0F, -7.0F, 16.0F, 0.0F, 14.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 23.0F, 0.0F));

		ModelPartData cube_r82 = top.addChild("cube_r82", ModelPartBuilder.create().uv(31, 214).cuboid(-7.75F, -27.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F))
		.uv(0, 204).cuboid(-8.65F, -26.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -20.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r83 = top.addChild("cube_r83", ModelPartBuilder.create().uv(212, 203).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F))
		.uv(203, 158).cuboid(-8.65F, -29.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -17.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r84 = top.addChild("cube_r84", ModelPartBuilder.create().uv(188, 212).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -17.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r85 = top.addChild("cube_r85", ModelPartBuilder.create().uv(211, 190).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -17.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

		ModelPartData cube_r86 = top.addChild("cube_r86", ModelPartBuilder.create().uv(174, 209).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -17.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r87 = top.addChild("cube_r87", ModelPartBuilder.create().uv(151, 209).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -17.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r88 = top.addChild("cube_r88", ModelPartBuilder.create().uv(207, 75).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -15.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r89 = top.addChild("cube_r89", ModelPartBuilder.create().uv(207, 59).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -15.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

		ModelPartData cube_r90 = top.addChild("cube_r90", ModelPartBuilder.create().uv(15, 207).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -15.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r91 = top.addChild("cube_r91", ModelPartBuilder.create().uv(205, 25).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -15.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData toprotor = top.addChild("toprotor", ModelPartBuilder.create(), ModelTransform.of(0.0F, -114.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

		ModelPartData cube_r92 = toprotor.addChild("cube_r92", ModelPartBuilder.create().uv(77, 148).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r93 = toprotor.addChild("cube_r93", ModelPartBuilder.create().uv(167, 188).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

		ModelPartData cube_r94 = toprotor.addChild("cube_r94", ModelPartBuilder.create().uv(165, 135).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

		ModelPartData cube_r95 = toprotor.addChild("cube_r95", ModelPartBuilder.create().uv(171, 0).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r96 = toprotor.addChild("cube_r96", ModelPartBuilder.create().uv(194, 188).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r97 = toprotor.addChild("cube_r97", ModelPartBuilder.create().uv(62, 178).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r98 = toprotor.addChild("cube_r98", ModelPartBuilder.create().uv(207, 101).cuboid(-7.75F, -27.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F))
		.uv(25, 180).cuboid(-8.65F, -26.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r99 = toprotor.addChild("cube_r99", ModelPartBuilder.create().uv(208, 89).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F))
		.uv(77, 181).cuboid(-8.65F, -29.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r100 = toprotor.addChild("cube_r100", ModelPartBuilder.create().uv(208, 114).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r101 = toprotor.addChild("cube_r101", ModelPartBuilder.create().uv(127, 189).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData monitors = top.addChild("monitors", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r102 = monitors.addChild("cube_r102", ModelPartBuilder.create().uv(25, 193).cuboid(7.25F, -48.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(152, 188).cuboid(14.0F, -54.0F, -6.0F, 1.0F, 8.0F, 12.0F, new Dilation(0.0F))
		.uv(113, 19).cuboid(14.5F, -53.0F, -7.0F, 0.0F, 6.0F, 1.0F, new Dilation(0.0F))
		.uv(54, 110).cuboid(14.5F, -53.0F, 6.0F, 0.0F, 6.0F, 1.0F, new Dilation(0.0F))
		.uv(113, 34).cuboid(-14.5F, -53.0F, 6.0F, 0.0F, 6.0F, 1.0F, new Dilation(0.0F))
		.uv(175, 25).cuboid(-14.5F, -53.0F, -7.0F, 0.0F, 6.0F, 1.0F, new Dilation(0.0F))
		.uv(179, 188).cuboid(-15.0F, -54.0F, -6.0F, 1.0F, 8.0F, 12.0F, new Dilation(0.0F))
		.uv(32, 193).cuboid(-8.25F, -48.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r103 = monitors.addChild("cube_r103", ModelPartBuilder.create().uv(0, 139).cuboid(13.5287F, 7.75F, -0.6609F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-7.25F, -47.0F, -4.0F, 0.281F, -0.4478F, -0.588F));

		ModelPartData cube_r104 = monitors.addChild("cube_r104", ModelPartBuilder.create().uv(165, 101).cuboid(-6.9713F, -0.5F, -0.6609F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-7.25F, -47.0F, -4.0F, -0.281F, -0.4478F, 0.588F));

		ModelPartData bottom = toyota.addChild("bottom", ModelPartBuilder.create().uv(57, 19).cuboid(-8.0F, -27.0F, -7.0F, 16.0F, 0.0F, 14.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 27.0F, 0.0F));

		ModelPartData cube_r105 = bottom.addChild("cube_r105", ModelPartBuilder.create().uv(155, 189).cuboid(0.0F, -9.0F, 6.5F, 0.0F, 7.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -27.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r106 = bottom.addChild("cube_r106", ModelPartBuilder.create().uv(92, 178).cuboid(0.0F, -9.0F, 6.5F, 0.0F, 7.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -27.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

		ModelPartData cube_r107 = bottom.addChild("cube_r107", ModelPartBuilder.create().uv(161, 220).cuboid(0.0F, -9.0F, 6.5F, 0.0F, 7.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -27.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r108 = bottom.addChild("cube_r108", ModelPartBuilder.create().uv(218, 155).cuboid(0.0F, -9.0F, 6.5F, 0.0F, 7.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -27.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r109 = bottom.addChild("cube_r109", ModelPartBuilder.create().uv(168, 220).cuboid(0.0F, -9.0F, 6.5F, 0.0F, 7.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -27.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r110 = bottom.addChild("cube_r110", ModelPartBuilder.create().uv(175, 220).cuboid(0.0F, -9.0F, 6.5F, 0.0F, 7.0F, 3.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -27.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r111 = bottom.addChild("cube_r111", ModelPartBuilder.create().uv(190, 135).cuboid(-0.1F, -27.0F, -5.75F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.25F, -16.0F, 0.5F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r112 = bottom.addChild("cube_r112", ModelPartBuilder.create().uv(190, 25).cuboid(-0.5F, -27.0F, -6.0F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.25F, -16.0F, 0.5F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r113 = bottom.addChild("cube_r113", ModelPartBuilder.create().uv(185, 158).cuboid(-0.9F, -27.0F, -5.25F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.25F, -16.0F, 0.5F, -3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r114 = bottom.addChild("cube_r114", ModelPartBuilder.create().uv(185, 25).cuboid(-0.9F, -27.0F, -5.75F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.25F, -16.0F, 0.5F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r115 = bottom.addChild("cube_r115", ModelPartBuilder.create().uv(50, 180).cuboid(-0.1F, -27.0F, -5.25F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.25F, -16.0F, 0.5F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r116 = bottom.addChild("cube_r116", ModelPartBuilder.create().uv(180, 25).cuboid(-0.5F, -27.0F, -5.0F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.25F, -16.0F, 0.5F, -3.1416F, 0.5236F, 3.1416F));

		ModelPartData cube_r117 = bottom.addChild("cube_r117", ModelPartBuilder.create().uv(5, 217).cuboid(-0.1F, -27.0F, -5.75F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.0F, 0.5F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r118 = bottom.addChild("cube_r118", ModelPartBuilder.create().uv(0, 217).cuboid(-0.5F, -27.0F, -6.0F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.0F, 0.5F, 0.0F, 0.0F, 0.0F));

		ModelPartData cube_r119 = bottom.addChild("cube_r119", ModelPartBuilder.create().uv(216, 125).cuboid(-0.9F, -27.0F, -5.25F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.0F, 0.5F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r120 = bottom.addChild("cube_r120", ModelPartBuilder.create().uv(146, 215).cuboid(-0.9F, -27.0F, -5.75F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.0F, 0.5F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r121 = bottom.addChild("cube_r121", ModelPartBuilder.create().uv(195, 25).cuboid(-0.1F, -27.0F, -5.25F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.0F, 0.5F, 3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r122 = bottom.addChild("cube_r122", ModelPartBuilder.create().uv(190, 158).cuboid(-0.5F, -27.0F, -5.0F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.0F, 0.5F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData cube_r123 = bottom.addChild("cube_r123", ModelPartBuilder.create().uv(192, 85).cuboid(-8.65F, -28.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F))
		.uv(214, 179).cuboid(-7.75F, -29.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F))
		.uv(135, 202).cuboid(-8.65F, -35.5F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r124 = bottom.addChild("cube_r124", ModelPartBuilder.create().uv(192, 69).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r125 = bottom.addChild("cube_r125", ModelPartBuilder.create().uv(202, 214).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r126 = bottom.addChild("cube_r126", ModelPartBuilder.create().uv(192, 56).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r127 = bottom.addChild("cube_r127", ModelPartBuilder.create().uv(123, 215).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r128 = bottom.addChild("cube_r128", ModelPartBuilder.create().uv(54, 214).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

		ModelPartData cube_r129 = bottom.addChild("cube_r129", ModelPartBuilder.create().uv(35, 201).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

		ModelPartData cube_r130 = bottom.addChild("cube_r130", ModelPartBuilder.create().uv(192, 110).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r131 = bottom.addChild("cube_r131", ModelPartBuilder.create().uv(196, 199).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F))
		.uv(100, 214).cuboid(-7.75F, -32.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F))
		.uv(60, 201).cuboid(-8.65F, -38.5F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r132 = bottom.addChild("cube_r132", ModelPartBuilder.create().uv(77, 214).cuboid(-7.75F, -30.0F, -4.5F, 2.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r133 = bottom.addChild("cube_r133", ModelPartBuilder.create().uv(202, 0).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -5.5F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r134 = bottom.addChild("cube_r134", ModelPartBuilder.create().uv(201, 135).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -5.5F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r135 = bottom.addChild("cube_r135", ModelPartBuilder.create().uv(110, 201).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -5.5F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

		ModelPartData cube_r136 = bottom.addChild("cube_r136", ModelPartBuilder.create().uv(85, 201).cuboid(-8.65F, -31.0F, -5.0F, 2.0F, 2.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -5.5F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData glass = toyota.addChild("glass", ModelPartBuilder.create().uv(34, 76).cuboid(-4.0F, -59.0F, -6.9F, 8.0F, 58.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -2.0F, 0.0F));

		ModelPartData cube_r137 = glass.addChild("cube_r137", ModelPartBuilder.create().uv(0, 76).cuboid(-4.0F, -75.0F, -6.9F, 8.0F, 58.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 16.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData cube_r138 = glass.addChild("cube_r138", ModelPartBuilder.create().uv(74, 64).cuboid(-4.0F, -75.0F, -6.9F, 8.0F, 58.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 16.0F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r139 = glass.addChild("cube_r139", ModelPartBuilder.create().uv(57, 64).cuboid(-4.0F, -75.0F, -6.9F, 8.0F, 58.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 16.0F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r140 = glass.addChild("cube_r140", ModelPartBuilder.create().uv(17, 76).cuboid(-4.0F, -75.0F, -6.9F, 8.0F, 58.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 16.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r141 = glass.addChild("cube_r141", ModelPartBuilder.create().uv(91, 64).cuboid(-4.0F, -75.0F, -6.9F, 8.0F, 58.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 16.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rotor = toyota.addChild("rotor", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r142 = rotor.addChild("cube_r142", ModelPartBuilder.create().uv(0, 180).cuboid(-1.0F, -33.0F, -5.25F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 3.1416F, -1.0472F, -3.1416F));

		ModelPartData cube_r143 = rotor.addChild("cube_r143", ModelPartBuilder.create().uv(178, 85).cuboid(-1.0F, -33.0F, -5.25F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 3.1416F, 0.0F, -3.1416F));

		ModelPartData cube_r144 = rotor.addChild("cube_r144", ModelPartBuilder.create().uv(178, 60).cuboid(-1.0F, -33.0F, -5.25F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 3.1416F, 1.0472F, -3.1416F));

		ModelPartData cube_r145 = rotor.addChild("cube_r145", ModelPartBuilder.create().uv(176, 158).cuboid(-1.0F, -33.0F, -5.25F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r146 = rotor.addChild("cube_r146", ModelPartBuilder.create().uv(174, 110).cuboid(-1.0F, -33.0F, -5.0F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -0.25F, 0.0F, 0.0F, 0.0F));

		ModelPartData cube_r147 = rotor.addChild("cube_r147", ModelPartBuilder.create().uv(165, 110).cuboid(-1.0F, -33.0F, -5.25F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r148 = rotor.addChild("cube_r148", ModelPartBuilder.create().uv(9, 180).cuboid(-1.0F, -33.0F, -5.25F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -83.0F, 0.0F, 3.1416F, 1.0472F, 0.0F));

		ModelPartData cube_r149 = rotor.addChild("cube_r149", ModelPartBuilder.create().uv(104, 34).cuboid(-1.0F, -33.0F, -5.25F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -83.0F, 0.0F, 0.0F, 1.0472F, 3.1416F));

		ModelPartData cube_r150 = rotor.addChild("cube_r150", ModelPartBuilder.create().uv(104, 19).cuboid(-1.0F, -33.0F, -5.0F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -83.0F, -0.25F, 0.0F, 0.0F, 3.1416F));

		ModelPartData cube_r151 = rotor.addChild("cube_r151", ModelPartBuilder.create().uv(57, 49).cuboid(-1.0F, -33.0F, -5.25F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -83.0F, 0.0F, 0.0F, -1.0472F, 3.1416F));

		ModelPartData cube_r152 = rotor.addChild("cube_r152", ModelPartBuilder.create().uv(57, 34).cuboid(-1.0F, -33.0F, -5.25F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -83.0F, 0.0F, 3.1416F, -1.0472F, 0.0F));

		ModelPartData cube_r153 = rotor.addChild("cube_r153", ModelPartBuilder.create().uv(57, 19).cuboid(-1.0F, -33.0F, -5.25F, 2.0F, 11.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -83.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

		ModelPartData rotorlights = rotor.addChild("rotorlights", ModelPartBuilder.create().uv(151, 220).cuboid(-0.5F, -29.0F, -5.25F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -21.0F, 0.5F));

		ModelPartData cube_r154 = rotorlights.addChild("cube_r154", ModelPartBuilder.create().uv(15, 220).cuboid(-0.1F, -27.0F, -4.5F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData cube_r155 = rotorlights.addChild("cube_r155", ModelPartBuilder.create().uv(10, 217).cuboid(-0.1F, -27.0F, -5.0F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r156 = rotorlights.addChild("cube_r156", ModelPartBuilder.create().uv(20, 220).cuboid(-0.9F, -27.0F, -5.0F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r157 = rotorlights.addChild("cube_r157", ModelPartBuilder.create().uv(25, 220).cuboid(-0.9F, -27.0F, -4.5F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		ModelPartData cube_r158 = rotorlights.addChild("cube_r158", ModelPartBuilder.create().uv(156, 220).cuboid(-0.5F, -27.0F, -4.25F, 1.0F, 15.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData rotorgizmo = rotor.addChild("rotorgizmo", ModelPartBuilder.create().uv(165, 85).cuboid(-1.5F, -35.0F, -1.5F, 3.0F, 12.0F, 3.0F, new Dilation(0.0F))
		.uv(192, 115).cuboid(-1.0F, -37.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r159 = rotorgizmo.addChild("cube_r159", ModelPartBuilder.create().uv(192, 110).cuboid(-1.0F, -37.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(165, 64).cuboid(-1.5F, -35.0F, -1.5F, 3.0F, 12.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -85.0F, 0.0F, -3.1416F, 0.0F, 0.0F));

		ModelPartData uppertimepiece = rotorgizmo.addChild("uppertimepiece", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -48.0F, 0.0F));

		ModelPartData cube_r160 = uppertimepiece.addChild("cube_r160", ModelPartBuilder.create().uv(66, 49).cuboid(-0.5F, -42.0F, 0.0F, 1.0F, 8.0F, 0.0F, new Dilation(0.001F))
		.uv(66, 19).cuboid(0.0F, -42.0F, -0.5F, 0.0F, 8.0F, 1.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -37.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

		ModelPartData lowertimepiece = rotorgizmo.addChild("lowertimepiece", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -37.0F, 0.0F));

		ModelPartData cube_r161 = lowertimepiece.addChild("cube_r161", ModelPartBuilder.create().uv(66, 34).cuboid(0.0F, -42.0F, -0.5F, 0.0F, 8.0F, 1.0F, new Dilation(0.001F))
		.uv(51, 110).cuboid(-0.5F, -42.0F, 0.0F, 1.0F, 8.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 37.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		return TexturedModelData.of(modelData, 256, 256);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.push();

		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180f));

		toyota.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);

		matrices.pop();
	}

	@Override
	public void renderWithAnimations(ConsoleBlockEntity console, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		matrices.push();

		matrices.translate(0.5f, -1.5f, -0.5f);

		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180f));

		super.renderWithAnimations(console, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);

		matrices.pop();
	}

	@Override
	public ModelPart getPart() {
		return toyota;
	}

	@Override
	public Animation getAnimationForState(TardisTravel.State state) {
		return Animation.Builder.create(0).build();
	}
}