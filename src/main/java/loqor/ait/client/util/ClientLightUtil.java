package loqor.ait.client.util;

import loqor.ait.client.renderers.AITRenderLayers;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class ClientLightUtil {

	private static final boolean HAS_IRIS = FabricLoader.getInstance().isModLoaded("iris");

	public static float getBrightnessForInterior(int lightLevel) {
		float f = (float) lightLevel / 15.0f;
		float g = f / (4.0f - 3.0f * f);
		return MathHelper.lerp(0f /* this number here adjusts the brightness todo stuff with it */, g, 1.0f);
	}

	public static <T> void renderEmissivable(
			boolean emissive, Renderable<T> renderable, @Nullable Identifier texture, T entity, ModelPart root,
			MatrixStack matrices, VertexConsumerProvider vertices, int light, int overlay, float red, float green, float blue, float alpha
	) {
		ClientLightUtil.renderEmissivable(emissive, renderable, texture, texture, entity, root, matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	public static <T> void renderEmissivable(
			boolean emissive, Renderable<T> renderable, @Nullable Identifier base, @Nullable Identifier glowing, T entity, ModelPart root,
			MatrixStack matrices, VertexConsumerProvider vertices, int light, int overlay, float red, float green, float blue, float alpha
	) {
		if (emissive) {
			ClientLightUtil.renderEmissive(renderable, glowing, entity, root, matrices, vertices, light, overlay, red, green, blue, alpha);
		} else {
			ClientLightUtil.render(renderable, base, entity, root, matrices, vertices, light, overlay, red, green, blue, alpha);
		}
	}

	public static <T> void renderEmissive(
			Renderable<T> renderable, @Nullable Identifier emissive, T entity, ModelPart root, MatrixStack matrices,
			VertexConsumerProvider vertices, int light, int overlay, float red, float green, float blue, float alpha
	) {
		if (emissive == null)
			return;

		RenderLayer layer = HAS_IRIS ? AITRenderLayers.getEntityCutoutNoCullZOffset(emissive)
				: AITRenderLayers.getBeaconBeam(emissive, true);

		light = HAS_IRIS ? LightmapTextureManager.MAX_LIGHT_COORDINATE : light;
		ClientLightUtil.render(renderable, entity, root, matrices, layer, vertices, light, overlay, red, green, blue, alpha);
	}

	public static <T> void render(
			Renderable<T> renderable, @Nullable Identifier texture, T entity, ModelPart root, MatrixStack matrices,
			VertexConsumerProvider vertices, int light, int overlay, float red, float green, float blue, float alpha
	) {
		if (texture == null)
			return;

		ClientLightUtil.render(renderable, entity, root, matrices, RenderLayer.getEntityCutoutNoCullZOffset(texture), vertices, light, overlay, red, green, blue, alpha);
	}

	public static <T> void render(
			Renderable<T> renderable, T entity, ModelPart root, MatrixStack matrices, RenderLayer layer,
			VertexConsumerProvider vertices, int light, int overlay, float red, float green, float blue, float alpha
	) {
		renderable.render(entity, root, matrices, vertices.getBuffer(layer), light, overlay, red, green, blue, alpha);
	}

	public static <T> void render(
			Renderable<T> renderable, T entity, ModelPart root, MatrixStack matrices,
			VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha
	) {
		renderable.render(entity, root, matrices, consumer, light, overlay, red, green, blue, alpha);
	}

	@FunctionalInterface
	public interface Renderable<T> {
		void render(T entity, ModelPart root, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha);
	}
}
