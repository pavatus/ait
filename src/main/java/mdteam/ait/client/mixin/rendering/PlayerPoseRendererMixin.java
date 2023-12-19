package mdteam.ait.client.mixin.rendering;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import org.spongepowered.asm.mixin.*;

@Mixin(value = BipedEntityModel.class, priority = 1001)

public abstract class PlayerPoseRendererMixin {
    @Shadow
    @Final
    public ModelPart rightArm;
    @Shadow
    @Final
    public ModelPart leftArm;
    private final MinecraftClient client = MinecraftClient.getInstance();

    //@Inject(method = "positionRightArm", at = @At("HEAD"), cancellable = true)
    //public void repositionRightArm(LivingEntity entity, CallbackInfo ci) {
    //    if (entity instanceof PlayerEntity player) {
    //        if(player.getMainArm().equals(Arm.RIGHT)) {
    //            if (player.getMainHandStack().getItem() instanceof PipboyItem && this.client.currentScreen instanceof PipboyMainScreen) {
    //                player.handSwinging = false;
    //                this.rightArm.pitch = this.rightArm.pitch + 1;
    //                this.rightArm.yaw = this.rightArm.yaw - 1;
    //                ci.cancel();
    //            }
    //        }
    //    }
    //}

    //@Inject(method = "positionLeftArm", at = @At("HEAD"), cancellable = true)
    //public void repositionLeftArm(LivingEntity entity, CallbackInfo ci) {
    //    if (entity instanceof PlayerEntity player) {
    //        if(player.getMainArm().equals(Arm.LEFT)) {
    //            if (player.getMainHandStack().getItem() instanceof PipboyItem && this.client.currentScreen instanceof PipboyMainScreen) {
    //                player.handSwinging = false;
    //                this.leftArm.pitch = this.leftArm.pitch - 1;
    //                this.leftArm.yaw = this.leftArm.yaw + 1;
    //                ci.cancel();
    //            }
    //        }
    //    }
    //}
}
