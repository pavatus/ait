package mdteam.ait.core.blockentities;

import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.link.LinkableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

import static mdteam.ait.tardis.util.TardisUtil.findTardisByInterior;

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
