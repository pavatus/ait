package loqor.ait.mixin.server.structure;

import loqor.ait.AITMod;
import loqor.ait.api.Structure;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.link.v2.block.InteriorLinkableBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin implements Structure {

    @Unique
    private Tardis tardis;

    @Override
    public void ait$setTardis(Tardis tardis) {
        this.tardis = tardis;
    }

    @Redirect(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    public void place(BlockEntity blockEntity, NbtCompound nbt) {
        if (blockEntity instanceof InteriorLinkableBlockEntity linkable) {
            AITMod.LOGGER.debug("Linked {} to {}", linkable, tardis);

            // It's faster to remove the tardis from the nbt than make it do id -> string -> map -> string -> id
            nbt.remove("tardis");
            linkable.link(tardis);
        }

        blockEntity.readNbt(nbt);
    }
}
