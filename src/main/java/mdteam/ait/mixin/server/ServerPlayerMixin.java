package mdteam.ait.mixin.server;


import mdteam.ait.core.AITDimensions;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.util.TardisUtil;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerMixin {


    @Shadow private int joinInvulnerabilityTicks;

    @Inject(method="tick", at = @At("TAIL"))
    private void AIT_tick(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;

        // if player is in tardis and y is less than -100 save them
        if (player.getY() <= -100 && player.getServerWorld().getRegistryKey().equals(AITDimensions.TARDIS_DIM_WORLD)) {
            Tardis found = TardisUtil.findTardisByInterior(player.getBlockPos(), true);

            if (found == null) return;
            TardisUtil.teleportInside(found, player);
            this.joinInvulnerabilityTicks = 60;
        }
    }
}
