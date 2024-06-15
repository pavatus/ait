package loqor.ait.tardis.link.v2.interior;

import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.link.v2.AbstractLinkableBlockEntity;
import loqor.ait.tardis.link.v2.TardisRef;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
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

        if (this.ref.isPresent())
            this.onLinked();

        this.sync();
        this.markDirty();
    }
}
