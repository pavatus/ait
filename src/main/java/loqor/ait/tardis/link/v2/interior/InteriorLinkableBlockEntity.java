package loqor.ait.tardis.link.v2.interior;

import loqor.ait.tardis.TardisManager;
import loqor.ait.tardis.link.v2.AbstractLinkableBlockEntity;
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
        super.setWorld(world);

        if (this.ref == null) {
            this.ref = new TardisRef(
                    TardisUtil.findTardisByInterior(pos, !world.isClient()),
                    uuid -> TardisManager.with(this.world, (o, manager) ->
                            manager.demandTardis(o, uuid))
            );

            this.onLinked();

            this.sync();
            this.markDirty();
        }
    }
}
