package loqor.ait.core.blockentities;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.tardis.link.v2.block.InteriorLinkableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class WaypointBankBlockEntity extends InteriorLinkableBlockEntity {
    public WaypointBankBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.WAYPOINT_BANK_BLOCK_ENTITY_TYPE, pos, state);
    }

    // TODO add inventory for waypoint bank: 16 slots in total. cba to do it tonight, gonna do some more stuff with uhhhh the moods and loyalty.
}
