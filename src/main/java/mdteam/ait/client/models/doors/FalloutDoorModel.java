package mdteam.ait.client.models.doors;

import com.mojang.blaze3d.systems.RenderSystem;
import io.wispforest.owo.mixin.shader.ShaderProgramAccessor;
import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class FalloutDoorModel extends DoorModel {
	public static final Identifier TEXTURE = new Identifier(AITMod.MOD_ID, "textures/blockentities/doors/shelter_door.png");
	public static final Identifier EMISSION = new Identifier(AITMod.MOD_ID, "textures/blockentities/doors/shelter_door_emission.png");

	private Framebuffer framebuffer;

	public ModelPart door;
	public ModelPart frame;
	public ModelPart root;
	public FalloutDoorModel(ModelPart root) {
		super(RenderLayer::getEntityCutoutNoCull);
		this.root = root;
		this.door = root.getChild("door");
		this.frame = root.getChild("frame");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData door = modelPartData.addChild("door", ModelPartBuilder.create().uv(24, 24).cuboid(0.0F, -16.0F, -0.5F, 11.0F, 32.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(5.5F, 8.0F, 6.975F));

		ModelPartData frame = modelPartData.addChild("frame", ModelPartBuilder.create().uv(24, 11).cuboid(-5.5F, -36.0F, -11.55F, 11.0F, 2.0F, 1.0F, new Dilation(0.0F))
		.uv(24, 0).cuboid(-7.5F, -41.0F, -11.55F, 15.0F, 5.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 35).cuboid(5.5F, -36.0F, -11.55F, 2.0F, 34.0F, 2.0F, new Dilation(0.0F))
		.uv(8, 35).cuboid(-7.5F, -36.0F, -11.55F, 2.0F, 34.0F, 2.0F, new Dilation(0.0F))
		.uv(0, 0).cuboid(-5.5F, -36.0F, -10.55F, 11.0F, 34.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 26.0F, 17.55F));
		return TexturedModelData.of(modelData, 128, 128);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {


		this.door.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
		this.frame.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}

	private void bindCustomShader(float red, float green, float blue, float alpha) {
		RenderSystem.setShaderTexture(0, framebuffer.getColorAttachment());
		RenderSystem.setShaderColor(red, green, blue, alpha);
		RenderSystem.setShader(GameRenderer::getRenderTypeEndPortalProgram);
	}

	private void unbindCustomShader() {
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
	}

	@Override
	public void renderWithAnimations(DoorBlockEntity door, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float pAlpha) {
		//this.door.yaw = -door.getLeftDoorRotation();

		matrices.push();
		matrices.translate(0,-1.5,0);

		this.door.setPivot(this.frame.pivotX, this.frame.pivotY, this.frame.pivotZ);
		/*MinecraftClient client = MinecraftClient.getInstance();

		Entity cameraEntity = client.getCameraEntity();
		framebuffer = new SimpleFramebuffer(MinecraftClient.getInstance().getWindow().getFramebufferWidth(),
				MinecraftClient.getInstance().getWindow().getFramebufferHeight(), false, MinecraftClient.IS_SYSTEM_MAC);

		if (cameraEntity != null) {
			matrices.loadIdentity();
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(cameraEntity.getPitch()));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(cameraEntity.getYaw()));
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0f));

			Camera camera = client.gameRenderer.getCamera();
			camera.update(
					client.world,
					cameraEntity instanceof Entity ? (Entity) cameraEntity : null,
					!client.options.getBobView().getValue(),
					!client.options.getBobView().getValue(),
					client.getTickDelta()
			);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(camera.getRotation().z()));
			framebuffer.beginWrite(true);
			client.getFramebuffer().beginWrite(true);
			bindCustomShader(red, green, blue, pAlpha);*/
			super.renderWithAnimations(door, root, matrices, vertices, light, overlay, red, green, blue, pAlpha);
			/*unbindCustomShader();
			framebuffer.endWrite();
			client.getFramebuffer().endWrite();
		}*/
		matrices.pop();
	}

	@Override
	public ModelPart getPart() {
		return root;
	}

	@Override
	public Identifier getTexture() {
		return TEXTURE;
	}

	@Override
	public Identifier getEmission() {
		return EMISSION;
	}
}