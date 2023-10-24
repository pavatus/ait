package mdteam.ait.client;

import mdteam.ait.AITMod;
import mdteam.ait.client.renderers.AITRadioRenderer;
import mdteam.ait.client.renderers.DisplayConsoleRenderer;
import mdteam.ait.client.renderers.doors.DoorRenderer;
import mdteam.ait.client.renderers.exteriors.ExteriorRenderer;
import mdteam.ait.core.AITBlockEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AITModClient implements ClientModInitializer {

	private static KeyBinding keyBinding;

	@Override
	public void onInitializeClient() {
		setupBlockRendering();
		//setKeyBinding();
		blockEntityRendererRegister();
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}

	public void blockEntityRendererRegister() {
		BlockEntityRendererRegistry.register(AITBlockEntityTypes.AIT_RADIO_BLOCK_ENTITY_TYPE, AITRadioRenderer::new);
		BlockEntityRendererRegistry.register(AITBlockEntityTypes.DISPLAY_CONSOLE_BLOCK_ENTITY_TYPE, DisplayConsoleRenderer::new);
		BlockEntityRendererRegistry.register(AITBlockEntityTypes.EXTERIOR_BLOCK_ENTITY_TYPE, ExteriorRenderer::new);
		BlockEntityRendererRegistry.register(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, DoorRenderer::new);
	}

	public void setKeyBinding() {
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key." + AITMod.MOD_ID + ".open",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_B,
				"category." + AITMod.MOD_ID + ".pipboy"
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