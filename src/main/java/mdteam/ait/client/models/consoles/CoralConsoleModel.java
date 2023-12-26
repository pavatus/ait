package mdteam.ait.client.models.consoles;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.console.hartnell.HartnellAnimations;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.TardisTravel;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class CoralConsoleModel extends ConsoleModel {

	public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/blockentities/consoles/coral_console.png");
	public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, "textures/blockentities/consoles/coral_console_emission.png");
	public static final Animation EMPTY_ANIM = Animation.Builder.create(1).build(); // temporary animation bc rn we have none

	private final ModelPart coral;

	public CoralConsoleModel(ModelPart root) {
		this.coral = root.getChild("coral");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData coral = modelPartData.addChild("coral", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData bb_main3_r1 = coral.addChild("bb_main3_r1", ModelPartBuilder.create().uv(89, 111).cuboid(-1.525F, -20.175F, 0.55F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 2.618F, 0.5236F, -3.1416F));

		ModelPartData bb_main2_r1 = coral.addChild("bb_main2_r1", ModelPartBuilder.create().uv(0, 21).cuboid(-9.0F, -20.2F, -0.75F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 2.618F, -0.5236F, 3.1416F));

		ModelPartData cube_r1 = coral.addChild("cube_r1", ModelPartBuilder.create().uv(49, 8).cuboid(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.5359F, -13.5F, -15.8564F, -0.6319F, -0.9025F, 0.7505F));

		ModelPartData cube_r2 = coral.addChild("cube_r2", ModelPartBuilder.create().uv(0, 34).cuboid(-0.25F, -4.5F, -0.5F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(18.0284F, -7.3756F, -7.5399F, 0.4821F, 0.2129F, 1.187F));

		ModelPartData cube_r3 = coral.addChild("cube_r3", ModelPartBuilder.create().uv(5, 88).cuboid(-18.5F, -6.5F, -3.0F, 1.0F, 9.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData middle_ring7_r1 = coral.addChild("middle_ring7_r1", ModelPartBuilder.create().uv(0, 47).cuboid(-1.0F, -0.5F, -1.0F, 1.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-17.3032F, -10.5F, -13.97F, 0.0F, -0.7854F, 0.0F));

		ModelPartData bone19 = coral.addChild("bone19", ModelPartBuilder.create().uv(17, 121).cuboid(-2.634F, -2.0F, 4.5622F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.741F, -22.3F, 0.0128F));

		ModelPartData cube_r4 = bone19.addChild("cube_r4", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone20 = bone19.addChild("bone20", ModelPartBuilder.create().uv(17, 121).cuboid(-3.366F, -2.0F, 4.5622F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r5 = bone20.addChild("cube_r5", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.7321F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone21 = bone20.addChild("bone21", ModelPartBuilder.create().uv(17, 121).cuboid(-3.7321F, -2.0F, 5.1962F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r6 = bone21.addChild("cube_r6", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0981F, 0.0F, 0.634F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone22 = bone21.addChild("bone22", ModelPartBuilder.create().uv(17, 121).cuboid(-3.366F, -2.0F, 5.8301F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r7 = bone22.addChild("cube_r7", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.7321F, 0.0F, 1.2679F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone23 = bone22.addChild("bone23", ModelPartBuilder.create().uv(17, 121).cuboid(-2.634F, -2.0F, 5.8301F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r8 = bone23.addChild("cube_r8", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 1.2679F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone24 = bone23.addChild("bone24", ModelPartBuilder.create().uv(17, 121).cuboid(-2.2679F, -2.0F, 5.1962F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r9 = bone24.addChild("cube_r9", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.366F, 0.0F, 0.634F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone25 = bone19.addChild("bone25", ModelPartBuilder.create().uv(17, 121).cuboid(-2.634F, -41.0F, 4.5622F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r10 = bone25.addChild("cube_r10", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -41.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone26 = bone25.addChild("bone26", ModelPartBuilder.create().uv(17, 121).cuboid(-3.366F, -41.0F, 4.5622F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r11 = bone26.addChild("cube_r11", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -41.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.7321F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone27 = bone26.addChild("bone27", ModelPartBuilder.create().uv(17, 121).cuboid(-3.7321F, -41.0F, 5.1962F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r12 = bone27.addChild("cube_r12", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -41.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0981F, 0.0F, 0.634F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone28 = bone27.addChild("bone28", ModelPartBuilder.create().uv(17, 121).cuboid(-3.366F, -41.0F, 5.8301F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r13 = bone28.addChild("cube_r13", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -41.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.7321F, 0.0F, 1.2679F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone29 = bone28.addChild("bone29", ModelPartBuilder.create().uv(17, 121).cuboid(-2.634F, -41.0F, 5.8301F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r14 = bone29.addChild("cube_r14", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -41.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 1.2679F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone30 = bone29.addChild("bone30", ModelPartBuilder.create().uv(17, 121).cuboid(-2.2679F, -41.0F, 5.1962F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r15 = bone30.addChild("cube_r15", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -41.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.366F, 0.0F, 0.634F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone7 = coral.addChild("bone7", ModelPartBuilder.create().uv(17, 121).cuboid(-2.634F, -2.0F, 4.5622F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.741F, -19.5F, 0.0128F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r16 = bone7.addChild("cube_r16", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone8 = bone7.addChild("bone8", ModelPartBuilder.create().uv(17, 121).cuboid(-3.366F, -2.0F, 4.5622F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r17 = bone8.addChild("cube_r17", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.7321F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone9 = bone8.addChild("bone9", ModelPartBuilder.create().uv(17, 121).cuboid(-3.7321F, -2.0F, 5.1962F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r18 = bone9.addChild("cube_r18", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0981F, 0.0F, 0.634F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone10 = bone9.addChild("bone10", ModelPartBuilder.create().uv(17, 121).cuboid(-3.366F, -2.0F, 5.8301F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r19 = bone10.addChild("cube_r19", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.7321F, 0.0F, 1.2679F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone11 = bone10.addChild("bone11", ModelPartBuilder.create().uv(17, 121).cuboid(-2.634F, -2.0F, 5.8301F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r20 = bone11.addChild("cube_r20", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 1.2679F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone12 = bone11.addChild("bone12", ModelPartBuilder.create().uv(17, 121).cuboid(-2.2679F, -2.0F, 5.1962F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r21 = bone12.addChild("cube_r21", ModelPartBuilder.create().uv(17, 121).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.366F, 0.0F, 0.634F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone13 = bone7.addChild("bone13", ModelPartBuilder.create().uv(103, 127).cuboid(-2.634F, -2.0F, 4.5622F, 6.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, 0.0F));

		ModelPartData cube_r22 = bone13.addChild("cube_r22", ModelPartBuilder.create().uv(103, 127).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone14 = bone13.addChild("bone14", ModelPartBuilder.create().uv(103, 127).cuboid(-3.366F, -2.0F, 4.5622F, 6.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r23 = bone14.addChild("cube_r23", ModelPartBuilder.create().uv(103, 127).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.7321F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone15 = bone14.addChild("bone15", ModelPartBuilder.create().uv(103, 127).cuboid(-3.7321F, -2.0F, 5.1962F, 6.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r24 = bone15.addChild("cube_r24", ModelPartBuilder.create().uv(103, 127).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0981F, 0.0F, 0.634F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone16 = bone15.addChild("bone16", ModelPartBuilder.create().uv(103, 127).cuboid(-3.366F, -2.0F, 5.8301F, 6.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r25 = bone16.addChild("cube_r25", ModelPartBuilder.create().uv(103, 127).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.7321F, 0.0F, 1.2679F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone17 = bone16.addChild("bone17", ModelPartBuilder.create().uv(103, 127).cuboid(-2.634F, -2.0F, 5.8301F, 6.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r26 = bone17.addChild("cube_r26", ModelPartBuilder.create().uv(103, 127).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 1.2679F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone18 = bone17.addChild("bone18", ModelPartBuilder.create().uv(103, 127).cuboid(-2.2679F, -2.0F, 5.1962F, 6.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r27 = bone18.addChild("cube_r27", ModelPartBuilder.create().uv(103, 127).cuboid(-1.0F, -2.0F, 5.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.366F, 0.0F, 0.634F, 0.0F, -0.5236F, 0.0F));

		ModelPartData rotor_bottom = coral.addChild("rotor_bottom", ModelPartBuilder.create().uv(68, 73).cuboid(-1.0F, -11.7571F, 2.0F, 2.0F, 25.0F, 2.0F, new Dilation(0.0F))
		.uv(82, 85).cuboid(-0.5F, -11.7571F, -3.5F, 1.0F, 25.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(0.0F, -29.8929F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData rotor_ring_bottom_r1 = rotor_bottom.addChild("rotor_ring_bottom_r1", ModelPartBuilder.create().uv(68, 73).cuboid(-1.0F, -45.3F, 2.0F, 2.0F, 25.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 33.5429F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData rotor_ring_bottom_r2 = rotor_bottom.addChild("rotor_ring_bottom_r2", ModelPartBuilder.create().uv(87, 85).cuboid(-0.5F, -0.3F, -3.5F, 1.0F, 25.0F, 1.0F, new Dilation(0.1F))
		.uv(68, 73).cuboid(-1.0F, -0.3F, 2.0F, 2.0F, 25.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -11.4571F, 0.0F, 3.1416F, 1.0472F, 3.1416F));

		ModelPartData rotor_ring_bottom_r3 = rotor_bottom.addChild("rotor_ring_bottom_r3", ModelPartBuilder.create().uv(0, 88).cuboid(-0.5F, -41.3F, -3.5F, 1.0F, 25.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(0.0F, 29.5429F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData console_collar_one16 = rotor_bottom.addChild("console_collar_one16", ModelPartBuilder.create(), ModelTransform.of(0.0F, 42.1429F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData console_collar_one17 = console_collar_one16.addChild("console_collar_one17", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone73 = rotor_bottom.addChild("bone73", ModelPartBuilder.create(), ModelTransform.of(0.0F, 42.1429F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone74 = bone73.addChild("bone74", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone75 = bone74.addChild("bone75", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone76 = bone75.addChild("bone76", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone77 = bone76.addChild("bone77", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone78 = bone77.addChild("bone78", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData rotor_ring = rotor_bottom.addChild("rotor_ring", ModelPartBuilder.create().uv(98, 22).cuboid(-1.866F, -3.2F, -5.6962F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.134F, 3.5929F, 0.1962F));

		ModelPartData cube_r28 = rotor_ring.addChild("cube_r28", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -3.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone62 = rotor_ring.addChild("bone62", ModelPartBuilder.create().uv(98, 22).cuboid(-2.134F, -3.2F, -5.6962F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r29 = bone62.addChild("cube_r29", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -3.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.2679F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone63 = bone62.addChild("bone63", ModelPartBuilder.create().uv(92, 92).cuboid(-2.2679F, -3.2F, -5.4641F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r30 = bone63.addChild("cube_r30", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -3.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.4019F, 0.0F, 0.2321F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone64 = bone63.addChild("bone64", ModelPartBuilder.create().uv(98, 22).cuboid(-2.134F, -3.2F, -5.2321F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r31 = bone64.addChild("cube_r31", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -3.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.2679F, 0.0F, 0.4641F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone65 = bone64.addChild("bone65", ModelPartBuilder.create().uv(98, 22).cuboid(-1.866F, -3.2F, -5.2321F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r32 = bone65.addChild("cube_r32", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -3.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.4641F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone66 = bone65.addChild("bone66", ModelPartBuilder.create().uv(98, 22).cuboid(-1.7321F, -3.2F, -5.4641F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r33 = bone66.addChild("cube_r33", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -3.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.134F, 0.0F, 0.2321F, 0.0F, -0.5236F, 0.0F));

		ModelPartData rotor_ring2 = rotor_ring.addChild("rotor_ring2", ModelPartBuilder.create().uv(98, 22).cuboid(-1.866F, -3.2F, -5.6962F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r34 = rotor_ring2.addChild("cube_r34", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -3.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone61 = rotor_ring2.addChild("bone61", ModelPartBuilder.create().uv(98, 22).cuboid(-2.134F, -3.2F, -5.6962F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r35 = bone61.addChild("cube_r35", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -3.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.2679F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone67 = bone61.addChild("bone67", ModelPartBuilder.create().uv(92, 92).cuboid(-2.2679F, -3.2F, -5.4641F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r36 = bone67.addChild("cube_r36", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -3.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.4019F, 0.0F, 0.2321F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone68 = bone67.addChild("bone68", ModelPartBuilder.create().uv(98, 22).cuboid(-2.134F, -3.2F, -5.2321F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r37 = bone68.addChild("cube_r37", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -3.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.2679F, 0.0F, 0.4641F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone69 = bone68.addChild("bone69", ModelPartBuilder.create().uv(98, 22).cuboid(-1.866F, -3.2F, -5.2321F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r38 = bone69.addChild("cube_r38", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -3.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.4641F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone70 = bone69.addChild("bone70", ModelPartBuilder.create().uv(98, 22).cuboid(-1.7321F, -3.2F, -5.4641F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r39 = bone70.addChild("cube_r39", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -3.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.134F, 0.0F, 0.2321F, 0.0F, -0.5236F, 0.0F));

		ModelPartData rotor_top = coral.addChild("rotor_top", ModelPartBuilder.create().uv(53, 84).cuboid(-0.5F, -69.9F, 2.5F, 1.0F, 25.0F, 1.0F, new Dilation(0.2F))
		.uv(59, 73).cuboid(-1.0F, -69.9F, -4.0F, 2.0F, 25.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 3.7F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData rotor_ring_top3_r1 = rotor_top.addChild("rotor_ring_top3_r1", ModelPartBuilder.create().uv(59, 73).cuboid(-1.0F, -65.9F, -4.0F, 2.0F, 25.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData rotor_ring_top3_r2 = rotor_top.addChild("rotor_ring_top3_r2", ModelPartBuilder.create().uv(48, 84).cuboid(-0.5F, -66.4F, 2.5F, 1.0F, 25.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(0.0F, -3.5F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData rotor_ring_top3_r3 = rotor_top.addChild("rotor_ring_top3_r3", ModelPartBuilder.create().uv(77, 85).cuboid(-0.5F, -24.9F, 2.5F, 1.0F, 25.0F, 1.0F, new Dilation(0.2F))
		.uv(59, 73).cuboid(-1.0F, -24.9F, -4.0F, 2.0F, 25.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -45.0F, 0.0F, 3.1416F, 1.0472F, 3.1416F));

		ModelPartData rotor_ring3 = rotor_top.addChild("rotor_ring3", ModelPartBuilder.create().uv(98, 22).cuboid(-1.866F, -25.2F, -5.6962F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.134F, -31.7F, 0.1962F));

		ModelPartData cube_r40 = rotor_ring3.addChild("cube_r40", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -25.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone71 = rotor_ring3.addChild("bone71", ModelPartBuilder.create().uv(98, 22).cuboid(-2.134F, -25.2F, -5.6962F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r41 = bone71.addChild("cube_r41", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -25.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.2679F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone72 = bone71.addChild("bone72", ModelPartBuilder.create().uv(92, 92).cuboid(-2.2679F, -25.2F, -5.4641F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r42 = bone72.addChild("cube_r42", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -25.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.4019F, 0.0F, 0.2321F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone79 = bone72.addChild("bone79", ModelPartBuilder.create().uv(98, 22).cuboid(-2.134F, -25.2F, -5.2321F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r43 = bone79.addChild("cube_r43", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -25.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.2679F, 0.0F, 0.4641F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone80 = bone79.addChild("bone80", ModelPartBuilder.create().uv(98, 22).cuboid(-1.866F, -25.2F, -5.2321F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r44 = bone80.addChild("cube_r44", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -25.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.4641F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone81 = bone80.addChild("bone81", ModelPartBuilder.create().uv(98, 22).cuboid(-1.7321F, -25.2F, -5.4641F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r45 = bone81.addChild("cube_r45", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -25.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.134F, 0.0F, 0.2321F, 0.0F, -0.5236F, 0.0F));

		ModelPartData rotor_ring4 = rotor_ring3.addChild("rotor_ring4", ModelPartBuilder.create().uv(98, 22).cuboid(-1.866F, -25.2F, -5.6962F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r46 = rotor_ring4.addChild("cube_r46", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -25.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone82 = rotor_ring4.addChild("bone82", ModelPartBuilder.create().uv(98, 22).cuboid(-2.134F, -25.2F, -5.6962F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r47 = bone82.addChild("cube_r47", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -25.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.2679F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone83 = bone82.addChild("bone83", ModelPartBuilder.create().uv(92, 92).cuboid(-2.2679F, -25.2F, -5.4641F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r48 = bone83.addChild("cube_r48", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -25.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.4019F, 0.0F, 0.2321F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone84 = bone83.addChild("bone84", ModelPartBuilder.create().uv(98, 22).cuboid(-2.134F, -25.2F, -5.2321F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r49 = bone84.addChild("cube_r49", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -25.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.2679F, 0.0F, 0.4641F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone85 = bone84.addChild("bone85", ModelPartBuilder.create().uv(98, 22).cuboid(-1.866F, -25.2F, -5.2321F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r50 = bone85.addChild("cube_r50", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -25.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.4641F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone86 = bone85.addChild("bone86", ModelPartBuilder.create().uv(98, 22).cuboid(-1.7321F, -25.2F, -5.4641F, 4.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r51 = bone86.addChild("cube_r51", ModelPartBuilder.create().uv(49, 35).cuboid(-1.0F, -25.2F, -6.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.134F, 0.0F, 0.2321F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone37 = coral.addChild("bone37", ModelPartBuilder.create().uv(96, 61).cuboid(-4.5897F, -18.0F, -7.4821F, 8.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.6F, -44.05F, -0.35F));

		ModelPartData cube_r52 = bone37.addChild("cube_r52", ModelPartBuilder.create().uv(92, 92).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-5.4558F, -16.0F, -6.9821F, 0.0F, 0.5236F, 0.0F));

		ModelPartData bone38 = bone37.addChild("bone38", ModelPartBuilder.create(), ModelTransform.pivot(4.5442F, 0.0F, -8.7141F));

		ModelPartData cube_r53 = bone38.addChild("cube_r53", ModelPartBuilder.create().uv(92, 92).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.134F, -16.0F, 1.2321F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r54 = bone38.addChild("cube_r54", ModelPartBuilder.create().uv(96, 61).cuboid(-4.0F, -2.0F, -9.4821F, 8.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-6.4796F, -16.0F, 9.9372F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone39 = bone38.addChild("bone39", ModelPartBuilder.create(), ModelTransform.pivot(-14.366F, 0.0F, 9.2942F));

		ModelPartData cube_r55 = bone39.addChild("cube_r55", ModelPartBuilder.create().uv(92, 92).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.366F, -16.0F, 0.366F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r56 = bone39.addChild("cube_r56", ModelPartBuilder.create().uv(96, 61).cuboid(-4.0F, -2.0F, -9.0F, 8.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.1603F, -16.0F, 0.4019F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone40 = bone39.addChild("bone40", ModelPartBuilder.create(), ModelTransform.pivot(-5.4282F, 0.0F, 8.3301F));

		ModelPartData cube_r57 = bone40.addChild("cube_r57", ModelPartBuilder.create().uv(96, 61).cuboid(-4.0F, -2.0F, -12.0F, 8.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r58 = bone40.addChild("cube_r58", ModelPartBuilder.create().uv(92, 92).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(9.7942F, -16.0F, -1.0359F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone41 = bone40.addChild("bone41", ModelPartBuilder.create().uv(96, 61).cuboid(-9.134F, -18.0F, -3.5F, 8.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(19.7942F, 0.0F, -0.0359F));

		ModelPartData cube_r59 = bone41.addChild("cube_r59", ModelPartBuilder.create().uv(92, 92).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.134F, -16.0F, -0.5F, 0.0F, 0.5236F, 0.0F));

		ModelPartData bone42 = bone41.addChild("bone42", ModelPartBuilder.create(), ModelTransform.pivot(10.7942F, 0.0F, 0.0359F));

		ModelPartData cube_r60 = bone42.addChild("cube_r60", ModelPartBuilder.create().uv(96, 61).cuboid(-3.2045F, -2.0F, -11.4103F, 8.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.1764F, -16.0F, 0.3941F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r61 = bone42.addChild("cube_r61", ModelPartBuilder.create().uv(41, 91).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-7.0622F, -16.0F, -7.9641F, 0.0F, 1.5708F, 0.0F));

		ModelPartData bone55 = coral.addChild("bone55", ModelPartBuilder.create().uv(90, 79).cuboid(-4.9558F, -43.4F, -9.5801F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -20.7F, 0.0F));

		ModelPartData cube_r62 = bone55.addChild("cube_r62", ModelPartBuilder.create().uv(5, 8).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-5.3218F, -41.4F, -8.2141F, 0.0F, 0.5236F, 0.0F));

		ModelPartData bone56 = bone55.addChild("bone56", ModelPartBuilder.create(), ModelTransform.pivot(4.5442F, 0.0F, -8.7141F));

		ModelPartData cube_r63 = bone56.addChild("cube_r63", ModelPartBuilder.create().uv(5, 8).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -41.4F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r64 = bone56.addChild("cube_r64", ModelPartBuilder.create().uv(90, 79).cuboid(-5.0F, -2.0F, -10.0F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-4.7942F, -41.4F, 8.9641F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone57 = bone56.addChild("bone57", ModelPartBuilder.create(), ModelTransform.pivot(-14.366F, 0.0F, 9.2942F));

		ModelPartData cube_r65 = bone57.addChild("cube_r65", ModelPartBuilder.create().uv(5, 8).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -41.4F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r66 = bone57.addChild("cube_r66", ModelPartBuilder.create().uv(90, 79).cuboid(-5.0F, -2.0F, -10.0F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(10.1603F, -41.4F, -0.3301F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone58 = bone57.addChild("bone58", ModelPartBuilder.create(), ModelTransform.pivot(-5.4282F, 0.0F, 8.3301F));

		ModelPartData cube_r67 = bone58.addChild("cube_r67", ModelPartBuilder.create().uv(90, 79).cuboid(-5.0F, -2.0F, -10.0F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -41.4F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r68 = bone58.addChild("cube_r68", ModelPartBuilder.create().uv(5, 8).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(9.9282F, -41.4F, -0.5359F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone59 = bone58.addChild("bone59", ModelPartBuilder.create().uv(90, 79).cuboid(-9.5F, -43.4F, -1.134F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(19.7942F, 0.0F, -0.0359F));

		ModelPartData cube_r69 = bone59.addChild("cube_r69", ModelPartBuilder.create().uv(5, 8).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -41.4F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData bone60 = bone59.addChild("bone60", ModelPartBuilder.create(), ModelTransform.pivot(10.7942F, 0.0F, 0.0359F));

		ModelPartData cube_r70 = bone60.addChild("cube_r70", ModelPartBuilder.create().uv(90, 79).cuboid(-5.0F, -2.0F, -10.0F, 10.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -41.4F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r71 = bone60.addChild("cube_r71", ModelPartBuilder.create().uv(5, 8).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-5.4282F, -41.4F, -8.3301F, 0.0F, 1.5708F, 0.0F));

		ModelPartData bone31 = coral.addChild("bone31", ModelPartBuilder.create().uv(96, 61).cuboid(-4.5897F, -4.0F, -7.4821F, 8.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.6F, -20.45F, -0.35F));

		ModelPartData cube_r72 = bone31.addChild("cube_r72", ModelPartBuilder.create().uv(92, 92).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-5.4558F, -2.0F, -6.9821F, 0.0F, 0.5236F, 0.0F));

		ModelPartData bone32 = bone31.addChild("bone32", ModelPartBuilder.create(), ModelTransform.pivot(4.5442F, 0.0F, -8.7141F));

		ModelPartData cube_r73 = bone32.addChild("cube_r73", ModelPartBuilder.create().uv(92, 92).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.134F, -2.0F, 1.2321F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r74 = bone32.addChild("cube_r74", ModelPartBuilder.create().uv(96, 61).cuboid(-4.0F, -2.0F, -9.4821F, 8.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-6.4796F, -2.0F, 9.9372F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone33 = bone32.addChild("bone33", ModelPartBuilder.create(), ModelTransform.pivot(-14.366F, 0.0F, 9.2942F));

		ModelPartData cube_r75 = bone33.addChild("cube_r75", ModelPartBuilder.create().uv(92, 92).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.366F, -2.0F, 0.366F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r76 = bone33.addChild("cube_r76", ModelPartBuilder.create().uv(96, 61).cuboid(-4.0F, -2.0F, -9.0F, 8.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(10.1603F, -2.0F, 0.4019F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone34 = bone33.addChild("bone34", ModelPartBuilder.create(), ModelTransform.pivot(-5.4282F, 0.0F, 8.3301F));

		ModelPartData cube_r77 = bone34.addChild("cube_r77", ModelPartBuilder.create().uv(96, 61).cuboid(-4.0F, -2.0F, -12.0F, 8.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r78 = bone34.addChild("cube_r78", ModelPartBuilder.create().uv(92, 92).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(9.7942F, -2.0F, -1.0359F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone35 = bone34.addChild("bone35", ModelPartBuilder.create().uv(96, 61).cuboid(-9.134F, -4.0F, -3.5F, 8.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(19.7942F, 0.0F, -0.0359F));

		ModelPartData cube_r79 = bone35.addChild("cube_r79", ModelPartBuilder.create().uv(92, 92).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.134F, -2.0F, -0.5F, 0.0F, 0.5236F, 0.0F));

		ModelPartData bone36 = bone35.addChild("bone36", ModelPartBuilder.create(), ModelTransform.pivot(10.7942F, 0.0F, 0.0359F));

		ModelPartData cube_r80 = bone36.addChild("cube_r80", ModelPartBuilder.create().uv(96, 61).cuboid(-3.2045F, -2.0F, -11.4103F, 8.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.1764F, -2.0F, 0.3941F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r81 = bone36.addChild("cube_r81", ModelPartBuilder.create().uv(41, 91).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-7.0622F, -2.0F, -7.9641F, 0.0F, 1.5708F, 0.0F));

		ModelPartData bone134 = coral.addChild("bone134", ModelPartBuilder.create().uv(87, 117).cuboid(-4.9558F, -2.0F, -9.5801F, 10.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -20.5F, 0.0F));

		ModelPartData cube_r82 = bone134.addChild("cube_r82", ModelPartBuilder.create().uv(5, 8).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-5.3218F, 0.0F, -8.2141F, 0.0F, 0.5236F, 0.0F));

		ModelPartData bone135 = bone134.addChild("bone135", ModelPartBuilder.create(), ModelTransform.pivot(4.5442F, 0.0F, -8.7141F));

		ModelPartData cube_r83 = bone135.addChild("cube_r83", ModelPartBuilder.create().uv(5, 8).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r84 = bone135.addChild("cube_r84", ModelPartBuilder.create().uv(87, 117).cuboid(-5.0F, -2.0F, -10.0F, 10.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-4.7942F, 0.0F, 8.9641F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone136 = bone135.addChild("bone136", ModelPartBuilder.create(), ModelTransform.pivot(-14.366F, 0.0F, 9.2942F));

		ModelPartData cube_r85 = bone136.addChild("cube_r85", ModelPartBuilder.create().uv(5, 8).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r86 = bone136.addChild("cube_r86", ModelPartBuilder.create().uv(87, 117).cuboid(-5.0F, -2.0F, -10.0F, 10.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(10.1603F, 0.0F, -0.3301F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone137 = bone136.addChild("bone137", ModelPartBuilder.create(), ModelTransform.pivot(-5.4282F, 0.0F, 8.3301F));

		ModelPartData cube_r87 = bone137.addChild("cube_r87", ModelPartBuilder.create().uv(87, 117).cuboid(-5.0F, -2.0F, -13.0F, 10.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r88 = bone137.addChild("cube_r88", ModelPartBuilder.create().uv(5, 8).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(9.9282F, 0.0F, -0.5359F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone138 = bone137.addChild("bone138", ModelPartBuilder.create().uv(87, 117).cuboid(-9.5F, -2.0F, -4.134F, 10.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(19.7942F, 0.0F, -0.0359F));

		ModelPartData cube_r89 = bone138.addChild("cube_r89", ModelPartBuilder.create().uv(5, 8).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData bone139 = bone138.addChild("bone139", ModelPartBuilder.create(), ModelTransform.pivot(10.7942F, 0.0F, 0.0359F));

		ModelPartData cube_r90 = bone139.addChild("cube_r90", ModelPartBuilder.create().uv(87, 117).cuboid(-5.0F, -2.0F, -13.0F, 10.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r91 = bone139.addChild("cube_r91", ModelPartBuilder.create().uv(5, 8).cuboid(0.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-5.4282F, 0.0F, -8.3301F, 0.0F, 1.5708F, 0.0F));

		ModelPartData monitor = coral.addChild("monitor", ModelPartBuilder.create(), ModelTransform.of(-6.4608F, -19.484F, -11.5705F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bb_main3_r2 = monitor.addChild("bb_main3_r2", ModelPartBuilder.create().uv(98, 56).cuboid(-4.5F, 1.5393F, 2.5872F, 9.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.158F, 0.1264F, -1.8898F, 1.5272F, -0.5236F, 0.0F));

		ModelPartData bb_main3_r3 = monitor.addChild("bb_main3_r3", ModelPartBuilder.create().uv(98, 42).cuboid(-4.5F, -1.5607F, -4.2378F, 9.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.158F, 0.1264F, -1.8898F, -1.5272F, 0.5236F, -3.1416F));

		ModelPartData bb_main3_r4 = monitor.addChild("bb_main3_r4", ModelPartBuilder.create().uv(20, 91).cuboid(-4.5F, -3.5F, -0.5F, 9.0F, 7.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.6304F, 0.0793F, -0.9758F, -3.098F, 0.5236F, -3.1416F));

		ModelPartData bb_main3_r5 = monitor.addChild("bb_main3_r5", ModelPartBuilder.create().uv(77, 73).cuboid(-0.275F, -21.676F, 5.9936F, 1.0F, 1.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(-7.3725F, 19.484F, 12.8715F, 3.098F, 0.5236F, -3.1416F));

		ModelPartData refuller = coral.addChild("refuller", ModelPartBuilder.create(), ModelTransform.pivot(18.0284F, -7.3756F, -7.5399F));

		ModelPartData cube_r92 = refuller.addChild("cube_r92", ModelPartBuilder.create().uv(7, 21).cuboid(-0.25F, -6.5F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(9, 47).cuboid(-0.25F, -4.5F, 0.0F, 1.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.4821F, 0.2129F, 1.187F));

		ModelPartData increment = coral.addChild("increment", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData ppweight_r1 = increment.addChild("ppweight_r1", ModelPartBuilder.create().uv(93, 61).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(12.0778F, -15.8131F, 0.0F, 0.0F, 1.5708F, 0.5672F));

		ModelPartData z = coral.addChild("z", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bb_main3_r6 = z.addChild("bb_main3_r6", ModelPartBuilder.create().uv(49, 48).cuboid(-4.775F, -20.425F, 4.3F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 2.618F, 0.5236F, -3.1416F));

		ModelPartData y = coral.addChild("y", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bb_main3_r7 = y.addChild("bb_main3_r7", ModelPartBuilder.create().uv(49, 48).cuboid(-0.775F, -20.425F, 4.3F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 2.618F, 0.5236F, -3.1416F));

		ModelPartData x = coral.addChild("x", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bb_main3_r8 = x.addChild("bb_main3_r8", ModelPartBuilder.create().uv(49, 48).cuboid(3.225F, -20.425F, 4.3F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 2.618F, 0.5236F, -3.1416F));

		ModelPartData randomiser = coral.addChild("randomiser", ModelPartBuilder.create(), ModelTransform.pivot(-13.307F, -17.5021F, 0.0F));

		ModelPartData randomiser_r1 = randomiser.addChild("randomiser_r1", ModelPartBuilder.create().uv(32, 73).cuboid(-6.8316F, 1.503F, -1.5F, 4.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.0F, -0.0436F, -0.5149F));

		ModelPartData communicator = coral.addChild("communicator", ModelPartBuilder.create(), ModelTransform.pivot(14.4968F, -16.9252F, -8.3697F));

		ModelPartData bb_main8_r1 = communicator.addChild("bb_main8_r1", ModelPartBuilder.create().uv(37, 111).cuboid(-4.25F, 1.35F, -5.5F, 4.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(0, 17).cuboid(-4.5F, 1.1F, -4.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, -3.1252F, 0.0405F, -2.7573F));

		ModelPartData door_control = coral.addChild("door_control", ModelPartBuilder.create(), ModelTransform.of(0.05F, -16.3071F, 20.8887F, 0.0F, 1.5708F, 0.0F));

		ModelPartData door_control_r1 = door_control.addChild("door_control_r1", ModelPartBuilder.create().uv(50, 77).cuboid(-9.9463F, -2.187F, -1.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(14, 88).cuboid(-7.9463F, -2.187F, -1.0F, 0.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(7.5817F, -1.195F, -0.05F, 0.0F, 0.0F, -0.4276F));

		ModelPartData handbrake = coral.addChild("handbrake", ModelPartBuilder.create(), ModelTransform.pivot(-17.4744F, -9.731F, -14.5842F));

		ModelPartData middle_ring7_r2 = handbrake.addChild("middle_ring7_r2", ModelPartBuilder.create().uv(2, 41).cuboid(-0.4447F, -0.6195F, -3.3922F, 1.0F, 1.0F, 3.0F, new Dilation(-0.1F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0436F, -0.7854F, 0.0F));

		ModelPartData throttle = coral.addChild("throttle", ModelPartBuilder.create().uv(5, 88).cuboid(-0.5F, -2.6566F, 0.1088F, 1.0F, 4.0F, 1.0F, new Dilation(-0.3F))
		.uv(5, 104).cuboid(-0.5F, -2.6566F, 0.1088F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(1.275F, -18.45F, -14.75F, -0.6545F, 0.0F, 0.0F));

		ModelPartData bone133 = coral.addChild("bone133", ModelPartBuilder.create(), ModelTransform.of(0.0F, -2.0F, 3.0F, -3.1416F, 1.0472F, -2.9234F));

		ModelPartData cube_r93 = bone133.addChild("cube_r93", ModelPartBuilder.create().uv(77, 73).cuboid(0.0F, -3.5F, -0.25F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-11.7165F, -17.4291F, 6.3239F, 0.5222F, 0.0058F, 1.3526F));

		ModelPartData silver_doohickey5 = coral.addChild("silver_doohickey5", ModelPartBuilder.create(), ModelTransform.pivot(-13.307F, -17.5021F, 0.0F));

		ModelPartData silver_doohickey2_r1 = silver_doohickey5.addChild("silver_doohickey2_r1", ModelPartBuilder.create().uv(56, 8).cuboid(-2.3386F, -19.6991F, 10.75F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(77, 79).cuboid(-1.5886F, -19.9491F, 10.75F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(13.307F, 18.5021F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData dial4 = silver_doohickey5.addChild("dial4", ModelPartBuilder.create(), ModelTransform.of(13.307F, 17.5021F, 0.0F, -0.6109F, 0.0F, 0.0F));

		ModelPartData silver_doohickey3_r1 = dial4.addChild("silver_doohickey3_r1", ModelPartBuilder.create().uv(49, 39).cuboid(-1.0886F, -23.9299F, 7.5736F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.3398F, 0.7887F, 0.7821F));

		ModelPartData silver_doohickey3_r2 = dial4.addChild("silver_doohickey3_r2", ModelPartBuilder.create().uv(49, 39).cuboid(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(15.2136F, -18.1729F, -2.9343F, 0.6016F, 0.7887F, 0.7821F));

		ModelPartData silver_doohickey4 = coral.addChild("silver_doohickey4", ModelPartBuilder.create(), ModelTransform.pivot(-13.307F, -17.5021F, 0.0F));

		ModelPartData silver_doohickey2_r2 = silver_doohickey4.addChild("silver_doohickey2_r2", ModelPartBuilder.create().uv(5, 39).cuboid(1.4114F, -19.2491F, 11.25F, 3.0F, 1.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 30).cuboid(-0.3386F, -19.2491F, 10.75F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(13.307F, 18.5021F, 0.0F, -3.1416F, 1.0472F, -3.098F));

		ModelPartData silver_doohickey3 = coral.addChild("silver_doohickey3", ModelPartBuilder.create(), ModelTransform.pivot(-13.307F, -17.5021F, 0.0F));

		ModelPartData silver_doohickey2_r3 = silver_doohickey3.addChild("silver_doohickey2_r3", ModelPartBuilder.create().uv(111, 83).cuboid(-1.5886F, -23.9991F, 7.25F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(13.307F, 18.5021F, 0.0F, 2.5744F, 0.0F, 3.1416F));

		ModelPartData dial2 = silver_doohickey3.addChild("dial2", ModelPartBuilder.create(), ModelTransform.of(13.307F, 17.5021F, 0.0F, -0.6109F, 0.0F, 0.0F));

		ModelPartData silver_doohickey3_r3 = dial2.addChild("silver_doohickey3_r3", ModelPartBuilder.create().uv(41, 94).cuboid(-1.0886F, -24.9299F, 9.5736F, 2.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 1.9199F, 0.0F, 3.1416F));

		ModelPartData bone132 = coral.addChild("bone132", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r94 = bone132.addChild("cube_r94", ModelPartBuilder.create().uv(5, 34).cuboid(-0.4F, -1.325F, -0.375F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-11.7165F, -17.4291F, 6.3239F, 0.5493F, -0.0052F, 1.5224F));

		ModelPartData bone131 = coral.addChild("bone131", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r95 = bone131.addChild("cube_r95", ModelPartBuilder.create().uv(5, 34).cuboid(-1.75F, 6.675F, -0.375F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-11.7165F, -17.4291F, 6.3239F, 0.4899F, 0.2594F, 1.0655F));

		ModelPartData bone130 = coral.addChild("bone130", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r96 = bone130.addChild("cube_r96", ModelPartBuilder.create().uv(7, 64).cuboid(-1.75F, 6.175F, -1.625F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-11.7165F, -17.4291F, 6.3239F, 0.4899F, 0.2594F, 1.0655F));

		ModelPartData bone129 = coral.addChild("bone129", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r97 = bone129.addChild("cube_r97", ModelPartBuilder.create().uv(0, 8).cuboid(0.0F, -1.0F, -2.25F, 0.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-13.6104F, -17.1386F, 8.5175F, 0.5236F, 0.1745F, 1.2217F));

		ModelPartData bone128 = coral.addChild("bone128", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r98 = bone128.addChild("cube_r98", ModelPartBuilder.create().uv(0, 64).cuboid(-0.75F, 2.175F, -0.625F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-11.7165F, -17.4291F, 6.3239F, 0.5236F, 0.1745F, 1.2217F));

		ModelPartData bone127 = coral.addChild("bone127", ModelPartBuilder.create(), ModelTransform.pivot(-11.7165F, -17.4291F, 6.3239F));

		ModelPartData cube_r99 = bone127.addChild("cube_r99", ModelPartBuilder.create().uv(0, 64).cuboid(-0.5F, -3.75F, -1.5F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.5236F, 0.0797F, 1.4747F));

		ModelPartData bone126 = coral.addChild("bone126", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r100 = bone126.addChild("cube_r100", ModelPartBuilder.create().uv(77, 73).cuboid(-0.5F, -2.0F, 1.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-11.7165F, -17.4291F, 6.3239F, 0.5236F, 0.0797F, 1.4747F));

		ModelPartData bone125 = coral.addChild("bone125", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r101 = bone125.addChild("cube_r101", ModelPartBuilder.create().uv(82, 73).cuboid(0.0F, -1.5F, 9.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-4.5359F, -13.5F, -15.8564F, -0.9599F, -0.4887F, 1.2566F));

		ModelPartData bone100 = coral.addChild("bone100", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bb_main3_r9 = bone100.addChild("bb_main3_r9", ModelPartBuilder.create().uv(41, 97).cuboid(-6.775F, -20.309F, 8.55F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 2.618F, 0.5236F, -3.1416F));

		ModelPartData bone99 = coral.addChild("bone99", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0036F, 0.0F));

		ModelPartData bb_main8_r2 = bone99.addChild("bb_main8_r2", ModelPartBuilder.create().uv(49, 14).cuboid(-2.1122F, 0.5446F, -0.7966F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(16.6978F, -13.5439F, -6.8697F, 3.1042F, 0.4076F, -2.6993F));

		ModelPartData bb_main8_r3 = bone99.addChild("bb_main8_r3", ModelPartBuilder.create().uv(92, 95).cuboid(-0.238F, 0.1146F, -0.2966F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(16.6978F, -13.5439F, -6.8697F, 2.8004F, 0.2306F, 2.6791F));

		ModelPartData fuckin_lever_thing2 = coral.addChild("fuckin_lever_thing2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData arm2_r1 = fuckin_lever_thing2.addChild("arm2_r1", ModelPartBuilder.create().uv(0, 26).cuboid(-0.2397F, -0.5044F, -1.4812F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(18.2068F, -14.8572F, -11.0674F, -2.8811F, 0.4595F, 0.5412F));

		ModelPartData arm2_r2 = fuckin_lever_thing2.addChild("arm2_r2", ModelPartBuilder.create().uv(25, 78).cuboid(-0.4897F, -0.5044F, -0.9812F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(18.2068F, -14.8572F, -11.0674F, 2.8218F, -0.422F, -2.4617F));

		ModelPartData arm2_r3 = fuckin_lever_thing2.addChild("arm2_r3", ModelPartBuilder.create().uv(49, 22).cuboid(-9.5F, -26.0F, -1.5F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(49, 27).cuboid(-10.0F, -26.5F, -1.75F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(55, 108).cuboid(-10.7252F, -24.5F, -1.4F, 5.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 2.8218F, -0.422F, -2.4617F));

		ModelPartData arm2_r4 = fuckin_lever_thing2.addChild("arm2_r4", ModelPartBuilder.create().uv(50, 73).cuboid(-1.25F, -0.25F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(17.6607F, -14.6034F, -12.0151F, 2.6516F, -0.1925F, -1.9152F));

		ModelPartData fuckin_lever_thing = coral.addChild("fuckin_lever_thing", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData arm2_r5 = fuckin_lever_thing.addChild("arm2_r5", ModelPartBuilder.create().uv(0, 26).cuboid(-0.2397F, -0.5044F, -1.4812F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(18.2068F, -14.8572F, -11.0674F, -2.8811F, 0.4595F, 0.5412F));

		ModelPartData arm2_r6 = fuckin_lever_thing.addChild("arm2_r6", ModelPartBuilder.create().uv(25, 78).cuboid(-0.4897F, -0.5044F, -0.9812F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(18.2068F, -14.8572F, -11.0674F, 2.8218F, -0.422F, -2.4617F));

		ModelPartData arm2_r7 = fuckin_lever_thing.addChild("arm2_r7", ModelPartBuilder.create().uv(49, 22).cuboid(-9.5F, -26.0F, -1.5F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(49, 27).cuboid(-10.0F, -26.5F, -1.75F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(55, 108).cuboid(-10.7252F, -24.5F, -1.4F, 5.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 2.8218F, -0.422F, -2.4617F));

		ModelPartData arm2_r8 = fuckin_lever_thing.addChild("arm2_r8", ModelPartBuilder.create().uv(50, 73).cuboid(-1.25F, -0.25F, 0.0F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(17.6607F, -14.6034F, -12.0151F, 2.6516F, -0.1925F, -1.9152F));

		ModelPartData purple_glow = coral.addChild("purple_glow", ModelPartBuilder.create(), ModelTransform.pivot(14.4968F, -16.9252F, -8.3697F));

		ModelPartData bb_main8_r4 = purple_glow.addChild("bb_main8_r4", ModelPartBuilder.create().uv(49, 18).cuboid(-1.1718F, 1.5252F, -7.8803F, 3.0F, 0.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 3.1416F, 0.0F, -2.6529F));

		ModelPartData nuther_blue_glow = coral.addChild("nuther_blue_glow", ModelPartBuilder.create(), ModelTransform.pivot(14.4968F, -16.9252F, -8.3697F));

		ModelPartData bb_main8_r5 = nuther_blue_glow.addChild("bb_main8_r5", ModelPartBuilder.create().uv(0, 21).cuboid(0.8282F, 1.5252F, -10.8803F, 1.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 3.1416F, 0.0F, -2.6529F));

		ModelPartData bone43 = coral.addChild("bone43", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

		ModelPartData bb_main3_r10 = bone43.addChild("bb_main3_r10", ModelPartBuilder.create().uv(0, 34).cuboid(-9.0F, -20.175F, -0.975F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 2.618F, 0.5236F, -3.1416F));

		ModelPartData bb_main8_r6 = bone43.addChild("bb_main8_r6", ModelPartBuilder.create().uv(92, 95).cuboid(0.0F, 0.0F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(16.85F, -14.4282F, 5.1303F, 3.1416F, 0.0F, 2.7576F));

		ModelPartData bb_main8_r7 = bone43.addChild("bb_main8_r7", ModelPartBuilder.create().uv(49, 14).cuboid(-5.25F, 1.6F, -14.5F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(14.4968F, -16.9252F, -8.3697F, 3.1416F, 0.0F, -2.6529F));

		ModelPartData bb_main4_r1 = bone43.addChild("bb_main4_r1", ModelPartBuilder.create().uv(0, 47).cuboid(-9.5F, -20.25F, 0.05F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.5236F));

		ModelPartData bb_main8_r8 = bone43.addChild("bb_main8_r8", ModelPartBuilder.create().uv(49, 43).cuboid(6.75F, -1.275F, 1.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(49, 56).cuboid(9.75F, -1.275F, 1.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -12.45F, 18.75F, 0.3054F, 0.5236F, 0.0F));

		ModelPartData orange_glow = coral.addChild("orange_glow", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, -12.45F, 18.75F));

		ModelPartData bb_main8_r9 = orange_glow.addChild("bb_main8_r9", ModelPartBuilder.create().uv(49, 67).cuboid(6.25F, -1.675F, -1.2F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, -0.5236F, 0.5236F, 0.0F));

		ModelPartData blue_glow = coral.addChild("blue_glow", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, -12.45F, 18.75F));

		ModelPartData bb_main8_r10 = blue_glow.addChild("bb_main8_r10", ModelPartBuilder.create().uv(0, 69).cuboid(9.75F, -1.675F, -1.2F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, -0.5236F, 0.5236F, 0.0F));

		ModelPartData clutter2 = coral.addChild("clutter2", ModelPartBuilder.create(), ModelTransform.pivot(-9.5986F, -14.6924F, 12.4127F));

		ModelPartData moving_two3_r1 = clutter2.addChild("moving_two3_r1", ModelPartBuilder.create().uv(98, 22).cuboid(-1.75F, -0.75F, -0.75F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.738F, 1.2045F, -1.6577F, -1.4524F, 0.3784F, -0.9392F));

		ModelPartData clutter = coral.addChild("clutter", ModelPartBuilder.create(), ModelTransform.pivot(-9.5986F, -14.6924F, 12.4127F));

		ModelPartData moving_two2_r1 = clutter.addChild("moving_two2_r1", ModelPartBuilder.create().uv(98, 22).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.738F, 1.2045F, -1.6577F, -1.627F, 0.3784F, -0.9392F));

		ModelPartData moving_two = coral.addChild("moving_two", ModelPartBuilder.create(), ModelTransform.pivot(-9.5986F, -14.6924F, 12.4127F));

		ModelPartData moving_two_r1 = moving_two.addChild("moving_two_r1", ModelPartBuilder.create().uv(98, 22).cuboid(-1.7F, -0.375F, -0.325F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, -1.2654F, -0.5236F, 0.0F));

		ModelPartData moving_one = coral.addChild("moving_one", ModelPartBuilder.create(), ModelTransform.pivot(-9.5986F, -14.6924F, 12.4127F));

		ModelPartData moving_one_r1 = moving_one.addChild("moving_one_r1", ModelPartBuilder.create().uv(98, 22).cuboid(-0.2F, -0.375F, -0.325F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, -1.2654F, -0.5236F, 0.0F));

		ModelPartData bone98 = coral.addChild("bone98", ModelPartBuilder.create(), ModelTransform.pivot(-7.5697F, -16.0238F, 12.8985F));

		ModelPartData bone98_r1 = bone98.addChild("bone98_r1", ModelPartBuilder.create().uv(99, 0).cuboid(0.0F, 0.25F, -1.25F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(49, 52).cuboid(-0.5F, 0.5F, -1.75F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, -0.6125F, -0.5417F, -0.0562F));

		ModelPartData ppweight2 = coral.addChild("ppweight2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData ppweight_r2 = ppweight2.addChild("ppweight_r2", ModelPartBuilder.create().uv(100, 66).cuboid(-0.3882F, 1.0213F, 0.6121F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.6118F, -16.9022F, 11.9615F, -0.6109F, -0.6109F, 0.0F));

		ModelPartData ppweight = coral.addChild("ppweight", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData ppweight_r3 = ppweight.addChild("ppweight_r3", ModelPartBuilder.create().uv(71, 101).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-12.25F, -12.5F, 13.75F, -0.5236F, -0.1745F, -0.2967F));

		ModelPartData bone97 = coral.addChild("bone97", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData bone97_r1 = bone97.addChild("bone97_r1", ModelPartBuilder.create().uv(49, 9).cuboid(-8.5F, 0.766F, -10.5F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -12.45F, 18.75F, -0.5236F, 0.0F, 0.0F));

		ModelPartData silver_doohickey = coral.addChild("silver_doohickey", ModelPartBuilder.create(), ModelTransform.pivot(-13.307F, -17.5021F, 0.0F));

		ModelPartData silver_doohickey_r1 = silver_doohickey.addChild("silver_doohickey_r1", ModelPartBuilder.create().uv(111, 83).cuboid(-1.1316F, 1.303F, -1.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		ModelPartData silver_doohickey2 = silver_doohickey.addChild("silver_doohickey2", ModelPartBuilder.create().uv(111, 83).cuboid(11.7184F, -0.247F, 9.75F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData dial = silver_doohickey2.addChild("dial", ModelPartBuilder.create().uv(0, 53).cuboid(-0.3386F, -23.9299F, 9.5736F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(13.307F, 17.5021F, 0.0F, -0.6109F, 0.0F, 0.0F));

		ModelPartData rheostat = coral.addChild("rheostat", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData rheostat_r1 = rheostat.addChild("rheostat_r1", ModelPartBuilder.create().uv(29, 100).cuboid(-7.7463F, -1.337F, -1.0F, 6.0F, 1.0F, 2.0F, new Dilation(-0.1F))
		.uv(107, 109).cuboid(-7.7463F, -0.337F, -1.0F, 6.0F, 1.0F, 2.0F, new Dilation(0.1F)), ModelTransform.of(-13.307F, -17.5021F, 0.0F, 0.0F, 0.0F, -0.4276F));

		ModelPartData under_console_cover = coral.addChild("under_console_cover", ModelPartBuilder.create(), ModelTransform.of(0.5F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData under_console_cover_r1 = under_console_cover.addChild("under_console_cover_r1", ModelPartBuilder.create().uv(0, 78).cuboid(-5.0F, -6.0F, -15.0F, 10.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData under_console_cover2 = under_console_cover.addChild("under_console_cover2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData under_console_cover2_r1 = under_console_cover2.addChild("under_console_cover2_r1", ModelPartBuilder.create().uv(0, 78).cuboid(-4.625F, -6.0F, -14.625F, 10.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData under_console_cover3 = under_console_cover2.addChild("under_console_cover3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData under_console_cover3_r1 = under_console_cover3.addChild("under_console_cover3_r1", ModelPartBuilder.create().uv(0, 78).cuboid(-4.675F, -6.0F, -14.275F, 10.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData under_console_cover4 = under_console_cover3.addChild("under_console_cover4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData under_console_cover4_r1 = under_console_cover4.addChild("under_console_cover4_r1", ModelPartBuilder.create().uv(0, 78).cuboid(-5.0F, -6.0F, -13.95F, 10.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData under_console_cover5 = under_console_cover4.addChild("under_console_cover5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData under_console_cover5_r1 = under_console_cover5.addChild("under_console_cover5_r1", ModelPartBuilder.create().uv(0, 78).cuboid(-5.375F, -6.0F, -14.25F, 10.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData under_console_cover6 = under_console_cover5.addChild("under_console_cover6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData under_console_cover6_r1 = under_console_cover6.addChild("under_console_cover6_r1", ModelPartBuilder.create().uv(0, 78).cuboid(-5.25F, -6.0F, -13.9F, 10.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData pipes_one = coral.addChild("pipes_one", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData pipes_one_r1 = pipes_one.addChild("pipes_one_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-7.25F, -10.0627F, 0.4244F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -1.0F, -10.0F, 0.7418F, 0.5236F, 0.0F));

		ModelPartData pipes_one2 = pipes_one.addChild("pipes_one2", ModelPartBuilder.create(), ModelTransform.pivot(-8.0F, -1.0F, -10.0F));

		ModelPartData pipes_one2_r1 = pipes_one2.addChild("pipes_one2_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-9.0F, 0.8827F, -2.0239F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0207F, -8.6441F, -5.2679F, 1.1781F, 0.5236F, 0.0F));

		ModelPartData pipes_one3 = pipes_one2.addChild("pipes_one3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData pipes_one3_r1 = pipes_one3.addChild("pipes_one3_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-9.0F, 2.8928F, -0.866F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0207F, -8.6441F, -5.2679F, 0.8727F, 0.5236F, 0.0F));

		ModelPartData pipes_one4 = pipes_one.addChild("pipes_one4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData pipes_one4_r1 = pipes_one4.addChild("pipes_one4_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-7.25F, -10.0627F, 0.4244F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -1.0F, -10.0F, 0.7418F, 0.5236F, 0.0F));

		ModelPartData pipes_one5 = pipes_one4.addChild("pipes_one5", ModelPartBuilder.create(), ModelTransform.pivot(-8.0F, -1.0F, -10.0F));

		ModelPartData pipes_one5_r1 = pipes_one5.addChild("pipes_one5_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-9.0F, 0.8827F, -2.0239F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0207F, -8.6441F, -5.2679F, 1.1781F, 0.5236F, 0.0F));

		ModelPartData pipes_one6 = pipes_one5.addChild("pipes_one6", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData pipes_one6_r1 = pipes_one6.addChild("pipes_one6_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-9.0F, 2.8928F, -0.866F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0207F, -8.6441F, -5.2679F, 0.8727F, 0.5236F, 0.0F));

		ModelPartData pipes_one7 = pipes_one4.addChild("pipes_one7", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData pipes_one7_r1 = pipes_one7.addChild("pipes_one7_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-7.25F, -10.0627F, 0.4244F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -1.0F, -10.0F, 0.7418F, 0.5236F, 0.0F));

		ModelPartData pipes_one8 = pipes_one7.addChild("pipes_one8", ModelPartBuilder.create(), ModelTransform.pivot(-8.0F, -1.0F, -10.0F));

		ModelPartData pipes_one8_r1 = pipes_one8.addChild("pipes_one8_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-9.0F, 0.8827F, -2.0239F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0207F, -8.6441F, -5.2679F, 1.1781F, 0.5236F, 0.0F));

		ModelPartData pipes_one9 = pipes_one8.addChild("pipes_one9", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData pipes_one9_r1 = pipes_one9.addChild("pipes_one9_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-9.0F, 2.8928F, -0.866F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0207F, -8.6441F, -5.2679F, 0.8727F, 0.5236F, 0.0F));

		ModelPartData pipes_one10 = pipes_one7.addChild("pipes_one10", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData pipes_one10_r1 = pipes_one10.addChild("pipes_one10_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-7.25F, -10.0627F, 0.4244F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -1.0F, -10.0F, 0.7418F, 0.5236F, 0.0F));

		ModelPartData pipes_one11 = pipes_one10.addChild("pipes_one11", ModelPartBuilder.create(), ModelTransform.pivot(-8.0F, -1.0F, -10.0F));

		ModelPartData pipes_one11_r1 = pipes_one11.addChild("pipes_one11_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-9.0F, 0.8827F, -2.0239F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0207F, -8.6441F, -5.2679F, 1.1781F, 0.5236F, 0.0F));

		ModelPartData pipes_one12 = pipes_one11.addChild("pipes_one12", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData pipes_one12_r1 = pipes_one12.addChild("pipes_one12_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-9.0F, 2.8928F, -0.866F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0207F, -8.6441F, -5.2679F, 0.8727F, 0.5236F, 0.0F));

		ModelPartData pipes_one13 = pipes_one10.addChild("pipes_one13", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData pipes_one13_r1 = pipes_one13.addChild("pipes_one13_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-7.25F, -10.0627F, 0.4244F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -1.0F, -10.0F, 0.7418F, 0.5236F, 0.0F));

		ModelPartData pipes_one14 = pipes_one13.addChild("pipes_one14", ModelPartBuilder.create(), ModelTransform.pivot(-8.0F, -1.0F, -10.0F));

		ModelPartData pipes_one14_r1 = pipes_one14.addChild("pipes_one14_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-9.0F, 0.8827F, -2.0239F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0207F, -8.6441F, -5.2679F, 1.1781F, 0.5236F, 0.0F));

		ModelPartData pipes_one15 = pipes_one14.addChild("pipes_one15", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData pipes_one15_r1 = pipes_one15.addChild("pipes_one15_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-9.0F, 2.8928F, -0.866F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0207F, -8.6441F, -5.2679F, 0.8727F, 0.5236F, 0.0F));

		ModelPartData pipes_one16 = pipes_one13.addChild("pipes_one16", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData pipes_one16_r1 = pipes_one16.addChild("pipes_one16_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-7.25F, -10.0627F, 0.4244F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -1.0F, -10.0F, 0.7418F, 0.5236F, 0.0F));

		ModelPartData pipes_one17 = pipes_one16.addChild("pipes_one17", ModelPartBuilder.create(), ModelTransform.pivot(-8.0F, -1.0F, -10.0F));

		ModelPartData pipes_one17_r1 = pipes_one17.addChild("pipes_one17_r1", ModelPartBuilder.create().uv(0, 73).cuboid(-9.0F, 0.8827F, -2.0239F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0207F, -8.6441F, -5.2679F, 1.1781F, 0.5236F, 0.0F));

		ModelPartData pipes_one18 = pipes_one17.addChild("pipes_one18", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData pipes_one18_r1 = pipes_one18.addChild("pipes_one18_r1", ModelPartBuilder.create().uv(61, 67).cuboid(-9.0F, 2.8928F, -0.866F, 18.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.0207F, -8.6441F, -5.2679F, 0.8727F, 0.5236F, 0.0F));

		ModelPartData throttle_casing = coral.addChild("throttle_casing", ModelPartBuilder.create(), ModelTransform.of(0.4F, 0.25F, 0.0F, -0.0436F, -0.5236F, 0.0F));

		ModelPartData throttle_casing_r1 = throttle_casing.addChild("throttle_casing_r1", ModelPartBuilder.create().uv(107, 92).cuboid(-0.5131F, 0.3242F, -0.543F, 5.0F, 2.0F, 3.0F, new Dilation(-0.5F)), ModelTransform.of(-8.1386F, -17.4991F, -16.0F, 0.5498F, -0.9599F, -0.6545F));

		ModelPartData throttle_casing_r2 = throttle_casing.addChild("throttle_casing_r2", ModelPartBuilder.create().uv(5, 108).cuboid(-0.4957F, 0.205F, -0.5509F, 5.0F, 2.0F, 3.0F, new Dilation(-0.4F)), ModelTransform.of(-8.1386F, -17.4991F, -16.0F, 0.5236F, -0.9599F, -0.6168F));

		ModelPartData bone = coral.addChild("bone", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone_r1 = bone.addChild("bone_r1", ModelPartBuilder.create().uv(98, 15).cuboid(-3.6F, -18.3288F, -8.5122F, 7.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		ModelPartData bone2 = bone.addChild("bone2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone2_r1 = bone2.addChild("bone2_r1", ModelPartBuilder.create().uv(98, 15).cuboid(-3.6F, -18.3288F, -8.5122F, 7.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		ModelPartData bone3 = bone2.addChild("bone3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone3_r1 = bone3.addChild("bone3_r1", ModelPartBuilder.create().uv(98, 15).cuboid(-3.6F, -18.3288F, -8.5122F, 7.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		ModelPartData bone4 = bone3.addChild("bone4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone4_r1 = bone4.addChild("bone4_r1", ModelPartBuilder.create().uv(98, 15).cuboid(-3.6F, -18.3288F, -8.5122F, 7.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		ModelPartData bone5 = bone4.addChild("bone5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone5_r1 = bone5.addChild("bone5_r1", ModelPartBuilder.create().uv(98, 15).cuboid(-3.6F, -18.3288F, -8.5122F, 7.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		ModelPartData bone6 = bone5.addChild("bone6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone6_r1 = bone6.addChild("bone6_r1", ModelPartBuilder.create().uv(98, 15).cuboid(-3.6F, -18.3288F, -8.5122F, 7.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

		ModelPartData arm8 = coral.addChild("arm8", ModelPartBuilder.create(), ModelTransform.of(0.0F, -23.0F, 0.0F, 0.0F, -1.5708F, 3.1416F));

		ModelPartData arm8_r1 = arm8.addChild("arm8_r1", ModelPartBuilder.create().uv(98, 8).cuboid(0.461F, -0.5297F, -2.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-23.6126F, -13.2895F, 0.0F, 0.0F, 0.0F, -0.6109F));

		ModelPartData arm8_r2 = arm8.addChild("arm8_r2", ModelPartBuilder.create().uv(98, 35).cuboid(-4.957F, -1.9242F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-13.307F, -17.5021F, 0.0F, 0.0F, 0.0F, -0.384F));

		ModelPartData arm8_r3 = arm8.addChild("arm8_r3", ModelPartBuilder.create().uv(21, 84).cuboid(-1.1997F, 0.4725F, -2.0F, 9.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-14.0055F, -18.3713F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData arm9 = arm8.addChild("arm9", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData arm9_r1 = arm9.addChild("arm9_r1", ModelPartBuilder.create().uv(98, 8).cuboid(0.461F, -0.5297F, -2.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-23.6126F, -13.2895F, 0.0F, 0.0F, 0.0F, -0.6109F));

		ModelPartData arm9_r2 = arm9.addChild("arm9_r2", ModelPartBuilder.create().uv(98, 35).cuboid(-4.957F, -1.9242F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-13.307F, -17.5021F, 0.0F, 0.0F, 0.0F, -0.384F));

		ModelPartData arm9_r3 = arm9.addChild("arm9_r3", ModelPartBuilder.create().uv(21, 84).cuboid(-0.1432F, -0.2646F, -2.0F, 9.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-12.8304F, -17.8266F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData arm10 = arm9.addChild("arm10", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData arm10_r1 = arm10.addChild("arm10_r1", ModelPartBuilder.create().uv(98, 8).cuboid(0.461F, -0.5297F, -2.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-23.6126F, -13.2895F, 0.0F, 0.0F, 0.0F, -0.6109F));

		ModelPartData arm10_r2 = arm10.addChild("arm10_r2", ModelPartBuilder.create().uv(98, 35).cuboid(0.0535F, -0.9783F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-18.307F, -16.5021F, 0.0F, 0.0F, 0.0F, -0.384F));

		ModelPartData arm10_r3 = arm10.addChild("arm10_r3", ModelPartBuilder.create().uv(21, 84).cuboid(-12.8774F, -20.0133F, -2.0F, 9.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-2.0946F, -38.729F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData arm11 = arm10.addChild("arm11", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData arm11_r1 = arm11.addChild("arm11_r1", ModelPartBuilder.create().uv(98, 8).cuboid(0.461F, -0.5297F, -2.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-23.6126F, -13.2895F, 0.0F, 0.0F, 0.0F, -0.6109F));

		ModelPartData arm11_r2 = arm11.addChild("arm11_r2", ModelPartBuilder.create().uv(98, 35).cuboid(-4.957F, -1.9242F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-13.307F, -17.5021F, 0.0F, 0.0F, 0.0F, -0.384F));

		ModelPartData arm11_r3 = arm11.addChild("arm11_r3", ModelPartBuilder.create().uv(21, 84).cuboid(-12.8774F, -20.0133F, -2.0F, 9.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-2.0946F, -38.729F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData arm12 = arm11.addChild("arm12", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData arm12_r1 = arm12.addChild("arm12_r1", ModelPartBuilder.create().uv(98, 8).cuboid(0.461F, -0.5297F, -2.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-23.6126F, -13.2895F, 0.0F, 0.0F, 0.0F, -0.6109F));

		ModelPartData arm12_r2 = arm12.addChild("arm12_r2", ModelPartBuilder.create().uv(98, 35).cuboid(-4.957F, -1.9242F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-13.307F, -17.5021F, 0.0F, 0.0F, 0.0F, -0.384F));

		ModelPartData arm12_r3 = arm12.addChild("arm12_r3", ModelPartBuilder.create().uv(21, 84).cuboid(-12.8774F, -20.0133F, -2.0F, 9.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-2.0946F, -38.729F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData arm17 = arm12.addChild("arm17", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData arm17_r1 = arm17.addChild("arm17_r1", ModelPartBuilder.create().uv(98, 8).cuboid(0.461F, -0.5297F, -2.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-23.6126F, -13.2895F, 0.0F, 0.0F, 0.0F, -0.6109F));

		ModelPartData arm17_r2 = arm17.addChild("arm17_r2", ModelPartBuilder.create().uv(98, 35).cuboid(-4.957F, -1.9242F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-13.307F, -17.5021F, 0.0F, 0.0F, 0.0F, -0.384F));

		ModelPartData arm17_r3 = arm17.addChild("arm17_r3", ModelPartBuilder.create().uv(21, 84).cuboid(-12.8774F, -20.0133F, -2.0F, 9.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-2.0946F, -38.729F, 0.0F, 0.0F, 0.0F, -2.0944F));

		ModelPartData arm6 = coral.addChild("arm6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData arm6_r1 = arm6.addChild("arm6_r1", ModelPartBuilder.create().uv(98, 8).cuboid(-0.6862F, 1.1086F, -2.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-23.6126F, -13.2895F, 0.0F, 0.0F, 0.0F, -0.6109F));

		ModelPartData arm6_r2 = arm6.addChild("arm6_r2", ModelPartBuilder.create().uv(98, 35).cuboid(-5.7062F, -0.0698F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-13.307F, -17.5021F, 0.0F, 0.0F, 0.0F, -0.384F));

		ModelPartData arm6_r3 = arm6.addChild("arm6_r3", ModelPartBuilder.create().uv(92, 85).cuboid(-11.8219F, -19.5164F, -2.0F, 7.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-0.6713F, 1.2264F, 0.0F, 0.0F, 0.0F, -0.0785F));

		ModelPartData arm2 = arm6.addChild("arm2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData arm2_r9 = arm2.addChild("arm2_r9", ModelPartBuilder.create().uv(98, 8).mirrored().cuboid(-0.6862F, 1.1086F, -2.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-23.6126F, -13.2895F, 0.0F, 0.0F, 0.0F, -0.6109F));

		ModelPartData arm2_r10 = arm2.addChild("arm2_r10", ModelPartBuilder.create().uv(98, 35).mirrored().cuboid(-5.7062F, -0.0698F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-13.307F, -17.5021F, 0.0F, 0.0F, 0.0F, -0.384F));

		ModelPartData arm2_r11 = arm2.addChild("arm2_r11", ModelPartBuilder.create().uv(92, 85).mirrored().cuboid(-11.8219F, -19.5164F, -2.0F, 7.0F, 2.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.6713F, 1.2264F, 0.0F, 0.0F, 0.0F, -0.0785F));

		ModelPartData arm3 = arm2.addChild("arm3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData arm3_r1 = arm3.addChild("arm3_r1", ModelPartBuilder.create().uv(98, 8).cuboid(-0.6862F, 1.1086F, -2.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-23.6126F, -13.2895F, 0.0F, 0.0F, 0.0F, -0.6109F));

		ModelPartData arm3_r2 = arm3.addChild("arm3_r2", ModelPartBuilder.create().uv(98, 35).cuboid(-5.7062F, -0.0698F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-13.307F, -17.5021F, 0.0F, 0.0F, 0.0F, -0.384F));

		ModelPartData arm3_r3 = arm3.addChild("arm3_r3", ModelPartBuilder.create().uv(92, 85).cuboid(-11.8219F, -19.5164F, -2.0F, 7.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-0.6713F, 1.2264F, 0.0F, 0.0F, 0.0F, -0.0785F));

		ModelPartData arm4 = arm3.addChild("arm4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData arm4_r1 = arm4.addChild("arm4_r1", ModelPartBuilder.create().uv(98, 8).mirrored().cuboid(-0.6862F, 1.1086F, -2.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-23.6126F, -13.2895F, 0.0F, 0.0F, 0.0F, -0.6109F));

		ModelPartData arm4_r2 = arm4.addChild("arm4_r2", ModelPartBuilder.create().uv(98, 35).mirrored().cuboid(-5.7062F, -0.0698F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-13.307F, -17.5021F, 0.0F, 0.0F, 0.0F, -0.384F));

		ModelPartData arm4_r3 = arm4.addChild("arm4_r3", ModelPartBuilder.create().uv(92, 85).mirrored().cuboid(-11.8219F, -19.5164F, -2.0F, 7.0F, 2.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.6713F, 1.2264F, 0.0F, 0.0F, 0.0F, -0.0785F));

		ModelPartData arm5 = arm4.addChild("arm5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData arm5_r1 = arm5.addChild("arm5_r1", ModelPartBuilder.create().uv(98, 8).cuboid(-0.6862F, 1.1086F, -2.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-23.6126F, -13.2895F, 0.0F, 0.0F, 0.0F, -0.6109F));

		ModelPartData arm5_r2 = arm5.addChild("arm5_r2", ModelPartBuilder.create().uv(98, 35).cuboid(-5.7062F, -0.0698F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-13.307F, -17.5021F, 0.0F, 0.0F, 0.0F, -0.384F));

		ModelPartData arm5_r3 = arm5.addChild("arm5_r3", ModelPartBuilder.create().uv(92, 85).cuboid(-11.8219F, -19.5164F, -2.0F, 7.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-0.6713F, 1.2264F, 0.0F, 0.0F, 0.0F, -0.0785F));

		ModelPartData arm7 = arm5.addChild("arm7", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData arm7_r1 = arm7.addChild("arm7_r1", ModelPartBuilder.create().uv(98, 8).mirrored().cuboid(-0.6862F, 1.1086F, -2.0F, 6.0F, 2.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-23.6126F, -13.2895F, 0.0F, 0.0F, 0.0F, -0.6109F));

		ModelPartData arm7_r2 = arm7.addChild("arm7_r2", ModelPartBuilder.create().uv(98, 35).mirrored().cuboid(-5.7062F, -0.0698F, -2.0F, 5.0F, 2.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-13.307F, -17.5021F, 0.0F, 0.0F, 0.0F, -0.384F));

		ModelPartData arm7_r3 = arm7.addChild("arm7_r3", ModelPartBuilder.create().uv(92, 85).mirrored().cuboid(-11.8219F, -19.5164F, -2.0F, 7.0F, 2.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(-0.6713F, 1.2264F, 0.0F, 0.0F, 0.0F, -0.0785F));

		ModelPartData crackled_rim = coral.addChild("crackled_rim", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData crackled_rim_r1 = crackled_rim.addChild("crackled_rim_r1", ModelPartBuilder.create().uv(49, 61).cuboid(-9.6F, -1.25F, -0.4165F, 20.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.4F, -11.2832F, -19.1674F, -2.0944F, 0.0F, 0.0F));

		ModelPartData crackled_rim2 = crackled_rim.addChild("crackled_rim2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData crackled_rim2_r1 = crackled_rim2.addChild("crackled_rim2_r1", ModelPartBuilder.create().uv(49, 61).cuboid(-9.6F, -1.25F, -0.4165F, 20.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.4F, -11.2832F, -19.1674F, -2.0944F, 0.0F, 0.0F));

		ModelPartData crackled_rim3 = crackled_rim2.addChild("crackled_rim3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData crackled_rim3_r1 = crackled_rim3.addChild("crackled_rim3_r1", ModelPartBuilder.create().uv(49, 61).cuboid(-9.6F, -1.25F, -0.4165F, 20.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.4F, -11.2832F, -19.1674F, -2.0944F, 0.0F, 0.0F));

		ModelPartData crackled_rim4 = crackled_rim3.addChild("crackled_rim4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData crackled_rim4_r1 = crackled_rim4.addChild("crackled_rim4_r1", ModelPartBuilder.create().uv(49, 61).cuboid(-9.6F, -1.25F, -0.4165F, 20.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.4F, -11.2832F, -19.1674F, -2.0944F, 0.0F, 0.0F));

		ModelPartData crackled_rim5 = crackled_rim4.addChild("crackled_rim5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData crackled_rim5_r1 = crackled_rim5.addChild("crackled_rim5_r1", ModelPartBuilder.create().uv(49, 61).cuboid(-9.6F, -1.25F, -0.4165F, 20.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.4F, -11.2832F, -19.1674F, -2.0944F, 0.0F, 0.0F));

		ModelPartData crackled_rim6 = crackled_rim5.addChild("crackled_rim6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData crackled_rim6_r1 = crackled_rim6.addChild("crackled_rim6_r1", ModelPartBuilder.create().uv(49, 61).cuboid(-9.6F, -1.25F, -0.4165F, 20.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.4F, -11.2832F, -19.1674F, -2.0944F, 0.0F, 0.0F));

		ModelPartData arm13 = coral.addChild("arm13", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData arm14 = arm13.addChild("arm14", ModelPartBuilder.create(), ModelTransform.pivot(-14.025F, -17.325F, 0.0F));

		ModelPartData middle_ring6 = arm14.addChild("middle_ring6", ModelPartBuilder.create(), ModelTransform.pivot(13.025F, 17.325F, 0.0F));

		ModelPartData arm15 = middle_ring6.addChild("arm15", ModelPartBuilder.create(), ModelTransform.pivot(14.525F, -17.325F, 0.0F));

		ModelPartData arm16 = arm15.addChild("arm16", ModelPartBuilder.create(), ModelTransform.pivot(-28.05F, 0.0F, 0.0F));

		ModelPartData middle_ring7 = middle_ring6.addChild("middle_ring7", ModelPartBuilder.create(), ModelTransform.of(1.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 3.1416F));

		ModelPartData middle_ring7_r3 = middle_ring7.addChild("middle_ring7_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-23.5F, 9.0F, -2.0F, 47.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData middle_ring2 = middle_ring7.addChild("middle_ring2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 3.1416F));

		ModelPartData middle_ring2_r1 = middle_ring2.addChild("middle_ring2_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-23.5F, -12.0F, -2.0F, 47.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData middle_ring3 = middle_ring2.addChild("middle_ring3", ModelPartBuilder.create().uv(0, 0).cuboid(-23.5F, 11.0F, -2.0F, 47.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -23.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bb_main = coral.addChild("bb_main", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData bb_main_r1 = bb_main.addChild("bb_main_r1", ModelPartBuilder.create().uv(49, 22).cuboid(-8.5F, 0.916F, -10.5F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F))
		.uv(49, 35).cuboid(-8.5F, 0.866F, -10.5F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -12.45F, 18.75F, -0.5236F, 0.0F, 0.0F));

		ModelPartData bb_main2 = bb_main.addChild("bb_main2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bb_main2_r2 = bb_main2.addChild("bb_main2_r2", ModelPartBuilder.create().uv(49, 22).cuboid(-8.5F, 0.866F, -11.5F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -12.45F, 18.75F, -0.5236F, 0.0F, 0.0F));

		ModelPartData bb_main3 = bb_main2.addChild("bb_main3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bb_main3_r11 = bb_main3.addChild("bb_main3_r11", ModelPartBuilder.create().uv(49, 48).cuboid(-8.5F, 0.866F, -10.5F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -12.45F, 18.75F, -0.5236F, 0.0F, 0.0F));

		ModelPartData bb_main4 = bb_main3.addChild("bb_main4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bb_main4_r2 = bb_main4.addChild("bb_main4_r2", ModelPartBuilder.create().uv(49, 22).cuboid(-8.5F, 0.991F, -10.5F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F))
		.uv(49, 35).cuboid(-8.5F, 0.866F, -10.5F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -12.45F, 18.75F, -0.5236F, 0.0F, 0.0F));

		ModelPartData bb_main5 = bb_main4.addChild("bb_main5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bb_main5_r1 = bb_main5.addChild("bb_main5_r1", ModelPartBuilder.create().uv(49, 22).cuboid(-8.5F, 0.866F, -10.5F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -12.45F, 18.75F, -0.5236F, 0.0F, 0.0F));

		ModelPartData bb_main6 = bb_main5.addChild("bb_main6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bb_main6_r1 = bb_main6.addChild("bb_main6_r1", ModelPartBuilder.create().uv(0, 8).cuboid(-8.5F, 0.766F, -10.5F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F))
		.uv(49, 22).cuboid(-8.5F, 0.866F, -10.5F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -12.45F, 18.75F, -0.5236F, 0.0F, 0.0F));

		ModelPartData bb_main7 = coral.addChild("bb_main7", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bb_main7_r1 = bb_main7.addChild("bb_main7_r1", ModelPartBuilder.create().uv(111, 46).cuboid(-2.75F, -0.1F, -2.2F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-16.3386F, -14.4991F, -5.0F, 0.0219F, 0.3152F, -0.4606F));

		ModelPartData bb_main8 = coral.addChild("bb_main8", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bb_main8_r11 = bb_main8.addChild("bb_main8_r11", ModelPartBuilder.create().uv(90, 73).cuboid(5.75F, -1.625F, -1.95F, 8.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 60).cuboid(1.5F, -1.425F, -9.4F, 18.0F, 0.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -11.45F, 18.75F, -0.5236F, 0.5236F, 0.0F));

		ModelPartData bb_main8_r12 = bb_main8.addChild("bb_main8_r12", ModelPartBuilder.create().uv(0, 60).cuboid(-0.25F, -0.9F, -1.5F, 2.0F, 1.0F, 2.0F, new Dilation(-0.3F)), ModelTransform.of(14.4968F, -15.9252F, -8.3697F, 2.9286F, -0.482F, -2.7051F));

		ModelPartData bb_main8_r13 = bb_main8.addChild("bb_main8_r13", ModelPartBuilder.create().uv(82, 79).cuboid(0.25F, -1.9F, -1.0F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(14.4968F, -15.9252F, -8.3697F, 2.9286F, -0.482F, -2.7051F));
		return TexturedModelData.of(modelData, 256, 256);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

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
	public Animation getAnimationForState(TardisTravel.State state) {
		return switch (state) {
			default -> HartnellAnimations.ROTOR;
			case LANDED -> EMPTY_ANIM;
		};
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		coral.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void renderWithAnimations(ConsoleBlockEntity console, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		if (console.getTardis() == null) return;
		matrices.push();
		matrices.translate(0.5f, -1.5f, -0.5f);
		// matrices.scale(0.5f, 0.5f, 0.5f);
		super.renderWithAnimations(console, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);
		matrices.pop();
	}

	@Override
	public ModelPart getPart() {
		return coral;
	}
}