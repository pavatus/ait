package loqor.ait.core.blockentities;


import java.util.Optional;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import loqor.ait.api.link.LinkableBlockEntity;
import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.dim.TardisDimension;

public class PlaqueBlockEntity extends LinkableBlockEntity {

    public PlaqueBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.PLAQUE_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public Optional<Tardis> findTardis() {
        if (this.tardisId == null && this.hasWorld()) {
            TardisDimension.get(this.world).ifPresent(this::setTardis);
        }
        return super.findTardis();
    }
}
