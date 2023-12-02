package mdteam.ait.client.models.exteriors;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class FalloutExteriorModel extends ExteriorModel {
	public static final Identifier EXTERIOR_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/exteriors/shelter.png"));
	public static final Identifier EXTERIOR_TEXTURE_EMISSION = new Identifier(AITMod.MOD_ID, "textures/blockentities/exteriors/shelter_emission.png");

	public ModelPart tardis;
	public ModelPart door;
	public ModelPart frame;
	public ModelPart bone37;
	public ModelPart bone38;
	public ModelPart bone39;
	public ModelPart bone40;
	public ModelPart bone41;
	public ModelPart bone42;
	public ModelPart bone7;
	public ModelPart bone8;
	public ModelPart bone9;
	public ModelPart bone10;
	public ModelPart bone11;
	public ModelPart bone12;
	public ModelPart bone31;
	public ModelPart bone32;
	public ModelPart bone33;
	public ModelPart bone34;
	public ModelPart bone35;
	public ModelPart bone36;
	public ModelPart bone25;
	public ModelPart bone26;
	public ModelPart bone27;
	public ModelPart bone28;
	public ModelPart bone29;
	public ModelPart bone30;
	public ModelPart bone19;
	public ModelPart bone20;
	public ModelPart bone21;
	public ModelPart bone22;
	public ModelPart bone23;
	public ModelPart bone24;
	public ModelPart bone43;
	public ModelPart bone44;
	public ModelPart bone45;
	public ModelPart bone46;
	public ModelPart bone47;
	public ModelPart bone48;
	public ModelPart bone13;
	public ModelPart bone14;
	public ModelPart bone15;
	public ModelPart bone16;
	public ModelPart bone17;
	public ModelPart bone18;
	public ModelPart bone;
	public ModelPart bone2;
	public ModelPart bone3;
	public ModelPart bone4;
	public ModelPart bone5;
	public ModelPart bone6;
	public ModelPart glow;
	public ModelPart bone49;
	public ModelPart bone50;
	public ModelPart bone51;
	public ModelPart bone52;
	public ModelPart bone53;
	public ModelPart bone54;
	public FalloutExteriorModel(ModelPart root) {
		super(RenderLayer::getEntityTranslucent);
		this.tardis = root.getChild("tardis");
		this.door = this.tardis.getChild("door");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData tardis = modelPartData.addChild("tardis", ModelPartBuilder.create().uv(2, 92).cuboid(-5.5F, -34.025F, -10.0F, 11.0F, 32.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData door = tardis.addChild("door", ModelPartBuilder.create().uv(24, 54).cuboid(-5.5F, -16.0F, -8.475F, 11.0F, 32.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -18.0F, -2.0F));

		ModelPartData frame = tardis.addChild("frame", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bone37 = frame.addChild("bone37", ModelPartBuilder.create().uv(4, 4).cuboid(-1.0F, -2.0F, 11.7F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone38 = bone37.addChild("bone38", ModelPartBuilder.create().uv(4, 4).cuboid(-1.0F, -2.0F, 11.7F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone39 = bone38.addChild("bone39", ModelPartBuilder.create().uv(4, 4).cuboid(-1.0F, -2.0F, 11.7F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone40 = bone39.addChild("bone40", ModelPartBuilder.create().uv(4, 4).cuboid(-1.0F, -2.0F, 11.7F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone41 = bone40.addChild("bone41", ModelPartBuilder.create().uv(4, 4).cuboid(-1.0F, -2.0F, 11.7F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone42 = bone41.addChild("bone42", ModelPartBuilder.create().uv(4, 4).cuboid(-1.0F, -2.0F, 11.7F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone7 = frame.addChild("bone7", ModelPartBuilder.create().uv(24, 47).cuboid(-5.5F, -34.0F, 10.525F, 11.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(52, 33).cuboid(-5.5F, -23.0F, 10.025F, 11.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(52, 33).cuboid(-5.5F, -14.0F, 10.025F, 11.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(24, 42).cuboid(-5.5F, -4.0F, 10.025F, 11.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(60, 60).cuboid(-5.5F, -11.0F, 10.525F, 11.0F, 7.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 0.0F));

		ModelPartData bone8 = bone7.addChild("bone8", ModelPartBuilder.create().uv(24, 47).cuboid(-5.5F, -34.0F, 10.525F, 11.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(52, 33).cuboid(-5.5F, -23.0F, 10.025F, 11.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(52, 33).cuboid(-5.5F, -14.0F, 10.025F, 11.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(24, 42).cuboid(-5.5F, -4.0F, 10.025F, 11.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone9 = bone8.addChild("bone9", ModelPartBuilder.create().uv(24, 47).cuboid(-5.5F, -34.0F, 10.525F, 11.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(52, 33).cuboid(-5.5F, -23.0F, 10.025F, 11.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(52, 33).cuboid(-5.5F, -14.0F, 10.025F, 11.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(24, 42).cuboid(-5.5F, -4.0F, 10.025F, 11.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone10 = bone9.addChild("bone10", ModelPartBuilder.create().uv(77, 32).cuboid(-5.5F, -34.0F, 9.525F, 11.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone11 = bone10.addChild("bone11", ModelPartBuilder.create().uv(24, 47).cuboid(-5.5F, -34.0F, 10.525F, 11.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(52, 33).cuboid(-5.5F, -23.0F, 10.025F, 11.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(60, 71).cuboid(-3.5F, -22.0F, 10.025F, 7.0F, 10.0F, 4.0F, new Dilation(0.0F))
		.uv(76, 0).cuboid(-2.5F, -34.0F, 9.025F, 3.0F, 12.0F, 3.0F, new Dilation(0.0F))
		.uv(52, 33).cuboid(-5.5F, -14.0F, 10.025F, 11.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(24, 42).cuboid(-5.5F, -4.0F, 10.025F, 11.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone12 = bone11.addChild("bone12", ModelPartBuilder.create().uv(24, 47).cuboid(-5.5F, -34.0F, 10.525F, 11.0F, 4.0F, 1.0F, new Dilation(0.0F))
		.uv(52, 33).cuboid(-5.5F, -23.0F, 10.025F, 11.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(52, 33).cuboid(-5.5F, -14.0F, 10.025F, 11.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(24, 42).cuboid(-5.5F, -4.0F, 10.025F, 11.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone31 = frame.addChild("bone31", ModelPartBuilder.create().uv(48, 54).cuboid(-1.0F, -34.0F, 8.875F, 2.0F, 34.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -2.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone32 = bone31.addChild("bone32", ModelPartBuilder.create().uv(48, 54).cuboid(-1.0F, -34.0F, 8.875F, 2.0F, 34.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone33 = bone32.addChild("bone33", ModelPartBuilder.create().uv(50, 56).cuboid(-1.0F, -34.0F, 10.875F, 2.0F, 34.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone34 = bone33.addChild("bone34", ModelPartBuilder.create().uv(50, 56).cuboid(-1.0F, -34.0F, 10.875F, 2.0F, 34.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone35 = bone34.addChild("bone35", ModelPartBuilder.create().uv(48, 54).cuboid(-1.0F, -34.0F, 8.875F, 2.0F, 34.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone36 = bone35.addChild("bone36", ModelPartBuilder.create().uv(48, 54).cuboid(-1.0F, -34.0F, 8.875F, 2.0F, 34.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone25 = frame.addChild("bone25", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -8.0F, 12.85F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -36.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData bone26 = bone25.addChild("bone26", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -8.0F, 12.85F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone27 = bone26.addChild("bone27", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -8.0F, 12.85F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone28 = bone27.addChild("bone28", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -8.0F, 12.85F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone29 = bone28.addChild("bone29", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -8.0F, 12.85F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone30 = bone29.addChild("bone30", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -8.0F, 12.85F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone19 = frame.addChild("bone19", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, -8.0F, -0.775F, 13.0F, 8.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -36.0F, 0.0F));

		ModelPartData bone20 = bone19.addChild("bone20", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, -8.0F, -0.775F, 13.0F, 8.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone21 = bone20.addChild("bone21", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, -8.0F, -0.775F, 13.0F, 8.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone22 = bone21.addChild("bone22", ModelPartBuilder.create().uv(0, 21).cuboid(-6.5F, -8.0F, -0.775F, 13.0F, 8.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone23 = bone22.addChild("bone23", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, -8.0F, -0.775F, 13.0F, 8.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone24 = bone23.addChild("bone24", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, -8.0F, -0.775F, 13.0F, 8.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone43 = frame.addChild("bone43", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, -8.0F, -0.775F, 13.0F, 8.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -36.0F, 0.0F));

		ModelPartData bone44 = bone43.addChild("bone44", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, -8.0F, -0.775F, 13.0F, 8.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone45 = bone44.addChild("bone45", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, -8.0F, -0.775F, 13.0F, 8.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone46 = bone45.addChild("bone46", ModelPartBuilder.create().uv(0, 21).cuboid(-6.5F, -8.0F, -0.775F, 13.0F, 8.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone47 = bone46.addChild("bone47", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, -8.0F, -0.775F, 13.0F, 8.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone48 = bone47.addChild("bone48", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, -8.0F, -0.775F, 13.0F, 8.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone13 = frame.addChild("bone13", ModelPartBuilder.create().uv(0, 42).cuboid(-5.5F, -34.0F, 9.525F, 11.0F, 34.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -2.0F, 0.0F));

		ModelPartData bone14 = bone13.addChild("bone14", ModelPartBuilder.create().uv(0, 42).cuboid(-5.5F, -34.0F, 9.525F, 11.0F, 34.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone15 = bone14.addChild("bone15", ModelPartBuilder.create().uv(0, 42).cuboid(-5.5F, -34.0F, 9.525F, 11.0F, 34.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone16 = bone15.addChild("bone16", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone17 = bone16.addChild("bone17", ModelPartBuilder.create().uv(0, 42).cuboid(-5.5F, -34.0F, 9.525F, 11.0F, 34.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone18 = bone17.addChild("bone18", ModelPartBuilder.create().uv(0, 42).cuboid(-5.5F, -34.0F, 9.525F, 11.0F, 34.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone = frame.addChild("bone", ModelPartBuilder.create().uv(39, 39).cuboid(-6.0F, -2.0F, -0.625F, 12.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bone2 = bone.addChild("bone2", ModelPartBuilder.create().uv(39, 39).cuboid(-6.0F, -2.0F, -0.625F, 12.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone3 = bone2.addChild("bone3", ModelPartBuilder.create().uv(39, 39).cuboid(-6.0F, -2.0F, -0.625F, 12.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone4 = bone3.addChild("bone4", ModelPartBuilder.create().uv(39, 39).cuboid(-6.0F, -2.0F, -0.625F, 12.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone5 = bone4.addChild("bone5", ModelPartBuilder.create().uv(39, 39).cuboid(-6.0F, -2.0F, -0.625F, 12.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone6 = bone5.addChild("bone6", ModelPartBuilder.create().uv(39, 39).cuboid(-6.0F, -2.0F, -0.625F, 12.0F, 2.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData glow = tardis.addChild("glow", ModelPartBuilder.create().uv(56, 54).cuboid(-4.5F, 0.0F, -21.025F, 11.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-1.0F, -40.0F, 8.8F));

		ModelPartData bone49 = glow.addChild("bone49", ModelPartBuilder.create().uv(81, 38).cuboid(-5.5F, -34.0F, 11.55F, 11.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 38.0F, -8.8F));

		ModelPartData bone50 = bone49.addChild("bone50", ModelPartBuilder.create().uv(81, 38).cuboid(-5.5F, -34.0F, 11.55F, 11.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone51 = bone50.addChild("bone51", ModelPartBuilder.create().uv(81, 38).cuboid(-5.5F, -34.0F, 11.55F, 11.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone52 = bone51.addChild("bone52", ModelPartBuilder.create(), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone53 = bone52.addChild("bone53", ModelPartBuilder.create().uv(81, 38).cuboid(-5.5F, -34.0F, 11.55F, 11.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone54 = bone53.addChild("bone54", ModelPartBuilder.create().uv(81, 38).cuboid(-5.5F, -34.0F, 11.55F, 11.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		this.tardis.render(matrices, vertices, light, overlay,1, 1, 1, 1);
	}

	@Override
	public ModelPart getPart() {
		return tardis;
	}


	@Override
	public void renderWithAnimations(ExteriorBlockEntity exterior, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		this.door.yaw = exterior.getLeftDoorRotation();

		super.renderWithAnimations(exterior, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);
	}

	@Override
	public Identifier getTexture() {
		return EXTERIOR_TEXTURE;
	}

	@Override
	public Identifier getEmission() {
		return EXTERIOR_TEXTURE_EMISSION;
	}
}