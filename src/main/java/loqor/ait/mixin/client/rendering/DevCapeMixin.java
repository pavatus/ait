package loqor.ait.mixin.client.rendering;

import com.mojang.authlib.GameProfile;
import loqor.ait.AITMod;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = PlayerListEntry.class, priority = 1001)
public abstract class DevCapeMixin {

    @Shadow public abstract GameProfile getProfile();

    @Inject(method = "getCapeTexture()Lnet/minecraft/util/Identifier;", at=@At("HEAD"), cancellable = true)
    public void getCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        String currentPlayerName = this.getProfile().getName();
        List<String> devs = List.of("Loqor", "classic_account", "duzo", "DrTheo");
        if (devs.stream().anyMatch(currentPlayerName::matches)) {
            cir.setReturnValue(new Identifier(AITMod.MOD_ID, "textures/mdteam/dev_capes/" + currentPlayerName.toLowerCase() + "_cape.png"));
        }
    }

}
