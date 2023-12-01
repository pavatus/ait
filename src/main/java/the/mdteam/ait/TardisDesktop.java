package the.mdteam.ait;

import mdteam.ait.AITMod;
import mdteam.ait.core.blockentities.ConsoleBlockEntity;
import mdteam.ait.core.blockentities.DoorBlockEntity;
import mdteam.ait.core.blocks.ConsoleBlock;
import mdteam.ait.core.helper.DesktopGenerator;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.AbsoluteBlockPos;
import mdteam.ait.data.Corners;
import net.minecraft.util.math.BlockPos;

public class TardisDesktop {

    @Exclude
    protected final Tardis tardis;
    private final TardisDesktopSchema schema;
    private AbsoluteBlockPos.Directed doorPos;

    private AbsoluteBlockPos.Directed consolePos;

    private final Corners corners;

    public TardisDesktop(Tardis tardis, TardisDesktopSchema schema) {
        this.tardis = tardis;
        this.schema = schema;
        this.corners = TardisUtil.findInteriorSpot();

        BlockPos doorPos = new DesktopGenerator(schema).place(
                TardisUtil.getTardisDimension(), this.getCorners().getFirst()
        );

        if (!(TardisUtil.getTardisDimension().getBlockEntity(doorPos) instanceof DoorBlockEntity door)) {
            AITMod.LOGGER.error("Failed to find the interior door!");
            return;
        }
        //if (!(TardisUtil.getTardisDimension().getBlockEntity(this.consolePos) instanceof ConsoleBlockEntity console)) {
        //AITMod.LOGGER.error("Failed to find console!");
        //return;
        //}

        // this is needed for door and console initialization. when we call #setTardis(ITardis) the desktop field is still null.
        door.setDesktop(this);
        //console.setDesktop(this);
        door.setTardis(tardis);
        //console.setTardis(tardis);
    }
    public TardisDesktop(Tardis tardis, TardisDesktopSchema schema, Corners corners, AbsoluteBlockPos.Directed door, AbsoluteBlockPos.Directed console) {
        this.tardis = tardis;
        this.schema = schema;
        this.corners = corners;
        this.doorPos = door;
        this.consolePos = console;
    }

    public TardisDesktopSchema getSchema() {
        return schema;
    }

    public AbsoluteBlockPos.Directed getInteriorDoorPos() {
        return doorPos;
    }

    public AbsoluteBlockPos.Directed getConsolePos() {
        return consolePos;
    }

    public void setInteriorDoorPos(AbsoluteBlockPos.Directed pos) {
        this.doorPos = pos;
    }

    public void setConsolePos(AbsoluteBlockPos.Directed pos) {
        this.consolePos = pos;
    }

    public Corners getCorners() {
        return corners;
    }
}
