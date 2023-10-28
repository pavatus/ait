package the.mdteam.ait;

import mdteam.ait.api.tardis.IDesktop;
import mdteam.ait.api.tardis.IDesktopSchema;
import mdteam.ait.api.tardis.ITardis;
import mdteam.ait.api.tardis.ITravel;
import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.data.AbsoluteBlockPos;

import java.util.UUID;

public class Tardis implements ITardis {

    private final ITravel travel = new TardisTravel();

    private final UUID uuid;
    private IDesktop desktop;
    private ExteriorEnum exteriorType;

    public Tardis(UUID uuid, AbsoluteBlockPos.Directed pos, IDesktopSchema schema, ExteriorEnum exteriorType) {
        this.uuid = uuid;
        this.desktop = new TardisDesktop(this, schema);
        this.exteriorType = exteriorType;

        this.travel.setPosition(pos);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void setDesktop(IDesktop desktop) {
        this.desktop = desktop;
    }

    @Override
    public IDesktop getDesktop() {
        return desktop;
    }

    @Override
    public void setExteriorType(ExteriorEnum exteriorType) {
        this.exteriorType = exteriorType;
    }

    @Override
    public ExteriorEnum getExteriorType() {
        return exteriorType;
    }

    @Override
    public ITravel getTravel() {
        return travel;
    }
}
