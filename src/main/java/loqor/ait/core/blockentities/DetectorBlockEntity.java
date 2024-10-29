package loqor.ait.core.blockentities;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import loqor.ait.api.link.v2.block.InteriorLinkableBlockEntity;
import loqor.ait.core.AITBlockEntityTypes;

public class DetectorBlockEntity extends InteriorLinkableBlockEntity {

    public DetectorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DETECTOR_BLOCK_ENTITY_TYPE, pos, state);
    }
}
