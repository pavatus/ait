package mdteam.ait.client.models.consoles;

import mdteam.ait.client.animation.console.alnico.AlnicoAnimations;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.tardis.TardisTravel;
import mdteam.ait.tardis.control.impl.pos.IncrementManager;
import mdteam.ait.tardis.data.FuelData;
import mdteam.ait.tardis.data.properties.PropertiesHandler;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.util.math.MatrixStack;

public class AlnicoConsoleModel extends ConsoleModel {
	private final ModelPart alnico;
	public AlnicoConsoleModel(ModelPart root) {
		this.alnico = root.getChild("alnico");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData alnico = modelPartData.addChild("alnico", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

		ModelPartData section1 = alnico.addChild("section1", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

		ModelPartData desktop = section1.addChild("desktop", ModelPartBuilder.create().uv(128, 110).cuboid(-9.5F, -13.25F, -27.65F, 19.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r1 = desktop.addChild("cube_r1", ModelPartBuilder.create().uv(154, 81).cuboid(-8.0F, -4.0F, -8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.001F))
		.uv(7, 158).cuboid(7.0F, -4.0F, -8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.001F))
		.uv(237, 39).cuboid(6.0F, -4.0F, -8.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.001F))
		.uv(237, 81).cuboid(-7.0F, -4.0F, -8.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.001F))
		.uv(132, 22).cuboid(-6.0F, -4.0F, -8.0F, 12.0F, 2.0F, 8.0F, new Dilation(0.0F))
		.uv(128, 129).cuboid(-5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(130, 83).cuboid(4.0F, -4.0F, 0.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(161, 129).cuboid(-9.0F, -4.0F, -10.0F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(221, 101).cuboid(-4.0F, -4.0F, 0.0F, 8.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -12.0F, -14.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData pillars = section1.addChild("pillars", ModelPartBuilder.create().uv(210, 173).cuboid(-6.0F, -19.0F, -12.0F, 12.0F, 6.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r2 = pillars.addChild("cube_r2", ModelPartBuilder.create().uv(207, 26).cuboid(-2.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(10.9554F, -14.2676F, -27.1301F, -1.8326F, -0.3491F, 0.0F));

		ModelPartData cube_r3 = pillars.addChild("cube_r3", ModelPartBuilder.create().uv(208, 206).cuboid(-2.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(12.0F, -13.75F, -30.0F, -1.309F, -0.3491F, 0.0F));

		ModelPartData cube_r4 = pillars.addChild("cube_r4", ModelPartBuilder.create().uv(45, 207).cuboid(-1.0F, -10.0F, -2.5F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-6.6827F, -8.1365F, -18.3146F, -1.8326F, 0.3491F, 0.0F));

		ModelPartData cube_r5 = pillars.addChild("cube_r5", ModelPartBuilder.create().uv(0, 209).cuboid(0.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-12.0F, -13.75F, -30.0F, -1.309F, 0.3491F, 0.0F));

		ModelPartData top = section1.addChild("top", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r6 = top.addChild("cube_r6", ModelPartBuilder.create().uv(242, 220).cuboid(-1.5F, -19.0F, -12.0F, 3.0F, 4.0F, 3.0F, new Dilation(0.05F))
		.uv(54, 233).cuboid(-4.0F, -12.4F, -27.0F, 8.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r7 = top.addChild("cube_r7", ModelPartBuilder.create().uv(35, 45).cuboid(-4.0F, -4.0F, -28.9F, 8.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r8 = top.addChild("cube_r8", ModelPartBuilder.create().uv(100, 84).cuboid(-3.0F, -18.0F, -22.0F, 6.0F, 0.0F, 17.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData bottom = section1.addChild("bottom", ModelPartBuilder.create().uv(125, 178).cuboid(-7.0F, -7.0F, -15.0F, 14.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r9 = bottom.addChild("cube_r9", ModelPartBuilder.create().uv(217, 0).cuboid(-2.5F, -9.0F, -19.0F, 5.0F, 9.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -2.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r10 = bottom.addChild("cube_r10", ModelPartBuilder.create().uv(239, 200).cuboid(8.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(220, 239).cuboid(-11.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 82).cuboid(-8.0F, -3.0F, -1.0F, 16.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -9.0F, -24.7F, -0.2618F, 0.0F, 0.0F));

		ModelPartData controls = section1.addChild("controls", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData biglever = controls.addChild("biglever", ModelPartBuilder.create().uv(35, 243).cuboid(-1.0F, -2.0F, -3.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F))
		.uv(114, 40).cuboid(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.25F, -11.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData bone = biglever.addChild("bone", ModelPartBuilder.create().uv(9, 72).cuboid(-1.5F, -3.8F, -0.5F, 0.0F, 4.0F, 1.0F, new Dilation(0.001F))
		.uv(9, 66).cuboid(1.5F, -3.8F, -0.5F, 0.0F, 4.0F, 1.0F, new Dilation(0.001F))
		.uv(152, 47).cuboid(-1.5F, -3.8F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.001F))
		.uv(48, 28).cuboid(-0.5F, -6.8F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(149, 81).cuboid(0.5F, -3.8F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -0.2F, -1.0F));

		ModelPartData tinyswitch = controls.addChild("tinyswitch", ModelPartBuilder.create().uv(202, 26).cuboid(10.0F, -1.0F, -3.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-8.0F, -15.95F, -12.55F, 0.2618F, 0.0F, 0.0F));

		ModelPartData bone3 = tinyswitch.addChild("bone3", ModelPartBuilder.create().uv(9, 112).cuboid(-1.0F, -0.75F, 0.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(162, 99).cuboid(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(11.0F, -1.0F, -2.0F));

		ModelPartData tinyswitch2 = controls.addChild("tinyswitch2", ModelPartBuilder.create().uv(192, 155).cuboid(10.0F, -1.0F, -3.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-14.0F, -15.95F, -12.55F, 0.2618F, 0.0F, 0.0F));

		ModelPartData bone2 = tinyswitch2.addChild("bone2", ModelPartBuilder.create().uv(102, 21).cuboid(-1.0F, -0.75F, 0.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(161, 134).cuboid(-0.5F, -0.75F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(11.0F, -1.0F, -2.0F));

		ModelPartData sideswitches = controls.addChild("sideswitches", ModelPartBuilder.create().uv(170, 223).cuboid(-2.0F, -1.0F, -3.0F, 3.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(-7.5F, -14.7806F, -20.0173F, 0.2472F, 0.3594F, 0.0043F));

		ModelPartData sideswitch1 = sideswitches.addChild("sideswitch1", ModelPartBuilder.create().uv(192, 123).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(148, 36).cuboid(-1.0F, -2.0F, 0.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-0.5F, 0.0F, 2.5F));

		ModelPartData sideswitch2 = sideswitches.addChild("sideswitch2", ModelPartBuilder.create().uv(183, 155).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(76, 149).cuboid(-1.0F, -2.0F, 0.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-0.5F, 0.0F, -1.5F));

		ModelPartData geiger1 = controls.addChild("geiger1", ModelPartBuilder.create().uv(235, 242).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(4.25F, -14.0F, -18.0F));

		ModelPartData needle1 = geiger1.addChild("needle1", ModelPartBuilder.create().uv(74, 114).cuboid(-0.5F, -2.0F, -0.02F, 1.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-3.0F, 1.0F, -2.0F));

		ModelPartData geiger2 = controls.addChild("geiger2", ModelPartBuilder.create().uv(110, 242).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(1.75F, -14.0F, -18.0F));

		ModelPartData needle2 = geiger2.addChild("needle2", ModelPartBuilder.create().uv(111, 75).cuboid(-0.5F, -2.0F, -0.02F, 1.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(2.0F, 1.0F, -2.0F));

		ModelPartData multiswitchpanel = controls.addChild("multiswitchpanel", ModelPartBuilder.create().uv(177, 212).cuboid(-3.0F, -3.0F, -1.0F, 6.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.25F, -26.65F));

		ModelPartData longswitch1 = multiswitchpanel.addChild("longswitch1", ModelPartBuilder.create().uv(137, 22).cuboid(0.0F, -2.5F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.001F))
		.uv(154, 140).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.25F, -2.5F, -0.5F));

		ModelPartData longswitch2 = multiswitchpanel.addChild("longswitch2", ModelPartBuilder.create().uv(124, 26).cuboid(0.0F, -2.5F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.001F))
		.uv(157, 99).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.75F, -2.5F, -0.5F));

		ModelPartData longswitch3 = multiswitchpanel.addChild("longswitch3", ModelPartBuilder.create().uv(124, 21).cuboid(0.0F, -2.5F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.001F))
		.uv(133, 159).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.75F, -2.5F, -0.5F));

		ModelPartData longswitch4 = multiswitchpanel.addChild("longswitch4", ModelPartBuilder.create().uv(124, 16).cuboid(0.0F, -2.5F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.001F))
		.uv(161, 123).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.25F, -2.5F, -0.5F));

		ModelPartData fliplever1 = controls.addChild("fliplever1", ModelPartBuilder.create().uv(190, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-12.15F, -12.25F, -21.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData bone5 = fliplever1.addChild("bone5", ModelPartBuilder.create().uv(70, 45).cuboid(-0.5F, -7.0F, -1.0F, 1.0F, 8.0F, 2.0F, new Dilation(0.0F))
		.uv(153, 39).cuboid(-0.5F, -7.0F, 1.0F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(95, 21).cuboid(-1.0F, -6.5F, 1.5F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(39, 100).cuboid(0.0F, -10.5F, 1.5F, 0.0F, 5.0F, 3.0F, new Dilation(0.001F))
		.uv(118, 150).cuboid(0.5F, -7.0F, 1.0F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(153, 43).cuboid(0.0F, -9.0F, 0.0F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(48, 4).cuboid(-0.5F, -13.0F, 0.0F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, 0.5F));

		ModelPartData bell = controls.addChild("bell", ModelPartBuilder.create().uv(70, 0).cuboid(0.0F, -7.0F, -3.0F, 0.0F, 7.0F, 4.0F, new Dilation(0.001F)), ModelTransform.of(9.5F, -14.0F, -24.0F, 0.2618F, -0.3491F, 0.0F));

		ModelPartData bone4 = bell.addChild("bone4", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -6.0F, -3.0F));

		ModelPartData cube_r11 = bone4.addChild("cube_r11", ModelPartBuilder.create().uv(101, 40).cuboid(-1.5F, -3.0F, -6.0F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(111, 188).cuboid(-1.0F, -5.0F, -5.5F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 6.0F, 3.0F, -0.2618F, 0.0F, 0.0F));

		ModelPartData dial5 = controls.addChild("dial5", ModelPartBuilder.create().uv(121, 144).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(116, 144).cuboid(-0.5F, -0.5F, -0.9F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(71, 2).cuboid(-0.75F, -0.75F, -0.25F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(-3.0F, -13.75F, -21.15F, -1.309F, 0.0F, 0.0F));

		ModelPartData dial6 = controls.addChild("dial6", ModelPartBuilder.create().uv(76, 143).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(128, 134).cuboid(-0.5F, -0.5F, -0.9F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(60, 0).cuboid(-0.75F, -0.75F, -0.25F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(-1.25F, -13.75F, -21.15F, -1.309F, 0.0F, 0.0F));

		ModelPartData section2 = alnico.addChild("section2", ModelPartBuilder.create(), ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData desktop2 = section2.addChild("desktop2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r12 = desktop2.addChild("cube_r12", ModelPartBuilder.create().uv(132, 28).cuboid(-8.0F, -4.0F, -8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(132, 36).cuboid(7.0F, -4.0F, -8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(236, 233).cuboid(6.0F, -4.0F, -8.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(237, 29).cuboid(-7.0F, -4.0F, -8.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(132, 11).cuboid(-6.0F, -4.0F, -8.0F, 12.0F, 2.0F, 8.0F, new Dilation(0.0F))
		.uv(109, 93).cuboid(-5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(128, 118).cuboid(4.0F, -4.0F, 0.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(161, 118).cuboid(-9.0F, -4.0F, -10.0F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(218, 204).cuboid(-4.0F, -4.0F, 0.0F, 8.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -12.0F, -14.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData pillars2 = section2.addChild("pillars2", ModelPartBuilder.create().uv(210, 164).cuboid(-6.0F, -19.0F, -12.0F, 12.0F, 6.0F, 2.0F, new Dilation(0.0F))
		.uv(128, 102).cuboid(-9.5F, -13.25F, -27.65F, 19.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r13 = pillars2.addChild("cube_r13", ModelPartBuilder.create().uv(206, 96).cuboid(-2.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(10.9554F, -14.2676F, -27.1301F, -1.8326F, -0.3491F, 0.0F));

		ModelPartData cube_r14 = pillars2.addChild("cube_r14", ModelPartBuilder.create().uv(30, 206).cuboid(-1.0F, -10.0F, -2.5F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-6.6827F, -8.1365F, -18.3146F, -1.8326F, 0.3491F, 0.0F));

		ModelPartData cube_r15 = pillars2.addChild("cube_r15", ModelPartBuilder.create().uv(202, 0).cuboid(-2.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(12.0F, -13.75F, -30.0F, -1.309F, -0.3491F, 0.0F));

		ModelPartData cube_r16 = pillars2.addChild("cube_r16", ModelPartBuilder.create().uv(15, 201).cuboid(0.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-12.0F, -13.75F, -30.0F, -1.309F, 0.3491F, 0.0F));

		ModelPartData top2 = section2.addChild("top2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r17 = top2.addChild("cube_r17", ModelPartBuilder.create().uv(0, 42).cuboid(-4.0F, -4.0F, -28.9F, 8.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r18 = top2.addChild("cube_r18", ModelPartBuilder.create().uv(198, 242).cuboid(-1.5F, -19.0F, -12.0F, 3.0F, 4.0F, 3.0F, new Dilation(0.05F))
		.uv(232, 151).cuboid(-4.0F, -12.4F, -27.0F, 8.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r19 = top2.addChild("cube_r19", ModelPartBuilder.create().uv(30, 100).cuboid(-3.0F, -18.0F, -22.0F, 6.0F, 0.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData bottom2 = section2.addChild("bottom2", ModelPartBuilder.create().uv(92, 178).cuboid(-7.0F, -7.0F, -15.0F, 14.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r20 = bottom2.addChild("cube_r20", ModelPartBuilder.create().uv(77, 214).cuboid(-2.5F, -9.0F, -19.0F, 5.0F, 9.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -2.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r21 = bottom2.addChild("cube_r21", ModelPartBuilder.create().uv(239, 165).cuboid(8.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(239, 173).cuboid(-11.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(70, 45).cuboid(-8.0F, -3.0F, -1.0F, 16.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -9.0F, -24.7F, -0.2618F, 0.0F, 0.0F));

		ModelPartData controls2 = section2.addChild("controls2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData computer = controls2.addChild("computer", ModelPartBuilder.create().uv(0, 160).cuboid(-4.0F, -0.25F, -4.95F, 8.0F, 2.0F, 9.0F, new Dilation(0.0F))
		.uv(98, 214).cuboid(-3.0F, -4.25F, -2.95F, 6.0F, 4.0F, 6.0F, new Dilation(0.0F))
		.uv(145, 140).cuboid(-3.0F, -4.25F, -3.95F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(35, 45).cuboid(3.0F, -4.25F, -3.95F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(0, 15).cuboid(-3.0F, -4.25F, -3.95F, 6.0F, 0.0F, 1.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, -16.25F, -13.95F));

		ModelPartData computernob = computer.addChild("computernob", ModelPartBuilder.create().uv(152, 99).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(148, 33).cuboid(-0.5F, -0.5F, -0.9F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(54, 82).cuboid(-0.75F, -0.75F, -0.25F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(2.5F, 0.75F, -4.95F));

		ModelPartData pumpswitch1 = controls2.addChild("pumpswitch1", ModelPartBuilder.create().uv(80, 239).cuboid(-1.0F, -1.0F, -2.4F, 2.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(-6.0F, -13.0F, -24.25F));

		ModelPartData bone6 = pumpswitch1.addChild("bone6", ModelPartBuilder.create().uv(132, 15).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(240, 212).cuboid(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 6.0F, new Dilation(0.001F))
		.uv(120, 239).cuboid(-0.5F, 0.0F, -1.0F, 1.0F, 0.0F, 6.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 0.0F, -2.4F));

		ModelPartData pumpswitch2 = controls2.addChild("pumpswitch2", ModelPartBuilder.create().uv(238, 192).cuboid(-1.0F, -1.0F, -2.4F, 2.0F, 2.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(6.0F, -13.0F, -24.25F));

		ModelPartData bone7 = pumpswitch2.addChild("bone7", ModelPartBuilder.create().uv(132, 11).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(129, 240).cuboid(0.0F, -0.5F, -1.0F, 0.0F, 1.0F, 6.0F, new Dilation(0.001F))
		.uv(99, 238).cuboid(-0.5F, 0.0F, -1.0F, 1.0F, 0.0F, 6.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 0.0F, -2.4F));

		ModelPartData dial1 = controls2.addChild("dial1", ModelPartBuilder.create().uv(147, 99).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(76, 146).cuboid(-0.5F, -0.5F, -0.9F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(9, 82).cuboid(-0.75F, -0.75F, -0.25F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-3.0F, -11.25F, -25.65F));

		ModelPartData dial2 = controls2.addChild("dial2", ModelPartBuilder.create().uv(132, 33).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(132, 25).cuboid(-0.5F, -0.5F, -0.9F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(7, 57).cuboid(-0.75F, -0.75F, -0.25F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-1.0F, -11.25F, -25.65F));

		ModelPartData dial3 = controls2.addChild("dial3", ModelPartBuilder.create().uv(132, 22).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(130, 88).cuboid(-0.5F, -0.5F, -0.9F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(51, 0).cuboid(-0.75F, -0.75F, -0.25F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(1.0F, -11.25F, -25.65F));

		ModelPartData dial4 = controls2.addChild("dial4", ModelPartBuilder.create().uv(128, 123).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(120, 33).cuboid(-0.5F, -0.5F, -0.9F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(42, 15).cuboid(-0.75F, -0.75F, -0.25F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(3.0F, -11.25F, -25.65F));

		ModelPartData waypointcatridge = controls2.addChild("waypointcatridge", ModelPartBuilder.create().uv(223, 218).cuboid(-1.5F, -4.0F, -1.0F, 3.0F, 4.0F, 6.0F, new Dilation(0.0F))
		.uv(9, 63).cuboid(-1.0F, -3.5F, -2.0F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-12.0F, -12.4F, -20.9F, 0.0F, 0.5236F, 0.0F));

		ModelPartData section3 = alnico.addChild("section3", ModelPartBuilder.create(), ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		ModelPartData desktop3 = section3.addChild("desktop3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r22 = desktop3.addChild("cube_r22", ModelPartBuilder.create().uv(124, 0).cuboid(-8.0F, -4.0F, -8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(130, 99).cuboid(7.0F, -4.0F, -8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(236, 142).cuboid(6.0F, -4.0F, -8.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(186, 236).cuboid(-7.0F, -4.0F, -8.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(132, 0).cuboid(-6.0F, -4.0F, -8.0F, 12.0F, 2.0F, 8.0F, new Dilation(0.0F))
		.uv(109, 83).cuboid(-5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(109, 88).cuboid(4.0F, -4.0F, 0.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(160, 52).cuboid(-9.0F, -4.0F, -10.0F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(216, 94).cuboid(-4.0F, -4.0F, 0.0F, 8.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -12.0F, -14.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData pillars3 = section3.addChild("pillars3", ModelPartBuilder.create().uv(209, 130).cuboid(-6.0F, -19.0F, -12.0F, 12.0F, 6.0F, 2.0F, new Dilation(0.0F))
		.uv(127, 73).cuboid(-9.5F, -13.25F, -27.65F, 19.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r23 = pillars3.addChild("cube_r23", ModelPartBuilder.create().uv(198, 185).cuboid(-2.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(10.9554F, -14.2676F, -27.1301F, -1.8326F, -0.3491F, 0.0F));

		ModelPartData cube_r24 = pillars3.addChild("cube_r24", ModelPartBuilder.create().uv(197, 70).cuboid(-1.0F, -10.0F, -2.5F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-6.6827F, -8.1365F, -18.3146F, -1.8326F, 0.3491F, 0.0F));

		ModelPartData cube_r25 = pillars3.addChild("cube_r25", ModelPartBuilder.create().uv(195, 159).cuboid(-2.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(12.0F, -13.75F, -30.0F, -1.309F, -0.3491F, 0.0F));

		ModelPartData cube_r26 = pillars3.addChild("cube_r26", ModelPartBuilder.create().uv(192, 25).cuboid(0.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-12.0F, -13.75F, -30.0F, -1.309F, 0.3491F, 0.0F));

		ModelPartData top3 = section3.addChild("top3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r27 = top3.addChild("cube_r27", ModelPartBuilder.create().uv(35, 24).cuboid(-4.0F, -4.0F, -28.9F, 8.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r28 = top3.addChild("cube_r28", ModelPartBuilder.create().uv(148, 242).cuboid(-1.5F, -19.0F, -12.0F, 3.0F, 4.0F, 3.0F, new Dilation(0.05F))
		.uv(12, 232).cuboid(-4.0F, -12.4F, -27.0F, 8.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r29 = top3.addChild("cube_r29", ModelPartBuilder.create().uv(0, 98).cuboid(-3.0F, -18.0F, -22.0F, 6.0F, 0.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData bottom3 = section3.addChild("bottom3", ModelPartBuilder.create().uv(160, 176).cuboid(-7.0F, -7.0F, -15.0F, 14.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r30 = bottom3.addChild("cube_r30", ModelPartBuilder.create().uv(213, 182).cuboid(-2.5F, -9.0F, -19.0F, 5.0F, 9.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -2.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r31 = bottom3.addChild("cube_r31", ModelPartBuilder.create().uv(50, 239).cuboid(8.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(65, 239).cuboid(-11.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(70, 24).cuboid(-8.0F, -3.0F, -1.0F, 16.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -9.0F, -24.7F, -0.2618F, 0.0F, 0.0F));

		ModelPartData controls3 = section3.addChild("controls3", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData telepathiccircuit = controls3.addChild("telepathiccircuit", ModelPartBuilder.create(), ModelTransform.of(0.0F, -14.75F, -16.9F, 0.5258F, 1.029F, 0.4614F));

		ModelPartData bone29 = telepathiccircuit.addChild("bone29", ModelPartBuilder.create().uv(189, 211).cuboid(0.0F, -9.0F, -4.5F, 0.0F, 9.0F, 9.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r32 = bone29.addChild("cube_r32", ModelPartBuilder.create().uv(137, 205).cuboid(0.0F, -9.0F, -4.5F, 0.0F, 9.0F, 9.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData lowerlever = controls3.addChild("lowerlever", ModelPartBuilder.create().uv(189, 70).cuboid(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(6.0F, -11.25F, -26.6F));

		ModelPartData bone8 = lowerlever.addChild("bone8", ModelPartBuilder.create().uv(0, 42).cuboid(0.0F, -1.5F, -0.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(77, 45).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.75F, -0.5F, -0.5F));

		ModelPartData bone9 = lowerlever.addChild("bone9", ModelPartBuilder.create().uv(35, 24).cuboid(0.0F, -1.5F, -0.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(77, 32).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.75F, -0.5F, -0.5F));

		ModelPartData lowerlever2 = controls3.addChild("lowerlever2", ModelPartBuilder.create().uv(141, 188).cuboid(-1.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-6.0F, -11.25F, -26.6F));

		ModelPartData bone10 = lowerlever2.addChild("bone10", ModelPartBuilder.create().uv(35, 0).cuboid(0.0F, -1.5F, -0.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(77, 28).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.75F, -0.5F, -0.5F));

		ModelPartData bone11 = lowerlever2.addChild("bone11", ModelPartBuilder.create().uv(0, 21).cuboid(0.0F, -1.5F, -0.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(77, 24).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.75F, -0.5F, -0.5F));

		ModelPartData geiger = controls3.addChild("geiger", ModelPartBuilder.create().uv(181, 30).cuboid(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 82).cuboid(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.2F)), ModelTransform.pivot(-0.5F, -10.75F, -26.6F));

		ModelPartData needle = geiger.addChild("needle", ModelPartBuilder.create().uv(43, 100).cuboid(-0.25F, -2.0F, -0.02F, 1.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(0.5F, 0.25F, -1.0F));

		ModelPartData randomizer = controls3.addChild("randomizer", ModelPartBuilder.create().uv(199, 211).cuboid(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-12.0F, -12.9F, -20.833F, 0.0F, 0.5236F, 0.0F));

		ModelPartData sideswitches2 = controls3.addChild("sideswitches2", ModelPartBuilder.create().uv(222, 35).cuboid(-2.0F, -1.0F, -3.0F, 3.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(-7.5F, -14.7806F, -20.0173F, 0.2472F, 0.3594F, 0.0043F));

		ModelPartData sideswitch3 = sideswitches2.addChild("sideswitch3", ModelPartBuilder.create().uv(181, 36).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(79, 97).cuboid(-1.0F, -2.0F, 0.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-0.5F, 0.0F, 2.5F));

		ModelPartData sideswitch4 = sideswitches2.addChild("sideswitch4", ModelPartBuilder.create().uv(174, 0).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(82, 61).cuboid(-1.0F, -2.0F, 0.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-0.5F, 0.0F, -1.5F));

		ModelPartData sideswitches5 = controls3.addChild("sideswitches5", ModelPartBuilder.create().uv(222, 25).cuboid(-1.0F, -1.0F, -3.0F, 3.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(7.5F, -14.7806F, -20.0173F, 0.2472F, -0.3594F, -0.0043F));

		ModelPartData sideswitch9 = sideswitches5.addChild("sideswitch9", ModelPartBuilder.create().uv(165, 0).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(82, 40).cuboid(-1.0F, -2.0F, 0.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(0.5F, 0.0F, 2.5F));

		ModelPartData sideswitch10 = sideswitches5.addChild("sideswitch10", ModelPartBuilder.create().uv(163, 85).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(77, 61).cuboid(-1.0F, -2.0F, 0.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(0.5F, 0.0F, -1.5F));

		ModelPartData section4 = alnico.addChild("section4", ModelPartBuilder.create(), ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData desktop4 = section4.addChild("desktop4", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r33 = desktop4.addChild("cube_r33", ModelPartBuilder.create().uv(22, 116).cuboid(-8.0F, -4.0F, -8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(122, 45).cuboid(7.0F, -4.0F, -8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(236, 19).cuboid(6.0F, -4.0F, -8.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(236, 62).cuboid(-7.0F, -4.0F, -8.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(130, 83).cuboid(-6.0F, -4.0F, -8.0F, 12.0F, 2.0F, 8.0F, new Dilation(0.0F))
		.uv(39, 109).cuboid(-5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(69, 109).cuboid(4.0F, -4.0F, 0.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(155, 159).cuboid(-9.0F, -4.0F, -10.0F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(213, 197).cuboid(-4.0F, -4.0F, 0.0F, 8.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -12.0F, -14.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData pillars4 = section4.addChild("pillars4", ModelPartBuilder.create().uv(207, 142).cuboid(-6.0F, -19.0F, -12.0F, 12.0F, 6.0F, 2.0F, new Dilation(0.0F))
		.uv(127, 65).cuboid(-9.5F, -13.25F, -27.65F, 19.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r34 = pillars4.addChild("cube_r34", ModelPartBuilder.create().uv(131, 188).cuboid(-2.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(10.9554F, -14.2676F, -27.1301F, -1.8326F, -0.3491F, 0.0F));

		ModelPartData cube_r35 = pillars4.addChild("cube_r35", ModelPartBuilder.create().uv(116, 188).cuboid(-1.0F, -10.0F, -2.5F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-6.6827F, -8.1365F, -18.3146F, -1.8326F, 0.3491F, 0.0F));

		ModelPartData cube_r36 = pillars4.addChild("cube_r36", ModelPartBuilder.create().uv(101, 188).cuboid(-2.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(12.0F, -13.75F, -30.0F, -1.309F, -0.3491F, 0.0F));

		ModelPartData cube_r37 = pillars4.addChild("cube_r37", ModelPartBuilder.create().uv(86, 188).cuboid(0.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-12.0F, -13.75F, -30.0F, -1.309F, 0.3491F, 0.0F));

		ModelPartData top4 = section4.addChild("top4", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r38 = top4.addChild("cube_r38", ModelPartBuilder.create().uv(0, 21).cuboid(-4.0F, -4.0F, -28.9F, 8.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r39 = top4.addChild("cube_r39", ModelPartBuilder.create().uv(242, 71).cuboid(-1.5F, -19.0F, -12.0F, 3.0F, 4.0F, 3.0F, new Dilation(0.05F))
		.uv(188, 230).cuboid(-4.0F, -12.4F, -27.0F, 8.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r40 = top4.addChild("cube_r40", ModelPartBuilder.create().uv(97, 65).cuboid(-3.0F, -18.0F, -22.0F, 6.0F, 0.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData bottom4 = section4.addChild("bottom4", ModelPartBuilder.create().uv(176, 134).cuboid(-7.0F, -7.0F, -15.0F, 14.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r41 = bottom4.addChild("cube_r41", ModelPartBuilder.create().uv(156, 212).cuboid(-2.5F, -9.0F, -19.0F, 5.0F, 9.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -2.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r42 = bottom4.addChild("cube_r42", ModelPartBuilder.create().uv(9, 238).cuboid(8.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(24, 239).cuboid(-11.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(70, 0).cuboid(-8.0F, -3.0F, -1.0F, 16.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -9.0F, -24.7F, -0.2618F, 0.0F, 0.0F));

		ModelPartData controls4 = section4.addChild("controls4", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData biglever2 = controls4.addChild("biglever2", ModelPartBuilder.create().uv(124, 159).cuboid(8.75F, -19.25F, 3.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F))
				.uv(88, 40).cuboid(7.75F, -18.25F, 4.0F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-20.75F, 5.5F, -16.5F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData bone12 = biglever2.addChild("bone12", ModelPartBuilder.create().uv(48, 49).cuboid(-1.75F, -3.8F, -0.5F, 0.0F, 4.0F, 1.0F, new Dilation(0.001F))
				.uv(50, 54).cuboid(1.25F, -3.8F, -0.5F, 0.0F, 4.0F, 1.0F, new Dilation(0.001F))
				.uv(48, 37).cuboid(-1.75F, -3.8F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.001F))
				.uv(13, 10).cuboid(-0.75F, -6.8F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
				.uv(69, 0).cuboid(0.25F, -3.8F, -0.5F, 1.0F, 0.0F, 1.0F, new Dilation(0.001F)), ModelTransform.pivot(10.0F, -17.45F, 5.0F));

		ModelPartData tinyswitch3 = controls4.addChild("tinyswitch3", ModelPartBuilder.create(), ModelTransform.of(-3.0F, -14.25F, -14.0F, -0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r43 = tinyswitch3.addChild("cube_r43", ModelPartBuilder.create().uv(96, 188).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData bone16 = tinyswitch3.addChild("bone16", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.9F, -0.5F));

		ModelPartData cube_r44 = bone16.addChild("cube_r44", ModelPartBuilder.create().uv(144, 81).cuboid(-0.75F, -3.0F, 0.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 1.9F, 0.5F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tinyswitch4 = controls4.addChild("tinyswitch4", ModelPartBuilder.create(), ModelTransform.of(0.0F, -14.25F, -14.0F, -0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r45 = tinyswitch4.addChild("cube_r45", ModelPartBuilder.create().uv(178, 186).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData bone13 = tinyswitch4.addChild("bone13", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.9F, -0.5F));

		ModelPartData cube_r46 = bone13.addChild("cube_r46", ModelPartBuilder.create().uv(140, 99).cuboid(-0.75F, -3.0F, 0.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 1.9F, 0.5F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tinyswitch5 = controls4.addChild("tinyswitch5", ModelPartBuilder.create(), ModelTransform.of(3.0F, -14.25F, -14.0F, -0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r47 = tinyswitch5.addChild("cube_r47", ModelPartBuilder.create().uv(163, 186).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData bone14 = tinyswitch5.addChild("bone14", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -1.9F, -0.5F));

		ModelPartData cube_r48 = bone14.addChild("cube_r48", ModelPartBuilder.create().uv(135, 99).cuboid(-0.75F, -3.0F, 0.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 1.9F, 0.5F, 0.2618F, 0.0F, 0.0F));

		ModelPartData tinylight = controls4.addChild("tinylight", ModelPartBuilder.create().uv(212, 0).cuboid(-1.0F, -0.925F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.4F))
		.uv(62, 0).cuboid(-1.0F, -0.075F, -1.0F, 2.0F, 0.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(-3.0F, -14.975F, -16.2F, 0.2618F, 0.0F, 0.0F));

		ModelPartData bone87 = tinylight.addChild("bone87", ModelPartBuilder.create().uv(40, 206).cuboid(-7.0F, -1.0F, -3.7F, 2.0F, 2.0F, 2.0F, new Dilation(-0.38F)), ModelTransform.pivot(6.0F, 0.075F, 2.7F));

		ModelPartData tinylight2 = controls4.addChild("tinylight2", ModelPartBuilder.create().uv(201, 96).cuboid(-1.0F, -0.925F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.4F))
		.uv(0, 57).cuboid(-1.0F, -0.075F, -1.0F, 2.0F, 0.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -14.975F, -16.2F, 0.2618F, 0.0F, 0.0F));

		ModelPartData bone15 = tinylight2.addChild("bone15", ModelPartBuilder.create().uv(193, 185).cuboid(-7.0F, -1.0F, -3.7F, 2.0F, 2.0F, 2.0F, new Dilation(-0.38F)), ModelTransform.pivot(6.0F, 0.075F, 2.7F));

		ModelPartData tinylight3 = controls4.addChild("tinylight3", ModelPartBuilder.create().uv(190, 6).cuboid(-1.0F, -0.925F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(-0.4F))
		.uv(53, 0).cuboid(-1.0F, -0.075F, -1.0F, 2.0F, 0.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(3.0F, -14.975F, -16.2F, 0.2618F, 0.0F, 0.0F));

		ModelPartData bone17 = tinylight3.addChild("bone17", ModelPartBuilder.create().uv(126, 188).cuboid(-7.0F, -1.0F, -3.7F, 2.0F, 2.0F, 2.0F, new Dilation(-0.38F)), ModelTransform.pivot(6.0F, 0.075F, 2.7F));

		ModelPartData keyboard = controls4.addChild("keyboard", ModelPartBuilder.create().uv(212, 87).cuboid(-5.0F, -2.0F, -3.0F, 10.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -13.25F, -19.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData wrench1 = controls4.addChild("wrench1", ModelPartBuilder.create().uv(132, 4).cuboid(-1.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-6.5F, -10.25F, -26.6F));

		ModelPartData bone18 = wrench1.addChild("bone18", ModelPartBuilder.create().uv(69, 101).cuboid(-1.5F, -1.5F, 0.0F, 3.0F, 7.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-0.5F, -1.5F, 0.0F));

		ModelPartData wrench2 = controls4.addChild("wrench2", ModelPartBuilder.create().uv(132, 0).cuboid(-1.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.5F, -10.25F, -26.6F));

		ModelPartData bone19 = wrench2.addChild("bone19", ModelPartBuilder.create().uv(9, 98).cuboid(-1.5F, -1.5F, 0.0F, 3.0F, 8.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-0.5F, -1.5F, 0.0F));

		ModelPartData wrench3 = controls4.addChild("wrench3", ModelPartBuilder.create().uv(120, 61).cuboid(-1.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, -10.25F, -26.6F));

		ModelPartData bone20 = wrench3.addChild("bone20", ModelPartBuilder.create().uv(79, 82).cuboid(-1.5F, -1.5F, 0.0F, 3.0F, 9.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-0.5F, -1.5F, 0.0F));

		ModelPartData wrench4 = controls4.addChild("wrench4", ModelPartBuilder.create().uv(75, 0).cuboid(-1.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(7.5F, -10.25F, -26.6F));

		ModelPartData bone21 = wrench4.addChild("bone21", ModelPartBuilder.create().uv(70, 24).cuboid(-1.5F, -1.5F, 0.0F, 3.0F, 10.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-0.5F, -1.5F, 0.0F));

		ModelPartData section5 = alnico.addChild("section5", ModelPartBuilder.create(), ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData desktop5 = section5.addChild("desktop5", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r49 = desktop5.addChild("cube_r49", ModelPartBuilder.create().uv(111, 40).cuboid(-8.0F, -4.0F, -8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(17, 116).cuboid(7.0F, -4.0F, -8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(90, 235).cuboid(6.0F, -4.0F, -8.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(236, 9).cuboid(-7.0F, -4.0F, -8.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(128, 129).cuboid(-6.0F, -4.0F, -8.0F, 12.0F, 2.0F, 8.0F, new Dilation(0.0F))
		.uv(79, 92).cuboid(-5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(9, 107).cuboid(4.0F, -4.0F, 0.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(130, 94).cuboid(-9.0F, -4.0F, -10.0F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(171, 85).cuboid(-4.0F, -4.0F, 0.0F, 8.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -12.0F, -14.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData pillars5 = section5.addChild("pillars5", ModelPartBuilder.create().uv(207, 63).cuboid(-6.0F, -19.0F, -12.0F, 12.0F, 6.0F, 2.0F, new Dilation(0.0F))
		.uv(127, 57).cuboid(-9.5F, -13.25F, -27.65F, 19.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r50 = pillars5.addChild("cube_r50", ModelPartBuilder.create().uv(183, 186).cuboid(-2.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(10.9554F, -14.2676F, -27.1301F, -1.8326F, -0.3491F, 0.0F));

		ModelPartData cube_r51 = pillars5.addChild("cube_r51", ModelPartBuilder.create().uv(168, 186).cuboid(-1.0F, -10.0F, -2.5F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-6.6827F, -8.1365F, -18.3146F, -1.8326F, 0.3491F, 0.0F));

		ModelPartData cube_r52 = pillars5.addChild("cube_r52", ModelPartBuilder.create().uv(153, 186).cuboid(-2.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(12.0F, -13.75F, -30.0F, -1.309F, -0.3491F, 0.0F));

		ModelPartData cube_r53 = pillars5.addChild("cube_r53", ModelPartBuilder.create().uv(71, 186).cuboid(0.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-12.0F, -13.75F, -30.0F, -1.309F, 0.3491F, 0.0F));

		ModelPartData top5 = section5.addChild("top5", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r54 = top5.addChild("cube_r54", ModelPartBuilder.create().uv(35, 3).cuboid(-4.0F, -4.0F, -28.9F, 8.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r55 = top5.addChild("cube_r55", ModelPartBuilder.create().uv(241, 128).cuboid(-1.5F, -19.0F, -12.0F, 3.0F, 4.0F, 3.0F, new Dilation(0.05F))
		.uv(225, 56).cuboid(-4.0F, -12.4F, -27.0F, 8.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r56 = top5.addChild("cube_r56", ModelPartBuilder.create().uv(70, 83).cuboid(-3.0F, -18.0F, -22.0F, 6.0F, 0.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData bottom5 = section5.addChild("bottom5", ModelPartBuilder.create().uv(59, 176).cuboid(-7.0F, -7.0F, -15.0F, 14.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r57 = bottom5.addChild("cube_r57", ModelPartBuilder.create().uv(212, 72).cuboid(-2.5F, -9.0F, -19.0F, 5.0F, 9.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -2.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r58 = bottom5.addChild("cube_r58", ModelPartBuilder.create().uv(237, 157).cuboid(8.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(238, 0).cuboid(-11.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(57, 66).cuboid(-8.0F, -3.0F, -1.0F, 16.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -9.0F, -24.7F, -0.2618F, 0.0F, 0.0F));

		ModelPartData controls5 = section5.addChild("controls5", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData taperecorder = controls5.addChild("taperecorder", ModelPartBuilder.create().uv(200, 122).cuboid(-5.0F, -1.0F, -3.0F, 10.0F, 2.0F, 5.0F, new Dilation(0.0F))
		.uv(235, 108).cuboid(-1.0F, -2.0F, -3.0F, 2.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -16.5F, -14.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData cube_r59 = taperecorder.addChild("cube_r59", ModelPartBuilder.create().uv(70, 40).cuboid(1.0F, -0.6282F, -2.2219F, 3.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(-1.2782F, -1.3718F, -2.136F, 0.0F, -0.7854F, 0.0F));

		ModelPartData cube_r60 = taperecorder.addChild("cube_r60", ModelPartBuilder.create().uv(70, 61).cuboid(-4.0F, -0.6282F, -2.2219F, 3.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(1.2782F, -1.3718F, -2.136F, 0.0F, 0.7854F, 0.0F));

		ModelPartData bone22 = taperecorder.addChild("bone22", ModelPartBuilder.create().uv(117, 214).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-3.5F, -2.0F, 0.5F));

		ModelPartData bone23 = taperecorder.addChild("bone23", ModelPartBuilder.create().uv(52, 167).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(3.5F, -2.0F, 0.5F));

		ModelPartData geiger4 = controls5.addChild("geiger4", ModelPartBuilder.create().uv(242, 98).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.25F, -14.0F, -18.0F));

		ModelPartData needle4 = geiger4.addChild("needle4", ModelPartBuilder.create().uv(39, 100).cuboid(-0.5F, -2.0F, -0.02F, 1.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 1.0F, -2.0F));

		ModelPartData geiger3 = controls5.addChild("geiger3", ModelPartBuilder.create().uv(145, 140).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(4.25F, -14.0F, -18.0F));

		ModelPartData needle3 = geiger3.addChild("needle3", ModelPartBuilder.create().uv(79, 9).cuboid(-0.5F, -2.0F, -0.02F, 1.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(0.0F, 1.0F, -2.0F));

		ModelPartData tinyswitch6 = controls5.addChild("tinyswitch6", ModelPartBuilder.create().uv(9, 0).cuboid(7.4378F, -3.4749F, -12.2364F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-11.5F, -10.7F, -12.55F));

		ModelPartData bone24 = tinyswitch6.addChild("bone24", ModelPartBuilder.create().uv(16, 63).cuboid(-1.0622F, -0.7749F, -0.0364F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(13, 52).cuboid(-0.5622F, -0.7749F, -0.5364F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(8.5F, -3.45F, -11.2F));

		ModelPartData tinyswitch7 = controls5.addChild("tinyswitch7", ModelPartBuilder.create().uv(9, 42).cuboid(6.5F, -3.4749F, -12.2364F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-7.5F, -10.7F, -12.55F));

		ModelPartData bone25 = tinyswitch7.addChild("bone25", ModelPartBuilder.create().uv(88, 21).cuboid(-1.0F, -0.7249F, 0.0136F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(77, 49).cuboid(-0.5F, -0.7249F, -0.4864F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(7.5F, -3.5F, -11.25F));

		ModelPartData tinyswitch9 = controls5.addChild("tinyswitch9", ModelPartBuilder.create().uv(9, 21).cuboid(-9.4378F, -3.4749F, -12.2364F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(11.5F, -10.7F, -12.55F));

		ModelPartData bone26 = tinyswitch9.addChild("bone26", ModelPartBuilder.create().uv(23, 63).cuboid(-0.9378F, -0.7749F, -0.0364F, 2.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(30, 63).cuboid(-0.4378F, -0.7749F, -0.5364F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F)), ModelTransform.pivot(-8.5F, -3.45F, -11.2F));

		ModelPartData tinylight4 = controls5.addChild("tinylight4", ModelPartBuilder.create().uv(81, 186).cuboid(2.0F, -2.2699F, -6.0191F, 2.0F, 2.0F, 2.0F, new Dilation(-0.4F))
		.uv(0, 36).cuboid(2.0F, -1.4199F, -6.0191F, 2.0F, 0.0F, 2.0F, new Dilation(0.001F)), ModelTransform.pivot(-9.0F, -11.925F, -19.1F));

		ModelPartData bone27 = tinylight4.addChild("bone27", ModelPartBuilder.create().uv(66, 186).cuboid(-4.0F, -2.3449F, -8.7191F, 2.0F, 2.0F, 2.0F, new Dilation(-0.38F)), ModelTransform.pivot(6.0F, 0.075F, 2.7F));

		ModelPartData tinylight5 = controls5.addChild("tinylight5", ModelPartBuilder.create().uv(36, 180).cuboid(-4.0F, -2.2699F, -6.0191F, 2.0F, 2.0F, 2.0F, new Dilation(-0.4F))
		.uv(35, 15).cuboid(-4.0F, -1.4199F, -6.0191F, 2.0F, 0.0F, 2.0F, new Dilation(0.001F)), ModelTransform.pivot(9.0F, -11.925F, -19.1F));

		ModelPartData bone28 = tinylight5.addChild("bone28", ModelPartBuilder.create().uv(0, 164).cuboid(2.0F, -2.3449F, -8.7191F, 2.0F, 2.0F, 2.0F, new Dilation(-0.38F)), ModelTransform.pivot(-6.0F, 0.075F, 2.7F));

		ModelPartData cassetteplayer = controls5.addChild("cassetteplayer", ModelPartBuilder.create().uv(88, 61).cuboid(-2.0F, 0.0F, -1.0F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -15.0306F, -20.0173F, 0.2618F, 0.0F, 0.0F));

		ModelPartData randomizer2 = controls5.addChild("randomizer2", ModelPartBuilder.create().uv(190, 17).cuboid(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-12.0F, -12.9F, -20.833F, 0.0F, 0.5236F, 0.0F));

		ModelPartData multiswitchpanel2 = controls5.addChild("multiswitchpanel2", ModelPartBuilder.create().uv(100, 144).cuboid(-3.0F, -3.0F, -1.0F, 6.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.25F, -26.65F));

		ModelPartData longswitch5 = multiswitchpanel2.addChild("longswitch5", ModelPartBuilder.create().uv(111, 70).cuboid(0.0F, -2.5F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.001F))
		.uv(115, 33).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.25F, -2.5F, -0.5F));

		ModelPartData longswitch6 = multiswitchpanel2.addChild("longswitch6", ModelPartBuilder.create().uv(111, 65).cuboid(0.0F, -2.5F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.001F))
		.uv(69, 114).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.75F, -2.5F, -0.5F));

		ModelPartData longswitch7 = multiswitchpanel2.addChild("longswitch7", ModelPartBuilder.create().uv(111, 16).cuboid(0.0F, -2.5F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.001F))
		.uv(109, 21).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.75F, -2.5F, -0.5F));

		ModelPartData longswitch8 = multiswitchpanel2.addChild("longswitch8", ModelPartBuilder.create().uv(79, 4).cuboid(0.0F, -2.5F, -0.5F, 0.0F, 3.0F, 1.0F, new Dilation(0.001F))
		.uv(77, 52).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(2.25F, -2.5F, -0.5F));

		ModelPartData computernob2 = controls5.addChild("computernob2", ModelPartBuilder.create().uv(48, 10).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(13, 31).cuboid(-0.5F, -0.5F, -0.9F, 1.0F, 1.0F, 1.0F, new Dilation(-0.25F))
		.uv(7, 36).cuboid(-0.75F, -0.75F, -0.25F, 1.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -15.7F, -16.9F, 0.2618F, 0.0F, 0.0F));

		ModelPartData section6 = alnico.addChild("section6", ModelPartBuilder.create(), ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData desktop6 = section6.addChild("desktop6", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r61 = desktop6.addChild("cube_r61", ModelPartBuilder.create().uv(99, 40).cuboid(-8.0F, -4.0F, -8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(109, 98).cuboid(7.0F, -4.0F, -8.0F, 1.0F, 0.0F, 1.0F, new Dilation(0.0F))
		.uv(232, 133).cuboid(6.0F, -4.0F, -8.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(111, 233).cuboid(-7.0F, -4.0F, -8.0F, 1.0F, 2.0F, 6.0F, new Dilation(0.0F))
		.uv(128, 118).cuboid(-6.0F, -4.0F, -8.0F, 12.0F, 2.0F, 8.0F, new Dilation(0.0F))
		.uv(46, 13).cuboid(-5.0F, -4.0F, 0.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(11, 55).cuboid(4.0F, -4.0F, 0.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(70, 16).cuboid(-9.0F, -4.0F, -10.0F, 18.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(26, 160).cuboid(-4.0F, -4.0F, 0.0F, 8.0F, 2.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -12.0F, -14.0F, 0.2618F, 0.0F, 0.0F));

		ModelPartData pillars6 = section6.addChild("pillars6", ModelPartBuilder.create().uv(205, 155).cuboid(-6.0F, -19.0F, -12.0F, 12.0F, 6.0F, 2.0F, new Dilation(0.0F))
		.uv(115, 49).cuboid(-9.5F, -13.25F, -27.65F, 19.0F, 4.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r62 = pillars6.addChild("cube_r62", ModelPartBuilder.create().uv(56, 186).cuboid(-2.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(10.9554F, -14.2676F, -27.1301F, -1.8326F, -0.3491F, 0.0F));

		ModelPartData cube_r63 = pillars6.addChild("cube_r63", ModelPartBuilder.create().uv(0, 183).cuboid(-1.0F, -10.0F, -2.5F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-6.6827F, -8.1365F, -18.3146F, -1.8326F, 0.3491F, 0.0F));

		ModelPartData cube_r64 = pillars6.addChild("cube_r64", ModelPartBuilder.create().uv(41, 180).cuboid(-2.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(12.0F, -13.75F, -30.0F, -1.309F, -0.3491F, 0.0F));

		ModelPartData cube_r65 = pillars6.addChild("cube_r65", ModelPartBuilder.create().uv(26, 180).cuboid(0.0F, -21.0F, 0.9F, 2.0F, 20.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(-12.0F, -13.75F, -30.0F, -1.309F, 0.3491F, 0.0F));

		ModelPartData top6 = section6.addChild("top6", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r66 = top6.addChild("cube_r66", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -4.0F, -28.9F, 8.0F, 2.0F, 18.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.2618F, 0.5236F, 0.0F));

		ModelPartData cube_r67 = top6.addChild("cube_r67", ModelPartBuilder.create().uv(107, 151).cuboid(-1.5F, -19.0F, -12.0F, 3.0F, 4.0F, 3.0F, new Dilation(0.05F))
		.uv(169, 123).cuboid(-4.0F, -12.4F, -27.0F, 8.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r68 = top6.addChild("cube_r68", ModelPartBuilder.create().uv(40, 82).cuboid(-3.0F, -18.0F, -22.0F, 6.0F, 0.0F, 17.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 0.5236F, 0.0F));

		ModelPartData bottom6 = section6.addChild("bottom6", ModelPartBuilder.create().uv(173, 105).cuboid(-7.0F, -7.0F, -15.0F, 14.0F, 7.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 2.0F));

		ModelPartData cube_r69 = bottom6.addChild("cube_r69", ModelPartBuilder.create().uv(132, 33).cuboid(-2.5F, -9.0F, -19.0F, 5.0F, 9.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -2.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r70 = bottom6.addChild("cube_r70", ModelPartBuilder.create().uv(237, 90).cuboid(8.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(136, 237).cuboid(-11.0F, -3.0F, -1.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 66).cuboid(-8.0F, -3.0F, -1.0F, 16.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -9.0F, -24.7F, -0.2618F, 0.0F, 0.0F));

		ModelPartData controls6 = section6.addChild("controls6", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData randomizer3 = controls6.addChild("randomizer3", ModelPartBuilder.create().uv(0, 158).cuboid(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-12.0F, -12.9F, -20.833F, 0.0F, 0.5236F, 0.0F));

		ModelPartData sideswitches3 = controls6.addChild("sideswitches3", ModelPartBuilder.create().uv(221, 108).cuboid(-2.0F, -1.0F, -3.0F, 3.0F, 2.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(-7.5F, -14.7806F, -20.0173F, 0.2472F, 0.3594F, 0.0043F));

		ModelPartData sideswitch5 = sideswitches3.addChild("sideswitch5", ModelPartBuilder.create().uv(115, 45).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(77, 55).cuboid(-1.0F, -2.0F, 0.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-0.5F, 0.0F, 2.5F));

		ModelPartData sideswitch6 = sideswitches3.addChild("sideswitch6", ModelPartBuilder.create().uv(111, 61).cuboid(-1.0F, -3.0F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(77, 40).cuboid(-1.0F, -2.0F, 0.0F, 2.0F, 1.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(-0.5F, 0.0F, -1.5F));

		ModelPartData geiger5 = controls6.addChild("geiger5", ModelPartBuilder.create().uv(153, 33).cuboid(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 88).cuboid(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.2F)), ModelTransform.pivot(-0.5F, -10.75F, -26.6F));

		ModelPartData needle5 = geiger5.addChild("needle5", ModelPartBuilder.create().uv(65, 66).cuboid(-0.25F, -2.0F, -0.02F, 1.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(0.5F, 0.25F, -1.0F));

		ModelPartData geiger6 = controls6.addChild("geiger6", ModelPartBuilder.create().uv(115, 6).cuboid(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(45, 88).cuboid(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.2F)), ModelTransform.pivot(-4.5F, -10.75F, -26.6F));

		ModelPartData needle6 = geiger6.addChild("needle6", ModelPartBuilder.create().uv(45, 66).cuboid(-0.25F, -2.0F, -0.02F, 1.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(0.5F, 0.25F, -1.0F));

		ModelPartData geiger7 = controls6.addChild("geiger7", ModelPartBuilder.create().uv(115, 0).cuboid(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.0F))
		.uv(45, 82).cuboid(-1.0F, -2.0F, -1.0F, 3.0F, 3.0F, 2.0F, new Dilation(0.2F)), ModelTransform.pivot(3.5F, -10.75F, -26.6F));

		ModelPartData needle7 = geiger7.addChild("needle7", ModelPartBuilder.create().uv(15, 15).cuboid(-0.25F, -2.0F, -0.02F, 1.0F, 2.0F, 0.0F, new Dilation(0.001F)), ModelTransform.pivot(0.5F, 0.25F, -1.0F));

		ModelPartData cashregister = controls6.addChild("cashregister", ModelPartBuilder.create().uv(0, 173).cuboid(-4.0F, -2.0F, -6.0F, 8.0F, 2.0F, 7.0F, new Dilation(0.0F))
		.uv(45, 66).cuboid(-4.0F, -9.0F, -2.0F, 8.0F, 7.0F, 3.0F, new Dilation(0.0F))
		.uv(223, 211).cuboid(-4.0F, -5.0F, -5.0F, 8.0F, 3.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -13.0F, -15.0F));

		ModelPartData registerlever = cashregister.addChild("registerlever", ModelPartBuilder.create().uv(11, 34).cuboid(0.0F, -1.0F, -1.0F, 1.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(0.5F, -3.0F, -0.5F, 0.0F, 2.0F, 1.0F, new Dilation(0.001F))
		.uv(48, 33).cuboid(0.0F, -5.0F, -0.5F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -3.5F, -3.5F));

		ModelPartData registerswitches = cashregister.addChild("registerswitches", ModelPartBuilder.create().uv(13, 46).cuboid(0.0F, -2.0F, -1.0F, 0.0F, 3.0F, 2.0F, new Dilation(0.001F))
		.uv(44, 45).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-2.75F, -4.5F, -4.0F));

		ModelPartData registerswitches2 = cashregister.addChild("registerswitches2", ModelPartBuilder.create().uv(13, 25).cuboid(0.0F, -2.0F, -1.0F, 0.0F, 3.0F, 2.0F, new Dilation(0.001F))
		.uv(44, 24).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.25F, -4.5F, -4.0F));

		ModelPartData registerswitches3 = cashregister.addChild("registerswitches3", ModelPartBuilder.create().uv(13, 4).cuboid(0.0F, -2.0F, -1.0F, 0.0F, 3.0F, 2.0F, new Dilation(0.001F))
		.uv(44, 0).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(2.25F, -4.5F, -4.0F));

		ModelPartData column = alnico.addChild("column", ModelPartBuilder.create().uv(131, 159).cuboid(-4.0F, -26.0F, -6.93F, 8.0F, 11.0F, 7.0F, new Dilation(0.0F))
		.uv(172, 57).cuboid(-4.0F, -72.0F, -6.93F, 8.0F, 5.0F, 7.0F, new Dilation(0.0F))
		.uv(35, 233).cuboid(-4.0F, -80.0F, -7.93F, 8.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

		ModelPartData cube_r71 = column.addChild("cube_r71", ModelPartBuilder.create().uv(56, 118).cuboid(0.0F, -109.0F, -9.0F, 0.0F, 46.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 41.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r72 = column.addChild("cube_r72", ModelPartBuilder.create().uv(51, 118).cuboid(0.0F, -32.0F, -9.0F, 0.0F, 46.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -36.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r73 = column.addChild("cube_r73", ModelPartBuilder.create().uv(66, 118).cuboid(0.0F, -109.0F, -9.0F, 0.0F, 46.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 41.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r74 = column.addChild("cube_r74", ModelPartBuilder.create().uv(61, 118).cuboid(0.0F, -32.0F, -9.0F, 0.0F, 46.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -36.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r75 = column.addChild("cube_r75", ModelPartBuilder.create().uv(71, 118).cuboid(0.0F, -32.0F, -9.0F, 0.0F, 46.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, -36.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

		ModelPartData cube_r76 = column.addChild("cube_r76", ModelPartBuilder.create().uv(127, 0).cuboid(0.0F, -109.0F, -9.0F, 0.0F, 46.0F, 2.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 41.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r77 = column.addChild("cube_r77", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -37.0F, -11.0F, 2.0F, 10.0F, 4.0F, new Dilation(0.0F))
		.uv(234, 182).cuboid(-1.0F, -92.0F, -10.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r78 = column.addChild("cube_r78", ModelPartBuilder.create().uv(0, 21).cuboid(-1.0F, -37.0F, -11.0F, 2.0F, 10.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, -2.618F, 0.0F));

		ModelPartData cube_r79 = column.addChild("cube_r79", ModelPartBuilder.create().uv(35, 0).cuboid(-1.0F, -37.0F, -11.0F, 2.0F, 10.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, 2.618F, 0.0F));

		ModelPartData cube_r80 = column.addChild("cube_r80", ModelPartBuilder.create().uv(35, 24).cuboid(-1.0F, -37.0F, -11.0F, 2.0F, 10.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r81 = column.addChild("cube_r81", ModelPartBuilder.create().uv(0, 42).cuboid(-1.0F, -37.0F, -11.0F, 2.0F, 10.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r82 = column.addChild("cube_r82", ModelPartBuilder.create().uv(35, 45).cuboid(-1.0F, -37.0F, -11.0F, 2.0F, 10.0F, 4.0F, new Dilation(0.0F))
		.uv(0, 235).cuboid(-1.0F, -92.0F, -10.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 12.0F, 0.0F, 0.0F, -0.5236F, 0.0F));

		ModelPartData cube_r83 = column.addChild("cube_r83", ModelPartBuilder.create().uv(233, 72).cuboid(-1.0F, -80.0F, -10.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r84 = column.addChild("cube_r84", ModelPartBuilder.create().uv(211, 232).cuboid(-1.0F, -80.0F, -10.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r85 = column.addChild("cube_r85", ModelPartBuilder.create().uv(177, 233).cuboid(-1.0F, -80.0F, -10.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r86 = column.addChild("cube_r86", ModelPartBuilder.create().uv(168, 233).cuboid(-1.0F, -80.0F, -10.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -3.1416F, 0.5236F, 3.1416F));

		ModelPartData cube_r87 = column.addChild("cube_r87", ModelPartBuilder.create().uv(98, 225).cuboid(-4.0F, -31.0F, -7.93F, 8.0F, 8.0F, 1.0F, new Dilation(0.0F))
		.uv(121, 140).cuboid(-4.0F, 23.0F, -6.93F, 8.0F, 11.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -49.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r88 = column.addChild("cube_r88", ModelPartBuilder.create().uv(140, 227).cuboid(-4.0F, -31.0F, -7.93F, 8.0F, 8.0F, 1.0F, new Dilation(0.0F))
		.uv(76, 144).cuboid(-4.0F, 23.0F, -6.93F, 8.0F, 11.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -49.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData cube_r89 = column.addChild("cube_r89", ModelPartBuilder.create().uv(77, 229).cuboid(-4.0F, -31.0F, -7.93F, 8.0F, 8.0F, 1.0F, new Dilation(0.0F))
		.uv(152, 140).cuboid(-4.0F, 23.0F, -6.93F, 8.0F, 11.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -49.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData cube_r90 = column.addChild("cube_r90", ModelPartBuilder.create().uv(223, 229).cuboid(-4.0F, -31.0F, -7.93F, 8.0F, 8.0F, 1.0F, new Dilation(0.0F))
		.uv(157, 33).cuboid(-4.0F, 23.0F, -6.93F, 8.0F, 11.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -49.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		ModelPartData cube_r91 = column.addChild("cube_r91", ModelPartBuilder.create().uv(231, 118).cuboid(-4.0F, -31.0F, -7.93F, 8.0F, 8.0F, 1.0F, new Dilation(0.0F))
		.uv(170, 92).cuboid(-4.0F, -23.0F, -6.93F, 8.0F, 5.0F, 7.0F, new Dilation(0.0F))
		.uv(100, 159).cuboid(-4.0F, 23.0F, -6.93F, 8.0F, 11.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -49.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r92 = column.addChild("cube_r92", ModelPartBuilder.create().uv(166, 4).cuboid(-4.0F, -23.0F, -6.93F, 8.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -49.0F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r93 = column.addChild("cube_r93", ModelPartBuilder.create().uv(69, 163).cuboid(-4.0F, -23.0F, -6.93F, 8.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -49.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r94 = column.addChild("cube_r94", ModelPartBuilder.create().uv(28, 167).cuboid(-4.0F, -23.0F, -6.93F, 8.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -49.0F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r95 = column.addChild("cube_r95", ModelPartBuilder.create().uv(166, 17).cuboid(-4.0F, -23.0F, -6.93F, 8.0F, 5.0F, 7.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -49.0F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData glass = column.addChild("glass", ModelPartBuilder.create().uv(77, 101).cuboid(-4.0F, -67.0F, -6.93F, 8.0F, 41.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData cube_r96 = glass.addChild("cube_r96", ModelPartBuilder.create().uv(34, 118).cuboid(-4.0F, -67.0F, -6.93F, 8.0F, 41.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r97 = glass.addChild("cube_r97", ModelPartBuilder.create().uv(94, 102).cuboid(-4.0F, -67.0F, -6.93F, 8.0F, 41.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r98 = glass.addChild("cube_r98", ModelPartBuilder.create().uv(111, 102).cuboid(-4.0F, -67.0F, -6.93F, 8.0F, 41.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 2.0944F, 0.0F));

		ModelPartData cube_r99 = glass.addChild("cube_r99", ModelPartBuilder.create().uv(0, 116).cuboid(-4.0F, -67.0F, -6.93F, 8.0F, 41.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData cube_r100 = glass.addChild("cube_r100", ModelPartBuilder.create().uv(17, 118).cuboid(-4.0F, -67.0F, -6.93F, 8.0F, 41.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -2.0944F, 0.0F));

		ModelPartData timerotor = column.addChild("timerotor", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData bottomgizmo = timerotor.addChild("bottomgizmo", ModelPartBuilder.create().uv(60, 212).cuboid(-2.0F, -14.0F, -2.0F, 4.0F, 14.0F, 4.0F, new Dilation(0.0F))
		.uv(162, 164).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 3.0F, 8.0F, new Dilation(0.0F))
		.uv(196, 52).cuboid(-3.5F, -8.0F, -3.5F, 7.0F, 3.0F, 7.0F, new Dilation(0.0F))
		.uv(218, 46).cuboid(-3.0F, -12.0F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F))
		.uv(15, 183).cuboid(-6.0F, -16.0F, 0.0F, 4.0F, 16.0F, 0.0F, new Dilation(0.001F))
		.uv(115, 16).cuboid(2.0F, -16.0F, 0.0F, 4.0F, 16.0F, 0.0F, new Dilation(0.001F))
		.uv(159, 227).cuboid(-1.0F, -19.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -17.0F, 0.0F));

		ModelPartData cube_r101 = bottomgizmo.addChild("cube_r101", ModelPartBuilder.create().uv(102, 61).cuboid(2.0F, -42.0F, 0.0F, 4.0F, 16.0F, 0.0F, new Dilation(0.001F))
		.uv(100, 83).cuboid(-6.0F, -42.0F, 0.0F, 4.0F, 16.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 26.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		ModelPartData topgizmo = timerotor.addChild("topgizmo", ModelPartBuilder.create().uv(123, 220).cuboid(-2.0F, -14.0F, -2.0F, 4.0F, 14.0F, 4.0F, new Dilation(0.0F))
		.uv(164, 73).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 3.0F, 8.0F, new Dilation(0.0F))
		.uv(183, 144).cuboid(-3.5F, -8.0F, -3.5F, 7.0F, 3.0F, 7.0F, new Dilation(0.0F))
		.uv(217, 15).cuboid(-3.0F, -12.0F, -3.0F, 6.0F, 3.0F, 6.0F, new Dilation(0.0F))
		.uv(60, 100).cuboid(-6.0F, -16.0F, 0.0F, 4.0F, 16.0F, 0.0F, new Dilation(0.001F))
		.uv(30, 98).cuboid(2.0F, -16.0F, 0.0F, 4.0F, 16.0F, 0.0F, new Dilation(0.001F))
		.uv(0, 63).cuboid(-1.0F, -19.0F, -1.0F, 2.0F, 12.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -67.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

		ModelPartData cube_r102 = topgizmo.addChild("cube_r102", ModelPartBuilder.create().uv(70, 82).cuboid(2.0F, -42.0F, 0.0F, 4.0F, 16.0F, 0.0F, new Dilation(0.001F))
		.uv(0, 98).cuboid(-6.0F, -42.0F, 0.0F, 4.0F, 16.0F, 0.0F, new Dilation(0.001F)), ModelTransform.of(0.0F, 26.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
		return TexturedModelData.of(modelData, 256, 256);
	}
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		alnico.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void renderWithAnimations(ConsoleBlockEntity console, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		if (console.getTardis().isEmpty()) return;

		matrices.push();

		matrices.translate(0.5f, -1.5f, -0.5f);

		ModelPart throttle = alnico.getChild("section1").getChild("controls").getChild("fliplever1").getChild("bone5");
		throttle.pitch = throttle.pitch + ((console.getTardis().get().getTravel().getSpeed() / (float) TardisTravel.MAX_SPEED) * 1.5f);

		ModelPart handbrake = alnico.getChild("section1").getChild("controls").getChild("biglever").getChild("bone");
		handbrake.pitch = !PropertiesHandler.getBool(console.getTardis().get().getHandlers().getProperties(), PropertiesHandler.HANDBRAKE) ? handbrake.pitch + 0.9f : handbrake.pitch - 0.9f;

		ModelPart power = alnico.getChild("section4").getChild("controls4").getChild("biglever2").getChild("bone12");
		power.pitch = !console.getTardis().get().hasPower() ? power.pitch - 0.9f : power.pitch + 0.9f;

		ModelPart autoPilot = alnico.getChild("section1").getChild("controls").getChild("multiswitchpanel").getChild("longswitch1");
		autoPilot.pitch = PropertiesHandler.getBool(console.getTardis().get().getHandlers().getProperties(), PropertiesHandler.AUTO_LAND) ? autoPilot.pitch + 0.5f : autoPilot.pitch;

		ModelPart fuelGauge = alnico.getChild("section3").getChild("controls3").getChild("geiger").getChild("needle");
		fuelGauge.roll = (float) (((console.getTardis().get().getFuel() / FuelData.TARDIS_MAX_FUEL) * 2) - 1);

		ModelPart increment = alnico.getChild("section5").getChild("controls5").getChild("multiswitchpanel2").getChild("longswitch5");
		increment.pitch = IncrementManager.increment(console.getTardis().get()) >= 10 ? IncrementManager.increment(console.getTardis().get()) >= 100 ? IncrementManager.increment(console.getTardis().get()) >= 1000 ? increment.pitch + 1.5f : increment.pitch + 1f : increment.pitch + 0.5f : increment.pitch;

		super.renderWithAnimations(console, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);

		matrices.pop();
	}

	@Override
	public ModelPart getPart() {
		return alnico;
	}

	@Override
	public Animation getAnimationForState(TardisTravel.State state) {
		return switch(state) {
			case FLIGHT, MAT, DEMAT -> AlnicoAnimations.CONSOLE_ALNICO_FLIGHT;
			case LANDED -> AlnicoAnimations.CONSOLE_ALNICO_IDLE;
			default -> Animation.Builder.create(0).build();
		};
	}
}