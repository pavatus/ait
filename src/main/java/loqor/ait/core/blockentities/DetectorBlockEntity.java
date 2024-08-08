package loqor.ait.core.blockentities;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.tardis.link.v2.block.InteriorLinkableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class DetectorBlockEntity extends InteriorLinkableBlockEntity {

	public DetectorBlockEntity(BlockPos pos, BlockState state) {
		super(AITBlockEntityTypes.DETECTOR_BLOCK_ENTITY_TYPE, pos, state);
	}
}
