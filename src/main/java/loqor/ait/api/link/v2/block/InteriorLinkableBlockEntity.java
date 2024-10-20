package loqor.ait.api.link.v2.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import loqor.ait.core.AITDimensions;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.util.TardisUtil;

public abstract class InteriorLinkableBlockEntity extends AbstractLinkableBlockEntity {

    public InteriorLinkableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);

        if (this.ref != null)
            return;

        if (world.getRegistryKey() != AITDimensions.TARDIS_DIM_WORLD)
            return;

        Tardis tardis = TardisUtil.findTardisByInterior(pos, !world.isClient());

        if (tardis == null)
            return;

        this.link(tardis);
    }
}
