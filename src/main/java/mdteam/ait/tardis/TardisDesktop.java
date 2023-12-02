package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.door.DoorBlockEntity;
import mdteam.ait.core.util.DesktopGenerator;
import mdteam.ait.core.util.TardisUtil;
import mdteam.ait.core.util.data.AbsoluteBlockPos;
import mdteam.ait.core.util.data.Corners;
import net.minecraft.util.math.BlockPos;

public class TardisDesktop extends AbstractTardisComponent {

    private final TardisDesktopSchema schema;
    private final Corners corners;

    private AbsoluteBlockPos.Directed doorPos;

    public TardisDesktop(ITardis tardis, TardisDesktopSchema schema) {
        this(tardis, schema, TardisUtil.findInteriorSpot());
    }

    @Override
    public void init() {
        BlockPos doorPos = new DesktopGenerator(schema).place(
                TardisUtil.getTardisDimension(), this.getCorners().getFirst()
        );

        if (!(TardisUtil.getTardisDimension().getBlockEntity(doorPos) instanceof DoorBlockEntity door)) {
            AITMod.LOGGER.error("Failed to find the interior door!");
            return;
        }

        door.setTardis(this.tardis);
    }

    protected TardisDesktop(ITardis tardis, TardisDesktopSchema schema, Corners corners) {
        super(tardis, "desktop");

        this.schema = schema;
        this.corners = corners;
    }

    public TardisDesktopSchema getSchema() {
        return schema;
    }

    public AbsoluteBlockPos.Directed getInteriorDoorPos() {
        return doorPos;
    }

    public void setInteriorDoorPos(AbsoluteBlockPos.Directed pos) {
        this.doorPos = pos;
    }

    public Corners getCorners() {
        return corners;
    }
}
