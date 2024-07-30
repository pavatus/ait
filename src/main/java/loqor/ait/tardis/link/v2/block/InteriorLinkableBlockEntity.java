package loqor.ait.tardis.link.v2.block;

import loqor.ait.tardis.link.v2.TardisRef;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class InteriorLinkableBlockEntity extends AbstractLinkableBlockEntity {

    public InteriorLinkableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void setWorld(World world) {

        if (world.getRegistryKey() != TardisUtil.getTardisDimension().getRegistryKey())
            return;

        super.setWorld(world);

        if (this.ref != null)
            return;

        this.ref = TardisRef.createAs(this,
                TardisUtil.findTardisByInterior(pos, !world.isClient())
        );

        this.onLinked();

        this.sync();
        this.markDirty();
    }
}
