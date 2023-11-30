package mdteam.ait.tardis;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.util.DesktopGenerator;
import mdteam.ait.core.util.TardisUtil;
import mdteam.ait.core.util.data.AbsoluteBlockPos;
import mdteam.ait.core.util.data.Corners;
import mdteam.ait.core.util.data.Exclude;
import net.minecraft.util.math.BlockPos;

public class TardisDesktop {

    @Exclude
    protected final Tardis tardis;
    private final TardisDesktopSchema schema;

    private AbsoluteBlockPos.Directed doorPos;
    private final Corners corners;

    public TardisDesktop(Tardis tardis, TardisDesktopSchema schema) {
        this(tardis, schema, TardisUtil.findInteriorSpot());

        BlockPos doorPos = new DesktopGenerator(schema).place(
                TardisUtil.getTardisDimension(), this.getCorners().getFirst()
        );

        if (!(TardisUtil.getTardisDimension().getBlockEntity(doorPos) instanceof DoorBlockEntity door)) {
            AITMod.LOGGER.error("Failed to find the interior door!");
            return;
        }

        // this is needed for door initialization. when we call #setTardis(ITardis) the desktop field is still null.
        door.setDesktop(this);
        door.setTardis(tardis);
    }

    protected TardisDesktop(Tardis tardis, TardisDesktopSchema schema, Corners corners) {
        this.tardis = tardis;
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
