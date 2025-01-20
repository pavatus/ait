package dev.pavatus.planet.mixin.client;

import dev.pavatus.planet.core.item.SpacesuitItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;

@Mixin(PlayerEntityModel.class)
public class PlayerEntityModelMixin<T extends LivingEntity> {

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void ait$setAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        PlayerEntityModel model = (PlayerEntityModel) (Object) this;

        boolean noHelmet = !(livingEntity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof SpacesuitItem);
        boolean noChestplate = !(livingEntity.getEquippedStack(EquipmentSlot.CHEST).getItem() instanceof SpacesuitItem);
        boolean noPants = !(livingEntity.getEquippedStack(EquipmentSlot.LEGS).getItem() instanceof SpacesuitItem);

        // i hate this just as much as everyone else seeing this block of code.
        model.head.visible = noHelmet;
        model.hat.visible = noHelmet;
        model.body.visible = noChestplate;
        model.jacket.visible = noChestplate;
        model.leftArm.visible = noChestplate;
        model.leftSleeve.visible = noChestplate;
        model.rightArm.visible = noChestplate;
        model.rightSleeve.visible = noChestplate;
        model.leftLeg.visible = noPants;
        model.rightLeg.visible = noPants;
        model.leftPants.visible = noPants;
        model.rightPants.visible = noPants;
    }

}
