package mdteam.ait.mixin.client;

import mdteam.ait.api.ICantBreak;
import mdteam.ait.core.blocks.ExteriorBlock;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "breakBlock", at = @At(value = "HEAD"), cancellable = true)
    public void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        World world = this.client.world;
        assert world != null;
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof ICantBreak) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
