package dev.amble.ait.client.models.machines;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class EngineModel extends SinglePartEntityModel {
    private final ModelPart bone;

    public EngineModel(ModelPart root) {
        this.bone = root.getChild("engine");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData engine = modelPartData.addChild("engine", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 10.15F, 0.0F));

        ModelPartData one = engine.addChild("one", ModelPartBuilder.create().uv(296, 363).cuboid(-16.0F, -0.25F, -16.0F, 32.0F, 0.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r1 = one.addChild("cube_r1", ModelPartBuilder.create().uv(130, 137).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(2.0F, 2.5F, 0.0F, 0.0F, 1.5708F, 0.3491F));

        ModelPartData cube_r2 = one.addChild("cube_r2", ModelPartBuilder.create().uv(195, 137).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 2.5F, -2.0F, 2.7925F, 0.0F, -3.1416F));

        ModelPartData cube_r3 = one.addChild("cube_r3", ModelPartBuilder.create().uv(260, 137).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-2.0F, 2.5F, 0.0F, 0.0F, -1.5708F, -0.3491F));

        ModelPartData cube_r4 = one.addChild("cube_r4", ModelPartBuilder.create().uv(325, 137).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 2.5F, 2.0F, -0.3491F, 0.0F, 0.0F));

        ModelPartData cube_r5 = one.addChild("cube_r5", ModelPartBuilder.create().uv(245, 43).cuboid(-12.0F, -42.0F, 0.0F, 24.0F, 42.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData cube_r6 = one.addChild("cube_r6", ModelPartBuilder.create().uv(294, 43).cuboid(-12.0F, -42.0F, 0.0F, 24.0F, 42.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData two = engine.addChild("two", ModelPartBuilder.create().uv(296, 331).cuboid(-16.0F, -0.25F, -16.0F, 32.0F, 0.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r7 = two.addChild("cube_r7", ModelPartBuilder.create().uv(390, 137).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r8 = two.addChild("cube_r8", ModelPartBuilder.create().uv(455, 137).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData cube_r9 = two.addChild("cube_r9", ModelPartBuilder.create().uv(520, 137).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r10 = two.addChild("cube_r10", ModelPartBuilder.create().uv(0, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r11 = two.addChild("cube_r11", ModelPartBuilder.create().uv(147, 43).cuboid(-12.0F, -42.0F, 0.0F, 24.0F, 42.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData cube_r12 = two.addChild("cube_r12", ModelPartBuilder.create().uv(196, 43).cuboid(-12.0F, -42.0F, 0.0F, 24.0F, 42.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData bone42 = two.addChild("bone42", ModelPartBuilder.create(), ModelTransform.of(0.0F, -32.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        ModelPartData bone43 = bone42.addChild("bone43", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData bone44 = bone42.addChild("bone44", ModelPartBuilder.create(), ModelTransform.of(0.0F, 8.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData bone45 = bone42.addChild("bone45", ModelPartBuilder.create(), ModelTransform.of(0.0F, 22.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r13 = bone45.addChild("cube_r13", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(4.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r14 = bone45.addChild("cube_r14", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 4.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r15 = bone45.addChild("cube_r15", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-4.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData bone46 = bone42.addChild("bone46", ModelPartBuilder.create(), ModelTransform.of(0.0F, -7.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData bone47 = bone42.addChild("bone47", ModelPartBuilder.create(), ModelTransform.of(0.0F, 15.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r16 = bone47.addChild("cube_r16", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(3.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r17 = bone47.addChild("cube_r17", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 3.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r18 = bone47.addChild("cube_r18", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -3.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData three = engine.addChild("three", ModelPartBuilder.create().uv(296, 331).cuboid(-16.0F, -0.25F, -16.0F, 32.0F, 0.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r19 = three.addChild("cube_r19", ModelPartBuilder.create().uv(390, 273).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r20 = three.addChild("cube_r20", ModelPartBuilder.create().uv(455, 273).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData cube_r21 = three.addChild("cube_r21", ModelPartBuilder.create().uv(520, 273).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r22 = three.addChild("cube_r22", ModelPartBuilder.create().uv(0, 290).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r23 = three.addChild("cube_r23", ModelPartBuilder.create().uv(49, 43).cuboid(-12.0F, -42.0F, 0.0F, 24.0F, 42.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData cube_r24 = three.addChild("cube_r24", ModelPartBuilder.create().uv(98, 43).cuboid(-12.0F, -42.0F, 0.0F, 24.0F, 42.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData bone36 = three.addChild("bone36", ModelPartBuilder.create(), ModelTransform.of(0.0F, -32.0F, 0.0F, 0.0F, -1.1781F, 0.0F));

        ModelPartData bone37 = bone36.addChild("bone37", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData bone38 = bone36.addChild("bone38", ModelPartBuilder.create(), ModelTransform.of(0.0F, 8.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r25 = bone38.addChild("cube_r25", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r26 = bone38.addChild("cube_r26", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-5.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r27 = bone38.addChild("cube_r27", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -5.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone39 = bone36.addChild("bone39", ModelPartBuilder.create(), ModelTransform.of(0.0F, 22.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r28 = bone39.addChild("cube_r28", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(5.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r29 = bone39.addChild("cube_r29", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r30 = bone39.addChild("cube_r30", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-5.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData bone40 = bone36.addChild("bone40", ModelPartBuilder.create(), ModelTransform.of(0.0F, -7.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData bone41 = bone36.addChild("bone41", ModelPartBuilder.create(), ModelTransform.of(0.0F, 15.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r31 = bone41.addChild("cube_r31", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(5.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r32 = bone41.addChild("cube_r32", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r33 = bone41.addChild("cube_r33", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -5.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData four = engine.addChild("four", ModelPartBuilder.create().uv(296, 299).cuboid(-16.0F, -0.25F, -16.0F, 32.0F, 0.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r34 = four.addChild("cube_r34", ModelPartBuilder.create().uv(455, 120).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(4.0F, 2.5F, 0.0F, 0.0F, 1.5708F, 0.3491F));

        ModelPartData cube_r35 = four.addChild("cube_r35", ModelPartBuilder.create().uv(520, 120).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 2.5F, -4.0F, 2.7925F, 0.0F, -3.1416F));

        ModelPartData cube_r36 = four.addChild("cube_r36", ModelPartBuilder.create().uv(0, 137).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-4.0F, 2.5F, 0.0F, 0.0F, -1.5708F, -0.3491F));

        ModelPartData cube_r37 = four.addChild("cube_r37", ModelPartBuilder.create().uv(65, 137).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 2.5F, 4.0F, -0.3491F, 0.0F, 0.0F));

        ModelPartData cube_r38 = four.addChild("cube_r38", ModelPartBuilder.create().uv(50, 386).cuboid(-6.0F, -36.0F, -6.0F, 12.0F, 36.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 1.0F, -0.0873F, 0.0F, 0.0F));

        ModelPartData cube_r39 = four.addChild("cube_r39", ModelPartBuilder.create().uv(75, 386).cuboid(-6.0F, -36.0F, -6.0F, 12.0F, 36.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(1.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0873F));

        ModelPartData cube_r40 = four.addChild("cube_r40", ModelPartBuilder.create().uv(100, 386).cuboid(-6.0F, -36.0F, -6.0F, 12.0F, 36.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -1.0F, 3.0543F, 0.0F, -3.1416F));

        ModelPartData cube_r41 = four.addChild("cube_r41", ModelPartBuilder.create().uv(125, 386).cuboid(-6.0F, -36.0F, -6.0F, 12.0F, 36.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(-1.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.0873F));

        ModelPartData cube_r42 = four.addChild("cube_r42", ModelPartBuilder.create().uv(0, 222).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -19.0F, 2.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r43 = four.addChild("cube_r43", ModelPartBuilder.create().uv(520, 205).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(2.0F, -19.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r44 = four.addChild("cube_r44", ModelPartBuilder.create().uv(455, 205).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-2.0F, -19.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r45 = four.addChild("cube_r45", ModelPartBuilder.create().uv(390, 205).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -19.0F, -2.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData cube_r46 = four.addChild("cube_r46", ModelPartBuilder.create().uv(260, 222).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(2.0F, -8.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r47 = four.addChild("cube_r47", ModelPartBuilder.create().uv(195, 222).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -8.0F, 2.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r48 = four.addChild("cube_r48", ModelPartBuilder.create().uv(130, 222).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -8.0F, -2.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData cube_r49 = four.addChild("cube_r49", ModelPartBuilder.create().uv(65, 222).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-2.0F, -8.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r50 = four.addChild("cube_r50", ModelPartBuilder.create().uv(130, 273).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 1.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r51 = four.addChild("cube_r51", ModelPartBuilder.create().uv(195, 273).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(1.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r52 = four.addChild("cube_r52", ModelPartBuilder.create().uv(260, 273).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -1.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData cube_r53 = four.addChild("cube_r53", ModelPartBuilder.create().uv(325, 273).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-1.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r54 = four.addChild("cube_r54", ModelPartBuilder.create().uv(390, 0).cuboid(-12.0F, -42.0F, 0.0F, 24.0F, 42.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData cube_r55 = four.addChild("cube_r55", ModelPartBuilder.create().uv(0, 43).cuboid(-12.0F, -42.0F, 0.0F, 24.0F, 42.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData bone28 = four.addChild("bone28", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -32.0F, 0.0F));

        ModelPartData bone31 = bone28.addChild("bone31", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r56 = bone31.addChild("cube_r56", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -2.5858F, -13.4142F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-7.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r57 = bone31.addChild("cube_r57", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -2.5858F, -13.4142F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -7.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone32 = bone28.addChild("bone32", ModelPartBuilder.create(), ModelTransform.of(0.0F, 8.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r58 = bone32.addChild("cube_r58", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r59 = bone32.addChild("cube_r59", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-5.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r60 = bone32.addChild("cube_r60", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -5.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone33 = bone28.addChild("bone33", ModelPartBuilder.create(), ModelTransform.of(0.0F, 22.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r61 = bone33.addChild("cube_r61", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(5.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r62 = bone33.addChild("cube_r62", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r63 = bone33.addChild("cube_r63", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-5.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData bone34 = bone28.addChild("bone34", ModelPartBuilder.create(), ModelTransform.of(0.0F, -7.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r64 = bone34.addChild("cube_r64", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(5.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r65 = bone34.addChild("cube_r65", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r66 = bone34.addChild("cube_r66", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-5.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData bone35 = bone28.addChild("bone35", ModelPartBuilder.create(), ModelTransform.of(0.0F, 15.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r67 = bone35.addChild("cube_r67", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(5.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r68 = bone35.addChild("cube_r68", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r69 = bone35.addChild("cube_r69", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -5.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData five = engine.addChild("five", ModelPartBuilder.create().uv(232, 299).cuboid(-16.0F, -0.25F, -16.0F, 32.0F, 0.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r70 = five.addChild("cube_r70", ModelPartBuilder.create().uv(343, 43).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(8.0F, 3.0F, 0.0F, 0.0F, 1.5708F, 0.2618F));

        ModelPartData cube_r71 = five.addChild("cube_r71", ModelPartBuilder.create().uv(0, 86).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 3.0F, -8.0F, 2.8798F, 0.0F, -3.1416F));

        ModelPartData cube_r72 = five.addChild("cube_r72", ModelPartBuilder.create().uv(65, 86).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 3.0F, 0.0F, 0.0F, -1.5708F, -0.2618F));

        ModelPartData cube_r73 = five.addChild("cube_r73", ModelPartBuilder.create().uv(130, 86).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 3.0F, 8.0F, -0.2618F, 0.0F, 0.0F));

        ModelPartData cube_r74 = five.addChild("cube_r74", ModelPartBuilder.create().uv(65, 205).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -1.0F, -6.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData cube_r75 = five.addChild("cube_r75", ModelPartBuilder.create().uv(0, 205).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-6.0F, -1.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r76 = five.addChild("cube_r76", ModelPartBuilder.create().uv(520, 188).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -1.0F, 6.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r77 = five.addChild("cube_r77", ModelPartBuilder.create().uv(455, 188).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(6.0F, -1.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r78 = five.addChild("cube_r78", ModelPartBuilder.create().uv(390, 188).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F))
                .uv(130, 205).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -26.0F, -2.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData cube_r79 = five.addChild("cube_r79", ModelPartBuilder.create().uv(325, 188).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F))
                .uv(325, 205).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(2.0F, -26.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r80 = five.addChild("cube_r80", ModelPartBuilder.create().uv(260, 188).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F))
                .uv(260, 205).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -26.0F, 2.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r81 = five.addChild("cube_r81", ModelPartBuilder.create().uv(195, 188).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F))
                .uv(195, 205).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-2.0F, -26.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r82 = five.addChild("cube_r82", ModelPartBuilder.create().uv(260, 256).cuboid(-8.0F, -6.9583F, -7.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 3.0F, 0.0F, -0.4363F, -0.7854F, 0.0F));

        ModelPartData cube_r83 = five.addChild("cube_r83", ModelPartBuilder.create().uv(195, 256).cuboid(-8.0F, -6.9583F, -7.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 3.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

        ModelPartData cube_r84 = five.addChild("cube_r84", ModelPartBuilder.create().uv(455, 256).cuboid(-8.0F, -6.9583F, -7.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 3.0F, 0.0F, 2.7053F, 0.7854F, 3.1416F));

        ModelPartData cube_r85 = five.addChild("cube_r85", ModelPartBuilder.create().uv(390, 256).cuboid(-8.0F, -6.9583F, -7.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 3.0F, 0.0F, 0.0F, 1.5708F, 0.4363F));

        ModelPartData cube_r86 = five.addChild("cube_r86", ModelPartBuilder.create().uv(325, 256).cuboid(-8.0F, -6.9583F, -7.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 3.0F, 0.0F, -0.4363F, 0.7854F, 0.0F));

        ModelPartData cube_r87 = five.addChild("cube_r87", ModelPartBuilder.create().uv(520, 256).cuboid(-8.0F, -6.9583F, -7.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 3.0F, 0.0F, 2.7053F, 0.0F, 3.1416F));

        ModelPartData cube_r88 = five.addChild("cube_r88", ModelPartBuilder.create().uv(0, 273).cuboid(-8.0F, -6.9583F, -7.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 3.0F, 0.0F, 2.7053F, -0.7854F, 3.1416F));

        ModelPartData cube_r89 = five.addChild("cube_r89", ModelPartBuilder.create().uv(65, 273).cuboid(-8.0F, -6.9583F, -7.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 3.0F, 0.0F, 0.0F, -1.5708F, -0.4363F));

        ModelPartData cube_r90 = five.addChild("cube_r90", ModelPartBuilder.create().uv(226, 343).cuboid(-6.0F, -36.0F, -6.0F, 12.0F, 36.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(1.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.0873F));

        ModelPartData cube_r91 = five.addChild("cube_r91", ModelPartBuilder.create().uv(130, 188).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -14.0F, -2.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData cube_r92 = five.addChild("cube_r92", ModelPartBuilder.create().uv(65, 188).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(2.0F, -14.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r93 = five.addChild("cube_r93", ModelPartBuilder.create().uv(0, 188).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -14.0F, 2.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r94 = five.addChild("cube_r94", ModelPartBuilder.create().uv(520, 171).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-2.0F, -14.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r95 = five.addChild("cube_r95", ModelPartBuilder.create().uv(251, 343).cuboid(-6.0F, -36.0F, -6.0F, 12.0F, 36.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(-1.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0873F));

        ModelPartData cube_r96 = five.addChild("cube_r96", ModelPartBuilder.create().uv(0, 386).cuboid(-6.0F, -36.0F, -6.0F, 12.0F, 36.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 1.0F, 3.0543F, 0.0F, -3.1416F));

        ModelPartData cube_r97 = five.addChild("cube_r97", ModelPartBuilder.create().uv(25, 386).cuboid(-6.0F, -36.0F, -6.0F, 12.0F, 36.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -1.0F, -0.0873F, 0.0F, 0.0F));

        ModelPartData cube_r98 = five.addChild("cube_r98", ModelPartBuilder.create().uv(292, 0).cuboid(-12.0F, -42.0F, 0.0F, 24.0F, 42.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData cube_r99 = five.addChild("cube_r99", ModelPartBuilder.create().uv(341, 0).cuboid(-12.0F, -42.0F, 0.0F, 24.0F, 42.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData bone24 = five.addChild("bone24", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -35.0F, 0.0F));

        ModelPartData bone25 = bone24.addChild("bone25", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r100 = bone25.addChild("cube_r100", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -2.5858F, -13.4142F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r101 = bone25.addChild("cube_r101", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -2.5858F, -13.4142F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r102 = bone25.addChild("cube_r102", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -2.5858F, -13.4142F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r103 = bone25.addChild("cube_r103", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -2.5858F, -13.4142F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone26 = bone24.addChild("bone26", ModelPartBuilder.create(), ModelTransform.of(0.0F, 8.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r104 = bone26.addChild("cube_r104", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(7.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r105 = bone26.addChild("cube_r105", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 7.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r106 = bone26.addChild("cube_r106", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-7.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r107 = bone26.addChild("cube_r107", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -7.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone27 = bone24.addChild("bone27", ModelPartBuilder.create(), ModelTransform.of(0.0F, 22.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r108 = bone27.addChild("cube_r108", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(7.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r109 = bone27.addChild("cube_r109", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 7.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r110 = bone27.addChild("cube_r110", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-7.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r111 = bone27.addChild("cube_r111", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -7.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone29 = bone24.addChild("bone29", ModelPartBuilder.create(), ModelTransform.of(0.0F, -7.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r112 = bone29.addChild("cube_r112", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(7.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r113 = bone29.addChild("cube_r113", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 7.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r114 = bone29.addChild("cube_r114", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-7.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r115 = bone29.addChild("cube_r115", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -7.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone30 = bone24.addChild("bone30", ModelPartBuilder.create(), ModelTransform.of(0.0F, 15.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r116 = bone30.addChild("cube_r116", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(7.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r117 = bone30.addChild("cube_r117", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 7.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r118 = bone30.addChild("cube_r118", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-7.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r119 = bone30.addChild("cube_r119", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -7.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData six = engine.addChild("six", ModelPartBuilder.create().uv(232, 299).cuboid(-16.0F, -0.25F, -16.0F, 32.0F, 0.0F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r120 = six.addChild("cube_r120", ModelPartBuilder.create().uv(195, 86).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 3.0F, 11.0F, -0.2618F, 0.0F, 0.0F));

        ModelPartData cube_r121 = six.addChild("cube_r121", ModelPartBuilder.create().uv(260, 86).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(11.0F, 3.0F, 0.0F, 0.0F, 1.5708F, 0.2618F));

        ModelPartData cube_r122 = six.addChild("cube_r122", ModelPartBuilder.create().uv(325, 86).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 3.0F, -11.0F, 2.8798F, 0.0F, -3.1416F));

        ModelPartData cube_r123 = six.addChild("cube_r123", ModelPartBuilder.create().uv(390, 86).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-11.0F, 3.0F, 0.0F, 0.0F, -1.5708F, -0.2618F));

        ModelPartData cube_r124 = six.addChild("cube_r124", ModelPartBuilder.create().uv(455, 171).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -2.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData cube_r125 = six.addChild("cube_r125", ModelPartBuilder.create().uv(390, 171).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, -2.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r126 = six.addChild("cube_r126", ModelPartBuilder.create().uv(325, 171).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -2.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r127 = six.addChild("cube_r127", ModelPartBuilder.create().uv(260, 171).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, -2.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r128 = six.addChild("cube_r128", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, -28.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r129 = six.addChild("cube_r129", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -28.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r130 = six.addChild("cube_r130", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -28.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData cube_r131 = six.addChild("cube_r131", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, -28.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r132 = six.addChild("cube_r132", ModelPartBuilder.create().uv(0, 171).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, -15.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r133 = six.addChild("cube_r133", ModelPartBuilder.create().uv(65, 171).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -15.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData cube_r134 = six.addChild("cube_r134", ModelPartBuilder.create().uv(130, 171).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, -15.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r135 = six.addChild("cube_r135", ModelPartBuilder.create().uv(195, 171).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -15.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r136 = six.addChild("cube_r136", ModelPartBuilder.create().uv(390, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, -37.0F, 0.0F, 0.0F, 1.5708F, 0.2618F));

        ModelPartData cube_r137 = six.addChild("cube_r137", ModelPartBuilder.create().uv(325, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -37.0F, -9.0F, 2.8798F, 0.0F, -3.1416F));

        ModelPartData cube_r138 = six.addChild("cube_r138", ModelPartBuilder.create().uv(520, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -37.0F, 9.0F, -0.2618F, 0.0F, 0.0F));

        ModelPartData cube_r139 = six.addChild("cube_r139", ModelPartBuilder.create().uv(455, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, -37.0F, 0.0F, 0.0F, -1.5708F, -0.2618F));

        ModelPartData cube_r140 = six.addChild("cube_r140", ModelPartBuilder.create().uv(325, 222).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -42.0F, 0.0F, 0.0F, -1.5708F, 2.7053F));

        ModelPartData cube_r141 = six.addChild("cube_r141", ModelPartBuilder.create().uv(390, 222).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -42.0F, 0.0F, 2.7053F, -0.7854F, 0.0F));

        ModelPartData cube_r142 = six.addChild("cube_r142", ModelPartBuilder.create().uv(455, 222).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -42.0F, 0.0F, 2.7053F, 0.0F, 0.0F));

        ModelPartData cube_r143 = six.addChild("cube_r143", ModelPartBuilder.create().uv(520, 222).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -42.0F, 0.0F, -0.4363F, 0.7854F, -3.1416F));

        ModelPartData cube_r144 = six.addChild("cube_r144", ModelPartBuilder.create().uv(0, 239).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -42.0F, 0.0F, 0.0F, 1.5708F, -2.7053F));

        ModelPartData cube_r145 = six.addChild("cube_r145", ModelPartBuilder.create().uv(65, 239).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -42.0F, 0.0F, 2.7053F, 0.7854F, 0.0F));

        ModelPartData cube_r146 = six.addChild("cube_r146", ModelPartBuilder.create().uv(130, 239).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -42.0F, 0.0F, -0.4363F, 0.0F, -3.1416F));

        ModelPartData cube_r147 = six.addChild("cube_r147", ModelPartBuilder.create().uv(195, 239).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -42.0F, 0.0F, -0.4363F, -0.7854F, -3.1416F));

        ModelPartData cube_r148 = six.addChild("cube_r148", ModelPartBuilder.create().uv(260, 239).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 1.0F, 0.0F, -0.4363F, -0.7854F, 0.0F));

        ModelPartData cube_r149 = six.addChild("cube_r149", ModelPartBuilder.create().uv(325, 239).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 1.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

        ModelPartData cube_r150 = six.addChild("cube_r150", ModelPartBuilder.create().uv(390, 239).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 2.7053F, 0.7854F, 3.1416F));

        ModelPartData cube_r151 = six.addChild("cube_r151", ModelPartBuilder.create().uv(455, 239).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.0F, 1.5708F, 0.4363F));

        ModelPartData cube_r152 = six.addChild("cube_r152", ModelPartBuilder.create().uv(520, 239).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 1.0F, 0.0F, -0.4363F, 0.7854F, 0.0F));

        ModelPartData cube_r153 = six.addChild("cube_r153", ModelPartBuilder.create().uv(0, 256).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 2.7053F, 0.0F, 3.1416F));

        ModelPartData cube_r154 = six.addChild("cube_r154", ModelPartBuilder.create().uv(65, 256).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 2.7053F, -0.7854F, 3.1416F));

        ModelPartData cube_r155 = six.addChild("cube_r155", ModelPartBuilder.create().uv(130, 256).cuboid(-8.0F, -6.9583F, -5.6559F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 1.0F, 0.0F, 0.0F, -1.5708F, -0.4363F));

        ModelPartData cube_r156 = six.addChild("cube_r156", ModelPartBuilder.create().uv(126, 343).cuboid(-6.0F, -36.0F, -6.0F, 12.0F, 36.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(1.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.0873F));

        ModelPartData cube_r157 = six.addChild("cube_r157", ModelPartBuilder.create().uv(151, 343).cuboid(-6.0F, -36.0F, -6.0F, 12.0F, 36.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(-1.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0873F));

        ModelPartData cube_r158 = six.addChild("cube_r158", ModelPartBuilder.create().uv(176, 343).cuboid(-6.0F, -36.0F, -6.0F, 12.0F, 36.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 1.0F, 3.0543F, 0.0F, -3.1416F));

        ModelPartData cube_r159 = six.addChild("cube_r159", ModelPartBuilder.create().uv(201, 343).cuboid(-6.0F, -36.0F, -6.0F, 12.0F, 36.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -1.0F, -0.0873F, 0.0F, 0.0F));

        ModelPartData cube_r160 = six.addChild("cube_r160", ModelPartBuilder.create().uv(194, 0).cuboid(-12.0F, -42.0F, 0.0F, 24.0F, 42.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        ModelPartData cube_r161 = six.addChild("cube_r161", ModelPartBuilder.create().uv(243, 0).cuboid(-12.0F, -42.0F, 0.0F, 24.0F, 42.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

        ModelPartData octagon2 = six.addChild("octagon2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -21.0F, 0.0F));

        ModelPartData bone14 = six.addChild("bone14", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -35.0F, 0.0F));

        ModelPartData bone6 = bone14.addChild("bone6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r162 = bone6.addChild("cube_r162", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r163 = bone6.addChild("cube_r163", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r164 = bone6.addChild("cube_r164", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r165 = bone6.addChild("cube_r165", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone12 = bone14.addChild("bone12", ModelPartBuilder.create(), ModelTransform.of(0.0F, 8.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r166 = bone12.addChild("cube_r166", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r167 = bone12.addChild("cube_r167", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r168 = bone12.addChild("cube_r168", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r169 = bone12.addChild("cube_r169", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone13 = bone14.addChild("bone13", ModelPartBuilder.create(), ModelTransform.of(0.0F, 22.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r170 = bone13.addChild("cube_r170", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r171 = bone13.addChild("cube_r171", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r172 = bone13.addChild("cube_r172", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r173 = bone13.addChild("cube_r173", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone11 = bone14.addChild("bone11", ModelPartBuilder.create(), ModelTransform.of(0.0F, -3.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r174 = bone11.addChild("cube_r174", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r175 = bone11.addChild("cube_r175", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r176 = bone11.addChild("cube_r176", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r177 = bone11.addChild("cube_r177", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone7 = bone14.addChild("bone7", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r178 = bone7.addChild("cube_r178", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r179 = bone7.addChild("cube_r179", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r180 = bone7.addChild("cube_r180", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r181 = bone7.addChild("cube_r181", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone10 = bone14.addChild("bone10", ModelPartBuilder.create(), ModelTransform.of(0.0F, -3.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r182 = bone10.addChild("cube_r182", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r183 = bone10.addChild("cube_r183", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r184 = bone10.addChild("cube_r184", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r185 = bone10.addChild("cube_r185", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone9 = bone14.addChild("bone9", ModelPartBuilder.create(), ModelTransform.of(0.0F, -6.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r186 = bone9.addChild("cube_r186", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r187 = bone9.addChild("cube_r187", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r188 = bone9.addChild("cube_r188", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r189 = bone9.addChild("cube_r189", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone8 = bone14.addChild("bone8", ModelPartBuilder.create(), ModelTransform.of(0.0F, -5.0F, 0.0F, 0.0F, 1.5708F, 3.1416F));

        ModelPartData cube_r190 = bone8.addChild("cube_r190", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -8.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r191 = bone8.addChild("cube_r191", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -8.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r192 = bone8.addChild("cube_r192", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -8.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r193 = bone8.addChild("cube_r193", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -8.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone15 = six.addChild("bone15", ModelPartBuilder.create(), ModelTransform.of(0.0F, -39.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

        ModelPartData bone16 = bone15.addChild("bone16", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r194 = bone16.addChild("cube_r194", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r195 = bone16.addChild("cube_r195", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r196 = bone16.addChild("cube_r196", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r197 = bone16.addChild("cube_r197", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone17 = bone15.addChild("bone17", ModelPartBuilder.create(), ModelTransform.of(0.0F, 8.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r198 = bone17.addChild("cube_r198", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r199 = bone17.addChild("cube_r199", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r200 = bone17.addChild("cube_r200", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r201 = bone17.addChild("cube_r201", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone18 = bone15.addChild("bone18", ModelPartBuilder.create(), ModelTransform.of(0.0F, 22.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r202 = bone18.addChild("cube_r202", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r203 = bone18.addChild("cube_r203", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r204 = bone18.addChild("cube_r204", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r205 = bone18.addChild("cube_r205", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone19 = bone15.addChild("bone19", ModelPartBuilder.create(), ModelTransform.of(0.0F, -3.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r206 = bone19.addChild("cube_r206", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r207 = bone19.addChild("cube_r207", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r208 = bone19.addChild("cube_r208", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r209 = bone19.addChild("cube_r209", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -12.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone20 = bone15.addChild("bone20", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r210 = bone20.addChild("cube_r210", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r211 = bone20.addChild("cube_r211", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r212 = bone20.addChild("cube_r212", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r213 = bone20.addChild("cube_r213", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone21 = bone15.addChild("bone21", ModelPartBuilder.create(), ModelTransform.of(0.0F, -3.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        ModelPartData cube_r214 = bone21.addChild("cube_r214", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r215 = bone21.addChild("cube_r215", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r216 = bone21.addChild("cube_r216", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r217 = bone21.addChild("cube_r217", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone22 = bone15.addChild("bone22", ModelPartBuilder.create(), ModelTransform.of(0.0F, -6.0F, 0.0F, 3.1416F, 0.7854F, 0.0F));

        ModelPartData cube_r218 = bone22.addChild("cube_r218", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r219 = bone22.addChild("cube_r219", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r220 = bone22.addChild("cube_r220", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r221 = bone22.addChild("cube_r221", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -10.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone23 = bone15.addChild("bone23", ModelPartBuilder.create(), ModelTransform.of(0.0F, -5.0F, 0.0F, 0.0F, 1.5708F, 3.1416F));

        ModelPartData cube_r222 = bone23.addChild("cube_r222", ModelPartBuilder.create().uv(260, 154).cuboid(-8.0F, -4.0F, -8.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(9.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F));

        ModelPartData cube_r223 = bone23.addChild("cube_r223", ModelPartBuilder.create().uv(195, 154).cuboid(-8.0F, -4.0F, -8.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 9.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r224 = bone23.addChild("cube_r224", ModelPartBuilder.create().uv(65, 154).cuboid(-8.0F, -4.0F, -8.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(-9.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F));

        ModelPartData cube_r225 = bone23.addChild("cube_r225", ModelPartBuilder.create().uv(130, 154).cuboid(-8.0F, -4.0F, -8.0F, 16.0F, 0.0F, 16.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, -9.0F, 2.3562F, 0.0F, -3.1416F));

        ModelPartData bone = engine.addChild("bone", ModelPartBuilder.create().uv(341, 460).cuboid(-24.0F, 8.0F, -24.0F, 48.0F, 0.0F, 48.0F, new Dilation(0.0F))
                .uv(357, 427).cuboid(-16.0F, -5.85F, -16.0F, 32.0F, 0.0F, 32.0F, new Dilation(0.0F))
                .uv(383, 524).cuboid(-3.0F, -3.0F, -24.0F, 6.0F, 6.0F, 48.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 5.85F, 0.0F));

        ModelPartData cube_r226 = bone.addChild("cube_r226", ModelPartBuilder.create().uv(383, 524).cuboid(-3.0F, -3.0F, -24.0F, 6.0F, 6.0F, 48.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r227 = bone.addChild("cube_r227", ModelPartBuilder.create().uv(373, 508).cuboid(-24.0F, -20.7846F, -12.0F, 48.0F, 0.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 8.0F, 0.0F, 0.0F, -1.5708F, 1.0472F));

        ModelPartData cube_r228 = bone.addChild("cube_r228", ModelPartBuilder.create().uv(373, 508).cuboid(-24.0F, -20.7846F, -12.0F, 48.0F, 0.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 8.0F, 0.0F, -2.0944F, 0.0F, -3.1416F));

        ModelPartData cube_r229 = bone.addChild("cube_r229", ModelPartBuilder.create().uv(373, 508).cuboid(-24.0F, -20.7846F, -12.0F, 48.0F, 0.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 8.0F, 0.0F, 0.0F, 1.5708F, -1.0472F));

        ModelPartData cube_r230 = bone.addChild("cube_r230", ModelPartBuilder.create().uv(373, 508).cuboid(-24.0F, -20.7846F, -12.0F, 48.0F, 0.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 8.0F, 0.0F, 1.0472F, 0.0F, 0.0F));

        ModelPartData bone2 = bone.addChild("bone2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 8.0F, 0.0F));

        ModelPartData cube_r231 = bone2.addChild("cube_r231", ModelPartBuilder.create().uv(391, 529).cuboid(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(-24.0416F, 0.0F, -24.0416F, 0.9163F, 0.7854F, 0.0F));

        ModelPartData bone3 = bone.addChild("bone3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 8.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        ModelPartData cube_r232 = bone3.addChild("cube_r232", ModelPartBuilder.create().uv(391, 529).cuboid(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(-24.0416F, 0.0F, -24.0416F, 0.9163F, 0.7854F, 0.0F));

        ModelPartData bone4 = bone.addChild("bone4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 8.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData cube_r233 = bone4.addChild("cube_r233", ModelPartBuilder.create().uv(391, 529).cuboid(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(-24.0416F, 0.0F, -24.0416F, 0.9163F, 0.7854F, 0.0F));

        ModelPartData bone5 = bone.addChild("bone5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 8.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        ModelPartData cube_r234 = bone5.addChild("cube_r234", ModelPartBuilder.create().uv(391, 529).cuboid(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(-24.0416F, 0.0F, -24.0416F, 0.9163F, 0.7854F, 0.0F));
        return TexturedModelData.of(modelData, 1024, 1024);
    }

    @Override
    public ModelPart getPart() {
        return bone;
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw,
            float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red,
            float green, float blue, float alpha) {
        bone.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
