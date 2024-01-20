package mdteam.ait.client.models.consoles;

import mdteam.ait.client.models.consoles.ConsoleModel;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.TardisTravel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class CopperConsoleModel extends ConsoleModel {
	private final ModelPart bone;
	public CopperConsoleModel(ModelPart root) {
		this.bone = root.getChild("bone");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData bone = modelPartData.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 22.0F, 0.0F));

		ModelPartData desktop = bone.addChild("desktop", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData rim = desktop.addChild("rim", ModelPartBuilder.create().uv(33, 29).cuboid(18.0F, -5.0F, -8.0F, 2.0F, 5.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

		ModelPartData panels = desktop.addChild("panels", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData rot = panels.addChild("rot", ModelPartBuilder.create().uv(0, 30).cuboid(-10.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-10.0F, -0.2F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 15).cuboid(-9.5F, -0.6F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(19.25F, -14.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData desktop2 = desktop.addChild("desktop2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rim2 = desktop2.addChild("rim2", ModelPartBuilder.create().uv(33, 29).cuboid(18.0F, -5.0F, -8.0F, 2.0F, 5.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

		ModelPartData panels8 = desktop2.addChild("panels8", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData rot2 = panels8.addChild("rot2", ModelPartBuilder.create().uv(0, 30).cuboid(-10.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-10.0F, -0.2F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 15).cuboid(-9.5F, -0.6F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(19.25F, -14.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData desktop3 = desktop2.addChild("desktop3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rim3 = desktop3.addChild("rim3", ModelPartBuilder.create().uv(33, 29).cuboid(18.0F, -5.0F, -8.0F, 2.0F, 5.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

		ModelPartData panels9 = desktop3.addChild("panels9", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData rot3 = panels9.addChild("rot3", ModelPartBuilder.create().uv(0, 30).cuboid(-10.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-10.0F, -0.2F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 15).cuboid(-9.5F, -0.6F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(19.25F, -14.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData desktop4 = desktop3.addChild("desktop4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rim4 = desktop4.addChild("rim4", ModelPartBuilder.create().uv(33, 29).cuboid(18.0F, -5.0F, -8.0F, 2.0F, 5.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

		ModelPartData panels10 = desktop4.addChild("panels10", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData rot4 = panels10.addChild("rot4", ModelPartBuilder.create().uv(0, 30).cuboid(-10.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-10.0F, -0.2F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 15).cuboid(-9.5F, -0.6F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(19.25F, -14.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData desktop5 = desktop4.addChild("desktop5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rim5 = desktop5.addChild("rim5", ModelPartBuilder.create().uv(33, 29).cuboid(18.0F, -5.0F, -8.0F, 2.0F, 5.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

		ModelPartData panels11 = desktop5.addChild("panels11", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData rot5 = panels11.addChild("rot5", ModelPartBuilder.create().uv(0, 30).cuboid(-10.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-10.0F, -0.2F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 15).cuboid(-9.5F, -0.6F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(19.25F, -14.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData desktop6 = desktop5.addChild("desktop6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rim6 = desktop6.addChild("rim6", ModelPartBuilder.create().uv(33, 29).cuboid(18.0F, -5.0F, -8.0F, 2.0F, 5.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

		ModelPartData panels12 = desktop6.addChild("panels12", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData rot6 = panels12.addChild("rot6", ModelPartBuilder.create().uv(0, 30).cuboid(-10.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-10.0F, -0.2F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(0, 15).cuboid(-9.5F, -0.6F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(19.25F, -14.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData pillars = bone.addChild("pillars", ModelPartBuilder.create().uv(35, 4).cuboid(18.5F, -13.5F, -10.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 30).cuboid(18.5F, -13.5F, 7.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = pillars.addChild("cube_r1", ModelPartBuilder.create().uv(31, 51).cuboid(-8.0F, -2.0F, 1.0F, 9.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 1.309F));

		ModelPartData cube_r2 = pillars.addChild("cube_r2", ModelPartBuilder.create().uv(54, 26).cuboid(-1.0F, -2.0F, 1.0F, 5.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r3 = pillars.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(7.5948F, -10.45F, -3.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r4 = pillars.addChild("cube_r4", ModelPartBuilder.create().uv(0, 15).cuboid(7.5948F, -10.45F, 0.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r5 = pillars.addChild("cube_r5", ModelPartBuilder.create().uv(0, 81).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, 8.366F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r6 = pillars.addChild("cube_r6", ModelPartBuilder.create().uv(54, 26).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, 8.387F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r7 = pillars.addChild("cube_r7", ModelPartBuilder.create().uv(85, 81).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 7.5F, 0.4819F, 0.0851F, -1.7445F));

		ModelPartData cube_r8 = pillars.addChild("cube_r8", ModelPartBuilder.create().uv(13, 81).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r9 = pillars.addChild("cube_r9", ModelPartBuilder.create().uv(56, 81).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.4835F, 0.116F, -1.7282F));

		ModelPartData cube_r10 = pillars.addChild("cube_r10", ModelPartBuilder.create().uv(0, 45).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, 1.8362F, 0.4784F, -0.0403F, -1.2752F));

		ModelPartData cube_r11 = pillars.addChild("cube_r11", ModelPartBuilder.create().uv(83, 53).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, -1.8362F, -0.4784F, 0.0403F, -1.2752F));

		ModelPartData cube_r12 = pillars.addChild("cube_r12", ModelPartBuilder.create().uv(67, 81).cuboid(0.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.48F, 0.0F, -1.789F));

		ModelPartData cube_r13 = pillars.addChild("cube_r13", ModelPartBuilder.create().uv(92, 53).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -7.5F, -0.4812F, -0.0697F, -1.7526F));

		ModelPartData cube_r14 = pillars.addChild("cube_r14", ModelPartBuilder.create().uv(40, 94).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, -8.387F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r15 = pillars.addChild("cube_r15", ModelPartBuilder.create().uv(78, 10).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, -8.366F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r16 = pillars.addChild("cube_r16", ModelPartBuilder.create().uv(93, 10).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.4835F, -0.116F, -1.7282F));

		ModelPartData cube_r17 = pillars.addChild("cube_r17", ModelPartBuilder.create().uv(93, 25).cuboid(0.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.48F, 0.0F, -1.789F));

		ModelPartData cube_r18 = pillars.addChild("cube_r18", ModelPartBuilder.create().uv(76, 81).cuboid(-1.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r19 = pillars.addChild("cube_r19", ModelPartBuilder.create().uv(26, 80).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r20 = pillars.addChild("cube_r20", ModelPartBuilder.create().uv(93, 93).cuboid(-1.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r21 = pillars.addChild("cube_r21", ModelPartBuilder.create().uv(25, 95).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r22 = pillars.addChild("cube_r22", ModelPartBuilder.create().uv(0, 96).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, 0.0F, 0.48F, 0.0F));

		ModelPartData pillars2 = pillars.addChild("pillars2", ModelPartBuilder.create().uv(35, 4).cuboid(18.5F, -13.5F, -10.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 30).cuboid(18.5F, -13.5F, 7.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r23 = pillars2.addChild("cube_r23", ModelPartBuilder.create().uv(59, 54).cuboid(-6.0F, -2.0F, 1.0F, 7.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 1.309F));

		ModelPartData cube_r24 = pillars2.addChild("cube_r24", ModelPartBuilder.create().uv(54, 26).cuboid(-1.0F, -2.0F, 1.0F, 5.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r25 = pillars2.addChild("cube_r25", ModelPartBuilder.create().uv(0, 0).cuboid(7.5948F, -10.45F, -3.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r26 = pillars2.addChild("cube_r26", ModelPartBuilder.create().uv(0, 15).cuboid(7.5948F, -10.45F, 0.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r27 = pillars2.addChild("cube_r27", ModelPartBuilder.create().uv(0, 81).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, 8.366F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r28 = pillars2.addChild("cube_r28", ModelPartBuilder.create().uv(54, 26).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, 8.387F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r29 = pillars2.addChild("cube_r29", ModelPartBuilder.create().uv(85, 81).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 7.5F, 0.4819F, 0.0851F, -1.7445F));

		ModelPartData cube_r30 = pillars2.addChild("cube_r30", ModelPartBuilder.create().uv(13, 81).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r31 = pillars2.addChild("cube_r31", ModelPartBuilder.create().uv(56, 81).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.4835F, 0.116F, -1.7282F));

		ModelPartData cube_r32 = pillars2.addChild("cube_r32", ModelPartBuilder.create().uv(0, 45).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, 1.8362F, 0.4784F, -0.0403F, -1.2752F));

		ModelPartData cube_r33 = pillars2.addChild("cube_r33", ModelPartBuilder.create().uv(83, 53).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, -1.8362F, -0.4784F, 0.0403F, -1.2752F));

		ModelPartData cube_r34 = pillars2.addChild("cube_r34", ModelPartBuilder.create().uv(67, 81).cuboid(0.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.48F, 0.0F, -1.789F));

		ModelPartData cube_r35 = pillars2.addChild("cube_r35", ModelPartBuilder.create().uv(92, 53).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -7.5F, -0.4812F, -0.0697F, -1.7526F));

		ModelPartData cube_r36 = pillars2.addChild("cube_r36", ModelPartBuilder.create().uv(40, 94).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, -8.387F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r37 = pillars2.addChild("cube_r37", ModelPartBuilder.create().uv(78, 10).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, -8.366F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r38 = pillars2.addChild("cube_r38", ModelPartBuilder.create().uv(93, 10).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.4835F, -0.116F, -1.7282F));

		ModelPartData cube_r39 = pillars2.addChild("cube_r39", ModelPartBuilder.create().uv(93, 25).cuboid(0.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.48F, 0.0F, -1.789F));

		ModelPartData cube_r40 = pillars2.addChild("cube_r40", ModelPartBuilder.create().uv(76, 81).cuboid(-1.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r41 = pillars2.addChild("cube_r41", ModelPartBuilder.create().uv(26, 80).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r42 = pillars2.addChild("cube_r42", ModelPartBuilder.create().uv(93, 93).cuboid(-1.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r43 = pillars2.addChild("cube_r43", ModelPartBuilder.create().uv(25, 95).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r44 = pillars2.addChild("cube_r44", ModelPartBuilder.create().uv(0, 96).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, 0.0F, 0.48F, 0.0F));

		ModelPartData pillars3 = pillars2.addChild("pillars3", ModelPartBuilder.create().uv(35, 4).cuboid(18.5F, -13.5F, -10.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 30).cuboid(18.5F, -13.5F, 7.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r45 = pillars3.addChild("cube_r45", ModelPartBuilder.create().uv(31, 51).cuboid(-8.0F, -2.0F, 1.0F, 9.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 1.309F));

		ModelPartData cube_r46 = pillars3.addChild("cube_r46", ModelPartBuilder.create().uv(54, 26).cuboid(-1.0F, -2.0F, 1.0F, 5.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r47 = pillars3.addChild("cube_r47", ModelPartBuilder.create().uv(0, 0).cuboid(7.5948F, -10.45F, -3.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r48 = pillars3.addChild("cube_r48", ModelPartBuilder.create().uv(0, 15).cuboid(7.5948F, -10.45F, 0.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r49 = pillars3.addChild("cube_r49", ModelPartBuilder.create().uv(0, 81).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, 8.366F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r50 = pillars3.addChild("cube_r50", ModelPartBuilder.create().uv(54, 26).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, 8.387F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r51 = pillars3.addChild("cube_r51", ModelPartBuilder.create().uv(85, 81).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 7.5F, 0.4819F, 0.0851F, -1.7445F));

		ModelPartData cube_r52 = pillars3.addChild("cube_r52", ModelPartBuilder.create().uv(13, 81).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r53 = pillars3.addChild("cube_r53", ModelPartBuilder.create().uv(56, 81).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.4835F, 0.116F, -1.7282F));

		ModelPartData cube_r54 = pillars3.addChild("cube_r54", ModelPartBuilder.create().uv(0, 45).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, 1.8362F, 0.4784F, -0.0403F, -1.2752F));

		ModelPartData cube_r55 = pillars3.addChild("cube_r55", ModelPartBuilder.create().uv(83, 53).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, -1.8362F, -0.4784F, 0.0403F, -1.2752F));

		ModelPartData cube_r56 = pillars3.addChild("cube_r56", ModelPartBuilder.create().uv(67, 81).cuboid(0.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.48F, 0.0F, -1.789F));

		ModelPartData cube_r57 = pillars3.addChild("cube_r57", ModelPartBuilder.create().uv(92, 53).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -7.5F, -0.4812F, -0.0697F, -1.7526F));

		ModelPartData cube_r58 = pillars3.addChild("cube_r58", ModelPartBuilder.create().uv(40, 94).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, -8.387F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r59 = pillars3.addChild("cube_r59", ModelPartBuilder.create().uv(78, 10).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, -8.366F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r60 = pillars3.addChild("cube_r60", ModelPartBuilder.create().uv(93, 10).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.4835F, -0.116F, -1.7282F));

		ModelPartData cube_r61 = pillars3.addChild("cube_r61", ModelPartBuilder.create().uv(93, 25).cuboid(0.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.48F, 0.0F, -1.789F));

		ModelPartData cube_r62 = pillars3.addChild("cube_r62", ModelPartBuilder.create().uv(76, 81).cuboid(-1.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r63 = pillars3.addChild("cube_r63", ModelPartBuilder.create().uv(26, 80).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r64 = pillars3.addChild("cube_r64", ModelPartBuilder.create().uv(93, 93).cuboid(-1.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r65 = pillars3.addChild("cube_r65", ModelPartBuilder.create().uv(25, 95).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r66 = pillars3.addChild("cube_r66", ModelPartBuilder.create().uv(0, 96).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, 0.0F, 0.48F, 0.0F));

		ModelPartData pillars4 = pillars3.addChild("pillars4", ModelPartBuilder.create().uv(35, 4).cuboid(18.5F, -13.5F, -10.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 30).cuboid(18.5F, -13.5F, 7.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r67 = pillars4.addChild("cube_r67", ModelPartBuilder.create().uv(59, 54).cuboid(-6.0F, -2.0F, 1.0F, 7.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 1.309F));

		ModelPartData cube_r68 = pillars4.addChild("cube_r68", ModelPartBuilder.create().uv(54, 26).cuboid(-1.0F, -2.0F, 1.0F, 5.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r69 = pillars4.addChild("cube_r69", ModelPartBuilder.create().uv(0, 0).cuboid(7.5948F, -10.45F, -3.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r70 = pillars4.addChild("cube_r70", ModelPartBuilder.create().uv(0, 15).cuboid(7.5948F, -10.45F, 0.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r71 = pillars4.addChild("cube_r71", ModelPartBuilder.create().uv(0, 81).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, 8.366F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r72 = pillars4.addChild("cube_r72", ModelPartBuilder.create().uv(54, 26).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, 8.387F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r73 = pillars4.addChild("cube_r73", ModelPartBuilder.create().uv(85, 81).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 7.5F, 0.4819F, 0.0851F, -1.7445F));

		ModelPartData cube_r74 = pillars4.addChild("cube_r74", ModelPartBuilder.create().uv(13, 81).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r75 = pillars4.addChild("cube_r75", ModelPartBuilder.create().uv(56, 81).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.4835F, 0.116F, -1.7282F));

		ModelPartData cube_r76 = pillars4.addChild("cube_r76", ModelPartBuilder.create().uv(0, 45).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, 1.8362F, 0.4784F, -0.0403F, -1.2752F));

		ModelPartData cube_r77 = pillars4.addChild("cube_r77", ModelPartBuilder.create().uv(83, 53).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, -1.8362F, -0.4784F, 0.0403F, -1.2752F));

		ModelPartData cube_r78 = pillars4.addChild("cube_r78", ModelPartBuilder.create().uv(67, 81).cuboid(0.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.48F, 0.0F, -1.789F));

		ModelPartData cube_r79 = pillars4.addChild("cube_r79", ModelPartBuilder.create().uv(92, 53).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -7.5F, -0.4812F, -0.0697F, -1.7526F));

		ModelPartData cube_r80 = pillars4.addChild("cube_r80", ModelPartBuilder.create().uv(40, 94).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, -8.387F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r81 = pillars4.addChild("cube_r81", ModelPartBuilder.create().uv(78, 10).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, -8.366F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r82 = pillars4.addChild("cube_r82", ModelPartBuilder.create().uv(93, 10).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.4835F, -0.116F, -1.7282F));

		ModelPartData cube_r83 = pillars4.addChild("cube_r83", ModelPartBuilder.create().uv(93, 25).cuboid(0.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.48F, 0.0F, -1.789F));

		ModelPartData cube_r84 = pillars4.addChild("cube_r84", ModelPartBuilder.create().uv(76, 81).cuboid(-1.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r85 = pillars4.addChild("cube_r85", ModelPartBuilder.create().uv(26, 80).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r86 = pillars4.addChild("cube_r86", ModelPartBuilder.create().uv(93, 93).cuboid(-1.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r87 = pillars4.addChild("cube_r87", ModelPartBuilder.create().uv(25, 95).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r88 = pillars4.addChild("cube_r88", ModelPartBuilder.create().uv(0, 96).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, 0.0F, 0.48F, 0.0F));

		ModelPartData pillars5 = pillars4.addChild("pillars5", ModelPartBuilder.create().uv(35, 4).cuboid(18.5F, -13.5F, -10.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 30).cuboid(18.5F, -13.5F, 7.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r89 = pillars5.addChild("cube_r89", ModelPartBuilder.create().uv(31, 51).cuboid(-8.0F, -2.0F, 1.0F, 9.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 1.309F));

		ModelPartData cube_r90 = pillars5.addChild("cube_r90", ModelPartBuilder.create().uv(54, 26).cuboid(-1.0F, -2.0F, 1.0F, 5.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r91 = pillars5.addChild("cube_r91", ModelPartBuilder.create().uv(0, 0).cuboid(7.5948F, -10.45F, -3.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r92 = pillars5.addChild("cube_r92", ModelPartBuilder.create().uv(0, 15).cuboid(7.5948F, -10.45F, 0.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r93 = pillars5.addChild("cube_r93", ModelPartBuilder.create().uv(0, 81).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, 8.366F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r94 = pillars5.addChild("cube_r94", ModelPartBuilder.create().uv(54, 26).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, 8.387F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r95 = pillars5.addChild("cube_r95", ModelPartBuilder.create().uv(85, 81).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 7.5F, 0.4819F, 0.0851F, -1.7445F));

		ModelPartData cube_r96 = pillars5.addChild("cube_r96", ModelPartBuilder.create().uv(13, 81).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r97 = pillars5.addChild("cube_r97", ModelPartBuilder.create().uv(56, 81).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.4835F, 0.116F, -1.7282F));

		ModelPartData cube_r98 = pillars5.addChild("cube_r98", ModelPartBuilder.create().uv(0, 45).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, 1.8362F, 0.4784F, -0.0403F, -1.2752F));

		ModelPartData cube_r99 = pillars5.addChild("cube_r99", ModelPartBuilder.create().uv(83, 53).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, -1.8362F, -0.4784F, 0.0403F, -1.2752F));

		ModelPartData cube_r100 = pillars5.addChild("cube_r100", ModelPartBuilder.create().uv(67, 81).cuboid(0.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.48F, 0.0F, -1.789F));

		ModelPartData cube_r101 = pillars5.addChild("cube_r101", ModelPartBuilder.create().uv(92, 53).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -7.5F, -0.4812F, -0.0697F, -1.7526F));

		ModelPartData cube_r102 = pillars5.addChild("cube_r102", ModelPartBuilder.create().uv(40, 94).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, -8.387F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r103 = pillars5.addChild("cube_r103", ModelPartBuilder.create().uv(78, 10).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, -8.366F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r104 = pillars5.addChild("cube_r104", ModelPartBuilder.create().uv(93, 10).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.4835F, -0.116F, -1.7282F));

		ModelPartData cube_r105 = pillars5.addChild("cube_r105", ModelPartBuilder.create().uv(93, 25).cuboid(0.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.48F, 0.0F, -1.789F));

		ModelPartData cube_r106 = pillars5.addChild("cube_r106", ModelPartBuilder.create().uv(76, 81).cuboid(-1.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r107 = pillars5.addChild("cube_r107", ModelPartBuilder.create().uv(26, 80).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r108 = pillars5.addChild("cube_r108", ModelPartBuilder.create().uv(93, 93).cuboid(-1.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r109 = pillars5.addChild("cube_r109", ModelPartBuilder.create().uv(25, 95).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r110 = pillars5.addChild("cube_r110", ModelPartBuilder.create().uv(0, 96).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, 0.0F, 0.48F, 0.0F));

		ModelPartData pillars6 = pillars5.addChild("pillars6", ModelPartBuilder.create().uv(35, 4).cuboid(18.5F, -13.5F, -10.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 30).cuboid(18.5F, -13.5F, 7.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r111 = pillars6.addChild("cube_r111", ModelPartBuilder.create().uv(59, 54).cuboid(-6.0F, -2.0F, 1.0F, 7.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 1.309F));

		ModelPartData cube_r112 = pillars6.addChild("cube_r112", ModelPartBuilder.create().uv(54, 26).cuboid(-1.0F, -2.0F, 1.0F, 5.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r113 = pillars6.addChild("cube_r113", ModelPartBuilder.create().uv(0, 0).cuboid(7.5948F, -10.45F, -3.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r114 = pillars6.addChild("cube_r114", ModelPartBuilder.create().uv(0, 15).cuboid(7.5948F, -10.45F, 0.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r115 = pillars6.addChild("cube_r115", ModelPartBuilder.create().uv(0, 81).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, 8.366F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r116 = pillars6.addChild("cube_r116", ModelPartBuilder.create().uv(54, 26).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, 8.387F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r117 = pillars6.addChild("cube_r117", ModelPartBuilder.create().uv(85, 81).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 7.5F, 0.4819F, 0.0851F, -1.7445F));

		ModelPartData cube_r118 = pillars6.addChild("cube_r118", ModelPartBuilder.create().uv(13, 81).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r119 = pillars6.addChild("cube_r119", ModelPartBuilder.create().uv(56, 81).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.4835F, 0.116F, -1.7282F));

		ModelPartData cube_r120 = pillars6.addChild("cube_r120", ModelPartBuilder.create().uv(0, 45).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, 1.8362F, 0.4784F, -0.0403F, -1.2752F));

		ModelPartData cube_r121 = pillars6.addChild("cube_r121", ModelPartBuilder.create().uv(83, 53).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, -1.8362F, -0.4784F, 0.0403F, -1.2752F));

		ModelPartData cube_r122 = pillars6.addChild("cube_r122", ModelPartBuilder.create().uv(67, 81).cuboid(0.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.48F, 0.0F, -1.789F));

		ModelPartData cube_r123 = pillars6.addChild("cube_r123", ModelPartBuilder.create().uv(92, 53).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -7.5F, -0.4812F, -0.0697F, -1.7526F));

		ModelPartData cube_r124 = pillars6.addChild("cube_r124", ModelPartBuilder.create().uv(40, 94).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, -8.387F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r125 = pillars6.addChild("cube_r125", ModelPartBuilder.create().uv(78, 10).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, -8.366F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r126 = pillars6.addChild("cube_r126", ModelPartBuilder.create().uv(93, 10).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.4835F, -0.116F, -1.7282F));

		ModelPartData cube_r127 = pillars6.addChild("cube_r127", ModelPartBuilder.create().uv(93, 25).cuboid(0.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.48F, 0.0F, -1.789F));

		ModelPartData cube_r128 = pillars6.addChild("cube_r128", ModelPartBuilder.create().uv(76, 81).cuboid(-1.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r129 = pillars6.addChild("cube_r129", ModelPartBuilder.create().uv(26, 80).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r130 = pillars6.addChild("cube_r130", ModelPartBuilder.create().uv(93, 93).cuboid(-1.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r131 = pillars6.addChild("cube_r131", ModelPartBuilder.create().uv(25, 95).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r132 = pillars6.addChild("cube_r132", ModelPartBuilder.create().uv(0, 96).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, 0.0F, 0.48F, 0.0F));

		ModelPartData bottom = bone.addChild("bottom", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData rotor = bottom.addChild("rotor", ModelPartBuilder.create().uv(41, 80).cuboid(4.2F, -9.0F, -3.0F, 1.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		ModelPartData base = bottom.addChild("base", ModelPartBuilder.create().uv(39, 5).cuboid(-0.35F, -8.0F, -5.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panels7 = bottom.addChild("panels7", ModelPartBuilder.create().uv(31, 63).cuboid(8.0F, -11.0F, -2.0F, 11.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r133 = panels7.addChild("cube_r133", ModelPartBuilder.create().uv(0, 45).cuboid(-1.0F, 0.0F, -7.0F, 1.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(16.4545F, -8.0F, 9.5F, -0.5087F, -0.1298F, 1.3428F));

		ModelPartData cube_r134 = panels7.addChild("cube_r134", ModelPartBuilder.create().uv(66, 47).cuboid(-11.0F, -0.999F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -9.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData cube_r135 = panels7.addChild("cube_r135", ModelPartBuilder.create().uv(58, 66).cuboid(-11.0F, -2.0F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(18.3156F, -9.1197F, 2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData bottom2 = bottom.addChild("bottom2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rotor2 = bottom2.addChild("rotor2", ModelPartBuilder.create().uv(41, 80).cuboid(4.2F, -9.0F, -3.0F, 1.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		ModelPartData base2 = bottom2.addChild("base2", ModelPartBuilder.create().uv(39, 5).cuboid(-0.35F, -8.0F, -5.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panels2 = bottom2.addChild("panels2", ModelPartBuilder.create().uv(31, 63).cuboid(8.0F, -11.0F, -2.0F, 11.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r136 = panels2.addChild("cube_r136", ModelPartBuilder.create().uv(0, 45).cuboid(-1.0F, 0.0F, -7.0F, 1.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(16.4545F, -8.0F, 9.5F, -0.5087F, -0.1298F, 1.3428F));

		ModelPartData cube_r137 = panels2.addChild("cube_r137", ModelPartBuilder.create().uv(66, 47).cuboid(-11.0F, -0.999F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -9.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData cube_r138 = panels2.addChild("cube_r138", ModelPartBuilder.create().uv(58, 66).cuboid(-11.0F, -2.0F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(18.3156F, -9.1197F, 2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData bottom3 = bottom2.addChild("bottom3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rotor3 = bottom3.addChild("rotor3", ModelPartBuilder.create().uv(41, 80).cuboid(4.2F, -9.0F, -3.0F, 1.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		ModelPartData base3 = bottom3.addChild("base3", ModelPartBuilder.create().uv(39, 5).cuboid(-0.35F, -8.0F, -5.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panels3 = bottom3.addChild("panels3", ModelPartBuilder.create().uv(31, 63).cuboid(8.0F, -11.0F, -2.0F, 11.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r139 = panels3.addChild("cube_r139", ModelPartBuilder.create().uv(0, 45).cuboid(-1.0F, 0.0F, -7.0F, 1.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(16.4545F, -8.0F, 9.5F, -0.5087F, -0.1298F, 1.3428F));

		ModelPartData cube_r140 = panels3.addChild("cube_r140", ModelPartBuilder.create().uv(66, 47).cuboid(-11.0F, -0.999F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -9.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData cube_r141 = panels3.addChild("cube_r141", ModelPartBuilder.create().uv(58, 66).cuboid(-11.0F, -2.0F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(18.3156F, -9.1197F, 2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData bottom4 = bottom3.addChild("bottom4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rotor4 = bottom4.addChild("rotor4", ModelPartBuilder.create().uv(41, 80).cuboid(4.2F, -9.0F, -3.0F, 1.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		ModelPartData base4 = bottom4.addChild("base4", ModelPartBuilder.create().uv(39, 5).cuboid(-0.35F, -8.0F, -5.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panels4 = bottom4.addChild("panels4", ModelPartBuilder.create().uv(31, 63).cuboid(8.0F, -11.0F, -2.0F, 11.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r142 = panels4.addChild("cube_r142", ModelPartBuilder.create().uv(0, 45).cuboid(-1.0F, 0.0F, -7.0F, 1.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(16.4545F, -8.0F, 9.5F, -0.5087F, -0.1298F, 1.3428F));

		ModelPartData cube_r143 = panels4.addChild("cube_r143", ModelPartBuilder.create().uv(66, 47).cuboid(-11.0F, -0.999F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -9.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData cube_r144 = panels4.addChild("cube_r144", ModelPartBuilder.create().uv(58, 66).cuboid(-11.0F, -2.0F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(18.3156F, -9.1197F, 2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData bottom5 = bottom4.addChild("bottom5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rotor5 = bottom5.addChild("rotor5", ModelPartBuilder.create().uv(41, 80).cuboid(4.2F, -9.0F, -3.0F, 1.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		ModelPartData base5 = bottom5.addChild("base5", ModelPartBuilder.create().uv(39, 5).cuboid(-0.35F, -8.0F, -5.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panels5 = bottom5.addChild("panels5", ModelPartBuilder.create().uv(31, 63).cuboid(8.0F, -11.0F, -2.0F, 11.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r145 = panels5.addChild("cube_r145", ModelPartBuilder.create().uv(0, 45).cuboid(-1.0F, 0.0F, -7.0F, 1.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(16.4545F, -8.0F, 9.5F, -0.5087F, -0.1298F, 1.3428F));

		ModelPartData cube_r146 = panels5.addChild("cube_r146", ModelPartBuilder.create().uv(66, 47).cuboid(-11.0F, -0.999F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -9.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData cube_r147 = panels5.addChild("cube_r147", ModelPartBuilder.create().uv(58, 66).cuboid(-11.0F, -2.0F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(18.3156F, -9.1197F, 2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData bottom6 = bottom5.addChild("bottom6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rotor6 = bottom6.addChild("rotor6", ModelPartBuilder.create().uv(41, 80).cuboid(4.2F, -9.0F, -3.0F, 1.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		ModelPartData base6 = bottom6.addChild("base6", ModelPartBuilder.create().uv(39, 5).cuboid(-0.35F, -8.0F, -5.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panels6 = bottom6.addChild("panels6", ModelPartBuilder.create().uv(31, 63).cuboid(8.0F, -11.0F, -2.0F, 11.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r148 = panels6.addChild("cube_r148", ModelPartBuilder.create().uv(0, 45).cuboid(-1.0F, 0.0F, -7.0F, 1.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(16.4545F, -8.0F, 9.5F, -0.5087F, -0.1298F, 1.3428F));

		ModelPartData cube_r149 = panels6.addChild("cube_r149", ModelPartBuilder.create().uv(66, 47).cuboid(-11.0F, -0.999F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -9.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData cube_r150 = panels6.addChild("cube_r150", ModelPartBuilder.create().uv(58, 66).cuboid(-11.0F, -2.0F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(18.3156F, -9.1197F, 2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData controls = bone.addChild("controls", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panel_1 = controls.addChild("panel_1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData rot7 = panel_1.addChild("rot7", ModelPartBuilder.create(), ModelTransform.of(20.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData atomic_acc = rot7.addChild("atomic_acc", ModelPartBuilder.create().uv(96, 77).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(86, 103).cuboid(-0.5F, -1.25F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.0482F, 0.2624F, 0.0F, 0.0F, 0.0F, -0.5236F));

		ModelPartData bone2 = atomic_acc.addChild("bone2", ModelPartBuilder.create().uv(103, 57).cuboid(-0.5F, -1.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(99, 90).cuboid(-0.5F, -1.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

		ModelPartData cube_r151 = bone2.addChild("cube_r151", ModelPartBuilder.create().uv(74, 98).cuboid(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r152 = bone2.addChild("cube_r152", ModelPartBuilder.create().uv(49, 99).cuboid(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 1.5708F, 0.0F, 3.1416F));

		ModelPartData cube_r153 = bone2.addChild("cube_r153", ModelPartBuilder.create().uv(99, 90).cuboid(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData cube_r154 = bone2.addChild("cube_r154", ModelPartBuilder.create().uv(49, 99).cuboid(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r155 = bone2.addChild("cube_r155", ModelPartBuilder.create().uv(49, 99).cuboid(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData button = rot7.addChild("button", ModelPartBuilder.create().uv(103, 54).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(103, 51).cuboid(-0.5F, -1.2F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.75F, 0.6F, 0.0F));

		ModelPartData bone3 = button.addChild("bone3", ModelPartBuilder.create().uv(50, 103).cuboid(-0.5F, -1.55F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData button2 = rot7.addChild("button2", ModelPartBuilder.create().uv(33, 103).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(28, 103).cuboid(-0.5F, -1.4F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(-2.25F, 0.5F, -2.0F));

		ModelPartData bone4 = button2.addChild("bone4", ModelPartBuilder.create().uv(102, 103).cuboid(-0.5F, -1.75F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.4F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData keyboard = rot7.addChild("keyboard", ModelPartBuilder.create().uv(23, 70).cuboid(-2.0F, -1.0F, -4.0F, 5.0F, 1.0F, 8.0F, new Dilation(-0.3F))
		.uv(73, 72).cuboid(-1.5F, -1.25F, -3.5F, 4.0F, 1.0F, 7.0F, new Dilation(-0.2F))
		.uv(50, 72).cuboid(-1.5F, -1.55F, -3.5F, 4.0F, 1.0F, 7.0F, new Dilation(-0.4F))
		.uv(17, 45).cuboid(-1.5F, -1.65F, -3.5F, 4.0F, 1.0F, 7.0F, new Dilation(-0.4F)), ModelTransform.pivot(3.35F, 2.5F, 0.0F));

		ModelPartData cash_reg = rot7.addChild("cash_reg", ModelPartBuilder.create().uv(18, 96).cuboid(-0.9F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.3F))
		.uv(23, 103).cuboid(-0.2F, -0.7F, -1.4F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(2.0F, 2.0F, -9.0F, 0.0F, 0.0F, -0.5672F));

		ModelPartData cube_r156 = cash_reg.addChild("cube_r156", ModelPartBuilder.create().uv(67, 98).cuboid(-0.15F, -1.25F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-0.5F, -0.5F, 0.0F, 0.0F, 0.0F, 0.7854F));

		ModelPartData pump = rot7.addChild("pump", ModelPartBuilder.create().uv(58, 96).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.9661F, 2.03F, -5.0F, 0.0F, -1.0036F, -0.5672F));

		ModelPartData bone5 = pump.addChild("bone5", ModelPartBuilder.create().uv(9, 96).cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 3.0F, new Dilation(-0.3F))
		.uv(97, 48).cuboid(-1.0F, -1.0F, -2.7F, 2.0F, 2.0F, 1.0F, new Dilation(-0.4F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData pump2 = rot7.addChild("pump2", ModelPartBuilder.create().uv(58, 96).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.9661F, 2.03F, 5.0F, 0.0F, -0.5672F, -0.5672F));

		ModelPartData bone6 = pump2.addChild("bone6", ModelPartBuilder.create().uv(9, 96).cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 3.0F, new Dilation(-0.3F))
		.uv(97, 48).cuboid(-1.0F, -1.0F, -2.7F, 2.0F, 2.0F, 1.0F, new Dilation(-0.4F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData lever = rot7.addChild("lever", ModelPartBuilder.create().uv(96, 82).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.1F))
		.uv(103, 66).cuboid(0.85F, -1.9F, -1.1F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(103, 66).cuboid(0.85F, -1.9F, 0.1F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(81, 103).cuboid(-1.9F, -1.7F, -1.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(81, 103).cuboid(-1.9F, -1.7F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-3.5F, 0.5F, -6.9F, 0.0F, 0.48F, 0.0F));

		ModelPartData bone7 = lever.addChild("bone7", ModelPartBuilder.create().uv(35, 4).cuboid(-0.75F, -0.75F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(102, 100).cuboid(-0.5F, -0.5F, -0.65F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(102, 97).cuboid(-0.5F, -3.0F, -0.9F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(0.5F, -1.5F, 1.15F, 0.0F, 0.0F, 0.3927F));

		ModelPartData cube_r157 = bone7.addChild("cube_r157", ModelPartBuilder.create().uv(10, 15).cuboid(-1.0F, -2.0F, -0.001F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.25F, -0.75F, 0.001F, 0.2618F, 0.0F, 0.0F));

		ModelPartData bone8 = lever.addChild("bone8", ModelPartBuilder.create().uv(35, 4).cuboid(-0.75F, -0.75F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(102, 97).cuboid(-0.5F, -3.0F, -0.1F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(102, 100).cuboid(-0.5F, -0.5F, -0.35F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(0.5F, -1.5F, -1.15F, 0.0F, 0.0F, 0.3927F));

		ModelPartData cube_r158 = bone8.addChild("cube_r158", ModelPartBuilder.create().uv(10, 15).cuboid(-1.0F, -2.0F, -0.001F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.25F, -0.75F, 0.001F, -0.2618F, 0.0F, 0.0F));

		ModelPartData bell = rot7.addChild("bell", ModelPartBuilder.create().uv(0, 40).cuboid(-2.0F, -2.0F, -1.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(51, 1).cuboid(-7.75F, -1.5F, -1.8F, 6.0F, 0.0F, 3.0F, new Dilation(0.0F))
		.uv(35, 0).cuboid(-7.75F, -1.8F, -1.8F, 6.0F, 0.0F, 3.0F, new Dilation(0.0F))
		.uv(35, 42).cuboid(-5.0F, -1.55F, -1.4F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(103, 86).cuboid(-5.1F, -2.25F, -1.4F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(103, 86).cuboid(-4.5F, -2.25F, -1.4F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(103, 86).cuboid(-3.9F, -2.25F, -1.4F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(103, 86).cuboid(-4.5F, -2.25F, -0.3F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(103, 86).cuboid(-5.1F, -2.25F, -0.3F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(103, 86).cuboid(-3.9F, -2.25F, -0.3F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(35, 42).cuboid(-5.0F, -1.55F, -0.3F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(103, 63).cuboid(-6.05F, -2.15F, -0.3F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F))
		.uv(87, 0).cuboid(2.45F, -1.85F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(43, 4).cuboid(2.45F, -2.05F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(8, 30).cuboid(2.7F, -2.25F, -0.25F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(73, 81).cuboid(1.7F, -1.85F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(95, 6).cuboid(-2.0F, -2.5F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F))
		.uv(95, 6).cuboid(0.0F, -2.5F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-5.0F, 0.75F, 6.1F, 0.0227F, -0.4795F, -0.0492F));

		ModelPartData bone9 = bell.addChild("bone9", ModelPartBuilder.create().uv(6, 45).cuboid(-1.65F, -0.1F, -0.65F, 2.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-1.0F, -2.5F, 0.0F, 0.0F, -0.1745F, 0.0F));

		ModelPartData bone10 = bell.addChild("bone10", ModelPartBuilder.create().uv(6, 45).cuboid(-1.65F, -0.1F, -0.65F, 2.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(1.0F, -2.5F, 0.0F, 0.0F, 1.2217F, 0.0F));

		ModelPartData bone11 = rot7.addChild("bone11", ModelPartBuilder.create().uv(96, 86).cuboid(-0.75F, -1.1F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.1F))
		.uv(50, 76).cuboid(-3.05F, -1.55F, 0.5F, 2.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(50, 76).cuboid(-2.25F, -1.55F, 1.1F, 2.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(34, 100).cuboid(0.25F, -0.9F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
		.uv(83, 96).cuboid(-0.75F, -1.35F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(-0.1F))
		.uv(76, 102).cuboid(-2.15F, -1.15F, -0.55F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(44, 30).cuboid(-0.75F, -2.55F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.35F))
		.uv(44, 30).cuboid(-0.75F, -2.55F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.35F))
		.uv(102, 94).cuboid(-0.75F, -2.55F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(102, 94).cuboid(-0.75F, -2.55F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(-7.0F, 0.5F, -5.1F, 0.0F, 0.0F, -0.0436F));

		ModelPartData bone12 = rot7.addChild("bone12", ModelPartBuilder.create().uv(68, 0).cuboid(-1.8107F, 0.2016F, -4.0F, 5.0F, 1.0F, 8.0F, new Dilation(0.0F))
		.uv(0, 72).cuboid(-1.8107F, 0.4016F, -4.0F, 5.0F, 0.0F, 8.0F, new Dilation(0.0F))
		.uv(102, 74).cuboid(-1.55F, -0.1F, -1.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-12.55F, -2.15F, 0.0F, 0.0F, 0.0F, 0.5236F));

		ModelPartData valve = rot7.addChild("valve", ModelPartBuilder.create().uv(71, 102).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(66, 102).cuboid(0.25F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(50, 81).cuboid(-0.5F, -1.25F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-17.75F, -4.25F, 0.5F, 0.0F, 0.0F, -0.6981F));

		ModelPartData bone13 = valve.addChild("bone13", ModelPartBuilder.create().uv(101, 0).cuboid(-1.05F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(-0.4F))
		.uv(45, 102).cuboid(-0.05F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(74, 26).cuboid(0.7F, -1.25F, -1.25F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(103, 60).cuboid(0.15F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(1.0F, -1.5F, 0.0F, 0.7854F, 0.0F, 0.0F));

		ModelPartData valve2 = rot7.addChild("valve2", ModelPartBuilder.create().uv(71, 102).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(66, 102).cuboid(0.25F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(50, 81).cuboid(-0.5F, -1.25F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-17.5F, -4.0F, 2.0F, -0.3054F, 0.0F, -0.6981F));

		ModelPartData bone14 = valve2.addChild("bone14", ModelPartBuilder.create().uv(101, 0).cuboid(-1.05F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(-0.4F))
		.uv(45, 102).cuboid(-0.05F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(74, 26).cuboid(0.7F, -1.25F, -1.25F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(103, 60).cuboid(0.15F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(1.0F, -1.5F, 0.0F, 1.0472F, 0.0F, 0.0F));

		ModelPartData cables2 = rot7.addChild("cables2", ModelPartBuilder.create().uv(99, 25).cuboid(-0.25F, 0.5F, -6.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 55).cuboid(-1.5F, 1.0F, 5.25F, 4.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 8.25F, 0.0F, 0.0F, 0.0F, 2.3562F));

		ModelPartData cube_r159 = cables2.addChild("cube_r159", ModelPartBuilder.create().uv(74, 25).cuboid(-1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		ModelPartData cube_r160 = cables2.addChild("cube_r160", ModelPartBuilder.create().uv(77, 37).cuboid(-1.0F, 0.0F, -6.0F, 3.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		ModelPartData panel_2 = controls.addChild("panel_2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rot8 = panel_2.addChild("rot8", ModelPartBuilder.create(), ModelTransform.of(20.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData cube_r161 = rot8.addChild("cube_r161", ModelPartBuilder.create().uv(59, 51).cuboid(0.1F, 1.5F, -2.0F, 0.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5672F));

		ModelPartData lever2 = rot8.addChild("lever2", ModelPartBuilder.create().uv(66, 72).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(19, 71).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.001F))
		.uv(49, 94).cuboid(-0.75F, -2.75F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.1F))
		.uv(89, 74).cuboid(-0.5F, -2.0F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-5.5F, -0.35F, 5.75F, 0.0F, -0.48F, 0.0F));

		ModelPartData lights2 = lever2.addChild("lights2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData light = lights2.addChild("light", ModelPartBuilder.create().uv(68, 4).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.03F)), ModelTransform.pivot(0.0F, -1.5F, 0.0F));

		ModelPartData light2 = lights2.addChild("light2", ModelPartBuilder.create().uv(68, 4).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.03F)), ModelTransform.pivot(0.0F, -1.5F, 0.0F));

		ModelPartData light3 = lights2.addChild("light3", ModelPartBuilder.create().uv(68, 4).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.03F)), ModelTransform.of(0.0F, -1.5F, 0.0F, 0.0F, 0.0F, -1.5708F));

		ModelPartData light4 = lights2.addChild("light4", ModelPartBuilder.create().uv(68, 4).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.03F)), ModelTransform.of(0.0F, -1.5F, 0.0F, 0.0F, 0.0F, -1.5708F));

		ModelPartData light5 = lights2.addChild("light5", ModelPartBuilder.create().uv(68, 4).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.03F)), ModelTransform.of(0.0F, -1.5F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData bone29 = lever2.addChild("bone29", ModelPartBuilder.create().uv(0, 15).cuboid(-0.75F, -2.25F, -1.25F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 76).cuboid(-0.5F, -3.05F, -1.85F, 1.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, -1.5F, 0.0F, 0.0F, 0.0F, -1.309F));

		ModelPartData bone30 = lever2.addChild("bone30", ModelPartBuilder.create().uv(0, 76).cuboid(-0.5F, -3.05F, -0.2F, 1.0F, 1.0F, 2.0F, new Dilation(-0.2F))
		.uv(0, 15).cuboid(-0.75F, -2.25F, 1.25F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.5F, 0.0F, 0.0F, 0.0F, -1.2217F));

		ModelPartData disc = rot8.addChild("disc", ModelPartBuilder.create().uv(73, 76).cuboid(-1.75F, 0.3F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
		.uv(85, 66).cuboid(-2.6F, 0.2F, -1.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
		.uv(40, 102).cuboid(1.85F, 0.15F, -2.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.pivot(-4.0F, -0.5F, 0.0F));

		ModelPartData cube_r162 = disc.addChild("cube_r162", ModelPartBuilder.create().uv(85, 66).cuboid(-2.6F, 0.0F, -1.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r163 = disc.addChild("cube_r163", ModelPartBuilder.create().uv(85, 66).cuboid(-2.6F, 0.0F, -1.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r164 = disc.addChild("cube_r164", ModelPartBuilder.create().uv(85, 66).cuboid(-2.6F, 0.0F, -1.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData cube_r165 = disc.addChild("cube_r165", ModelPartBuilder.create().uv(85, 66).cuboid(-2.6F, 0.0F, -1.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
		.uv(73, 76).cuboid(-1.75F, 0.1F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r166 = disc.addChild("cube_r166", ModelPartBuilder.create().uv(85, 66).cuboid(-2.6F, 0.0F, -1.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
		.uv(73, 76).cuboid(-1.75F, 0.1F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r167 = disc.addChild("cube_r167", ModelPartBuilder.create().uv(73, 76).cuboid(-1.75F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.3F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r168 = disc.addChild("cube_r168", ModelPartBuilder.create().uv(73, 76).cuboid(-1.75F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.3F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r169 = disc.addChild("cube_r169", ModelPartBuilder.create().uv(73, 76).cuboid(-1.75F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.3F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData bone31 = disc.addChild("bone31", ModelPartBuilder.create().uv(0, 30).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(17, 50).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.75F, 0.05F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData bone34 = disc.addChild("bone34", ModelPartBuilder.create().uv(0, 30).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(17, 50).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.75F, 0.05F, -1.0F, 0.0F, -0.4363F, 0.0F));

		ModelPartData bone35 = disc.addChild("bone35", ModelPartBuilder.create().uv(0, 30).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(17, 50).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.75F, 0.05F, 1.0F, 0.0F, 0.4363F, 0.0F));

		ModelPartData bone32 = disc.addChild("bone32", ModelPartBuilder.create().uv(0, 30).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(17, 50).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.05F, -1.75F, 0.0F, -1.5708F, 0.0F));

		ModelPartData bone33 = disc.addChild("bone33", ModelPartBuilder.create().uv(0, 30).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(17, 50).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.05F, 1.75F, 0.0F, 1.5708F, 0.0F));

		ModelPartData dial = rot8.addChild("dial", ModelPartBuilder.create().uv(9, 48).cuboid(0.0F, -1.25F, -1.25F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(102, 37).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.25F, 0.9F, 9.0F, 0.0F, 0.0F, -0.5672F));

		ModelPartData crank = rot8.addChild("crank", ModelPartBuilder.create().uv(10, 0).cuboid(-0.2F, -0.5F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.2686F, 0.6717F, -9.75F, 0.0F, 0.0F, -0.5672F));

		ModelPartData bone36 = crank.addChild("bone36", ModelPartBuilder.create().uv(81, 100).cuboid(-0.6F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(102, 34).cuboid(0.8F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(35, 15).cuboid(1.1F, -0.25F, -0.25F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.25F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		ModelPartData crank2 = rot8.addChild("crank2", ModelPartBuilder.create().uv(10, 0).cuboid(-0.2F, -0.5F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.2686F, 0.6717F, -8.25F, 0.0F, 0.0F, -0.5672F));

		ModelPartData bone37 = crank2.addChild("bone37", ModelPartBuilder.create().uv(81, 100).cuboid(-0.6F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(102, 34).cuboid(0.8F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(35, 15).cuboid(1.1F, -0.25F, -0.25F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		ModelPartData button3 = rot8.addChild("button3", ModelPartBuilder.create().uv(42, 42).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -0.25F, -4.0F));

		ModelPartData bone38 = button3.addChild("bone38", ModelPartBuilder.create().uv(102, 31).cuboid(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cables = rot8.addChild("cables", ModelPartBuilder.create().uv(99, 25).cuboid(-0.25F, 0.5F, -6.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 55).cuboid(-1.5F, 1.0F, 5.25F, 4.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 8.25F, 0.0F, 0.0F, 0.0F, 2.3562F));

		ModelPartData cube_r170 = cables.addChild("cube_r170", ModelPartBuilder.create().uv(74, 25).cuboid(-1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		ModelPartData cube_r171 = cables.addChild("cube_r171", ModelPartBuilder.create().uv(77, 37).cuboid(-1.0F, 0.0F, -6.0F, 3.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		ModelPartData lever3 = rot8.addChild("lever3", ModelPartBuilder.create().uv(95, 74).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(95, 74).cuboid(-7.0F, -1.0F, -1.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(87, 25).cuboid(-1.0F, -1.8F, -1.0F, 2.0F, 1.0F, 1.0F, new Dilation(-0.1F))
		.uv(35, 26).cuboid(-6.0F, -1.7F, -1.0F, 6.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(87, 25).cuboid(-7.0F, -1.8F, -1.0F, 2.0F, 1.0F, 1.0F, new Dilation(-0.1F))
		.uv(61, 101).cuboid(-6.5F, -2.05F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(61, 101).cuboid(-0.5F, -2.05F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(83, 32).cuboid(-4.0F, -2.5F, -1.5F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(102, 28).cuboid(-3.5F, -2.0F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, 0.0F, -6.75F, -0.0182F, 0.4796F, -0.0393F));

		ModelPartData bone39 = lever3.addChild("bone39", ModelPartBuilder.create().uv(41, 80).cuboid(0.0F, -0.75F, 0.0F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(56, 101).cuboid(-0.5F, -0.5F, 1.75F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-3.0F, -1.5F, 1.0F, 0.48F, 0.0F, 0.0F));

		ModelPartData cube_r172 = bone39.addChild("cube_r172", ModelPartBuilder.create().uv(41, 80).cuboid(0.0F, -0.75F, 0.25F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -0.25F, 0.0F, 0.0F, -1.5708F));

		ModelPartData panel = rot8.addChild("panel", ModelPartBuilder.create().uv(42, 70).cuboid(-1.0F, -0.7F, -3.0F, 3.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(93, 42).cuboid(-0.75F, -1.0F, -2.8F, 3.0F, 1.0F, 4.0F, new Dilation(-0.2F)), ModelTransform.of(-13.0F, -0.5F, 2.5F, 0.0F, 0.0F, 0.48F));

		ModelPartData cube_r173 = panel.addChild("cube_r173", ModelPartBuilder.create().uv(87, 0).cuboid(-2.75F, -0.4F, -4.2F, 4.0F, 0.0F, 5.0F, new Dilation(0.0F))
		.uv(89, 68).cuboid(-2.75F, -0.3F, -4.2F, 4.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		ModelPartData dial2 = panel.addChild("dial2", ModelPartBuilder.create().uv(19, 101).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F))
		.uv(35, 0).cuboid(-0.5F, -1.75F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(1.25F, -0.1F, -2.25F));

		ModelPartData dial3 = panel.addChild("dial3", ModelPartBuilder.create().uv(19, 101).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(35, 0).cuboid(-0.5F, -1.75F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(1.25F, -0.1F, 0.25F));

		ModelPartData light15 = panel.addChild("light15", ModelPartBuilder.create().uv(14, 101).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(-0.5F, -0.75F, -2.4F));

		ModelPartData bone40 = light15.addChild("bone40", ModelPartBuilder.create().uv(9, 101).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.19F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData light16 = panel.addChild("light16", ModelPartBuilder.create().uv(14, 101).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(-0.5F, -0.75F, -1.5F));

		ModelPartData bone41 = light16.addChild("bone41", ModelPartBuilder.create().uv(9, 101).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.19F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData light17 = panel.addChild("light17", ModelPartBuilder.create().uv(14, 101).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(-0.5F, -0.75F, -0.6F));

		ModelPartData bone42 = light17.addChild("bone42", ModelPartBuilder.create().uv(9, 101).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.19F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData light18 = panel.addChild("light18", ModelPartBuilder.create().uv(14, 101).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(-0.5F, -0.75F, 0.3F));

		ModelPartData bone43 = light18.addChild("bone43", ModelPartBuilder.create().uv(9, 101).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.19F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData fluid_reservoir = rot8.addChild("fluid_reservoir", ModelPartBuilder.create().uv(0, 71).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-15.0F, -2.0F, -2.5F, 0.0F, 0.0F, -0.829F));

		ModelPartData cube_r174 = fluid_reservoir.addChild("cube_r174", ModelPartBuilder.create().uv(44, 36).cuboid(-1.2549F, -2.1001F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.0999F))
		.uv(88, 100).cuboid(-0.5049F, -2.6001F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(75, 72).cuboid(-0.5049F, -2.1001F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.75F, -1.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData cube_r175 = fluid_reservoir.addChild("cube_r175", ModelPartBuilder.create().uv(74, 32).cuboid(-0.5546F, -0.5912F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(1.75F, -1.25F, 0.0F, 0.0F, 0.0F, -0.3054F));

		ModelPartData cube_r176 = fluid_reservoir.addChild("cube_r176", ModelPartBuilder.create().uv(81, 44).cuboid(0.35F, -1.45F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(-0.099F))
		.uv(34, 95).cuboid(0.75F, -0.85F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(34, 95).cuboid(2.0F, -0.85F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(70, 44).cuboid(0.0F, -0.45F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(0.75F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		ModelPartData cube_r177 = fluid_reservoir.addChild("cube_r177", ModelPartBuilder.create().uv(89, 70).cuboid(-0.8158F, -0.9721F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(4.75F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5672F));

		ModelPartData needle = rot8.addChild("needle", ModelPartBuilder.create().uv(11, 33).cuboid(0.0F, -3.0F, -0.25F, 0.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(87, 28).cuboid(-0.5F, -2.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.pivot(-4.0F, 0.0F, 0.0F));

		ModelPartData cube_r178 = needle.addChild("cube_r178", ModelPartBuilder.create().uv(11, 33).cuboid(0.0F, -3.0F, -0.25F, 0.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData panel_3 = controls.addChild("panel_3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData rot9 = panel_3.addChild("rot9", ModelPartBuilder.create(), ModelTransform.of(20.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData meter = rot9.addChild("meter", ModelPartBuilder.create().uv(68, 10).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F))
		.uv(17, 45).cuboid(-0.15F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-14.0F, -1.0F, -3.25F, 0.0F, 0.0F, -0.829F));

		ModelPartData bone15 = meter.addChild("bone15", ModelPartBuilder.create().uv(0, 0).cuboid(0.85F, -1.0F, -0.25F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1F, 0.25F, 0.0F, 0.6109F, 0.0F, 0.0F));

		ModelPartData meter2 = rot9.addChild("meter2", ModelPartBuilder.create().uv(68, 10).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F))
		.uv(17, 45).cuboid(-0.15F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-14.0F, -1.0F, -1.15F, 0.0F, 0.0F, -0.829F));

		ModelPartData bone16 = meter2.addChild("bone16", ModelPartBuilder.create().uv(0, 0).cuboid(0.85F, -1.0F, -0.25F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1F, 0.25F, 0.0F, 0.6109F, 0.0F, 0.0F));

		ModelPartData meter3 = rot9.addChild("meter3", ModelPartBuilder.create().uv(68, 10).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F))
		.uv(17, 45).cuboid(-0.15F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-14.0F, -1.0F, 1.15F, 0.0F, 0.0F, -0.829F));

		ModelPartData bone17 = meter3.addChild("bone17", ModelPartBuilder.create().uv(0, 0).cuboid(0.85F, -1.0F, -0.25F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1F, 0.25F, 0.0F, 0.6109F, 0.0F, 0.0F));

		ModelPartData meter4 = rot9.addChild("meter4", ModelPartBuilder.create().uv(68, 10).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F))
		.uv(17, 45).cuboid(-0.15F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-14.0F, -1.0F, 3.25F, 0.0F, 0.0F, -0.829F));

		ModelPartData bone18 = meter4.addChild("bone18", ModelPartBuilder.create().uv(0, 0).cuboid(0.85F, -1.0F, -0.25F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1F, 0.25F, 0.0F, 0.6109F, 0.0F, 0.0F));

		ModelPartData bone19 = rot9.addChild("bone19", ModelPartBuilder.create().uv(35, 36).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 4.0F, new Dilation(0.2F))
		.uv(35, 30).cuboid(-1.25F, -2.1F, -0.75F, 2.0F, 1.0F, 4.0F, new Dilation(0.2F)), ModelTransform.of(-17.0F, -1.75F, -1.0F, 0.0F, 0.0F, 0.7418F));

		ModelPartData vent = rot9.addChild("vent", ModelPartBuilder.create().uv(17, 54).cuboid(-3.0F, -1.501F, -1.0F, 4.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.851F, 7.75F, 0.0227F, -0.4795F, -0.0492F));

		ModelPartData cube_r179 = vent.addChild("cube_r179", ModelPartBuilder.create().uv(28, 55).cuboid(-2.9F, 0.05F, -1.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -1.601F, 0.0F, 0.0F, 0.0F, 0.288F));

		ModelPartData levers = rot9.addChild("levers", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -1.0F));

		ModelPartData lever4 = levers.addChild("lever4", ModelPartBuilder.create().uv(67, 0).cuboid(-1.0F, -0.25F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.pivot(-3.55F, -0.15F, 2.0F));

		ModelPartData bone20 = lever4.addChild("bone20", ModelPartBuilder.create().uv(35, 19).cuboid(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(103, 69).cuboid(-0.25F, -1.3F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(-0.5F, 0.0F, -0.4F));

		ModelPartData lever5 = levers.addChild("lever5", ModelPartBuilder.create().uv(67, 0).cuboid(-1.0F, -0.25F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-2.75F, -0.15F, 3.25F, 0.0F, 0.7854F, 0.0F));

		ModelPartData bone21 = lever5.addChild("bone21", ModelPartBuilder.create().uv(35, 19).cuboid(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(103, 69).cuboid(-0.25F, -1.3F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(-0.5F, 0.0F, -0.4F));

		ModelPartData lever6 = levers.addChild("lever6", ModelPartBuilder.create().uv(67, 0).cuboid(-1.0F, -0.25F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-1.25F, -0.15F, 3.5F, 0.0F, 1.5708F, 0.0F));

		ModelPartData bone22 = lever6.addChild("bone22", ModelPartBuilder.create().uv(35, 19).cuboid(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(103, 69).cuboid(-0.25F, -1.3F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(-0.5F, 0.0F, -0.4F));

		ModelPartData needle2 = rot9.addChild("needle2", ModelPartBuilder.create().uv(125, 17).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(126, 18).cuboid(-0.25F, -2.6F, 0.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
		.uv(121, 21).cuboid(-0.5F, -0.8F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(-2.0F, -0.2F, -3.5F));

		ModelPartData cube_r180 = needle2.addChild("cube_r180", ModelPartBuilder.create().uv(126, 18).cuboid(-0.25F, -2.6F, 0.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData bone23 = rot9.addChild("bone23", ModelPartBuilder.create().uv(118, 36).cuboid(-1.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-6.25F, 1.75F, 0.0F, 0.0F, 0.0F, -0.5672F));

		ModelPartData bone24 = bone23.addChild("bone24", ModelPartBuilder.create().uv(124, 46).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F))
		.uv(124, 50).cuboid(-0.5F, -0.7F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.05F))
		.uv(124, 50).cuboid(-0.5F, -1.2F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.05F)), ModelTransform.pivot(1.0F, -2.0F, 0.0F));

		ModelPartData bone26 = bone24.addChild("bone26", ModelPartBuilder.create().uv(124, 46).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(114, 78).cuboid(-1.0F, -1.5F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.3F))
		.uv(116, 71).cuboid(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(-0.2F))
		.uv(117, 87).cuboid(-0.5F, -1.0F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(-0.4F)), ModelTransform.pivot(0.0F, -0.6F, 0.0F));

		ModelPartData bone27 = bone26.addChild("bone27", ModelPartBuilder.create().uv(120, 103).cuboid(-0.5F, -1.4F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.04F))
		.uv(116, 111).cuboid(-0.5F, -1.85F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.24F))
		.uv(124, 126).cuboid(-0.5F, -1.25F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.25F, 0.0F));

		ModelPartData stabilizers = rot9.addChild("stabilizers", ModelPartBuilder.create().uv(124, 59).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 0.5F, 4.5F));

		ModelPartData bone25 = stabilizers.addChild("bone25", ModelPartBuilder.create().uv(124, 65).cuboid(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F))
		.uv(124, 63).cuboid(-0.5F, -1.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

		ModelPartData rotor7 = bone.addChild("rotor7", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void renderWithAnimations(ConsoleBlockEntity console, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		matrices.push();

		matrices.translate(0.5f, -1.5f, -0.5f);

		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(150f));

		super.renderWithAnimations(console, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);

		matrices.pop();
	}

	@Override
	public ModelPart getPart() {
		return bone;
	}

	@Override
	public Animation getAnimationForState(TardisTravel.State state) {
		return Animation.Builder.create(0).build();
	}
}