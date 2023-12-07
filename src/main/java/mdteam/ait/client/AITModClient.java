package mdteam.ait.client;

import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.AITRadioRenderer;
import mdteam.ait.client.renderers.AITRenderLayers;
import mdteam.ait.client.renderers.consoles.ConsoleRenderer;
import mdteam.ait.client.renderers.doors.DoorRenderer;
import mdteam.ait.client.renderers.entities.ControlEntityRenderer;
import mdteam.ait.client.renderers.exteriors.ExteriorRenderer;
import mdteam.ait.client.screens.MonitorScreen;
import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.AITEntityTypes;
import mdteam.ait.core.AITScreenHandlers;
import mdteam.ait.core.entities.BaseControlEntity;
import mdteam.ait.core.entities.ConsoleControlEntity;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.ShaderStage;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.GameEventDebugRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.registry.RegistryLoader;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.text.ParseException;

@Environment(value= EnvType.CLIENT)
public class AITModClient implements ClientModInitializer {

	private static KeyBinding keyBinding;

	private final Identifier PORTAL_EFFECT_SHADER = new Identifier(AITMod.MOD_ID, "shaders/core/portal_effect.json");

	@Override
	public void onInitializeClient() {
		setupBlockRendering();
		blockEntityRendererRegister();
		entityAttributeRegister();
		entityRenderRegister();
		//setKeyBinding();
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}


	//@TODO Shader stuff, decide whether or not to use this or glScissor stuff. - Loqor
	/*public void shaderStuffForBOTI() {
		CoreShaderRegistrationCallback.EVENT.register(manager -> {
			manager.register(PORTAL_EFFECT_SHADER, VertexFormats.POSITION_TEXTURE, ShaderProgram::attachReferencedShaders);
		});
	}

	public ShaderProgram getShader() throws IOException {
		return new FabricShaderProgram(MinecraftClient.getInstance().getResourceManager(), PORTAL_EFFECT_SHADER, VertexFormats.POSITION_TEXTURE);
	}*/

	public void blockEntityRendererRegister() {
		BlockEntityRendererRegistry.register(AITBlockEntityTypes.AIT_RADIO_BLOCK_ENTITY_TYPE, AITRadioRenderer::new);
		BlockEntityRendererRegistry.register(AITBlockEntityTypes.DISPLAY_CONSOLE_BLOCK_ENTITY_TYPE, ConsoleRenderer::new);
		BlockEntityRendererRegistry.register(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, ExteriorRenderer::new);
		BlockEntityRendererRegistry.register(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, DoorRenderer::new);
	}

	public void entityRenderRegister() {
		EntityRendererRegistry.register(AITEntityTypes.CONTROL_ENTITY_TYPE, ControlEntityRenderer::new);
	}

	public void entityAttributeRegister() {
		FabricDefaultAttributeRegistry.register(AITEntityTypes.CONTROL_ENTITY_TYPE, ConsoleControlEntity.createControlAttributes());
	}

	public void setKeyBinding() {
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key." + AITMod.MOD_ID + ".open",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_B,
				"category." + AITMod.MOD_ID + ".snap"
		));
		//ClientTickEvents.END_CLIENT_TICK.register(client -> {
		//	if (keyBinding.wasPressed()) {
		//		if(client.player.getMainHandStack().getItem() instanceof PipboyItem) {
		//			//item.setPipColor(client.player.getMainHandStack(), item.getPipColor(client.player.getMainHandStack()));
		//			client.setScreen(new PipboyMainScreen(MinecraftClient.getInstance().player, null));
		//		}
		//	}
		//});
	}

	public void setupBlockRendering() {
		BlockRenderLayerMap map = BlockRenderLayerMap.INSTANCE;
		//map.putBlock(FMCBlocks.RADIO, RenderLayer.getCutout());
	}
}