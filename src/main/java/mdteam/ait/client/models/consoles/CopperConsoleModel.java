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
	private final ModelPart copper;
	public CopperConsoleModel(ModelPart root) {
		this.copper = root.getChild("copper");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData copper = modelPartData.addChild("copper", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 22.0F, 0.0F));

		ModelPartData desktop = copper.addChild("desktop", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData rim = desktop.addChild("rim", ModelPartBuilder.create().uv(51, 50).cuboid(18.0F, -5.0F, -8.0F, 2.0F, 5.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

		ModelPartData panels = desktop.addChild("panels", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData rot = panels.addChild("rot", ModelPartBuilder.create().uv(28, 30).cuboid(-10.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(28, 0).cuboid(-10.0F, -0.2F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(28, 15).cuboid(-9.5F, -0.6F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(19.25F, -14.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData desktop2 = desktop.addChild("desktop2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rim2 = desktop2.addChild("rim2", ModelPartBuilder.create().uv(51, 50).cuboid(18.0F, -5.0F, -8.0F, 2.0F, 5.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

		ModelPartData panels8 = desktop2.addChild("panels8", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData rot2 = panels8.addChild("rot2", ModelPartBuilder.create().uv(28, 30).cuboid(-10.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(28, 0).cuboid(-10.0F, -0.2F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(28, 15).cuboid(-9.5F, -0.6F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(19.25F, -14.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData desktop3 = desktop2.addChild("desktop3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rim3 = desktop3.addChild("rim3", ModelPartBuilder.create().uv(51, 50).cuboid(18.0F, -5.0F, -8.0F, 2.0F, 5.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

		ModelPartData panels9 = desktop3.addChild("panels9", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData rot3 = panels9.addChild("rot3", ModelPartBuilder.create().uv(28, 30).cuboid(-10.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(28, 0).cuboid(-10.0F, -0.2F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(28, 15).cuboid(-9.5F, -0.6F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(19.25F, -14.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData desktop4 = desktop3.addChild("desktop4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rim4 = desktop4.addChild("rim4", ModelPartBuilder.create().uv(51, 50).cuboid(18.0F, -5.0F, -8.0F, 2.0F, 5.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

		ModelPartData panels10 = desktop4.addChild("panels10", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData rot4 = panels10.addChild("rot4", ModelPartBuilder.create().uv(28, 30).cuboid(-10.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(28, 0).cuboid(-10.0F, -0.2F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(28, 15).cuboid(-9.5F, -0.6F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(19.25F, -14.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData desktop5 = desktop4.addChild("desktop5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rim5 = desktop5.addChild("rim5", ModelPartBuilder.create().uv(51, 50).cuboid(18.0F, -5.0F, -8.0F, 2.0F, 5.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

		ModelPartData panels11 = desktop5.addChild("panels11", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData rot5 = panels11.addChild("rot5", ModelPartBuilder.create().uv(28, 30).cuboid(-10.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(28, 0).cuboid(-10.0F, -0.2F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(28, 15).cuboid(-9.5F, -0.6F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(19.25F, -14.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData desktop6 = desktop5.addChild("desktop6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rim6 = desktop6.addChild("rim6", ModelPartBuilder.create().uv(51, 50).cuboid(18.0F, -5.0F, -8.0F, 2.0F, 5.0F, 16.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

		ModelPartData panels12 = desktop6.addChild("panels12", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData rot6 = panels12.addChild("rot6", ModelPartBuilder.create().uv(28, 30).cuboid(-10.0F, 0.0F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(28, 0).cuboid(-10.0F, -0.2F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F))
		.uv(28, 15).cuboid(-9.5F, -0.6F, -7.0F, 10.0F, 0.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(19.25F, -14.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData pillars = copper.addChild("pillars", ModelPartBuilder.create().uv(0, 63).cuboid(18.5F, -13.5F, -10.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(28, 30).cuboid(18.5F, -13.5F, 7.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r1 = pillars.addChild("cube_r1", ModelPartBuilder.create().uv(63, 0).cuboid(-8.0F, -2.0F, 1.0F, 9.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 1.309F));

		ModelPartData cube_r2 = pillars.addChild("cube_r2", ModelPartBuilder.create().uv(31, 66).cuboid(-1.0F, -2.0F, 1.0F, 5.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r3 = pillars.addChild("cube_r3", ModelPartBuilder.create().uv(28, 0).cuboid(7.5948F, -10.45F, -3.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r4 = pillars.addChild("cube_r4", ModelPartBuilder.create().uv(28, 15).cuboid(7.5948F, -10.45F, 0.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r5 = pillars.addChild("cube_r5", ModelPartBuilder.create().uv(0, 100).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, 8.366F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r6 = pillars.addChild("cube_r6", ModelPartBuilder.create().uv(63, 0).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, 8.387F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r7 = pillars.addChild("cube_r7", ModelPartBuilder.create().uv(100, 103).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 7.5F, 0.4819F, 0.0851F, -1.7445F));

		ModelPartData cube_r8 = pillars.addChild("cube_r8", ModelPartBuilder.create().uv(101, 71).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r9 = pillars.addChild("cube_r9", ModelPartBuilder.create().uv(51, 96).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.4835F, 0.116F, -1.7282F));

		ModelPartData cube_r10 = pillars.addChild("cube_r10", ModelPartBuilder.create().uv(28, 45).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, 1.8362F, 0.4784F, -0.0403F, -1.2752F));

		ModelPartData cube_r11 = pillars.addChild("cube_r11", ModelPartBuilder.create().uv(63, 30).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, -1.8362F, -0.4784F, 0.0403F, -1.2752F));

		ModelPartData cube_r12 = pillars.addChild("cube_r12", ModelPartBuilder.create().uv(82, 101).cuboid(0.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.48F, 0.0F, -1.789F));

		ModelPartData cube_r13 = pillars.addChild("cube_r13", ModelPartBuilder.create().uv(106, 40).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -7.5F, -0.4812F, -0.0697F, -1.7526F));

		ModelPartData cube_r14 = pillars.addChild("cube_r14", ModelPartBuilder.create().uv(63, 15).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, -8.387F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r15 = pillars.addChild("cube_r15", ModelPartBuilder.create().uv(21, 93).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, -8.366F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r16 = pillars.addChild("cube_r16", ModelPartBuilder.create().uv(108, 11).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.4835F, -0.116F, -1.7282F));

		ModelPartData cube_r17 = pillars.addChild("cube_r17", ModelPartBuilder.create().uv(13, 105).cuboid(0.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.48F, 0.0F, -1.789F));

		ModelPartData cube_r18 = pillars.addChild("cube_r18", ModelPartBuilder.create().uv(91, 101).cuboid(-1.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r19 = pillars.addChild("cube_r19", ModelPartBuilder.create().uv(36, 96).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r20 = pillars.addChild("cube_r20", ModelPartBuilder.create().uv(22, 108).cuboid(-1.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r21 = pillars.addChild("cube_r21", ModelPartBuilder.create().uv(72, 48).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r22 = pillars.addChild("cube_r22", ModelPartBuilder.create().uv(91, 0).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, 0.0F, 0.48F, 0.0F));

		ModelPartData pillars2 = pillars.addChild("pillars2", ModelPartBuilder.create().uv(0, 63).cuboid(18.5F, -13.5F, -10.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(28, 30).cuboid(18.5F, -13.5F, 7.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r23 = pillars2.addChild("cube_r23", ModelPartBuilder.create().uv(63, 15).cuboid(-6.0F, -2.0F, 1.0F, 7.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 1.309F));

		ModelPartData cube_r24 = pillars2.addChild("cube_r24", ModelPartBuilder.create().uv(31, 66).cuboid(-1.0F, -2.0F, 1.0F, 5.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r25 = pillars2.addChild("cube_r25", ModelPartBuilder.create().uv(28, 0).cuboid(7.5948F, -10.45F, -3.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r26 = pillars2.addChild("cube_r26", ModelPartBuilder.create().uv(28, 15).cuboid(7.5948F, -10.45F, 0.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r27 = pillars2.addChild("cube_r27", ModelPartBuilder.create().uv(0, 100).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, 8.366F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r28 = pillars2.addChild("cube_r28", ModelPartBuilder.create().uv(63, 0).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, 8.387F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r29 = pillars2.addChild("cube_r29", ModelPartBuilder.create().uv(100, 103).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 7.5F, 0.4819F, 0.0851F, -1.7445F));

		ModelPartData cube_r30 = pillars2.addChild("cube_r30", ModelPartBuilder.create().uv(101, 71).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r31 = pillars2.addChild("cube_r31", ModelPartBuilder.create().uv(51, 96).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.4835F, 0.116F, -1.7282F));

		ModelPartData cube_r32 = pillars2.addChild("cube_r32", ModelPartBuilder.create().uv(28, 45).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, 1.8362F, 0.4784F, -0.0403F, -1.2752F));

		ModelPartData cube_r33 = pillars2.addChild("cube_r33", ModelPartBuilder.create().uv(63, 30).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, -1.8362F, -0.4784F, 0.0403F, -1.2752F));

		ModelPartData cube_r34 = pillars2.addChild("cube_r34", ModelPartBuilder.create().uv(82, 101).cuboid(0.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.48F, 0.0F, -1.789F));

		ModelPartData cube_r35 = pillars2.addChild("cube_r35", ModelPartBuilder.create().uv(106, 40).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -7.5F, -0.4812F, -0.0697F, -1.7526F));

		ModelPartData cube_r36 = pillars2.addChild("cube_r36", ModelPartBuilder.create().uv(63, 15).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, -8.387F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r37 = pillars2.addChild("cube_r37", ModelPartBuilder.create().uv(21, 93).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, -8.366F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r38 = pillars2.addChild("cube_r38", ModelPartBuilder.create().uv(108, 11).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.4835F, -0.116F, -1.7282F));

		ModelPartData cube_r39 = pillars2.addChild("cube_r39", ModelPartBuilder.create().uv(13, 105).cuboid(0.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.48F, 0.0F, -1.789F));

		ModelPartData cube_r40 = pillars2.addChild("cube_r40", ModelPartBuilder.create().uv(91, 101).cuboid(-1.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r41 = pillars2.addChild("cube_r41", ModelPartBuilder.create().uv(36, 96).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r42 = pillars2.addChild("cube_r42", ModelPartBuilder.create().uv(22, 108).cuboid(-1.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r43 = pillars2.addChild("cube_r43", ModelPartBuilder.create().uv(72, 48).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r44 = pillars2.addChild("cube_r44", ModelPartBuilder.create().uv(91, 0).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, 0.0F, 0.48F, 0.0F));

		ModelPartData pillars3 = pillars2.addChild("pillars3", ModelPartBuilder.create().uv(0, 63).cuboid(18.5F, -13.5F, -10.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(28, 30).cuboid(18.5F, -13.5F, 7.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r45 = pillars3.addChild("cube_r45", ModelPartBuilder.create().uv(63, 0).cuboid(-8.0F, -2.0F, 1.0F, 9.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 1.309F));

		ModelPartData cube_r46 = pillars3.addChild("cube_r46", ModelPartBuilder.create().uv(31, 66).cuboid(-1.0F, -2.0F, 1.0F, 5.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r47 = pillars3.addChild("cube_r47", ModelPartBuilder.create().uv(28, 0).cuboid(7.5948F, -10.45F, -3.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r48 = pillars3.addChild("cube_r48", ModelPartBuilder.create().uv(28, 15).cuboid(7.5948F, -10.45F, 0.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r49 = pillars3.addChild("cube_r49", ModelPartBuilder.create().uv(0, 100).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, 8.366F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r50 = pillars3.addChild("cube_r50", ModelPartBuilder.create().uv(63, 0).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, 8.387F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r51 = pillars3.addChild("cube_r51", ModelPartBuilder.create().uv(100, 103).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 7.5F, 0.4819F, 0.0851F, -1.7445F));

		ModelPartData cube_r52 = pillars3.addChild("cube_r52", ModelPartBuilder.create().uv(101, 71).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r53 = pillars3.addChild("cube_r53", ModelPartBuilder.create().uv(51, 96).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.4835F, 0.116F, -1.7282F));

		ModelPartData cube_r54 = pillars3.addChild("cube_r54", ModelPartBuilder.create().uv(28, 45).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, 1.8362F, 0.4784F, -0.0403F, -1.2752F));

		ModelPartData cube_r55 = pillars3.addChild("cube_r55", ModelPartBuilder.create().uv(63, 30).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, -1.8362F, -0.4784F, 0.0403F, -1.2752F));

		ModelPartData cube_r56 = pillars3.addChild("cube_r56", ModelPartBuilder.create().uv(82, 101).cuboid(0.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.48F, 0.0F, -1.789F));

		ModelPartData cube_r57 = pillars3.addChild("cube_r57", ModelPartBuilder.create().uv(106, 40).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -7.5F, -0.4812F, -0.0697F, -1.7526F));

		ModelPartData cube_r58 = pillars3.addChild("cube_r58", ModelPartBuilder.create().uv(63, 15).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, -8.387F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r59 = pillars3.addChild("cube_r59", ModelPartBuilder.create().uv(21, 93).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, -8.366F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r60 = pillars3.addChild("cube_r60", ModelPartBuilder.create().uv(108, 11).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.4835F, -0.116F, -1.7282F));

		ModelPartData cube_r61 = pillars3.addChild("cube_r61", ModelPartBuilder.create().uv(13, 105).cuboid(0.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.48F, 0.0F, -1.789F));

		ModelPartData cube_r62 = pillars3.addChild("cube_r62", ModelPartBuilder.create().uv(91, 101).cuboid(-1.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r63 = pillars3.addChild("cube_r63", ModelPartBuilder.create().uv(36, 96).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r64 = pillars3.addChild("cube_r64", ModelPartBuilder.create().uv(22, 108).cuboid(-1.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r65 = pillars3.addChild("cube_r65", ModelPartBuilder.create().uv(72, 48).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r66 = pillars3.addChild("cube_r66", ModelPartBuilder.create().uv(91, 0).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, 0.0F, 0.48F, 0.0F));

		ModelPartData pillars4 = pillars3.addChild("pillars4", ModelPartBuilder.create().uv(0, 63).cuboid(18.5F, -13.5F, -10.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(28, 30).cuboid(18.5F, -13.5F, 7.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r67 = pillars4.addChild("cube_r67", ModelPartBuilder.create().uv(63, 15).cuboid(-6.0F, -2.0F, 1.0F, 7.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 1.309F));

		ModelPartData cube_r68 = pillars4.addChild("cube_r68", ModelPartBuilder.create().uv(31, 66).cuboid(-1.0F, -2.0F, 1.0F, 5.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r69 = pillars4.addChild("cube_r69", ModelPartBuilder.create().uv(28, 0).cuboid(7.5948F, -10.45F, -3.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r70 = pillars4.addChild("cube_r70", ModelPartBuilder.create().uv(28, 15).cuboid(7.5948F, -10.45F, 0.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r71 = pillars4.addChild("cube_r71", ModelPartBuilder.create().uv(0, 100).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, 8.366F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r72 = pillars4.addChild("cube_r72", ModelPartBuilder.create().uv(63, 0).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, 8.387F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r73 = pillars4.addChild("cube_r73", ModelPartBuilder.create().uv(100, 103).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 7.5F, 0.4819F, 0.0851F, -1.7445F));

		ModelPartData cube_r74 = pillars4.addChild("cube_r74", ModelPartBuilder.create().uv(101, 71).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r75 = pillars4.addChild("cube_r75", ModelPartBuilder.create().uv(51, 96).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.4835F, 0.116F, -1.7282F));

		ModelPartData cube_r76 = pillars4.addChild("cube_r76", ModelPartBuilder.create().uv(28, 45).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, 1.8362F, 0.4784F, -0.0403F, -1.2752F));

		ModelPartData cube_r77 = pillars4.addChild("cube_r77", ModelPartBuilder.create().uv(63, 30).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, -1.8362F, -0.4784F, 0.0403F, -1.2752F));

		ModelPartData cube_r78 = pillars4.addChild("cube_r78", ModelPartBuilder.create().uv(82, 101).cuboid(0.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.48F, 0.0F, -1.789F));

		ModelPartData cube_r79 = pillars4.addChild("cube_r79", ModelPartBuilder.create().uv(106, 40).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -7.5F, -0.4812F, -0.0697F, -1.7526F));

		ModelPartData cube_r80 = pillars4.addChild("cube_r80", ModelPartBuilder.create().uv(63, 15).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, -8.387F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r81 = pillars4.addChild("cube_r81", ModelPartBuilder.create().uv(21, 93).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, -8.366F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r82 = pillars4.addChild("cube_r82", ModelPartBuilder.create().uv(108, 11).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.4835F, -0.116F, -1.7282F));

		ModelPartData cube_r83 = pillars4.addChild("cube_r83", ModelPartBuilder.create().uv(13, 105).cuboid(0.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.48F, 0.0F, -1.789F));

		ModelPartData cube_r84 = pillars4.addChild("cube_r84", ModelPartBuilder.create().uv(91, 101).cuboid(-1.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r85 = pillars4.addChild("cube_r85", ModelPartBuilder.create().uv(36, 96).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r86 = pillars4.addChild("cube_r86", ModelPartBuilder.create().uv(22, 108).cuboid(-1.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r87 = pillars4.addChild("cube_r87", ModelPartBuilder.create().uv(72, 48).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r88 = pillars4.addChild("cube_r88", ModelPartBuilder.create().uv(91, 0).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, 0.0F, 0.48F, 0.0F));

		ModelPartData pillars5 = pillars4.addChild("pillars5", ModelPartBuilder.create().uv(0, 63).cuboid(18.5F, -13.5F, -10.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(28, 30).cuboid(18.5F, -13.5F, 7.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r89 = pillars5.addChild("cube_r89", ModelPartBuilder.create().uv(63, 0).cuboid(-8.0F, -2.0F, 1.0F, 9.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 1.309F));

		ModelPartData cube_r90 = pillars5.addChild("cube_r90", ModelPartBuilder.create().uv(31, 66).cuboid(-1.0F, -2.0F, 1.0F, 5.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r91 = pillars5.addChild("cube_r91", ModelPartBuilder.create().uv(28, 0).cuboid(7.5948F, -10.45F, -3.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r92 = pillars5.addChild("cube_r92", ModelPartBuilder.create().uv(28, 15).cuboid(7.5948F, -10.45F, 0.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r93 = pillars5.addChild("cube_r93", ModelPartBuilder.create().uv(0, 100).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, 8.366F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r94 = pillars5.addChild("cube_r94", ModelPartBuilder.create().uv(63, 0).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, 8.387F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r95 = pillars5.addChild("cube_r95", ModelPartBuilder.create().uv(100, 103).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 7.5F, 0.4819F, 0.0851F, -1.7445F));

		ModelPartData cube_r96 = pillars5.addChild("cube_r96", ModelPartBuilder.create().uv(101, 71).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r97 = pillars5.addChild("cube_r97", ModelPartBuilder.create().uv(51, 96).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.4835F, 0.116F, -1.7282F));

		ModelPartData cube_r98 = pillars5.addChild("cube_r98", ModelPartBuilder.create().uv(28, 45).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, 1.8362F, 0.4784F, -0.0403F, -1.2752F));

		ModelPartData cube_r99 = pillars5.addChild("cube_r99", ModelPartBuilder.create().uv(63, 30).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, -1.8362F, -0.4784F, 0.0403F, -1.2752F));

		ModelPartData cube_r100 = pillars5.addChild("cube_r100", ModelPartBuilder.create().uv(82, 101).cuboid(0.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.48F, 0.0F, -1.789F));

		ModelPartData cube_r101 = pillars5.addChild("cube_r101", ModelPartBuilder.create().uv(106, 40).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -7.5F, -0.4812F, -0.0697F, -1.7526F));

		ModelPartData cube_r102 = pillars5.addChild("cube_r102", ModelPartBuilder.create().uv(63, 15).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, -8.387F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r103 = pillars5.addChild("cube_r103", ModelPartBuilder.create().uv(21, 93).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, -8.366F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r104 = pillars5.addChild("cube_r104", ModelPartBuilder.create().uv(108, 11).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.4835F, -0.116F, -1.7282F));

		ModelPartData cube_r105 = pillars5.addChild("cube_r105", ModelPartBuilder.create().uv(13, 105).cuboid(0.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.48F, 0.0F, -1.789F));

		ModelPartData cube_r106 = pillars5.addChild("cube_r106", ModelPartBuilder.create().uv(91, 101).cuboid(-1.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r107 = pillars5.addChild("cube_r107", ModelPartBuilder.create().uv(36, 96).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r108 = pillars5.addChild("cube_r108", ModelPartBuilder.create().uv(22, 108).cuboid(-1.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r109 = pillars5.addChild("cube_r109", ModelPartBuilder.create().uv(72, 48).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r110 = pillars5.addChild("cube_r110", ModelPartBuilder.create().uv(91, 0).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, 0.0F, 0.48F, 0.0F));

		ModelPartData pillars6 = pillars5.addChild("pillars6", ModelPartBuilder.create().uv(0, 63).cuboid(18.5F, -13.5F, -10.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(28, 30).cuboid(18.5F, -13.5F, 7.5F, 2.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r111 = pillars6.addChild("cube_r111", ModelPartBuilder.create().uv(63, 15).cuboid(-6.0F, -2.0F, 1.0F, 7.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 1.309F));

		ModelPartData cube_r112 = pillars6.addChild("cube_r112", ModelPartBuilder.create().uv(31, 66).cuboid(-1.0F, -2.0F, 1.0F, 5.0F, 7.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(8.0941F, -18.3532F, -5.5F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r113 = pillars6.addChild("cube_r113", ModelPartBuilder.create().uv(28, 0).cuboid(7.5948F, -10.45F, -3.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r114 = pillars6.addChild("cube_r114", ModelPartBuilder.create().uv(28, 15).cuboid(7.5948F, -10.45F, 0.2638F, 3.0F, 10.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.45F, 0.0F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r115 = pillars6.addChild("cube_r115", ModelPartBuilder.create().uv(0, 100).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, 8.366F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r116 = pillars6.addChild("cube_r116", ModelPartBuilder.create().uv(63, 0).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, 8.387F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r117 = pillars6.addChild("cube_r117", ModelPartBuilder.create().uv(100, 103).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 7.5F, 0.4819F, 0.0851F, -1.7445F));

		ModelPartData cube_r118 = pillars6.addChild("cube_r118", ModelPartBuilder.create().uv(101, 71).cuboid(-5.0F, -13.0F, -1.0F, 5.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.4899F, -0.1932F, -1.1492F));

		ModelPartData cube_r119 = pillars6.addChild("cube_r119", ModelPartBuilder.create().uv(51, 96).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.4835F, 0.116F, -1.7282F));

		ModelPartData cube_r120 = pillars6.addChild("cube_r120", ModelPartBuilder.create().uv(28, 45).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, 1.8362F, 0.4784F, -0.0403F, -1.2752F));

		ModelPartData cube_r121 = pillars6.addChild("cube_r121", ModelPartBuilder.create().uv(63, 30).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 6.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.5948F, 0.6969F, -1.8362F, -0.4784F, 0.0403F, -1.2752F));

		ModelPartData cube_r122 = pillars6.addChild("cube_r122", ModelPartBuilder.create().uv(82, 101).cuboid(0.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.48F, 0.0F, -1.789F));

		ModelPartData cube_r123 = pillars6.addChild("cube_r123", ModelPartBuilder.create().uv(106, 40).cuboid(0.0F, -13.0F, -1.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -7.5F, -0.4812F, -0.0697F, -1.7526F));

		ModelPartData cube_r124 = pillars6.addChild("cube_r124", ModelPartBuilder.create().uv(63, 15).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0383F, -7.5F, -8.387F, 0.0F, 0.48F, 0.0F));

		ModelPartData cube_r125 = pillars6.addChild("cube_r125", ModelPartBuilder.create().uv(21, 93).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.0014F, -13.538F, -8.366F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r126 = pillars6.addChild("cube_r126", ModelPartBuilder.create().uv(108, 11).cuboid(0.0F, -13.0F, 0.0F, 4.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.4835F, -0.116F, -1.7282F));

		ModelPartData cube_r127 = pillars6.addChild("cube_r127", ModelPartBuilder.create().uv(13, 105).cuboid(0.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, -0.48F, 0.0F, -1.789F));

		ModelPartData cube_r128 = pillars6.addChild("cube_r128", ModelPartBuilder.create().uv(91, 101).cuboid(-1.0F, -13.0F, -3.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, 10.5F, 0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r129 = pillars6.addChild("cube_r129", ModelPartBuilder.create().uv(36, 96).cuboid(-6.0F, -13.0F, 0.0F, 6.0F, 13.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.4899F, 0.1932F, -1.1492F));

		ModelPartData cube_r130 = pillars6.addChild("cube_r130", ModelPartBuilder.create().uv(22, 108).cuboid(-1.0F, -13.0F, 0.0F, 1.0F, 13.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -13.5F, -10.5F, -0.48F, 0.0F, -1.0472F));

		ModelPartData cube_r131 = pillars6.addChild("cube_r131", ModelPartBuilder.create().uv(72, 48).cuboid(-3.0F, -6.0F, -1.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, 10.5F, 0.0F, -0.48F, 0.0F));

		ModelPartData cube_r132 = pillars6.addChild("cube_r132", ModelPartBuilder.create().uv(91, 0).cuboid(-3.0F, -6.0F, 0.0F, 3.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(20.5F, -7.5F, -10.5F, 0.0F, 0.48F, 0.0F));

		ModelPartData bottom = copper.addChild("bottom", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData rotor = bottom.addChild("rotor", ModelPartBuilder.create().uv(67, 98).cuboid(4.2F, -9.0F, -3.0F, 1.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		ModelPartData base = bottom.addChild("base", ModelPartBuilder.create().uv(28, 45).cuboid(-0.35F, -8.0F, -5.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panels7 = bottom.addChild("panels7", ModelPartBuilder.create().uv(70, 72).cuboid(8.0F, -11.0F, -2.0F, 11.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r133 = panels7.addChild("cube_r133", ModelPartBuilder.create().uv(0, 63).cuboid(-1.0F, 0.0F, -7.0F, 1.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(16.4545F, -8.0F, 9.5F, -0.5087F, -0.1298F, 1.3428F));

		ModelPartData cube_r134 = panels7.addChild("cube_r134", ModelPartBuilder.create().uv(76, 27).cuboid(-11.0F, -0.999F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -9.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData cube_r135 = panels7.addChild("cube_r135", ModelPartBuilder.create().uv(76, 33).cuboid(-11.0F, -2.0F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(18.3156F, -9.1197F, 2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData bottom2 = bottom.addChild("bottom2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rotor2 = bottom2.addChild("rotor2", ModelPartBuilder.create().uv(67, 98).cuboid(4.2F, -9.0F, -3.0F, 1.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		ModelPartData base2 = bottom2.addChild("base2", ModelPartBuilder.create().uv(28, 45).cuboid(-0.35F, -8.0F, -5.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panels2 = bottom2.addChild("panels2", ModelPartBuilder.create().uv(70, 72).cuboid(8.0F, -11.0F, -2.0F, 11.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r136 = panels2.addChild("cube_r136", ModelPartBuilder.create().uv(0, 63).cuboid(-1.0F, 0.0F, -7.0F, 1.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(16.4545F, -8.0F, 9.5F, -0.5087F, -0.1298F, 1.3428F));

		ModelPartData cube_r137 = panels2.addChild("cube_r137", ModelPartBuilder.create().uv(76, 27).cuboid(-11.0F, -0.999F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -9.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData cube_r138 = panels2.addChild("cube_r138", ModelPartBuilder.create().uv(76, 33).cuboid(-11.0F, -2.0F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(18.3156F, -9.1197F, 2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData bottom3 = bottom2.addChild("bottom3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rotor3 = bottom3.addChild("rotor3", ModelPartBuilder.create().uv(67, 98).cuboid(4.2F, -9.0F, -3.0F, 1.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		ModelPartData base3 = bottom3.addChild("base3", ModelPartBuilder.create().uv(28, 45).cuboid(-0.35F, -8.0F, -5.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panels3 = bottom3.addChild("panels3", ModelPartBuilder.create().uv(70, 72).cuboid(8.0F, -11.0F, -2.0F, 11.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r139 = panels3.addChild("cube_r139", ModelPartBuilder.create().uv(0, 63).cuboid(-1.0F, 0.0F, -7.0F, 1.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(16.4545F, -8.0F, 9.5F, -0.5087F, -0.1298F, 1.3428F));

		ModelPartData cube_r140 = panels3.addChild("cube_r140", ModelPartBuilder.create().uv(76, 27).cuboid(-11.0F, -0.999F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -9.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData cube_r141 = panels3.addChild("cube_r141", ModelPartBuilder.create().uv(76, 33).cuboid(-11.0F, -2.0F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(18.3156F, -9.1197F, 2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData bottom4 = bottom3.addChild("bottom4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rotor4 = bottom4.addChild("rotor4", ModelPartBuilder.create().uv(67, 98).cuboid(4.2F, -9.0F, -3.0F, 1.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		ModelPartData base4 = bottom4.addChild("base4", ModelPartBuilder.create().uv(28, 45).cuboid(-0.35F, -8.0F, -5.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panels4 = bottom4.addChild("panels4", ModelPartBuilder.create().uv(70, 72).cuboid(8.0F, -11.0F, -2.0F, 11.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r142 = panels4.addChild("cube_r142", ModelPartBuilder.create().uv(0, 63).cuboid(-1.0F, 0.0F, -7.0F, 1.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(16.4545F, -8.0F, 9.5F, -0.5087F, -0.1298F, 1.3428F));

		ModelPartData cube_r143 = panels4.addChild("cube_r143", ModelPartBuilder.create().uv(76, 27).cuboid(-11.0F, -0.999F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -9.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData cube_r144 = panels4.addChild("cube_r144", ModelPartBuilder.create().uv(76, 33).cuboid(-11.0F, -2.0F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(18.3156F, -9.1197F, 2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData bottom5 = bottom4.addChild("bottom5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rotor5 = bottom5.addChild("rotor5", ModelPartBuilder.create().uv(67, 98).cuboid(4.2F, -9.0F, -3.0F, 1.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		ModelPartData base5 = bottom5.addChild("base5", ModelPartBuilder.create().uv(28, 45).cuboid(-0.35F, -8.0F, -5.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panels5 = bottom5.addChild("panels5", ModelPartBuilder.create().uv(70, 72).cuboid(8.0F, -11.0F, -2.0F, 11.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r145 = panels5.addChild("cube_r145", ModelPartBuilder.create().uv(0, 63).cuboid(-1.0F, 0.0F, -7.0F, 1.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(16.4545F, -8.0F, 9.5F, -0.5087F, -0.1298F, 1.3428F));

		ModelPartData cube_r146 = panels5.addChild("cube_r146", ModelPartBuilder.create().uv(76, 27).cuboid(-11.0F, -0.999F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -9.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData cube_r147 = panels5.addChild("cube_r147", ModelPartBuilder.create().uv(76, 33).cuboid(-11.0F, -2.0F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(18.3156F, -9.1197F, 2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData bottom6 = bottom5.addChild("bottom6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rotor6 = bottom6.addChild("rotor6", ModelPartBuilder.create().uv(67, 98).cuboid(4.2F, -9.0F, -3.0F, 1.0F, 7.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		ModelPartData base6 = bottom6.addChild("base6", ModelPartBuilder.create().uv(28, 45).cuboid(-0.35F, -8.0F, -5.0F, 9.0F, 10.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panels6 = bottom6.addChild("panels6", ModelPartBuilder.create().uv(70, 72).cuboid(8.0F, -11.0F, -2.0F, 11.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r148 = panels6.addChild("cube_r148", ModelPartBuilder.create().uv(0, 63).cuboid(-1.0F, 0.0F, -7.0F, 1.0F, 11.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(16.4545F, -8.0F, 9.5F, -0.5087F, -0.1298F, 1.3428F));

		ModelPartData cube_r149 = panels6.addChild("cube_r149", ModelPartBuilder.create().uv(76, 27).cuboid(-11.0F, -0.999F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -9.0F, 2.0F, 0.0F, 0.0F, -0.2182F));

		ModelPartData cube_r150 = panels6.addChild("cube_r150", ModelPartBuilder.create().uv(76, 33).cuboid(-11.0F, -2.0F, -4.0F, 11.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(18.3156F, -9.1197F, 2.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData controls = copper.addChild("controls", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData panel_1 = controls.addChild("panel_1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData rot7 = panel_1.addChild("rot7", ModelPartBuilder.create(), ModelTransform.of(20.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData atomic_acc = rot7.addChild("atomic_acc", ModelPartBuilder.create().uv(117, 40).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(123, 74).cuboid(-0.5F, -1.25F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.0482F, 0.2624F, 0.0F, 0.0F, 0.0F, -0.5236F));

		ModelPartData bone2 = atomic_acc.addChild("bone2", ModelPartBuilder.create().uv(123, 32).cuboid(-0.5F, -1.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(90, 118).cuboid(-0.5F, -1.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

		ModelPartData cube_r151 = bone2.addChild("cube_r151", ModelPartBuilder.create().uv(118, 81).cuboid(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

		ModelPartData cube_r152 = bone2.addChild("cube_r152", ModelPartBuilder.create().uv(83, 118).cuboid(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 1.5708F, 0.0F, 3.1416F));

		ModelPartData cube_r153 = bone2.addChild("cube_r153", ModelPartBuilder.create().uv(90, 118).cuboid(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData cube_r154 = bone2.addChild("cube_r154", ModelPartBuilder.create().uv(83, 118).cuboid(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r155 = bone2.addChild("cube_r155", ModelPartBuilder.create().uv(83, 118).cuboid(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData button = rot7.addChild("button", ModelPartBuilder.create().uv(118, 122).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(122, 104).cuboid(-0.5F, -1.2F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.75F, 0.6F, 0.0F));

		ModelPartData bone3 = button.addChild("bone3", ModelPartBuilder.create().uv(122, 101).cuboid(-0.5F, -1.55F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData button2 = rot7.addChild("button2", ModelPartBuilder.create().uv(101, 122).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(122, 98).cuboid(-0.5F, -1.4F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(-2.25F, 0.5F, -2.0F));

		ModelPartData bone4 = button2.addChild("bone4", ModelPartBuilder.create().uv(123, 119).cuboid(-0.5F, -1.75F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.4F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData keyboard = rot7.addChild("keyboard", ModelPartBuilder.create().uv(23, 83).cuboid(-2.0F, -1.0F, -4.0F, 5.0F, 1.0F, 8.0F, new Dilation(-0.3F))
		.uv(72, 89).cuboid(-1.5F, -1.25F, -3.5F, 4.0F, 1.0F, 7.0F, new Dilation(-0.2F))
		.uv(88, 62).cuboid(-1.5F, -1.55F, -3.5F, 4.0F, 1.0F, 7.0F, new Dilation(-0.4F))
		.uv(17, 66).cuboid(-1.5F, -1.65F, -3.5F, 4.0F, 1.0F, 7.0F, new Dilation(-0.4F)), ModelTransform.pivot(3.35F, 2.5F, 0.0F));

		ModelPartData cash_reg = rot7.addChild("cash_reg", ModelPartBuilder.create().uv(42, 83).cuboid(-0.9F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.3F))
		.uv(122, 92).cuboid(-0.2F, -0.7F, -1.4F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(2.0F, 2.0F, -9.0F, 0.0F, 0.0F, -0.5672F));

		ModelPartData cube_r156 = cash_reg.addChild("cube_r156", ModelPartBuilder.create().uv(118, 77).cuboid(-0.15F, -1.25F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-0.5F, -0.5F, 0.0F, 0.0F, 0.0F, 0.7854F));

		ModelPartData pump = rot7.addChild("pump", ModelPartBuilder.create().uv(117, 5).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.9661F, 2.03F, -5.0F, 0.0F, -1.0036F, -0.5672F));

		ModelPartData bone5 = pump.addChild("bone5", ModelPartBuilder.create().uv(74, 116).cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 3.0F, new Dilation(-0.3F))
		.uv(13, 100).cuboid(-1.0F, -1.0F, -2.7F, 2.0F, 2.0F, 1.0F, new Dilation(-0.4F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData pump2 = rot7.addChild("pump2", ModelPartBuilder.create().uv(117, 5).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.9661F, 2.03F, 5.0F, 0.0F, -0.5672F, -0.5672F));

		ModelPartData bone6 = pump2.addChild("bone6", ModelPartBuilder.create().uv(74, 116).cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 1.0F, 3.0F, new Dilation(-0.3F))
		.uv(13, 100).cuboid(-1.0F, -1.0F, -2.7F, 2.0F, 2.0F, 1.0F, new Dilation(-0.4F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData lever = rot7.addChild("lever", ModelPartBuilder.create().uv(118, 57).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.1F))
		.uv(45, 123).cuboid(0.85F, -1.9F, -1.1F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(45, 123).cuboid(0.85F, -1.9F, 0.1F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(50, 123).cuboid(-1.9F, -1.7F, -1.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(50, 123).cuboid(-1.9F, -1.7F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-3.5F, 0.5F, -6.9F, 0.0F, 0.48F, 0.0F));

		ModelPartData bone7 = lever.addChild("bone7", ModelPartBuilder.create().uv(0, 63).cuboid(-0.75F, -0.75F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(88, 122).cuboid(-0.5F, -0.5F, -0.65F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(83, 122).cuboid(-0.5F, -3.0F, -0.9F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(0.5F, -1.5F, 1.15F, 0.0F, 0.0F, 0.3927F));

		ModelPartData cube_r157 = bone7.addChild("cube_r157", ModelPartBuilder.create().uv(38, 0).cuboid(-1.0F, -2.0F, -0.001F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.25F, -0.75F, 0.001F, 0.2618F, 0.0F, 0.0F));

		ModelPartData bone8 = lever.addChild("bone8", ModelPartBuilder.create().uv(0, 63).cuboid(-0.75F, -0.75F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(83, 122).cuboid(-0.5F, -3.0F, -0.1F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(88, 122).cuboid(-0.5F, -0.5F, -0.35F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(0.5F, -1.5F, -1.15F, 0.0F, 0.0F, 0.3927F));

		ModelPartData cube_r158 = bone8.addChild("cube_r158", ModelPartBuilder.create().uv(38, 0).cuboid(-1.0F, -2.0F, -0.001F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.25F, -0.75F, 0.001F, -0.2618F, 0.0F, 0.0F));

		ModelPartData bell = rot7.addChild("bell", ModelPartBuilder.create().uv(28, 40).cuboid(-2.0F, -2.0F, -1.0F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(104, 63).cuboid(-7.75F, -1.5F, -1.8F, 6.0F, 0.0F, 3.0F, new Dilation(0.0F))
		.uv(103, 26).cuboid(-7.75F, -1.8F, -1.8F, 6.0F, 0.0F, 3.0F, new Dilation(0.0F))
		.uv(63, 27).cuboid(-5.0F, -1.55F, -1.4F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(123, 115).cuboid(-5.1F, -2.25F, -1.4F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(123, 115).cuboid(-4.5F, -2.25F, -1.4F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(123, 115).cuboid(-3.9F, -2.25F, -1.4F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(123, 115).cuboid(-4.5F, -2.25F, -0.3F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(123, 115).cuboid(-5.1F, -2.25F, -0.3F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(123, 115).cuboid(-3.9F, -2.25F, -0.3F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(63, 27).cuboid(-5.0F, -1.55F, -0.3F, 2.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(40, 123).cuboid(-6.05F, -2.15F, -0.3F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F))
		.uv(88, 101).cuboid(2.45F, -1.85F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(8, 63).cuboid(2.45F, -2.05F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(36, 30).cuboid(2.7F, -2.25F, -0.25F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(67, 101).cuboid(1.7F, -1.85F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(55, 117).cuboid(-2.0F, -2.5F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F))
		.uv(55, 117).cuboid(0.0F, -2.5F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-5.0F, 0.75F, 6.1F, 0.0227F, -0.4795F, -0.0492F));

		ModelPartData bone9 = bell.addChild("bone9", ModelPartBuilder.create().uv(79, 84).cuboid(-1.65F, -0.1F, -0.65F, 2.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-1.0F, -2.5F, 0.0F, 0.0F, -0.1745F, 0.0F));

		ModelPartData bone10 = bell.addChild("bone10", ModelPartBuilder.create().uv(79, 84).cuboid(-1.65F, -0.1F, -0.65F, 2.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(1.0F, -2.5F, 0.0F, 0.0F, 1.2217F, 0.0F));

		ModelPartData bone11 = rot7.addChild("bone11", ModelPartBuilder.create().uv(98, 89).cuboid(-0.75F, -1.1F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.1F))
		.uv(42, 88).cuboid(-3.05F, -1.55F, 0.5F, 2.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(42, 88).cuboid(-2.25F, -1.55F, 1.1F, 2.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(118, 116).cuboid(0.25F, -0.9F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
		.uv(88, 89).cuboid(-0.75F, -1.35F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(-0.1F))
		.uv(65, 122).cuboid(-2.15F, -1.15F, -0.55F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(100, 0).cuboid(-0.75F, -2.55F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.35F))
		.uv(100, 0).cuboid(-0.75F, -2.55F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.35F))
		.uv(122, 66).cuboid(-0.75F, -2.55F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(122, 66).cuboid(-0.75F, -2.55F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(-7.0F, 0.5F, -5.1F, 0.0F, 0.0F, -0.0436F));

		ModelPartData bone12 = rot7.addChild("bone12", ModelPartBuilder.create().uv(79, 79).cuboid(-1.8107F, 0.2016F, -4.0F, 5.0F, 1.0F, 8.0F, new Dilation(0.0F))
		.uv(86, 48).cuboid(-1.8107F, 0.4016F, -4.0F, 5.0F, 0.0F, 8.0F, new Dilation(0.0F))
		.uv(122, 52).cuboid(-1.55F, -0.1F, -1.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-12.55F, -2.15F, 0.0F, 0.0F, 0.0F, 0.5236F));

		ModelPartData valve = rot7.addChild("valve", ModelPartBuilder.create().uv(122, 49).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(17, 122).cuboid(0.25F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(119, 13).cuboid(-0.5F, -1.25F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-17.75F, -4.25F, 0.5F, 0.0F, 0.0F, -0.6981F));

		ModelPartData bone13 = valve.addChild("bone13", ModelPartBuilder.create().uv(119, 10).cuboid(-1.05F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(-0.4F))
		.uv(12, 122).cuboid(-0.05F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(67, 96).cuboid(0.7F, -1.25F, -1.25F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(123, 35).cuboid(0.15F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(1.0F, -1.5F, 0.0F, 0.7854F, 0.0F, 0.0F));

		ModelPartData valve2 = rot7.addChild("valve2", ModelPartBuilder.create().uv(122, 49).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(17, 122).cuboid(0.25F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(119, 13).cuboid(-0.5F, -1.25F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-17.5F, -4.0F, 2.0F, -0.3054F, 0.0F, -0.6981F));

		ModelPartData bone14 = valve2.addChild("bone14", ModelPartBuilder.create().uv(119, 10).cuboid(-1.05F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(-0.4F))
		.uv(12, 122).cuboid(-0.05F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.15F))
		.uv(67, 96).cuboid(0.7F, -1.25F, -1.25F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(123, 35).cuboid(0.15F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.of(1.0F, -1.5F, 0.0F, 1.0472F, 0.0F, 0.0F));

		ModelPartData cables2 = rot7.addChild("cables2", ModelPartBuilder.create().uv(6, 115).cuboid(-0.25F, 0.5F, -6.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(77, 12).cuboid(-1.5F, 1.0F, 5.25F, 4.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 8.25F, 0.0F, 0.0F, 0.0F, 2.3562F));

		ModelPartData cube_r159 = cables2.addChild("cube_r159", ModelPartBuilder.create().uv(100, 96).cuboid(-1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		ModelPartData cube_r160 = cables2.addChild("cube_r160", ModelPartBuilder.create().uv(101, 33).cuboid(-1.0F, 0.0F, -6.0F, 3.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		ModelPartData panel_2 = controls.addChild("panel_2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rot8 = panel_2.addChild("rot8", ModelPartBuilder.create(), ModelTransform.of(20.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData cube_r161 = rot8.addChild("cube_r161", ModelPartBuilder.create().uv(57, 48).cuboid(0.1F, 1.5F, -2.0F, 0.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5672F));

		ModelPartData lever2 = rot8.addChild("lever2", ModelPartBuilder.create().uv(76, 98).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(60, 75).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.001F))
		.uv(109, 116).cuboid(-0.75F, -2.75F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.1F))
		.uv(0, 115).cuboid(-0.5F, -2.0F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-5.5F, -0.35F, 5.75F, 0.0F, -0.48F, 0.0F));

		ModelPartData lights2 = lever2.addChild("lights2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData light = lights2.addChild("light", ModelPartBuilder.create().uv(88, 65).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.03F)), ModelTransform.pivot(0.0F, -1.5F, 0.0F));

		ModelPartData light2 = lights2.addChild("light2", ModelPartBuilder.create().uv(88, 65).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.03F)), ModelTransform.pivot(0.0F, -1.5F, 0.0F));

		ModelPartData light3 = lights2.addChild("light3", ModelPartBuilder.create().uv(88, 65).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.03F)), ModelTransform.of(0.0F, -1.5F, 0.0F, 0.0F, 0.0F, -1.5708F));

		ModelPartData light4 = lights2.addChild("light4", ModelPartBuilder.create().uv(88, 65).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.03F)), ModelTransform.of(0.0F, -1.5F, 0.0F, 0.0F, 0.0F, -1.5708F));

		ModelPartData light5 = lights2.addChild("light5", ModelPartBuilder.create().uv(88, 65).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.03F)), ModelTransform.of(0.0F, -1.5F, 0.0F, 0.0F, 0.0F, -3.1416F));

		ModelPartData bone29 = lever2.addChild("bone29", ModelPartBuilder.create().uv(28, 30).cuboid(-0.75F, -2.25F, -1.25F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
		.uv(16, 89).cuboid(-0.5F, -3.05F, -1.85F, 1.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, -1.5F, 0.0F, 0.0F, 0.0F, -1.309F));

		ModelPartData bone30 = lever2.addChild("bone30", ModelPartBuilder.create().uv(16, 89).cuboid(-0.5F, -3.05F, -0.2F, 1.0F, 1.0F, 2.0F, new Dilation(-0.2F))
		.uv(28, 30).cuboid(-0.75F, -2.25F, 1.25F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.5F, 0.0F, 0.0F, 0.0F, -1.2217F));

		ModelPartData disc = rot8.addChild("disc", ModelPartBuilder.create().uv(97, 118).cuboid(-1.75F, 0.3F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
		.uv(117, 45).cuboid(-2.6F, 0.2F, -1.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
		.uv(4, 122).cuboid(1.85F, 0.15F, -2.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.pivot(-4.0F, -0.5F, 0.0F));

		ModelPartData cube_r162 = disc.addChild("cube_r162", ModelPartBuilder.create().uv(117, 45).cuboid(-2.6F, 0.0F, -1.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r163 = disc.addChild("cube_r163", ModelPartBuilder.create().uv(117, 45).cuboid(-2.6F, 0.0F, -1.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r164 = disc.addChild("cube_r164", ModelPartBuilder.create().uv(117, 45).cuboid(-2.6F, 0.0F, -1.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData cube_r165 = disc.addChild("cube_r165", ModelPartBuilder.create().uv(117, 45).cuboid(-2.6F, 0.0F, -1.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
		.uv(97, 118).cuboid(-1.75F, 0.1F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r166 = disc.addChild("cube_r166", ModelPartBuilder.create().uv(117, 45).cuboid(-2.6F, 0.0F, -1.5F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
		.uv(97, 118).cuboid(-1.75F, 0.1F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.2F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r167 = disc.addChild("cube_r167", ModelPartBuilder.create().uv(97, 118).cuboid(-1.75F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.3F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r168 = disc.addChild("cube_r168", ModelPartBuilder.create().uv(97, 118).cuboid(-1.75F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.3F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r169 = disc.addChild("cube_r169", ModelPartBuilder.create().uv(97, 118).cuboid(-1.75F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.3F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData bone31 = disc.addChild("bone31", ModelPartBuilder.create().uv(28, 45).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(70, 12).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.75F, 0.05F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData bone34 = disc.addChild("bone34", ModelPartBuilder.create().uv(28, 45).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(70, 12).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.75F, 0.05F, -1.0F, 0.0F, -0.4363F, 0.0F));

		ModelPartData bone35 = disc.addChild("bone35", ModelPartBuilder.create().uv(28, 45).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(70, 12).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.75F, 0.05F, 1.0F, 0.0F, 0.4363F, 0.0F));

		ModelPartData bone32 = disc.addChild("bone32", ModelPartBuilder.create().uv(28, 45).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(70, 12).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.05F, -1.75F, 0.0F, -1.5708F, 0.0F));

		ModelPartData bone33 = disc.addChild("bone33", ModelPartBuilder.create().uv(28, 45).cuboid(-1.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(70, 12).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.05F, 1.75F, 0.0F, 1.5708F, 0.0F));

		ModelPartData dial = rot8.addChild("dial", ModelPartBuilder.create().uv(0, 89).cuboid(0.0F, -1.25F, -1.25F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(113, 121).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.25F, 0.9F, 9.0F, 0.0F, 0.0F, -0.5672F));

		ModelPartData crank = rot8.addChild("crank", ModelPartBuilder.create().uv(28, 15).cuboid(-0.2F, -0.5F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.2686F, 0.6717F, -9.75F, 0.0F, 0.0F, -0.5672F));

		ModelPartData bone36 = crank.addChild("bone36", ModelPartBuilder.create().uv(118, 89).cuboid(-0.6F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(121, 109).cuboid(0.8F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(57, 48).cuboid(1.1F, -0.25F, -0.25F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.25F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		ModelPartData crank2 = rot8.addChild("crank2", ModelPartBuilder.create().uv(28, 15).cuboid(-0.2F, -0.5F, -0.5F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.2686F, 0.6717F, -8.25F, 0.0F, 0.0F, -0.5672F));

		ModelPartData bone37 = crank2.addChild("bone37", ModelPartBuilder.create().uv(118, 89).cuboid(-0.6F, -0.5F, -0.5F, 2.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(121, 109).cuboid(0.8F, 0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(57, 48).cuboid(1.1F, -0.25F, -0.25F, 0.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		ModelPartData button3 = rot8.addChild("button3", ModelPartBuilder.create().uv(63, 40).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, -0.25F, -4.0F));

		ModelPartData bone38 = button3.addChild("bone38", ModelPartBuilder.create().uv(108, 121).cuboid(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cables = rot8.addChild("cables", ModelPartBuilder.create().uv(6, 115).cuboid(-0.25F, 0.5F, -6.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(77, 12).cuboid(-1.5F, 1.0F, 5.25F, 4.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, 8.25F, 0.0F, 0.0F, 0.0F, 2.3562F));

		ModelPartData cube_r170 = cables.addChild("cube_r170", ModelPartBuilder.create().uv(100, 96).cuboid(-1.0F, 0.0F, 0.0F, 3.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		ModelPartData cube_r171 = cables.addChild("cube_r171", ModelPartBuilder.create().uv(101, 33).cuboid(-1.0F, 0.0F, -6.0F, 3.0F, 0.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		ModelPartData lever3 = rot8.addChild("lever3", ModelPartBuilder.create().uv(74, 112).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(74, 112).cuboid(-7.0F, -1.0F, -1.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(28, 108).cuboid(-1.0F, -1.8F, -1.0F, 2.0F, 1.0F, 1.0F, new Dilation(-0.1F))
		.uv(51, 72).cuboid(-6.0F, -1.7F, -1.0F, 6.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(28, 108).cuboid(-7.0F, -1.8F, -1.0F, 2.0F, 1.0F, 1.0F, new Dilation(-0.1F))
		.uv(78, 121).cuboid(-6.5F, -2.05F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(78, 121).cuboid(-0.5F, -2.05F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(114, 72).cuboid(-4.0F, -2.5F, -1.5F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(96, 121).cuboid(-3.5F, -2.0F, 0.25F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.5F, 0.0F, -6.75F, -0.0182F, 0.4796F, -0.0393F));

		ModelPartData bone39 = lever3.addChild("bone39", ModelPartBuilder.create().uv(70, 38).cuboid(0.0F, -0.75F, 0.0F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(73, 121).cuboid(-0.5F, -0.5F, 1.75F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-3.0F, -1.5F, 1.0F, 0.48F, 0.0F, 0.0F));

		ModelPartData cube_r172 = bone39.addChild("cube_r172", ModelPartBuilder.create().uv(70, 38).cuboid(0.0F, -0.75F, 0.25F, 0.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -0.25F, 0.0F, 0.0F, -1.5708F));

		ModelPartData panel = rot8.addChild("panel", ModelPartBuilder.create().uv(49, 111).cuboid(-1.0F, -0.7F, -3.0F, 3.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(111, 103).cuboid(-0.75F, -1.0F, -2.8F, 3.0F, 1.0F, 4.0F, new Dilation(-0.2F)), ModelTransform.of(-13.0F, -0.5F, 2.5F, 0.0F, 0.0F, 0.48F));

		ModelPartData cube_r173 = panel.addChild("cube_r173", ModelPartBuilder.create().uv(83, 39).cuboid(-2.75F, -0.4F, -4.2F, 4.0F, 0.0F, 5.0F, new Dilation(0.0F))
		.uv(104, 57).cuboid(-2.75F, -0.3F, -4.2F, 4.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		ModelPartData dial2 = panel.addChild("dial2", ModelPartBuilder.create().uv(121, 71).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F))
		.uv(34, 45).cuboid(-0.5F, -1.75F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(1.25F, -0.1F, -2.25F));

		ModelPartData dial3 = panel.addChild("dial3", ModelPartBuilder.create().uv(121, 71).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(34, 45).cuboid(-0.5F, -1.75F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(1.25F, -0.1F, 0.25F));

		ModelPartData light15 = panel.addChild("light15", ModelPartBuilder.create().uv(60, 121).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(-0.5F, -0.75F, -2.4F));

		ModelPartData bone40 = light15.addChild("bone40", ModelPartBuilder.create().uv(55, 121).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.19F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData light16 = panel.addChild("light16", ModelPartBuilder.create().uv(60, 121).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(-0.5F, -0.75F, -1.5F));

		ModelPartData bone41 = light16.addChild("bone41", ModelPartBuilder.create().uv(55, 121).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.19F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData light17 = panel.addChild("light17", ModelPartBuilder.create().uv(60, 121).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(-0.5F, -0.75F, -0.6F));

		ModelPartData bone42 = light17.addChild("bone42", ModelPartBuilder.create().uv(55, 121).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.19F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData light18 = panel.addChild("light18", ModelPartBuilder.create().uv(60, 121).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(-0.5F, -0.75F, 0.3F));

		ModelPartData bone43 = light18.addChild("bone43", ModelPartBuilder.create().uv(55, 121).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.19F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData fluid_reservoir = rot8.addChild("fluid_reservoir", ModelPartBuilder.create().uv(79, 79).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-15.0F, -2.0F, -2.5F, 0.0F, 0.0F, -0.829F));

		ModelPartData cube_r174 = fluid_reservoir.addChild("cube_r174", ModelPartBuilder.create().uv(104, 118).cuboid(-1.2549F, -2.1001F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.0999F))
		.uv(121, 29).cuboid(-0.5049F, -2.6001F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F))
		.uv(8, 119).cuboid(-0.5049F, -2.1001F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.75F, -1.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		ModelPartData cube_r175 = fluid_reservoir.addChild("cube_r175", ModelPartBuilder.create().uv(109, 89).cuboid(-0.5546F, -0.5912F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(1.75F, -1.25F, 0.0F, 0.0F, 0.0F, -0.3054F));

		ModelPartData cube_r176 = fluid_reservoir.addChild("cube_r176", ModelPartBuilder.create().uv(114, 36).cuboid(0.35F, -1.45F, -0.5F, 3.0F, 1.0F, 1.0F, new Dilation(-0.099F))
		.uv(121, 0).cuboid(0.75F, -0.85F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(121, 0).cuboid(2.0F, -0.85F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(72, 56).cuboid(0.0F, -0.45F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(0.75F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		ModelPartData cube_r177 = fluid_reservoir.addChild("cube_r177", ModelPartBuilder.create().uv(120, 63).cuboid(-0.8158F, -0.9721F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(4.75F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5672F));

		ModelPartData needle = rot8.addChild("needle", ModelPartBuilder.create().uv(39, 33).cuboid(0.0F, -3.0F, -0.25F, 0.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(50, 120).cuboid(-0.5F, -2.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.pivot(-4.0F, 0.0F, 0.0F));

		ModelPartData cube_r178 = needle.addChild("cube_r178", ModelPartBuilder.create().uv(39, 33).cuboid(0.0F, -3.0F, -0.25F, 0.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData panel_3 = controls.addChild("panel_3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData rot9 = panel_3.addChild("rot9", ModelPartBuilder.create(), ModelTransform.of(20.0F, -13.0F, 0.0F, 0.0F, 0.0F, 0.5672F));

		ModelPartData meter = rot9.addChild("meter", ModelPartBuilder.create().uv(113, 67).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F))
		.uv(33, 66).cuboid(-0.15F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-14.0F, -1.0F, -3.25F, 0.0F, 0.0F, -0.829F));

		ModelPartData bone15 = meter.addChild("bone15", ModelPartBuilder.create().uv(28, 0).cuboid(0.85F, -1.0F, -0.25F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1F, 0.25F, 0.0F, 0.6109F, 0.0F, 0.0F));

		ModelPartData meter2 = rot9.addChild("meter2", ModelPartBuilder.create().uv(113, 67).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F))
		.uv(33, 66).cuboid(-0.15F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-14.0F, -1.0F, -1.15F, 0.0F, 0.0F, -0.829F));

		ModelPartData bone16 = meter2.addChild("bone16", ModelPartBuilder.create().uv(28, 0).cuboid(0.85F, -1.0F, -0.25F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1F, 0.25F, 0.0F, 0.6109F, 0.0F, 0.0F));

		ModelPartData meter3 = rot9.addChild("meter3", ModelPartBuilder.create().uv(113, 67).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F))
		.uv(33, 66).cuboid(-0.15F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-14.0F, -1.0F, 1.15F, 0.0F, 0.0F, -0.829F));

		ModelPartData bone17 = meter3.addChild("bone17", ModelPartBuilder.create().uv(28, 0).cuboid(0.85F, -1.0F, -0.25F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1F, 0.25F, 0.0F, 0.6109F, 0.0F, 0.0F));

		ModelPartData meter4 = rot9.addChild("meter4", ModelPartBuilder.create().uv(113, 67).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.2F))
		.uv(33, 66).cuboid(-0.15F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-14.0F, -1.0F, 3.25F, 0.0F, 0.0F, -0.829F));

		ModelPartData bone18 = meter4.addChild("bone18", ModelPartBuilder.create().uv(28, 0).cuboid(0.85F, -1.0F, -0.25F, 0.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1F, 0.25F, 0.0F, 0.6109F, 0.0F, 0.0F));

		ModelPartData bone19 = rot9.addChild("bone19", ModelPartBuilder.create().uv(113, 92).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 1.0F, 4.0F, new Dilation(0.2F))
		.uv(119, 23).cuboid(-0.5F, -3.0F, 0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.2F))
		.uv(113, 51).cuboid(-1.25F, -2.1F, -0.75F, 2.0F, 1.0F, 4.0F, new Dilation(0.2F)), ModelTransform.of(-17.0F, -1.75F, -1.0F, 0.0F, 0.0F, 0.7418F));

		ModelPartData vent = rot9.addChild("vent", ModelPartBuilder.create().uv(0, 73).cuboid(-3.0F, -1.501F, -1.0F, 4.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 0.851F, 7.75F, 0.0227F, -0.4795F, -0.0492F));

		ModelPartData cube_r179 = vent.addChild("cube_r179", ModelPartBuilder.create().uv(97, 57).cuboid(-2.9F, 0.05F, -1.0F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -1.601F, 0.0F, 0.0F, 0.0F, 0.288F));

		ModelPartData levers = rot9.addChild("levers", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, -1.0F));

		ModelPartData lever4 = levers.addChild("lever4", ModelPartBuilder.create().uv(113, 98).cuboid(-1.0F, -0.25F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.pivot(-3.55F, -0.15F, 2.0F));

		ModelPartData bone20 = lever4.addChild("bone20", ModelPartBuilder.create().uv(63, 30).cuboid(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(123, 45).cuboid(-0.25F, -1.3F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(-0.5F, 0.0F, -0.4F));

		ModelPartData lever5 = levers.addChild("lever5", ModelPartBuilder.create().uv(113, 98).cuboid(-1.0F, -0.25F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-2.75F, -0.15F, 3.25F, 0.0F, 0.7854F, 0.0F));

		ModelPartData bone21 = lever5.addChild("bone21", ModelPartBuilder.create().uv(63, 30).cuboid(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(123, 45).cuboid(-0.25F, -1.3F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(-0.5F, 0.0F, -0.4F));

		ModelPartData lever6 = levers.addChild("lever6", ModelPartBuilder.create().uv(113, 98).cuboid(-1.0F, -0.25F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F)), ModelTransform.of(-1.25F, -0.15F, 3.5F, 0.0F, 1.5708F, 0.0F));

		ModelPartData bone22 = lever6.addChild("bone22", ModelPartBuilder.create().uv(63, 30).cuboid(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(123, 45).cuboid(-0.25F, -1.3F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(-0.5F, 0.0F, -0.4F));

		ModelPartData needle2 = rot9.addChild("needle2", ModelPartBuilder.create().uv(62, 50).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(38, 15).cuboid(-0.25F, -2.6F, 0.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
		.uv(45, 120).cuboid(-0.5F, -0.8F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(-2.0F, -0.2F, -3.5F));

		ModelPartData cube_r180 = needle2.addChild("cube_r180", ModelPartBuilder.create().uv(38, 15).cuboid(-0.25F, -2.6F, 0.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData bone23 = rot9.addChild("bone23", ModelPartBuilder.create().uv(67, 91).cuboid(-1.0F, -2.0F, -1.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-6.25F, 1.75F, 0.0F, 0.0F, 0.0F, -0.5672F));

		ModelPartData bone24 = bone23.addChild("bone24", ModelPartBuilder.create().uv(40, 111).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F))
		.uv(69, 30).cuboid(-0.5F, -0.7F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.05F))
		.uv(69, 30).cuboid(-0.5F, -1.2F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.05F)), ModelTransform.pivot(1.0F, -2.0F, 0.0F));

		ModelPartData bone26 = bone24.addChild("bone26", ModelPartBuilder.create().uv(36, 93).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(112, 0).cuboid(-1.0F, -1.5F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.3F))
		.uv(53, 83).cuboid(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(-0.2F))
		.uv(53, 88).cuboid(-0.5F, -1.0F, -1.5F, 1.0F, 1.0F, 3.0F, new Dilation(-0.4F)), ModelTransform.pivot(0.0F, -0.6F, 0.0F));

		ModelPartData bone27 = bone26.addChild("bone27", ModelPartBuilder.create().uv(69, 119).cuboid(-0.5F, -1.4F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.04F))
		.uv(119, 26).cuboid(-0.5F, -1.85F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.24F))
		.uv(64, 119).cuboid(-0.5F, -1.25F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.25F, 0.0F));

		ModelPartData stabilizers = rot9.addChild("stabilizers", ModelPartBuilder.create().uv(40, 120).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.0F, 0.5F, 4.5F));

		ModelPartData bone25 = stabilizers.addChild("bone25", ModelPartBuilder.create().uv(0, 120).cuboid(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F))
		.uv(118, 119).cuboid(-0.5F, -1.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.pivot(0.0F, -1.0F, 0.0F));

		ModelPartData column = copper.addChild("column", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData wood = column.addChild("wood", ModelPartBuilder.create(), ModelTransform.of(3.25F, -24.0F, -5.25F, 0.0785F, -0.5236F, 0.0F));

		ModelPartData bone = wood.addChild("bone", ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, -86.0F, -6.05F, 7.0F, 61.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.0981F, 31.0F, 5.8301F));

		ModelPartData bone28 = wood.addChild("bone28", ModelPartBuilder.create().uv(17, 0).cuboid(-0.5F, -86.0F, -6.05F, 4.0F, 61.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.0981F, 31.0F, 5.8301F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone44 = wood.addChild("bone44", ModelPartBuilder.create().uv(17, 0).cuboid(-3.5F, -86.0F, -6.05F, 4.0F, 61.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.0981F, 31.0F, 5.8301F, 0.0F, -1.0472F, 0.0F));

		ModelPartData wood2 = column.addChild("wood2", ModelPartBuilder.create(), ModelTransform.of(-3.25F, -24.0F, 5.25F, -3.0631F, 0.5236F, -3.1416F));

		ModelPartData bone47 = wood2.addChild("bone47", ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, -86.0F, -6.05F, 7.0F, 61.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.0981F, 31.0F, 5.8301F));

		ModelPartData bone48 = wood2.addChild("bone48", ModelPartBuilder.create().uv(17, 0).cuboid(-0.5F, -86.0F, -6.05F, 4.0F, 61.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.0981F, 31.0F, 5.8301F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone49 = wood2.addChild("bone49", ModelPartBuilder.create().uv(17, 0).cuboid(-3.5F, -86.0F, -6.05F, 4.0F, 61.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.0981F, 31.0F, 5.8301F, 0.0F, -1.0472F, 0.0F));

		ModelPartData rings = column.addChild("rings", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bone51 = rings.addChild("bone51", ModelPartBuilder.create().uv(40, 111).cuboid(4.15F, -27.0F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 0.0F));

		ModelPartData bone52 = bone51.addChild("bone52", ModelPartBuilder.create().uv(40, 111).cuboid(4.15F, -27.0F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone53 = bone52.addChild("bone53", ModelPartBuilder.create().uv(40, 111).cuboid(4.15F, -27.0F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone54 = bone53.addChild("bone54", ModelPartBuilder.create().uv(40, 111).cuboid(4.15F, -27.0F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone55 = bone54.addChild("bone55", ModelPartBuilder.create().uv(40, 111).cuboid(4.15F, -27.0F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone56 = bone55.addChild("bone56", ModelPartBuilder.create().uv(40, 111).cuboid(4.15F, -27.0F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone57 = rings.addChild("bone57", ModelPartBuilder.create().uv(99, 86).cuboid(5.0F, -27.0F, -3.5F, 1.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -19.0F, 0.0F));

		ModelPartData bone58 = bone57.addChild("bone58", ModelPartBuilder.create().uv(99, 86).cuboid(5.0F, -27.0F, -3.5F, 1.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone59 = bone58.addChild("bone59", ModelPartBuilder.create().uv(99, 86).cuboid(5.0F, -27.0F, -3.5F, 1.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone60 = bone59.addChild("bone60", ModelPartBuilder.create().uv(99, 86).cuboid(5.0F, -27.0F, -3.5F, 1.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone61 = bone60.addChild("bone61", ModelPartBuilder.create().uv(99, 86).cuboid(5.0F, -27.0F, -3.5F, 1.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone62 = bone61.addChild("bone62", ModelPartBuilder.create().uv(99, 86).cuboid(5.0F, -27.0F, -3.5F, 1.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone63 = rings.addChild("bone63", ModelPartBuilder.create().uv(87, 90).cuboid(5.9F, -27.0F, -4.0F, 1.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -34.0F, 0.0F));

		ModelPartData bone64 = bone63.addChild("bone64", ModelPartBuilder.create().uv(87, 90).cuboid(5.9F, -27.0F, -4.0F, 1.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone65 = bone64.addChild("bone65", ModelPartBuilder.create().uv(87, 90).cuboid(5.9F, -27.0F, -4.0F, 1.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone66 = bone65.addChild("bone66", ModelPartBuilder.create().uv(87, 90).cuboid(5.9F, -27.0F, -4.0F, 1.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone67 = bone66.addChild("bone67", ModelPartBuilder.create().uv(87, 90).cuboid(5.9F, -27.0F, -4.0F, 1.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone68 = bone67.addChild("bone68", ModelPartBuilder.create().uv(87, 90).cuboid(5.9F, -27.0F, -4.0F, 1.0F, 2.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone69 = rings.addChild("bone69", ModelPartBuilder.create().uv(41, 84).cuboid(6.75F, -27.0F, -4.5F, 1.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -49.0F, 0.0F));

		ModelPartData bone70 = bone69.addChild("bone70", ModelPartBuilder.create().uv(41, 84).cuboid(6.75F, -27.0F, -4.5F, 1.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone71 = bone70.addChild("bone71", ModelPartBuilder.create().uv(41, 84).cuboid(6.75F, -27.0F, -4.5F, 1.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone72 = bone71.addChild("bone72", ModelPartBuilder.create().uv(41, 84).cuboid(6.75F, -27.0F, -4.5F, 1.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone73 = bone72.addChild("bone73", ModelPartBuilder.create().uv(41, 84).cuboid(6.75F, -27.0F, -4.5F, 1.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone74 = bone73.addChild("bone74", ModelPartBuilder.create().uv(41, 84).cuboid(6.75F, -27.0F, -4.5F, 1.0F, 2.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone50 = rings.addChild("bone50", ModelPartBuilder.create().uv(109, 80).cuboid(4.2F, -27.0F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(63, 12).cuboid(4.2F, -26.5F, 0.0F, 3.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -49.0F, 0.0F));

		ModelPartData bone79 = bone50.addChild("bone79", ModelPartBuilder.create().uv(109, 80).cuboid(4.2F, -27.0F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(63, 12).cuboid(4.2F, -26.5F, 0.0F, 3.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone80 = bone79.addChild("bone80", ModelPartBuilder.create().uv(109, 80).cuboid(4.2F, -27.0F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(63, 12).cuboid(4.2F, -26.5F, 0.0F, 3.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone81 = bone80.addChild("bone81", ModelPartBuilder.create().uv(109, 80).cuboid(4.2F, -27.0F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(63, 12).cuboid(4.2F, -26.5F, 0.0F, 3.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone82 = bone81.addChild("bone82", ModelPartBuilder.create().uv(109, 80).cuboid(4.2F, -27.0F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(63, 12).cuboid(4.2F, -26.5F, 0.0F, 3.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone83 = bone82.addChild("bone83", ModelPartBuilder.create().uv(109, 80).cuboid(4.2F, -27.0F, -3.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(63, 12).cuboid(4.2F, -26.5F, 0.0F, 3.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone45 = rings.addChild("bone45", ModelPartBuilder.create().uv(72, 48).cuboid(8.5F, -27.0F, -5.5F, 1.0F, 2.0F, 11.0F, new Dilation(0.0F))
		.uv(107, 30).cuboid(6.5F, -26.5F, -3.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -19.0F, 0.0F));

		ModelPartData bone46 = bone45.addChild("bone46", ModelPartBuilder.create().uv(72, 48).cuboid(8.5F, -27.0F, -5.5F, 1.0F, 2.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone75 = bone46.addChild("bone75", ModelPartBuilder.create().uv(72, 48).cuboid(8.5F, -27.0F, -5.5F, 1.0F, 2.0F, 11.0F, new Dilation(0.0F))
		.uv(107, 30).cuboid(6.5F, -26.5F, 2.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone76 = bone75.addChild("bone76", ModelPartBuilder.create().uv(72, 48).cuboid(8.5F, -27.0F, -5.5F, 1.0F, 2.0F, 11.0F, new Dilation(0.0F))
		.uv(107, 30).cuboid(6.5F, -26.5F, -3.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone77 = bone76.addChild("bone77", ModelPartBuilder.create().uv(72, 48).cuboid(8.5F, -27.0F, -5.5F, 1.0F, 2.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone78 = bone77.addChild("bone78", ModelPartBuilder.create().uv(72, 48).cuboid(8.5F, -27.0F, -5.5F, 1.0F, 2.0F, 11.0F, new Dilation(0.0F))
		.uv(107, 30).cuboid(6.5F, -26.5F, 2.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData rotor7 = column.addChild("rotor7", ModelPartBuilder.create().uv(0, 89).cuboid(-2.5F, -31.0F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.5F))
		.uv(114, 30).cuboid(-1.0F, -34.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.1F))
		.uv(17, 66).cuboid(-0.5F, -48.5F, -0.5F, 1.0F, 5.0F, 1.0F, new Dilation(-0.1F))
		.uv(62, 91).cuboid(-0.5F, -25.5F, -0.5F, 1.0F, 22.0F, 1.0F, new Dilation(-0.1F))
		.uv(119, 20).cuboid(-0.5F, -49.75F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(64, 112).cuboid(-1.5F, -37.5F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.2F))
		.uv(64, 112).cuboid(-1.5F, -40.65F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(97, 40).cuboid(-1.0F, -44.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F))
		.uv(97, 40).cuboid(-1.0F, -49.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(-0.2F))
		.uv(64, 112).cuboid(-1.5F, -43.4F, -1.5F, 3.0F, 3.0F, 3.0F, new Dilation(-0.2F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData top = column.addChild("top", ModelPartBuilder.create().uv(87, 12).cuboid(-2.5F, -62.0F, -2.5F, 5.0F, 5.0F, 5.0F, new Dilation(0.0F))
		.uv(31, 111).cuboid(-1.0F, -75.0F, -1.0F, 2.0F, 13.0F, 2.0F, new Dilation(0.3F))
		.uv(96, 23).cuboid(-1.0F, -75.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.6F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData horn = column.addChild("horn", ModelPartBuilder.create().uv(88, 93).cuboid(0.0F, -2.0F, 0.0F, 3.0F, 2.0F, 0.0F, new Dilation(0.0F))
		.uv(17, 63).cuboid(4.0F, -6.0F, -0.9F, 4.0F, 1.0F, 1.0F, new Dilation(-0.1F))
		.uv(119, 17).cuboid(6.75F, -6.75F, 0.1F, 2.0F, 2.0F, 0.0F, new Dilation(0.0F))
		.uv(86, 48).cuboid(4.0F, -9.0F, 0.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(103, 11).cuboid(4.0F, -10.75F, 0.0F, 1.0F, 2.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(6.0F, -27.0F, -2.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r181 = horn.addChild("cube_r181", ModelPartBuilder.create().uv(111, 109).cuboid(-0.6F, -2.75F, -1.0F, 3.0F, 3.0F, 3.0F, new Dilation(0.0F))
		.uv(100, 0).cuboid(2.4F, -3.75F, -2.0F, 3.0F, 5.0F, 5.0F, new Dilation(0.0F))
		.uv(60, 72).cuboid(5.5F, -5.75F, -4.0F, 0.0F, 9.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(4.5F, -10.0F, 0.0F, 0.0F, 0.0F, -0.1309F));

		ModelPartData cube_r182 = horn.addChild("cube_r182", ModelPartBuilder.create().uv(62, 48).cuboid(1.0F, 0.0F, 0.0F, 4.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(39, 38).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(5.1492F, -3.3736F, 0.0F, 0.0F, 0.0F, -1.5708F));

		ModelPartData cube_r183 = horn.addChild("cube_r183", ModelPartBuilder.create().uv(76, 39).cuboid(0.0F, -2.0F, 0.0F, 4.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0036F));

		ModelPartData rack = column.addChild("rack", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bone86 = rack.addChild("bone86", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bone84 = bone86.addChild("bone84", ModelPartBuilder.create(), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r184 = bone84.addChild("cube_r184", ModelPartBuilder.create().uv(63, 30).cuboid(0.0F, -0.5F, -0.25F, 0.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -46.0F, 9.0F, 0.0436F, 0.0F, 0.0F));

		ModelPartData cube_r185 = bone84.addChild("cube_r185", ModelPartBuilder.create().uv(63, 30).cuboid(0.0F, 0.0F, -0.25F, 0.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -48.0F, 9.0F, -0.0436F, 0.0F, 0.0F));

		ModelPartData bone85 = bone86.addChild("bone85", ModelPartBuilder.create(), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r186 = bone85.addChild("cube_r186", ModelPartBuilder.create().uv(63, 30).cuboid(0.0F, -0.5F, -0.25F, 0.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -46.0F, 9.0F, 0.0436F, 0.0F, 0.0F));

		ModelPartData cube_r187 = bone85.addChild("cube_r187", ModelPartBuilder.create().uv(63, 30).cuboid(0.0F, 0.0F, -0.25F, 0.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -48.0F, 9.0F, -0.0436F, 0.0F, 0.0F));

		ModelPartData bone87 = bone86.addChild("bone87", ModelPartBuilder.create().uv(72, 62).cuboid(-12.0F, -46.0F, 20.75F, 11.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData bone88 = bone87.addChild("bone88", ModelPartBuilder.create().uv(57, 45).cuboid(-12.0F, -46.0F, 20.75F, 24.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone89 = bone88.addChild("bone89", ModelPartBuilder.create().uv(72, 62).cuboid(1.0F, -46.0F, 20.75F, 11.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone90 = rack.addChild("bone90", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData bone91 = bone90.addChild("bone91", ModelPartBuilder.create(), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r188 = bone91.addChild("cube_r188", ModelPartBuilder.create().uv(63, 30).cuboid(0.0F, -0.5F, -0.25F, 0.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -46.0F, 9.0F, 0.0436F, 0.0F, 0.0F));

		ModelPartData cube_r189 = bone91.addChild("cube_r189", ModelPartBuilder.create().uv(63, 30).cuboid(0.0F, 0.0F, -0.25F, 0.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -48.0F, 9.0F, -0.0436F, 0.0F, 0.0F));

		ModelPartData bone92 = bone90.addChild("bone92", ModelPartBuilder.create(), ModelTransform.of(0.0F, 2.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r190 = bone92.addChild("cube_r190", ModelPartBuilder.create().uv(63, 30).cuboid(0.0F, -0.5F, -0.25F, 0.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -46.0F, 9.0F, 0.0436F, 0.0F, 0.0F));

		ModelPartData cube_r191 = bone92.addChild("cube_r191", ModelPartBuilder.create().uv(63, 30).cuboid(0.0F, 0.0F, -0.25F, 0.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, -48.0F, 9.0F, -0.0436F, 0.0F, 0.0F));

		ModelPartData bone93 = bone90.addChild("bone93", ModelPartBuilder.create().uv(72, 62).cuboid(-12.0F, -46.0F, 20.75F, 11.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData bone94 = bone93.addChild("bone94", ModelPartBuilder.create().uv(57, 45).cuboid(-12.0F, -46.0F, 20.75F, 24.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone95 = bone94.addChild("bone95", ModelPartBuilder.create().uv(72, 62).cuboid(1.0F, -46.0F, 20.75F, 11.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.push();

		matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(150f));

		copper.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);

		matrices.pop();
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
		return copper;
	}

	@Override
	public Animation getAnimationForState(TardisTravel.State state) {
		return Animation.Builder.create(0).build();
	}
}