package mdteam.ait.client.renderers;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.BiFunction;
import java.util.function.Function;

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
}
