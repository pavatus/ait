package mdteam.ait.client.models.consoles;

import mdteam.ait.AITMod;
import mdteam.ait.client.animation.console.borealis.BorealisAnimations;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.data.DoorData;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import mdteam.ait.tardis.TardisTravel;
import org.joml.Vector3f;

public class BorealisConsoleModel extends ConsoleModel {
    public static final Identifier CONSOLE_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/borealis_console.png"));
    public static final Identifier CONSOLE_TEXTURE_EMISSION = new Identifier(AITMod.MOD_ID, "textures/blockentities/consoles/borealis_console_emission.png");
    public ModelPart base_console;

    public BorealisConsoleModel(ModelPart root) {
        super(RenderLayer::getEntityCutoutNoCull);
        this.base_console = root.getChild("base_console");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base_console = modelPartData.addChild("base_console", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData panels = base_console.addChild("panels", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -30.0F, 0.0F));

        ModelPartData bone25 = panels.addChild("bone25", ModelPartBuilder.create().uv(0, 0).cuboid(-15.0F, 1.774F, -2.9235F, 30.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData bone38 = panels.addChild("bone38", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone44 = bone38.addChild("bone44", ModelPartBuilder.create().uv(0, 0).cuboid(-15.0F, 1.774F, -2.9235F, 30.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData bone56 = bone38.addChild("bone56", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone152 = bone56.addChild("bone152", ModelPartBuilder.create().uv(0, 0).cuboid(-15.0F, 1.774F, -2.9235F, 30.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData bone153 = bone56.addChild("bone153", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone154 = bone153.addChild("bone154", ModelPartBuilder.create().uv(0, 0).cuboid(-15.0F, 1.774F, -2.9235F, 30.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData bone155 = bone153.addChild("bone155", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone156 = bone155.addChild("bone156", ModelPartBuilder.create().uv(0, 0).cuboid(-15.0F, 1.774F, -2.9235F, 30.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData bone157 = bone155.addChild("bone157", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone158 = bone157.addChild("bone158", ModelPartBuilder.create().uv(0, 0).cuboid(-15.0F, 1.774F, -2.9235F, 30.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData collar = base_console.addChild("collar", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bone86 = collar.addChild("bone86", ModelPartBuilder.create().uv(96, 130).cuboid(-2.0F, -2.0F, -14.3F, 4.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -44.5F, 0.0F, 0.0F, -0.5236F, 0.0F));

        ModelPartData bone87 = bone86.addChild("bone87", ModelPartBuilder.create().uv(96, 130).cuboid(-2.0F, -2.0F, -14.3F, 4.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone88 = bone87.addChild("bone88", ModelPartBuilder.create().uv(96, 130).cuboid(-2.0F, -2.0F, -14.3F, 4.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone89 = bone88.addChild("bone89", ModelPartBuilder.create().uv(96, 130).cuboid(-2.0F, -2.0F, -14.3F, 4.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone90 = bone89.addChild("bone90", ModelPartBuilder.create().uv(96, 130).cuboid(-2.0F, -2.0F, -14.3F, 4.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone91 = bone90.addChild("bone91", ModelPartBuilder.create().uv(96, 130).cuboid(-2.0F, -2.0F, -14.3F, 4.0F, 7.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone92 = collar.addChild("bone92", ModelPartBuilder.create().uv(123, 27).cuboid(-5.0F, -2.0F, -12.3F, 10.0F, 6.0F, 3.0F, new Dilation(0.0F))
                .uv(128, 20).cuboid(-5.5F, -2.975F, -13.275F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -43.5F, 0.0F));

        ModelPartData bone93 = bone92.addChild("bone93", ModelPartBuilder.create().uv(123, 27).cuboid(-5.0F, -2.0F, -12.3F, 10.0F, 6.0F, 3.0F, new Dilation(0.0F))
                .uv(128, 20).cuboid(-5.5F, -2.975F, -13.275F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone94 = bone93.addChild("bone94", ModelPartBuilder.create().uv(123, 27).cuboid(-5.0F, -2.0F, -12.3F, 10.0F, 6.0F, 3.0F, new Dilation(0.0F))
                .uv(128, 20).cuboid(-5.5F, -2.975F, -13.275F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone95 = bone94.addChild("bone95", ModelPartBuilder.create().uv(123, 27).cuboid(-5.0F, -2.0F, -12.3F, 10.0F, 6.0F, 3.0F, new Dilation(0.0F))
                .uv(128, 20).cuboid(-5.5F, -2.975F, -13.275F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone96 = bone95.addChild("bone96", ModelPartBuilder.create().uv(123, 27).cuboid(-5.0F, -2.0F, -12.3F, 10.0F, 6.0F, 3.0F, new Dilation(0.0F))
                .uv(128, 20).cuboid(-5.5F, -2.975F, -13.275F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone97 = bone96.addChild("bone97", ModelPartBuilder.create().uv(123, 27).cuboid(-5.0F, -2.0F, -12.3F, 10.0F, 6.0F, 3.0F, new Dilation(0.0F))
                .uv(128, 20).cuboid(-5.5F, -2.975F, -13.275F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone104 = collar.addChild("bone104", ModelPartBuilder.create().uv(70, 92).cuboid(2.0F, 1.0F, -13.05F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(70, 92).cuboid(-4.0F, 1.0F, -13.05F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -45.5F, 0.0F));

        ModelPartData bone105 = bone104.addChild("bone105", ModelPartBuilder.create().uv(70, 92).cuboid(2.0F, 1.0F, -13.05F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(70, 92).cuboid(-4.0F, 1.0F, -13.05F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone106 = bone105.addChild("bone106", ModelPartBuilder.create().uv(70, 92).cuboid(2.0F, 1.0F, -13.05F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(70, 92).cuboid(-4.0F, 1.0F, -13.05F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone107 = bone106.addChild("bone107", ModelPartBuilder.create().uv(70, 92).cuboid(2.0F, 1.0F, -13.05F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(70, 92).cuboid(-4.0F, 1.0F, -13.05F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone108 = bone107.addChild("bone108", ModelPartBuilder.create().uv(70, 92).cuboid(2.0F, 1.0F, -13.05F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(70, 92).cuboid(-4.0F, 1.0F, -13.05F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone109 = bone108.addChild("bone109", ModelPartBuilder.create().uv(70, 92).cuboid(2.0F, 1.0F, -13.05F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(70, 92).cuboid(-4.0F, 1.0F, -13.05F, 2.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone98 = collar.addChild("bone98", ModelPartBuilder.create().uv(112, 119).cuboid(-6.0F, 2.0F, -13.3F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -43.5F, 0.0F));

        ModelPartData bone99 = bone98.addChild("bone99", ModelPartBuilder.create().uv(112, 119).cuboid(-6.0F, 2.0F, -13.3F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone100 = bone99.addChild("bone100", ModelPartBuilder.create().uv(112, 119).cuboid(-6.0F, 2.0F, -13.3F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone101 = bone100.addChild("bone101", ModelPartBuilder.create().uv(112, 119).cuboid(-6.0F, 2.0F, -13.3F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone102 = bone101.addChild("bone102", ModelPartBuilder.create().uv(112, 119).cuboid(-6.0F, 2.0F, -13.3F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone103 = bone102.addChild("bone103", ModelPartBuilder.create().uv(112, 119).cuboid(-6.0F, 2.0F, -13.3F, 12.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData lower_dividers = base_console.addChild("lower_dividers", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -28.0F, 0.0F));

        ModelPartData bone32 = lower_dividers.addChild("bone32", ModelPartBuilder.create().uv(0, 75).cuboid(-17.0F, -0.5858F, -2.5858F, 34.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -27.8F, -0.7854F, 0.0F, 0.0F));

        ModelPartData bone26 = lower_dividers.addChild("bone26", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone27 = bone26.addChild("bone27", ModelPartBuilder.create().uv(0, 75).cuboid(-17.0F, -0.5858F, -2.5858F, 34.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -27.8F, -0.7854F, 0.0F, 0.0F));

        ModelPartData bone28 = bone26.addChild("bone28", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone29 = bone28.addChild("bone29", ModelPartBuilder.create().uv(0, 75).cuboid(-17.0F, -0.5858F, -2.5858F, 34.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -27.8F, -0.7854F, 0.0F, 0.0F));

        ModelPartData bone30 = bone28.addChild("bone30", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone33 = bone30.addChild("bone33", ModelPartBuilder.create().uv(0, 75).cuboid(-17.0F, -0.5858F, -2.5858F, 34.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -27.8F, -0.7854F, 0.0F, 0.0F));

        ModelPartData bone34 = bone30.addChild("bone34", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone35 = bone34.addChild("bone35", ModelPartBuilder.create().uv(0, 75).cuboid(-17.0F, -0.5858F, -2.5858F, 34.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -27.8F, -0.7854F, 0.0F, 0.0F));

        ModelPartData bone36 = bone34.addChild("bone36", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone37 = bone36.addChild("bone37", ModelPartBuilder.create().uv(0, 75).cuboid(-17.0F, -0.5858F, -2.5858F, 34.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -27.8F, -0.7854F, 0.0F, 0.0F));

        ModelPartData dividers2 = base_console.addChild("dividers2", ModelPartBuilder.create().uv(67, 44).cuboid(-2.0F, -2.0F, -36.3F, 4.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -30.5F, 0.0F, 0.0F, -0.5236F, 0.0F));

        ModelPartData bone40 = dividers2.addChild("bone40", ModelPartBuilder.create().uv(67, 44).cuboid(-2.0F, -2.0F, -36.3F, 4.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone39 = bone40.addChild("bone39", ModelPartBuilder.create().uv(67, 44).cuboid(-2.0F, -2.0F, -36.3F, 4.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone41 = bone39.addChild("bone41", ModelPartBuilder.create().uv(67, 44).cuboid(-2.0F, -2.0F, -36.3F, 4.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone42 = bone41.addChild("bone42", ModelPartBuilder.create().uv(67, 44).cuboid(-2.0F, -2.0F, -36.3F, 4.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone43 = bone42.addChild("bone43", ModelPartBuilder.create().uv(67, 44).cuboid(-2.0F, -2.0F, -36.3F, 4.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData dividers = base_console.addChild("dividers", ModelPartBuilder.create(), ModelTransform.of(0.0F, -30.5F, 0.0F, 0.0F, -0.5236F, 0.0F));

        ModelPartData bone50 = dividers.addChild("bone50", ModelPartBuilder.create().uv(55, 75).cuboid(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 22.0F, new Dilation(0.0F))
                .uv(81, 0).cuboid(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 22.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -34.3F, 0.4363F, 0.0F, 0.0F));

        ModelPartData bone45 = dividers.addChild("bone45", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone46 = bone45.addChild("bone46", ModelPartBuilder.create().uv(55, 75).cuboid(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 22.0F, new Dilation(0.0F))
                .uv(81, 0).cuboid(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 22.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -34.3F, 0.4363F, 0.0F, 0.0F));

        ModelPartData bone47 = bone45.addChild("bone47", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone48 = bone47.addChild("bone48", ModelPartBuilder.create().uv(55, 75).cuboid(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 22.0F, new Dilation(0.0F))
                .uv(81, 0).cuboid(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 22.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -34.3F, 0.4363F, 0.0F, 0.0F));

        ModelPartData bone49 = bone47.addChild("bone49", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone51 = bone49.addChild("bone51", ModelPartBuilder.create().uv(55, 75).cuboid(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 22.0F, new Dilation(0.0F))
                .uv(81, 0).cuboid(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 22.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -34.3F, 0.4363F, 0.0F, 0.0F));

        ModelPartData bone52 = bone49.addChild("bone52", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone53 = bone52.addChild("bone53", ModelPartBuilder.create().uv(55, 75).cuboid(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 22.0F, new Dilation(0.0F))
                .uv(81, 0).cuboid(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 22.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -34.3F, 0.4363F, 0.0F, 0.0F));

        ModelPartData bone54 = bone52.addChild("bone54", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone55 = bone54.addChild("bone55", ModelPartBuilder.create().uv(55, 75).cuboid(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 22.0F, new Dilation(0.0F))
                .uv(81, 0).cuboid(-0.5F, -2.0F, -1.0F, 1.0F, 4.0F, 22.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -34.3F, 0.4363F, 0.0F, 0.0F));

        ModelPartData bone110 = base_console.addChild("bone110", ModelPartBuilder.create(), ModelTransform.of(0.0F, -44.5F, 0.0F, 0.0F, -0.5236F, 0.0F));

        ModelPartData leaf1 = bone110.addChild("leaf1", ModelPartBuilder.create().uv(0, 84).cuboid(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -12.3F, -1.0472F, 0.0F, 0.0F));

        ModelPartData bone111 = bone110.addChild("bone111", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData leaf2 = bone111.addChild("leaf2", ModelPartBuilder.create().uv(0, 84).cuboid(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -12.3F, -1.0472F, 0.0F, 0.0F));

        ModelPartData bone113 = bone111.addChild("bone113", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData leaf3 = bone113.addChild("leaf3", ModelPartBuilder.create().uv(0, 84).cuboid(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -12.3F, -1.0472F, 0.0F, 0.0F));

        ModelPartData bone115 = bone113.addChild("bone115", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData leaf4 = bone115.addChild("leaf4", ModelPartBuilder.create().uv(0, 84).cuboid(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -12.3F, -1.0472F, 0.0F, 0.0F));

        ModelPartData bone118 = bone115.addChild("bone118", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData leaf5 = bone118.addChild("leaf5", ModelPartBuilder.create().uv(0, 84).cuboid(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -12.3F, -1.0472F, 0.0F, 0.0F));

        ModelPartData bone120 = bone118.addChild("bone120", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData leaf6 = bone120.addChild("leaf6", ModelPartBuilder.create().uv(0, 84).cuboid(-1.0F, -7.0F, 0.0F, 2.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -12.3F, -1.0472F, 0.0F, 0.0F));

        ModelPartData bone84 = base_console.addChild("bone84", ModelPartBuilder.create().uv(106, 10).cuboid(-6.5F, -23.5F, -22.8F, 13.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData bone85 = bone84.addChild("bone85", ModelPartBuilder.create().uv(86, 75).cuboid(-5.5F, -3.0F, -4.5F, 11.0F, 7.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -22.5F, -20.3F, 0.4363F, 0.0F, 0.0F));

        ModelPartData bone148 = bone84.addChild("bone148", ModelPartBuilder.create().uv(106, 10).cuboid(-6.5F, -23.5F, -22.8F, 13.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

        ModelPartData bone149 = bone148.addChild("bone149", ModelPartBuilder.create().uv(86, 75).cuboid(-5.5F, -3.0F, -4.5F, 11.0F, 7.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -22.5F, -20.3F, 0.4363F, 0.0F, 0.0F));

        ModelPartData bone166 = bone149.addChild("bone166", ModelPartBuilder.create().uv(120, 71).cuboid(-4.5F, -5.0F, 0.0F, 9.0F, 5.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 3.0F, -4.5F, 0.6545F, 0.0F, 0.0F));

        ModelPartData bone150 = bone148.addChild("bone150", ModelPartBuilder.create().uv(106, 10).cuboid(-6.5F, -23.5F, -22.8F, 13.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

        ModelPartData bone151 = bone150.addChild("bone151", ModelPartBuilder.create().uv(86, 75).cuboid(-5.5F, -3.0F, -4.5F, 11.0F, 7.0F, 11.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -22.5F, -20.3F, 0.4363F, 0.0F, 0.0F));

        ModelPartData bone62 = base_console.addChild("bone62", ModelPartBuilder.create().uv(73, 126).cuboid(-2.5F, -18.5F, -17.8F, 5.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone81 = bone62.addChild("bone81", ModelPartBuilder.create().uv(0, 44).cuboid(-17.0803F, -9.0F, -12.65F, 5.0F, 9.0F, 5.0F, new Dilation(0.0F))
                .uv(74, 116).cuboid(-18.0803F, -10.0F, -13.65F, 7.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(14.5803F, -13.5F, -10.15F));

        ModelPartData bone63 = bone62.addChild("bone63", ModelPartBuilder.create().uv(73, 126).cuboid(-2.5F, -18.5F, -17.8F, 5.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

        ModelPartData bone64 = bone63.addChild("bone64", ModelPartBuilder.create().uv(0, 44).cuboid(-17.0803F, -9.0F, -12.65F, 5.0F, 9.0F, 5.0F, new Dilation(0.0F))
                .uv(74, 116).cuboid(-18.0803F, -10.0F, -13.65F, 7.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(14.5803F, -13.5F, -10.15F));

        ModelPartData bone82 = bone63.addChild("bone82", ModelPartBuilder.create().uv(73, 126).cuboid(-2.5F, -18.5F, -17.8F, 5.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

        ModelPartData bone83 = bone82.addChild("bone83", ModelPartBuilder.create().uv(0, 44).cuboid(-17.0803F, -9.0F, -12.65F, 5.0F, 9.0F, 5.0F, new Dilation(0.0F))
                .uv(74, 116).cuboid(-18.0803F, -10.0F, -13.65F, 7.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(14.5803F, -13.5F, -10.15F));

        ModelPartData bone60 = base_console.addChild("bone60", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -26.0F, 0.0F));

        ModelPartData bone61 = bone60.addChild("bone61", ModelPartBuilder.create().uv(0, 22).cuboid(-15.0F, -1.226F, 0.0765F, 30.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -30.8F, -0.2182F, 0.0F, 0.0F));

        ModelPartData bone65 = bone60.addChild("bone65", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone66 = bone65.addChild("bone66", ModelPartBuilder.create().uv(0, 22).cuboid(-15.0F, -1.226F, 0.0765F, 30.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -30.8F, -0.2182F, 0.0F, 0.0F));

        ModelPartData bone67 = bone65.addChild("bone67", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone68 = bone67.addChild("bone68", ModelPartBuilder.create().uv(0, 22).cuboid(-15.0F, -1.226F, 0.0765F, 30.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -30.8F, -0.2182F, 0.0F, 0.0F));

        ModelPartData bone69 = bone67.addChild("bone69", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone70 = bone69.addChild("bone70", ModelPartBuilder.create().uv(0, 22).cuboid(-15.0F, -1.226F, 0.0765F, 30.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -30.8F, -0.2182F, 0.0F, 0.0F));

        ModelPartData bone144 = bone69.addChild("bone144", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone145 = bone144.addChild("bone145", ModelPartBuilder.create().uv(0, 22).cuboid(-15.0F, -1.226F, 0.0765F, 30.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -30.8F, -0.2182F, 0.0F, 0.0F));

        ModelPartData bone146 = bone144.addChild("bone146", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone147 = bone146.addChild("bone147", ModelPartBuilder.create().uv(0, 22).cuboid(-15.0F, -1.226F, 0.0765F, 30.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, -30.8F, -0.2182F, 0.0F, 0.0F));

        ModelPartData bone58 = base_console.addChild("bone58", ModelPartBuilder.create(), ModelTransform.of(0.0F, -28.5F, 0.0F, 0.0F, -0.5236F, 0.0F));

        ModelPartData bone59 = bone58.addChild("bone59", ModelPartBuilder.create().uv(65, 44).cuboid(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, -34.3F, -0.3927F, 0.0F, 0.0F));

        ModelPartData bone71 = bone58.addChild("bone71", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone72 = bone71.addChild("bone72", ModelPartBuilder.create().uv(65, 44).cuboid(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, -34.3F, -0.3927F, 0.0F, 0.0F));

        ModelPartData bone73 = bone71.addChild("bone73", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone74 = bone73.addChild("bone74", ModelPartBuilder.create().uv(65, 44).cuboid(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, -34.3F, -0.3927F, 0.0F, 0.0F));

        ModelPartData bone75 = bone73.addChild("bone75", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone76 = bone75.addChild("bone76", ModelPartBuilder.create().uv(65, 44).cuboid(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, -34.3F, -0.3927F, 0.0F, 0.0F));

        ModelPartData bone77 = bone75.addChild("bone77", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone78 = bone77.addChild("bone78", ModelPartBuilder.create().uv(65, 44).cuboid(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, -34.3F, -0.3927F, 0.0F, 0.0F));

        ModelPartData bone79 = bone77.addChild("bone79", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone80 = bone79.addChild("bone80", ModelPartBuilder.create().uv(65, 44).cuboid(-2.0F, -6.0F, 0.0F, 4.0F, 6.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, -34.3F, -0.3927F, 0.0F, 0.0F));

        ModelPartData bone13 = base_console.addChild("bone13", ModelPartBuilder.create().uv(23, 130).cuboid(-1.0F, -16.0F, -15.6832F, 2.0F, 16.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        ModelPartData bone14 = bone13.addChild("bone14", ModelPartBuilder.create().uv(23, 130).cuboid(-1.0F, -16.0F, -15.6832F, 2.0F, 16.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone15 = bone14.addChild("bone15", ModelPartBuilder.create().uv(23, 130).cuboid(-1.0F, -16.0F, -15.6832F, 2.0F, 16.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone16 = bone15.addChild("bone16", ModelPartBuilder.create().uv(23, 130).cuboid(-1.0F, -16.0F, -15.6832F, 2.0F, 16.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone17 = bone16.addChild("bone17", ModelPartBuilder.create().uv(23, 130).cuboid(-1.0F, -16.0F, -15.6832F, 2.0F, 16.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone18 = bone17.addChild("bone18", ModelPartBuilder.create().uv(23, 130).cuboid(-1.0F, -16.0F, -15.6832F, 2.0F, 16.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone19 = base_console.addChild("bone19", ModelPartBuilder.create().uv(0, 110).cuboid(-6.0F, -20.0F, -11.8F, 12.0F, 20.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData bone20 = bone19.addChild("bone20", ModelPartBuilder.create().uv(0, 110).cuboid(-6.0F, -20.0F, -11.8F, 12.0F, 20.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 22).cuboid(-3.0F, -11.0F, -13.8F, 6.0F, 9.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone21 = bone20.addChild("bone21", ModelPartBuilder.create().uv(0, 110).cuboid(-6.0F, -20.0F, -11.8F, 12.0F, 20.0F, 1.0F, new Dilation(0.0F))
                .uv(128, 132).cuboid(-3.5F, -10.0F, -12.8F, 7.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone132 = bone21.addChild("bone132", ModelPartBuilder.create().uv(137, 143).cuboid(-1.0F, -2.0F, 0.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(82, 44).cuboid(2.5F, -3.0F, 0.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-1.5F, -9.5F, -12.3F, 0.3491F, 0.0F, 0.0F));

        ModelPartData bone22 = bone21.addChild("bone22", ModelPartBuilder.create().uv(0, 110).cuboid(-6.0F, -20.0F, -11.8F, 12.0F, 20.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 34).cuboid(-3.0F, -6.0F, -14.8F, 6.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(81, 0).cuboid(-3.0F, -11.0F, -12.05F, 6.0F, 9.0F, 1.0F, new Dilation(0.0F))
                .uv(61, 130).cuboid(-1.0F, -14.0F, -14.3F, 2.0F, 14.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone23 = bone22.addChild("bone23", ModelPartBuilder.create().uv(0, 110).cuboid(-6.0F, -20.0F, -11.8F, 12.0F, 20.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone24 = bone23.addChild("bone24", ModelPartBuilder.create().uv(0, 110).cuboid(-6.0F, -20.0F, -11.8F, 12.0F, 20.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-3.0F, -11.0F, -13.8F, 6.0F, 9.0F, 2.0F, new Dilation(0.0F))
                .uv(16, 44).cuboid(-2.0F, -9.0F, -14.8F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(98, 16).cuboid(-0.5F, -4.75F, -14.8F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone7 = base_console.addChild("bone7", ModelPartBuilder.create().uv(83, 138).cuboid(-1.0F, -4.0F, -17.6832F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

        ModelPartData bone8 = bone7.addChild("bone8", ModelPartBuilder.create().uv(83, 138).cuboid(-1.0F, -4.0F, -17.6832F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone9 = bone8.addChild("bone9", ModelPartBuilder.create().uv(83, 138).cuboid(-1.0F, -4.0F, -17.6832F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone10 = bone9.addChild("bone10", ModelPartBuilder.create().uv(83, 138).cuboid(-1.0F, -4.0F, -17.6832F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone11 = bone10.addChild("bone11", ModelPartBuilder.create().uv(83, 138).cuboid(-1.0F, -4.0F, -17.6832F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone12 = bone11.addChild("bone12", ModelPartBuilder.create().uv(83, 138).cuboid(-1.0F, -4.0F, -17.6832F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone = base_console.addChild("bone", ModelPartBuilder.create().uv(47, 102).cuboid(-8.0F, -4.0F, -15.8F, 16.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bone2 = bone.addChild("bone2", ModelPartBuilder.create().uv(47, 102).cuboid(-8.0F, -4.0F, -15.8F, 16.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone3 = bone2.addChild("bone3", ModelPartBuilder.create().uv(47, 102).cuboid(-8.0F, -4.0F, -15.8F, 16.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone4 = bone3.addChild("bone4", ModelPartBuilder.create().uv(47, 102).cuboid(-8.0F, -4.0F, -15.8F, 16.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone5 = bone4.addChild("bone5", ModelPartBuilder.create().uv(47, 102).cuboid(-8.0F, -4.0F, -15.8F, 16.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone6 = bone5.addChild("bone6", ModelPartBuilder.create().uv(47, 102).cuboid(-8.0F, -4.0F, -15.8F, 16.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone208 = base_console.addChild("bone208", ModelPartBuilder.create().uv(81, 27).cuboid(-7.0F, -1.0F, -11.8F, 14.0F, 1.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bone209 = bone208.addChild("bone209", ModelPartBuilder.create().uv(81, 27).cuboid(-7.0F, -1.0F, -11.8F, 14.0F, 1.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone210 = bone209.addChild("bone210", ModelPartBuilder.create().uv(81, 27).cuboid(-7.0F, -1.0F, -11.8F, 14.0F, 1.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone211 = bone210.addChild("bone211", ModelPartBuilder.create().uv(81, 27).cuboid(-7.0F, -1.0F, -11.8F, 14.0F, 1.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone212 = bone211.addChild("bone212", ModelPartBuilder.create().uv(81, 27).cuboid(-7.0F, -1.0F, -11.8F, 14.0F, 1.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone213 = bone212.addChild("bone213", ModelPartBuilder.create().uv(81, 27).cuboid(-7.0F, -1.0F, -11.8F, 14.0F, 1.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData rotor = base_console.addChild("rotor", ModelPartBuilder.create().uv(0, 44).cuboid(-11.0F, -42.0F, -11.0F, 22.0F, 1.0F, 22.0F, new Dilation(0.0F))
                .uv(52, 111).cuboid(-3.5F, -53.5F, -3.5F, 7.0F, 2.0F, 7.0F, new Dilation(0.0F))
                .uv(27, 110).cuboid(-3.0F, -55.0F, -3.0F, 6.0F, 13.0F, 6.0F, new Dilation(0.0F))
                .uv(135, 48).cuboid(-2.0F, -56.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(52, 111).cuboid(-3.5F, -47.5F, -3.5F, 7.0F, 2.0F, 7.0F, new Dilation(0.0F))
                .uv(52, 111).cuboid(-3.5F, -50.5F, -3.5F, 7.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData bone138 = rotor.addChild("bone138", ModelPartBuilder.create().uv(81, 27).cuboid(-1.5F, -7.475F, -8.775F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F))
                .uv(97, 0).cuboid(-0.5F, -8.975F, -7.775F, 1.0F, 11.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -43.5F, 0.0F));

        ModelPartData bone139 = bone138.addChild("bone139", ModelPartBuilder.create().uv(81, 27).cuboid(-1.5F, -7.475F, -8.775F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F))
                .uv(97, 0).cuboid(-0.5F, -8.975F, -7.775F, 1.0F, 11.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone140 = bone139.addChild("bone140", ModelPartBuilder.create().uv(81, 27).cuboid(-1.5F, -7.475F, -8.775F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F))
                .uv(97, 0).cuboid(-0.5F, -8.975F, -7.775F, 1.0F, 11.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone141 = bone140.addChild("bone141", ModelPartBuilder.create().uv(81, 27).cuboid(-1.5F, -7.475F, -8.775F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F))
                .uv(97, 0).cuboid(-0.5F, -8.975F, -7.775F, 1.0F, 11.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone142 = bone141.addChild("bone142", ModelPartBuilder.create().uv(81, 27).cuboid(-1.5F, -7.475F, -8.775F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F))
                .uv(97, 0).cuboid(-0.5F, -8.975F, -7.775F, 1.0F, 11.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone143 = bone142.addChild("bone143", ModelPartBuilder.create().uv(81, 27).cuboid(-1.5F, -7.475F, -8.775F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F))
                .uv(97, 0).cuboid(-0.5F, -8.975F, -7.775F, 1.0F, 11.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData glow = base_console.addChild("glow", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData NORTH2 = glow.addChild("NORTH2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -30.0F, 0.0F));

        ModelPartData bone31 = NORTH2.addChild("bone31", ModelPartBuilder.create().uv(145, 54).cuboid(-9.5F, 0.524F, -0.4235F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(145, 54).cuboid(-6.75F, 0.524F, -0.4235F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(145, 54).cuboid(7.5F, 0.524F, -0.4235F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(145, 54).cuboid(4.75F, 0.524F, -0.4235F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 140).cuboid(1.0F, 1.024F, 0.5765F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 140).cuboid(-2.0F, 1.024F, 0.5765F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData bone178 = bone31.addChild("bone178", ModelPartBuilder.create(), ModelTransform.of(-9.0F, 0.524F, 0.0765F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone179 = bone31.addChild("bone179", ModelPartBuilder.create(), ModelTransform.of(-6.25F, 0.524F, 0.0765F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone180 = bone31.addChild("bone180", ModelPartBuilder.create(), ModelTransform.of(5.25F, 0.524F, 0.0765F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone181 = bone31.addChild("bone181", ModelPartBuilder.create(), ModelTransform.of(8.0F, 0.524F, 0.0765F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone182 = bone31.addChild("bone182", ModelPartBuilder.create().uv(37, 98).cuboid(0.0F, -0.75F, -2.5F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-7.75F, 1.274F, 6.0765F, 0.0F, 0.0F, 0.6109F));

        ModelPartData bone183 = bone31.addChild("bone183", ModelPartBuilder.create().uv(37, 98).mirrored().cuboid(-1.0F, -0.75F, -2.5F, 1.0F, 1.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(7.75F, 1.274F, 6.0765F, 0.0F, 0.0F, -0.6109F));

        ModelPartData bone184 = bone31.addChild("bone184", ModelPartBuilder.create().uv(79, 138).cuboid(0.0F, -0.75F, -2.5F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-8.75F, 1.274F, 6.0765F, 0.0F, 0.0F, 0.6109F));

        ModelPartData bone185 = bone31.addChild("bone185", ModelPartBuilder.create().uv(79, 138).mirrored().cuboid(-1.0F, -0.75F, -2.5F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(8.75F, 1.274F, 6.0765F, 0.0F, 0.0F, -0.6109F));

        ModelPartData NORTH_WEST2 = glow.addChild("NORTH_WEST2", ModelPartBuilder.create(), ModelTransform.of(0.0F, -30.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData northwestcontrolpanel2 = NORTH_WEST2.addChild("northwestcontrolpanel2", ModelPartBuilder.create().uv(144, 136).cuboid(8.5F, -0.226F, 0.0765F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData wheatleyeye = northwestcontrolpanel2.addChild("wheatleyeye", ModelPartBuilder.create().uv(52, 121).cuboid(-1.0F, -1.0F, -4.25F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.274F, 6.0765F, -0.9163F, 0.0F, 0.0F));

        ModelPartData bone189 = northwestcontrolpanel2.addChild("bone189", ModelPartBuilder.create().uv(94, 119).cuboid(-4.0F, 0.0F, -5.0F, 4.0F, 1.0F, 9.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, 1.024F, 6.0765F, 0.0F, 0.0F, 0.3927F));

        ModelPartData bone190 = northwestcontrolpanel2.addChild("bone190", ModelPartBuilder.create().uv(94, 119).mirrored().cuboid(0.0F, 0.0F, -5.0F, 4.0F, 1.0F, 9.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(4.0F, 1.024F, 6.0765F, 0.0F, 0.0F, -0.3927F));

        ModelPartData SOUTH_WEST2 = glow.addChild("SOUTH_WEST2", ModelPartBuilder.create(), ModelTransform.of(0.0F, -30.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

        ModelPartData bone188 = SOUTH_WEST2.addChild("bone188", ModelPartBuilder.create().uv(136, 37).cuboid(-10.0F, 1.274F, 3.0765F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(86, 81).cuboid(-1.0F, 1.024F, 1.5765F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(98, 42).cuboid(1.0F, 1.024F, 9.0765F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(9, 132).cuboid(-1.5F, 0.024F, 9.0765F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(98, 42).cuboid(-2.0F, 1.024F, 9.0765F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(16, 59).cuboid(4.0F, 0.774F, 4.0765F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(16, 59).cuboid(-5.0F, 0.774F, 4.0765F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(144, 61).cuboid(-9.5F, 1.024F, 3.5765F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(144, 61).cuboid(-7.25F, 1.024F, 7.5765F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(136, 37).cuboid(-7.75F, 1.274F, 7.0765F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(144, 61).mirrored().cuboid(5.25F, 1.024F, 7.5765F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(136, 37).mirrored().cuboid(4.75F, 1.274F, 7.0765F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(144, 61).mirrored().cuboid(7.5F, 1.024F, 3.5765F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)).mirrored(false)
                .uv(136, 37).mirrored().cuboid(7.0F, 1.274F, 3.0765F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData SOUTH2 = glow.addChild("SOUTH2", ModelPartBuilder.create(), ModelTransform.of(0.0F, -30.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData bone191 = SOUTH2.addChild("bone191", ModelPartBuilder.create().uv(98, 61).cuboid(-6.5F, 1.524F, 10.0765F, 13.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(32, 130).cuboid(-5.0F, 0.274F, 10.5765F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(32, 130).cuboid(-1.0F, 0.274F, 10.5765F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(32, 130).cuboid(3.0F, 0.274F, 10.5765F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 140).cuboid(-2.0F, 1.024F, 3.5765F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 140).cuboid(1.0F, 1.024F, 3.5765F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(46, 111).cuboid(-9.0F, 1.024F, 0.0765F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData bone192 = bone191.addChild("bone192", ModelPartBuilder.create(), ModelTransform.of(-7.5F, 1.274F, 1.5765F, 0.0F, -0.7854F, 0.0F));

        ModelPartData SOUTH_EAST2 = glow.addChild("SOUTH_EAST2", ModelPartBuilder.create(), ModelTransform.of(0.0F, -30.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

        ModelPartData bone193 = SOUTH_EAST2.addChild("bone193", ModelPartBuilder.create().uv(123, 37).cuboid(-6.0F, 1.274F, 7.5765F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(101, 94).cuboid(2.0F, 0.524F, 7.5765F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(45, 98).cuboid(3.0F, 0.024F, 10.5765F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(98, 47).cuboid(-5.0F, 0.524F, 10.5765F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(86, 94).cuboid(-4.5F, 0.274F, 11.0765F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(131, 86).cuboid(5.0F, 1.024F, -0.4235F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(131, 86).cuboid(8.0F, 1.024F, -0.4235F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(11, 138).cuboid(0.5F, 1.024F, 0.5765F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(131, 86).cuboid(-6.0F, 1.024F, -0.4235F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(131, 86).cuboid(-9.0F, 1.024F, -0.4235F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData bone194 = bone193.addChild("bone194", ModelPartBuilder.create(), ModelTransform.of(3.5F, 0.024F, 11.0765F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone195 = bone193.addChild("bone195", ModelPartBuilder.create(), ModelTransform.of(2.5F, 0.524F, 7.8265F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone196 = bone193.addChild("bone196", ModelPartBuilder.create(), ModelTransform.of(4.0F, 1.024F, 8.8265F, 0.7854F, 0.0F, 0.0F));

        ModelPartData bone197 = bone193.addChild("bone197", ModelPartBuilder.create(), ModelTransform.of(-0.75F, 1.774F, 2.5765F, 0.0F, 0.0F, -0.7854F));

        ModelPartData NORTH_EAST2 = glow.addChild("NORTH_EAST2", ModelPartBuilder.create(), ModelTransform.of(0.0F, -30.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

        ModelPartData bone198 = NORTH_EAST2.addChild("bone198", ModelPartBuilder.create().uv(0, 98).cuboid(8.5F, 0.774F, 1.0765F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(86, 75).cuboid(6.5F, 0.774F, 0.0765F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(96, 116).cuboid(3.5F, 0.524F, 11.5765F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(106, 0).cuboid(-5.5F, 0.524F, 1.0765F, 11.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(45, 98).cuboid(-1.0F, 0.024F, 11.5765F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData bone199 = bone198.addChild("bone199", ModelPartBuilder.create(), ModelTransform.of(-8.5F, 0.774F, 3.0765F, 0.7854F, 0.0F, 0.0F));

        ModelPartData bone200 = bone198.addChild("bone200", ModelPartBuilder.create().uv(12, 12).cuboid(2.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.5F, 1.524F, 10.8265F, 0.7854F, 0.0F, 0.0F));

        ModelPartData bone201 = bone198.addChild("bone201", ModelPartBuilder.create(), ModelTransform.of(3.5F, 0.024F, 12.0765F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone202 = glow.addChild("bone202", ModelPartBuilder.create().uv(41, 68).cuboid(-5.0F, 1.0F, -12.55F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -44.5F, 0.0F));

        ModelPartData bone203 = bone202.addChild("bone203", ModelPartBuilder.create().uv(41, 68).cuboid(-5.0F, 1.0F, -12.55F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone204 = bone203.addChild("bone204", ModelPartBuilder.create().uv(41, 68).cuboid(-5.0F, 1.0F, -12.55F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone205 = bone204.addChild("bone205", ModelPartBuilder.create().uv(41, 68).cuboid(-5.0F, 1.0F, -12.55F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone206 = bone205.addChild("bone206", ModelPartBuilder.create().uv(41, 68).cuboid(-5.0F, 1.0F, -12.55F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData bone207 = bone206.addChild("bone207", ModelPartBuilder.create().uv(41, 68).cuboid(-5.0F, 1.0F, -12.55F, 10.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData NORTH = base_console.addChild("NORTH", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -30.0F, 0.0F));

        ModelPartData northcontrolpanel = NORTH.addChild("northcontrolpanel", ModelPartBuilder.create().uv(130, 108).cuboid(4.0F, 1.274F, -0.9235F, 6.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(130, 108).cuboid(-10.0F, 1.274F, -0.9235F, 6.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(41, 92).cuboid(-6.0F, 1.274F, 3.0765F, 12.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(103, 111).cuboid(-5.0F, 0.774F, 7.0765F, 10.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(81, 16).cuboid(-3.5F, 0.274F, 10.5765F, 7.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(45, 138).cuboid(-3.0F, 0.774F, 8.5765F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(145, 141).cuboid(-0.5F, 0.524F, 8.0765F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(41, 84).cuboid(-5.0F, 1.274F, 7.0765F, 10.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(81, 11).cuboid(-3.0F, 1.274F, 0.0765F, 6.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(117, 143).cuboid(-9.0F, 1.274F, 3.0765F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(8, 144).cuboid(-8.0F, 1.274F, 6.0765F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(117, 143).mirrored().cuboid(7.0F, 1.274F, 3.0765F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(8, 144).mirrored().cuboid(7.0F, 1.274F, 6.0765F, 1.0F, 1.0F, 3.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData door_control = northcontrolpanel.addChild("door_control", ModelPartBuilder.create().uv(96, 142).cuboid(-2.5F, -4.476F, -28.7235F, 5.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 4.0F, 29.8F));

        ModelPartData bone137 = northcontrolpanel.addChild("bone137", ModelPartBuilder.create().uv(67, 44).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-9.0F, 0.524F, 0.0765F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone159 = northcontrolpanel.addChild("bone159", ModelPartBuilder.create().uv(67, 44).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-6.25F, 0.524F, 0.0765F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone160 = northcontrolpanel.addChild("bone160", ModelPartBuilder.create().uv(67, 44).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(5.25F, 0.524F, 0.0765F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone161 = northcontrolpanel.addChild("bone161", ModelPartBuilder.create().uv(67, 44).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(8.0F, 0.524F, 0.0765F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone162 = northcontrolpanel.addChild("bone162", ModelPartBuilder.create(), ModelTransform.of(-7.75F, 1.274F, 6.0765F, 0.0F, 0.0F, 0.6109F));

        ModelPartData bone163 = northcontrolpanel.addChild("bone163", ModelPartBuilder.create(), ModelTransform.of(7.75F, 1.274F, 6.0765F, 0.0F, 0.0F, -0.6109F));

        ModelPartData bone164 = northcontrolpanel.addChild("bone164", ModelPartBuilder.create(), ModelTransform.of(-8.75F, 1.274F, 6.0765F, 0.0F, 0.0F, 0.6109F));

        ModelPartData bone165 = northcontrolpanel.addChild("bone165", ModelPartBuilder.create(), ModelTransform.of(8.75F, 1.274F, 6.0765F, 0.0F, 0.0F, -0.6109F));

        ModelPartData NORTH_WEST = base_console.addChild("NORTH_WEST", ModelPartBuilder.create(), ModelTransform.of(0.0F, -30.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

        ModelPartData northwestcontrolpanel = NORTH_WEST.addChild("northwestcontrolpanel", ModelPartBuilder.create().uv(0, 84).cuboid(-7.0F, 1.024F, 0.0765F, 14.0F, 1.0F, 12.0F, new Dilation(0.0F))
                .uv(0, 12).cuboid(8.0F, 0.524F, -0.9235F, 3.0F, 2.0F, 5.0F, new Dilation(0.0F))
                .uv(76, 102).cuboid(3.0F, 1.024F, 0.0765F, 4.0F, 1.0F, 12.0F, new Dilation(0.0F))
                .uv(133, 101).cuboid(-2.5F, -0.976F, 8.5765F, 5.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData monitor = northwestcontrolpanel.addChild("monitors", ModelPartBuilder.create().uv(100, 94).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
                .uv(98, 42).cuboid(-4.5F, -4.5F, -4.5F, 9.0F, 9.0F, 9.0F, new Dilation(0.0F))
                .uv(70, 138).cuboid(4.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F))
                .uv(36, 138).cuboid(-6.0F, -2.0F, -2.0F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.274F, 6.0765F, -0.9163F, 0.0F, 0.0F));

        ModelPartData bone131 = northwestcontrolpanel.addChild("bone131", ModelPartBuilder.create().uv(68, 84).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(9.5F, 1.274F, 4.0765F, 0.7854F, 0.0F, 0.0F));

        ModelPartData bone129 = northwestcontrolpanel.addChild("bone129", ModelPartBuilder.create(), ModelTransform.of(-4.0F, 1.024F, 6.0765F, 0.0F, 0.0F, 0.3927F));

        ModelPartData bone130 = northwestcontrolpanel.addChild("bone130", ModelPartBuilder.create(), ModelTransform.of(4.0F, 1.024F, 6.0765F, 0.0F, 0.0F, -0.3927F));

        ModelPartData SOUTH_WEST = base_console.addChild("SOUTH_WEST", ModelPartBuilder.create(), ModelTransform.of(0.0F, -30.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

        ModelPartData bone133 = SOUTH_WEST.addChild("bone133", ModelPartBuilder.create().uv(0, 68).cuboid(-9.0F, 1.524F, -0.9235F, 18.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(126, 42).cuboid(-5.0F, 1.274F, 1.0765F, 10.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(145, 129).cuboid(-4.0F, 0.274F, 1.5765F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(137, 5).cuboid(-3.0F, 1.524F, 6.0765F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(116, 125).cuboid(-4.5F, 1.024F, 8.0765F, 9.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(125, 94).cuboid(-4.5F, 1.524F, 8.0765F, 9.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(134, 138).cuboid(-1.5F, -0.976F, 9.0765F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(106, 143).cuboid(-7.75F, 1.524F, 4.0765F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(106, 143).mirrored().cuboid(5.75F, 1.524F, 4.0765F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(0, 44).cuboid(-6.75F, -0.976F, 8.0765F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 44).mirrored().cuboid(5.75F, -0.976F, 8.0765F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData bone134 = bone133.addChild("bone134", ModelPartBuilder.create().uv(111, 132).cuboid(-2.5F, -0.5F, -2.0F, 5.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 1.524F, 5.0765F, 0.7854F, 0.0F, 0.0F));

        ModelPartData bone135 = bone133.addChild("bone135", ModelPartBuilder.create().uv(145, 129).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(3.0F, 0.774F, 2.5765F, 0.0F, -0.829F, 0.0F));

        ModelPartData SOUTH = base_console.addChild("SOUTH", ModelPartBuilder.create(), ModelTransform.of(0.0F, -30.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData southcontrolpanel = SOUTH.addChild("southcontrolpanel", ModelPartBuilder.create().uv(121, 138).cuboid(-1.5F, 1.024F, 10.0765F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(108, 138).cuboid(-5.5F, 1.024F, 10.0765F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(108, 138).mirrored().cuboid(2.5F, 1.024F, 10.0765F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)).mirrored(false)
                .uv(41, 87).mirrored().cuboid(1.5F, 1.024F, 11.0765F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)).mirrored(false)
                .uv(41, 87).cuboid(-2.5F, 1.024F, 11.0765F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(81, 11).cuboid(-3.0F, 1.274F, 3.0765F, 6.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 59).cuboid(5.0F, 1.274F, -0.9235F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(137, 0).cuboid(6.0F, 0.774F, 0.0765F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(0, 59).cuboid(-10.0F, 1.274F, -0.9235F, 5.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(41, 84).cuboid(-8.0F, 0.274F, 1.0765F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(67, 62).cuboid(-4.0F, 1.524F, 0.0765F, 8.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(41, 92).cuboid(-6.0F, 1.274F, 6.0765F, 12.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData XYZmod = southcontrolpanel.addChild("XYZmod", ModelPartBuilder.create().uv(96, 142).cuboid(-2.5F, -4.476F, -28.7235F, 5.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 4.0F, 32.8F));

        ModelPartData randomiser = southcontrolpanel.addChild("randomiser", ModelPartBuilder.create().uv(138, 10).cuboid(-1.5F, -0.5F, -1.5F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-7.5F, 1.274F, 1.5765F, 0.0F, -0.7854F, 0.0F));

        ModelPartData land_type = southcontrolpanel.addChild("land_type", ModelPartBuilder.create().uv(143, 69).cuboid(-2.0F, -3.226F, -29.7235F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 12).cuboid(-1.0F, -3.726F, -29.7235F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 2.5F, 30.8F));

        ModelPartData SOUTH_EAST = base_console.addChild("SOUTH_EAST", ModelPartBuilder.create(), ModelTransform.of(0.0F, -30.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

        ModelPartData southeastcontrolpanel = SOUTH_EAST.addChild("southeastcontrolpanel", ModelPartBuilder.create().uv(120, 81).cuboid(-5.5F, 1.024F, 10.0765F, 11.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(140, 125).cuboid(1.0F, 1.024F, 7.0765F, 4.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(45, 71).cuboid(-6.0F, 1.524F, 7.0765F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(52, 121).cuboid(4.0F, 1.274F, -0.9235F, 6.0F, 1.0F, 7.0F, new Dilation(0.0F))
                .uv(135, 54).cuboid(0.0F, 1.274F, 0.0765F, 2.0F, 1.0F, 5.0F, new Dilation(0.0F))
                .uv(0, 59).cuboid(0.5F, 0.024F, 2.5765F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
                .uv(67, 47).cuboid(-1.5F, 0.774F, 7.5765F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(140, 86).cuboid(-2.0F, 1.274F, 0.5765F, 1.0F, 1.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 132).cuboid(-3.5F, 1.274F, -0.4235F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 132).cuboid(2.5F, 1.274F, -0.4235F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(52, 121).cuboid(-10.0F, 1.274F, -0.9235F, 6.0F, 1.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData handbrake = southeastcontrolpanel.addChild("handbrake", ModelPartBuilder.create().uv(136, 113).cuboid(-2.5F, -4.0F, -0.5F, 5.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-7.0F, 1.524F, 2.5765F, -1.309F, 0.0F, 0.0F));

        ModelPartData throttle = southeastcontrolpanel.addChild("throttle", ModelPartBuilder.create().uv(96, 142).cuboid(4.5F, -4.476F, -32.7235F, 5.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 4.0F, 32.8F));

        ModelPartData bone173 = southeastcontrolpanel.addChild("bone173", ModelPartBuilder.create().uv(128, 143).cuboid(-1.0F, -0.5F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.75F, 1.774F, 2.5765F, 0.0F, 0.0F, -0.7854F));

        ModelPartData bone171 = southeastcontrolpanel.addChild("bone171", ModelPartBuilder.create().uv(67, 44).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.5F, 0.024F, 11.0765F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone172 = southeastcontrolpanel.addChild("bone172", ModelPartBuilder.create().uv(67, 44).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.5F, 0.524F, 7.8265F, 0.6109F, 0.0F, 0.0F));

        ModelPartData bone170 = southeastcontrolpanel.addChild("bone170", ModelPartBuilder.create().uv(0, 103).cuboid(-2.0F, -0.5F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(4.0F, 1.024F, 8.8265F, 0.7854F, 0.0F, 0.0F));

        ModelPartData NORTH_EAST = base_console.addChild("NORTH_EAST", ModelPartBuilder.create(), ModelTransform.of(0.0F, -30.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

        ModelPartData bone174 = NORTH_EAST.addChild("bone174", ModelPartBuilder.create().uv(0, 98).cuboid(-6.5F, 1.024F, 0.0765F, 13.0F, 1.0F, 10.0F, new Dilation(0.0F))
                .uv(36, 130).cuboid(-10.5F, 1.024F, -0.9235F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(49, 141).cuboid(-9.5F, 0.274F, 0.0765F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(125, 61).cuboid(4.5F, 1.024F, -0.9235F, 6.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(67, 57).cuboid(-5.5F, 0.774F, 11.0765F, 7.0F, 1.0F, 3.0F, new Dilation(0.0F))
                .uv(143, 119).cuboid(-5.0F, 0.274F, 11.5765F, 3.0F, 1.0F, 2.0F, new Dilation(0.0F))
                .uv(143, 15).cuboid(3.0F, 0.774F, 11.0765F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -4.0F, -27.8F, 0.48F, 0.0F, 0.0F));

        ModelPartData bone175 = bone174.addChild("bone175", ModelPartBuilder.create().uv(0, 103).cuboid(-1.0F, -0.5F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-8.5F, 0.774F, 3.0765F, 0.7854F, 0.0F, 0.0F));

        ModelPartData bone176 = bone174.addChild("bone176", ModelPartBuilder.create().uv(0, 103).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-3.5F, 1.524F, 10.8265F, 0.7854F, 0.0F, 0.0F));

        ModelPartData bone177 = bone174.addChild("bone177", ModelPartBuilder.create().uv(67, 44).cuboid(-4.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.5F, 0.024F, 12.0765F, 0.6109F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 256, 256);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        this.base_console.render(matrices, vertices, light, overlay, 1, 1, 1, 1);
    }

    @Override
    public void renderWithAnimations(ConsoleBlockEntity console, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
        if (console.getTardis() == null) return;
        matrices.push();
        // fixme id do it but i genuinely dont want to bc i cba

        ModelPart southEastControls = this.base_console.getChild("SOUTH_EAST").getChild("southeastcontrolpanel");
        ModelPart northControls = this.base_console.getChild("NORTH").getChild("northcontrolpanel");
        ModelPart southControls = this.base_console.getChild("SOUTH").getChild("southcontrolpanel");

        boolean isInFlight = console.getTardis().getTravel().getState() == TardisTravel.State.DEMAT || console.getTardis().getTravel().getState() == TardisTravel.State.FLIGHT;
        boolean isHandbrakeActive = PropertiesHandler.getBool(console.getTardis().getHandlers().getProperties(), PropertiesHandler.HANDBRAKE);
        boolean leftDoor = console.getTardis().getDoor().getDoorState() == DoorData.DoorStateEnum.FIRST;
        boolean rightDoor = console.getTardis().getDoor().getDoorState() == DoorData.DoorStateEnum.SECOND;
        boolean locked = console.getTardis().getDoor().locked();
        boolean isUpOrDown = PropertiesHandler.getBool(console.getTardis().getHandlers().getProperties(), PropertiesHandler.FIND_GROUND);

        int increment = console.getTardis().getTravel().getPosManager().increment;
        float throttleZ = southEastControls.getChild("throttle").pivotZ;
        float doorZ = northControls.getChild("door_control").pivotZ;
        float doorY = northControls.getChild("door_control").pivotY;
        float incrementModZ = southControls.getChild("XYZmod").pivotZ;
        float landTypeY = southControls.getChild("land_type").pivotY;
        Vector3f handbrakeRotation = new Vector3f(isHandbrakeActive ? 0 : -1.309F * -2, 0, 0);
        southEastControls.getChild("throttle").pivotZ = isInFlight ? throttleZ + 3f : throttleZ;
        southEastControls.getChild("handbrake").rotate(handbrakeRotation);
        northControls.getChild("door_control").pivotZ = leftDoor ? doorZ + 1 : rightDoor ? doorZ + 2 : doorZ;
        northControls.getChild("door_control").pivotY = locked ? doorY + 1 : doorY;
        southControls.getChild("XYZmod").pivotZ = increment == 10 ? incrementModZ + 1 : increment == 100 ? incrementModZ + 2 : increment == 1000 ? incrementModZ + 3 : incrementModZ;
        southControls.getChild("land_type").pivotY = isUpOrDown ? landTypeY : landTypeY + 1;
        matrices.pop();

        matrices.push();

        matrices.translate(0.5f, -0.75f, -0.5f);
        matrices.scale(0.5f, 0.5f, 0.5f);

        super.renderWithAnimations(console, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);

        matrices.pop();
    }

    @Override
    public ModelPart getPart() {
        return this.base_console;
    }

    @Override
    public Animation getAnimationForState(TardisTravel.State state) {
        return switch (state) {
            case LANDED -> BorealisAnimations.CONSOLE_IDLE_WHEATLEY;
            case DEMAT -> BorealisAnimations.CONSOLE_ROTOR_DEMATERIALIZE;
            case FLIGHT, CRASH -> BorealisAnimations.CONSOLE_ROTOR_INFLIGHT;
            case MAT -> BorealisAnimations.CONSOLE_ROTOR_MATERIALIZE;
        };
    }
}