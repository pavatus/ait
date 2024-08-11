package loqor.ait.mixin.client.rendering;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;

import loqor.ait.core.util.AITModTags;

// TODO not entirely sure why Theo removed this - it was necessary for a reason.
//  It removes that weird bopping for the sonic screwdriver and just makes it *that* much better of
// a tool to use.
@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "applyEquipOffset", at = @At("HEAD"), cancellable = true)
    private void applyEquipProgress(MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo ci) {
        if (this.client.player == null)
            return;

        PlayerEntity player = this.client.player;

        if (noBop(player.getMainHandStack()) || noBop(player.getOffHandStack())) {
            int i = arm == Arm.RIGHT ? 1 : -1;
            matrices.translate((float) i * 0.56F, -0.52F, -0.72F);

            ci.cancel();
        }
    }

    @Unique private static boolean noBop(ItemStack stack) {
        return stack.getRegistryEntry().isIn(AITModTags.Items.NO_BOP);
    }
}
