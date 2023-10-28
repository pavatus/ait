package the.mdteam.ait;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.IDesktop;
import mdteam.ait.api.tardis.IDesktopSchema;
import mdteam.ait.api.tardis.ITardis;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.helper.DesktopGenerator;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.data.Corners;
import net.minecraft.util.math.BlockPos;

public class TardisDesktop implements IDesktop {

    private final IDesktopSchema schema;
    private final AbsoluteBlockPos.Directed doorPos;
    private final Corners corners;

    public TardisDesktop(ITardis tardis, IDesktopSchema schema) {
        this.schema = schema;
        this.corners = TardisUtil.findInteriorSpot();

        BlockPos doorPos = new DesktopGenerator(schema).place(
                TardisUtil.getTardisDimension(), this.getCorners().getFirst()
        );

        if (!(TardisUtil.getTardisDimension().getBlockEntity(doorPos) instanceof DoorBlockEntity door)) {
            AITMod.LOGGER.error("Failed to find the interior door!");
            this.doorPos = null;
            return;
        }

        this.doorPos = new AbsoluteBlockPos.Directed(doorPos, TardisUtil.getTardisDimension(), door.getFacing());
        door.setTardis(tardis);
    }

    @Override
    public IDesktopSchema getSchema() {
        return schema;
    }

    @Override
    public AbsoluteBlockPos.Directed getInteriorDoorPos() {
        return doorPos;
    }

    @Override
    public Corners getCorners() {
        return corners;
    }
}
