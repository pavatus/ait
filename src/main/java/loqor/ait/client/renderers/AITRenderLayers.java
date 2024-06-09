package loqor.ait.client.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.BiFunction;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class AITRenderLayers extends RenderLayer {

	private static final BiFunction<Identifier, Boolean, RenderLayer> TARDIS_EMISSION = Util.memoize((texture, affectsOutline) -> {
		RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().program(BEACON_BEAM_PROGRAM).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).writeMaskState(COLOR_MASK).build(false);
		return RenderLayer.of("tardis_emission", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, multiPhaseParameters);
	});

	private static final Function<Identifier, RenderLayer> TARDIS_RENDER = Util.memoize(texture -> {
		RenderLayer.MultiPhaseParameters multiPhaseParameters = RenderLayer.MultiPhaseParameters.builder().program(ENTITY_TRANSLUCENT_CULL_PROGRAM).texture(new RenderPhase.Texture(texture, false, false)).transparency(TRANSLUCENT_TRANSPARENCY).cull(new Cull(true)).writeMaskState(DEPTH_MASK).build(true);
		return RenderLayer.of("tardis", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, multiPhaseParameters);
	});

	public static RenderLayer tardisRenderEmissionCull(Identifier texture, boolean affectsOutline) {
		return TARDIS_EMISSION.apply(texture, false);
	}

	public static RenderLayer tardisRenderCull(Identifier texture) {
		return TARDIS_RENDER.apply(texture);
	}

	private AITRenderLayers(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	// @TODO Blurring for the exterior, referenced from the Tempad mod -> https://github.com/terrarium-earth/Tempad/blob/1.20.x/fabric/src/main/java/me/codexadrian/tempad/common/fabric/FabricTempadClient.java
	/*public static final class RenderLayerAccessor extends RenderLayer {

		public static final RenderLayer BLURRING_RENDER_LAYER = RenderLayer.of("exteriorBlur", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder().transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY).cull(new RenderPhase.Cull(false)).program(new RenderPhase.ShaderProgram(() -> exteriorBlur)).target(new RenderPhase.Target("exterior_blur", () -> {
			Target target = INSTANCE.getBlurTarget();
			if (target != null) {
				target.startDrawing();
			}
		})))

		private RenderLayerAccessor(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
			super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
		}
	}*/
}
