package mdteam.ait.mixin.client.rendering;/*
package mdteam.ait.mixin.client.rendering;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import mdteam.ait.AITMod;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(value = PlayerListEntry.class, priority = 1001)
public abstract class DevCapeMixin {

    @Shadow public abstract @Nullable Text getDisplayName();

    @Shadow public abstract GameProfile getProfile();

    @Inject(method = "Lnet/minecraft/client/network/PlayerListEntry;getCapeTexture()Lnet/minecraft/util/Identifier;", at=@At("HEAD"), cancellable = true)
    public void getCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        String currentPlayerName = this.getProfile().getName();
        List<String> devs = List.of("Loqor", "classic_account", "duzo", "DrTheo");
        if (devs.stream().anyMatch(currentPlayerName::matches)) {
            cir.setReturnValue(new Identifier(AITMod.MOD_ID, "textures/mdteam/dev_capes/" + currentPlayerName + "_cape.png"));
        }
    }

}
*/
