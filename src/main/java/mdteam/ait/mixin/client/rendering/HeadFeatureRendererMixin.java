package mdteam.ait.mixin.client.rendering;

import mdteam.ait.core.item.WearableArmorItem;
import mdteam.ait.core.item.WearableItem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeadFeatureRenderer.class)
public abstract class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
		extends FeatureRenderer<T, M> {
	public HeadFeatureRendererMixin(FeatureRendererContext<T, M> context) {
		super(context);
	}

	@Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", cancellable = true)
	public void ait$render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
		if ((livingEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof WearableItem wearable && wearable.hasCustomRenderer()) || (livingEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof WearableArmorItem armor && armor.hasCustomRenderer())) {
			ci.cancel();
		}
	}
}
