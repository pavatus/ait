package loqor.ait.mixin.client.rendering;

import loqor.ait.core.util.AITModTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "applyEquipOffset", at = @At("HEAD"), cancellable = true)
    private void applyEquipProgress(MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo ci) {
        if (this.client.player == null)
            return;

        PlayerEntity player = this.client.player;
        ItemStack stack = arm == Arm.RIGHT ? player.getMainHandStack()
                : player.getOffHandStack();

        if (stack.getRegistryEntry().isIn(AITModTags.Items.NO_BOP)) {
            int i = arm == Arm.RIGHT ? 1 : -1;
            matrices.translate((float)i * 0.56F, -1.12F, -0.72F);

            ci.cancel();
        }
    }
}
