package loqor.ait.tardis.link;

import loqor.ait.core.AITDimensions;
import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class InteriorLinkableBlockEntity extends AbstractLinkableBlockEntity {

    public InteriorLinkableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        this.ref = new TardisRef(
                TardisUtil.findTardisByInterior(pos, !world.isClient()),
                uuid -> TardisManager.with(this.world, (o, manager) ->
                        manager.demandTardis(o, uuid))
        );

        this.sync();
        this.markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if(this.hasWorld() && world.getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD) {
            super.readNbt(nbt);
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        if(this.hasWorld() && world.getRegistryKey() == AITDimensions.TARDIS_DIM_WORLD) {
            super.writeNbt(nbt);
        }
    }
}
