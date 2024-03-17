package loqor.ait.core.blockentities;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.link.LinkableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

import static loqor.ait.tardis.util.TardisUtil.findTardisByInterior;

public class DetectorBlockEntity extends LinkableBlockEntity {
	public DetectorBlockEntity(BlockPos pos, BlockState state) {
		super(AITBlockEntityTypes.DETECTOR_BLOCK_ENTITY_TYPE, pos, state);
	}

	@Override
	public Optional<Tardis> findTardis() {
		if (this.tardisId == null && this.hasWorld()) {
			assert this.getWorld() != null;
			Tardis found = findTardisByInterior(pos, !this.getWorld().isClient());
			if (found != null)
				this.setTardis(found);
		}
		return super.findTardis();
	}
}
