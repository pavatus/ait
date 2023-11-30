package mdteam.ait.mixin.server;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import mdteam.ait.core.events.BlockEntityPreLoadEvent;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

    @Inject(method = "createFromNbt", at = @At(value = "HEAD"))
    private static void onLoadBlockEntity(BlockPos pos, BlockState state, NbtCompound nbt, CallbackInfoReturnable<BlockEntity> cir) {
        BlockEntityPreLoadEvent.LOAD.invoker().onBlockEntityPreLoad();
    }
}
