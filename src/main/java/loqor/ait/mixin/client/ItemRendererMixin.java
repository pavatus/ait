package loqor.ait.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import loqor.ait.core.AITItems;
import loqor.ait.core.item.SonicItem;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("HEAD"))
    public void renderItem(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
        if (stack.isEmpty())
            return;

        if (!stack.isOf(AITItems.SONIC_SCREWDRIVER))
            return;

        matrices.push();

        model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
        matrices.translate(-0.5f, -0.5f, -0.5f);

        matrices.push();
        matrices.scale(1.0f, -1.0f, -1.0f);

        // render model here

        SonicItem.findSchema(stack).models();
        //VertexConsumer vertexConsumer2 = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.sonicModel.getLayer(TridentEntityModel.TEXTURE), false, stack.hasGlint());
        //this.sonicModel.render(matrices, vertexConsumer2, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);

        matrices.pop();
        matrices.pop();
    }
}
