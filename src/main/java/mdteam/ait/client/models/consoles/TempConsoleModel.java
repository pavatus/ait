package mdteam.ait.client.models.consoles;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.ExteriorBlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class TempConsoleModel extends ConsoleModel {

	public static final Identifier CONSOLE_TEXTURE = new Identifier(AITMod.MOD_ID, ("textures/blockentities/consoles/display_console.png"));
	public static final Identifier CONSOLE_TEXTURE_EMISSION = new Identifier(AITMod.MOD_ID, "textures/blockentities/consoles/display_console_emission.png");

	public ModelPart console;
	public ModelPart cube_r1;
	public ModelPart panel_three_small_lights_r1;
	public ModelPart cube_r2;
	public ModelPart cube_r3;
	public ModelPart bone49;
	public ModelPart cube_r4;
	public ModelPart bone48;
	public ModelPart cube_r5;
	public ModelPart bone47;
	public ModelPart cube_r6;
	public ModelPart bone46;
	public ModelPart cube_r7;
	public ModelPart bone45;
	public ModelPart cube_r8;
	public ModelPart bone44;
	public ModelPart cube_r9;
	public ModelPart bone43;
	public ModelPart cube_r10;
	public ModelPart bone41;
	public ModelPart cube_r11;
	public ModelPart bone42;
	public ModelPart cube_r12;
	public ModelPart glowies;
	public ModelPart cube_r13;
	public ModelPart fast_return_switch;
	public ModelPart fast_return_switch_r1;
	public ModelPart exterior_facing;
	public ModelPart exterior_facing_r1;
	public ModelPart dimension_changer;
	public ModelPart dimension_changer_r1;
	public ModelPart door_control;
	public ModelPart door_control_r1;
	public ModelPart refuller;
	public ModelPart refuller_r1;
	public ModelPart monitor;
	public ModelPart monitor_r1;
	public ModelPart communicator;
	public ModelPart communicator_r1;
	public ModelPart randomiser;
	public ModelPart randomiser_r1;
	public ModelPart increment;
	public ModelPart increment_r1;
	public ModelPart z;
	public ModelPart z_r1;
	public ModelPart y;
	public ModelPart y_r1;
	public ModelPart x;
	public ModelPart x_r1;
	public ModelPart handbrake;
	public ModelPart lever_section_2_r1;
	public ModelPart lever_section_1_r1;
	public ModelPart throttle;
	public ModelPart lamp;
	public ModelPart cube_r14;
	public ModelPart cube_r15;
	public ModelPart cube_r16;
	public ModelPart cube_r17;
	public ModelPart cube_r18;
	public ModelPart cube_r19;
	public ModelPart useless_ass_panel;
	public ModelPart cube_r20;
	public ModelPart cube_r21;
	public ModelPart cube_r22;
	public ModelPart cube_r23;
	public ModelPart bone10;
	public ModelPart cube_r24;
	public ModelPart bone9;
	public ModelPart cube_r25;
	public ModelPart bone8;
	public ModelPart cube_r26;
	public ModelPart control_four;
	public ModelPart cube_r27;
	public ModelPart cube_r28;
	public ModelPart control_three;
	public ModelPart cube_r29;
	public ModelPart panel_three_lights;
	public ModelPart cube_r30;
	public ModelPart cube_r31;
	public ModelPart control_two;
	public ModelPart cube_r32;
	public ModelPart buttons;
	public ModelPart base_r1;
	public ModelPart cube_r33;
	public ModelPart bone7;
	public ModelPart base_r2;
	public ModelPart switches_r1;
	public ModelPart crank_lever;
	public ModelPart base_r3;
	public ModelPart bone6;
	public ModelPart lights_right_r1;
	public ModelPart dials;
	public ModelPart cube_r34;
	public ModelPart bone5;
	public ModelPart cube_r35;
	public ModelPart panel_two_glowies;
	public ModelPart cube_r36;
	public ModelPart ConsoleCollarOne6;
	public ModelPart corner_one_r1;
	public ModelPart corner_one_r2;
	public ModelPart corner_one_r3;
	public ModelPart cube_r37;
	public ModelPart cube_r38;
	public ModelPart cube_r39;
	public ModelPart ConsoleCollarTwo3;
	public ModelPart corner_one_r4;
	public ModelPart corner_one_r5;
	public ModelPart corner_one_r6;
	public ModelPart cube_r40;
	public ModelPart cube_r41;
	public ModelPart cube_r42;
	public ModelPart upper_console_two;
	public ModelPart corner_one_r7;
	public ModelPart corner_one_r8;
	public ModelPart corner_one_r9;
	public ModelPart cube_r43;
	public ModelPart cube_r44;
	public ModelPart cube_r45;
	public ModelPart upper_console_one;
	public ModelPart corner_one_r10;
	public ModelPart corner_one_r11;
	public ModelPart corner_one_r12;
	public ModelPart cube_r46;
	public ModelPart cube_r47;
	public ModelPart cube_r48;
	public ModelPart bone23;
	public ModelPart bone24;
	public ModelPart bone25;
	public ModelPart bone26;
	public ModelPart bone27;
	public ModelPart bone28;
	public ModelPart top_rotor;
	public ModelPart cube_r49;
	public ModelPart cube_r50;
	public ModelPart cube_r51;
	public ModelPart cube_r52;
	public ModelPart top_rotor_ring;
	public ModelPart cube_r53;
	public ModelPart cube_r54;
	public ModelPart cube_r55;
	public ModelPart bone35;
	public ModelPart bone36;
	public ModelPart bone37;
	public ModelPart bone38;
	public ModelPart bone39;
	public ModelPart bone40;
	public ModelPart bottom_rotor;
	public ModelPart cube_r56;
	public ModelPart cube_r57;
	public ModelPart cube_r58;
	public ModelPart cube_r59;
	public ModelPart cube_r60;
	public ModelPart cube_r61;
	public ModelPart bottom_rotor_ring;
	public ModelPart cube_r62;
	public ModelPart cube_r63;
	public ModelPart cube_r64;
	public ModelPart bone29;
	public ModelPart bone30;
	public ModelPart bone31;
	public ModelPart bone32;
	public ModelPart bone33;
	public ModelPart bone34;
	public ModelPart base;
	public ModelPart cube_r65;
	public ModelPart cube_r66;
	public ModelPart cube_r67;
	public ModelPart cube_r68;
	public ModelPart cube_r69;
	public ModelPart bone;
	public ModelPart cube_r70;
	public ModelPart cube_r71;
	public ModelPart bone4;
	public ModelPart corner_r1;
	public ModelPart corner_r2;
	public ModelPart bone3;
	public ModelPart corner_r3;
	public ModelPart corner_r4;
	public ModelPart bone2;
	public ModelPart corner_r5;
	public ModelPart corner_r6;
	public ModelPart panel6;
	public ModelPart cube_r72;
	public ModelPart panel5;
	public ModelPart cube_r73;
	public ModelPart panel4;
	public ModelPart cube_r74;
	public ModelPart panel3;
	public ModelPart cube_r75;
	public ModelPart panel2;
	public ModelPart cube_r76;
	public ModelPart panel;
	public ModelPart cube_r77;
	public ModelPart console_collar_two;
	public ModelPart corner_one_r13;
	public ModelPart corner_one_r14;
	public ModelPart corner_one_r15;
	public ModelPart cube_r78;
	public ModelPart cube_r79;
	public ModelPart cube_r80;
	public ModelPart bone17;
	public ModelPart bone18;
	public ModelPart bone19;
	public ModelPart bone20;
	public ModelPart bone21;
	public ModelPart bone22;
	public ModelPart console_collar_one;
	public ModelPart corner_one_r16;
	public ModelPart corner_one_r17;
	public ModelPart corner_one_r18;
	public ModelPart cube_r81;
	public ModelPart cube_r82;
	public ModelPart cube_r83;
	public ModelPart console_corners;
	public ModelPart corner_r7;
	public ModelPart corner_r8;
	public ModelPart corner_r9;
	public ModelPart corner_r10;
	public ModelPart corner_r11;
	public ModelPart corner_r12;
	public ModelPart under_console_struts;
	public ModelPart cube_r84;
	public ModelPart cube_r85;
	public ModelPart cube_r86;
	public ModelPart clawlegs3;
	public ModelPart cube_r87;
	public ModelPart cube_r88;
	public ModelPart cube_r89;
	public ModelPart clawlegs2;
	public ModelPart cube_r90;
	public ModelPart cube_r91;
	public ModelPart cube_r92;
	public ModelPart clawlegs;
	public ModelPart cube_r93;
	public ModelPart cube_r94;
	public ModelPart cube_r95;
	public ModelPart console_plinth_three;
	public ModelPart corner_one_r19;
	public ModelPart corner_one_r20;
	public ModelPart corner_one_r21;
	public ModelPart cube_r96;
	public ModelPart cube_r97;
	public ModelPart cube_r98;
	public ModelPart console_plinth_two;
	public ModelPart corner_one_r22;
	public ModelPart corner_one_r23;
	public ModelPart corner_one_r24;
	public ModelPart corner_one_r25;
	public ModelPart corner_one_r26;
	public ModelPart corner_one_r27;
	public ModelPart cube_r99;
	public ModelPart cube_r100;
	public ModelPart cube_r101;
	public ModelPart console_plinth_two2;
	public ModelPart corner_one_r28;
	public ModelPart corner_one_r29;
	public ModelPart corner_one_r30;
	public ModelPart corner_one_r31;
	public ModelPart corner_one_r32;
	public ModelPart corner_one_r33;
	public ModelPart cube_r102;
	public ModelPart cube_r103;
	public ModelPart cube_r104;
	public ModelPart bone11;
	public ModelPart bone12;
	public ModelPart bone13;
	public ModelPart bone14;
	public ModelPart bone15;
	public ModelPart bone16;
	public ModelPart console_plinth_one;
	public ModelPart corner_one_r34;
	public ModelPart corner_one_r35;
	public ModelPart corner_one_r36;
	public ModelPart corner_one_r37;
	public ModelPart corner_one_r38;
	public ModelPart corner_one_r39;
	public ModelPart cube_r105;
	public ModelPart cube_r106;
	public ModelPart cube_r107;


	public TempConsoleModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.console = root.getChild("console");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData console = modelPartData.addChild("console", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r1 = console.addChild("cube_r1", ModelPartBuilder.create().uv(0, 88).cuboid(-3.3F, -8.1567F, 13.3478F, 7.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -20.7F, -0.8F, -0.7418F, -1.0472F, 0.0F));

		ModelPartData panel_three_small_lights_r1 = console.addChild("panel_three_small_lights_r1", ModelPartBuilder.create().uv(148, 139).cuboid(-5.625F, -7.6067F, 11.8478F, 1.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -20.7F, -0.8F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData cube_r2 = console.addChild("cube_r2", ModelPartBuilder.create().uv(75, 0).cuboid(-3.35F, 1.3F, -2.5F, 8.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(12.3022F, -19.5876F, -6.1706F, -0.7418F, 2.0944F, 0.0F));

		ModelPartData cube_r3 = console.addChild("cube_r3", ModelPartBuilder.create().uv(108, 157).cuboid(-2.25F, -8.0067F, 14.5478F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.95F, -21.45F, -0.8F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData bone49 = console.addChild("bone49", ModelPartBuilder.create(), ModelTransform.pivot(-6.2387F, -21.4261F, -7.1154F));

		ModelPartData cube_r4 = bone49.addChild("cube_r4", ModelPartBuilder.create().uv(135, 25).cuboid(-2.375F, -7.9067F, 6.5728F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(8.1887F, -0.0239F, 6.3154F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData bone48 = console.addChild("bone48", ModelPartBuilder.create(), ModelTransform.pivot(-7.7387F, -21.4261F, -4.5174F));

		ModelPartData cube_r5 = bone48.addChild("cube_r5", ModelPartBuilder.create().uv(153, 5).cuboid(0.625F, -7.9067F, 6.5728F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(9.6887F, -0.0239F, 3.7174F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData bone47 = console.addChild("bone47", ModelPartBuilder.create(), ModelTransform.pivot(-9.2387F, -21.4261F, -1.9193F));

		ModelPartData cube_r6 = bone47.addChild("cube_r6", ModelPartBuilder.create().uv(152, 53).cuboid(3.625F, -7.9067F, 6.5728F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(11.1887F, -0.0239F, 1.1193F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData bone46 = console.addChild("bone46", ModelPartBuilder.create(), ModelTransform.pivot(-8.1191F, -19.993F, -8.9988F));

		ModelPartData cube_r7 = bone46.addChild("cube_r7", ModelPartBuilder.create().uv(105, 212).cuboid(-3.8F, -7.6067F, 7.3478F, 2.5F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(9.1441F, -0.707F, 8.2238F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData bone45 = console.addChild("bone45", ModelPartBuilder.create(), ModelTransform.pivot(-9.3691F, -19.993F, -6.8338F));

		ModelPartData cube_r8 = bone45.addChild("cube_r8", ModelPartBuilder.create().uv(102, 212).cuboid(-1.3F, -7.6067F, 7.3478F, 2.5F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(10.3941F, -0.707F, 6.0588F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData bone44 = console.addChild("bone44", ModelPartBuilder.create(), ModelTransform.pivot(-10.6191F, -19.993F, -4.6687F));

		ModelPartData cube_r9 = bone44.addChild("cube_r9", ModelPartBuilder.create().uv(105, 212).cuboid(1.2F, -7.6067F, 7.3478F, 2.5F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(11.6441F, -0.707F, 3.8937F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData bone43 = console.addChild("bone43", ModelPartBuilder.create(), ModelTransform.pivot(-11.8691F, -19.993F, -2.5036F));

		ModelPartData cube_r10 = bone43.addChild("cube_r10", ModelPartBuilder.create().uv(102, 212).cuboid(3.7F, -7.6067F, 7.3478F, 2.5F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(12.8941F, -0.707F, 1.7286F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData bone41 = console.addChild("bone41", ModelPartBuilder.create(), ModelTransform.pivot(-11.9607F, -17.7881F, 5.1817F));

		ModelPartData cube_r11 = bone41.addChild("cube_r11", ModelPartBuilder.create().uv(3, 48).cuboid(-1.8F, -7.9567F, 11.9478F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(12.9607F, -2.9119F, -5.9817F, -0.7418F, -1.0472F, 0.0F));

		ModelPartData bone42 = console.addChild("bone42", ModelPartBuilder.create(), ModelTransform.pivot(-10.4607F, -17.7881F, 7.7798F));

		ModelPartData cube_r12 = bone42.addChild("cube_r12", ModelPartBuilder.create().uv(3, 48).cuboid(1.2F, -7.9567F, 11.9478F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(11.4607F, -2.9119F, -8.5798F, -0.7418F, -1.0472F, 0.0F));

		ModelPartData glowies = console.addChild("glowies", ModelPartBuilder.create(), ModelTransform.pivot(-11.2607F, -17.8381F, 6.5058F));

		ModelPartData cube_r13 = glowies.addChild("cube_r13", ModelPartBuilder.create().uv(1, 48).cuboid(-0.3F, -7.9067F, 11.9478F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(12.2107F, -2.9119F, -7.2808F, -0.7418F, -1.0472F, 0.0F));

		ModelPartData fast_return_switch = console.addChild("fast_return_switch", ModelPartBuilder.create(), ModelTransform.pivot(1.95F, -21.45F, -0.8F));

		ModelPartData fast_return_switch_r1 = fast_return_switch.addChild("fast_return_switch_r1", ModelPartBuilder.create().uv(79, 216).cuboid(-1.7F, -7.1317F, 4.9728F, 1.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(-0.95F, 0.75F, 0.0F, -0.7418F, 1.0472F, 0.0F));

		ModelPartData exterior_facing = console.addChild("exterior_facing", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData exterior_facing_r1 = exterior_facing.addChild("exterior_facing_r1", ModelPartBuilder.create().uv(128, 167).cuboid(4.4F, -8.6067F, 9.1228F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -20.7F, -0.8F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData dimension_changer = console.addChild("dimension_changer", ModelPartBuilder.create(), ModelTransform.pivot(9.2366F, -19.7411F, 8.8052F));

		ModelPartData dimension_changer_r1 = dimension_changer.addChild("dimension_changer_r1", ModelPartBuilder.create().uv(153, 18).cuboid(3.5F, 0.3F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7418F, 1.0472F, 0.0F));

		ModelPartData door_control = console.addChild("door_control", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -20.7F, -0.8F));

		ModelPartData door_control_r1 = door_control.addChild("door_control_r1", ModelPartBuilder.create().uv(143, 167).cuboid(-0.3F, -8.6567F, 8.8478F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7418F, -1.0472F, 0.0F));

		ModelPartData refuller = console.addChild("refuller", ModelPartBuilder.create(), ModelTransform.pivot(-12.7164F, -16.8731F, 7.3501F));

		ModelPartData refuller_r1 = refuller.addChild("refuller_r1", ModelPartBuilder.create().uv(0, 11).cuboid(-3.0F, -0.6F, -0.5F, 6.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.1745F, -1.0472F, 0.0F));

		ModelPartData monitor = console.addChild("monitor", ModelPartBuilder.create(), ModelTransform.pivot(12.3022F, -19.5876F, -6.1706F));

		ModelPartData monitor_r1 = monitor.addChild("monitor_r1", ModelPartBuilder.create().uv(134, 0).cuboid(-1.775F, 1.3F, -6.0F, 5.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7418F, 2.0944F, 0.0F));

		ModelPartData communicator = console.addChild("communicator", ModelPartBuilder.create(), ModelTransform.pivot(-0.075F, -19.0F, -1.2F));

		ModelPartData communicator_r1 = communicator.addChild("communicator_r1", ModelPartBuilder.create().uv(60, 54).cuboid(-2.0145F, -0.59F, -1.5135F, 4.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(5.2855F, 3.994F, 19.3645F, -0.0436F, 0.0F, 0.0F));

		ModelPartData randomiser = console.addChild("randomiser", ModelPartBuilder.create(), ModelTransform.pivot(1.95F, -21.45F, -0.8F));

		ModelPartData randomiser_r1 = randomiser.addChild("randomiser_r1", ModelPartBuilder.create().uv(159, 133).cuboid(6.25F, -8.0067F, 14.0478F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData increment = console.addChild("increment", ModelPartBuilder.create(), ModelTransform.pivot(12.3022F, -19.5876F, -6.1706F));

		ModelPartData increment_r1 = increment.addChild("increment_r1", ModelPartBuilder.create().uv(55, 105).cuboid(-2.0F, 0.8F, 1.475F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7418F, 2.0944F, 0.0F));

		ModelPartData z = console.addChild("z", ModelPartBuilder.create(), ModelTransform.pivot(12.3022F, -19.5876F, -6.1706F));

		ModelPartData z_r1 = z.addChild("z_r1", ModelPartBuilder.create().uv(108, 0).cuboid(-0.25F, 0.8F, -0.025F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7418F, 2.0944F, 0.0F));

		ModelPartData y = console.addChild("y", ModelPartBuilder.create(), ModelTransform.pivot(12.3022F, -19.5876F, -6.1706F));

		ModelPartData y_r1 = y.addChild("y_r1", ModelPartBuilder.create().uv(9, 107).cuboid(1.25F, 0.8F, -0.025F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7418F, 2.0944F, 0.0F));

		ModelPartData x = console.addChild("x", ModelPartBuilder.create(), ModelTransform.pivot(12.3022F, -19.5876F, -6.1706F));

		ModelPartData x_r1 = x.addChild("x_r1", ModelPartBuilder.create().uv(92, 122).cuboid(2.75F, 0.8F, -0.025F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7418F, 2.0944F, 0.0F));

		ModelPartData handbrake = console.addChild("handbrake", ModelPartBuilder.create(), ModelTransform.pivot(4.7098F, -18.2762F, -15.4415F));

		ModelPartData lever_section_2_r1 = handbrake.addChild("lever_section_2_r1", ModelPartBuilder.create().uv(138, 167).cuboid(-0.5F, -1.9F, 0.1F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, -4.0F, -0.829F, -0.0107F, 0.0091F));

		ModelPartData lever_section_1_r1 = handbrake.addChild("lever_section_1_r1", ModelPartBuilder.create().uv(83, 39).cuboid(-0.5F, -3.5F, 0.3F, 1.0F, 4.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.0133F, 1.6435F, -2.453F, -0.829F, -0.0107F, 0.0091F));

		ModelPartData throttle = console.addChild("throttle", ModelPartBuilder.create().uv(30, 162).cuboid(-0.5F, -3.9092F, -0.4945F, 1.0F, 4.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.pivot(-4.52F, -17.8958F, 12.0975F));

		ModelPartData lamp = console.addChild("lamp", ModelPartBuilder.create(), ModelTransform.pivot(-8.9458F, -22.5354F, 8.9259F));

		ModelPartData cube_r14 = lamp.addChild("cube_r14", ModelPartBuilder.create().uv(22, 201).cuboid(-1.75F, -0.575F, -0.725F, 4.0F, 1.0F, 1.0F, new Dilation(-0.3F))
		.uv(21, 198).cuboid(-1.75F, -0.575F, -0.725F, 4.0F, 1.0F, 1.0F, new Dilation(-0.2F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r15 = lamp.addChild("cube_r15", ModelPartBuilder.create().uv(13, 195).cuboid(-0.5F, -1.4752F, -0.498F, 1.0F, 2.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(1.6485F, -0.3839F, -0.692F, -1.5708F, -1.0472F, 0.0F));

		ModelPartData cube_r16 = lamp.addChild("cube_r16", ModelPartBuilder.create().uv(15, 204).cuboid(-0.5017F, -0.175F, -0.575F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(1.6485F, -0.3839F, -0.692F, -1.1345F, -1.0472F, 0.0F));

		ModelPartData cube_r17 = lamp.addChild("cube_r17", ModelPartBuilder.create().uv(13, 195).cuboid(-0.5005F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(0.5524F, -0.3178F, -0.0591F, 2.0071F, 1.0472F, 3.1416F));

		ModelPartData cube_r18 = lamp.addChild("cube_r18", ModelPartBuilder.create().uv(8, 200).cuboid(-0.525F, -1.0F, -0.8F, 1.0F, 2.0F, 1.0F, new Dilation(-0.3F)), ModelTransform.of(1.9422F, 0.4F, -0.8327F, -0.3054F, -1.0472F, 0.0F));

		ModelPartData cube_r19 = lamp.addChild("cube_r19", ModelPartBuilder.create().uv(7, 196).cuboid(3.2F, -7.8567F, 6.9478F, 1.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(9.9458F, 1.8354F, -9.7259F, -0.7418F, -1.0472F, 0.0F));

		ModelPartData useless_ass_panel = console.addChild("useless_ass_panel", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -20.7F, -0.8F));

		ModelPartData cube_r20 = useless_ass_panel.addChild("cube_r20", ModelPartBuilder.create().uv(130, 33).cuboid(-9.75F, 0.525F, 4.475F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(9, 135).cuboid(1.725F, 0.525F, 4.475F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(98, 39).cuboid(1.25F, 0.6F, 3.75F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(148, 125).cuboid(-10.25F, 0.6F, 3.75F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(9.8904F, -0.7301F, 3.0544F, -0.7418F, 1.0472F, 0.0F));

		ModelPartData cube_r21 = useless_ass_panel.addChild("cube_r21", ModelPartBuilder.create().uv(148, 130).cuboid(-7.5F, 0.0F, -2.5F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(132, 148).cuboid(-1.5F, 0.0F, -2.5F, 2.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(9.8904F, -0.7301F, 3.0544F, -0.3927F, 1.0472F, 0.0F));

		ModelPartData cube_r22 = useless_ass_panel.addChild("cube_r22", ModelPartBuilder.create().uv(41, 84).cuboid(-2.45F, -6.7567F, 4.6978F, 2.0F, 0.0F, 2.0F, new Dilation(0.0F))
		.uv(94, 0).cuboid(-2.2F, -6.8567F, 6.6978F, 2.0F, 0.0F, 2.0F, new Dilation(0.0F))
		.uv(66, 116).cuboid(-5.7F, -6.8567F, 9.4478F, 9.0F, 0.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, -0.7418F, 1.0472F, 0.0F));

		ModelPartData cube_r23 = useless_ass_panel.addChild("cube_r23", ModelPartBuilder.create().uv(140, 32).cuboid(1.5F, 0.3F, 3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(43, 142).cuboid(-0.5F, 0.3F, 3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(147, 94).cuboid(3.5F, 0.3F, 3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(75, 151).cuboid(5.5F, 0.3F, 3.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(152, 37).cuboid(1.5F, 0.3F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(152, 49).cuboid(-0.5F, 0.3F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(152, 62).cuboid(3.5F, 0.3F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(153, 10).cuboid(5.5F, 0.3F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(9, 155).cuboid(5.5F, 0.3F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(157, 37).cuboid(1.5F, 0.3F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(157, 49).cuboid(-0.5F, 0.3F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(8.2366F, 0.9589F, 9.6052F, -0.7418F, 1.0472F, 0.0F));

		ModelPartData bone10 = console.addChild("bone10", ModelPartBuilder.create(), ModelTransform.pivot(12.3022F, -21.5876F, -6.1706F));

		ModelPartData cube_r24 = bone10.addChild("cube_r24", ModelPartBuilder.create().uv(41, 88).cuboid(-2.5F, 1.225F, 1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F))
		.uv(113, 0).cuboid(2.75F, 0.8F, 1.475F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(120, 33).cuboid(1.25F, 0.8F, 1.475F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(33, 94).cuboid(-3.375F, 1.3F, -1.0F, 8.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.7418F, 2.0944F, 0.0F));

		ModelPartData bone9 = console.addChild("bone9", ModelPartBuilder.create(), ModelTransform.pivot(12.3022F, -21.5876F, -6.1706F));

		ModelPartData cube_r25 = bone9.addChild("cube_r25", ModelPartBuilder.create().uv(157, 62).cuboid(-5.8F, 0.8F, 1.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(50, 88).cuboid(-6.25F, 1.3F, 1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.7418F, 2.0944F, 0.0F));

		ModelPartData bone8 = console.addChild("bone8", ModelPartBuilder.create(), ModelTransform.pivot(12.3022F, -21.5876F, -6.1706F));

		ModelPartData cube_r26 = bone8.addChild("cube_r26", ModelPartBuilder.create().uv(103, 0).cuboid(6.0F, 0.8F, 1.475F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(52, 101).cuboid(5.5F, 1.3F, 1.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.7418F, 2.0944F, 0.0F));

		ModelPartData control_four = console.addChild("control_four", ModelPartBuilder.create(), ModelTransform.pivot(1.95F, -23.45F, -0.8F));

		ModelPartData cube_r27 = control_four.addChild("cube_r27", ModelPartBuilder.create().uv(158, 18).cuboid(0.75F, -8.0067F, 14.5478F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData cube_r28 = control_four.addChild("cube_r28", ModelPartBuilder.create().uv(33, 101).cuboid(-4.275F, -7.6067F, 11.8478F, 7.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-0.95F, 2.75F, 0.0F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData control_three = console.addChild("control_three", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -22.7F, -0.8F));

		ModelPartData cube_r29 = control_three.addChild("cube_r29", ModelPartBuilder.create().uv(66, 110).cuboid(-3.8F, -7.6067F, 7.3478F, 10.0F, 0.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData panel_three_lights = console.addChild("panel_three_lights", ModelPartBuilder.create(), ModelTransform.pivot(1.95F, -23.45F, -0.8F));

		ModelPartData cube_r30 = panel_three_lights.addChild("cube_r30", ModelPartBuilder.create().uv(144, 53).cuboid(-2.375F, -7.9067F, 6.5728F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, 2.3998F, -1.0472F, 3.1416F));

		ModelPartData cube_r31 = panel_three_lights.addChild("cube_r31", ModelPartBuilder.create().uv(144, 53).cuboid(0.625F, -7.9067F, 6.5728F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(144, 53).cuboid(3.625F, -7.9067F, 6.5728F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData control_two = console.addChild("control_two", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -22.7F, -0.8F));

		ModelPartData cube_r32 = control_two.addChild("cube_r32", ModelPartBuilder.create().uv(145, 108).cuboid(-1.3F, -8.1567F, 7.8478F, 3.0F, 1.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.7418F, -1.0472F, 0.0F));

		ModelPartData buttons = console.addChild("buttons", ModelPartBuilder.create(), ModelTransform.pivot(1.115F, -18.0429F, -17.24F));

		ModelPartData base_r1 = buttons.addChild("base_r1", ModelPartBuilder.create().uv(33, 106).cuboid(-11.6F, -0.96F, 0.18F, 3.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(4.4676F, 0.3773F, 3.3941F, -0.829F, -0.0107F, 0.0091F));

		ModelPartData cube_r33 = buttons.addChild("cube_r33", ModelPartBuilder.create().uv(110, 170).cuboid(-5.5F, -2.925F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(105, 170).cuboid(-6.8F, -2.925F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(170, 158).cuboid(-5.5F, -4.225F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(128, 170).cuboid(-6.8F, -4.225F, 1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.829F, 0.0F, 0.0F));

		ModelPartData bone7 = console.addChild("bone7", ModelPartBuilder.create(), ModelTransform.pivot(0.3826F, -19.6656F, -13.8459F));

		ModelPartData base_r2 = bone7.addChild("base_r2", ModelPartBuilder.create().uv(60, 39).cuboid(-1.84F, -4.54F, -0.02F, 3.0F, 7.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.829F, 0.0F, 0.0F));

		ModelPartData switches_r1 = bone7.addChild("switches_r1", ModelPartBuilder.create().uv(159, 125).cuboid(-0.5F, -3.5F, -0.5F, 1.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.3176F, 1.6204F, 0.355F, -0.9959F, 0.4981F, -0.6353F));

		ModelPartData crank_lever = console.addChild("crank_lever", ModelPartBuilder.create(), ModelTransform.pivot(4.5826F, -19.6656F, -13.8459F));

		ModelPartData base_r3 = crank_lever.addChild("base_r3", ModelPartBuilder.create().uv(90, 116).cuboid(-0.94F, -0.24F, -1.42F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 162).cuboid(-1.44F, -0.74F, -0.42F, 3.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.829F, -0.0107F, 0.0091F));

		ModelPartData bone6 = console.addChild("bone6", ModelPartBuilder.create(), ModelTransform.pivot(4.5826F, -19.6656F, -13.8459F));

		ModelPartData lights_right_r1 = bone6.addChild("lights_right_r1", ModelPartBuilder.create().uv(33, 122).cuboid(5.8F, -2.4F, 0.1F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F))
		.uv(0, 65).cuboid(-2.4F, -2.4F, 0.1F, 4.0F, 3.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-8.2376F, 0.1919F, 1.8456F, -0.829F, 0.0F, 0.0F));

		ModelPartData dials = bone6.addChild("dials", ModelPartBuilder.create(), ModelTransform.pivot(-4.7026F, -3.6492F, 3.3765F));

		ModelPartData cube_r34 = dials.addChild("cube_r34", ModelPartBuilder.create().uv(168, 18).cuboid(2.2F, -2.1F, 0.4F, 2.0F, 2.0F, 0.0F, new Dilation(0.0F))
		.uv(64, 168).cuboid(-0.8F, -2.1F, 0.4F, 2.0F, 2.0F, 0.0F, new Dilation(0.0F))
		.uv(169, 21).cuboid(-3.8F, -2.1F, 0.4F, 2.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.829F, 0.0F, 0.0F));

		ModelPartData bone5 = console.addChild("bone5", ModelPartBuilder.create(), ModelTransform.pivot(1.95F, -23.45F, -0.8F));

		ModelPartData cube_r35 = bone5.addChild("cube_r35", ModelPartBuilder.create().uv(91, 110).cuboid(5.725F, -7.7067F, 13.5478F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.7418F, -2.0944F, 0.0F));

		ModelPartData panel_two_glowies = console.addChild("panel_two_glowies", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -22.7F, -0.8F));

		ModelPartData cube_r36 = panel_two_glowies.addChild("cube_r36", ModelPartBuilder.create().uv(0, 29).cuboid(-1.8F, -8.1567F, 6.2478F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 47).cuboid(-1.8F, -7.9817F, 11.9478F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
		.uv(83, 39).mirrored().cuboid(3.2F, -8.1567F, 9.6478F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)).mirrored(false)
		.uv(120, 25).mirrored().cuboid(-6.8F, -8.1567F, 9.6478F, 4.0F, 1.0F, 6.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.7418F, -1.0472F, 0.0F));

		ModelPartData ConsoleCollarOne6 = console.addChild("ConsoleCollarOne6", ModelPartBuilder.create(), ModelTransform.of(0.0F, -74.5F, 0.0F, 3.1416F, 0.0F, 0.0F));

		ModelPartData corner_one_r1 = ConsoleCollarOne6.addChild("corner_one_r1", ModelPartBuilder.create().uv(9, 122).cuboid(-0.45F, -3.0F, -9.0121F, 1.0F, 7.0F, 1.0F, new Dilation(0.1F))
		.uv(147, 81).cuboid(-0.4482F, -3.0F, 7.837F, 1.0F, 7.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.0626F, -16.425F, 0.0698F, 0.0F, -0.5236F, 0.0F));

		ModelPartData corner_one_r2 = ConsoleCollarOne6.addChild("corner_one_r2", ModelPartBuilder.create().uv(75, 138).cuboid(-0.55F, -3.0F, 7.9129F, 1.0F, 7.0F, 1.0F, new Dilation(0.1F))
		.uv(9, 142).cuboid(-0.55F, -3.0F, -8.7621F, 1.0F, 7.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0874F, -16.425F, 0.0698F, 0.0F, -1.5708F, 0.0F));

		ModelPartData corner_one_r3 = ConsoleCollarOne6.addChild("corner_one_r3", ModelPartBuilder.create().uv(9, 94).cuboid(-0.45F, -3.0F, -8.9871F, 1.0F, 7.0F, 1.0F, new Dilation(0.1F))
		.uv(108, 144).cuboid(-0.4982F, -3.0F, 7.712F, 1.0F, 7.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0624F, -16.425F, 0.1198F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r37 = ConsoleCollarOne6.addChild("cube_r37", ModelPartBuilder.create().uv(0, 94).cuboid(-4.044F, -4.218F, -7.996F, 8.0F, 7.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -15.3F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r38 = ConsoleCollarOne6.addChild("cube_r38", ModelPartBuilder.create().uv(33, 110).cuboid(-4.044F, -4.218F, -7.996F, 8.0F, 7.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -15.3F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r39 = ConsoleCollarOne6.addChild("cube_r39", ModelPartBuilder.create().uv(82, 116).cuboid(-4.044F, -4.218F, -7.996F, 8.0F, 7.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -15.3F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData ConsoleCollarTwo3 = console.addChild("ConsoleCollarTwo3", ModelPartBuilder.create(), ModelTransform.pivot(0.095F, -2.9F, -0.206F));

		ModelPartData corner_one_r4 = ConsoleCollarTwo3.addChild("corner_one_r4", ModelPartBuilder.create().uv(158, 25).cuboid(-0.5232F, -7.65F, 5.687F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F))
		.uv(120, 116).cuboid(-0.5232F, -7.65F, -6.788F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1576F, -48.925F, 0.2758F, 0.0F, -0.5236F, 0.0F));

		ModelPartData corner_one_r5 = ConsoleCollarTwo3.addChild("corner_one_r5", ModelPartBuilder.create().uv(132, 153).cuboid(-0.4482F, -7.65F, 5.687F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F))
		.uv(137, 153).cuboid(-0.4482F, -7.65F, -6.763F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1576F, -48.925F, 0.2758F, 0.0F, 0.5236F, 0.0F));

		ModelPartData corner_one_r6 = ConsoleCollarTwo3.addChild("corner_one_r6", ModelPartBuilder.create().uv(142, 153).cuboid(-0.5482F, -7.65F, 5.687F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F))
		.uv(115, 116).cuboid(-0.5732F, -7.65F, -6.738F, 1.0F, 8.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1576F, -48.925F, 0.2758F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r40 = ConsoleCollarTwo3.addChild("cube_r40", ModelPartBuilder.create().uv(136, 158).cuboid(-2.96F, -9.418F, -6.282F, 6.0F, 8.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.403F, -47.14F, 0.338F, -3.1416F, -2.0944F, 3.1416F));

		ModelPartData cube_r41 = ConsoleCollarTwo3.addChild("cube_r41", ModelPartBuilder.create().uv(0, 164).cuboid(-3.26F, -9.418F, -6.182F, 6.0F, 8.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.403F, -47.14F, 0.338F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r42 = ConsoleCollarTwo3.addChild("cube_r42", ModelPartBuilder.create().uv(66, 164).cuboid(-3.26F, -9.418F, -5.882F, 6.0F, 8.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.403F, -47.14F, 0.338F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData upper_console_two = console.addChild("upper_console_two", ModelPartBuilder.create(), ModelTransform.of(0.0F, -78.5F, 0.0F, 3.1416F, 0.0F, 0.0F));

		ModelPartData corner_one_r7 = upper_console_two.addChild("corner_one_r7", ModelPartBuilder.create().uv(42, 122).cuboid(-0.45F, -3.0F, -9.0121F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F))
		.uv(120, 128).cuboid(-0.4482F, -3.0F, 7.837F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.0626F, -25.425F, 0.0698F, 0.0F, -0.5236F, 0.0F));

		ModelPartData corner_one_r8 = upper_console_two.addChild("corner_one_r8", ModelPartBuilder.create().uv(66, 122).cuboid(-0.55F, -3.0F, 7.9129F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F))
		.uv(71, 122).cuboid(-0.55F, -3.0F, -8.7621F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0874F, -25.425F, 0.0698F, 0.0F, -1.5708F, 0.0F));

		ModelPartData corner_one_r9 = upper_console_two.addChild("corner_one_r9", ModelPartBuilder.create().uv(9, 65).cuboid(-0.45F, -3.0F, -8.9871F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F))
		.uv(92, 128).cuboid(-0.4982F, -3.0F, 7.712F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0624F, -25.425F, 0.1198F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r43 = upper_console_two.addChild("cube_r43", ModelPartBuilder.create().uv(142, 101).cuboid(-4.044F, -4.218F, -7.996F, 8.0F, 2.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -24.3F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r44 = upper_console_two.addChild("cube_r44", ModelPartBuilder.create().uv(66, 144).cuboid(-4.044F, -4.218F, -7.996F, 8.0F, 2.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -24.3F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r45 = upper_console_two.addChild("cube_r45", ModelPartBuilder.create().uv(99, 148).cuboid(-4.044F, -4.218F, -7.996F, 8.0F, 2.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -24.3F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData upper_console_one = console.addChild("upper_console_one", ModelPartBuilder.create(), ModelTransform.of(-0.205F, -100.2F, -0.206F, 0.0F, 0.0F, -3.1416F));

		ModelPartData corner_one_r10 = upper_console_one.addChild("corner_one_r10", ModelPartBuilder.create().uv(100, 167).cuboid(-0.5232F, -4.65F, 5.687F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(25, 167).cuboid(-0.5232F, -4.65F, -6.788F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1576F, -47.925F, 0.2758F, 0.0F, -0.5236F, 0.0F));

		ModelPartData corner_one_r11 = upper_console_one.addChild("corner_one_r11", ModelPartBuilder.create().uv(30, 167).cuboid(-0.4482F, -4.65F, 5.687F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(35, 167).cuboid(-0.4482F, -4.65F, -6.763F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1576F, -47.925F, 0.2758F, 0.0F, 0.5236F, 0.0F));

		ModelPartData corner_one_r12 = upper_console_one.addChild("corner_one_r12", ModelPartBuilder.create().uv(73, 167).cuboid(-0.5482F, -4.65F, 5.687F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(95, 167).cuboid(-0.5732F, -4.65F, -6.738F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1576F, -47.925F, 0.2758F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r46 = upper_console_one.addChild("cube_r46", ModelPartBuilder.create().uv(159, 62).cuboid(-2.96F, -6.418F, -6.282F, 6.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.403F, -46.14F, 0.338F, -3.1416F, -2.0944F, 3.1416F));

		ModelPartData cube_r47 = upper_console_one.addChild("cube_r47", ModelPartBuilder.create().uv(103, 167).cuboid(-3.26F, -6.418F, -6.182F, 6.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.403F, -46.14F, 0.338F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r48 = upper_console_one.addChild("cube_r48", ModelPartBuilder.create().uv(161, 167).cuboid(-3.26F, -6.418F, -5.882F, 6.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.403F, -46.14F, 0.338F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData bone23 = upper_console_one.addChild("bone23", ModelPartBuilder.create().uv(125, 33).cuboid(-2.65F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.55F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.55F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.pivot(-0.095F, -49.825F, 0.206F));

		ModelPartData bone24 = upper_console_one.addChild("bone24", ModelPartBuilder.create().uv(125, 33).cuboid(-2.625F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.525F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.575F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.095F, -49.825F, 0.206F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone25 = upper_console_one.addChild("bone25", ModelPartBuilder.create().uv(125, 33).cuboid(-2.625F, -2.0F, 5.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.525F, -2.0F, 5.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.575F, -2.0F, 5.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.095F, -49.825F, 0.206F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone26 = upper_console_one.addChild("bone26", ModelPartBuilder.create().uv(125, 33).cuboid(-2.625F, -2.0F, 5.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.525F, -2.0F, 5.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.575F, -2.0F, 5.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.095F, -49.825F, 0.206F, 0.0F, 2.0944F, 0.0F));

		ModelPartData bone27 = upper_console_one.addChild("bone27", ModelPartBuilder.create().uv(125, 33).cuboid(-2.625F, -2.0F, 5.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.525F, -2.0F, 5.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.575F, -2.0F, 5.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.095F, -49.825F, 0.206F, 0.0F, 3.1416F, 0.0F));

		ModelPartData bone28 = upper_console_one.addChild("bone28", ModelPartBuilder.create().uv(125, 33).cuboid(-2.6F, -2.0F, 5.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.5F, -2.0F, 5.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.6F, -2.0F, 5.2F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.095F, -49.825F, 0.206F, 0.0F, -2.0944F, 0.0F));

		ModelPartData top_rotor = console.addChild("top_rotor", ModelPartBuilder.create().uv(175, 183).cuboid(-0.8401F, -17.16F, -3.4292F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F))
		.uv(165, 183).cuboid(-3.9254F, -17.16F, -0.2382F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F))
		.uv(160, 183).cuboid(2.2513F, -17.16F, -0.2626F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F))
		.uv(133, 183).cuboid(-0.834F, -17.16F, 2.8534F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.325F, -55.5F, -0.325F, 0.0F, 0.0F, -3.1416F));

		ModelPartData cube_r49 = top_rotor.addChild("cube_r49", ModelPartBuilder.create().uv(123, 183).cuboid(-0.375F, -10.0F, -0.625F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.3112F, -7.16F, 2.48F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r50 = top_rotor.addChild("cube_r50", ModelPartBuilder.create().uv(128, 183).cuboid(-0.125F, -10.0F, -0.825F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.347F, -7.16F, 2.9981F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r51 = top_rotor.addChild("cube_r51", ModelPartBuilder.create().uv(170, 183).cuboid(-0.525F, -10.0F, -0.85F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(2.137F, -7.16F, -1.9058F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r52 = top_rotor.addChild("cube_r52", ModelPartBuilder.create().uv(118, 183).cuboid(-1.075F, -10.0F, -0.4F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-2.5211F, -7.16F, -2.4239F, 0.0F, 1.5708F, 0.0F));

		ModelPartData top_rotor_ring = top_rotor.addChild("top_rotor_ring", ModelPartBuilder.create(), ModelTransform.pivot(-0.155F, 25.35F, 0.531F));

		ModelPartData cube_r53 = top_rotor_ring.addChild("cube_r53", ModelPartBuilder.create().uv(135, 25).cuboid(-2.96F, -2.418F, -4.718F, 6.0F, 1.0F, 10.0F, new Dilation(-0.2F)), ModelTransform.of(-0.403F, -34.14F, -0.338F, 3.1416F, 2.0944F, 3.1416F));

		ModelPartData cube_r54 = top_rotor_ring.addChild("cube_r54", ModelPartBuilder.create().uv(60, 39).cuboid(-3.26F, -2.418F, -4.818F, 6.0F, 1.0F, 10.0F, new Dilation(-0.2F)), ModelTransform.of(-0.403F, -34.14F, -0.338F, 3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r55 = top_rotor_ring.addChild("cube_r55", ModelPartBuilder.create().uv(115, 116).cuboid(-3.26F, -2.418F, -5.118F, 6.0F, 1.0F, 10.0F, new Dilation(-0.2F)), ModelTransform.of(-0.403F, -34.14F, -0.338F, 3.1416F, 0.0F, 3.1416F));

		ModelPartData bone35 = top_rotor_ring.addChild("bone35", ModelPartBuilder.create(), ModelTransform.pivot(-0.095F, -20.825F, -0.206F));

		ModelPartData bone36 = top_rotor_ring.addChild("bone36", ModelPartBuilder.create(), ModelTransform.of(-0.095F, -20.825F, -0.206F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone37 = top_rotor_ring.addChild("bone37", ModelPartBuilder.create(), ModelTransform.of(-0.095F, -20.825F, -0.206F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone38 = top_rotor_ring.addChild("bone38", ModelPartBuilder.create(), ModelTransform.of(-0.195F, -20.825F, -0.256F, 0.0F, -3.1416F, 0.0F));

		ModelPartData bone39 = top_rotor_ring.addChild("bone39", ModelPartBuilder.create(), ModelTransform.of(-0.195F, -20.825F, -0.256F, 0.0F, -2.0944F, 0.0F));

		ModelPartData bone40 = top_rotor_ring.addChild("bone40", ModelPartBuilder.create(), ModelTransform.of(-0.195F, -20.825F, -0.256F, 0.0F, 2.0944F, 0.0F));

		ModelPartData bottom_rotor = console.addChild("bottom_rotor", ModelPartBuilder.create().uv(103, 183).cuboid(2.66F, -13.16F, -1.31F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F))
		.uv(145, 179).cuboid(-1.54F, -13.16F, 2.87F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.075F, -22.0F, -0.325F));

		ModelPartData cube_r56 = bottom_rotor.addChild("cube_r56", ModelPartBuilder.create().uv(140, 179).cuboid(-0.5F, -8.0F, -1.0F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.68F, -5.16F, 3.37F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r57 = bottom_rotor.addChild("cube_r57", ModelPartBuilder.create().uv(155, 179).cuboid(-0.5F, -8.0F, -0.5F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-3.02F, -5.16F, -0.81F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r58 = bottom_rotor.addChild("cube_r58", ModelPartBuilder.create().uv(129, 0).cuboid(-0.5F, -8.0F, -0.5F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-3.02F, -5.16F, 1.29F, 0.0F, 3.1416F, 0.0F));

		ModelPartData cube_r59 = bottom_rotor.addChild("cube_r59", ModelPartBuilder.create().uv(150, 179).cuboid(-0.5F, -8.0F, -0.5F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.16F, -5.16F, 1.29F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r60 = bottom_rotor.addChild("cube_r60", ModelPartBuilder.create().uv(108, 183).cuboid(-0.5F, -8.0F, -0.5F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.04F, -5.16F, -2.89F, 0.0F, 1.5708F, 0.0F));

		ModelPartData cube_r61 = bottom_rotor.addChild("cube_r61", ModelPartBuilder.create().uv(113, 183).cuboid(-1.0F, -8.0F, -0.5F, 1.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.68F, -5.16F, -2.89F, 0.0F, 3.1416F, 0.0F));

		ModelPartData bottom_rotor_ring = bottom_rotor.addChild("bottom_rotor_ring", ModelPartBuilder.create(), ModelTransform.pivot(0.17F, 18.1F, 0.119F));

		ModelPartData cube_r62 = bottom_rotor_ring.addChild("cube_r62", ModelPartBuilder.create().uv(135, 25).cuboid(-2.96F, -2.418F, -5.282F, 6.0F, 1.0F, 10.0F, new Dilation(-0.2F)), ModelTransform.of(-0.403F, -23.14F, 0.338F, -3.1416F, -2.0944F, 3.1416F));

		ModelPartData cube_r63 = bottom_rotor_ring.addChild("cube_r63", ModelPartBuilder.create().uv(60, 39).cuboid(-3.26F, -2.418F, -5.182F, 6.0F, 1.0F, 10.0F, new Dilation(-0.2F)), ModelTransform.of(-0.403F, -23.14F, 0.338F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r64 = bottom_rotor_ring.addChild("cube_r64", ModelPartBuilder.create().uv(115, 116).cuboid(-3.26F, -2.418F, -4.882F, 6.0F, 1.0F, 10.0F, new Dilation(-0.2F)), ModelTransform.of(-0.403F, -23.14F, 0.338F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData bone29 = bottom_rotor_ring.addChild("bone29", ModelPartBuilder.create(), ModelTransform.pivot(-0.095F, -20.825F, 0.206F));

		ModelPartData bone30 = bottom_rotor_ring.addChild("bone30", ModelPartBuilder.create(), ModelTransform.of(-0.095F, -20.825F, 0.206F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone31 = bottom_rotor_ring.addChild("bone31", ModelPartBuilder.create(), ModelTransform.of(-0.095F, -20.825F, 0.206F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone32 = bottom_rotor_ring.addChild("bone32", ModelPartBuilder.create(), ModelTransform.of(-0.195F, -20.825F, 0.256F, 0.0F, 3.1416F, 0.0F));

		ModelPartData bone33 = bottom_rotor_ring.addChild("bone33", ModelPartBuilder.create(), ModelTransform.of(-0.195F, -20.825F, 0.256F, 0.0F, 2.0944F, 0.0F));

		ModelPartData bone34 = bottom_rotor_ring.addChild("bone34", ModelPartBuilder.create(), ModelTransform.of(-0.195F, -20.825F, 0.256F, 0.0F, -2.0944F, 0.0F));

		ModelPartData base = console.addChild("base", ModelPartBuilder.create().uv(60, 0).cuboid(-3.195F, -9.268F, 5.409F, 6.0F, 9.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.172F, -3.18F, -0.065F));

		ModelPartData cube_r65 = base.addChild("cube_r65", ModelPartBuilder.create().uv(60, 21).cuboid(-2.785F, -12.218F, 5.5F, 6.0F, 9.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.8F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r66 = base.addChild("cube_r66", ModelPartBuilder.create().uv(0, 54).cuboid(-3.008F, -11.818F, 5.669F, 6.0F, 9.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.65F, 2.8F, 0.078F, 0.0F, 1.0516F, 0.0F));

		ModelPartData cube_r67 = base.addChild("cube_r67", ModelPartBuilder.create().uv(0, 36).cuboid(-3.035F, -11.818F, 5.5F, 6.0F, 9.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.403F, 2.8F, 0.338F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r68 = base.addChild("cube_r68", ModelPartBuilder.create().uv(0, 18).cuboid(-3.435F, -11.818F, 5.318F, 6.0F, 9.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.455F, 2.8F, 0.247F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData cube_r69 = base.addChild("cube_r69", ModelPartBuilder.create().uv(0, 0).cuboid(-3.737F, -11.818F, 4.603F, 6.0F, 9.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-1.053F, 2.8F, 0.416F, -3.1416F, -1.0516F, 3.1416F));

		ModelPartData bone = console.addChild("bone", ModelPartBuilder.create().uv(97, 74).cuboid(-13.7738F, 5.9336F, 4.6663F, 5.0F, 1.0F, 4.0F, new Dilation(0.0F))
		.uv(60, 59).cuboid(-13.2808F, 5.2736F, 5.1563F, 4.0F, 1.0F, 3.0F, new Dilation(0.0F))
		.uv(57, 199).cuboid(-13.2808F, 5.0736F, 6.1563F, 4.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(6.6188F, -21.6496F, 12.4767F));

		ModelPartData cube_r70 = bone.addChild("cube_r70", ModelPartBuilder.create().uv(82, 102).cuboid(-2.4855F, -0.41F, -1.9865F, 5.0F, 1.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-1.4083F, 6.6436F, 5.6878F, -0.0436F, 0.0F, 0.0F));

		ModelPartData cube_r71 = bone.addChild("cube_r71", ModelPartBuilder.create().uv(154, 148).cuboid(-3.64F, -0.6F, -2.62F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(155, 120).cuboid(2.36F, -0.6F, -2.62F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(161, 158).cuboid(-0.64F, -0.825F, -2.62F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(96, 67).cuboid(-2.74F, -0.6F, -0.02F, 7.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(138, 120).cuboid(-2.74F, -0.6F, 2.7F, 6.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(60, 11).cuboid(3.72F, -0.525F, 2.678F, 3.0F, 0.0F, 2.0F, new Dilation(0.0F))
		.uv(97, 81).cuboid(-7.14F, -0.6F, 5.42F, 15.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-6.9538F, 1.3496F, -2.3367F, -0.7418F, 0.0F, 0.0F));

		ModelPartData bone4 = console.addChild("bone4", ModelPartBuilder.create(), ModelTransform.of(-6.9884F, -20.7191F, -11.4088F, 0.0F, -2.0944F, 0.0F));

		ModelPartData corner_r1 = bone4.addChild("corner_r1", ModelPartBuilder.create().uv(138, 81).cuboid(0.7713F, -5.2958F, -0.8599F, 3.0F, 14.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.9505F, 0.5656F, 1.2216F));

		ModelPartData corner_r2 = bone4.addChild("corner_r2", ModelPartBuilder.create().uv(99, 144).cuboid(-11.0181F, 5.2816F, -0.0915F, 3.0F, 14.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(8.6884F, 2.7191F, -12.2912F, 0.0F, 0.0F, 0.9163F));

		ModelPartData bone3 = console.addChild("bone3", ModelPartBuilder.create(), ModelTransform.pivot(-6.3884F, -20.7191F, 11.8912F));

		ModelPartData corner_r3 = bone3.addChild("corner_r3", ModelPartBuilder.create().uv(120, 0).cuboid(-1.45F, -7.0F, -0.9F, 3.0F, 14.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.8F, 0.0F, 0.9505F, 0.5656F, 1.2216F));

		ModelPartData corner_r4 = bone3.addChild("corner_r4", ModelPartBuilder.create().uv(66, 138).cuboid(-13.2395F, 3.577F, -0.0915F, 3.0F, 14.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(8.6884F, 5.5191F, -12.2912F, 0.0F, 0.0F, 0.9163F));

		ModelPartData bone2 = console.addChild("bone2", ModelPartBuilder.create(), ModelTransform.of(9.9198F, -20.8799F, 5.6383F, 0.0F, 2.0944F, 0.0F));

		ModelPartData corner_r5 = bone2.addChild("corner_r5", ModelPartBuilder.create().uv(0, 94).cuboid(0.7713F, -5.2958F, -1.0849F, 3.0F, 14.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(3.5917F, 0.0608F, 6.0529F, 0.9505F, 0.5656F, 1.2216F));

		ModelPartData corner_r6 = bone2.addChild("corner_r6", ModelPartBuilder.create().uv(0, 122).cuboid(-11.0181F, 5.2816F, -0.0165F, 3.0F, 14.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(12.2802F, 2.7799F, -6.2383F, 0.0F, 0.0F, 0.9163F));

		ModelPartData panel6 = console.addChild("panel6", ModelPartBuilder.create().uv(0, 0).cuboid(-11.0029F, 5.2697F, 6.9223F, 22.0F, 2.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(-0.0371F, -20.4717F, 0.0187F, 0.0F, 1.0472F, 0.0F));

		ModelPartData cube_r72 = panel6.addChild("cube_r72", ModelPartBuilder.create().uv(41, 74).cuboid(-11.5F, -6.1321F, 4.799F, 21.0F, 0.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(1.0371F, -2.2283F, -0.8187F, -0.7418F, 0.0F, 0.0F));

		ModelPartData panel5 = console.addChild("panel5", ModelPartBuilder.create().uv(0, 18).cuboid(-11.0029F, 5.2697F, 6.9223F, 22.0F, 2.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(-0.0371F, -20.4717F, 0.0187F, 0.0F, 2.0944F, 0.0F));

		ModelPartData cube_r73 = panel5.addChild("cube_r73", ModelPartBuilder.create().uv(82, 88).cuboid(-11.5F, -6.1321F, 4.799F, 21.0F, 0.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(1.0371F, -2.2283F, -0.8187F, -0.7418F, 0.0F, 0.0F));

		ModelPartData panel4 = console.addChild("panel4", ModelPartBuilder.create().uv(0, 36).cuboid(-11.0029F, 5.2697F, 6.9223F, 22.0F, 2.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(-0.0371F, -20.4717F, 0.0187F, 0.0F, 3.1416F, 0.0F));

		ModelPartData cube_r74 = panel4.addChild("cube_r74", ModelPartBuilder.create().uv(96, 39).cuboid(-11.5F, -6.1321F, 4.799F, 21.0F, 0.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(1.0371F, -2.2283F, -0.8187F, -0.7418F, 0.0F, 0.0F));

		ModelPartData panel3 = console.addChild("panel3", ModelPartBuilder.create().uv(0, 54).cuboid(-11.0029F, 5.2697F, 6.9223F, 22.0F, 2.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(-0.0371F, -20.4717F, 0.0187F, 0.0F, -2.0944F, 0.0F));

		ModelPartData cube_r75 = panel3.addChild("cube_r75", ModelPartBuilder.create().uv(96, 39).cuboid(-11.5F, -6.1321F, 4.799F, 21.0F, 0.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(1.0371F, -2.2283F, -0.8187F, -0.7418F, 0.0F, 0.0F));

		ModelPartData panel2 = console.addChild("panel2", ModelPartBuilder.create().uv(60, 3).cuboid(-11.0029F, 5.2697F, 6.9223F, 22.0F, 2.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(-0.0371F, -20.4717F, 0.0187F, 0.0F, -1.0472F, 0.0F));

		ModelPartData cube_r76 = panel2.addChild("cube_r76", ModelPartBuilder.create().uv(89, 102).cuboid(-11.3F, -6.1321F, 4.799F, 21.0F, 0.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(1.0371F, -2.2283F, -0.8187F, -0.7418F, 0.0F, 0.0F));

		ModelPartData panel = console.addChild("panel", ModelPartBuilder.create().uv(60, 21).cuboid(-11.0029F, 5.2697F, 6.9223F, 22.0F, 2.0F, 15.0F, new Dilation(0.0F)), ModelTransform.pivot(-0.0371F, -20.4717F, 0.0187F));

		ModelPartData cube_r77 = panel.addChild("cube_r77", ModelPartBuilder.create().uv(103, 67).cuboid(-11.5F, -8.1965F, 2.9073F, 21.0F, 0.0F, 13.0F, new Dilation(0.0F)), ModelTransform.of(1.0371F, 0.5717F, -0.8187F, -0.7418F, 0.0F, 0.0F));

		ModelPartData console_collar_two = console.addChild("console_collar_two", ModelPartBuilder.create(), ModelTransform.pivot(0.095F, -2.9F, -0.206F));

		ModelPartData corner_one_r13 = console_collar_two.addChild("corner_one_r13", ModelPartBuilder.create().uv(5, 167).cuboid(-0.5232F, -2.65F, 5.687F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(0, 167).cuboid(-0.5232F, -2.65F, -6.788F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1576F, -20.925F, 0.2758F, 0.0F, -0.5236F, 0.0F));

		ModelPartData corner_one_r14 = console_collar_two.addChild("corner_one_r14", ModelPartBuilder.create().uv(166, 163).cuboid(-0.4482F, -2.65F, 5.687F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(44, 166).cuboid(-0.4482F, -2.65F, -6.763F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1576F, -20.925F, 0.2758F, 0.0F, 0.5236F, 0.0F));

		ModelPartData corner_one_r15 = console_collar_two.addChild("corner_one_r15", ModelPartBuilder.create().uv(165, 62).cuboid(-0.5482F, -2.65F, 5.687F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F))
		.uv(161, 163).cuboid(-0.5732F, -2.65F, -6.738F, 1.0F, 3.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(-0.1576F, -20.925F, 0.2758F, 0.0F, -1.5708F, 0.0F));

		ModelPartData cube_r78 = console_collar_two.addChild("cube_r78", ModelPartBuilder.create().uv(171, 78).cuboid(-2.96F, -4.418F, -6.282F, 6.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.403F, -19.14F, 0.338F, -3.1416F, -2.0944F, 3.1416F));

		ModelPartData cube_r79 = console_collar_two.addChild("cube_r79", ModelPartBuilder.create().uv(175, 94).cuboid(-3.26F, -4.418F, -6.182F, 6.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.403F, -19.14F, 0.338F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r80 = console_collar_two.addChild("cube_r80", ModelPartBuilder.create().uv(25, 176).cuboid(-3.26F, -4.418F, -5.882F, 6.0F, 3.0F, 12.0F, new Dilation(0.0F)), ModelTransform.of(-0.403F, -19.14F, 0.338F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData bone17 = console_collar_two.addChild("bone17", ModelPartBuilder.create().uv(125, 33).cuboid(-2.65F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.55F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.55F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.pivot(-0.095F, -20.825F, 0.206F));

		ModelPartData bone18 = console_collar_two.addChild("bone18", ModelPartBuilder.create().uv(125, 33).cuboid(-2.6F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.5F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.6F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.095F, -20.825F, 0.206F, 0.0F, -1.0472F, 0.0F));

		ModelPartData bone19 = console_collar_two.addChild("bone19", ModelPartBuilder.create().uv(125, 33).cuboid(-2.65F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.55F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.55F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.095F, -20.825F, 0.206F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone20 = console_collar_two.addChild("bone20", ModelPartBuilder.create().uv(125, 33).cuboid(-2.65F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.55F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.55F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.195F, -20.825F, 0.256F, 0.0F, 3.1416F, 0.0F));

		ModelPartData bone21 = console_collar_two.addChild("bone21", ModelPartBuilder.create().uv(125, 33).cuboid(-2.65F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.55F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.55F, -2.0F, 5.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.195F, -20.825F, 0.256F, 0.0F, 2.0944F, 0.0F));

		ModelPartData bone22 = console_collar_two.addChild("bone22", ModelPartBuilder.create().uv(125, 33).cuboid(-2.65F, -2.0F, 5.225F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.55F, -2.0F, 5.225F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.55F, -2.0F, 5.225F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.195F, -20.825F, 0.256F, 0.0F, -2.0944F, 0.0F));

		ModelPartData console_collar_one = console.addChild("console_collar_one", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -27.5F, 0.0F));

		ModelPartData corner_one_r16 = console_collar_one.addChild("corner_one_r16", ModelPartBuilder.create().uv(87, 128).cuboid(-0.45F, -1.0F, -9.0121F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 128).cuboid(-0.4482F, -1.0F, 7.837F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.0626F, 4.575F, 0.0698F, 0.0F, -0.5236F, 0.0F));

		ModelPartData corner_one_r17 = console_collar_one.addChild("corner_one_r17", ModelPartBuilder.create().uv(76, 122).cuboid(-0.55F, -1.0F, 7.9129F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F))
		.uv(82, 128).cuboid(-0.55F, -1.0F, -8.7621F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0874F, 4.575F, 0.0698F, 0.0F, -1.5708F, 0.0F));

		ModelPartData corner_one_r18 = console_collar_one.addChild("corner_one_r18", ModelPartBuilder.create().uv(97, 102).cuboid(-0.45F, -1.0F, -8.9871F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F))
		.uv(115, 128).cuboid(-0.4982F, -1.0F, 7.712F, 1.0F, 2.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0624F, 4.575F, 0.1198F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r81 = console_collar_one.addChild("cube_r81", ModelPartBuilder.create().uv(148, 120).cuboid(-4.044F, -2.218F, -7.996F, 8.0F, 2.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 5.7F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r82 = console_collar_one.addChild("cube_r82", ModelPartBuilder.create().uv(148, 139).cuboid(-4.044F, -2.218F, -7.996F, 8.0F, 2.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 5.7F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r83 = console_collar_one.addChild("cube_r83", ModelPartBuilder.create().uv(152, 25).cuboid(-4.044F, -2.218F, -7.996F, 8.0F, 2.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 5.7F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData console_corners = console.addChild("console_corners", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 5.018F, -10.66F));

		ModelPartData corner_r7 = console_corners.addChild("corner_r7", ModelPartBuilder.create().uv(33, 142).cuboid(-24.5574F, -1.0F, -2.4577F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.1F, -19.218F, 10.66F, 0.0F, -1.0079F, 0.0F));

		ModelPartData corner_r8 = console_corners.addChild("corner_r8", ModelPartBuilder.create().uv(82, 122).cuboid(-24.5574F, -1.0F, -2.4577F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.1F, -19.218F, 10.66F, -3.1416F, 1.0036F, 3.1416F));

		ModelPartData corner_r9 = console_corners.addChild("corner_r9", ModelPartBuilder.create().uv(82, 94).cuboid(-24.5574F, -1.0F, -2.4577F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.1F, -19.218F, 10.66F, -3.1416F, -1.0865F, 3.1416F));

		ModelPartData corner_r10 = console_corners.addChild("corner_r10", ModelPartBuilder.create().uv(96, 53).cuboid(-24.5574F, -1.0F, -2.4577F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-0.1F, -19.218F, 10.66F, 0.0F, 1.0908F, 0.0F));

		ModelPartData corner_r11 = console_corners.addChild("corner_r11", ModelPartBuilder.create().uv(96, 59).cuboid(-1.475F, -1.0F, -1.55F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(23.0F, -19.218F, 10.56F, -3.1416F, 0.0428F, 3.1416F));

		ModelPartData corner_r12 = console_corners.addChild("corner_r12", ModelPartBuilder.create().uv(145, 102).cuboid(-1.475F, -1.0F, -1.55F, 3.0F, 2.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-23.2F, -19.218F, 10.76F, 0.0F, 0.0436F, 0.0F));

		ModelPartData under_console_struts = console.addChild("under_console_struts", ModelPartBuilder.create(), ModelTransform.pivot(-0.075F, -0.3F, 0.0F));

		ModelPartData cube_r84 = under_console_struts.addChild("cube_r84", ModelPartBuilder.create().uv(0, 142).cuboid(-1.137F, -21.08F, -2.62F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.6981F, 2.0944F, 0.0F));

		ModelPartData cube_r85 = under_console_struts.addChild("cube_r85", ModelPartBuilder.create().uv(9, 72).cuboid(-1.037F, -20.98F, -2.62F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.6981F, -2.0944F, 0.0F));

		ModelPartData cube_r86 = under_console_struts.addChild("cube_r86", ModelPartBuilder.create().uv(0, 72).cuboid(-1.037F, -21.03F, -2.62F, 2.0F, 13.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 0.0F, -0.6981F, 0.0F, 0.0F));

		ModelPartData clawlegs3 = console.addChild("clawlegs3", ModelPartBuilder.create(), ModelTransform.of(6.4081F, 4.3721F, 3.8203F, 0.0F, 2.0944F, 0.0F));

		ModelPartData cube_r87 = clawlegs3.addChild("cube_r87", ModelPartBuilder.create().uv(159, 67).cuboid(5.882F, -6.36F, -0.886F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(143, 148).cuboid(2.902F, -6.42F, -0.876F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r88 = clawlegs3.addChild("cube_r88", ModelPartBuilder.create().uv(60, 64).cuboid(-0.2336F, -0.5769F, -0.9569F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.5853F, -7.6123F, 0.2412F, -2.8449F, -0.4363F, 2.5133F));

		ModelPartData cube_r89 = clawlegs3.addChild("cube_r89", ModelPartBuilder.create().uv(41, 72).cuboid(-0.65F, -1.2026F, -0.9984F, 4.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2901F, -9.0239F, -0.3154F, -2.618F, 0.0F, 1.5708F));

		ModelPartData clawlegs2 = console.addChild("clawlegs2", ModelPartBuilder.create(), ModelTransform.of(-0.0919F, 4.3721F, -7.1797F, 0.0F, -2.0944F, 0.0F));

		ModelPartData cube_r90 = clawlegs2.addChild("cube_r90", ModelPartBuilder.create().uv(159, 67).cuboid(5.882F, -6.36F, -0.886F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(151, 0).cuboid(2.902F, -6.42F, -0.876F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r91 = clawlegs2.addChild("cube_r91", ModelPartBuilder.create().uv(33, 148).cuboid(-0.2336F, -0.5769F, -0.9569F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.5853F, -7.6123F, 0.2412F, -2.8449F, -0.4363F, 2.5133F));

		ModelPartData cube_r92 = clawlegs2.addChild("cube_r92", ModelPartBuilder.create().uv(41, 78).cuboid(-0.55F, -1.2026F, -0.9984F, 4.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2901F, -9.0239F, -0.3154F, -2.618F, 0.0F, 1.5708F));

		ModelPartData clawlegs = console.addChild("clawlegs", ModelPartBuilder.create(), ModelTransform.pivot(-6.2419F, 4.3721F, 3.8953F));

		ModelPartData cube_r93 = clawlegs.addChild("cube_r93", ModelPartBuilder.create().uv(159, 67).cuboid(5.882F, -6.86F, -0.886F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F))
		.uv(152, 44).cuboid(2.902F, -6.92F, -0.876F, 3.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.5F, 0.0F, 3.1416F, -0.5236F, 3.1416F));

		ModelPartData cube_r94 = clawlegs.addChild("cube_r94", ModelPartBuilder.create().uv(93, 47).cuboid(-0.5F, -1.0F, -0.95F, 4.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(-0.5853F, -7.1123F, 0.2412F, -2.8449F, -0.4363F, 2.5133F));

		ModelPartData cube_r95 = clawlegs.addChild("cube_r95", ModelPartBuilder.create().uv(82, 88).cuboid(-1.175F, -1.2F, -1.0F, 4.0F, 3.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.2901F, -8.5239F, -0.3154F, -2.618F, 0.0F, 1.5708F));

		ModelPartData console_plinth_three = console.addChild("console_plinth_three", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -39.6F, 0.0F));

		ModelPartData corner_one_r19 = console_plinth_three.addChild("corner_one_r19", ModelPartBuilder.create().uv(55, 94).cuboid(-0.45F, -1.0F, -9.0121F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F))
		.uv(64, 163).cuboid(-0.4482F, -1.0F, 7.837F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.0626F, 25.575F, 0.0698F, 0.0F, -0.5236F, 0.0F));

		ModelPartData corner_one_r20 = console_plinth_three.addChild("corner_one_r20", ModelPartBuilder.create().uv(104, 44).cuboid(-0.55F, -1.0F, 7.9129F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F))
		.uv(66, 115).cuboid(-0.55F, -1.0F, -8.7621F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0874F, 25.575F, 0.0698F, 0.0F, -1.5708F, 0.0F));

		ModelPartData corner_one_r21 = console_plinth_three.addChild("corner_one_r21", ModelPartBuilder.create().uv(33, 94).cuboid(-0.45F, -1.0F, -8.9871F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F))
		.uv(163, 30).cuboid(-0.4982F, -1.0F, 7.712F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0624F, 25.575F, 0.1198F, 0.0F, 0.5236F, 0.0F));

		ModelPartData cube_r96 = console_plinth_three.addChild("cube_r96", ModelPartBuilder.create().uv(120, 5).cuboid(-4.044F, -2.218F, -7.996F, 8.0F, 3.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 26.7F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r97 = console_plinth_three.addChild("cube_r97", ModelPartBuilder.create().uv(0, 122).cuboid(-4.044F, -2.218F, -7.996F, 8.0F, 3.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 26.7F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r98 = console_plinth_three.addChild("cube_r98", ModelPartBuilder.create().uv(115, 128).cuboid(-4.044F, -2.218F, -7.996F, 8.0F, 3.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 26.7F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData console_plinth_two = console.addChild("console_plinth_two", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -24.7F, 0.0F));

		ModelPartData corner_one_r22 = console_plinth_two.addChild("corner_one_r22", ModelPartBuilder.create().uv(161, 53).cuboid(-0.45F, -1.0F, -9.0121F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.0626F, 22.575F, 0.0698F, 0.0F, -0.5236F, 0.0F));

		ModelPartData corner_one_r23 = console_plinth_two.addChild("corner_one_r23", ModelPartBuilder.create().uv(162, 0).cuboid(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-8.3255F, 23.075F, 0.0198F, 0.0F, 1.5708F, 0.0F));

		ModelPartData corner_one_r24 = console_plinth_two.addChild("corner_one_r24", ModelPartBuilder.create().uv(25, 162).cuboid(-0.55F, -1.0F, -8.7621F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0874F, 22.575F, 0.0698F, 0.0F, -1.5708F, 0.0F));

		ModelPartData corner_one_r25 = console_plinth_two.addChild("corner_one_r25", ModelPartBuilder.create().uv(120, 25).cuboid(-0.45F, -1.0F, -8.9871F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0624F, 22.575F, 0.1198F, 0.0F, 0.5236F, 0.0F));

		ModelPartData corner_one_r26 = console_plinth_two.addChild("corner_one_r26", ModelPartBuilder.create().uv(69, 163).cuboid(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(4.17F, 23.075F, 7.2307F, 0.0F, -2.618F, 0.0F));

		ModelPartData corner_one_r27 = console_plinth_two.addChild("corner_one_r27", ModelPartBuilder.create().uv(91, 163).cuboid(-0.5F, -1.5F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-4.1862F, 23.075F, 7.3157F, 0.0F, 2.618F, 0.0F));

		ModelPartData cube_r99 = console_plinth_two.addChild("cube_r99", ModelPartBuilder.create().uv(33, 138).cuboid(-4.044F, -2.218F, -7.996F, 8.0F, 3.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 23.7F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r100 = console_plinth_two.addChild("cube_r100", ModelPartBuilder.create().uv(138, 81).cuboid(-4.044F, -2.218F, -7.996F, 8.0F, 3.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 23.7F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r101 = console_plinth_two.addChild("cube_r101", ModelPartBuilder.create().uv(0, 142).cuboid(-4.044F, -2.218F, -7.996F, 8.0F, 3.0F, 16.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 23.7F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData console_plinth_two2 = console_plinth_two.addChild("console_plinth_two2", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData corner_one_r28 = console_plinth_two2.addChild("corner_one_r28", ModelPartBuilder.create().uv(35, 162).cuboid(-1.19F, -1.21F, -7.7259F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-0.0563F, 20.3175F, 0.0628F, 0.0F, -0.6109F, 0.0F));

		ModelPartData corner_one_r29 = console_plinth_two2.addChild("corner_one_r29", ModelPartBuilder.create().uv(163, 25).cuboid(-0.56F, -1.66F, -0.44F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-7.493F, 20.7675F, 0.0178F, 0.0F, 1.5708F, 0.0F));

		ModelPartData corner_one_r30 = console_plinth_two2.addChild("corner_one_r30", ModelPartBuilder.create().uv(132, 81).cuboid(0.42F, -1.21F, -7.4759F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0787F, 20.3175F, 0.0628F, 0.0F, -1.4399F, 0.0F));

		ModelPartData corner_one_r31 = console_plinth_two2.addChild("corner_one_r31", ModelPartBuilder.create().uv(159, 139).cuboid(-1.265F, -1.21F, -7.8034F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0562F, 20.3175F, 0.1078F, 0.0F, 0.4363F, 0.0F));

		ModelPartData corner_one_r32 = console_plinth_two2.addChild("corner_one_r32", ModelPartBuilder.create().uv(40, 162).cuboid(-0.46F, -1.66F, -0.29F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(3.753F, 20.7675F, 6.5076F, 0.0F, -2.618F, 0.0F));

		ModelPartData corner_one_r33 = console_plinth_two2.addChild("corner_one_r33", ModelPartBuilder.create().uv(161, 9).cuboid(-0.46F, -1.66F, -0.34F, 1.0F, 3.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(-3.7676F, 20.7675F, 6.5842F, 0.0F, 2.6616F, 0.0F));

		ModelPartData cube_r102 = console_plinth_two2.addChild("cube_r102", ModelPartBuilder.create().uv(152, 44).cuboid(-3.5396F, -2.2962F, -7.1964F, 7.0F, 3.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 21.33F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r103 = console_plinth_two2.addChild("cube_r103", ModelPartBuilder.create().uv(153, 0).cuboid(-3.4396F, -2.2962F, -6.9214F, 7.0F, 3.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 21.33F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r104 = console_plinth_two2.addChild("cube_r104", ModelPartBuilder.create().uv(35, 158).cuboid(-3.4396F, -2.2962F, -7.0964F, 7.0F, 3.0F, 14.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 21.33F, 0.0F, -3.1416F, 0.0F, 3.1416F));

		ModelPartData bone11 = console_plinth_two2.addChild("bone11", ModelPartBuilder.create().uv(125, 33).cuboid(-2.7F, -2.0F, 6.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.6F, -2.0F, 6.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.5F, -2.0F, 6.3F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.pivot(0.0F, 21.675F, 0.0F));

		ModelPartData bone12 = console_plinth_two2.addChild("bone12", ModelPartBuilder.create().uv(125, 33).cuboid(-2.7F, -2.0F, 6.05F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.6F, -2.0F, 6.05F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.5F, -2.0F, 6.05F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 21.675F, -0.1F, 0.0F, 2.0944F, 0.0F));

		ModelPartData bone13 = console_plinth_two2.addChild("bone13", ModelPartBuilder.create().uv(125, 33).cuboid(-2.425F, -2.0F, 6.15F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-0.325F, -2.0F, 6.15F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(1.775F, -2.0F, 6.15F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 21.675F, -0.1F, 0.0F, -2.0944F, 0.0F));

		ModelPartData bone14 = console_plinth_two2.addChild("bone14", ModelPartBuilder.create().uv(125, 33).cuboid(1.675F, -2.0F, 6.05F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-2.425F, -2.0F, 6.05F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.0F, 21.675F, 0.3F, 0.0F, 1.0472F, 0.0F));

		ModelPartData bone15 = console_plinth_two2.addChild("bone15", ModelPartBuilder.create().uv(125, 33).cuboid(1.675F, -2.0F, 6.05F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-2.425F, -2.0F, 6.05F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.1F, 21.675F, 0.0F, 0.0F, 3.1416F, 0.0F));

		ModelPartData bone16 = console_plinth_two2.addChild("bone16", ModelPartBuilder.create().uv(125, 33).cuboid(1.675F, -2.0F, 6.45F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F))
		.uv(125, 33).cuboid(-2.425F, -2.0F, 6.45F, 1.0F, 1.0F, 1.0F, new Dilation(0.1F)), ModelTransform.of(0.1F, 21.675F, 0.0F, 0.0F, -1.0472F, 0.0F));

		ModelPartData console_plinth_one = console.addChild("console_plinth_one", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -39.8F, 0.0F));

		ModelPartData corner_one_r34 = console_plinth_one.addChild("corner_one_r34", ModelPartBuilder.create().uv(162, 37).cuboid(-0.093F, -1.817F, 9.9186F, 1.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(0.0799F, 40.624F, 0.1534F, 0.0F, -0.576F, 0.0F));

		ModelPartData corner_one_r35 = console_plinth_one.addChild("corner_one_r35", ModelPartBuilder.create().uv(162, 5).cuboid(-0.343F, -1.817F, -11.2564F, 1.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(0.0799F, 40.624F, 0.1534F, 0.0F, -0.4887F, 0.0F));

		ModelPartData corner_one_r36 = console_plinth_one.addChild("corner_one_r36", ModelPartBuilder.create().uv(105, 167).cuboid(-0.093F, -1.817F, 9.9186F, 1.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(0.0799F, 40.624F, 0.1534F, 0.0F, 1.5272F, 0.0F));

		ModelPartData corner_one_r37 = console_plinth_one.addChild("corner_one_r37", ModelPartBuilder.create().uv(163, 18).cuboid(-0.043F, -1.817F, -11.2564F, 1.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(0.0799F, 40.624F, 0.1534F, -3.1416F, 1.5272F, 3.1416F));

		ModelPartData corner_one_r38 = console_plinth_one.addChild("corner_one_r38", ModelPartBuilder.create().uv(110, 167).cuboid(-0.093F, -1.817F, 9.9186F, 1.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(0.0799F, 40.624F, 0.1534F, 0.0F, 0.48F, 0.0F));

		ModelPartData corner_one_r39 = console_plinth_one.addChild("corner_one_r39", ModelPartBuilder.create().uv(133, 167).cuboid(-0.043F, -1.817F, -11.2564F, 1.0F, 1.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(0.0799F, 40.624F, 0.1534F, 0.0F, 0.5672F, 0.0F));

		ModelPartData cube_r105 = console_plinth_one.addChild("cube_r105", ModelPartBuilder.create().uv(55, 52).cuboid(-4.9363F, -3.279F, -10.0349F, 10.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 42.064F, 0.0F, -3.1416F, 1.0472F, 3.1416F));

		ModelPartData cube_r106 = console_plinth_one.addChild("cube_r106", ModelPartBuilder.create().uv(0, 72).cuboid(-4.9363F, -3.279F, -10.0349F, 10.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 42.064F, 0.0F, -3.1416F, -1.0472F, 3.1416F));

		ModelPartData cube_r107 = console_plinth_one.addChild("cube_r107", ModelPartBuilder.create().uv(41, 88).cuboid(-4.9363F, -3.279F, -10.0349F, 10.0F, 1.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 42.064F, 0.0F, -3.1416F, 0.0F, 3.1416F));
		return TexturedModelData.of(modelData, 256, 256);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		this.console.render(matrices, vertices, light, overlay,1, 1, 1, 1);
	}

	@Override
	public void renderWithAnimations(ConsoleBlockEntity console, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		//this.door.yaw = exterior.getLeftDoorRotation();
		matrices.push();
		matrices.translate(0.5, 1.35, 0.5);
		matrices.scale(0.9f, 0.9f, 0.9f);
		super.renderWithAnimations(console, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);
		matrices.pop();
	}

	@Override
	public Identifier getTexture() {
		return CONSOLE_TEXTURE;
	}

	@Override
	public Identifier getEmission() {
		return CONSOLE_TEXTURE_EMISSION;
	}

	@Override
	public ModelPart getPart() {
		return this.console;
	}
}