package the.mdteam.ait;

import mdteam.ait.AITMod;
import mdteam.ait.api.tardis.IDesktop;
import mdteam.ait.api.tardis.IDesktopSchema;
import mdteam.ait.api.tardis.ILinkable;
import mdteam.ait.api.tardis.ITardis;
import mdteam.ait.core.helper.DesktopGenerator;
import mdteam.ait.core.helper.TardisUtil;
import mdteam.ait.data.Corners;
import net.minecraft.util.math.BlockPos;

public class TardisDesktop implements IDesktop {

    private final IDesktopSchema schema;
    private final BlockPos doorPos;
    private final Corners corners;

    public TardisDesktop(ITardis tardis, IDesktopSchema schema) {
        this.schema = schema;
        this.corners = TardisUtil.findInteriorSpot();

        this.doorPos = new DesktopGenerator(schema).place(
                TardisUtil.getTardisDimension(), this.getCorners().getFirst()
        );

        if (!(TardisUtil.getTardisDimension().getBlockEntity(this.doorPos) instanceof ILinkable linkable)) {
            AITMod.LOGGER.error("Failed to find the interior door!");
            return;
        }

        linkable.setTardis(tardis);
    }

    @Override
    public IDesktopSchema getSchema() {
        return schema;
    }

    @Override
    public BlockPos getInteriorDoorPos() {
        return doorPos;
    }

    @Override
    public Corners getCorners() {
        return corners;
    }
}
