package the.mdteam.ait;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.data.AbsoluteBlockPos;
import the.mdteam.ait.wrapper.ServerTardis;
import the.mdteam.ait.wrapper.ServerTardisTravel;

import java.util.UUID;
import java.util.function.Function;

public class Tardis {

    private final TardisTravel travel;

    private final UUID uuid;
    private TardisDesktop desktop;
    private ExteriorEnum exteriorType;

    public Tardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorEnum exteriorType) {
        this(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), exteriorType);
    }

    protected Tardis(UUID uuid, Function<Tardis, TardisTravel> travel, Function<Tardis, TardisDesktop> desktop, ExteriorEnum exteriorType) {
        this.uuid = uuid;
        this.travel = travel.apply(this);

        this.desktop = desktop.apply(this);
        this.exteriorType = exteriorType;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setDesktop(TardisDesktop desktop) {
        this.desktop = desktop;
    }

    public TardisDesktop getDesktop() {
        return desktop;
    }

    public void setExteriorType(ExteriorEnum exteriorType) {
        this.exteriorType = exteriorType;
    }

    public ExteriorEnum getExteriorType() {
        return exteriorType;
    }

    public TardisTravel getTravel() {
        return travel;
    }

    // @TODO shitty hotfix for incomplete code
    public ServerTardis onServer() {
        if (this.getTravel().getPosition().getWorld().isClient())
            return null;

        return new ServerTardis(this);
    }
}
