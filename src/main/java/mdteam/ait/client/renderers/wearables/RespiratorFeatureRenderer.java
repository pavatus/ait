package mdteam.ait.client.renderers.wearables;

import mdteam.ait.AITMod;
import mdteam.ait.client.models.wearables.RespiratorModel;
import mdteam.ait.core.AITItems;
import mdteam.ait.core.item.WearableArmorItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class RespiratorFeatureRenderer<T extends LivingEntity, M extends PlayerEntityModel<T>>
		extends FeatureRenderer<T, M> {

	private static final Identifier RESPIRATOR = new Identifier(AITMod.MOD_ID, "textures/entity/wearables/respirator.png");
	private static final Identifier FACELESS_RESPIRATOR = new Identifier(AITMod.MOD_ID, "textures/entity/wearables/faceless_respirator.png");
	private final RespiratorModel model;

	public RespiratorFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
		super(context);
		this.model = new RespiratorModel(RespiratorModel.getTexturedModelData().createModel());
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
		if (livingEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() != AITItems.RESPIRATOR
				|| livingEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() != AITItems.FACELESS_RESPIRATOR) return;

		if (!(livingEntity instanceof AbstractClientPlayerEntity)) return;

		matrixStack.push();

		this.model.mask.copyTransform(this.getContextModel().head);
		this.model.setAngles(livingEntity, f, g, j, k, l);

		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySmoothCutout(livingEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() == AITItems.RESPIRATOR ? RESPIRATOR : FACELESS_RESPIRATOR));
		this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1, 1, 1, 1f);

		matrixStack.pop();
	}
}