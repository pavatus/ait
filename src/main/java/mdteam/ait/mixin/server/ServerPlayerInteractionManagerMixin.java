package mdteam.ait.mixin.server;

import mdteam.ait.core.blocks.ExteriorBlock;
import net.minecraft.block.Block;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

    @Shadow protected ServerWorld world;

    @Inject(method = "tryBreakBlock", at = @At(value = "HEAD"), cancellable = true)
    public void tryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Block block = this.world.getBlockState(pos).getBlock();
        if (block instanceof ExteriorBlock) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
