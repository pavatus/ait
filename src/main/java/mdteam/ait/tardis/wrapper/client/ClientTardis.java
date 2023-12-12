package mdteam.ait.tardis.wrapper.client;

import mdteam.ait.client.renderers.exteriors.ExteriorEnum;
import mdteam.ait.core.util.data.AbsoluteBlockPos;
import mdteam.ait.tardis.*;

import java.util.UUID;
import java.util.function.Function;

public class ClientTardis implements ITardis {

    private final UUID uuid;
    private TardisTravel travel;
    private TardisDesktop desktop;
    private TardisExterior exterior;
    private TardisDoor door;

    public ClientTardis(UUID uuid, AbsoluteBlockPos.Directed pos, TardisDesktopSchema schema, ExteriorEnum exteriorType) {
        this(uuid, tardis -> new TardisTravel(tardis, pos), tardis -> new TardisDesktop(tardis, schema), tardis -> new TardisExterior(tardis, exteriorType), TardisDoor::new);
    }

    protected ClientTardis(UUID uuid, Function<ITardis, TardisTravel> travel, Function<ITardis, TardisDesktop> desktop,
                           Function<ITardis, TardisExterior> exterior, Function<ITardis, TardisDoor> door) {
        this.uuid = uuid;
        this.travel = travel.apply(this);
        this.desktop = desktop.apply(this);
        this.exterior = exterior.apply(this);
        this.door = door.apply(this);

        this.init();
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public TardisExterior getExterior() {
        return this.exterior;
    }

    public void setExterior(TardisExterior exterior) {
        this.exterior = exterior;
    }

    @Override
    public TardisTravel getTravel() {
        return this.travel;
    }

    public void setTravel(TardisTravel travel) {
        this.travel = travel;
    }

    @Override
    public TardisDoor getDoor() {
        return this.door;
    }

    public void setDoor(TardisDoor door) {
        this.door = door;
    }

    @Override
    public TardisDesktop getDesktop() {
        return this.desktop;
    }

    @Override
    public void setDesktop(TardisDesktop desktop) {
        this.desktop = desktop;
    }
}
