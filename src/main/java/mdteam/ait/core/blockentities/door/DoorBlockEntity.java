package mdteam.ait.core.blockentities.door;

import mdteam.ait.core.AITBlockEntityTypes;
import mdteam.ait.core.util.TardisUtil;
import mdteam.ait.core.util.data.AbsoluteBlockPos;
import mdteam.ait.tardis.Tardis;
import mdteam.ait.tardis.TardisDesktop;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class DoorBlockEntity extends AbstractDoorBlockEntity {

    public DoorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);

        // even though TardisDesktop links the door, we need to link it here as well to avoid desync
        this.setTardis(TardisUtil.findTardisByInterior(pos));
    }

    @Override
    protected void teleport(Entity entity) {
        TardisUtil.teleportOutside(this.tardis, (ServerPlayerEntity) entity);
    }

    @Override
    public void setTardis(Tardis tardis) {
        super.setTardis(tardis);
        this.linkDesktop();
    }

    @Override
    public void setDesktop(TardisDesktop desktop) {
        desktop.setInteriorDoorPos(new AbsoluteBlockPos.Directed(
                this.pos, TardisUtil.getTardisDimension(), this.getFacing())
        );
    }
}
