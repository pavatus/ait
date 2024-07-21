package loqor.ait.mixin.server.structure;

import loqor.ait.AITMod;
import loqor.ait.api.Structure;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.link.v2.block.InteriorLinkableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

import static net.minecraft.structure.StructureTemplate.StructureBlockInfo;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin implements Structure {

    @Unique
    private Tardis tardis;

    @Override
    public void ait$setTardis(Tardis tardis) {
        this.tardis = tardis;
    }

    @Inject(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void place(ServerWorldAccess world, BlockPos pos, BlockPos pivot, StructurePlacementData placementData, Random random, int flags, CallbackInfoReturnable<Boolean> cir,
                      List a, BlockBox b, List c, List d, List e, int f, int g, int h, int i, int j, int k, List l, Iterator m, StructureBlockInfo info, BlockPos o, FluidState p, BlockState q, BlockEntity blockEntity
    ) {
        if (blockEntity instanceof InteriorLinkableBlockEntity linkable) {
            AITMod.LOGGER.debug("Linked {} to {}", linkable, tardis);

            // It's faster to remove the tardis from the nbt than make it do id -> string -> map -> string -> id
            info.nbt().remove("tardis");
            linkable.link(tardis);
        }
    }
}
