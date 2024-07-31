package loqor.ait.core.blockentities;

import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.blocks.WaypointBankBlock;
import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.link.v2.block.InteriorLinkableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class WaypointBankBlockEntity extends InteriorLinkableBlockEntity {
    public WaypointBankBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.WAYPOINT_BANK_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    public void link(Tardis tardis) {
        if (this.getCachedState().get(WaypointBankBlock.HALF) == DoubleBlockHalf.LOWER) {
            super.link(tardis);
        }
    }

    @Override
    public void link(UUID uuid) {
        if (this.getCachedState().get(WaypointBankBlock.HALF) == DoubleBlockHalf.LOWER) {
            super.link(uuid);
        }
    }
}
